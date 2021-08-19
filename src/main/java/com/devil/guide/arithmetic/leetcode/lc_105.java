package com.devil.guide.arithmetic.leetcode;


/**
 * 给定一棵树的前序遍历preorder 与中序遍历 inorder。请构造二叉树并返回其根节点。
 * <p>
 * Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
 * Output: [3,9,20,null,null,15,7]
 * <p>
 * Input: preorder = [-1], inorder = [-1]
 * Output: [-1]
 * <p>
 * 1 <= preorder.length <= 3000
 * inorder.length == preorder.length
 * -3000 <= preorder[i], inorder[i] <= 3000
 * preorder和inorder均无重复元素
 * inorder均出现在preorder
 * preorder保证为二叉树的前序遍历序列
 * inorder保证为二叉树的中序遍历序列
 *
 * @author Devil
 * @date 2021/7/12 5:23 下午
 */
public class lc_105 {

    /**
     * 前序遍历为 VLR 也就是根节点先遍历 中序遍历为 LVR 做节点先遍历
     * preorder[0] 必定为 root
     * 在 inorder 中找到 preorder[0] 的坐标 坐标左侧为左树 右侧为右树
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return buildIn(preorder, 0, inorder, 0, preorder.length);
    }

    /**
     * @param pidx   前序遍历的开始位置
     * @param iidx   中序遍历的开始位置
     * @param length 数据长度
     * @return
     */
    public TreeNode buildIn(int[] preorder, int pidx, int[] inorder, int iidx, int length) {
        if (length == 0) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[pidx]);
        if (length == 1) {
            return root;
        }
        for (int i = iidx; i < length; i++) {
            if (root.val == inorder[i]) {
                // 左侧
                root.left = buildIn(preorder, pidx + 1, inorder, iidx, i - iidx);
                // 右侧
                root.right = buildIn(preorder, i + 1, inorder, i + 1, inorder.length - i - 1);
                break;
            }
        }
        return root;
    }

    public static void main(String[] args) {
        lc_105 a = new lc_105();
        System.out.println(a.buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7}));
        System.out.println(a.buildTree(new int[]{-1}, new int[]{-1}));
        System.out.println(a.buildTree(new int[]{3, 5}, new int[]{3,5}));
    }

}
