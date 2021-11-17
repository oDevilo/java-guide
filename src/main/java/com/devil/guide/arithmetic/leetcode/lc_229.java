package com.devil.guide.arithmetic.leetcode;

import org.limbo.utils.JacksonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Devil
 * @since 2021/9/23
 */
/*
    给定一个大小为n的整数数组，找出其中所有出现超过 n/3 次的元素。
    进阶：尝试设计时间复杂度为 O(n)、空间复杂度为 O(1)的算法解决此问题。

    输入：[3,2,3]
    输出：[3]

    输入：nums = [1]
    输出：[1]

    输入：[1,1,1,3,3,2,2,2]
    输出：[1,2]

    1 <= nums.length <= 5 * 104
    -109 <= nums[i] <= 109
 */
public class lc_229 {

    /*
      简单思路：使用hashmap存放数字和出现的次数，如果超过次数就放入数组
      时间复杂度为 O(n)、空间复杂度为 O(n)

      进阶：摩尔投票法
      时间复杂度为 O(n)、空间复杂度为 O(1)

      普通的摩尔投票法，用来检测一组数，其中超过二分之一的那个数
      创建一个数组，读取当前位的数，与数组元素比较
      1. 数组为空，则放入当前数
      2. 数组内的数等于当前数字，放入当前数
      3. 数组内的数不等于当前数字，移除一个数组内的数
      则最后剩余的数出现次数超过二分之一就是满足的那个数

      题目要求的是超过 n/3 那么最多可能存在两个数满足情况
      创建一个n/3 * 2长度的数组，前面半段表示先出现的数，后半段表示出现的第二个数，如果出现第三个不同的数则可以消除对应的值
      最终剩余的数，再遍历一遍，如果超过n/3就返回

     */
    public List<Integer> majorityElement(int[] nums) {
        int a = 0;
        int b = 0;
        int a_v = 0; // a对应的数暂存的数量
        int b_v = 0;

        for (int num : nums) {
            if (a_v > 0 && a == num) {
                a_v++;
            } else if (b_v > 0 && b == num) {
                b_v++;
            } else if (a_v <= 0) {
                a = num;
                a_v++;
            } else if (b_v <= 0) {
                b = num;
                b_v++;
            } else { // 出现第三个不同的数 消除
                a_v--;
                b_v--;
            }
        }

        int count_a = 0;
        int count_b = 0;

        for (int num : nums) {
            if (num == a && a_v > 0) {
                count_a++;
            } else if (num == b && b_v > 0) {
                count_b++;
            }
        }
        List<Integer> result = new ArrayList<>();
        if (count_a > nums.length / 3) {
            result.add(a);
        }
        if (count_b > nums.length / 3) {
            result.add(b);
        }
        return result;
    }

    public static void main(String[] args) {
        lc_229 a = new lc_229();
        System.out.println(JacksonUtils.toJSONString(a.majorityElement(new int[]{2, 1, 1, 3, 1, 4, 5, 6})));
        System.out.println(JacksonUtils.toJSONString(a.majorityElement(new int[]{3, 2, 3})));
        System.out.println(JacksonUtils.toJSONString(a.majorityElement(new int[]{1})));
        System.out.println(JacksonUtils.toJSONString(a.majorityElement(new int[]{1, 1, 1, 3, 3, 2, 2, 2})));
    }

}
