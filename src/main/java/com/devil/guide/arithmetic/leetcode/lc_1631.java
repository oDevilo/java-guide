package com.devil.guide.arithmetic.leetcode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 你准备参加一场远足活动。给你一个二维rows x columns的地图heights，其中heights[row][col]表示格子(row, col)的高度。
 * 一开始你在最左上角的格子(0, 0)，且你希望去最右下角的格子(rows-1, columns-1)（注意下标从 0 开始编号）。
 * 你每次可以往 上，下，左，右四个方向之一移动，你想要找到耗费 体力 最小的一条路径。
 * 一条路径耗费的 体力值是路径上相邻格子之间 高度差绝对值的 最大值决定的。
 * 请你返回从左上角走到右下角的最小体力消耗值。
 * <p>
 * 输入：heights = [[1,2,2],[3,8,2],[5,3,5]]
 * 输出：2
 * 解释：路径 [1,3,5,3,5] 连续格子的差值绝对值最大为 2 。
 * 这条路径比路径 [1,2,2,2,5] 更优，因为另一条路径差值最大值为 3 。
 * <p>
 * 输入：heights = [[1,2,3],[3,8,4],[5,3,5]]
 * 输出：1
 * 解释：路径 [1,2,3,4,5] 的相邻格子差值绝对值最大为 1 ，比路径 [1,3,5,3,5] 更优。
 * <p>
 * 输入：heights = [[1,2,1,1,1],[1,2,1,2,1],[1,2,1,2,1],[1,2,1,2,1],[1,1,1,2,1]]
 * 输出：0
 * 解释：上图所示路径不需要消耗任何体力。
 * <p>
 * <p>
 * 提示：
 * rows == heights.length
 * columns == heights[i].length
 * 1 <= rows, columns <= 100
 * 1 <= heights[i][j] <= 10^6
 *
 * @author Devil
 * @date 2021/7/13 5:52 下午
 */
public class lc_1631 {

    /**
     * 二分法
     * 由于 1 <= heights[i][j] <= 10^6 所以结果的最大值为 10^6
     * 可以使用二分法求结果 x
     * 每次求的时候 使用深度优先 或 广度优先 求是否存在满足 小于等于 x 的线路
     * 下面使用广度优先
     */
    public int minimumEffortPath(int[][] heights) {
        int left = 0, right = 999999, ans = 0;
        while (left <= right) {
            int mid = (left + right) / 2;
            // 广度优先 存储当前这层的所有节点
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, 0});
            // 存储遍历过的节点
            boolean[][] seen = new boolean[heights.length][heights[0].length];
            seen[0][0] = true;
            while (!queue.isEmpty()) {
                // 获取现在要遍历的节点，对其各个方向进行遍历
                int[] cell = queue.poll();
                int x = cell[0], y = cell[1];
                // 上
                if (y > 0 && !seen[x][y - 1] && Math.abs(heights[x][y] - heights[x][y - 1]) <= mid) {
                    queue.offer(new int[]{x, y - 1});
                    seen[x][y - 1] = true;
                }
                // 下
                if (y < heights[0].length - 1 && !seen[x][y + 1] && Math.abs(heights[x][y] - heights[x][y + 1]) <= mid) {
                    queue.offer(new int[]{x, y + 1});
                    seen[x][y + 1] = true;
                }
                // 左
                if (x > 0 && !seen[x - 1][y] && Math.abs(heights[x][y] - heights[x - 1][y]) <= mid) {
                    queue.offer(new int[]{x - 1, y});
                    seen[x - 1][y] = true;
                }
                // 右
                if (x < heights.length - 1 && !seen[x + 1][y] && Math.abs(heights[x][y] - heights[x + 1][y]) <= mid) {
                    queue.offer(new int[]{x + 1, y});
                    seen[x + 1][y] = true;
                }
            }
            // 如果最后一个为true表示此路能通 往小的区间继续找 否则找大区间
            if (seen[heights.length - 1][heights[0].length - 1]) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        lc_1631 a = new lc_1631();
        System.out.println(a.minimumEffortPath(new int[][]{{1}}));
        System.out.println(a.minimumEffortPath(new int[][]{{1, 2, 2}, {3, 8, 2}, {5, 3, 5}}));
        System.out.println(a.minimumEffortPath(new int[][]{{1, 2, 3}, {3, 8, 4}, {5, 3, 5}}));
        System.out.println(a.minimumEffortPath(new int[][]{{1, 10, 6, 7, 9, 10, 4, 9}}));
        System.out.println(a.minimumEffortPath(new int[][]{{1, 2, 1, 1, 1}, {1, 2, 1, 2, 1}, {1, 2, 1, 2, 1}, {1, 2, 1, 2, 1}, {1, 1, 1, 2, 1}}));
    }

}
