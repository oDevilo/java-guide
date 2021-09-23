package com.devil.guide.arithmetic;

import java.io.*;
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
    /**
     * 文件个数
     */
    private final int fileCount;
    /**
     * 小文件行数
     */
    private final int fileLines;

    private final ThreadPoolExecutor corePool;
    /**
     * 文件名和文件的映射
     */
    private final Map<String, SplitFileReader> fileMap;

    private final BlockingQueue<List<String>> writeQueue;

    /**
     * @param coreSize  核数 单位个
     * @param memory    内存大小 单位B
     * @param totalCount 总行数
     */
    BigFileSort(int coreSize, int memory, int totalCount) {
        // 内存 / 核数 则为每个核能操作的内存大小  再除以每行的最大大小 则为每个小文件的行数
        this.fileLines = memory/ coreSize / 256;
        this.fileCount = (int) Math.ceil((double) totalCount / fileLines);
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
            new Thread(new WriteTask(outFileName, writeQueue)).start();

            // 4. 取链表第一个值，然后从对应文件再读取一个值，放到对应位置 直到数全部读取完
            writeToOut(link);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void readInFile(String fileName, CountDownLatch latch) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        List<String> lines = new ArrayList<>();
        String line = reader.readLine();
        int fileIdx = 1;
        while (line != null) {
            lines.add(line);
            line = reader.readLine();
            // 任务达到一定值，往任务队列里面丢
            if (lines.size() >= fileLines) {
                String smallFileName = fileIdx + ".txt";
                corePool.submit(new SingleFileSortTask(smallFileName, lines, latch));
                fileMap.put(fileName, new SplitFileReader(smallFileName));
                lines = new ArrayList<>();
                fileIdx++;
            }
        }
        if (lines.size() > 0) {
            String smallFileName = fileIdx + ".txt";
            corePool.submit(new SingleFileSortTask(smallFileName, lines, latch));
            fileMap.put(fileName, new SplitFileReader(smallFileName));
        }
    }

    private LinkedList<FileValueNode> initLink() {
        LinkedList<FileValueNode> link = new LinkedList<>();
        for (Map.Entry<String, SplitFileReader> entry : this.fileMap.entrySet()) {
            SplitFileReader splitFileReader = entry.getValue();
            splitFileReader.init();
            String fline = splitFileReader.nextLine();
            // 将值放到一个合适位置
            if (link.size() <= 0) {
                link.add(new FileValueNode(entry.getKey(), fline));
            } else {
                addToPos(entry.getKey(), fline, link);
            }
        }
        return link;
    }

    private void writeToOut(LinkedList<FileValueNode> link) {
        List<String> lines = new ArrayList<>();
        while (link.size() > 0) {
            FileValueNode first = link.pollFirst();
            lines.add(first.getLine());
            if (lines.size() >= fileLines) {
                // 往文件写入
                writeQueue.add(lines);
                lines = new ArrayList<>();
            }
            // 从对应文件补充一个数，直到文件读取完
            SplitFileReader splitFileReader = fileMap.get(first.getFileName());
            String line = splitFileReader.nextLine();
            if (line != null && line.length() > 0) {
                addToPos(first.getFileName(), line, link);
            }
        }
        if (lines.size() > 0 ){
            writeQueue.add(lines);
        }
    }

    private void addToPos(String fileName, String line, LinkedList<FileValueNode> link) {
        int pos = link.size();
        for (int i = 0; i < link.size(); i++) {
            if (getStringScore(link.get(i).getLine()) > getStringScore(line)) {
                pos = i;
            }
        }
        link.add(pos, new FileValueNode(fileName, line));
    }

    /**
     * 小文件排序任务
     */
    static class SingleFileSortTask implements Runnable {

        private final String fileName;

        private final List<String> lines;

        private final CountDownLatch latch;

        SingleFileSortTask(String fileName, List<String> lines, CountDownLatch latch) {
            this.fileName = fileName;
            this.lines = lines;
            this.latch = latch;
        }

        @Override
        public void run() {
            // 排序
            lines.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return getStringScore(o1) - getStringScore(o2);
                }
            });
            // 往文件写
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (String line : lines) {
                    writer.write(line);
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }

    /**
     * 往最后文件写数据的任务
     */
    static class WriteTask implements Runnable {

        BufferedWriter writer;

        BlockingQueue<List<String>> queue;

        WriteTask(String fileName, BlockingQueue<List<String>> queue) {
            try {
                this.writer = new BufferedWriter(new FileWriter(fileName));
                this.queue = queue;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    List<String> take = queue.take();
                    for (String line : take) {
                        writer.write(line);
                    }
                    writer.flush();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 文件读取的工具
     */
    static class SplitFileReader {

        private final String fileName;

        private BufferedReader reader;

        SplitFileReader(String fileName) {
            this.fileName = fileName;
        }

        public void init() {
            try {
                this.reader = new BufferedReader(new FileReader(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public String nextLine() {
            try {
                return reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
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
