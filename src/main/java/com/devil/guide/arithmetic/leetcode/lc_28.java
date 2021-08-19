package com.devil.guide.arithmetic.leetcode;

/**
 * 设计 strStr
 * 求 haystack 中 needle 出现的索引
 *
 * @author Devil
 * @date 2021/7/1 6:53 下午
 */
public class lc_28 {

    /**
     * 参考 https://www.cnblogs.com/dusf/p/kmp.html
     */
    public int strStr(String haystack, String needle) {
        if (needle.equals("")) {
            return 0;
        }
        int[] next = getNext(needle);
        char[] t = haystack.toCharArray();
        char[] p = needle.toCharArray();
        int i = 0; // i 表示当前在主字符串的匹配位置
        int j = 0; // j 表示当前在副字符串的匹配位置
        while (i<t.length && j<p.length) {
            if (t[i] == p[j]) { // 当相等 继续向下匹配
                i++;
                j++;
            } else if (j == 0) { // 由于上个if 可以知道 t[i] != j[0] 那么 移动 i
                i++;
            } else { // j 到指定位置 k
                j = next[j];
            }
        }
        if (j == p.length) { // 匹配到的情况
            return i - p.length;
        }
        return -1;
    }

    /**
     * next 数组的意义：
     * 当 p[j] != t[i] 的时候 暴力做法是将 j 归零，重新比较 这种情况下时间复杂度是 O(n^m)
     * 如果 m 很大，时间成本会很高
     * 对于有些 p
     * 如：   A B A B A
     * j     0 1 2 3 4
     * 在 j = 4 的时候 如果出现 p[4] != t[4] 我们可以看到 由于 p[0 ~ 1] = p[2 ~ 3] 那么我们只需要重新比较 t[4] 和 p [2] 是否相等即可
     * 因为如果程序能进行到 p[4] 和 t[4] 比较这一步
     * 可知 t[0] = p[0] t[1] = p[1] t[2] = p[2] t[3] = p[3]
     * 由  p[0]= p[2] p[1]= p[3]
     * 得 t[2] = p[0] t[3] = p[1]
     * 而我们知道在 j 的时候 只需移动到 k 就少了很多比较次数
     *
     *
     * 对于 next 数组的定义：
     * next[j] = k 表示 当当前位置与 主字符串 t 的当前位置 i 的字符 p[j] != t[i] 时候 需要将 p 的指针 j 移动到哪个位置 k
     * 之后需要比较的是 p[next[j]] 和 t[i]
     * 因为 p[j] != t[i] 时
     * 有 p[0 ~ (j - 1)] = t[(i - j) ~ (i - 1)] (p[j] 前 j - 1 个数 和 t[i] 前 j - 1 个数 都是一一对应的)
     * 由 p[0 ~ (k - 1)] = p[(j - k) ~ (j - 1)] (由k的定义可以知道 p[k] 前 k - 1 个数 和 p[j] 前 k - 1 个数相等)
     * 得 p[0 ~ (k - 1)] = t[(i - k) ~ (i - 1)] (由上面可以知道 p[k] 前 k - 1 个数 和 t[i] 前 k - 1 个数相等)
     * 所以在j的时候，只需将 p 的指针移动到 k 即可重新进行比较
     *
     *
     * 如何计算next数组：
     * 如：   A B A B A C A
     * j     0 1 2 3 4 5 6
     *
     * 如果我们要计算  j = 4 也就是 next[4] 的时候
     * 我们的思路肯定是有两个指针 l 和 r
     * l 从 0 开始 往右移动 r 从 3 开始向左移动
     * 当满足 p[0 ~ l] = p[r ~ (j - 1)] 的时候，两个指针继续移动，直到不满足这个情况
     * 那么此时 p[0 ~ (l - 1)] = p[(r - 1) ~ (j - 1)] 是最大满足的情况
     *
     * 其实上面的思路也就是 比较 去除 p[j] 后对应的 ABAB 前缀集 和 后缀集
     * 前缀集是去掉最后一个字符 ABA，从左到右的集合
     * A, AB, ABA
     * 后缀集是去掉最后一个字符 BAB，从左到右的集合
     * B, AB, BAB
     * 其中他们最大重复的子串是 AB 那么 k = 2 则 next[4] = 2 表示下次比较的应该是 p[2] = A
     *
     * 从几个例子找找规律
     * p[0]
     * next[0] = 0
     *
     * p[1]
     * t [A, C, A]
     * p = [A, A] next[1] = 0
     *
     * 对于 p[2]
     * 1. t = [A, C, A, A] P = [A, C, B] next[2] = 0 下个比较 t[2] 和 p[0]
     * 2. t = [A, A, A, A] P = [A, A, B] next[2] = 1 下个比较 t[2] 和 p[1]
     *    因为 p[0] = p[1] 所以 t[0] = t[1] 可以知道 p[0] = t[1] 而由于 t[2] != p[2] 那么需要比较的就是 t[2] 和 p[1] 如果相等则可以继续向下比较了
     *
     * 由定义可以知道
     * p[0 ~ (k - 1)] = p[(j - k) ~ (j - 1)]
     *
     * 如果 p[k] = p[j]
     * 如
     * t A B C A B A C D
     * P A B C A B C
     *   0 1 2 3 4 5 6 7
     * 当 j = 5 的时候 由 前缀集 和 后缀集 可以得到 k = 2 此时 p[k] = p[j]
     * 则 p[0 ~ k] = p[(j - k - 1) ~ (j - 1)] 那么 next[j + 1] = k + 1
     *
     * 如果 p[k] != p[j]
     * 如
     * t A B A C D A B A C D
     * p A B A C D A B A B C
     *   0 1 2 3 4 5 6 7 8 9
     * 对于 j = 8 可以知道 k = 3 （这是由上面的情况计算而来 也就是 next[8] 是由 j = 7 的时候计算而来）
     * p[0 ~ 3] = ABAC
     * P[5 ~ 8] = ABAB
     * 由于 p[k] != p[j] 肯定是不满足了 而应该 从 0 ~ k 中继续找一个合适的 n 位置 也就是要找一个满足上面 p[8] = p[n] 的情况的
     * 所以 p[0 ~ n] = p[(8 -n) ~ 8] 这个时候 我们应该移动指针 k 到 next[next[8]] = next[3] = 1 的位置
     * 因为对于 next[3] = 1 来说 已经保证了 p[0 ~ 1] = p[1 ~ 2] 也就是我们重复利用了之前的结果
     *
     * 后面无非又是 p[8] = p[1] 和 p[8] != p[1] 的情况
     * 如果一直是 ！的情况 我们需要有一个可以终止 这个情况应该是 k = 0 的时候 也就是需要从头比较了
     *
     */
    public int[] getNext(String pattern) {
        char[] p = pattern.toCharArray();
        int[] next = new int[p.length];
        next[0] = 0;
        int j = 1; // next[0] 已经计算了 所以从 1 开始计算
        int k = 0;
        while (j < p.length - 1) {
            if (p[j] == p[k]) {
                ++j;
                ++k;
                next[j] = k;
            } else {
                if (k == 0) {
                    j++;
                } else {
                    k = next[k];
                }
            }
        }
        return next;
    }

    public static void main(String[] args) {
        lc_28 a = new lc_28();
        System.out.println(a.strStr("hello", "ll")); // 2
        System.out.println(a.strStr("aaaaa", "bba")); // -1
        System.out.println(a.strStr("", "")); // 0
        System.out.println(a.strStr("aaaa", "aaaaa")); // -1
        System.out.println(a.strStr("mississippi", "issip")); // 4
    }
}
