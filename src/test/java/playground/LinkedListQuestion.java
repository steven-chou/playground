package playground;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class LinkedListQuestion {

    /**
     * Delete Node in a Linked List
     * You are given the node to be deleted node. You will not be given access to the first node of head.
     * All the values of the linked list are unique, and it is guaranteed that the given node node is not the last node in the linked list.
     * https://leetcode.com/problems/delete-node-in-a-linked-list/solution/
     */
    @Test
    void testDeleteNode() {
        //deleteNode(new ListNode(1, null));
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

    /**
     * 1. Copy the val of the next node of the deleting node to the deleting node
     * 2. Set the next pointer of the deleting node to the next node of the deleting node
     * 3. Set the next pointer of the next node of the deleting node to null
     * Step 1: 1 -> 2 -> 3 -> 4 (delete node 2)
     * Step 2: 1 -> 3 -> 3 -> 4
     * Step 3: 1 -> 3 -> 4   3 -> null
     *
     * @param node - The node which will be deleted from the list
     */
    void deleteNode(ListNode node) {
        ListNode nextNode = node.next;
        node.val = nextNode.val;
        node.next = nextNode.next;
        nextNode.next = null;
    }

    /**
     * Remove Linked List Elements
     * Given the head of a linked list and an integer val, remove all the nodes of the linked list that
     * has Node.val == val, and return the new head.
     * https://leetcode.com/problems/remove-linked-list-elements/description/
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     *
     * @param head head node of linked list
     * @param val  deleting node value
     * @return head node
     */
    ListNode removeElements(ListNode head, int val) {
        // Sentinel nodes is needed and its main purpose is to standardize the situation, for example,
        // make linked list to be never empty and never headless and hence
        // simplify insert and delete. This is important when deleting node(s) are at head
        ListNode sentinel = new ListNode(0);
        sentinel.next = head;
        ListNode prev = sentinel, current = head;
        while (current != null) {
            if (current.val == val) {
                prev.next = current.next;
                current = current.next;
            } else {
                prev = current;
                current = current.next;
            }
        }
        return sentinel.next;
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
        // Need the sentinel node to handel the scenario we will delete the head/first node.
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
     * Given the head of a singly linked list, reverse the list, and return the reversed list.
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

    /**
     * Need one ptr to first save the "next" node, and another "previous" ptr(init to null) to store the current
     * node reference before moving to the next node
     * Time Complexity: O(L).
     * Space Complexity: O(1)
     */
    ListNode reverseList(ListNode head) {// TODO: Important!
        // previous ptr needs to start w/ null, cuz when reversing the list, the original head will become tail
        // and the next pointer will be point to null
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
    ListNode recursiveReverseList(ListNode head) { // less performant
        if (head == null || head.next == null)
            return head;
        ListNode p = recursiveReverseList(head.next);
        head.next.next = head;
        head.next = null;
        return p;
    }


    /**
     * Merge Two Sorted Lists
     * You are given the heads of two sorted linked lists list1 and list2.
     * Merge the two lists in a one sorted list. The list should be made by splicing together
     * the nodes of the first two lists.
     * Return the head of the merged linked list.
     * Input: list1 = [1,2,4], list2 = [1,3,4]
     * Output: [1,1,2,3,4,4]
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

    /**
     * Use a dummy head and tail ptr to compare the l1 and l2 node value then update the tail.next
     * then advance either l1 and l2 ptr until one of them reach the end then append the other to the tail
     * First we maintain a dummy "head" node that allows to return the head of merged list later.
     * We also maintain a "tail" node for this merged list, and we keep updating its next ptr to the
     * added new node from L1 or L2
     * Then, we do the following until at least one of L1 and L2 points to null:
     * if the value at L1 is less than or equal to the value at L2, then we connect l1 to the tails node and increment L1.
     * Otherwise, we do the same, but for L2. Then, regardless of which list we connected, we increment tail to keep it
     * one step behind one of our list heads.
     * After the loop terminates, at most one of L1 and L2 is non-null.
     * Therefore (because the input lists were in sorted order), if either list is non-null, it contains only elements greater
     * than all of the previously-merged elements. This means that we can simply connect the non-null list to the
     * merged list and return it.
     * Time Complexity: O(m+n).
     * Space Complexity: O(1)
     */
    //
    ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // maintain an unchanging reference to node ahead of the return node.
        ListNode head = new ListNode(-1);
        ListNode tail = head;
        // Iterate until we reach the end of ONE list
        while (l1 != null && l2 != null) {
            // Compare the heads of both lists
            if (l1.val <= l2.val) {
                tail.next = l1;
                l1 = l1.next;
            } else {
                tail.next = l2;
                l2 = l2.next;
            }
            tail = tail.next;
        }

        // At least one of l1 and l2 can still have nodes at this point, i.e. null, so connect
        // the non-null list to the end of the merged list.
        tail.next = l1 == null ? l2 : l1;

        return head.next;
    }

    /**
     * Palindrome Linked List
     * https://leetcode.com/problems/palindrome-linked-list/
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

    /**
     * Reverse Second Half In-place approach
     * 1. Find the end of the first half.
     * 2. Reverse the second half.
     * 3. Determine whether there is a palindrome.
     * 4. Restore the list.
     * 5. Return the result.
     * Time Complexity: O(n). Space Complexity: O(1)*
     * An inferior solution with space complexity O(n) is copying ListNode to an ArrayList, and use two pointers iterating from both side and do comparison
     */
    boolean isPalindrome(ListNode head) {
        if (head == null) {
            return true;
        }
        // Find the end of first half and reverse second half.
        ListNode firstHalfEnd = findEndOfFirstHalf(head);
        ListNode reversedSecondHead = reverseList(firstHalfEnd.next);

        // Check whether there is a palindrome.
        ListNode p1 = head;
        ListNode p2 = reversedSecondHead;
        boolean result = true;
        while (result && p2 != null) {
            // Compare the node from p1 and p2
            // p1(first half) may have the middle node at the end, so its length may be larger than p2 so we have that
            // covered in the while condition
            if (p1.val != p2.val) {
                result = false;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        // Restore the second half list and return the result.
        firstHalfEnd.next = reverseList(reversedSecondHead);
        return result;
    }

    /**
     * Two runners pointer technique TODO: Memorize this!
     * we have 2 runners one fast and one slow, running down the nodes of the Linked List.
     * In each second, the fast runner moves down 2 nodes, and the slow runner just 1 node.
     * By the time the fast runner gets to the end of the list, the slow runner will be half way.
     * If there is an odd-number of nodes, then the "middle" node should remain attached to the first half.
     * For example:
     * [1,2,3,4,5] ---> [1,2,(S->)3], [4,(F->)5]
     * [1,2,3,4] ---> [1,(S->)2], [3,(F->)4]
     */
    ListNode findEndOfFirstHalf(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast.next != null && fast.next.next != null) {// TODO: key
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    /**
     * Linked List Cycle
     * Given head, the head of a linked list, determine if the linked list has a cycle in it.
     * There is a cycle in a linked list if there is some node in the list that can be reached again by continuously
     * following the next pointer. Internally, pos is used to denote the index of the node that tail's next pointer is
     * connected to. Note that pos is not passed as a parameter.
     * Return true if there is a cycle in the linked list. Otherwise, return false
     * <p>
     * Input: head = [3,2,0,-4], pos = 1
     * Output: true
     * <p>
     * Input: head = [1,2], pos = 0
     * Output: true
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

    /**
     * Floyd's Cycle Finding Algorithm
     * Maintain two pointers at different speed, slow one move one step at a time while the fast pointer moves two steps at a time.
     * If the cycle exists, the fast pointer will eventually meet the slow one. Thus return true.
     * If the fast pointer is null or the next node of the fiast pointer is null(when the number of nodes is even), there is no cycle
     * Time Complexity: O(n). Space Complexity: O(1)
     */
    boolean hasCycle(ListNode head) {
        if (head == null)
            return false;
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null) { // TODO: key
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast)
                return true;
        }
        return false;
    }

    /**
     * Going through each node one by one and record each node's reference (or memory address) in a hash table
     * Time Complexity: O(n). Space Complexity: O(n)
     */
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

    /**
     * Linked List Cycle II
     * Given the head of a linked list, return the node where the cycle begins. If there is no cycle, return null.
     * <p>
     * There is a cycle in a linked list if there is some node in the list that can be reached again by
     * continuously following the next pointer. Internally, pos is used to denote the index of the node that
     * tail's next pointer is connected to (0-indexed). It is -1 if there is no cycle. Note that pos is not
     * passed as a parameter.
     * <p>
     * Do not modify the linked list.
     * <p>
     * Input: head = [3,2,0,-4], pos = 1
     * Output: tail connects to node index 1
     * Explanation: There is a cycle in the linked list, where tail connects to the second node.
     * <p>
     * https://leetcode.com/problems/linked-list-cycle-ii/description/
     */
    @Test
    void testDetectCycle() {
        ListNode headNode1 = createListNode(4);
        headNode1.next.next.next = headNode1.next;
        Assertions.assertThat(detectCycle(headNode1).val).isEqualTo(2);
        ListNode headNode2 = createListNode(5);
        Assertions.assertThat(detectCycle(headNode2)).isNull();
    }

    /**
     * Use Floyd's Tortoise and Hare Algorithm to detect linked list cycle and entry point
     * <p>
     * Algo:
     * 1. Initialize the slow and fast pointers to the head of the linked list.
     * 2. Move the slow one step and the fast two steps at a time until they meet or either fast or fast.next becomes null.
     * 3. If the hare or hare.next pointer is null, it means the fast came to the dead end and we return null as there is no cycle.
     * 4. Reset the fast pointer to the head of the linked list.
     * 5. Move both pointers one step at a time until they meet again. The meeting point is the node where the cycle begins.
     * 6. Return the meeting point node.
     * <p>
     * Time Complexity: O(n).
     * Space Complexity: O(1)
     */
    ListNode detectCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        boolean hasCycle = false;
        // Phase 1: Detect cycle
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }
        if (!hasCycle)
            return null;
        // Phase 2: Find the cycle entrance
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    /**
     * Add Two Numbers
     * You are given two non-empty linked lists representing two non-negative integers.
     * The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
     * <p>
     * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
     * <p>
     * Input: l1 = [2,4,3], l2 = [5,6,4]
     * Output: [7,0,8]
     * Explanation: 342 + 465 = 807.
     * https://leetcode.com/problems/add-two-numbers/description/
     */
    @Test
    void testAddTwoNumbers() {
        ListNode headNode1 = createListNode(3);
        headNode1.val = 2;
        headNode1.next.val = 4;
        headNode1.next.next.val = 3;

        ListNode headNode2 = createListNode(3);
        headNode2.val = 5;
        headNode2.next.val = 6;
        headNode2.next.next.val = 4;

        ListNode node = addTwoNumbers(headNode1, headNode2);
        Assertions.assertThat(node.val).isEqualTo(7);
        Assertions.assertThat(node.next.val).isEqualTo(0);
        Assertions.assertThat(node.next.next.val).isEqualTo(8);
    }

    /**
     * Iterate two lists and create new node for the retruning list
     * 1. Need to keep track of the carry when we sum up the valuses from two lists
     * 2. The loop won't termintae until we reach the ends of both lists and carry is also 0
     * Pseudocode
     * - Initialize current node to dummy head of the returning list.
     * - Initialize carry to 0.
     * - Loop through lists l1 and l2 until you reach both ends and carry is 0.
     * -- Set x to node l1's value. If l1 has reached the end of l1, set to 0.
     * -- Set y to node l2's value. If l2 has reached the end of l2, set to 0.
     * -- Set sum = x + y + carry.
     * -- Update carry = sum / 10.
     * -- Create a new node with the digit value of (sum mod 10) and set it to current node's next,
     * then advance current node to next.
     * -- Advance both l1 and l2.
     * - Return dummy head's next node.
     * Time Complexity: O(max(m, n)), where m and n represents the length of l1 and l2
     * Space Complexity: O(1)
     */
    ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode currPtr = dummyHead;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) { // Be careful of the carry != 0 condition
            int x = (l1 != null) ? l1.val : 0;
            int y = (l2 != null) ? l2.val : 0;
            int sum = x + y + carry;
            carry = sum / 10;
            currPtr.next = new ListNode(sum % 10);
            currPtr = currPtr.next;
            if (l1 != null)
                l1 = l1.next;
            if (l2 != null)
                l2 = l2.next;
        }
        return dummyHead.next;
    }

    /**
     * Odd Even Linked List
     * Given the head of a singly linked list, group all the nodes with odd indices together followed by the nodes
     * with even indices, and return the reordered list.
     * <p>
     * The first node is considered odd, and the second node is even, and so on.
     * Note that the relative order inside both the even and odd groups should remain as it was in the input.
     * You must solve the problem in O(1) extra space complexity and O(n) time complexity.
     * https://leetcode.com/problems/odd-even-linked-list/description/
     */
    @Test
    void testOddEvenList() {
        ListNode headNode1 = createListNode(5);
        headNode1.val = 1;
        headNode1.next.val = 2;
        headNode1.next.next.val = 3;
        headNode1.next.next.next.val = 4;
        headNode1.next.next.next.next.val = 5;
        ListNode node = oddEvenList(headNode1);
        Assertions.assertThat(node.val).isEqualTo(1);
        Assertions.assertThat(node.next.val).isEqualTo(3);
        Assertions.assertThat(node.next.next.val).isEqualTo(5);
        Assertions.assertThat(node.next.next.next.val).isEqualTo(2);
        Assertions.assertThat(node.next.next.next.next.val).isEqualTo(4);
    }

    /**
     * Put the odd nodes in a linked list and the even nodes in another. Then link the evenList to the tail of the oddList.
     * In the beginning, we need three ptrs, odd -> head, evenHead -> head.next, even -> head.next.
     * evenHead doesn't move and we will set the odd.next to it in the end.
     * Then we will start to connect odd.next to even.next and move the odd as well.
     * Same thing as the even pth as long as even and even.next is not null.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    ListNode oddEvenList(ListNode head) {
        if (head == null)
            return null;
        ListNode odd = head, even = head.next, evenHead = head.next;
        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = even.next;
            even.next = odd.next;
            even = odd.next;
        }
        odd.next = evenHead;
        return head;
    }

    /**
     * Intersection of Two Linked Lists
     * Given the heads of two singly linked-lists headA and headB, return the node at which the two lists intersect.
     * If the two linked lists have no intersection at all, return null.
     * <p>
     * Input: intersectVal = 8, listA = [4,1,8,4,5], listB = [5,6,1,8,4,5], skipA = 2, skipB = 3
     * Output: Intersected at '8'
     * Explanation: The intersected node's value is 8 (note that this must not be 0 if the two lists intersect).
     * From the head of A, it reads as [4,1,8,4,5]. From the head of B, it reads as [5,6,1,8,4,5].
     * There are 2 nodes before the intersected node in A; There are 3 nodes before the intersected node in B.
     * - Note that the intersected node's value is not 1 because the nodes with value 1 in A and B (2nd node in A
     * and 3rd node in B) are different node references. In other words, they point to two different locations in memory,
     * while the nodes with value 8 in A and B (3rd node in A and 4th node in B) point to the same location in memory.
     * https://leetcode.com/problems/intersection-of-two-linked-lists/description/
     */
    @Test
    void testGetIntersectionNode() {
        ListNode headNode1 = createListNode(2);
        headNode1.val = 4;
        headNode1.next.val = 1;
        ListNode eight = new ListNode(8);
        headNode1.next.next = eight;
        ListNode four = new ListNode(4);
        headNode1.next.next.next = four;
        ListNode five = new ListNode(5);
        headNode1.next.next.next.next = five;

        ListNode headNode2 = createListNode(6);
        headNode2.val = 5;
        headNode2.next.val = 6;
        headNode2.next.next.val = 1;
        headNode2.next.next.next = eight;
        headNode2.next.next.next.next = four;
        headNode2.next.next.next.next.next = five;
        Assertions.assertThat(getIntersectionNode(headNode1, headNode2)).isEqualTo(eight);
    }

    /*
     * If we say that c is the shared part, a is exclusive part of list A and b is exclusive part of list B,
     * then we can have one pointer that goes over a + c + b and the other that goes over b + c + a.
     *         |------- a -------|
     *                           |------- c -------|
     *
     * |----------- b -----------|
     * In this case, even tho two lists have different length, if there is intersection between them,
     * they will eventually meet at that point after both ptrs travel a+b+c distance. If there is not,
     * both of them will at null after a+b distance.
     * Time Complexity: O(n + m), n be the list A length, m be the list B length.
     * Space Complexity: O(1)
     */
    ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode ptrA = headA, ptrB = headB;
        while (ptrA != ptrB) {
            ptrA = ptrA == null ? headB : ptrA.next;
            ptrB = ptrB == null ? headA : ptrB.next;
        }
        return ptrB;
        // Note: In the case lists do not intersect, the pointers for A and B
        // will still line up in the 2nd iteration, just that here won't be
        // a common node down the list and both will reach their respective ends
        // at the same time. So pA will be NULL in that case.
    }

    /**
     * Merge k Sorted Lists
     * You are given an array of k linked-lists lists, each linked-list is sorted in ascending order.
     * <p>
     * Merge all the linked-lists into one sorted linked-list and return it.
     * <p>
     * Input: lists = [[1,4,5],[1,3,4],[2,6]]
     * Output: [1,1,2,3,4,4,5,6]
     * Explanation: The linked-lists are:
     * [
     * 1->4->5,
     * 1->3->4,
     * 2->6
     * ]
     * merging them into one sorted list:
     * 1->1->2->3->4->4->5->6
     * <p>
     * https://leetcode.com/problems/merge-k-sorted-lists/description/
     */
    @Test
    void testMergeKLists() {
        ListNode headNode1 = createListNode(3);
        headNode1.val = 1;
        headNode1.next.val = 4;
        headNode1.next.next.val = 5;

        ListNode headNode2 = createListNode(3);
        headNode2.val = 1;
        headNode2.next.val = 3;
        headNode2.next.next.val = 4;

        ListNode headNode3 = createListNode(2);
        headNode3.val = 2;
        headNode3.next.val = 6;


        ListNode[] input = {headNode1, headNode2, headNode3};
        ListNode nodes = mergeKLists(input);
//        ListNode nodes = mergeKListsDivideConquer(input);

        Assertions.assertThat(nodes.val).isEqualTo(1);
        Assertions.assertThat(nodes.next.val).isEqualTo(1);
        Assertions.assertThat(nodes.next.next.val).isEqualTo(2);
        Assertions.assertThat(nodes.next.next.next.val).isEqualTo(3);
        Assertions.assertThat(nodes.next.next.next.next.val).isEqualTo(4);
        Assertions.assertThat(nodes.next.next.next.next.next.val).isEqualTo(4);
        Assertions.assertThat(nodes.next.next.next.next.next.next.val).isEqualTo(5);
        Assertions.assertThat(nodes.next.next.next.next.next.next.next.val).isEqualTo(6);
    }

    /**
     * Add the head of each LinkedList to MinHeap first, then remove the top node from the heap and append to the result list,
     * then add its next node to the heap if it is not null.
     * <p>
     * The idea is we want to keep only one node from each LinkedList in the heap at one time, and take advantage of
     * sorting and retrieving the min value at constant time from the heap.
     * <p>
     * Time Complexity: O(Nlogk) where k is the number of linked lists, n be the total number of nodes
     * Space complexity: O(k)
     */
    ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0)
            return null;
        Queue<ListNode> minHeap = new PriorityQueue<>(Comparator.comparingInt(x -> x.val));
        for (ListNode node : lists) {
            if (node != null)
                // Only add the head nodes of each LinkedList to the heap to limit heap size to k
                minHeap.offer(node);
        }
        ListNode head = new ListNode(0);
        ListNode tail = head;
        while (!minHeap.isEmpty()) {
            tail.next = minHeap.poll();
            tail = tail.next;
            if (tail.next != null)
                // Now we add the next node to the heap
                minHeap.offer(tail.next);
        }
        return head.next;
    }

    /**
     * Leverage the solution of "Merge Two Sorted Lists" problem, and we take one pair of lists and call that helper method
     * to merge into one list. At each iteration, we can have k lists reduce to k/2 merged list, then k/4 until single list.
     * <p>
     * Time Complexity: O(Nlogk) where k is the number of linked lists, n be the total number of nodes
     * We can merge two sorted linked list in O(n) time where n is the total number of nodes in two lists.
     * Sum up the merge process and we can get O(Nlogk)
     * Space complexity: O(1)
     */
    ListNode mergeKListsDivideConquer(ListNode[] lists) {
        int listCount = lists.length;
        if (listCount == 0)
            return null;
        if (listCount == 1)
            return lists[0];

        while (listCount != 1) {
            // The listCount is reduced half after each iteration of merging every pair of lists
            if (listCount % 2 != 0) {
                // If odd-number of lists, merge first list with the last list first
                // we leverage the mergeTwoLists method built for the "Merge Two Sorted Lists" problem
                lists[0] = mergeTwoLists(lists[0], lists[listCount - 1]);
            }

            int j = 0;
            for (int i = 0; i < listCount - 1; i = i + 2) {
                // merge each pair of lists into one
                // j is increment by 1, it is ok to store the merged list in the previous list cuz it was already merged at the previous iteration
                lists[j++] = mergeTwoLists(lists[i], lists[i + 1]);
            }
            listCount = listCount / 2; // now we reduce the number of lists half
        }
        return lists[0];
    }

    /**
     * Copy List with Random Pointer
     * A linked list of length n is given such that each node contains an additional random pointer, which
     * could point to any node in the list, or null.
     * <p>
     * Construct a deep copy of the list. The deep copy should consist of exactly n brand new nodes, where
     * each new node has its value set to the value of its corresponding original node. Both the next and
     * random pointer of the new nodes should point to new nodes in the copied list such that the pointers
     * in the original list and copied list represent the same list state. None of the pointers in the new
     * list should point to nodes in the original list.
     * <p>
     * For example, if there are two nodes X and Y in the original list, where X.random --> Y, then for the
     * corresponding two nodes x and y in the copied list, x.random --> y.
     * <p>
     * Return the head of the copied linked list.
     * <p>
     * The linked list is represented in the input/output as a list of n nodes. Each node is represented as
     * a pair of [val, random_index] where:
     * <p>
     * val: an integer representing Node.val
     * random_index: the index of the node (range from 0 to n-1) that the random pointer points to, or null
     * if it does not point to any node.
     * <p>
     * Your code will only be given the head of the original linked list.
     * <p>
     * Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
     * Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]
     * <p>
     * Input: head = [[3,null],[3,0],[3,null]]
     * Output: [[3,null],[3,0],[3,null]]
     * <p>
     * https://leetcode.com/problems/copy-list-with-random-pointer/description/
     */
    @Test
    void testCopyRandomList() {
        Node node1 = new Node(7);
        Node node2 = new Node(13);
        Node node3 = new Node(11);
        node1.next = node2;
        node1.random = null;
        node2.next = node3;
        node2.random = node1;
        node3.random = node2;
        Node copy = copyRandomList(node1);
        Assertions.assertThat(copy.val).isEqualTo(7);
        Assertions.assertThat(copy.next.val).isEqualTo(13);
        Assertions.assertThat(copy.next.next.val).isEqualTo(11);
        Assertions.assertThat(copy.next.next.next).isNull();
        Assertions.assertThat(copy.random).isNull();
        Assertions.assertThat(copy.next.random).isEqualTo(copy);
        Assertions.assertThat(copy.next.next.random).isEqualTo(copy.next);
    }

    private class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    /**
     * Iterate the given Linked List first to create the new node and put in the oldNodeToNewNode Map.
     * Then iterate the original list again to set the next and random ptr value of new nodes using the map.
     * <p>
     * Time Complexity: O(n)
     * Space complexity: O(n)
     */
    Node copyRandomList(Node head) {
        if (head == null)
            return null;
        Map<Node, Node> oldNodeToNewNode = new HashMap<>();
        Node current = head;
        // loop 1: make copy of every nodes
        while (current != null) {
            oldNodeToNewNode.put(current, new Node(current.val));
            current = current.next;
        }
        // loop 2: assign next and random pointers by looking up the map
        current = head;
        while (current != null) {
            Node newNode = oldNodeToNewNode.get(current);
            newNode.next = oldNodeToNewNode.get(current.next);
            newNode.random = oldNodeToNewNode.get(current.random);
            current = current.next;
        }
        return oldNodeToNewNode.get(head);
    }

    /**
     * Iterate the list and create the new node and place it in between the current and the next one(A->A'->B->B'...)
     * Iterate again to set the random ptr for the new nodes.
     * Iterate third time to split the old and new nodes into the original and new list
     * <p>
     * Note: this solution CHANGES the original list during the execution
     * <p>
     * Time Complexity: O(n)
     * Space complexity: O(1)
     */
    Node copyRandomListII(Node head) {
        if (head == null)
            return null;

        // Creating a new weaved list of original and copied nodes.
        Node ptr = head;
        while (ptr != null) {
            // Cloned node
            Node newNode = new Node(ptr.val);
            // Inserting the cloned node just next to the original node.
            // If A->B->C is the original linked list,
            // Linked list after weaving cloned nodes would be A->A'->B->B'->C->C'
            newNode.next = ptr.next;
            ptr.next = newNode;
            ptr = newNode.next;
        }

        ptr = head;
        // Now link the random pointers of the new nodes created.
        // Iterate the newly created list and use the original nodes' random pointers,
        // to assign references to random pointers for cloned nodes.
        while (ptr != null) {
            ptr.next.random = (ptr.random != null) ? ptr.random.next : null;
            ptr = ptr.next.next;
        }

        // Unweave the linked list to get back the original linked list and the cloned list.
        // i.e. A->A'->B->B'->C->C' would be broken to A->B->C and A'->B'->C'
        Node oldListPtr = head; // A->B->C
        Node newListPtr = head.next; // A'->B'->C'
        Node oldHead = head.next;
        while (oldListPtr != null) {
            oldListPtr.next = oldListPtr.next.next;
            newListPtr.next = (newListPtr.next != null) ? newListPtr.next.next : null;
            oldListPtr = oldListPtr.next;
            newListPtr = newListPtr.next;
        }
        return oldHead;
    }

    /**
     * Middle of the Linked List
     * Given the head of a singly linked list, return the middle node of the linked list.
     * <p>
     * If there are two middle nodes, return the second middle node.
     * <p>
     * Input: head = [1,2,3,4,5]
     * Output: [3,4,5]
     * Explanation: The middle node of the list is node 3.
     * <p>
     * Input: head = [1,2,3,4,5,6]
     * Output: [4,5,6]
     * Explanation: Since the list has two middle nodes with values 3 and 4, we return the second one.
     */
    @Test
    void testMiddleNode() {
        ListNode headNode1 = createListNode(5);
        headNode1.val = 1;
        headNode1.next.val = 2;
        headNode1.next.next.val = 3;
        headNode1.next.next.next.val = 4;
        headNode1.next.next.next.next.val = 5;
        ListNode headNode2 = createListNode(6);
        headNode2.val = 1;
        headNode2.next.val = 2;
        headNode2.next.next.val = 3;
        headNode2.next.next.next.val = 4;
        headNode2.next.next.next.next.val = 5;
        headNode2.next.next.next.next.next.val = 6;
        ListNode node = middleNode(headNode1);
        Assertions.assertThat(node.val).isEqualTo(3);
        Assertions.assertThat(node.next.val).isEqualTo(4);
        Assertions.assertThat(node.next.next.val).isEqualTo(5);
        Assertions.assertThat(node.next.next.next).isNull();
        node = middleNode(headNode2);
        Assertions.assertThat(node.val).isEqualTo(4);
        Assertions.assertThat(node.next.val).isEqualTo(5);
        Assertions.assertThat(node.next.next.val).isEqualTo(6);
        Assertions.assertThat(node.next.next.next).isNull();
    }

    /**
     * Use Fast and Slow Pointer approach, fast ptr goes 2 steps and slow goes 1 step at a time when the fast ptr and its
     * next is not null. The slow ptr in the end is the mid-point.
     * <p>
     * Time Complexity: O(N), where NNN is the number of nodes in the given list.
     * Space Complexity: O(1), the space used by slow and fast.
     */
    ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * Reorder List
     * You are given the head of a singly linked-list. The list can be represented as:
     * <p>
     * L0 → L1 → … → Ln - 1 → Ln
     * Reorder the list to be on the following form:
     * <p>
     * L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
     * You may not modify the values in the list's nodes. Only nodes themselves may be changed.
     * <p>
     * Input: head = [1,2,3,4]
     * Output: [1,4,2,3]
     * <p>
     * Input: head = [1,2,3,4,5]
     * Output: [1,5,2,4,3]
     * <p>
     * https://leetcode.com/problems/reorder-list/description/
     */
    @Test
    void testReorderList() {
        ListNode headNode1 = createListNode(4);
        headNode1.val = 1;
        headNode1.next.val = 2;
        headNode1.next.next.val = 3;
        headNode1.next.next.next.val = 4;

        ListNode headNode2 = createListNode(5);
        headNode2.val = 1;
        headNode2.next.val = 2;
        headNode2.next.next.val = 3;
        headNode2.next.next.next.val = 4;
        headNode2.next.next.next.next.val = 5;

        reorderList(headNode1);
        Assertions.assertThat(headNode1.val).isEqualTo(1);
        Assertions.assertThat(headNode1.next.val).isEqualTo(4);
        Assertions.assertThat(headNode1.next.next.val).isEqualTo(2);
        Assertions.assertThat(headNode1.next.next.next.val).isEqualTo(3);
        reorderList(headNode2);
        Assertions.assertThat(headNode2.val).isEqualTo(1);
        Assertions.assertThat(headNode2.next.val).isEqualTo(5);
        Assertions.assertThat(headNode2.next.next.val).isEqualTo(2);
        Assertions.assertThat(headNode2.next.next.next.val).isEqualTo(4);
        Assertions.assertThat(headNode2.next.next.next.next.val).isEqualTo(3);
    }

    /**
     * First find the middle of the linked list, then reverse the second part of the list, then merge two lists.
     * This problem is a combination of three easy problems:
     * 1. Find Middle of the Linked List
     * If there are two middle nodes, return the second middle node.
     * Example: for the list 1->2->3->4->5->6, the middle element is 4.
     * <p>
     * 2. Reverse Linked List
     * Once a middle node has been found, reverse the second part of the list.
     * Example: convert 1->2->3->4->5->6 into 1->2->3->4 and 6->5->4.
     * <p>
     * 3. Merge Two Lists.
     * Example: merge 1->2->3->4 and 6->5->4 into 1->6->2->5->3->4.
     * <p>
     * Time complexity: O(N)
     * Space complexity: O(1)
     */
    void reorderList(ListNode head) {
        // Find the middle
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // Now slow ptr is the head of the 2nd part

        // Reverse the second part of the list
        ListNode prev = null, current = slow;
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        // Now prev is the head of reversed 2nd part

        // Merge two lists
        ListNode firstCurrent = head, secondCurrent = prev;
        while (secondCurrent.next != null) {
            // The terminate condition is VERY crucial
            // After reverse the second part, the next ptr of the last node of the first part is still point to the last node of the
            // second part. So we need to consider the two ptr positions at two different condition when we finished the merge
            // Odd-sized list
            // 1 -----> 4 -----> 2 (firstCurrent) -----> 3 (secondCurrent) -----> null
            // Even-sized list
            // 1 -----> 5 -----> 2 -----> 4 -----> 3 (firstCurrent & secondCurrent) -----> null
            ListNode firstNext = firstCurrent.next;
            ListNode secondNext = secondCurrent.next;
            firstCurrent.next = secondCurrent;
            secondCurrent.next = firstNext;
            firstCurrent = firstNext;
            secondCurrent = secondNext;
        }
    }

    /**
     * Swap Nodes in Pairs
     * Given a linked list, swap every two adjacent nodes and return its head. You must solve the
     * problem without modifying the values in the list's nodes (i.e., only nodes themselves may be changed.)
     * <p>
     * Input: head = [1,2,3,4]
     * Output: [2,1,4,3]
     * <p>
     * Input: head = []
     * Output: []
     * <p>
     * Input: head = [1]
     * Output: [1]
     * <p>
     * https://leetcode.com/problems/swap-nodes-in-pairs/description/
     */
    @Test
    void testSwapPairs() {
        ListNode headNode1 = createListNode(4);
        headNode1.val = 1;
        headNode1.next.val = 2;
        headNode1.next.next.val = 3;
        headNode1.next.next.next.val = 4;
        headNode1 = swapPairs(headNode1);
        Assertions.assertThat(headNode1.val).isEqualTo(2);
        Assertions.assertThat(headNode1.next.val).isEqualTo(1);
        Assertions.assertThat(headNode1.next.next.val).isEqualTo(4);
        Assertions.assertThat(headNode1.next.next.next.val).isEqualTo(3);
    }

    /**
     * Iterate the linked list in steps of two nodes. Need another ptr, prevNode, to save the second node ptr after the swap,
     * so we can link the current swapped pair to the previous pair.
     * <p>
     * Time Complexity : O(N) where N is the size of the linked list.
     * <p>
     * Space Complexity : O(1)
     */
    ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode prevNode = dummy;
        while (head != null && head.next != null) {
            ListNode firstNode = head;
            ListNode secondNode = head.next;
            // Swap
            firstNode.next = secondNode.next;
            secondNode.next = firstNode;
            // Update prevNode
            prevNode.next = secondNode;
            prevNode = firstNode;
            // Move to process next pair
            head = firstNode.next;
        }
        return dummy.next;
    }
}
