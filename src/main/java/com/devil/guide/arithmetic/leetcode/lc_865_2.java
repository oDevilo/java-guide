package com.devil.guide.arithmetic.leetcode;

import org.limbo.utils.tuple.Tuple2;
import org.limbo.utils.tuple.Tuples;

/**
 * 与 lc_1123 重复
 *
 * 给定一个根为root的二叉树，每个节点的深度是 该节点到根的最短距离 。
 * 如果一个节点在 整个树 的任意节点之间具有最大的深度，则该节点是 最深的 。
 * 一个节点的 子树 是该节点加上它的所有后代的集合。
 * 返回能满足 以该节点为根的子树中包含所有最深的节点 这一条件的具有最大深度的节点
 *
 * 示例 1：
 * 输入：root = [3,5,1,6,2,0,8,null,null,7,4]
 * 输出：[2,7,4]
 * 解释：
 * 我们返回值为 2 的节点，在图中用黄色标记。
 * 在图中用蓝色标记的是树的最深的节点。
 * 注意，节点 5、3 和 2 包含树中最深的节点，但节点 2 的子树最小，因此我们返回它。
 *
 * 示例 2：
 * 输入：root = [1]
 * 输出：[1]
 * 解释：根节点是树中最深的节点。
 *
 * 示例 3：
 * 输入：root = [0,1,3,null,2]
 * 输出：[2]
 * 解释：树中最深的节点为 2 ，有效子树为节点 2、1 和 0 的子树，但节点 2 的子树最小。
 *
 * 树中节点的数量介于 1 和 500 之间。
 * 0 <= Node.val <= 500
 * 每个节点的值都是独一无二的。
 *
 * @author Devil
 * @since 2021/9/14
 */
public class lc_865_2 {

    public TreeNode subtreeWithAllDeepest(TreeNode root) {
        return dfs(root).getA();
    }

    /**
     * Tuple2 保存节点信息以及深度 这里的深度和题目的深度刚好相反 这里是节点到叶子的距离
     */
    public Tuple2<TreeNode, Integer> dfs(TreeNode node) {
        // 叶子节点 深度为0
        if (node == null) return Tuples.of(null, 0);
        Tuple2<TreeNode, Integer> l = dfs(node.left);
        Tuple2<TreeNode, Integer> r = dfs(node.right);
        // 如果左节点深度更大 返回左节点
        if (l.getB() > r.getB()) return Tuples.of(l.getA(), l.getB() + 1);
        // 如果右节点深度更大 返回右节点
        if (l.getB() < r.getB()) return Tuples.of(r.getA(), r.getB() + 1);
        // 如果左节点和右节点深度相同，返回当前节点
        return Tuples.of(node, r.getB() + 1);
    }

    public static void main(String[] args) {
        // 3,5,1,6,2,0,8,null,null,7,4
        TreeNode t3 = new TreeNode();
        t3.val = 3;
        TreeNode t5 = new TreeNode();
        t5.val = 5;
        TreeNode t1 = new TreeNode();
        t1.val = 1;
        TreeNode t6 = new TreeNode();
        t6.val = 6;
        TreeNode t2 = new TreeNode();
        t2.val = 2;
        TreeNode t0 = new TreeNode();
        t0.val = 0;
        TreeNode t8 = new TreeNode();
        t8.val = 8;
        TreeNode t7 = new TreeNode();
        t7.val = 7;
        TreeNode t4 = new TreeNode();
        t4.val = 4;

        t3.left = t5;
        t3.right = t1;

        t5.left = t6;
        t5.right = t2;

        t1.left = t0;
        t1.right = t8;

        t2.left = t7;
        t2.right = t4;

        lc_865_2 a = new lc_865_2();
        TreeNode treeNode = a.subtreeWithAllDeepest(t3);
        System.out.println(treeNode.val);
    }

}
