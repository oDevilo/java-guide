//package com.devil.guide.arithmetic;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * 题目：
// * 有一个文本文件，2亿行，每行长度255个字符以内，长度不定，均为可见字符。
// * 在16Core64G内存的机器上，对文件进行读入，排序（字典），输出成一个文件。
// * 要求完成整个工作的时间越短越好
//
// * 思路：
// * 假设都是 ascii 码
// * 整个文件大小最大的情况为 200000000 * 255 = 51000000000b > 48637GB 超过内存大小，考虑多线程，以及多文件处理
// *
// * 按 ascii 码以及字符长度区间分桶 桶的计算大小为 256 * 256 = 65536
// * 上面按 65536 分桶不合理 最坏的情况，假如刚好2亿行都分到一个文件区间，那这个桶就有所有数据，还是无法在内存中排序
// *
// * 目前只能考虑
// * 1. 使用buffer，当buffer满了的时候，将buffer内数据排序写入小文件，直到大文件读取完
// * 2. 从每个小文件获取第一个元素，排序后拿取最小值，拿取最小值的文件读取下一行
// * 3. 重复2，直到所有小文件读取完
// * 4. 考虑下16核多线程处理方式
// *
// * @author Devil
// * @since 2021/9/16
// */
//public class BigFileSort {
//    /** 桶数量 */
//    int bucket;
//    /** 每次处理的数量 */
//    int splitSize;
//
//    ThreadPoolExecutor corePool;
//    ThreadPoolExecutor ioPool;
//
//    BigFileSort(int coreSize, int bucket, int splitSize) {
//        this.bucket = bucket;
//        this.splitSize = splitSize;
//        this.corePool = new ThreadPoolExecutor(coreSize, coreSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(16));
//        this.ioPool = new ThreadPoolExecutor(bucket, bucket, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(16));
//    }
//
//    public void sort(String inFileName, String outFileName) {
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(inFileName));
//            List<String> lines = new ArrayList<>();
//            String line = reader.readLine();
//            while (line != null) {
//                lines.add(line);
//                if (lines.size() >= splitSize) {
//                    // 往任务队列里面丢
//                    executor.submit(new)
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void submit(List<String> lines) {
//        executor.submit(new Runnable() {
//            @Override
//            public void run() {
//                // 把每行写入指定的文件
//
//            }
//        });
//    }
//
//    private int getStringScore(String str) {
//        char[] chars = str.toCharArray();
//        int result = 0;
//        for (char c : chars) {
//            result = result * 10 + c;
//        }
//        return result;
//    }
//
//
//}
