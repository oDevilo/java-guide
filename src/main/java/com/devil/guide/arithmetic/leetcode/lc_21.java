package com.devil.guide.arithmetic.leetcode;

/**
 * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 * <p>
 * 输入：l1 = [1,2,4], l2 = [1,3,4]
 * 输出：[1,1,2,3,4,4]
 * <p>
 * 输入：l1 = [], l2 = []
 * 输出：[]
 * <p>
 * 输入：l1 = [], l2 = [0]
 * 输出：[0]
 * <p>
 * 两个链表的节点数目范围是 [0, 50]
 * -100 <= Node.val <= 100
 * l1 和 l2 均按 非递减顺序 排列
 *
 * @author Devil
 * @date 2021/7/6 9:54 上午
 */
public class lc_21 {

    /**
     * 双指针的做法
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode head = new ListNode(0);
        ListNode now = head;
        while (l1 != null && l2 != null) {
            if (l1.val > l2.val) {
                now.next = l2;
                now = now.next;
                l2 = l2.next;
            } else {
                now.next = l1;
                now = now.next;
                l1 = l1.next;
            }
        }

        // 如果是由于 l1 = null 跳出循环的，需要把 l2 之后的数据都拼上
        now.next = l1 == null ? l2 : l1;
        return head.next; // 初始化的时候创建了一个占位用的，所以需要返回的是后一个
    }

    public static void main(String[] args) {
        lc_21 a = new lc_21();
        a.print(new int[]{1, 2, 4}, new int[]{1, 3, 4});
        a.print(new int[]{}, new int[]{});
        a.print(new int[]{}, new int[]{0});
    }

    private void print(int[] arr1, int[] arr2) {
        ListNode l1 = null;
        ListNode l2 = null;
        if (arr1.length > 0) {
            l1 = new ListNode(arr1[0]);
            ListNode now = l1;
            for (int i = 1; i < arr1.length; i++) {
                now.next = new ListNode(arr1[i]);
                now = now.next;
            }
        }
        if (arr2.length > 0) {
            l2 = new ListNode(arr2[0]);
            ListNode now = l2;
            for (int i = 1; i < arr2.length; i++) {
                now.next = new ListNode(arr2[i]);
                now = now.next;
            }
        }
        ListNode listNode = mergeTwoLists(l1, l2);
        while (listNode != null) {
            System.out.print(listNode.val);
            listNode = listNode.next;
        }
        System.out.println();
    }


    private static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}
