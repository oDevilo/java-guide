package com.devil.guide.arithmetic.leetcode;

/**
 * 有 n 个城市通过一些航班连接。给你一个数组flights ，其中 flights[i] = [fromi, toi, pricei]，
 * 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
 * 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，你的任务是找到出一条最多经过 k站中转的路线，
 * 使得从 src 到 dst 的 价格最便宜 ，并返回该价格。如果不存在这样的路线，则输出 -1。
 *
 * 输入:
 * n = 3, edges = [[0,1,100],[1,2,100],[0,2,500]]
 * src = 0, dst = 2, k = 1
 * 输出: 200
 *
 * 输入:
 * n = 3, edges = [[0,1,100],[1,2,100],[0,2,500]]
 * src = 0, dst = 2, k = 0
 * 输出: 500
 *
 * 1 <= n <= 100
 * 0 <= flights.length <= (n * (n - 1) / 2)
 * flights[i].length == 3
 * 0 <= fromi, toi < n
 * fromi != toi
 * 1 <= pricei <= 104
 * 航班没有重复，且不存在自环
 * 0 <= src, dst, k < n
 * src != dst
 *
 * @author Devil
 * @since 2021/8/24
 */
public class lc_787 {

    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        int[][] graph = new int[n][n];
        return 0;
    }
}
