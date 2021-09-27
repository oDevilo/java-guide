package com.devil.guide.arithmetic.interview;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * 题目：
 * 有一个文本文件，2亿行，每行长度255个字符以内，长度不定，均为可见字符。
 * 在16Core64G内存的机器上，对文件进行读入，排序（字典），输出成一个文件。
 * 要求完成整个工作的时间越短越好
 * <p>
 * 思路：
 * 假设都是 ascii 码
 * 整个文件大小最大的情况为 200000000 * 255 = 51000000000b > 48637GB 超过内存大小，考虑多线程，以及多文件处理
 * <p>
 *     2147483647
 * 按 ascii 码以及字符长度区间分桶 桶的计算大小为 256 * 256 = 65536
 * 上面按 65536 分桶不合理 最坏的情况，假如刚好2亿行都分到一个文件区间，那这个桶就有所有数据，还是无法在内存中排序
 * <p>
 * 目前只能考虑
 * 1. 使用buffer，当buffer满了的时候，将buffer内数据排序写入小文件，直到大文件读取完
 * 2. 从每个小文件获取第一个元素，排序后拿取最小值，拿取最小值的文件读取下一行
 * 3. 重复2，直到所有小文件读取完
 * 4. 考虑下16核多线程处理方式
 *
 * @author Devil
 * @since 2021/9/16
 */
public class BigFileSort {
    /** 核数 */
    private int coreSize = 16;
    /** 假设一个文件按 2g 2147483648 / 256 = 8388608 */
    private int fileLines = 8000000;
    /** 文件个数 200000000 / 8000000 = 25 */
    private int fileCount = 25;

    /** 排序和小文件写入的线程池 */
    private ThreadPoolExecutor corePool;
    /** 文件名和文件的映射 */
    private Map<String, NioFile> fileMap;
    /** 根据字符串排序的链表 */
    private BlockingQueue<List<String>> writeQueue;

    BigFileSort() {
        this.corePool = new ThreadPoolExecutor(coreSize, coreSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(coreSize));
        this.fileMap = new HashMap<>();
        this.writeQueue = new LinkedBlockingQueue<>(coreSize);
    }

    public void sort(String inFileName, String outFileName) {
        try {
            CountDownLatch latch = new CountDownLatch(fileCount);
            // 1. 逐行读取文件内容
            readInFile(inFileName, latch);

            // 2. 在所有小文件写入后 初始化链表用于排序
            latch.await();
            LinkedList<FileValueNode> link = initLink();

            // 3. 启动线程进行消费写入最终文件的操作
            new Thread(new WriteTask(new NioFile(outFileName), writeQueue)).start();

            // 4. 取链表第一个值，然后从对应文件再读取一个值，放到对应位置 直到数全部读取完
            writeToOut(link);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void readInFile(String fileName, CountDownLatch latch) throws IOException {
        NioFile nioFile = new NioFile(fileName);
        List<String> lines = new ArrayList<>(); // 每个小文件的数据
        int fileIdx = 1;
        String line = nioFile.nextLine();
        while (line != null) {
            lines.add(line);
            if (lines.size() >= fileLines) {
                NioFile sFile = new NioFile(fileIdx + ".txt");
                corePool.submit(new SingleFileSortTask(sFile, lines, latch));
                fileMap.put(fileName, sFile);
                lines = new ArrayList<>();
                fileIdx++;
            }
            line = nioFile.nextLine();
        }
        if (lines.size() > 0) {
            NioFile sFile = new NioFile(fileIdx + ".txt");
            corePool.submit(new SingleFileSortTask(sFile, lines, latch));
            fileMap.put(fileName, sFile);
        }
    }

    private LinkedList<FileValueNode> initLink() throws IOException {
        LinkedList<FileValueNode> link = new LinkedList<>();
        for (Map.Entry<String, NioFile> entry : this.fileMap.entrySet()) {
            NioFile nioFile = entry.getValue();
            String fline = nioFile.nextLine();
            addToPos(new FileValueNode(entry.getKey(), fline), link);
        }
        return link;
    }

    private void writeToOut(LinkedList<FileValueNode> link) throws IOException {
        List<String> lines = new ArrayList<>();
        while (link.size() > 0) {
            // 将头部最小的元素添加到列表
            FileValueNode first = link.pollFirst();
            lines.add(first.getLine());
            // 超过一定量就往文件写入
            if (lines.size() >= fileLines) {
                writeQueue.add(lines);
                lines = new ArrayList<>();
            }
            // 从对应文件补充一个数 如果这个文件读取完了就跳过
            NioFile nioFile = fileMap.get(first.getFileName());
            String line = nioFile.nextLine();
            if (line != null && line.length() > 0) {
                addToPos(new FileValueNode(first.getFileName(), line), link);
            }
        }
        if (lines.size() > 0 ){
            writeQueue.add(lines);
        }
    }

    /**
     * 在对应位置放入节点
     */
    private void addToPos(FileValueNode node, LinkedList<FileValueNode> link) {
        if (link.size() <= 0) {
            link.add(node);
        } else {
            int pos = link.size();
            for (int i = 0; i < link.size(); i++) {
                if (getStringScore(link.get(i).getLine()) > getStringScore(node.getLine())) {
                    pos = i;
                }
            }
            link.add(pos, node);
        }
    }

    /**
     * 小文件排序任务
     */
    static class SingleFileSortTask implements Runnable {

        private final NioFile nioFile;

        private final List<String> lines;

        private final CountDownLatch latch;

        SingleFileSortTask(NioFile nioFile, List<String> lines, CountDownLatch latch) {
            this.nioFile = nioFile;
            this.lines = lines;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                // 排序
                lines.sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return getStringScore(o1) - getStringScore(o2);
                    }
                });
                // 往文件写
                nioFile.write(lines);
                // 计数器减少
                latch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 往最后文件写数据的任务
     */
    static class WriteTask implements Runnable {

        NioFile nioFile;

        BlockingQueue<List<String>> queue;

        WriteTask(NioFile nioFile, BlockingQueue<List<String>> queue) {
            this.nioFile = nioFile;
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    List<String> lines = queue.take();
                    nioFile.write(lines);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 文件读写工具
     */
    static class NioFile {

        private RandomAccessFile randomAccessFile;

        private FileChannel channel;

        private ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024); // 每次读取文件的缓冲

        private ByteBuffer lineBuffer = ByteBuffer.allocate(256); // 存每一行的数据

        private int bytesRead = -100;

        NioFile(String fileName) throws IOException {
            this.randomAccessFile = new RandomAccessFile(fileName, "rw");
            this.channel = randomAccessFile.getChannel();

        }

        public String nextLine() throws IOException {
            if (bytesRead == -100) {
                bytesRead = channel.read(buffer);
                if (bytesRead == -1) {
                    String line = StandardCharsets.UTF_8.decode(lineBuffer).toString();
                    lineBuffer.clear(); // 清理下次直接返回 null
                    return line.trim().equals("") ? null : line; // 文件已经读取完
                }
                buffer.flip(); // 切换到读
            }
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                if (b == 10 || b == 13) { // 换行
                    lineBuffer.flip();
                    String line = StandardCharsets.UTF_8.decode(lineBuffer).toString();
                    lineBuffer.clear();
                    return line; // 读取到一行数据 直接返回
                } else {
                    lineBuffer.put(b);
                }
            }
            buffer.clear();
            bytesRead = -100;
            return nextLine(); // 如果没有在while返回那么说明缓冲区内数据不满一行，需要从下一数据中获取
        }

        public void write(List<String> lines) throws IOException {
            buffer.clear();
            // 4000行一次写入
            int size = 0;
            for (String line : lines) {
                buffer.put(line.getBytes());
                size++;
                while (size >= 4000) {
                    channel.write(buffer);
                    buffer.clear();
                    size = 0;
                }
            }
            if (size > 0) {
                channel.write(buffer);
                buffer.clear();
            }
        }

    }

    /**
     * 链表中的节点数据 维护当前行和对应的文件名称
     */
    static class FileValueNode {
        private final String fileName;

        private final String line;

        FileValueNode(String fileName, String line) {
            this.fileName = fileName;
            this.line = line;
        }

        public String getFileName() {
            return fileName;
        }

        public String getLine() {
            return line;
        }
    }

    /**
     * 根据String获取其分值大小
     */
    private static int getStringScore(String str) {
        char[] chars = str.toCharArray();
        int result = 0;
        for (char c : chars) {
            result = result * 10 + c;
        }
        return result;
    }

}
