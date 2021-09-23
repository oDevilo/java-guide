package com.devil.guide.arithmetic;

import org.limbo.utils.verifies.Verifies;

import java.util.*;

/**
 * 为最不经常使用（LFU）缓存算法设计并实现数据结构。它应该支持以下操作：get 和 put。
 * get(key) - 如果键存在于缓存中，则获取键的值（总是正数），否则返回 -999。
 * put(key, value) - 如果键已存在，则变更其值；如果键不存在，请插入键值对。
 * 当缓存达到其容量时，则应该在插入新项之前，使最不经常使用的项无效。
 * 在此问题中，两个键具有相同使用频率时，则去除值较小的键，值相同则去除最久未使用的键。
 * 使用次数 = 对其调用get的次数*2 + 对其调用put函数的次数；使用次数会在对应项被移除后置为0
 *
 * @author Devil
 * @since 2021/9/16
 */
public class LFU {

    /** key value 映射 */
    private final Map<Integer, Integer> kv;

    /** key 的使用频率 */
    private final Map<Integer, Integer> kFrequently;

    /** 使用频率对应的 key 链表 刚使用的会到头部 */
    private final Map<Integer, Deque<Integer>> frequentlyKeys;

    /** 容量 */
    private final int capacity;

    /** 最小频率 */
    private int minFrequently;

    private static int EMPTY = -999;

    private int putScore;

    private int getScore;

    public LFU(int capacity, int putScore, int getScore) {
        this.kv = new HashMap<>();
        this.kFrequently = new HashMap<>();
        this.frequentlyKeys = new HashMap<>();
        this.capacity = capacity;
        this.putScore = putScore;
        this.getScore = getScore;
    }

    public int get(int key) {
        if (!kv.containsKey(key)) {
            return EMPTY;
        }
        // 增加使用频率
        incrFrequently(key, getScore);
        return kv.get(key);
    }

    /**
     * 增加频率
     * @param key key
     * @param score 分数
     */
    private void incrFrequently(int key, int score) {
        Integer oldFre = kFrequently.get(key);
        Integer newFre = oldFre + score;
        kFrequently.put(key, newFre);
        // 往新频率里面加到头部
        frequentlyKeys.putIfAbsent(newFre, new LinkedList<>());
        frequentlyKeys.get(newFre).addFirst(key);
        // 从旧的频率里面移除
        frequentlyKeys.get(oldFre).remove(key);
        if (frequentlyKeys.get(oldFre).size() == 0) {
            frequentlyKeys.remove(oldFre);
            // 如果删除的是最小的频率的，需要找到下一个
            if (oldFre == minFrequently) {
                this.minFrequently = nextMinFrequently();
            }
        }
    }

    private int nextMinFrequently() {
        int min = 0;
        Set<Integer> frequently = frequentlyKeys.keySet();
        for (Integer integer : frequently) {
            if (min == 0) {
                min = integer;
            } else if (min > integer) {
                min = integer;
            }
        }
        return min;
    }

    public void put(int key, int value) {
        if (capacity <= 0)
            return;

        // 如果已存在 直接替换 增加频率
        if (kv.containsKey(key)) {
            kv.put(key, value);
            incrFrequently(key, putScore);
            return;
        }

        // 要超过容量了 移除掉最不经常使用的
        if (kv.size() >= capacity) {
            removeLeastFrequentlyKey();
        }

        // 插入一个新的 key 那么 minFre 为 1
        this.minFrequently = 1;

        kv.put(key, value);
        // 没有key的使用频率 表示第一次
        kFrequently.put(key, putScore);
        frequentlyKeys.put(putScore, new LinkedList<>());
        frequentlyKeys.get(putScore).addFirst(key);
    }

    /**
     * 删除最不经常使用的key
     */
    private void removeLeastFrequentlyKey() {
        // 移除尾部的
        Integer key = frequentlyKeys.get(minFrequently).removeLast();
        if (frequentlyKeys.get(minFrequently).size() == 0) {
            frequentlyKeys.remove(minFrequently);
        }
        kv.remove(key);
        kFrequently.remove(key);
    }

    public static void main(String[] args) {
        LFU lfu = new LFU(2, 1, 2);
        lfu.put(1, 1);
        lfu.put(1, 2);
        lfu.put(1, 3); // 1 - 3
        lfu.put(2, 3);
        lfu.put(2, 4); // 2 - 2
        lfu.put(3, 3);
        // 2 被移除 所以3可以获取到
        Verifies.verify(lfu.get(3) == 3); // 3 - 3
        lfu.put(4, 4); // 4 - 1
        // 应该移除掉较早的 1
        Verifies.verify(lfu.get(1) == EMPTY);
    }

}
