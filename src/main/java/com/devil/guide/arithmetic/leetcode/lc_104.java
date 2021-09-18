package com.devil.guide.arithmetic.leetcode;

import javax.xml.bind.SchemaOutputResolver;

/**
 * 给定一个二叉树，找出其最大深度。
 *
 * 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 *
 * 说明:叶子节点是指没有子节点的节点。
 *
 * 示例：
 * 给定二叉树 [3,9,20,null,null,15,7]，
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回它的最大深度3 。
 *
 * @author Devil
 * @since 2021/9/14
 */
public class lc_104 {

    public int maxDepth(TreeNode root) {
        return dfs(root, 0);
    }

    /**
     * 深度优先
     */
    private int dfs(TreeNode node, int depth) {
        if (node == null) return depth;
        int l = dfs(node.left, depth + 1);
        int r = dfs(node.right, depth + 1);
        if (l > r) return l;
        if (l < r) return r;
        return l;
    }

    public static void main(String[] args) {
        TreeNode t3 = new TreeNode();
        t3.val = 3;
        TreeNode t9 = new TreeNode();
        t9.val = 9;
        TreeNode t20 = new TreeNode();
        t20.val = 20;
        TreeNode t15 = new TreeNode();
        t15.val = 15;
        TreeNode t7 = new TreeNode();
        t7.val = 7;

        t3.left = t9;
        t3.right = t20;

        t20.left = t15;
        t20.right = t7;

        lc_104 a = new lc_104();
        System.out.println(a.maxDepth(t3));
    }
}
