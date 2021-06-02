package playground;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class LinkedListQuestion {

    /**
     * Delete Node in a Linked List
     * https://leetcode.com/problems/delete-node-in-a-linked-list/solution/
     */
    @Test
    void testDeleteNode() {

    }

    private static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    //Time & Space Complexity: O(1)
    void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }

    /**
     * Remove Nth Node From End of List
     * https://leetcode.com/problems/remove-nth-node-from-end-of-list/solution/
     */
    @Test
    void testRemoveNthFromEnd() {
        ListNode headNode = createListNode(5);
        ListNode head = removeNthFromEnd(headNode, 2);
        Assertions.assertThat(head.val).isEqualTo(1);
    }

    //Time Complexity: O(L). Space Complexity: O(1)
    ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode firstPtr = dummy;
        ListNode secondPtr = dummy;
        // Advances first pointer so that the gap between first and second is n nodes apart
        for (int i = 1; i <= n + 1; i++) {
            firstPtr = firstPtr.next;
        }
        // Move first to the end, maintaining the gap
        while (firstPtr != null) {
            firstPtr = firstPtr.next;
            secondPtr = secondPtr.next;
        }
        secondPtr.next = secondPtr.next.next;
        return dummy.next;
    }

    ListNode createListNode(int num) {
        ListNode headNode = null;
        for (int i = num; i > 0; i--) {
            headNode = new ListNode(i, headNode);
        }
        return headNode;
    }

    /**
     * Reverse Linked List
     * https://leetcode.com/problems/reverse-linked-list/solution/
     */
    @Test
    void testReverseList() {
        ListNode headNode = createListNode(4);
        ListNode head = reverseList(headNode);
        Assertions.assertThat(head.val).isEqualTo(4);
        headNode = createListNode(3);
        head = recursiveReverseList(headNode);
        Assertions.assertThat(head.val).isEqualTo(3);
    }

    //Time Complexity: O(L). Space Complexity: O(1)
    ListNode reverseList(ListNode head) {// TODO: Important!
        ListNode previous = null;
        ListNode current = head;
        while (current != null) {
            var nextTemp = current.next;
            current.next = previous;
            previous = current;
            current = nextTemp;
        }
        return previous;
    }

    //Time Complexity: O(n). Space Complexity: O(n)
    ListNode recursiveReverseList(ListNode head) { // TODO: MEMORIZE!
        if (head == null || head.next == null)
            return head;
        ListNode p = recursiveReverseList(head.next);
        head.next.next = head;
        head.next = null;
        return p;
    }


    /**
     * Merge Two Sorted Lists
     * https://leetcode.com/problems/merge-two-sorted-lists/solution/
     */
    @Test
    void testMergeTwoLists() {
        ListNode headNode1 = createListNode(3);
        headNode1.next.next.val = 4;
        // (1,2,4)
        ListNode headNode2 = createListNode(3);
        headNode2.next.val = 3;
        headNode2.next.next.val = 4;
        // (1,3,4)
        var head = mergeTwoLists(headNode1, headNode2);
        Assertions.assertThat(head.val).isEqualTo(1);
        Assertions.assertThat(head.next.val).isEqualTo(1);
        Assertions.assertThat(head.next.next.val).isEqualTo(2);
        Assertions.assertThat(head.next.next.next.val).isEqualTo(3);
        Assertions.assertThat(head.next.next.next.next.val).isEqualTo(4);
        Assertions.assertThat(head.next.next.next.next.next.val).isEqualTo(4);
    }

    //Time Complexity: O(m+n). Space Complexity: O(1)
    ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // maintain an unchanging reference to node ahead of the return node.
        ListNode preHead = new ListNode(-1);

        ListNode prev = preHead;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }

        // At least one of l1 and l2 can still have nodes at this point, so connect
        // the non-null list to the end of the merged list.
        prev.next = l1 == null ? l2 : l1;

        return preHead.next;
    }

    /**
     * Palindrome Linked List
     * https://leetcode.com/problems/palindrome-linked-list/solution/
     */
    @Test
    void testIsPalindrome() {
        ListNode headNode1 = createListNode(4);
        headNode1.next.next.val = 2;
        headNode1.next.next.next.val = 1;
        // (1,2,2,1)
        Assertions.assertThat(isPalindrome(headNode1)).isTrue();
        ListNode headNode2 = createListNode(5);
        headNode2.next.next.next.val = 2;
        headNode2.next.next.next.next.val = 1;
        // (1,2,3,2,1)
        Assertions.assertThat(isPalindrome(headNode2)).isTrue();
        ListNode headNode3 = createListNode(4);
        // (1,2,3,4)
        Assertions.assertThat(isPalindrome(headNode3)).isFalse();
    }

    // Time Complexity: O(L). Space Complexity: O(1)
    // An inferior solution with space complexity O(n) is copying ListNode to an ArrayList, and use two pointers iterating from both side and do comparison
    boolean isPalindrome(ListNode head) {
        if (head == null) {
            return true;
        }
        // Find the end of first half and reverse second half.
        ListNode endOfFirstHalf = findEndOfFirstHalf(head);
        ListNode reversedSecondHead = reverseList(endOfFirstHalf.next);

        // Check whether or not there is a palindrome.
        ListNode p1 = head;
        ListNode p2 = reversedSecondHead;
        boolean result = true;
        while (result && p2 != null) { // p1 list can be longer than p2 list
            if (p1.val != p2.val) {
                result = false;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        // Restore the second half list and return the result.
        endOfFirstHalf.next = reverseList(reversedSecondHead);
        return result;
    }

    /*
    Returns the middle node if an odd-number of nodes, the last node of the first half if an even-number of nodes
     */
    ListNode findEndOfFirstHalf(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    /**
     * Linked List Cycle
     * https://leetcode.com/problems/linked-list-cycle/solution/
     */
    @Test
    void testLinkedListHasCycle() {
        ListNode headNode1 = createListNode(4);
        headNode1.next.next = headNode1;
        Assertions.assertThat(hasCycle(headNode1)).isTrue();
        ListNode headNode2 = createListNode(5);
        Assertions.assertThat(hasCycle(headNode2)).isFalse();

    }
    // Time Complexity: O(n). Space Complexity: O(1)
    // The slow pointer moves one step at a time while the fast pointer moves two steps at a time.
    // If there is no cycle in the list, the fast pointer will eventually reach the end and we can return false in this case.
    // If there is a cyclic the slow pointers will eventually meet the slow pointer.
    boolean hasCycle(ListNode head) {
        if (head == null)
            return false;
        ListNode slow = head;
        ListNode fast = head.next;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        }

        return true;
    }

    // Time Complexity: O(n). Space Complexity: O(n)
    boolean hasCycleInferior(ListNode head) {
        Set<ListNode> visitedNodes = new HashSet<>();
        while (head != null) {
            if (visitedNodes.contains(head)) {
                return true;
            }
            visitedNodes.add(head);
            head = head.next;
        }
        return false;
    }

}
