package com.devil.guide.arithmetic.leetcode;

/**
 * 给你一个下标从 0开始的整数数组nums，如果恰好删除一个元素后，数组严格递增，那么请你返回true，
 * 否则返回false。如果数组本身已经是严格递增的，请你也返回true。
 * 数组nums是 严格递增的定义为：对于任意下标的1 <= i < nums.length都满足nums[i - 1] < nums[i]。
 * <p>
 * 输入：nums = [1,2,10,5,7]
 * 输出：true
 * 解释：从 nums 中删除下标 2 处的 10 ，得到 [1,2,5,7] 。
 * [1,2,5,7] 是严格递增的，所以返回 true 。
 * <p>
 * 输入：nums = [2,3,1,2]
 * 输出：false
 * 解释：
 * [3,1,2] 是删除下标 0 处元素后得到的结果。
 * [2,1,2] 是删除下标 1 处元素后得到的结果。
 * [2,3,2] 是删除下标 2 处元素后得到的结果。
 * [2,3,1] 是删除下标 3 处元素后得到的结果。
 * 没有任何结果数组是严格递增的，所以返回 false 。
 * <p>
 * 输入：nums = [1,1,1]
 * 输出：false
 * 解释：删除任意元素后的结果都是 [1,1] 。
 * [1,1] 不是严格递增的，所以返回 false 。
 * <p>
 * 输入：nums = [1,2,3]
 * 输出：true
 * 解释：[1,2,3] 已经是严格递增的，所以返回 true 。
 * <p>
 * 2 <= nums.length <= 1000
 * 1 <= nums[i] <= 1000
 * <p>
 * <p>
 * 思路与算法
 * <p>
 * 数组 \textit{nums}nums 严格递增的充分必要条件是对于任意两个相邻下标对 (i - 1, i)(i−1,i)
 * 均有 \textit{nums}[i] > \textit{nums}[i-1]nums[i]>nums[i−1]。换言之，如果存在下标 ii
 * 有 \textit{nums}[i] \le \textit{nums}[i-1]nums[i]≤nums[i−1]，那么 \textit{nums}nums 并非严格递增。
 * <p>
 * 因此，我们可以从左至右遍历寻找非递增的相邻下标对。假设对于某个下标对 (i - 1, i)(i−1,i)
 * 有 \textit{nums}[i] \le \textit{nums}[i-1]nums[i]≤nums[i−1]，
 * 如果我们想使得 \textit{nums}nums 在删除一个元素后严格递增，那么必须至少删除下标对 (i - 1, i)(i−1,i) 对应的元素之一。
 * <p>
 * 我们可以用 \textit{check}(\textit{idx})check(idx) 函数来检查数组 \textit{nums}nums
 * 删去下标为 \textit{idx}idx 的元素后是否严格递增。具体地，我们对 \textit{nums}nums 进行一次遍历，
 * 在遍历的过程中记录上一个元素的下标，并与当前遍历到的元素进行比较。需要注意的是，下标为 \textit{idx}idx 的元素需要被跳过。
 *
 * @author Devil
 * @since 2021/11/4
 */
public class lc_1909 {

    /**
     * 如果存在非递增的点 n 则 nums[n] <= nums[n - 1] 需要考虑删除 n 或者 n - 1
     * 删除 n 后 需要满足 nums[n - 1] < nums[n + 1]
     * 删除 n - 1 后 需要满足 nums[n - 2] < nums[n]
     * 则如果出现 nums[n - 1] >= nums[n + 1] 且 nums[n - 2] >= nums[n] 说明删除哪个都无法满足 直接返回失败
     * 出现一次n后继续遍历，如果出现一次新的非递增情况，则返回失败
     */
    public boolean canBeIncreasing(int[] nums) {
        int pos = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] <= nums[i - 1]) {
                if (pos != 0) {
                    return false;
                }
                if (i + 1 < nums.length && nums[i - 1] >= nums[i + 1] && i - 2 >= 0 && nums[i - 2] >= nums[i]) {
                    return false;
                }
                pos = i;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        lc_1909 a = new lc_1909();
        System.out.println(a.canBeIncreasing(new int[]{1, 2, 10, 5, 7}));
        System.out.println(a.canBeIncreasing(new int[]{2, 3, 1, 2}));
        System.out.println(a.canBeIncreasing(new int[]{1, 1, 1}));
        System.out.println(a.canBeIncreasing(new int[]{1, 2, 3}));
    }
}
