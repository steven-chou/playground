package playground;

import javafx.util.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.data.Index.atIndex;

/**
 * TODO: Tips
 *   1. Java built-in sorting methods:
 *      - Arrays.sort(int[] a) uses dual-pivot Quicksort on primitives. It offers O(n log(n)) performance and is typically faster than
 *        traditional (one-pivot) Quicksort implementations. However, it uses a stable, adaptive, iterative implementation of mergesort
 *        algorithm for Array of Objects, i.e. Arrays.sort(Object[] a)
 *      - Collections.sort(List<T> list)/List.sort(Comparator). Same performance as Arrays.sort(Object[] a)
 *      - Comparable interface -> int compareTo(Obj)) & Comparator interface ->  int compare(ObjA, ObjB)
 *      - Comparator.comparing static method and Lambda
 *      https://www.baeldung.com/java-comparator-comparable
 *      https://www.baeldung.com/java-8-comparator-comparing
 *      https://www.baeldung.com/java-8-sort-lambda
 *   2. Java built-in binary search(Arrays.binarySearch OR Collections.binarySearch) has O(logN) time complexity.
 *      When the key is NOT found, it returns (-(insertion point) - 1). The insertion point is defined as the point at
 *      which the key would be inserted into the array: the index of the first element greater than the key,
 *      or a.length if all elements in the array are less than the specified key. Therefore, to get a positive insertion
 *      index from the result, you need (index < 0) ? -index - 1
 *   3. Generate random int
 *      new Random().nextInt(upperBound) -> Returns a pseudorandom, uniformly distributed value between 0 (inclusive) and the specified value, i.e. upperBound(EXCLUSIVE)
 *   4. Generate random int in a given range, [min(inclusive), max(exclusive)] *If need max inclusive, just do max+1
 *      new Random().nextInt(max - min) + min
 *      https://www.baeldung.com/java-generating-random-numbers-in-range
 */
public class SortingSearching {
    @Test
    void testBinarySearch() {
        int[] nums = new int[]{-1, 0, 3, 5, 9, 12};
        Assertions.assertThat(binarySearch(nums, 9)).isEqualTo(4);
    }

    /**
     * Time complexity: O(log n)
     * We reduce the search size to half at each iteration. n -> n/2 -> n/4 ->...
     * After k times, the size becomes n/2^k, and we want to find the k so the search size is reduced to a constant,
     * so we can make a decision. In other word, n/2^k = O(1)
     * --> n/2^k = 1 --> n = 2^k --> (Take log base 2 on both side) --> log n = log 2^k --> log n = k
     * Therefore, it is O(log n)
     * <p>
     * Space complexity: O(1)
     */
    int binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        // To avoid integer overflow (https://blog.research.google/2006/06/extra-extra-read-all-about-it-nearly.html)
        // Also can use unsigned right shit ops, x >>> 1, right-shifts one place, and the result is the same as x/2.
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target)
                left = mid + 1;
            else if (nums[mid] > target)
                right = mid - 1;
            else
                return mid;
        }
        return -1;
    }

    /**
     * Merge Sorted Array
     * https://leetcode.com/problems/merge-sorted-array/description/
     */
    @Test
    void testMerge() {
        int[] nums1 = new int[]{1, 2, 3, 0, 0, 0};
        int[] nums2 = new int[]{2, 5, 6};
        merge(nums1, 3, nums2, 3);
        Assertions.assertThat(nums1).containsExactly(1, 2, 2, 3, 5, 6);

        nums1 = new int[]{0};
        nums2 = new int[]{1};
        merge(nums1, 0, nums2, 1);
        Assertions.assertThat(nums1).containsExactly(1);
    }

    // TODO: Interview Tip: Whenever you're trying to solve an array problem in-place, always consider the possibility
    //  of iterating backwards instead of forwards through the array. It can completely change the problem, and make it a lot easier.

    /**
     * Three Pointers (Start From the End)*
     * Maintain 3 ptrs, p1 to point at index m - 1 of nums1, p2 to point at index n - 1 of nums2, and p to point at index m + n - 1 of nums1.
     * Then move p backwards through the array, each time writing the bigger value pointed at by p1 or p2 to nums1 where p is point at.
     * Time Complexity: O(n+m). Space Complexity: O(1)
     */
    void merge(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1;
        int p2 = n - 1;
        for (int p = m + n - 1; p >= 0; p--) {
            if (p1 >= 0 && p2 >= 0)
                // Take the bigger one and write it to nums1
                nums1[p] = nums1[p1] > nums2[p2] ? nums1[p1--] : nums2[p2--];
            else if (p1 >= 0) // p2 passed the head
                nums1[p] = nums1[p1--];
            else // p1 passed the head
                nums1[p] = nums2[p2--];
        }
    }

    /**
     * First Bad Version
     * https://leetcode.com/problems/first-bad-version/editorial/
     */
    @Test
    void testFirstBadVersion() {
        firstBadVersion(5);
    }

    /**
     * Use binary search to search space and cut in half each time
     * Time Complexity: O(log n). Space Complexity: O(1)
     */
    int firstBadVersion(int n) {
        int left = 1, right = n;
        while (left < right) {
            // IMPORTANT: Must calculate mid this way to avoid overflow
            // instead of (left+right) / 2
            int mid = left + (right - left) / 2;
            if (isBadVersion(mid))
                // mid may or may not be the first bad ver, so make the search range [left, mid](inclusive)
                right = mid;
            else
                // all ver including mid are good, so make the search range [mid+1, right](inclusive)
                left = mid + 1;
        }
        return left; // <-- key
    }

    boolean isBadVersion(int verNum) {
        return verNum == 4;
    }

    /**
     * Missing Number
     * https://leetcode.com/problems/missing-number/description/
     */
    @Test
    void testFindMissingNumber() {
        int[] input = new int[]{3, 0, 1};
        Assertions.assertThat(missingNumber(input)).isEqualTo(2);
    }

    /**
     * Sum approach
     * The question is actually to find the missing number from a continuous integer series. Since there is only one, we can
     * just compute the expected sum of the series and subtract the sum of the input integers then get the answer.
     * The sub-optimal solution is put all input numbers into a set, then have a loop starting from 0 to the length of input inclusive
     * and check if set has it. This approach has O(n) space complexity
     * Time Complexity: O(n). Space Complexity: O(1)
     */
    int missingNumber(int[] nums) {
        int expectedSum = 0;
        for (int i = 0; i <= nums.length; i++)
            expectedSum += i;
        // or we can just do expectedSum = nums.length * (nums.length + 1)/2;
        int actualSum = 0;
        for (int num : nums) {
            actualSum += num;
        }
        return expectedSum - actualSum;
    }

    /**
     * Sort Colors
     * Given an array nums with n objects colored red, white, or blue, sort them in-place so that objects of the same color are adjacent, with the colors in the order red, white, and blue.
     * <p>
     * We will use the integers 0, 1, and 2 to represent the color red, white, and blue, respectively.
     * <p>
     * You must solve this problem without using the library's sort function.
     * <p>
     * Input: nums = [2,0,2,1,1,0]
     * Output: [0,0,1,1,2,2]
     * https://leetcode.com/problems/sort-colors/description/
     */
    @Test
    void testSortColors() {
        int[] input = new int[]{2, 0, 2, 1, 1, 0};
        sortColors(input);
        Assertions.assertThat(input).containsExactly(0, 0, 1, 1, 2, 2);
    }

    /**
     * One Pass w/ three pointers (TRICKY!!)
     * Use three pointers to track the rightmost boundary of zeros(p0), the leftmost boundary of
     * twos(p2) and the current element under the consideration(curr).
     * The idea is cuz we want to move 0 and 2 to the two ends of the array and keep 1 in the middle,
     * while the curr ptr iterates the array, if we encounter 0 or 2, we swap curr with the p0 or p2 respectively
     * and also advance p0 or p2 as well. Since the array only has 0, 1, and 2, 1 will be all placed in the middle
     * after the end of iteration.
     * <p>
     * Algo:
     * 1. Initialise the rightmost boundary of zeros : p0 = 0.
     * During the algorithm execution nums[idx < p0] = 0.
     * 2. Initialise the leftmost boundary of twos : p2 = n - 1.
     * During the algorithm execution nums[idx > p2] = 2.
     * 3. Initialise the index of current element to consider : curr = 0.
     * 4. While curr <= p2 :
     * If nums[curr] = 0 : swap curr and p0 elements and
     * move both pointers to the right.
     * <p>
     * If nums[curr] = 2 : swap curr and p2 elements. Move
     * pointer p2 to the left.
     * (Do NOT move curr here, cuz curr pointed item can be anything and need to check)
     * <p>
     * If nums[curr] = 1 : move pointer curr to the right.
     * Time complexity : O(N)
     * Space complexity : O(1)
     */
    void sortColors(int[] nums) {
        int p0 = 0, p2 = nums.length - 1;
        int curr = 0;
        while (curr <= p2) {
            if (nums[curr] == 0) {
                int tmp = nums[p0];
                nums[p0] = nums[curr];
                nums[curr] = tmp;
                ++p0;
                ++curr;
            } else if (nums[curr] == 2) {
                int tmp = nums[p2];
                nums[p2] = nums[curr];
                nums[curr] = tmp;
                --p2;
                // We do NOT advance curr here cuz after the swap, nums[curr] can be anything,
                // so we need to check in the next iteration
            } else
                ++curr;
        }
    }

    /**
     * Top K Frequent Elements
     * <p>
     * Given an integer array nums and an integer k, return the k most frequent elements.
     * You may return the answer in any order.
     * <p>
     * Input: nums = [1,1,1,2,2,3], k = 2
     * Output: [1,2]
     * <p>
     * https://leetcode.com/problems/top-k-frequent-elements/description/
     */
    @Test
    void testTopKFrequent() {
        int[] input = new int[]{1, 1, 1, 2, 2, 3};
        Assertions.assertThat(topKFrequent(input, 2)).containsOnly(1, 2);
    }

    /**
     * Iterate array and create numberToFrequency Map, then put each number to the corresponding slot at the frequency array
     * (each array index is a List and contains the number(s) w/ the frequency number equal to the index)
     * 1. Iterate the array and build the number -> count map.
     * 2. Create a "bucket" array with the length (input array length + 1), which has List<Integer> for each slot.
     * 3. We use the bucket index to represent the possible count, and put the number from the map into the list
     * at its corresponding count index of the bucket.
     * 4. Iterate from the end of bucket array and collect and flatten out the numbers into a list
     * 5. Return the first k items from the list
     * <p>
     * Note: We use the "bucket" array to avoid the additional sorting(O(NlogN)) overhead
     * Time complexity : O(N)
     * Space complexity : O(N)
     */
    int[] topKFrequent(int[] nums, int k) {
        if (k == nums.length)
            return nums;
        Map<Integer, Integer> numToCount = new HashMap<>();
        for (int num : nums) {
            // Compute the count for each number
            numToCount.put(num, numToCount.getOrDefault(num, 0) + 1);
        }
        // The index of the countBuckets represents the possible count number [0...nums.length]
        // That's why we need to allocate one more slot for the array and 0 index won't be used.
        // Each bucket holds a list of the numbers whose count is equal to bucket index
        List<Integer>[] countBuckets = new List[nums.length + 1];
        for (int i = 0; i < countBuckets.length; i++) {
            countBuckets[i] = new ArrayList<>(); // init the List for each bucket
        }

        // Take the number from the map and put it to the associated count bucket
        numToCount.forEach((num, count) -> countBuckets[count].add(num));

        List<Integer> tmp = new ArrayList<>();
        for (int i = countBuckets.length - 1; i >= 0; i--) {
            // Collect the numbers from end of the bucket array
            tmp.addAll(countBuckets[i]);
        }

        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            // Collect the top K items
            result[i] = tmp.get(i);
        }
        return result;
    }

    /**
     * Kth Largest Element in an Array
     * Given an integer array nums and an integer k, return the kth largest element in the array.
     * Note that it is the kth largest element in the sorted order, not the kth distinct element.
     * <p>
     * Input: nums = [3,2,1,5,6,4], k = 2
     * Output: 5
     * <p>
     * Input: nums = [3,2,3,1,2,4,5,5,6], k = 4
     * Output: 4
     * <p>
     * https://leetcode.com/problems/kth-largest-element-in-an-array/description/
     */
    @Test
    void testFindKthLargest() {
        int[] input = new int[]{3, 2, 1, 5, 6, 4};
        Assertions.assertThat(findKthLargest(input, 2)).isEqualTo(5);
        Assertions.assertThat(findKthLargestMinHeap(input, 2)).isEqualTo(5);
        input = new int[]{3, 3, 3, 5, 5, 4};
        Assertions.assertThat(findKthLargest(input, 5)).isEqualTo(3);
    }

    /**
     * Quickselect
     * Use the modified Quickselect algo(Find the kth smallest element).
     * Define a quickSelect function, and we call it recursively to keep partitioning the input list into
     * three different sections until the answer is found.
     * <p>
     * First, we choose a random pivot index. We partition nums into 3 sections:
     * left is the section with elements greater than the pivot
     * mid is the section with elements equal to the pivot
     * right is the section with elements less than the pivot
     * * We change the left and right section definition from the original algo to adapt to our question.
     * <p>
     * If k <= left.length, return quickSelect(left, k).
     * If left.length + mid.length < k, return quickSelect(right, k - left.length - mid.length).
     * Otherwise, return pivot.
     * <p>
     * Time complexity : O(n) on average, O(n^2) in the worst case
     * Each call we make to quickSelect will cost O(n) since we need to iterate over nums to create left,
     * mid, and right. The number of times we call quickSelect is dependent on how the pivots are chosen.
     * The worst pivots to choose are the extreme (greatest/smallest) ones because they reduce our search
     * space by the least amount. Because we are randomly generating pivots, we may end up calling
     * quickSelect O(n) times, leading to a time complexity of O(n^2).
     * <p>
     * Space complexity : O(N), we need O(n) space to create left, mid, and right.
     * Other implementations of Quickselect(use in place swapping) can avoid creating these three in memory,
     * but in the worst-case scenario, those implementations would still require O(n) space for the recursion call stack.
     * ## Check LeetCode for the solution using in-place swap submitted by ppl
     */
    int findKthLargest(int[] nums, int k) {
        List<Integer> list = new ArrayList<>();
        for (int num : nums)
            list.add(num);
        return quickSelect(list, k);
    }

    int quickSelect(List<Integer> nums, int k) {
        int pivotIdx = new Random().nextInt(nums.size());
        int pivot = nums.get(pivotIdx);
        List<Integer> left = new ArrayList<>(); // elements greater than the pivot
        List<Integer> mid = new ArrayList<>(); // elements equal to the pivot
        List<Integer> right = new ArrayList<>(); // elements less than the pivot

        for (Integer num : nums) {
            if (num > pivot)
                left.add(num);
            else if (num < pivot)
                right.add(num);
            else
                mid.add(num);
        }

        if (k <= left.size())
            // Target is at left section, keep searching there
            return quickSelect(left, k);
        else if (k > left.size() + mid.size())
            // Target is at right section, search there BUT updating k cuz the elements in left and mid
            // are greater than the answer, deleting them means we must shift k.
            return quickSelect(right, k - left.size() - mid.size());
        else
            // Here it means k falls in the mid section, and the pivot we selected is there too, so we just return it.
            // (doesn't matter if there are duplicate values at mid)
            return pivot;
    }

    /**
     * Min-Heap
     * The problem is asking for the kth largest element. Iterate the array, and we first add elements to Min Heap until
     * it has K elements. Then, if the current element is larger than the Heap’s top element, delete the Heap’s top element,
     * and add the current element to the MinHeap. In the end, the top element in the Min Heap is the K-th largest element.
     * <p>
     * Time complexity: O(n⋅logk)
     * Operations on a heap cost logarithmic time relative to its size. Because our heap is limited to
     * the size of k, operation cost at most O(logk). We iterate over nums, performing one or two heap
     * operations at each iteration.
     * We iterate n times, performing up to logk work at each iteration, giving us a time complexity of O(n⋅logk).
     * <p>
     * Space complexity: O(k)
     * The heap uses O(k) space.
     */
    int findKthLargestMinHeap(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            if (minHeap.size() < k)
                minHeap.offer(num); // Add elements to Min Heap until K elements
            else {
                if (num > minHeap.peek()) {
                    // If the current element is larger than the Heap’s top element, delete the Heap’s top element,
                    // and add the current element to the Min Heap.
                    minHeap.poll();
                    minHeap.offer(num);
                }
            }
        } //At this point, heap has top k the largest elements and the smallest one is at the top
        return minHeap.peek();
    }

    /**
     * Find Peak Element
     * A peak element is an element that is strictly greater than its neighbors.
     * Given a 0-indexed integer array nums, find a peak element, and return its index. If the array contains multiple
     * peaks, return the index to any of the peaks.
     * You may imagine that nums[-1] = nums[n] = -∞. In other words, an element is always considered to be strictly greater
     * than a neighbor that is outside the array.
     * <p>
     * You must write an algorithm that runs in O(log n) time.
     * ** nums[i] != nums[i + 1] for all valid i.
     * Input: nums = [1,2,3,1]
     * Output: 2
     * Explanation: 3 is a peak element and your function should return the index number 2.
     * <p>
     * Input: nums = [1,2,1,3,5,6,4]
     * Output: 5
     * Explanation: Your function can return either index number 1 where the peak element is 2, or index number 5 where the peak element is 6.
     * <p>
     * https://leetcode.com/problems/find-peak-element/description/
     */
    @Test
    void testFindPeakElement() {
        int[] input = new int[]{1, 2, 3, 1};
        Assertions.assertThat(findPeakElement(input)).isEqualTo(2);
        input = new int[]{1};
        Assertions.assertThat(findPeakElement(input)).isEqualTo(0);
        findPeakElement(new int[]{4, 3, 2, 1});
    }

    /**
     * Binary Search
     * *Note: we can return ANY peak in the array and no duplicate value for the two adjacent items.
     * Implementation summary
     * Binary search but not search for pre-defined target.
     * - if mid's next element is greater than mid, set left = mid + 1 ==> search the right side
     * - if mid's last element is less than mid, set right = mid - 1 ==> search the left side
     * - else we are at the conditions
     * -- 1. mid is the biggest among its adjacent two neighbors, this is the peak
     * -- 2. left == right == mid, peak is at either head or tail of the array
     * continue until the loop ends
     * <p>
     * Observation:
     * 1. If the array is a descending list, head will be the peak
     * 2. If the array is an ascending list, tail will be the peak
     * 3. There can be 3 possible cases in terms of peak element in the array.
     * - Case 1: Peak lies on the right end(tail)
     * -- We compare the mid with the next element, if it is ascending, we explore the right part.
     * <p>
     * - Case 2: Peak lies on the left end(head)
     * -- We compare the mid with the previous element, if it is descending, we explore the left part.
     * <p>
     * - Case 3: Peak lies somewhere in the middle.
     * -- Apply the same logic as first two cases and shift the mid accordingly until find the peak
     * <p>
     * Algo:
     * We start off by finding the middle element, mid from the given nums array.
     * If this element happens to be lying in a descending sequence of numbers. or a local falling
     * slope(found by comparing nums[i] to its right neighbour), it means that the peak will always
     * lie towards the left of this element. Thus, we reduce the search space to the left of
     * mid(including itself) and perform the same process on left subarray.
     * <p>
     * If the middle element, mid, lies in an ascending sequence of numbers, or a rising
     * slope(determined by comparing nums[i] to its right neighbour), it obviously implies
     * that the peak lies towards the right of this element. Thus, we reduce the search space to the
     * right of mid and perform the same process on the right subarray.
     * <p>
     * In this way, we keep on reducing the search space till we eventually reach a state where only one element
     * is remaining in the search space. This single element is the peak element.
     * Time complexity: O(log n)
     * Space complexity : O(1)
     */
    int findPeakElement(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2; // Prevent (left + right) integer overflow
            if (mid < nums.length - 1 && nums[mid] < nums[mid + 1])
                left = mid + 1; // Continue to search right part
            else if (mid > 0 && nums[mid] < nums[mid - 1])
                right = mid - 1; // Continue to search left part
            else
                // Terminal condition can be
                // 1. mid is the max(peak) of the mid and its adjacent two neighbors
                // 2. left == right == mid, at either head or tail of the array
                return mid;
        }
        return -1;
    }

    /**
     * Find First and Last Position of Element in Sorted Array
     * Given an array of integers nums sorted in non-decreasing order, find the starting and ending
     * position of a given target value.
     * If target is not found in the array, return [-1, -1].
     * You must write an algorithm with O(log n) runtime complexity.
     * <p>
     * Input: nums = [5,7,7,8,8,10], target = 8
     * Output: [3,4]
     * <p>
     * Input: nums = [5,7,7,8,8,10], target = 6
     * Output: [-1,-1]
     * <p>
     * https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/description/
     */
    @Test
    void testSearchRange() {
        int[] input = new int[]{5, 7, 7, 8, 8, 10};
        Assertions.assertThat(searchRange(input, 8)).containsOnly(3, 4);
        input = new int[]{5, 7, 7, 8, 8, 10};
        Assertions.assertThat(searchRange(input, 6)).containsOnly(-1, -1);
    }

    /*
     * Two Binary Search
     * We need to do binary search for the first and the last element in the array respectively.
     *
     * The idea is we need to add extra logic to the common search function to determine if the found target
     * is the first/last item and update the high/lower bound to continue to search on the part of the array if needed.
     *
     * 1. Define a function that will take a flag to do either findFirst or findLast element logic.
     *    The caller will call it with true and false sequentially.
     * 2. At the condition that we find the target, i.e. nums[mid] == target, insdie the while loop.
     *    If findFirst
     *    	Two terminal conditions indicating the first element is found, so we can return the mid
     * 		    1. mid is the same as low, which means mid is the first element in the current search range cus it is already at the leftmost bound.
     *   		2. nums[mid - 1] is NOT equal to target. Cus array is ascending, it means mid must be the first element.
     *      Otherwise, we update high = mid - 1, to continue search on the left side for the first element
     *    If findLast(findFirst == false)
     *    	Two terminal conditions indicating the last element is found, so we can return the mid
     * 		1. mid is the same as high, which means mid is the last element in the current search range cus it is already at the rightmost bound.
     * 		2. nums[mid + 1] is NOT equal to target. Cus array is ascending, it means mid must be the last element.
     *    Otherwise, we update low = mid + 1, to continue search on the right side for the last element
     *
     * Time complexity: O(log n)
     * Space complexity : O(1)
     */
    int[] searchRange(int[] nums, int target) {
        int firstIdx = searchTargetIdx(nums, target, true);
        if (firstIdx == -1)
            return new int[]{-1, -1};
        int lastIdx = searchTargetIdx(nums, target, false);
        return new int[]{firstIdx, lastIdx};
    }

    int searchTargetIdx(int[] nums, int target, boolean findFirst) {
        int low = 0, high = nums.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] < target)
                low = mid + 1;
            else if (nums[mid] > target)
                high = mid - 1;
            else {
                // We found the target. Below is the extra thing to do besides the std binary search
                if (findFirst) {
                    if (mid == low || nums[mid - 1] != target)
                        // Terminal conditions indicating the first element is found
                        // 1. mid is the same as low, which means mid is the first element in the current search range cus it is already at the leftmost bound.
                        // 2. nums[mid - 1] is NOT equal to target. Cus array is ascending, it means mid must be the first element.
                        return mid;
                    high = mid - 1;
                    // Continue search on the left side for the first element.
                } else {
                    if (mid == high || nums[mid + 1] != target)
                        // Terminal conditions indicating the last element is found
                        // 1. mid is the same as high, which means mid is the last element in the current search range cus it is already at the rightmost bound.
                        // 2. nums[mid + 1] is NOT equal to target. Cus array is ascending, it means mid must be the last element.
                        return mid;
                    // Continue search on the right side for the last element.
                    low = mid + 1;
                }
            }
        }
        return -1;
    }

    /**
     * Merge Intervals
     * Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals,
     * and return an array of the non-overlapping intervals that cover all the intervals in the input.
     * <p>
     * Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
     * Output: [[1,6],[8,10],[15,18]]
     * Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
     * <p>
     * Input: intervals = [[1,4],[4,5]]
     * Output: [[1,5]]
     * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
     * <p>
     * https://leetcode.com/problems/merge-intervals/description/
     */
    @Test
    void testMergeIntervals() {
        // [[1,3],[2,6],[8,10],[15,18]]
        int[][] input = new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] result = merge(input); // [[1,6],[8,10],[15,18]]
        Assertions.assertThat(result).contains(new int[]{1, 6}, atIndex(0));
        Assertions.assertThat(result).contains(new int[]{8, 10}, atIndex(1));
        Assertions.assertThat(result).contains(new int[]{15, 18}, atIndex(2));
    }

    /**
     * Sorting
     * If we sort the intervals by their start value, then each set of intervals that can be
     * merged will appear as a contiguous "run" in the sorted list.
     * <p>
     * First, we sort the list. Then, we insert the first interval into our merged list and
     * continue considering each interval in turn as follows:
     * If the current interval begins after the previous interval ends, then they do not overlap,
     * and we can append the current interval to merged.
     * Otherwise, they do overlap, and we merge them by updating the end of the previous interval
     * if it is less than the end of the current interval.
     * <p>
     * Time complexity: O(n⋅log n), dominated by the sorting
     * Space complexity: O(n)
     */
    int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(x -> x[0]));
        // or Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> merged = new ArrayList<>();
        for (int[] interval : intervals) {
            if (merged.isEmpty() || interval[0] > merged.get(merged.size() - 1)[1]) {
                // 1. The first interval being processed. 2. Start of the current interval is after the last merged interval
                merged.add(interval); // No merge, just add to list
            } else {
                int[] lastMergedInterval = merged.get(merged.size() - 1);
                // Merge and take the larger interval end
                lastMergedInterval[1] = Math.max(lastMergedInterval[1], interval[1]);
            }
        }
        return merged.toArray(new int[0][]);
    }

    /**
     * Search in Rotated Sorted Array
     * There is an integer array nums sorted in ascending order (with distinct values).
     * <p>
     * Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length)
     * such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed).
     * For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].
     * <p>
     * Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums,
     * or -1 if it is not in nums.
     * <p>
     * You must write an algorithm with O(log n) runtime complexity.
     * All values of nums are unique.
     * <p>
     * Input: nums = [4,5,6,7,0,1,2], target = 0
     * Output: 4
     * <p>
     * Input: nums = [4,5,6,7,0,1,2], target = 3
     * Output: -1
     * https://leetcode.com/problems/search-in-rotated-sorted-array/description/
     */
    @Test
    void testSearchRotatedSortedArray() {
        int[] input = new int[]{4, 5, 6, 7, 0, 1, 2};
        Assertions.assertThat(search(input, 0)).isEqualTo(4);
        input = new int[]{4, 5, 6, 7, 0, 1, 2};
        Assertions.assertThat(search(input, 3)).isEqualTo(-1);
    }

    /**
     * Find Pivot Index w/ modified binary search + Binary Search on two separate parts
     * The key is to pinpoint the pivot position first.(the smallest element in array)
     * Ex, [4, 5, 6, 7, 0, 1] ==> peak: 0
     * We use a modified binary search alog and find the leftmost element that is smaller
     * than or equal to the last element in nums.
     * <p>
     * In binary search, when we have the mid, if nums[mid] > nums[length-1], it suggests that
     * the pivot value lies on the right of nums[mid]. We will then proceed with the right
     * half of the search space, which is [mid + 1 ~ right]
     * <p>
     * Otherwise, the pivot value is nums[mid] or it's situated to the left of nums[mid],
     * we continue with the left half of the searching space, which is [left ~ mid - 1].
     * <p>
     * In short, when the rotatation exists, the alog tries to move the left ptr once we
     * see mid is greater than the tail element. However, when all three pointers fall on the
     * same position, which means it is at the greatest element in array(right before the peak),
     * then left will be increment and the loops ends. That's why "left" is the peak.
     * <p>
     * After the peak is found, we perform two binary search on two different sections.
     * <p>
     * Time complexity: O(log n)
     * Space complexity: O(1)
     */
    int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        if (nums[right] > nums[0])
            // If the array is not rotated and the array is in ascending order, then last element > first element.
            return binarySearchWithBoundary(nums, left, right, target);

        // First find the index of the pivot element (the smallest element)
        // Ex, [4, 5, 6, 7, 0, 1] ==> peak: 0
        while (left <= right) {
            // Run this modified binary search algo until left passes right
            // left will be the peak we want
            // When nums is not rotated, left will fall on the head of array --> This use case is now handled in the beginning
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[nums.length - 1]) // This condition is the KEY!!
                // Search the right section
                left = mid + 1;
            else
                right = mid - 1;
        }
        // Binary search over elements on the pivot element's left
        int idx = binarySearchWithBoundary(nums, 0, left - 1, target);
        if (idx != -1)
            return idx;
        // Binary search over elements on the pivot element's right
        return binarySearchWithBoundary(nums, left, nums.length - 1, target);
    }

    int binarySearchWithBoundary(int[] nums, int leftBound, int rightBound, int target) {
        int left = leftBound, right = rightBound;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > target)
                right = mid - 1;
            else if (nums[mid] < target)
                left = mid + 1;
            else
                return mid;
        }
        return -1;
    }

    /**
     * Find Minimum in Rotated Sorted Array
     * Suppose an array of length n sorted in ascending order is rotated between 1 and n times.
     * For example, the array nums = [0,1,2,4,5,6,7] might become:
     * <p>
     * [4,5,6,7,0,1,2] if it was rotated 4 times.
     * [0,1,2,4,5,6,7] if it was rotated 7 times.
     * Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].
     * <p>
     * Given the sorted rotated array nums of unique elements, return the minimum element of this array.
     * <p>
     * You must write an algorithm that runs in O(log n) time.
     * <p>
     * Input: nums = [3,4,5,1,2]
     * Output: 1
     * Explanation: The original array was [1,2,3,4,5] rotated 3 times.
     * <p>
     * Input: nums = [4,5,6,7,0,1,2]
     * Output: 0
     * Explanation: The original array was [0,1,2,4,5,6,7] and it was rotated 4 times.
     * https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/description/
     */
    @Test
    void testFindMinhRotatedSortedArray() {
        int[] input = new int[]{3, 4, 5, 1, 2};
        Assertions.assertThat(findMin(input)).isEqualTo(1);
        input = new int[]{4, 5, 6, 7, 0, 1, 2};
        Assertions.assertThat(findMin(input)).isEqualTo(0);
    }

    /**
     * Modified Binary Search
     * This question is just the sub-problem of the "Search in Rotated Sorted Array".
     * We first implement a modified binary search to find the "pivot element" (the smallest element),
     * then do two binary search on two separate parts of array.
     * Here, we just need the code for finding the pivot index and return its value.
     * Time complexity: O(log n)
     * Space complexity: O(1)
     */
    int findMin(int[] nums) {
        int left = 0, right = nums.length - 1;
        if (nums[right] >= nums[0])
            // If the array is not rotated and the array is in ascending order, then last element > first element.
            return nums[0];

        // First find the index of the pivot element (the smallest element)
        // Ex, [4, 5, 6, 7, 0, 1] ==> peak: 0
        while (left <= right) {
            // Run this modified binary search algo until left passes right
            // left will be the peak we want
            // When nums is not rotated, left will fall on the head of array --> This use case is now handled in the beginning
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[nums.length - 1]) // This condition is the KEY!!
                // Search the right section
                left = mid + 1;
            else
                right = mid - 1;
        }
        return nums[left];
    }

    /**
     * Meeting Rooms
     * Given an array of meeting time intervals where intervals[i] = [starti, endi],
     * determine if a person could attend all meetings.
     * <p>
     * Input: intervals = [[0,30],[5,10],[15,20]]
     * Output: false
     * <p>
     * Input: intervals = [[7,10],[2,4]]
     * Output: true
     * https://leetcode.com/problems/meeting-rooms/description/
     */
    @Test
    void testCanAttendMeetings() {
        int[][] input = new int[][]{{0, 30}, {5, 10}, {15, 20}};
        Assertions.assertThat(canAttendMeetings(input)).isFalse();
        input = new int[][]{{7, 10}, {2, 4}};
        Assertions.assertThat(canAttendMeetings(input)).isTrue();
    }

    /**
     * Sorting
     * The idea here is to sort the meetings by starting time. Then, go through the meetings one by one and make
     * sure that each meeting ends before the next one starts.
     * <p>
     * Time complexity : O(nlogn)
     * The time complexity is dominated by sorting. Once the array has been sorted, only O(n) time is
     * taken to go through the array and determine if there is any overlap.
     * <p>
     * Space complexity : O(1).
     */
    boolean canAttendMeetings(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(x -> x[0]));
        for (int i = 0; i < intervals.length - 1; i++) {
            int currentEndTime = intervals[i][1];
            int nextStartTime = intervals[i + 1][0];
            if (currentEndTime > nextStartTime)
                return false;
        }
        return true;
    }

    /**
     * Meeting Rooms II
     * Given an array of meeting time intervals where intervals[i] = [starti, endi],
     * return the minimum number of conference rooms required.
     * <p>
     * Input: intervals = [[0,30],[5,10],[15,20]]
     * Output: 2
     * <p>
     * Input: intervals = [[7,10],[2,4]]
     * Output: 1
     * <p>
     * https://leetcode.com/problems/meeting-rooms-ii/description/
     */
    @Test
    void testMinMeetingRooms() {
        int[][] input = new int[][]{{0, 30}, {5, 10}, {15, 20}};
        Assertions.assertThat(minMeetingRooms(input)).isEqualTo(2);
        input = new int[][]{{7, 10}, {2, 4}};
        Assertions.assertThat(minMeetingRoomsMinHeap(input)).isEqualTo(1);
    }

    /**
     * Chronological Ordering Event
     * Imagine we plot every start and end time in a single timeline in chronological order. Instead of considering a meeting
     * (start/end time pair), we only consider each individual event and its meaning.
     * When we encounter an ending event, that means some meeting that started earlier has ended now. We are not
     * really concerned with which meeting has ended. All we need is that some meeting ended thus making a room available.
     * <p>
     * When we encounter a start event, we need to know the earliest end time of any meeting, so we can decide
     * if we need a meeting room.
     * <p>
     * Given two points above, we want to keep start and end time in separate array and sort them. So we can easily
     * compare each of them and keep track of the meeting end time.
     * <p>
     * We use two index to iterate the start and end time array at the same time, and compare one by one.
     * 1. For a given end time, the thing we know is there is a meeting ending and the room will be available if there is
     * a meeting started at or later than the end time. Thus, if the start time is greater than it, we can use the room.
     * 2. If the start time is earlier than the current end time, we need a meeting room for it.
     * <p>
     * Time Complexity: O(NlogN) because all we are doing is sorting the two arrays for start timings and end timings
     * individually and each of them would contain N elements considering there are N intervals.
     * <p>
     * Space Complexity: O(N) because we create two separate arrays of size N, one for recording the start times and one
     * for the end times.
     */
    int minMeetingRooms(int[][] intervals) {
        int[] start = new int[intervals.length];
        int[] end = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            start[i] = intervals[i][0];
            end[i] = intervals[i][1];
        }
        Arrays.sort(start);
        Arrays.sort(end);

        int usedRoomsCount = 0;
        for (int startIdx = 0, endIdx = 0; startIdx < intervals.length; startIdx++) {
            if (start[startIdx] < end[endIdx])
                // Current start time is earlier than end time, we need a meeting room.
                // The room counter starts at 0, so the first start time will always increment room count
                usedRoomsCount++;
            else
                // Current start time is equal or later than end time. so we can use the room just ended
                // But increment the next end time to track when the next meeting is available
                endIdx++;
        }
        return usedRoomsCount;
    }

    /**
     * MinHeap to track of the room availability based on the earliest end time
     * At any point in time we have multiple rooms that can be occupied and we don't really care which room
     * is free as long as we find one when required for a new meeting.
     * <p>
     * Instead of manually iterating on every room that's been allocated and checking if the room is
     * available or not, we can keep all the rooms in a min heap where the key for the min heap would be
     * the ending time of meeting.
     * <p>
     * So, every time we want to check if any room is free or not, simply check the topmost element of the
     * min heap as that would be the room that would get free the earliest out of all the other rooms
     * currently occupied.
     * <p>
     * If the room we extracted from the top of the min heap isn't free, then no other room is.
     * So, we can save time here and simply allocate a new room.
     * <p>
     * Time Complexity: O(NlogN)
     * Sorting of the array that takes O(NlogN) considering that the array consists of N elements.
     * With the min-heap. In the worst case, all N meetings will collide with each other.
     * In any case we have N add operations on the heap. In the worst case we will have N extract-min
     * operations as well. Overall complexity being (NlogN) since extract-min operation on a heap takes O(logN).
     * <p>
     * Space Complexity: O(N)
     * Constructing the min-heap and that can contain N elements in the worst case as
     * described above in the time complexity section. Hence, the space complexity is O(N).
     * <p>
     * This Min Heap solution uses more memory than the two index start/end time iteration approach above due to the
     * PriorityQueue
     */
    int minMeetingRoomsMinHeap(int[][] intervals) {
        // Sort the intervals by start time
        Arrays.sort(intervals, Comparator.comparingInt(x -> x[0]));
        // Each item in the MinHeap is the meeting room we currently use and its end time
        PriorityQueue<Integer> roomEndTimeMinHeap = new PriorityQueue<>();
        for (int[] interval : intervals) {
            int startTime = interval[0];
            if (!roomEndTimeMinHeap.isEmpty() && startTime >= roomEndTimeMinHeap.peek())
                // When the current meeting starts at or later than the meeting room ending the earliest in the MinHeap,
                // i.e. the top element, which means the meeting can take this room. So we remove it from heap to claim it.
                // (The room will be added to the heap w/ the updated end time later)
                roomEndTimeMinHeap.poll();
            // Add the meeting room w/ end time of this meeting to the heap, so we can keep track of the room
            // availability
            roomEndTimeMinHeap.offer(interval[1]);
        }
        return roomEndTimeMinHeap.size();
    }

    /**
     * Search a 2D Matrix
     * You are given an m x n integer matrix matrix with the following two properties:
     * <p>
     * Each row is sorted in non-decreasing order.
     * The first integer of each row is greater than the last integer of the previous row.
     * Given an integer target, return true if target is in matrix or false otherwise.
     * <p>
     * You must write a solution in O(log(m * n)) time complexity.
     * <p>
     * Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
     * Output: true
     * <p>
     * https://leetcode.com/problems/search-a-2d-matrix/description/
     */
    @Test
    void testSearchMatrix() {
        int[][] input = new int[][]{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        Assertions.assertThat(searchMatrix(input, 3)).isTrue();
        Assertions.assertThat(searchMatrix(input, 13)).isFalse();
    }

    /**
     * Binary Search (Binary Search (virtual row-concatenated array)
     * We can treat the matrix like single array(rows are virtually concatenated), so we can just run
     * binary search to find the target.
     * Only thing we need is to map the index on this virtual array to the cell in the matrix.
     * <p>
     * Given the idx on this virtual array and the matrix w/ m(rows) x n(cols)
     * rowIdx = idx / n
     * colIdx = idx % n
     * <p>
     * Time complexity: O(log(mn)) since it's a standard binary search.
     * <p>
     * Space complexity: O(1).
     */
    boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int left = 0, right = m * n - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midVal = matrix[mid / n][mid % n];
            if (midVal > target)
                right = mid - 1;
            else if (midVal < target)
                left = mid + 1;
            else
                return true;
        }
        return false;
    }

    /**
     * Search a 2D Matrix II
     * Write an efficient algorithm that searches for a value target in an m x n integer matrix.
     * This matrix has the following properties:
     * <p>
     * Integers in each row are sorted in ascending from left to right.
     * Integers in each column are sorted in ascending from top to bottom.
     * <p>
     * Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 5
     * Output: true
     * <p>
     * https://leetcode.com/problems/search-a-2d-matrix-ii/description/
     */
    @Test
    void testSearchMatrixII() {
        int[][] input = new int[][]{{1, 4, 7, 11, 15}, {2, 5, 8, 12, 19}, {3, 6, 9, 16, 22}, {10, 13, 14, 17, 24}, {18, 21, 23, 26, 30}};
        Assertions.assertThat(searchMatrixII(input, 5)).isTrue();
        Assertions.assertThat(searchMatrixII(input, 20)).isFalse();
    }

    /**
     * Search Space Reduction
     * - Start from bottom-left cell and move the pointer up or right by comparing the value to target.
     * (Taking advantage of sorted row/col, so we can prune unneeded elements)
     * <p>
     * First, we initialize a (row, col) pointer to the bottom-left of the matrix.
     * (top-right also work, but moving direction will be different).
     * If the currently-pointed-to value is larger than target we can move one row "up".
     * Cuz the rows are sorted from left-to-right, we know that every value to the right of the current
     * value is larger. Therefore, if the current value is already larger than target, we know that
     * every value to its right will also be too large.
     * Otherwise, if the currently-pointed-to value is smaller than target, we can move one column
     * "right".
     * <p>
     * Time complexity : O(n+m)
     * The key to the time complexity analysis is noticing that, on every
     * iteration (during which we do not return true) either row or col is
     * is decremented/incremented exactly once. Because row can only be
     * decremented m times and col can only be incremented n times
     * before causing the while loop to terminate, the loop cannot run for
     * more than n+m iterations. Because all other work is constant, the
     * overall time complexity is linear in the sum of the dimensions of the
     * matrix.
     * <p>
     * Space complexity : O(1)
     * Because this approach only manipulates a few pointers, its memory
     * footprint is constant.
     */
    boolean searchMatrixII(int[][] matrix, int target) {
        int rowNum = matrix.length, colNum = matrix[0].length;
        // start our "pointer" in the bottom-left, so when doing the search, we want either move up or right.
        // we can also start from the top-right, and search by moving down or left.
        int row = rowNum - 1;
        int col = 0;
        while (row >= 0 && col <= colNum - 1) {
            if (matrix[row][col] > target)
                // Move up cuz everything right to the current cell on the same row must be larger.
                --row;
            else if (matrix[row][col] < target)
                // Move right cuz everything above the current cell on the same column must be less.
                ++col;
            else
                // Found the target
                return true;
        }
        return false;
    }

    /**
     * K Closest Points to Origin
     * Given an array of points where points[i] = [xi, yi] represents a point on the X-Y plane and an integer k,
     * return the k closest points to the origin (0, 0).
     * <p>
     * The distance between two points on the X-Y plane is the Euclidean distance (i.e., √(x1 - x2)2 + (y1 - y2)2).
     * <p>
     * You may return the answer in any order. The answer is guaranteed to be unique (except for the order that it is in).
     * <p>
     * Input: points = [[1,3],[-2,2]], k = 1
     * Output: [[-2,2]]
     * Explanation:
     * The distance between (1, 3) and the origin is sqrt(10).
     * The distance between (-2, 2) and the origin is sqrt(8).
     * Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
     * We only want the closest k = 1 points from the origin, so the answer is just [[-2,2]].
     * https://leetcode.com/problems/k-closest-points-to-origin/
     */
    @Test
    void testKClosest() {
        int[][] input = new int[][]{{1, 3}, {-2, 2}};
        Assertions.assertThat(kClosest(input, 1)).contains(new int[]{-2, 2}, atIndex(0));
    }

    /**
     * Use the MinHeap to get the top K smallest element(distance to origin)
     * Algo:
     * 1. Construct a PriorityQueue<Pair<Integer, int[]>> w/ comparator to compare the distance (Pair's key)
     * 2. Add all elements into the Min Heap.
     * 3. Traversing and deleting the top element, and store the value into the result array T.
     * Repeat step 3 until we have removed the K smallest elements.
     * <p>
     * Note: LeetCode has another solution using QuickSelect algo, which gives O(N) time complexity
     * <p>
     * Time complexity: O(K⋅logN + N), where N is the total num of points
     * Adding all points to the heap takes O(N). Remove items from heap K times takes K⋅O(logN)
     * Thus O(K⋅logN + N)
     * Space complexity: O(N)
     * the heap will store all N elements.
     */
    int[][] kClosest(int[][] points, int k) {
        Comparator<Pair<Integer, int[]>> byDistance = Comparator.comparingInt(x -> x.getKey());
        PriorityQueue<Pair<Integer, int[]>> minHeap = new PriorityQueue<>(byDistance);
        for (int[] point : points) {
            // No need to do square root cuz it doesn't change the order
            minHeap.offer(new Pair<>(point[0] * point[0] + point[1] * point[1], point));
        }
        int[][] result = new int[k][2];
        int i = 0;
        while (k > 0) {
            result[i++] = minHeap.poll().getValue();
            k--;
        }
        return result;
    }
}
