package com.devil.guide.arithmetic.leetcode;

/**
 * 给你一个字符串 s，由若干单词组成，单词前后用一些空格字符隔开。返回字符串中最后一个单词的长度。
 * 单词 是指仅由字母组成、不包含任何空格字符的最大子字符串。
 *
 * 输入：s = "Hello World"
 * 输出：5
 *
 * 输入：s = "   fly me   to   the moon  "
 * 输出：4
 *
 * 输入：s = "luffy is still joyboy"
 * 输出：6
 *
 * 1 <= s.length <= 104
 * s 仅有英文字母和空格 ' ' 组成
 * s 中至少存在一个单词
 *
 * @author Devil
 * @since 2021/9/14
 */
public class lc_58 {

    public int lengthOfLastWord(String s) {
        char[] chars = s.toCharArray();
        int old = 0;
        int now = 0;
        for (char c : chars) {
            if (c == ' ') {
                if (now == 0) {
                    continue;
                }
                old = now;
                now = 0;
                continue;
            }
            now++;
        }
        return now == 0 ? old : now;
    }

    public static void main(String[] args) {
        lc_58 a = new lc_58();
        System.out.println(a.lengthOfLastWord("Hello World"));
        System.out.println(a.lengthOfLastWord("   fly me   to   the moon  "));
        System.out.println(a.lengthOfLastWord("luffy is still joyboy"));
    }
}
