package com.devil.guide.arithmetic.leetcode;

/**
 *
 * 编写一个高效的算法来判断m x n矩阵中，是否存在一个目标值。该矩阵具有如下特性：
 *
 * 每行中的整数从左到右按升序排列。
 * 每行的第一个整数大于前一行的最后一个整数。
 *
 *
 * 输入：matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
 * 输出：true
 *
 * 输入：matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13
 * 输出：false
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 100
 * -104 <= matrix[i][j], target <= 104
 *
 * @author Devil
 * @since 2021/9/14
 */
public class lc_74 {

    /**
     * 由题目可以知道矩阵是有序的
     * 如果将矩阵行前后拼接可以使用二分法查找
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int length = matrix.length * matrix[0].length;
        int left = 0;
        int right = length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int v = matrix[mid / matrix[0].length][mid % matrix[0].length];
            if (v > target) {
                right = mid - 1;
            } else if (v < target) {
                left = mid + 1;
            } else {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        lc_74 a = new lc_74();
        System.out.println(a.searchMatrix(new int[][]{{1,3,5,7}, {10,11,16,20}, {23,30,34,60}}, 3));
        System.out.println(a.searchMatrix(new int[][]{{1,3,5,7}, {10,11,16,20}, {23,30,34,60}}, 13));
    }

}
