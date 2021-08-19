package com.devil.guide.arithmetic.leetcode;

import org.limbo.utils.JacksonUtils;

/**
 *
 * 一个整型数组 nums 里除两个数字之外，其他数字都出现了两次。
 * 请写程序找出这两个只出现一次的数字。要求时间复杂度是O(n)，空间复杂度是O(1)。
 *
 * 输入：nums = [4,1,4,6]
 * 输出：[1,6] 或 [6,1]
 *
 * 输入：nums = [1,2,10,4,1,4,3,3]
 * 输出：[2,10] 或 [10,2]
 *
 * 2 <= nums.length <= 10000
 *
 * @author Devil
 * @date 2021/7/22 1:42 下午
 */
public class lc_offer_56 {

    /**
     * 前提思考：如何找到 整型数组 nums 里除x之外，其他数字都出现了两次的x
     * 这个时候可以考虑使用异或操作，相同的为0，不同为1，等于相同数会被消除，最终就剩x了
     *
     * 但是这个题目是有x、y两个数，那么直接操作肯定不行，所以要考虑如何将这两个数分成两组，每组单独异或后剩余x和y
     * 现在就要考虑这个分组怎么分
     *
     * 我们将nums先进行一次异或，获取到数 z 那么 z的二进制第i为 为 0 代表 x y 第 i 位 相同， 1则为不同
     * 由于 x、y 不等 所以 z 中必定存在一位 1
     *
     * 我们随便获取一位为1的 假设位数为 i
     * 对 nums 使用如下方式进行分组 nums[j] 的 第 i 位 为 0 放入 第一组 为 1 放入第二组
     *
     * 原理：
     * 为 0 的一组 为 1 的一组是因为 异或操作 x y 这两个数 i 位上必定 一个为 0 一个为 1，这样就将他们分到了两组中
     * 对于剩余的数据，将为0的一组 为 1的分一组，对于每组 必定是偶数，能相互抵消形成0，这是因为 异或 而且存在一个和当前数相同的数，
     * 所以对于任意除 x y 之外的数，不管第i位是0还是1，必定存在另一个数的第i位数和他相同
     *
     */
    public int[] singleNumbers(int[] nums) {
        // 获取 z
        int z = nums[0];
        for (int i = 1;i<nums.length;i++) {
            z = z ^ nums[i];
        }
        // 获取第一个为 1 的位置
        int m = 1;
        while (true) {
            if ((z & m) != 0) { // & 是都为 1 才为 1 由于z的前i-1位都是0结果为0，所以只有在i的时候才会不等于0
                break;
            }
            m=m<<1; // 左移一位
        }
        // 分组处理
        int x = 0;
        int y = 0;
        for (int num : nums) {
            if ((num & m) != 0) { // num 第i位 为 1
                x = x ^ num;
            } else { // num 第i位 为 0
                y = y ^ num;
            }
        }
        return new int[]{x, y};
    }

    public static void main(String[] args) {
        lc_offer_56 a = new lc_offer_56();
        System.out.println(JacksonUtils.toJSONString(a.singleNumbers(new int[]{1,2,5,2})));
        System.out.println(JacksonUtils.toJSONString(a.singleNumbers(new int[]{4,1,4,6})));
        System.out.println(JacksonUtils.toJSONString(a.singleNumbers(new int[]{1,2,10,4,1,4,3,3})));
    }
}
