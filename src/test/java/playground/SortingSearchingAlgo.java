package playground;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class SortingSearchingAlgo {
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
     * https://leetcode.com/problems/sort-an-array/editorial/
     */
    @Test
    void testMergeSort() {
        int[] nums = new int[]{5, 2, 3, 1};
        Assertions.assertThat(mergeSort(nums)).containsExactly(1, 2, 3, 5);
        Assertions.assertThat(mergeSort(new int[]{5, 1, 1, 2, 0, 0})).containsExactly(0, 0, 1, 1, 2, 5);
    }

    /**
     * Algo:
     * 1. Divide the data set into two equal parts
     * 2. Recursively sort each half
     * 3. Merge the sorted halves
     * 4. Repeat the process until the entire data is sorted, i.e. The input has single or zero element.
     * <p>
     * Time complexity: O(n⋅log n)
     * There are a total of N elements on each level in the recursion tree. Therefore, it takes O(N)
     * time for the merging process to complete on each level. And there are a total of logN levels.
     * Space complexity: O(n)
     * The recursive stack will take O(log n) space, and we used additional array of size n when copying the split array.
     * https://www.youtube.com/watch?v=alJswNJ4P3U
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

    /**
     * The merge function compares the first elements of each array and insert the smaller element into the final array.
     * This process continues until one of the halves is empty. The remaining elements of the other half are then
     * inserted into the final array.
     */
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

    @Test
    void testQuickSort() {
        int[] nums = new int[]{5, 2, 3, 1};
        quicksort(nums, 0, nums.length - 1);
        Assertions.assertThat(nums).containsExactly(1, 2, 3, 5);
        nums = new int[]{5, 1, 1, 2, 0, 0};
        quicksort(nums, 0, nums.length - 1);
        Assertions.assertThat(nums).containsExactly(0, 0, 1, 1, 2, 5);
    }

    /**
     * quicksort will be called recursively for the elements to the left of pivot and the elements to the right of pivot
     * 1. We choose an element from the list, called the pivot. We’ll use it to divide the list into two sub-lists.
     * 2. We reorder all the elements around the pivot – the ones with smaller value are placed before it, and all the
     * -  elements greater than the pivot after it. After this step, the pivot is in its final position. This is the
     * -  important partition step.
     * 3. We apply the above steps recursively to both sub-lists on the left and right of the pivot.
     * <p>
     * Time Complexity:
     * Worst case scenario: When pivot is at min/max value every time, it will be close to O(n^2)
     * At top recursion level, require n-1 comparison(split n input to 0 and n-1), second level we
     * require n-2 comparison for n-1 input, and so on so forth. We will have a extreme skewed recursion tree. To sum
     * them up, it will be close to (n-1)*n / 2 work. Thus, O(n^2)
     * <p>
     * Best case scenario: Picking the medium number as pivot
     * At each level, we split the input size half, so the work per level is
     * (number of sub-problem) * (sub-problem input size)
     * 1st lvl: n-1, 2nd lvl: 2*(n/2 - 1), 3rd lvl: 4*(n/4 - 1), i-th level: 2^i*(n/2^i - 1)
     * <p>
     * Recursion tree has log base 2 (n) height, therefore we have the summation(i = 0 to log(n)-1)
     * of 2^i * (n/2^i - 1) ==> n⋅log(n) - (n + 1). Hence, O(n⋅log(n))
     * Check the merge sort complexity explanation for math explanation
     * <p>
     * Space Complexity: O(log(n)) due to the recursion stack height
     * <p>
     * https://www.youtube.com/watch?v=uXBnyYuwPe8&t=1019s
     */
    void quicksort(int[] arr, int left, int right) {
        if (right <= left) // base case for recursion
            return;

        // Only proceed if left is less than right
        // Find the position of pivot
        int pivotFinalRestingPosition = partition(arr, left, right);

        // Recursively call left and right subarray to the pivot
        quicksort(arr, left, pivotFinalRestingPosition - 1);
        quicksort(arr, pivotFinalRestingPosition + 1, right);
    }

    /**
     * The partition function that chooses a pivot, partitions the array around the
     * pivot, places the pivot value where it belongs, and then returns the index of
     * where the pivot finally lies
     */
    private int partition(int[] arr, int left, int right) {
        // usually right is the last index of the split section or the original array
        int pivot = arr[right];

        /*
         * i ptr will keep track of the "tail" of the section of items less than the pivot
         * so that at the end we can "sandwich" the pivot between the section less than
         * it and the section greater than it
         */
        int i = left - 1;

        for (int j = left; j < right; j++) {
            // Look for the number point by j equal or smaller than the pivot
            if (arr[j] <= pivot) {
                // Idea is when j ptr finds a number smaller or equal to pivot, we want to put it on the "less than pivot"
                // section. Cuz i sits at the tail/last item of that section, we advance i and then perform swap i with j
                i++;

                swap(arr, i, j);
            }
        }
        // Now i is at the tail of the section smaller or equal to pivot, so we want to put the pivot at i's next
        // position
        swap(arr, i + 1, right);
        return i + 1; // Return the pivot's final resting position
    }

    // Helper function to swap elements at 2 different array indices
    private void swap(int[] arr, int first, int second) {
        int temp = arr[first];
        arr[first] = arr[second];
        arr[second] = temp;
    }

    @Test
    void testInsertionSort() {
        int[] nums = new int[]{5, 2, 3, 1};
        insertionSort(nums);
        Assertions.assertThat(nums).containsExactly(1, 2, 3, 5);
        nums = new int[]{5, 1, 1, 2, 0, 0};
        insertionSort(nums);
        Assertions.assertThat(nums).containsExactly(0, 0, 1, 1, 2, 5);
    }

    /**
     * Starting from the second element in the array, for each element that is out of order, i.e. larger than previous
     * element, continuously swap places with previous elements until it is inserted in its correct relative location
     * based on what you’ve processed thus far.
     * Time complexity of O(n^2) in the worst case, when the array is in reverse order, where every element has to be
     * inserted at the very beginning of the list, which leads to a total of 1 + 2 + ... (n-1) or O(n^2) swaps.
     * But it can be much faster in the best case, when the array is already sorted or nearly sorted, with a time
     * complexity of O(n).
     * Space complexity of insertion sort is O(1). All operations are performed in-place.
     */
    void insertionSort(int[] arr) {
        // Mutates elements in arr by inserting out of place elements into appropriate
        // index repeatedly until arr is sorted
        for (int i = 1; i < arr.length; i++) {
            int currentIndex = i;
            while (currentIndex > 0 && arr[currentIndex - 1] > arr[currentIndex]) {
                // Iteratively comparing to the previous element and swap elements that are out of order
                int temp = arr[currentIndex];
                arr[currentIndex] = arr[currentIndex - 1];
                arr[currentIndex - 1] = temp;
                currentIndex -= 1;
            }
        }
    }

    @Test
    void testSelectionSort() {
        int[] nums = new int[]{5, 2, 3, 1};
        selectionSort(nums);
        Assertions.assertThat(nums).containsExactly(1, 2, 3, 5);
        nums = new int[]{5, 1, 1, 2, 0, 0};
        selectionSort(nums);
        Assertions.assertThat(nums).containsExactly(0, 0, 1, 1, 2, 5);
    }

    /**
     * Repeatedly finding the minimum element in the list(after the current element) and moving it to the front of the
     * list through a swap. It will proceed to swap elements appropriately until the entire list is sorted. requiring
     * O(n^2) time to sort the list in the worst case. space complexity of selection sort is O(1) due to in-place operation
     */
    void selectionSort(int[] arr) {
        // Mutates arr so that it is sorted via selecting the minimum element and
        // swapping it with the corresponding index
        int minIdx;
        for (int i = 0; i < arr.length; i++) {
            minIdx = i;
            // Find the index of min value starting from i+1 index position
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            // Swap current index with minimum element in rest of list
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    @Test
    void testBubbleSort() {
        int[] nums = new int[]{5, 2, 3, 1};
        bubbleSort(nums);
        Assertions.assertThat(nums).containsExactly(1, 2, 3, 5);
        nums = new int[]{5, 1, 1, 2, 0, 0};
        bubbleSort(nums);
        Assertions.assertThat(nums).containsExactly(0, 0, 1, 1, 2, 5);
    }

    /**
     * Consider two adjacent elements at a time. If these two adjacent elements are out of order (the left element is
     * strictly greater than the right element), we swap them. It then proceeds to the next pair of adjacent elements.
     * We repeat this process until no more swaps are made in a single pass, which means the list is sorted.
     * Time complexity of O(n^2). If the array has n elements, each pass will consider (n−1) pairs. In the worst case,
     * when the minimum element is at the end of the list, it will take (n−1) passes to get it to the proper place at
     * the front of the list, and then one more additional pass to determine that no more swaps are needed. Space
     * complexity of bubble sort is O(1)
     */
    void bubbleSort(int[] arr) {
        // Mutates arr so that it is sorted via swapping adjacent elements until
        // the arr is sorted.
        boolean hasSwapped = true;
        while (hasSwapped) {
            hasSwapped = false;
            for (int i = 0; i < arr.length - 1; i++) {
                if (arr[i] > arr[i + 1]) {
                    // Swap adjacent elements
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    hasSwapped = true;
                }
            }
        }
    }

    @Test
    void testHeapSort() {
        int[] nums = new int[]{5, 2, 3, 1};
        heapSort(nums);
        Assertions.assertThat(nums).containsExactly(1, 2, 3, 5);
        nums = new int[]{5, 1, 1, 2, 0, 0};
        heapSort(nums);
        Assertions.assertThat(nums).containsExactly(0, 0, 1, 1, 2, 5);
    }

    /**
     * Algo:
     * 1. Build the binary heap:
     * - Organize the elements of the array into a max binary heap such that the parent node is either greater than or
     * - equal to its children nodes. In the resulting max binary heap we will have the largest element at the root node.
     * 2. Swap the root node and the last element:
     * - Swap the root node (which is the largest element) with the last element in the heap. So that we place the
     * largest element at the end, thus, trying to sort in ascending order.
     * 3. Rebuild the heap:
     * - Rebuild the heap with the new root node, and while not considering the already swapped elements from the array
     * - to satisfy the heap property.
     * 4. Repeat steps 2 and 3: Repeat steps 2 and 3 until the binary heap is empty.
     * <p>
     * Time complexity: O(n⋅log n). Heapify takes O(log n) time and we do it for n nodes
     * Space complexity of O(1), if not considering the O(log n) recursive stack
     */
    void heapSort(int[] arr) {
        // Mutates elements in lst by utilizing the max heap data structure
        // Build heap; heapify (top-down) all elements except leaf nodes.
        for (int i = arr.length / 2 - 1; i >= 0; i--) { // Tricky!
            maxHeapify(arr, arr.length, i);
        }

        for (int i = arr.length - 1; i > 0; i--) {
            // swap last element with first element. The first element in the array is the top node in the max heap,
            // which is the max value.
            int temp = arr[i];
            arr[i] = arr[0];
            arr[0] = temp;
            // note that we reduce the heap size by 1 every iteration
            maxHeapify(arr, i, 0);
        }
    }

    /**
     * Function to heapify a subtree (in top-down order) rooted at index.
     */
    private void maxHeapify(int[] arr, int heapSize, int index) {
        // If the parent node is stored at index i, the left child will be stored at index 2i + 1 and the right child
        // at index 2i + 2 (assuming the indexing starts at 0).
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int largest = index; // Initialize largest as root index
        // If left child is larger than root.
        if (left < heapSize && arr[left] > arr[largest]) {
            largest = left;
        }
        // If right child is larger than largest so far.
        if (right < heapSize && arr[right] > arr[largest]) {
            largest = right;
        }
        // If largest is not root, i.e. index, swap the value at index with the largest element
        // Recursively heapify the affected subtree rooted at largest (i.e. move down).
        if (largest != index) {
            int temp = arr[index];
            arr[index] = arr[largest];
            arr[largest] = temp;
            maxHeapify(arr, heapSize, largest);
        }
    }

    private void countingSort(int[] arr) {
        // Create the counting hash map.
        Map<Integer, Integer> counts = new HashMap<>();
        int minVal = arr[0], maxVal = arr[0];

        // Find the minimum and maximum values in the array,
        // and update it's count in the hash map.
        for (int i = 0; i < arr.length; i++) {
            minVal = Math.min(minVal, arr[i]);
            maxVal = Math.max(maxVal, arr[i]);
            counts.put(arr[i], counts.getOrDefault(arr[i], 0) + 1);
        }

        int index = 0;
        // Place each element in its correct position in the array.
        for (int val = minVal; val <= maxVal; ++val) {
            // Append all 'val's together if they exist.
            while (counts.getOrDefault(val, 0) > 0) {
                arr[index] = val;
                index += 1;
                counts.put(val, counts.get(val) - 1);
            }
        }
    }

    private void bucketSort(int[] arr, int placeValue) {
        ArrayList<List<Integer>> buckets = new ArrayList<>(10);
        for (int digit = 0; digit < 10; ++digit) {
            buckets.add(digit, new ArrayList<Integer>());
        }

        // Store the respective number based on its digit.
        for (int val : arr) {
            int digit = Math.abs(val) / placeValue;
            digit = digit % 10;
            buckets.get(digit).add(val);
        }

        // Overwrite 'arr' in sorted order of current place digits.
        int index = 0;
        for (int digit = 0; digit < 10; ++digit) {
            for (int val : buckets.get(digit)) {
                arr[index] = val;
                index++;
            }
        }
    }

    // Radix sort function.
    private void radixSort(int[] arr) {
        // Find the absolute maximum element to find max number of digits.
        int maxElement = arr[0];
        for (int val : arr) {
            maxElement = Math.max(Math.abs(val), maxElement);
        }
        int maxDigits = 0;
        while (maxElement > 0) {
            maxDigits += 1;
            maxElement /= 10;
        }

        // Radix sort, least significant digit place to most significant.
        int placeValue = 1;
        for (int digit = 0; digit < maxDigits; ++digit) {
            bucketSort(arr, placeValue);
            placeValue *= 10;
        }

        // Seperate out negatives and reverse them.
        List<Integer> negatives = new ArrayList<>();
        List<Integer> positives = new ArrayList<>();
        for (int val : arr) {
            if (val < 0) {
                negatives.add(val);
            } else {
                positives.add(val);
            }
        }
        Collections.reverse(negatives);

        // Final 'answer' will be 'negative' elements, then 'positive' elements.
        int index = 0;
        for (int val : negatives) {
            arr[index] = val;
            index++;
        }
        for (int val : positives) {
            arr[index] = val;
            index++;
        }
    }
}
