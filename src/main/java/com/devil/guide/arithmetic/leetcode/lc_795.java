package com.devil.guide.arithmetic.leetcode;

import java.io.IOException;

/**
 * 给定一个元素都是正整数的数组A，正整数 L以及R(L <= R)。
 * <p>
 * 求连续、非空且其中最大元素满足大于等于L小于等于R的子数组个数。
 * <p>
 * 例如 :
 * 输入:
 * A = [2, 1, 4, 3]
 * L = 2
 * R = 3
 * 输出: 3
 * 解释: 满足条件的子数组: [2], [2, 1], [3].
 * <p>
 * L, R 和A[i] 都是整数，范围在[0, 10^9]。
 * 数组A的长度范围在[1, 50000]。
 *
 * @author Devil
 * @since 2021/7/30
 */
public class lc_795 {

    /**
     * 如果我们将数组中的元素进行标记，
     * 0 表示 小于 L 的
     * 1 表示 大于等于L 小于等于R的
     * 2 表示 大于 R 的
     * <p>
     * 那么对于例子可以得到 [1, 0, 2, 1]
     * <p>
     * 我们要求的应该是不包含 2 的区间 所以可以拆解为 [1, 0] [1]
     * 对于不包含2的每个区间 我们要求 包含 1 的所有可能性
     * <p>
     * count方法表示 数组中 去除 大于 v 的数之后 所剩的所有子数组个数
     * <p>
     * count(nums, r) - count(nums, l - 1)
     * 这个算式是因为 count(nums, r) 包含了 只包含 小于 l 的元素 要减去这部分数据
     */
    public int numSubarrayBoundedMax(int[] nums, int left, int right) {
        return count(nums, right) - count(nums, left - 1);
    }

    public int count(int[] nums, int v) {
        int sum = 0;
        int c = 0;
//        for (int num : nums) {
//            if (num <= v) {
//                c++;
//            } else {
//                sum += (1 + c) * c / 2;
//                c = 0;
//            }
//        }
//        sum += (1 + c) * c / 2;
        // 不能用上面这段代码，虽然逻辑正确，但是由于 当c = 50000 的时候 50000 * 50001 超出int最大值
        for (int num : nums) {
            // [1, 2, 3] 可能的子区间个数为 [1] & [1, 2] [2] & [1. 2. 3] [2, 3] [3]
            // 为  1 + 2 + 3
            c = num <= v ? c + 1 : 0;
            sum += c;
        }
        return sum;
    }


    public static void main(String[] args) throws IOException {
        System.out.println(50000L * 50001 / 2);
        System.out.println(Integer.MAX_VALUE);
//        String s = IOUtils.toString(new FileInputStream("/data/a.t"), Charset.defaultCharset());
//        String[] split = s.trim().split(",");
//        int[] d = new int[split.length];
//        for (int i = 0; i < split.length; i++) {
//            d[i] = Integer.parseInt(split[i]);
//        }
//        lc_795 b = new lc_795();
//        b.numSubarrayBoundedMax(d, 1, 50000);
    }

}
