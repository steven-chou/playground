package playground;

import javafx.util.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.data.Index.atIndex;

/**
 * TODO: Tips
 *   1. Java built-in sorting methods:
 *      - Arrays.sort(int[] a) uses dual-pivot Quicksort on primitives. It offers O(n⋅log(n)) performance and is typically faster than
 *        traditional (one-pivot) Quicksort implementations. However, it uses a stable, adaptive, iterative implementation of mergesort
 *        algorithm for Array of Objects, i.e. Arrays.sort(Object[] a).
 *        Space Complexity: O(n) - int, Object type
 *        https://stackoverflow.com/questions/22571586/will-arrays-sort-increase-time-complexity-and-space-time-complexity
 *      - Collections.sort(List<T> list)/List.sort(Comparator). Same performance as Arrays.sort(Object[] a)
 *      - Comparable interface -> int compareTo(Obj)) & Comparator interface ->  int compare(ObjA, ObjB)
 *      - Comparator using lambda
 *         Ex: Comparator<Integer> byDistance = (a, b) -> Integer.compare(Math.abs(a - x), Math.abs(b - x));
 *             ==> Comparator<Integer> byDistance = Comparator.comparingInt(a -> Math.abs(a - x));
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

    @Test
    void testMergeSort() {
        int[] nums = new int[]{5, 2, 3, 1};
        //Assertions.assertThat(mergeSort(nums)).containsExactly(1, 2, 3, 5);
        Assertions.assertThat(mergeSort(new int[]{5, 1, 1, 2, 0, 0})).containsExactly(0, 0, 1, 1, 2, 5);
    }

    /**
     * Time complexity: O(n⋅log n)
     * There are a total of N elements on each level in the recursion tree. Therefore, it takes O(N)
     * time for the merging process to complete on each level. And there are a total of logN levels.
     * Space complexity: O(n)
     * The recursive stack will take O(log n) space and we used additional array of size n when copying the split array.
     */
    int[] mergeSort(int[] nums) {
        // Base case is when a single element (which is already sorted)
        if (nums.length <= 1)
            return nums;

        int pivot = nums.length / 2;
        // Split array into two parts and recursively sort them
        int[] left = mergeSort(Arrays.copyOfRange(nums, 0, pivot));
        int[] right = mergeSort(Arrays.copyOfRange(nums, pivot, nums.length));
        // Combine the two arrays into one larger array
        return merge(left, right);
    }

    int[] merge(int[] leftArray, int[] rightArray) {
        int[] mergedArray = new int[leftArray.length + rightArray.length];
        int lPtr = 0, rPtr = 0;
        // Merge two arrays into a sorted array
        for (int i = 0; i < mergedArray.length; i++) {
            if (lPtr == leftArray.length) {
                // leftArray is exhausted, so keep adding the remaining elements from rightArray
                mergedArray[i] = rightArray[rPtr++];
            } else if (rPtr == rightArray.length) {
                // rightArray is exhausted, so keep adding the remaining elements from leftArray
                mergedArray[i] = leftArray[lPtr++];
            } else {
                // Take the smaller one from one of the arrays
                if (leftArray[lPtr] < rightArray[rPtr]) {
                    mergedArray[i] = leftArray[lPtr++];
                } else
                    mergedArray[i] = rightArray[rPtr++];
            }
        }
        return mergedArray;
    }

    /**
     * Merge Sorted Array
     * You are given two integer arrays nums1 and nums2, sorted in non-decreasing order, and two integers m
     * and n, representing the number of elements in nums1 and nums2 respectively.
     * <p>
     * Merge nums1 and nums2 into a single array sorted in non-decreasing order.
     * <p>
     * The final sorted array should not be returned by the function, but instead be stored inside the array nums1.
     * To accommodate this, nums1 has a length of m + n, where the first m elements denote the elements that should
     * be merged, and the last n elements are set to 0 and should be ignored. nums2 has a length of n.
     * <p>
     * Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
     * Output: [1,2,2,3,5,6]
     * Explanation: The arrays we are merging are [1,2,3] and [2,5,6].
     * The result of the merge is [1,2,2,3,5,6] with the underlined elements coming from nums1.
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
     * Maintain 3 ptrs(2 read, 1 write), r1 to point at index m - 1 of nums1, r2 to point at index n - 1 of nums2,
     * and w to point at index m + n - 1 of nums1. Then use ptr w to iterate nums1 array backward, for each index
     * point by w, set it to the bigger value of p1 or p2 and move decrement ptr accordingly. Need to consider
     * the condition that either p1 or p2 fall out of its array, i.e. p1 or p2 < 0
     * Time Complexity: O(n+m). Space Complexity: O(1)
     */
    void merge(int[] nums1, int m, int[] nums2, int n) {
        int r1 = m - 1;
        int r2 = n - 1;
        for (int w = m + n - 1; w >= 0; w--) {
            if (r1 >= 0 && r2 >= 0)
                // Take the bigger one and write it to nums1
                nums1[w] = nums1[r1] > nums2[r2] ? nums1[r1--] : nums2[r2--];
            else if (r1 >= 0) // r2 passed the head
                nums1[w] = nums1[r1--];
            else // r1 passed the head
                nums1[w] = nums2[r2--];
        }
    }

    /**
     * First Bad Version
     * You are a product manager and currently leading a team to develop a new product. Unfortunately, the latest
     * version of your product fails the quality check. Since each version is developed based on the previous version,
     * all the versions after a bad version are also bad.
     * <p>
     * Suppose you have n versions [1, 2, ..., n] and you want to find out the first bad one, which causes all the
     * following ones to be bad.
     * <p>
     * You are given an API bool isBadVersion(version) which returns whether version is bad. Implement a function
     * to find the first bad version. You should minimize the number of calls to the API.
     * <p>
     * Input: n = 5, bad = 4
     * Output: 4
     * Explanation:
     * call isBadVersion(3) -> false
     * call isBadVersion(5) -> true
     * call isBadVersion(4) -> true
     * Then 4 is the first bad version.
     * https://leetcode.com/problems/first-bad-version/editorial/
     */
    @Test
    void testFirstBadVersion() {
        firstBadVersionUseTemplate(5);
        firstBadVersion(5);
    }

    /**
     * Use the "Find First True" binary search template.
     * The condition is "isBadVersion(i) == true"
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    int firstBadVersionUseTemplate(int n) {
        // left -> invalid (0), right -> valid (1 to n)
        int left = 0, right = n;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (isBadVersion(mid))
                right = mid;
            else
                left = mid;
        }
        return right;
    }

    /**
     * Use std Find first true binary search style
     */
    int firstBadVersion(int n) {
        int left = 1, right = n;
        int ans = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (isBadVersion(mid)) {
                ans = mid;
                right = mid - 1;
            } else
                left = mid + 1;
        }
        return ans;
    }

    boolean isBadVersion(int verNum) {
        return verNum == 4;
    }

    /**
     * Missing Number
     * Given an array nums containing n distinct numbers in the range [0, n],
     * return the only number in the range that is missing from the array.
     * <p>
     * Input: nums = [3,0,1]
     * Output: 2
     * Explanation: n = 3 since there are 3 numbers, so all numbers are in the range [0,3].
     * 2 is the missing number in the range since it does not appear in nums.
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
        Assertions.assertThat(findKthLargestMaxHeap(input, 2)).isEqualTo(5);
        input = new int[]{3, 3, 3, 5, 5, 4};
        Assertions.assertThat(findKthLargest(input, 5)).isEqualTo(3);
        Assertions.assertThat(findKthLargestMaxHeap(input, 5)).isEqualTo(3);
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
     * Another solution using the MaxHeap to find the K-th largest element.
     * Time complexity: O(k⋅logn)
     * Space complexity: O(k)
     */
    int findKthLargestMaxHeap(int[] nums, int k) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        for (int num : nums)
            maxHeap.add(num);
        int answer = 0;
        while (!maxHeap.isEmpty() && k > 0) {
            answer = maxHeap.poll();
            k--;
        }
        return answer;
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
        Assertions.assertThat(findPeakElementUseTemplate(input)).isEqualTo(2);
        input = new int[]{1};
        Assertions.assertThat(findPeakElement(input)).isEqualTo(0);
        Assertions.assertThat(findPeakElementUseTemplate(input)).isEqualTo(0);
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
     * Use the "Find First True" template. There are three cases
     * Say we want to map the element to T after the peak, false if it increases than previous one
     * 1. [5,4,3,2,1] --> [T,T,T,T,T]
     * 2. [1,2,3,4,5] --> [F,F,F,F,T]
     * 3. [1,2,5,4,3] --> [F,F,T,T,T]
     * 4. [5,4,3,4,5] --> [T,T,T,F,F]
     * Therefore, the condition here is nums[i] > nums[i+1], and we want to find the first
     * element satisfied it.
     */
    int findPeakElementUseTemplate(int[] nums) {
        int left = -1;
        int right = nums.length - 1;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (mid == nums.length - 1 || nums[mid] > nums[mid + 1])
                // For the edge case, [F,F,F,F,T], when mid falls on the last item, it is considered peak as well, so
                // the condition holds true
                right = mid;
            else
                left = mid;
        }
        return right;
    }

    /**
     * Unfortunately, the std binary search template doesn't quite work here. If will fail at the edge case
     * such as one element array
     */
    int findPeakElementSTD(int[] nums) {
        int left = 0, right = nums.length - 1;
        int ans = -1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            if (mid < nums.length - 1 && (nums[mid] > nums[mid + 1] || mid == nums.length - 1)) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
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
        Assertions.assertThat(searchRangeWithTemplate(input, 8)).containsOnly(3, 4);
        input = new int[]{5, 7, 7, 8, 8, 10};
        Assertions.assertThat(searchRange(input, 6)).containsOnly(-1, -1);
        Assertions.assertThat(searchRangeWithTemplate(input, 6)).containsOnly(-1, -1);
        Assertions.assertThat(searchRangeWithTemplate(new int[]{1}, 1)).containsOnly(0, 0);
    }

    /**
     * Use the "Find First True" binary search to find the first index and the "Find Last True" binary search to find the last index
     * <p>
     * The condition for the "Find First True" is num[i] >= target, so we can map to the pattern [F,F,T,T,T,T] for
     * the input [1,2,3,3,4,5], target: 3
     * <p>
     * The condition for the "Find Last True" is num[i] <= target, so we can map to the pattern [T,T,T,T,F,F] for
     * the input [1,2,3,3,4,5], target: 3
     * <p>
     * We need to add additional check after the loop terminates for right/left is not moved or not equal to target when the
     * target doesn't exist in the array.
     * Time complexity: O(log n)
     * Space complexity : O(1)
     */
    int[] searchRangeWithTemplate(int[] nums, int target) {
        int firstIdx = findFirstTargetIdx(nums, target);
        if (firstIdx == -1)
            return new int[]{-1, -1};
        int lastIdx = findLastTargetIdx(nums, target);
        return new int[]{firstIdx, lastIdx};
    }

    int findFirstTargetIdx(int[] nums, int target) {
        // init left to -1, right to nums.length. If the right remains unchanged till the end(That's why it begins outside
        // the bound), we know that the element doesn't exist, and we can return -1, otherwise we return right.
        int left = -1, right = nums.length; // init right to the o
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] >= target)
                right = mid;
            else
                left = mid;
        }
        if (right == nums.length || nums[right] != target)
            return -1;
        return right;
    }

    int findLastTargetIdx(int[] nums, int target) {
        // if the left remains unchanged till the end, we know that the element doesn't exist
        // we can return left (because when it is unchanged, it is already -1)
        int left = -1, right = nums.length;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] <= target)
                left = mid;
            else
                right = mid;
        }
        if (left == -1 || nums[left] != target)
            return -1;
        return left;
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
        Assertions.assertThat(findMinWithTemplate(input)).isEqualTo(1);
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
     * Use the "Find First True" template. We want to set up the TRUE condition when the found element is in the sub-array
     * having the min number.
     * Case 1(array is rotated): For [4,5,6,7,0,1,2], the mapped boolean is [F, F, F, F, T, T, T]
     * Case 2(array is NOT rotated): For [1,2,3,4], the mapped boolean is [T, T, T, T]
     * Therefore, to satisfy both cases, the condition will be nums[i] < nums[nums.length - 1], and we want to find the first
     * element satisfying it.
     * Cuz regardless how many the array is rotated, the min must be on an ascending section
     * extending to the last element in the array. All elements in this section must satisfy this
     * condition.
     */
    int findMinWithTemplate(int[] nums) {
        // range: left -> invalid (-1), right -> valid (0 - nums.length - 1)
        int left = -1, right = nums.length - 1;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < nums[nums.length - 1])
                right = mid;
            else
                left = mid;
        }
        return nums[right];
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
        Assertions.assertThat(searchUseTemplate(input, 0)).isEqualTo(4);
        Assertions.assertThat(search(input, 0)).isEqualTo(4);
        input = new int[]{4, 5, 6, 7, 0, 1, 2};
        Assertions.assertThat(searchUseTemplate(input, 3)).isEqualTo(-1);
        Assertions.assertThat(search(input, 3)).isEqualTo(-1);
    }

    /**
     * Leverage the solution of "Find Minimum in Rotated Sorted Array" and use the found index of the minimum element
     * to either search for the target on the left side of the pivot or the right side of the pivot. This would be
     * decided based on whether the target is smaller than the right most element or not.
     */
    int searchUseTemplate(int[] nums, int target) {
        int left = -1, right = nums.length - 1;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < nums[nums.length - 1])
                right = mid;
            else
                left = mid;
        }
        // right is the index of pivot/min element index
        if (target <= nums[nums.length - 1])
            // target is inside the range [pivot, tail]
            // Binary search over elements on the pivot element's right
            return binarySearchWithBoundary(nums, right, nums.length - 1, target);
        else
            // target is inside the range [head, pivot]
            // Binary search over elements on the pivot element's left
            return binarySearchWithBoundary(nums, 0, right - 1, target);
    }

    /**
     * Use the standard binary search template to search for the target value
     * The special "Find First True" template can also be used, and the condition will be nums[mid] >= target
     * and we also need to check if the nums[right] is equal to target before returning it.
     */
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


    /**
     * Search a 2D Matrix
     * You are given an m x n integer matrix with the following two properties:
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
     * The idx on this virtual array and can be mapped to the m(rows) x n(cols) matrix
     * rowIdx = idx / n
     * colIdx = idx % n
     * <p>
     * And left = 0, right = m * n -1
     *
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
     * Start from bottom-left cell and move the pointer up if it is greater than the target,
     * move right if it is less than target. Return true if target is found, otherwise, keep
     * searching as long as the pointer is not out of bound of the grid.
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
     * Kth Smallest Element in a Sorted Matrix
     * Given an n x n matrix where each of the rows and columns is sorted in ascending order, return
     * the kth smallest element in the matrix.
     * <p>
     * Note that it is the kth smallest element in the sorted order, not the kth distinct element.
     * <p>
     * You must find a solution with a memory complexity better than O(n^2).
     * <p>
     * Input: matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
     * Output: 13
     * Explanation: The elements in the matrix are [1,5,9,10,11,12,13,13,15], and the 8th smallest
     * number is 13
     * <p>
     * Input: matrix = [[-5]], k = 1
     * Output: -5
     */
    @Test
    void testKthSmallest() {
        int[][] input = new int[][]{{1, 5, 9}, {10, 11, 13}, {12, 13, 15}};
        Assertions.assertThat(kthSmallest(input, 8)).isEqualTo(13);
        Assertions.assertThat(kthSmallest(input, 5)).isEqualTo(11);
        Assertions.assertThat(kthSmallest(new int[][]{{-5, -4}, {-5, -4}}, 2)).isEqualTo(-5);
        Assertions.assertThat(kthSmallest(new int[][]{{-5}}, 1)).isEqualTo(-5);
    }

    /**
     * Use the "Find First True" binary search template. The monotonic function is the count of
     * numbers less than or equal to K in the matrix. We use the fact that column and row are
     * sorted to navigate from the lower left cell to compute the count.
     * <p>
     * Observation:
     * 1. We don't have a straightforward sorted array, and we can't use the same trick from the
     * problem "Search a 2D Matrix", cuz the matrix is not sorted in the same way.
     * <p>
     * 2. An alternate could be to apply the Binary Search on the number range instead of the index
     * range. As we know that the smallest number of our matrix is at the top left corner and the
     * biggest number is at the bottom lower corner. These two number can represent the range of
     * our search space.
     * <p>
     * 3. The hard part is to come up with a monotonic function that returns true for all values
     * greater than or equal to the kth element.
     * <p>
     * For kth element, we know that there will be exactly k elements in the matrix which will be
     * lower than or equal to the kth element. This should give us a hint on the monotonic function.
     * Given a number x in the range - what is the count of numbers in the matrix which are lower
     * than or equal to x?
     * <p>
     * f(x) = countOfNumbersLessEqual(matrix, x) >= k
     * <p>
     * For the search range of all numbers between lo and hi, this would give a pattern of FFF..TTT.
     * We want to find the first T. So this is a Find First True type problem.
     * <p>
     * 4. How to count the number less than or equal to a given number in the matrix?
     * Because the matrix is sorted in both row and column, we start at the lower left corner of the
     * matrix. If the current number is less than or equal to x, we add all the numbers in the
     * current column to our count (count += rowIndex + 1), and move to the next column.
     * If the current number is greater than x, we move to the previous row (the number directly
     * above). Continue the same process.
     * <p>
     * Time Complexity: O(N⋅log(Max−Min))
     * The binary search range is bound by max number and min number in the matrix
     * And for each iteration, we iterate the matrix to compute count in O(n)
     * Thus, O(N⋅log(Max−Min))
     * <p>
     * Space Complexity: O(1)
     */
    int kthSmallest(int[][] matrix, int k) {
        int left = matrix[0][0] - 1, right = matrix[matrix.length - 1][matrix[0].length - 1];
        while (left + 1 < right) {
//            int mid = left + right >>> 1; Doesn't work when the input has negative number
            int mid = left + (right - left) / 2;
            if (countOfNumbersLessEqual(matrix, mid) >= k) {
                // This is mystery(?) why the calculated mid can still fall on the number in the matrix in the end.
                // LeetCode solution tracks the min and max number in the countOfNumbersLessEqual method and uses them
                // to update right or left. So the right or left is always set to a value in the matrix
                right = mid;
            } else {
                left = mid;
            }
        }
        return right;
    }

    /**
     * Count all the numbers smaller than or equal to the target in the matrix
     * We start from the lower left corner of the matrix and then move towards as far as columns are concerned
     * and upwards as far as the rows are concerned. The algo takes advantage of the fact that rows and columns
     * are sorted respectively.
     * This operation has O(n) time complexity
     */
    private int countOfNumbersLessEqual(int[][] matrix, int target) {
        int r = matrix.length - 1;
        int c = 0;
        int count = 0;
        while (r >= 0 && c < matrix[0].length) {
            if (matrix[r][c] <= target) {
                // current number is equal or less than the target. Cuz the column is sorted, it means all numbers
                // above it at the same column are smaller than target. Thus add them to the count.
                count += r + 1;
                // Move to the next column
                c += 1;
            } else {
                // number > target, next number on the same column will be greater, so need to move upward.
                // Move to the row above
                r -= 1;
            }
        }
        return count;
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

    /**
     * Median of Two Sorted Arrays
     * Given two sorted arrays nums1 and nums2 of size m and n respectively,
     * return the median of the two sorted arrays.
     * <p>
     * The overall run time complexity should be O(log (m+n)).
     * <p>
     * Input: nums1 = [1,3], nums2 = [2]
     * Output: 2.00000
     * Explanation: merged array = [1,2,3] and median is 2.
     * <p>
     * Input: nums1 = [1,2], nums2 = [3,4]
     * Output: 2.50000
     * Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
     * <p>
     * https://leetcode.com/problems/median-of-two-sorted-arrays/description/
     */
    @Test
    void testFindMedianSortedArrays() {
        int[] a1 = new int[]{1, 2};
        int[] a2 = new int[]{3, 4, 5};
        Assertions.assertThat(findMedianSortedArrays(a1, a2)).isEqualTo(3);
        a1 = new int[]{1, 2, 3};
        a2 = new int[]{4, 5, 6};
        Assertions.assertThat(findMedianSortedArrays(a1, a2)).isEqualTo(3.5);
        a1 = new int[]{4, 5, 6};
        a2 = new int[]{1, 2, 3};
        Assertions.assertThat(findMedianSortedArrays(a1, a2)).isEqualTo(3.5);
        a1 = new int[]{1, 5, 6};
        a2 = new int[]{2, 3, 4};
        Assertions.assertThat(findMedianSortedArrays(a1, a2)).isEqualTo(3.5);
    }

    /**
     * Use binary search on the smaller array to search for partition points on both arrays. Two left partitions should have total
     * (m + n + 1) / 2 elements and the condition (maxLeftA <= minRightB && maxLeftB <= minRightA) holds true. Then the median
     * can be derived from the elements of two boundaries.
     * <p>
     * Observation:
     * 1. Since both arrays are sorted, if we can find out the position on both arrays to partition it into left and right parts.
     * And the elements from both left parts are just like the left half part of the merged array of A and B. Then the median must
     * lie either at the boundary of the two halves or within one of the two arrays.
     * * --------------------------------
     * A |<- LeftA ->|   <- RightA ->   |
     * * --------------------------------
     * *             ^
     * *             PA
     * * --------------------------------
     * B |   <- LeftB -> | <- RightB -> |
     * * --------------------------------
     * *                 ^
     * *                 PB
     * 2. When partitioning two arrays we need to make sure the number of elements from (LeftA + LeftB) is roughly the same as
     * (RightA + RightB), we will use (m + n + 1) / 2 for the smaller half, i.e. (LeftA + LeftB). Once we decide the position
     * to partition the array A, i.e. PA, PB can be set as (m + n + 1) / 2 - PA. Here we want to define PA and PB as the FIRST
     * element in the RightA and RightB respectively.
     * <p>
     * 3. If we first consider the case that the smaller half is conisit of some elements from both LeftA and LeftB.
     * Say PA is on element 6 on array A, and PB is on element 5 on array B. We need the following values to calculate the
     * median.
     * maxLeftA: the maximum element of the LeftA part in array A
     * minRightA: the minimum element of the RightA part in array A
     * maxLeftB: the maximum element of the LeftB part in array B
     * minRightB: the minimum element of the RightB part in array B
     * <p>
     * * ----------------------
     * A |  |  | 3| 6|  |  |  |
     * * ----------------------
     * *         ^  ^
     * *  maxLeftA  minRightA
     * <p>
     * * ----------------------
     * B |  |  |  | 4| 5|  |  |
     * * ----------------------
     * *            ^  ^
     * *     maxLeftB  minRightB
     * <p>
     * When we partition two arrays correctly like above diagram, we will have the condition,
     * (maxLeftA <= minRightB && maxLeftB <= minRightA) holds true.
     * We just need to find the maximum value from the smaller half as max(A[maxLeftA], B[maxLeftB]) and the
     * minimum value from the larger half as min(A[minRightA], B[minRightB]). The median value depends on these four boundary
     * values and the total length of the input arrays(odd/even) and we can compute it by situation.
     * <p>
     * Algo:
     * 1. Our goal is to use the binary search on the smaller array to search for the correct partition index, midArrayA(PA)
     * to satisfy the condition (maxLeftA <= minRightB && maxLeftB <= minRightA) == true
     * <p>
     * 2. Whenever we move the midArrayA(PA), the midArrayB(PB) will be updated using the above formula.
     * <p>
     * 3. When maxLeftA > minRightB, it means we need to shrink the left part of the array A to make the maxLeftA smaller.
     * So we need to move the right ptr to the left, and the midArrayA will be shifted to the left as well.
     * <p>
     * 4. When maxLeftB > minRightA, it means we need to expand the left part of the array A to make minRightA bigger.
     * So We need to move the left ptr to the right, and the midArrayA will be shifted to the right as well.
     * <p>
     * Time complexity: O(log(min(m,n)))
     * Let m be the size of array nums1 and n be the size of array nums2.
     * We perform a binary search over the smaller array of size min(m,n).
     * <p>
     * Space complexity: O(1)
     * <p>
     * Check video for more detail
     * https://www.youtube.com/watch?v=KB9IcSCDQ9k&ab_channel=HuaHua
     */
    double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // Ensure nums1 is the smaller array
        if (nums1.length > nums2.length)
            return findMedianSortedArrays(nums2, nums1);

        int m = nums1.length, n = nums2.length;
        int left = 0, right = m;// BE CAREFUL of the boundary is [0-m] INCLUSIVE
        // Perform binary search on the smaller array, nums1.

        while (left <= right) {
            // mid idx of nums1
            int midArrayA = left + (right - left) / 2; // The first element index of the right/larger half part
            // Let the smaller half of combined array size: (m + n + 1) / 2
            // mid idx of nums2: The rest of the half of combined array size
            int midArrayB = (m + n + 1) / 2 - midArrayA;

            // These are three edge cases can cause index out of bound:
            // (1) When the last element of array A is less than the first element of array B. Ex: A:[1,2,3] B:[4,5,6]
            // midArrayA will be moved out of bound of array A eventually and midArrayB will be at the head of array B. In this case, we can
            // conclude the median is determined between the maxLeftA and the minRightB, so to satisfying the condition of the median position
            // found (maxLeftA <= minRightB && maxLeftB <= minRightA), we set minRightA to int MAX value and maxLeftB to int MIN value.
            // Doing so also avoid the index out of bound error.
            // (2) In the opposite case like A:[4,5,6] B:[1,2,3]. midArrayA will be at the head of array A, and midArrayB is moved out of bound.
            // So maxLeftA is will be set to int MIN value , and minRightB is set to int MAX value.
            // (3) Either midArrayA is moved out of bound or midArrayB is at the head, which means the median is most likely on the other array

            // the maximum element of the left part partitioned by the midArrayA idx in array A, i.e. previous element of midArrayA
            int maxLeftA = (midArrayA == 0) ? Integer.MIN_VALUE : nums1[midArrayA - 1];
            // the minimum element to the right part partitioned by the midArrayA idx in array A, i.e. the value of current midArrayA point to
            int minRightA = (midArrayA == m) ? Integer.MAX_VALUE : nums1[midArrayA];
            // the maximum element of the left part partitioned by the midArrayB idx in array B, i.e. previous element of midArrayB
            int maxLeftB = (midArrayB == 0) ? Integer.MIN_VALUE : nums2[midArrayB - 1];
            // the minimum element to the right part partitioned by the midArrayB idx in array B, i.e. the value of current midArrayB point to
            int minRightB = (midArrayB == n) ? Integer.MAX_VALUE : nums2[midArrayB];

            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {
                // Found the correct partitions made by midArrayA and midArrayB
                if ((m + n) % 2 == 0) {
                    // If the combined array has an even length, take the average
                    // Two elements are from the left half and other two are from the right half. So we take the bigger one from the
                    // left, and smaller one from the right, which are the middle two elements in the merged array
                    return (Math.max(maxLeftA, maxLeftB) + Math.min(minRightA, minRightB)) / 2.0;
                } else {
                    // If the combined array has an odd length, return the maximum of the left elements
                    // In the beginning, we let the combined smaller/left size (m + n + 1)/2, therefore the median is the bigger one
                    // of the left max on both array.
                    return Math.max(maxLeftA, maxLeftB);
                }
            } else if (maxLeftA > minRightB) {
                // maxLeftA is too large to be in the smaller half, and it should be in the larger half of array A,
                // which means we need to shrink the left part of the array A to make the maxLeftA smaller.
                // so we need to move the right ptr to the left and the midArrayA will be shifted to the left too
                right = midArrayA - 1;
            } else {
                // maxLeftB > minRightA
                // We need a bigger minRightA, so we need to expand the left part of the array A to make minRightA bigger.
                // We need to move the left ptr to the right, so the midArrayA will be shifted to the right.
                left = midArrayA + 1;
            }
        }
        return 0.0;
    }

    /**
     * Find the Duplicate Number
     * Given an array of integers nums containing n + 1 integers where each integer is
     * in the range [1, n] inclusive.
     * <p>
     * There is only one repeated number in nums, return this repeated number.
     * <p>
     * You must solve the problem without modifying the array nums and uses only constant extra space.
     * <p>
     * Input: nums = [1,3,4,2,2]
     * Output: 2
     * <p>
     * Input: nums = [3,1,3,4,2]
     * Output: 3
     * <p>
     * https://leetcode.com/problems/find-the-duplicate-number/description/
     */
    @Test
    void testFindDuplicate() {
        Assertions.assertThat(findDuplicate(new int[]{1, 3, 4, 2, 2})).isEqualTo(2);
    }

    /**
     * Considering the array as linked list that each element is a node and the value also represents next node index.
     * Then apply Floyd's tortoise and hare algo to find the cycle entrance point.
     * <p>
     * Observation:
     * The problem can be reduced to the problem "Linked List Cycle II". The thought process is given the constraint
     * that the nums is [1-n], we can see each element as a linked list node w/ value as nums[i], and its next ptr is
     * the element whose index == nums[i].
     * <p>
     * Ex:
     * The next ptr of nums[3](Node 3) and nums[5](Node 5) are both Node 1
     * -----------------------------------
     * |index| 0 | 1 | 2 | 3 | 4 | 5 | 6 |
     * -----------------------------------
     * |nums | 2 | 6 | 4 | 1 | 3 | 1 | 5 |
     * -----------------------------------
     * <p>
     * * 2 --> 4 --> 3 --> 1 --> 6 --> 5
     * *                   ^           |
     * *                   |___________|
     * <p>
     * Algo:
     * Now we can apply the Floyd's tortoise and hare algo to find the cycle entrance.
     * Phase 1: hare = nums[nums[hare]] is twice as fast as tortoise = nums[tortoise], the loop terminates when
     * two meet.
     * <p>
     * Phase 2: Reset the tortoise to the starting position and let hare move at the speed of tortoise:
     * tortoise = nums[tortoise], hare = nums[hare], the hare starts from the previous intersection point.
     * Start the same loop until they meet again. Where they meet each other is the cycle entrance, return
     * either tortoise or hare.
     * <p>
     * Time Complexity: O(n)
     * <p>
     * Space Complexity: O(1)
     */
    int findDuplicate(int[] nums) {
        // Find the intersection point of the two runners.
        int tortoise = nums[0];
        int hare = nums[0];
        do {
            // Phase 1: Find the first intersection point(This is NOT the cycle entrance)
            tortoise = nums[tortoise]; // move to the node/element w/ the index the same as the current element
            hare = nums[nums[hare]]; // move to two elements as the same way above
        } while (tortoise != hare);

        // Phase 2: Find the true cycle entrance
        tortoise = nums[0];
        while (tortoise != hare) {
            tortoise = nums[tortoise];
            hare = nums[hare];
        }
        return hare;
    }

    /**
     * Koko Eating Bananas
     * Koko loves to eat bananas. There are n piles of bananas, the ith pile has piles[i] bananas. The guards
     * have gone and will come back in h hours.
     * <p>
     * Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses some pile of bananas
     * and eats k bananas from that pile. If the pile has less than k bananas, she eats all of them instead
     * and will not eat any more bananas during this hour.
     * <p>
     * Koko likes to eat slowly but still wants to finish eating all the bananas before the guards return.
     * <p>
     * Return the minimum integer k such that she can eat all the bananas within h hours.
     * <p>
     * Input: piles = [3,6,7,11], h = 8
     * Output: 4
     * <p>
     * Input: piles = [30,11,23,4,20], h = 5
     * Output: 30
     * <p>
     * Input: piles = [30,11,23,4,20], h = 6
     * Output: 23
     * <p>
     * https://leetcode.com/problems/koko-eating-bananas/description/
     */
    @Test
    void testMinEatingSpeed() {
        Assertions.assertThat(minEatingSpeed(new int[]{3, 6, 7, 11}, 8)).isEqualTo(4);
        Assertions.assertThat(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5)).isEqualTo(30);
        Assertions.assertThat(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 6)).isEqualTo(23);
        Assertions.assertThat(minEatingSpeedII(new int[]{3, 6, 7, 11}, 8)).isEqualTo(4);
        Assertions.assertThat(minEatingSpeedII(new int[]{30, 11, 23, 4, 20}, 5)).isEqualTo(30);
        Assertions.assertThat(minEatingSpeedII(new int[]{30, 11, 23, 4, 20}, 6)).isEqualTo(23);
    }

    /**
     * Use binary search to find the least number(speed) in [1 - max(piles)] range and the hours needed to finish
     * all piles at this speed is less than or equal to the hour limit.
     * <p>
     * Observation:
     * 1. The eating speed, k, must be within the range of [1 - max(piles)] inclusive. Using the brute force,
     * we will need to try every number in the range and calculate the hours to finish all piles and check if
     * it is less or equal to the required hour limit.
     * <p>
     * 2. Hence, we can think of this range as an ascending number array, and use binary search to find the
     * first/min number(speed) that the total hours it takes to finish all piles is less than or equal to the hour limit.
     * <p>
     * Algo:
     * 1. Use the "Find first true" binary search template. The condition is "Hours to finish all piles at speed(k) <= hour limit(h)"
     * 2. The search space is [1 - max(piles)]. mid ptr is the k we check in the condition
     * <p>
     * Time complexity: O(n⋅logm)
     * n be the length of the input array piles and m be the maximum number of bananas
     * in a single pile from piles.
     * The initial search space is from 1 to m, it takes log m comparisons to reduce the search space to 1.
     * For each eating speed middle, we traverse the array and calculate the overall time Koko spends,
     * which takes O(n) for each traversal.
     * To sum up, the time complexity is O(n⋅logm)
     * <p>
     * Space complexity: O(1)
     */
    int minEatingSpeed(int[] piles, int h) {
        int maxPile = 0;
        for (int pile : piles)
            maxPile = Math.max(pile, maxPile);
        // Use "Find first true" template
        // left -> invalid (0), right -> valid (1 to max(piles))
        int left = 0, right = maxPile;
        while (left + 1 < right) {
            // mid is the eating speed, k.
            int mid = left + (right - left) / 2;
            // compute hours needed using speed, mid.
            int hours = 0;
            for (int pile : piles) {
                hours += Math.ceil((double) pile / mid);
            }
            if (hours <= h)
                right = mid;
            else
                left = mid;
        }
        return right;
    }

    /**
     * Use standard binary search "Find first true"
     */
    int minEatingSpeedII(int[] piles, int h) {
        int maxPile = 0;
        for (int pile : piles)
            maxPile = Math.max(pile, maxPile);
        int left = 1, right = maxPile;
        int ans = 0;
        while (left <= right) {
            // mid is the eating speed, k.
            int mid = left + (right - left) / 2;
            // compute hours needed using speed, mid.
            int hours = 0;
            for (int pile : piles) {
                hours += Math.ceil((double) pile / mid);
            }
            if (hours <= h) {
                ans = mid;
                right = mid - 1;
            } else
                left = mid + 1;
        }
        return ans;
    }

    /**
     * Single Element in a Sorted Array
     * You are given a sorted array consisting of only integers where every element
     * appears exactly twice, except for one element which appears exactly once.
     * <p>
     * Return the single element that appears only once.
     * <p>
     * Your solution must run in O(log n) time and O(1) space.
     * <p>
     * Input: nums = [1,1,2,3,3,4,4,8,8]
     * Output: 2
     * <p>
     * Input: nums = [3,3,7,7,10,11,11]
     * Output: 10
     * <p>
     * https://leetcode.com/problems/single-element-in-a-sorted-array/description/
     */
    @Test
    void testSingleNonDuplicate() {
        Assertions.assertThat(singleNonDuplicate(new int[]{1, 1, 2, 3, 3, 4, 4, 8, 8})).isEqualTo(2);
        Assertions.assertThat(singleNonDuplicate(new int[]{3, 3, 7, 7, 10, 11, 11})).isEqualTo(10);
    }

    /**
     * Use the Find First True binary search template. The monotonic condition is when the number is
     * at even index, the next number is different number. If at odd index, the previous number is
     * different number.
     * <p>
     * Observation:
     * If all the elements had duplicates, what property would they have?
     * Example: [1, 1, 3, 3, 5, 5, 7, 7, 9, 9];
     * Elements at the index [0, 2, 4, ..., n - 2] would all have a duplicate element on the right.
     * Elements at the index [1, 3, 5, ..., n - 1] would all have a duplicate element on the left.
     * <p>
     * Now if we insert 6 in the array in sorted order. This is going to disturb the property above.
     * After insertion: [1, 1, 3, 3, 5, 5, 6, 7, 7, 9, 9];
     * Because 6 is inserted at index 6, the above property still holds true until the index 5;
     * <p>
     * Now we can use the inverse of the above property to derive the monotonic condition and apply
     * the "Find first true" template
     * For all the numbers starting from the index 6, the following holds true:
     * <p>
     * if index is even, there is no duplicate element on the right
     * if index is odd, there is no duplicate element on the left
     * <p>
     * We want to find the minimum index for which the above property holds true.
     * <p>
     * Time complexity: O(log n)
     * Space complexity: O(1)
     */
    int singleNonDuplicate(int[] nums) {
        int left = -1, right = nums.length - 1;
        while (left + 1 < right) {
            int mid = left + right >>> 1;
            if (differentNumberOnOneSide(nums, mid))
                right = mid;
            else
                left = mid;
        }
        return nums[right];
    }

    boolean differentNumberOnOneSide(int[] nums, int idx) {
        if (idx % 2 == 0)
            // even index element should have different next element
            // The unique number must be at even index
            return nums[idx] != nums[idx + 1];
        else
            // odd index should have different previous element
            return nums[idx] != nums[idx - 1];
    }

    /**
     * Missing Element in Sorted Array
     * Given an integer array nums which is sorted in ascending order, and all the elements are
     * unique and all of its elements are unique, and given also an integer k, return the kth
     * missing number starting from the leftmost number of the array.
     * <p>
     * Input: nums = [4,7,9,10], k = 1
     * Output: 5
     * Explanation: The first missing number is 5.
     * <p>
     * Input: nums = [4,7,9,10], k = 3
     * Output: 8
     * Explanation: The missing numbers are [5,6,8,...], hence the third missing number is 8.
     * <p>
     * Input: nums = [1,2,4], k = 3
     * Output: 6
     * Explanation: The missing numbers are [3,5,6,7,...], hence the third missing number is 6.
     * <p>
     * https://leetcode.com/problems/missing-element-in-sorted-array/description/
     */
    @Test
    void testMissingElement() {
        Assertions.assertThat(missingElement(new int[]{4, 7, 9, 10}, 1)).isEqualTo(5);
        Assertions.assertThat(missingElement(new int[]{4, 7, 9, 10}, 3)).isEqualTo(8);
        Assertions.assertThat(missingElement(new int[]{1, 2, 4}, 3)).isEqualTo(6);
    }

    /**
     * Use the Find Last True binary search template. The monotonic condition is the number of
     * missing elements at a given index < k. Once we find the index(left), the answer is
     * nums[left] + (k - countOfMissingElementsAt(left))
     * <p>
     * Observation:
     * 1. The naive solution is to iterate the array and if there is a gap between current
     * and the next element, count the numbers in the missing range until count == k.
     * <p>
     * 2. For each of the array indexes we can find the number of missing elements in the
     * array in O(1). The number of missing elements at each index could either be less
     * than k, or greater than or equal to k. It is easier for us to use the former one.
     * Cuz if we find the first index having greater than or equal to k, it is hard to
     * find out how many number we should move backward to the k position.
     * <p>
     * 3. So the monotonic function is "Find Last True" type TTTTTFFF. If we can find the
     * last index lo where the number of missing elements is less k, then we can find the
     * kth missing element using the following formula:
     * <p>
     * kthMissing = nums[lo] + (k - countOfMissingElementsAt(lo))
     * <p>
     * countOfMissingElementsAt is the monotonic function. It is defined as
     * The number of missing element within the range [0, idx]
     * ---> The number of elements that should have been there - the number of existing number
     * ---> (nums[idx] - nums[0] + 1) - (idx + 1) = nums[idx] - nums[0] - idx
     * <p>
     * Time complexity: O(log n)
     * Space complexity: O(1)
     */
    int missingElement(int[] nums, int k) {
        int left = 0, right = nums.length;
        while (left + 1 < right) {
            int mid = left + right >>> 1;
            if (countOfMissingElementAt(nums, mid) < k) {
                left = mid;
            } else {
                right = mid;
            }
        }
        // left is the last idx of the element that the number of missing elements is less than k
        return nums[left] + k - countOfMissingElementAt(nums, left);
    }

    /**
     * The number of missing element within the range [0, idx] is
     * The number of elements that should have been there - the number of existing number
     * (nums[idx] - nums[0] + 1) - (idx + 1) = nums[idx] - nums[0] - idx
     */
    private int countOfMissingElementAt(int[] nums, int idx) {
        return nums[idx] - nums[0] - idx;
    }

    /**
     * Find K Closest Elements
     * Given a sorted integer array arr, two integers k and x, return the k closest integers
     * to x in the array. The result should also be sorted in ascending order.
     * <p>
     * An integer a is closer to x than an integer b if:
     * <p>
     * |a - x| < |b - x|, or
     * |a - x| == |b - x| and a < b
     * <p>
     * <p>
     * Input: arr = [1,2,3,4,5], k = 4, x = 3
     * Output: [1,2,3,4]
     * <p>
     * Input: arr = [1,2,3,4,5], k = 4, x = -1
     * Output: [1,2,3,4]
     * <p>
     * https://leetcode.com/problems/find-k-closest-elements/description/
     */
    @Test
    void testFindClosestElements() {
        Assertions.assertThat(findClosestElements(new int[]{1, 2, 3, 4, 5}, 4, 3)).containsExactly(1, 2, 3, 4);
        Assertions.assertThat(findClosestElements(new int[]{1, 2, 3, 4, 5}, 4, -1)).containsExactly(1, 2, 3, 4);
        Assertions.assertThat(findClosestElementsBinarySearch(new int[]{1, 2, 3, 4, 5}, 4, 3)).containsExactly(1, 2, 3, 4);
        Assertions.assertThat(findClosestElementsBinarySearch(new int[]{1, 2, 3, 4, 5}, 4, -1)).containsExactly(1, 2, 3, 4);
        Assertions.assertThat(findClosestElementsBinarySearch(new int[]{1}, 1, 1)).containsExactly(1);
        Assertions.assertThat(findClosestElementsBinarySearch(new int[]{0, 1, 2, 2, 2, 3, 6, 8, 8, 9}, 5, 9)).containsExactly(3, 6, 8, 8, 9);
        Assertions.assertThat(findClosestElementsBinarySearch(new int[]{1, 1, 2, 2, 2, 2, 2, 3, 3}, 3, 3)).containsExactly(2, 3, 3);

        Assertions.assertThat(findClosestElementsBinarySearchAndWindow(new int[]{1, 2, 3, 4, 5}, 4, 3)).containsExactly(1, 2, 3, 4);
        Assertions.assertThat(findClosestElementsBinarySearchAndWindow(new int[]{1, 2, 3, 4, 5}, 4, -1)).containsExactly(1, 2, 3, 4);
        Assertions.assertThat(findClosestElementsBinarySearchAndWindow(new int[]{1, 3}, 1, 2)).containsExactly(1);
        Assertions.assertThat(findClosestElementsBinarySearchAndWindow(new int[]{3, 5, 8, 10}, 2, 15)).containsExactly(8, 10);
        Assertions.assertThat(findClosestElementsBinarySearchAndWindow(new int[]{0, 0, 1, 2, 3, 3, 4, 7, 7, 8}, 3, 5)).containsExactly(3, 3, 4);
    }

    /**
     * Add all elements to the MinHeap sorting the element by the distance to x. Then pop K elements from the
     * heap and sort k-sized list and return.
     * <p>
     * Time complexity: O(N⋅logN + K⋅logN + K⋅logK)
     * (Add N numbers to heap) + (pop k numbers from heap) + (sort k numbers)
     * Space complexity: O(N)
     */
    List<Integer> findClosestElements(int[] arr, int k, int x) {
        Comparator<Integer> byDistance = (a, b) -> {
            int result = Integer.compare(Math.abs(a - x), Math.abs(b - x));
            if (result == 0)
                return a.compareTo(b);
            return result;
        };

        Queue<Integer> pq = new PriorityQueue<>(byDistance);
        for (int num : arr) {
            pq.add(num);
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            result.add(pq.poll());
        }
        Collections.sort(result);
        return result;
    }

    /**
     * 1. Use binary search to find the number closest to x. Need to consider edge case, e.g. x is outside the array
     * and prefer the smaller number when multiple numbers have the same distance to x.
     * <p>
     * 2. Use the found index as starting point, use left and right ptr to move one of them by comparing the element
     * distance to x each time until they form a k-sized window.
     * <p>
     * 3. Return the k elements starting from left ptr.
     * <p>
     * Time complexity: O(log(N)+k)
     * Space complexity: O(1)
     */
    List<Integer> findClosestElementsBinarySearchAndWindow(int[] arr, int k, int x) {
        List<Integer> result = new ArrayList<>();
        if (arr.length == k) {
            for (int i = 0; i < k; i++)
                result.add(arr[i]);
            return result;
        }

        // Binary search to find the element, the first greater than or equal to x
        int left = 0;
        int right = arr.length - 1;
        int idx = -1;
        while (left <= right) {
            int mid = (left + right) >>> 1;
            if (arr[mid] >= x) {
                idx = mid;
                right = mid - 1;
            } else {
                // arr[mid] < x
                left = mid + 1;
            }
        }
        // Edge case(all numbers in the array are less than x): idx would remain -1, so set idx to right
        idx = idx == -1 ? right : idx;
        // Now we need to check if the element preceding idx also has the same distance to x
        // Ex: x: 4, arr[idx]: 5, arr[idx-1]: 3, we should prefer smaller index element when distance are the same.
        if (idx != 0 && Math.abs(arr[idx - 1] - x) <= Math.abs(arr[idx] - x))
            idx--;

        // Use two ptr, left and right, and idx as starting point to build a k-sized window
        left = idx;
        right = idx;
        // Expand the window[left(inclusive) - right(inclusive)] until we have k elements
        while (right - left + 1 < k) {
            if (left > 0 && right < arr.length - 1) {
                // Expand the side having the element w/ the smaller distance to x
                if (Math.abs(arr[left - 1] - x) <= Math.abs(arr[right + 1] - x))
                    --left;
                else
                    ++right;
            } else if (left == 0) {
                // Can't go left anymore, so expand right
                ++right;
            } else if (right == arr.length - 1) {
                // Can't go right anymore, so expand left
                --left;
            }
        }
        // Collect the k elements from left ptr
        while (k > 0) {
            result.add(arr[left++]);
            --k;
        }
        return result;
    }

    /**
     * Use a custom binary search to find the optimal left bound of a k-sized window. The algo to search it is to
     * compare the (arr[mid] + arr[mid+k]) / 2 with x. If x is smaller, search the left part, otherwise, search the
     * right part. The logic behind this is unknown and not well explained on LeetCode and very confusing. There is
     * no clear relation between this value and the distance to x from k elements in this window. Approach 2 is more
     * convincing and easy to reason than this one.
     * <p>
     * Time complexity: O(log(N-k)+k)
     * Space complexity: O(1)
     */
    List<Integer> findClosestElementsBinarySearch(int[] arr, int k, int x) {
        // The search range is the possible index values can be used as the left bound of the k-sized window, which
        // contains the k closet elements to x
        int left = 0;
        int right = arr.length - k; // Init to the index of the left bound of the last window in the array

        int leftBound = left; // init to the final leftBound to the left ptr for the edge case of 1 element array
        while (left <= right) {
            int mid = (left + right) >>> 1;
            // When the left bound of the next K-sized window is out of bound, this means the current mid is already on the left
            // bound of the last window in the array. This window is the answer, so we can break here. The final leftBound
            // will be taken care by the right bound check after the loop.
            if (mid + k >= arr.length)
                break;

            // We compute the avg of two left bound values, and use it as the reference point to decide
            // how to adjust/move the window. Note: this reference point does NOT take the distance to the X from any
            // elements in the window at all. It is questionable why the LeetCode solution uses this and their
            // explanation is also wrong and confusing. The only possible explanation is it uses the avg of the two left
            // bound number to roughly estimate the distance to the X from K elements in the window. The algo is to make
            // a window so the midVal can be close to the x as possible as we can. So if x <= midVal, we shift the left
            // bound to left, otherwise, shift to the right
            double midVal = ((double) arr[mid + k] + arr[mid]) / 2;
            if (x <= midVal) {
                leftBound = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        // This is for the edge case that the last window is the answer. And the right ptr is never moved in the search
        // and left == right + 1 after the above loop terminates
        if (right == arr.length - k)
            leftBound = right;

        // Collect the K elements from the leftBound
        List<Integer> result = new ArrayList<>();
        for (int i = leftBound; i < leftBound + k; i++) {
            result.add(arr[i]);
        }
        return result;
    }

    /**
     * This is the version copied from LeetCode
     */
    List<Integer> findClosestElementsLeetCode(int[] arr, int k, int x) {
        // Initialize binary search bounds
        int left = 0;
        int right = arr.length - k;

        // This is the confusing part from the LeetCode provided solution
        while (left < right) {
            int mid = (left + right) / 2;
            if (x - arr[mid] > arr[mid + k] - x) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        // Create output in correct format
        List<Integer> result = new ArrayList<Integer>();
        for (int i = left; i < left + k; i++) {
            result.add(arr[i]);
        }
        return result;
    }


}
