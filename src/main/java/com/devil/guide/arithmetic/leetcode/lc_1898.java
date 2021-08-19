package com.devil.guide.arithmetic.leetcode;

/**
 * 给你两个字符串 s 和 p ，其中 p 是 s 的一个 子序列 。
 * 同时，给你一个元素 互不相同 且下标 从 0 开始 计数的整数数组removable ，该数组是 s 中下标的一个子集（s 的下标也 从 0 开始 计数）。
 * <p>
 * 请你找出一个整数 k（0 <= k <= removable.length），选出removable 中的 前 k 个下标，然后从 s 中移除这些下标对应的 k 个字符。
 * 整数 k 需满足：在执行完上述步骤后， p 仍然是 s 的一个 子序列 。更正式的解释是，对于每个 0 <= i < k ，先标记出位于 s[removable[i]] 的字符，
 * 接着移除所有标记过的字符，然后检查 p 是否仍然是 s 的一个子序列。
 * <p>
 * 返回你可以找出的 最大 k ，满足在移除字符后 p 仍然是 s 的一个子序列。
 * <p>
 * 字符串的一个 子序列 是一个由原字符串生成的新字符串，生成过程中可能会移除原字符串中的一些字符（也可能不移除）但不改变剩余字符之间的相对顺序。
 * <p>
 * <p>
 * 输入：s = "abcacb", p = "ab", removable = [3,1,0]
 * 输出：2
 * 解释：在移除下标 3 和 1 对应的字符后，"abcacb" 变成 "accb" 。
 * "ab" 是 "accb" 的一个子序列。
 * 如果移除下标 3、1 和 0 对应的字符后，"abcacb" 变成 "ccb" ，那么 "ab" 就不再是 s 的一个子序列。
 * 因此，最大的 k 是 2 。
 * <p>
 * 输入：s = "abcbddddd", p = "abcd", removable = [3,2,1,4,5,6]
 * 输出：1
 * 解释：在移除下标 3 对应的字符后，"abcbddddd" 变成 "abcddddd" 。
 * "abcd" 是 "abcddddd" 的一个子序列。
 * <p>
 * 输入：s = "abcab", p = "abc", removable = [0,1,2,3,4]
 * 输出：0
 * 解释：如果移除数组 removable 的第一个下标，"abc" 就不再是 s 的一个子序列。
 * <p>
 * <p>
 * 提示：
 * <p>
 * 1 <= p.length <= s.length <= 105
 * 0 <= removable.length < s.length
 * 0 <= removable[i] < s.length
 * p 是 s 的一个 子字符串
 * s 和 p 都由小写英文字母组成
 * removable 中的元素 互不相同
 *
 * @author Devil
 * @date 2021/7/5 9:40 上午
 */
public class lc_1898 {

    /**
     * 可以知道 对于 removable[k] 满足 的时候 removable[k - 1] 比然也满足条件
     * 可以使用二分法找对应的值
     */
    public int maximumRemovals(String s, String p, int[] removable) {
        if (removable.length == 0 || s.length() == p.length()) {
            return 0;
        }
        char[] ss = s.toCharArray();
        char[] ps = p.toCharArray();
        // 二分法
        int l = 0;
        int r = removable.length + 1;
        while (l < r) {
            int mid = (l + r - 1) / 2;
            if (check(ss, ps, removable, mid)) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l - 1;
    }

    /**
     * s 在移除 removable 前 k 个之后是否满足 p 为子序列
     */
    private boolean check(char[] s, char[] p, int[] removable, int k) {
        int i = 0;
        boolean[] state = new boolean[s.length]; // false表示未移除 true表示对应位置的 在 s 里面是移除的
        while (i < k) { // 需要比较 0 ~ k-1
            state[removable[i]] = true;
            i++;
        }
        int l = 0;
        for (int j = 0; j < s.length; j++) {
            if (state[j]) { // 如果已经移除了 比较下一个
                continue;
            }
            if (s[j] == p[l]) {
                l++;
                if (l == p.length) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        lc_1898 a = new lc_1898();
        System.out.println(a.maximumRemovals("abcacb", "ab", new int[]{3, 1, 0}));
        System.out.println(a.maximumRemovals("abcbddddd", "abcd", new int[]{3, 2, 1, 4, 5, 6}));
        System.out.println(a.maximumRemovals("abcab", "abc", new int[]{0, 1, 2, 3, 4}));
        System.out.println(a.maximumRemovals("qobftgcueho", "obue", new int[]{5, 3, 0, 6, 4, 9, 10, 7, 2, 8}));
    }


}
