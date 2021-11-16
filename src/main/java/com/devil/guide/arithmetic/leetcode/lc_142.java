package com.devil.guide.arithmetic.leetcode;

import java.time.temporal.Temporal;

/**
 *
 * 给定一个链表，返回链表开始入环的第一个节点。如果链表无环，则返回null。
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。
 * 如果 pos 是 -1，则在该链表中没有环。注意，pos 仅仅是用于标识环的情况，并不会作为参数传递到函数中。
 *
 * 说明：不允许修改给定的链表。
 *
 * 进阶：你是否可以使用 O(1) 空间解决此题？
 *
 * 输入：head = [3,2,0,-4], pos = 1
 * 输出：返回索引为 1 的链表节点
 * 解释：链表中有一个环，其尾部连接到第二个节点。
 *
 * 输入：head = [1,2], pos = 0
 * 输出：返回索引为 0 的链表节点
 * 解释：链表中有一个环，其尾部连接到第一个节点。
 *
 * 输入：head = [1], pos = -1
 * 输出：返回 null
 * 解释：链表中没有环。
 *
 * 链表中节点的数目范围在范围 [0, 104] 内
 * -105 <= Node.val <= 105
 * pos 的值为 -1 或者链表中的一个有效索引
 *
 * @author Devil
 * @since 2021/9/28
 */
public class lc_142 {
    /**
     * 快慢指针法
     * 如果快指针到结尾 则说明无环 如果快慢指针交汇 说明有环
     * o -> o -> o -> o -> o
     *          ^          |
     *          |          |
     *          o <- o <- o
     *
     * 设:
     * 链表头到 入口 的长度为 L
     * 入口到交汇点的长度为 X
     * 环的周长为 R
     * 那么快慢指针走过的路程分别为
     * F = L + X + n x R
     * S = L + X
     * F = 2 x S
     * 可以得到
     * L = n x R - X
     * 分两种情况：
     * 1. 入口就是头结点，由于 F = 2 x S 那么慢指针走一圈，快指针刚好走两圈，交汇处就是入口
     * 2. 入口不是头结点，如上图，从1可以知道即使同时出发会在入口处相遇，那么如果快指针先出发，那么慢指针肯定没走过一圈就会被快指针追上
     *
     * 所以 在2的情况下 n = 1
     * L = R - X
     * 也就是说，当快慢指针相遇时，我们有另一个慢指针从头结点开始，当两个慢指针相遇的时候则就是入口点
     */
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) { // 如果这种情况，表示已经到结尾了
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) { // 交汇 有环
                ListNode tmp = head;
                if (tmp == fast) { // 如果头结点就是入环口
                    return head;
                }
                while (true) {
                    slow = slow.next;
                    tmp = tmp.next;
                    if (slow == tmp) {
                        return slow;
                    }
                }
            }
        }
        return null;
    }
}
