package com.devil.guide.arithmetic.leetcode;

import java.util.Arrays;

/**
 * 返回与给定的前序和后序遍历匹配的任何二叉树。
 * pre和post遍历中的值是不同的正整数。
 * <p>
 * 输入：pre = [1,2,4,5,3,6,7], post = [4,5,2,6,7,3,1]
 * 输出：[1,2,3,4,5,6,7]
 * <p>
 * 1 <= pre.length == post.length <= 30
 * pre[]和post[]都是1, 2, ..., pre.length的排列
 * 每个输入保证至少有一个答案。如果有多个答案，可以返回其中一个。
 *
 * @author Devil
 * @date 2021/7/7 4:53 下午
 */
public class lc_889 {

    /**
     * VLR 前序 LRV 后序
     * 可知 pre[0] 为根节点  post[length - 1] 为根节点
     */
    public TreeNode constructFromPrePost(int[] pre, int[] post) {
        if (pre.length == 0) {
            return null;
        }
        TreeNode root = new TreeNode(pre[0]);
        if (pre.length == 1) {
            return root;
        }
        for (int i = 0; i < post.length - 1; i++) {
            if (pre[1] == post[i]) { // pre[1] 不是左节点就是右节点
                // 左节点
                root.left = constructFromPrePost(Arrays.copyOfRange(pre, 1, i + 2), Arrays.copyOfRange(post, 0, i + 1));
                // 右节点
                if (i != post.length - 2) { // 如果只有一边 都放在左边
                    root.right = constructFromPrePost(Arrays.copyOfRange(pre, i + 2, pre.length), Arrays.copyOfRange(post, i + 1, post.length - 1));
                }

                break;
            }
        }
        return root;
    }

    public static void main(String[] args) {
        lc_889 a = new lc_889();
        System.out.println(a.constructFromPrePost(new int[]{1,2,4,5,3,6,7}, new int[]{4,5,2,6,7,3,1}));
    }
}
