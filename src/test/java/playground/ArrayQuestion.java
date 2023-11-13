package playground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/*  TODO: Useful tips:
      - List ---> PRIMITIVE TYPE array, e.g. List<Integer> --> int[]
        1. Iterate the List and add them to the array
             int[] array = new int[list.size()];
             for(int i = 0; i < list.size(); i++) array[i] = list.get(i);
        2. int[] array = myIntList.stream().mapToInt(Integer::intValue).toArray() ==> more memory
     - List ---> REFERENCE type array, e.g. List<Foo> --> Foo[]
       Foo[] array = list.toArray(new Foo[0]); ==> new Foo[0] just means an empty array. More efficient in newer JVM
     - REFERENCE type array ---> List, e.g. String[] --> List<String>
       List<T> myList = Arrays.asList(T... a)
       Ex:
         String[] array = {"foo", "bar"};
         List<String> list = Arrays.asList(array);
         or list = Arrays.asList("foo", "bar");
     - PRIMITIVE TYPE array ---> List, e.g. int[] --> List<Integer>
        1. Iterate the array and add them to the List
	        List<Integer> output = new ArrayList<>();
            for (int value : intArray) output.add(value);
        2. List<Integer> output = IntStream.of(intArray).boxed().collect(Collectors.toList());
     - ArrayList vs LinkedList in Java --- IMPORTANT!
     https://stackoverflow.com/questions/322715/when-to-use-linkedlist-over-arraylist-in-java
     - Map tips
       1. For Map<K, List<V>>, use computeIfAbsent(myKey, k -> new ArrayList<>()).add(myValue) to simplify the logic to check if
          the map contains myKey and init the ArrayList conditionally and add the myValue to the list. (Also apply to MultiMap use case)
          https://stackoverflow.com/questions/48183999/what-is-the-difference-between-putifabsent-and-computeifabsent-in-java-8-map
     - Creating subarray
       T[] copyOfRange(T[] original, int from, int to)
       Ex: int[] remainingArray = Arrays.copyOfRange(myArray, 2, myArray.length) --> Copy all element from idx 2 to the end
 */
public class ArrayQuestion {

    /*
     TODO: MEMORIZE
      1. Reverse array(https://www.baeldung.com/java-invert-array)

     */

    /**
     * Remove Duplicates from Sorted Array
     * https://leetcode.com/problems/remove-duplicates-from-sorted-array/description/
     */
    @Test
    void removeDuplicateFromArray() {
        int[] intArray = new int[]{1, 1, 2, 3, 3, 3, 4, 5, 5, 6};
        assertThat(removeDuplicates(intArray)).isEqualTo(6);
        assertThat(intArray).containsExactly(1, 2, 3, 4, 5, 6, 4, 5, 5, 6);
    }


    //Time Complexity: O(N). Space Complexity: O(1)
    public int removeDuplicates(int[] nums) {
        if (nums.length == 0 || nums.length == 1)
            return nums.length;
        int insertedIndex = 1;
        for (int i = 1; i < nums.length; i++) {
            // We skip to next index if we see a duplicate element
            if (nums[i - 1] != nums[i]) {
//              Storing the unique element at insertIndex index and incrementing the insertIndex by 1
                nums[insertedIndex] = nums[i];
                insertedIndex++;
            }
        }
        return insertedIndex;
    }


    /**
     * Best Time to Buy and Sell Stock I
     * You are given an array prices where prices[i] is the price of a given stock on the ith day.
     * You want to maximize your profit by choosing a SINGLE day to buy one stock and choosing a different day in the future to sell that stock.
     * (Only one buy and sell transaction in the end)
     * Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.
     * https://leetcode.com/problems/best-time-to-buy-and-sell-stock/description/
     */
    @Test
    void maxProfitToBuySellStockOne() {
        int[] intArray = new int[]{7, 1, 5, 3, 6, 4};
        // Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
        // Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
        assertThat(maxProfitOne(intArray)).isEqualTo(5);
        intArray = new int[]{7, 6, 4, 3, 1};
        // no transactions are done and the max profit = 0
        assertThat(maxProfitOne(intArray)).isEqualTo(0);

    }

    // Simple One Pass approach:
    // The points of interest are the peaks and valleys in the given graph. We need to find the largest price following
    // each valley, which difference could be the max profit. We can maintain two variables - minprice and maxprofit
    // corresponding to the smallest valley and maximum profit (maximum difference between selling price and minprice)
    // obtained so far respectively.
    // Time Complexity: O(N). Space Complexity: O(1)
    int maxProfitOne(int[] prices) {
        int maxProfit = 0; // Keep track of the largest difference so far
        int minPrice = Integer.MAX_VALUE; // Keep track of the best buy day so far
        for (int i = 0; i < prices.length; i++) {
            // For each iteration we do either one of the two things
            // 1. Found a smaller minPrice: this new minPrice MAY produce the larger maxProfit later on
            // 2. Update maxProfit if the difference between current value and minPrice is bigger
            if (prices[i] < minPrice)
                minPrice = prices[i];
            else
                maxProfit = Math.max(maxProfit, prices[i] - minPrice);
        }
        return maxProfit;
    }

    /**
     * Best Time to Buy and Sell Stock II
     * You are given an integer array prices where prices[i] is the price of a given stock on the ith day.
     * On each day, you may decide to buy and/or sell the stock. You can only hold at most one share of the stock at any time.
     * However, you can buy it then immediately sell it on the same day.(Transactions on multiple days is allowed)
     * Find and return the maximum profit you can achieve.
     * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/description/
     */
    @Test
    void maxProfitToBuySellStockTwo() {
        int[] intArray = new int[]{7, 1, 5, 3, 6, 4};
        // Buy on day 2 (price = 1) and sell on day 3 (price = 5), profit = 5-1 = 4.
        // Then buy on day 4 (price = 3) and sell on day 5 (price = 6), profit = 6-3 = 3.
        // Total profit is 4 + 3 = 7.
        assertThat(maxProfitTwo(intArray)).isEqualTo(7);
        intArray = new int[]{1, 2, 3, 4, 5};
        // Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
        assertThat(maxProfitTwo(intArray)).isEqualTo(4);

    }

    // Simple One Pass approach:
    // Instead of looking for every peak following a valley, we can simply go on crawling over the slope and keep
    // on adding the profit obtained from every consecutive transaction. In the end,we will be using the peaks
    // and valleys effectively, but we need not track the costs corresponding to the peaks and valleys along with
    // the maximum profit, but we can directly keep on adding the difference between the consecutive numbers of the
    // array if the second number is larger than the first one, and at the total sum we obtain will be the maximum profit.
    // Time Complexity: O(N). Space Complexity: O(1)
    int maxProfitTwo(int[] prices) {
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1])
                maxProfit += prices[i] - prices[i - 1];
        }
        return maxProfit;
    }


    /**
     * Rotate Array
     * https://leetcode.com/problems/rotate-array/solution/
     */
    @Test
    void rotateArray() {
        int[] intArray = new int[]{1, 2, 3, 4, 5, 6, 7};
        rotate(intArray, 2);
        Assertions.assertArrayEquals(new int[]{6, 7, 1, 2, 3, 4, 5}, intArray);

    }

    /**
     * This approach is based on the fact that when we rotate the array k times, k elements from the
     * back end of the array come to the front and the rest of the elements from the front shift backwards.
     * So we firstly reverse all the elements of the array. Then, reversing the first k elements followed
     * by reversing the rest n−k elements gives us the required result.
     * Time Complexity: O(N). Space Complexity: O(1)
     */
    void rotate(int[] nums, int k) {
        k %= nums.length; // Need to do mod cuz the length may be greater than array length
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    // TODO: MEMORIZE! Use while loop to reverse array
    void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }

    /**
     * Contains Duplicate
     * https://leetcode.com/problems/contains-duplicate/solution/
     */
    @Test
    void containsDuplicate() {
        int[] intArray = new int[]{1, 1, 3, 4, 5, 6};
        Assertions.assertTrue(checkDuplicate(intArray));
    }

    /**
     * Approach 1: Sorting
     * Time Complexity: O(n log(n)). Space Complexity: O(1)
     */
    boolean checkDuplicate(int[] nums) { // O(n Log n), Space: O(1)
        Arrays.sort(nums);// JDK uses Dual-Pivot Quicksort, O(n log(n))
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Approach 2: Use Set (Slower)
     * Time Complexity: O(n). Space Complexity: O(n)
     */
    boolean checkDuplicateSet(int[] nums) {
        Set<Integer> numSet = new HashSet<>(nums.length);
        for (Integer n : nums) {
            if (numSet.contains(n))
                return true;
            numSet.add(n);
        }
        return false;
    }

    /**
     * Single Number
     * Given a non-empty array of integers nums, every element appears twice except for one. Find that single one.
     * You must implement a solution with a linear runtime complexity and use only constant extra space.
     * https://leetcode.com/problems/single-number/solution/
     */
    @Test
    void singleNumber() {
        int[] intArray = new int[]{1, 1, 3, 4, 4};
        Assertions.assertEquals(3, checkSingleNumber(intArray));
    }

    /**
     * Time Complexity: O(n). Space Complexity: O(1)
     */
    int checkSingleNumber(int[] nums) { // O(n), Space: O(1)
        int a = 0;
        for (int i : nums) {
            a ^= i;
        }
        return a;
    }

    int checkSingleNumberMap(int[] nums) { // O(n), Space: O(n)
        Map<Integer, Integer> numToCount = new HashMap<>();
        for (int num : nums) {
            Integer count = numToCount.getOrDefault(num, 0);
            numToCount.put(num, ++count);
        }
        for (Map.Entry<Integer, Integer> entry : numToCount.entrySet()) {
            if (entry.getValue().equals(1))
                return entry.getKey();
        }
        return 0;
    }

    /**
     * Intersection of Two Arrays
     * https://leetcode.com/problems/intersection-of-two-arrays/solution/
     */
    @Test
    void findIntersect() {
        int[] intArray1 = new int[]{1, 2, 2, 1};
        int[] intArray2 = new int[]{2, 2};
        Assertions.assertArrayEquals(new int[]{2}, intersectUnique(intArray1, intArray2));
        // Another approach is using the Map solution of the findIntersectTwo question and
        // put the matched integer in a Set instead of the original array
    }

    //Time Complexity: O(n + m). Space Complexity: O(n+m)
    int[] intersectUnique(int[] nums1, int[] nums2) {
        // TODO: Useful tips => Converting int array to Set using Stream
        Set<Integer> set1 = Arrays.stream(nums1).boxed().collect(Collectors.toSet());
        Set<Integer> set2 = Arrays.stream(nums2).boxed().collect(Collectors.toSet());
        set1.retainAll(set2);
        return set1.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Intersection of Two Arrays II
     * https://leetcode.com/problems/intersection-of-two-arrays-ii/solution/
     */
    @Test
    void findIntersectTwo() {
        int[] intArray1 = new int[]{4, 9, 5};
        int[] intArray2 = new int[]{9, 4, 9, 8, 4};
        Assertions.assertArrayEquals(new int[]{9, 4}, intersect(intArray1, intArray2));
    }

    //Time Complexity: O(n + m). Space Complexity: O(min(n,m))
    int[] intersect(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length)
            // check array sizes and use a hash map for the smaller array
            intersect(nums2, nums1);
        Map<Integer, Integer> valToCount = new HashMap<>();
        for (int i : nums1) {
            valToCount.put(i, valToCount.getOrDefault(i, 0) + 1);
        }
        int k = 0;
        for (int i : nums2) {
            int count = valToCount.getOrDefault(i, 0);
            if (count > 0) {
                // When we find common numbers, copy them to the first array starting from the beginning
                nums1[k++] = i;
                valToCount.put(i, --count);
            }
        }
        return Arrays.copyOfRange(nums1, 0, k);
    }

    /**
     * When the two input arrays are sorted, or the output needs to be sorted.
     * This approach can be considered.
     * Time Complexity: O(nlog n + mlog m). Space Complexity: O(n+m)
     */
    int[] intersectWithSort(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int i = 0, j = 0, k = 0;
        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] < nums2[j])
                ++i;
            else if (nums1[i] > nums2[j])
                ++j;
            else {
                // nums1[i] == nums2[j]
                nums1[k++] = nums1[i++];
                j++;
            }
        }
        return Arrays.copyOfRange(nums1, 0, k);
    }


    /**
     * Plus One
     * You are given a large integer represented as an integer array digits, where each digits[i] is the
     * ith digit of the integer. The digits are ordered from most significant to least significant in
     * left-to-right order. The large integer does not contain any leading 0's.
     * Increment the large integer by one and return the resulting array of digits.
     * https://leetcode.com/problems/plus-one/solution/
     */
    @Test
    void testPlusOne() {
        int[] intArray1 = new int[]{4, 3, 2, 1};
        int[] intArray2 = new int[]{1, 2, 9};
        int[] intArray3 = new int[]{9, 9};

        Assertions.assertArrayEquals(new int[]{4, 3, 2, 2}, plusOne(intArray1));
        Assertions.assertArrayEquals(new int[]{1, 3, 0}, plusOne(intArray2));
        Assertions.assertArrayEquals(new int[]{1, 0, 0}, plusOne(intArray3));
    }

    //Time Complexity: O(n). Space Complexity: O(n)
    public int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            // Move from the end
            if (digits[i] == 9)
                // We set every 9 to 0 before we encounter any non-nine digit
                digits[i] = 0;
            else {
                // Here we encounter the rightmost not-nine digit, so we increase this by 1
                digits[i]++;
                // We are done here!
                return digits;
            }
        }
        /*
        We're here because all the digits were equal to nine. Now they have all been set to zero.
        We then append the digit 1 in front of the other digits and return the result.
        For example, 999 ---> 1000
         */
        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;
    }

    /**
     * Move Zeroes
     * https://leetcode.com/problems/move-zeroes/solution/
     */
    @Test
    void testMoveZeroes() {
        int[] nums = new int[]{0, 1, 0, 3, 12};
        moveZeroes(nums);
        Assertions.assertArrayEquals(new int[]{1, 3, 12, 0, 0}, nums);
    }

    // Time Complexity: O(n). Space Complexity: O(1)
    void moveZeroes(int[] nums) {
        // To track the index where the next non-zero element should be placed.
        int nextNonZeroValInsertIndx = 0;
        for (int num : nums) {
            if (num != 0) {
                // If the current element is non-zero, assign it to the position nums[i] in the array.
                // This moves the non-zero element to the left side of the array.
                nums[nextNonZeroValInsertIndx] = num;
                nextNonZeroValInsertIndx++;
            }
        }
        // At this point, we already moved all non-zero element to the left side.
        for (int i = nextNonZeroValInsertIndx; i < nums.length; i++) {
            // Start from nextNonZeroValInsertIndx and set every element to zero until the end
            nums[i] = 0;
        }
    }


    /**
     * Two Sum
     * Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
     * You may assume that each input would have exactly one solution, and you may not use the same element twice.
     * <p>
     * Input: nums = [2,7,11,15], target = 9
     * Output: [0,1]
     * https://leetcode.com/problems/two-sum/solution/
     */
    @Test
    void testTwoSum() {
        int[] nums = new int[]{2, 7, 11, 15};
        Assertions.assertArrayEquals(new int[]{0, 1}, twoSum(nums, 9));
        nums = new int[]{3, 3};
        Assertions.assertArrayEquals(new int[]{0, 1}, twoSum(nums, 6));
        nums = new int[]{3, 2, 4};
        Assertions.assertArrayEquals(new int[]{1, 2}, twoSum(nums, 6));
    }

    /**
     * One-pass Hash Table
     * While we are iterating and inserting elements into the hash table, we also look back to check
     * if current element's complement already exists in the hash table. If it exists, we have found
     * a solution and return the indices immediately.
     * Time Complexity: O(n). Space Complexity: O(n)
     */
    int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> valToIdx = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            var complement = target - nums[i];
            if (valToIdx.containsKey(complement)) {
                return new int[]{valToIdx.get(complement), i};
            }
            valToIdx.put(nums[i], i);
        }
        return null;
    }

    /**
     * Two Sum II - Input Array Is Sorted
     * Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order,
     * find two numbers such that they add up to a specific target number. Let these two numbers be
     * numbers[index1] and numbers[index2] where 1 <= index1 < index2 < numbers.length.
     * <p>
     * Return the indices of the two numbers, index1 and index2, added by one as an integer array [index1, index2] of length 2.
     * The tests are generated such that there is exactly one solution. You may not use the same element twice.
     * Your solution MUST use only constant extra space.
     * <p>
     * Input: numbers = [2,7,11,15], target = 9
     * Output: [1,2]
     * Explanation: The sum of 2 and 7 is 9. Therefore, index1 = 1, index2 = 2. We return [1, 2].
     * <p>
     * Input: numbers = [2,3,4], target = 6
     * Output: [1,3]
     * Explanation: The sum of 2 and 4 is 6. Therefore index1 = 1, index2 = 3. We return [1, 3].
     * <p>
     * https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/description/
     */
    @Test
    void testTwoSumII() {
        int[] nums = new int[]{2, 7, 11, 15};
        assertThat(twoSumII(nums, 9)).containsOnly(1, 2);
    }

    /**
     * Two Pointers at head and tail - move one of them depending on the comparison of their sum and target
     * We use two indices, initially pointing to the first and the last element, respectively.
     * Compare the sum of these two elements with target. If the sum is equal to target, we found the
     * exactly only solution. If it is less than target, we increase the smaller index by one. If it is
     * greater than target, we decrease the larger index by one. Move the indices and repeat the comparison
     * until the solution is found. This approach uses the fact of assending order, moveing to right will
     * increase the sum, while mvoing to left will decrease.
     * <p>
     * Time complexity: O(n).
     * Space complexity: O(1)
     */
    int[] twoSumII(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int sum = nums[left] + nums[right];
            if (sum > target)
                --right;
            else if (sum < target)
                ++left;
            else
                return new int[]{++left, ++right};
        }
        return new int[0];
    }

    /**
     * 3Sum
     * Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]]
     * such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
     * <p>
     * Notice that the solution set must not contain duplicate triplets.
     * <p>
     * Input: nums = [-1,0,1,2,-1,-4]
     * Output: [[-1,-1,2],[-1,0,1]]
     * Explanation:
     * nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0.
     * nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0.
     * nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0.
     * The distinct triplets are [-1,0,1] and [-1,-1,2].
     * Notice that the order of the output and the order of the triplets does not matter.
     * <p>
     * https://leetcode.com/problems/3sum/description/
     */
    @Test
    void testThreeSum() {
        int[] nums = new int[]{-1, 0, 1, 2, -1, -4};
        assertThat(threeSum(nums)).containsOnly(List.of(-1, -1, 2), List.of(-1, 0, 1));
    }

    /**
     * Double for loops - Fix the first number and iterate the elements after it and use Set to look up visited number
     * This is the extension of the 2 Sum question. This solution doesn't sort/change the input array so it has
     * worse performance(check Leetcode for other solutions)
     * Algo:
     * Start iterating num array from beginning (Outer loop)
     * - First we check if we have visited the num before from the outter loop in a Set
     * - If not, this means now we pick the first number
     * - 	Start iterating num array from the next elemnt of it
     * - 		This means now we pick the second number, so we compute the compliment, which means the 3rd number we need
     * - 		Check if we have visited such number from the visitedNum Set in the inner loop
     * - 			If we did, construct the triplet and sort it and add to the result
     * - 		Add the number to the visitedNum Set
     * - Return the results
     * <p>
     * Time Complexity: O(n^2). We have outer and inner loops, each going through n elements.
     * <p>
     * Space Complexity: O(n) for the hashset/hashmap.
     */
    List<List<Integer>> threeSum(int[] nums) {
        Set<List<Integer>> results = new HashSet<>();
        Set<Integer> outerLoopVisited = new HashSet<>(); // Keep track of the number we visited
        for (int i = 0; i < nums.length; i++) { // Fix the first number
            if (outerLoopVisited.add(i)) { // skip the duplicate
                Set<Integer> visitedNum = new HashSet<>(); // Keep track the num we've seen
                for (int j = i + 1; j < nums.length; j++) { // Inner loop starts form the next element
                    int compliment = -nums[i] - nums[j]; // The 3rd number we need to form the triplet
                    if (visitedNum.contains(compliment)) { // Look back to check if we have seen it
                        List<Integer> triplet = Arrays.asList(nums[i], nums[j], compliment);
                        triplet.sort(null); // Need to sort it otherwise to avoid same elements but different order
                        results.add(triplet);
                    }
                    visitedNum.add(nums[j]);
                }
            }
        }
        return new ArrayList<>(results);
    }


    /**
     * Valid Sudoku
     * https://leetcode.com/problems/valid-sudoku/solution/
     * EPI 5.17
     */
    @Test
    void testSudoku() {
        char[][] board = {
                {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };

        Assertions.assertTrue(isValidSudoku(board));
        char[][] boardTwo = {
                {'8', '3', '.', '.', '7', '.', '.', '.', '.'},
                {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
        };
        Assertions.assertFalse(isValidSudoku(boardTwo));
    }

    /**
     * 1. Use three HashSet char arrays to store the char from the input for rows, columns and boxes(one box is 3x3).
     * 2. Iterate the input 2D array, for each (r,c), check the existence in the associated hashset for the row, column and box respectively. If it exists, return false, i.e. invalid sudoku. otherwise, add to the set.
     * 3. Return true after the end of loop
     * Time Complexity: O(n^2). Space Complexity: O(n^2)
     */
    boolean isValidSudoku(char[][] board) {
        final int N = 9;
        HashSet<Character>[] rows = new HashSet[N];
        HashSet<Character>[] cols = new HashSet[N];
        HashSet<Character>[] boxes = new HashSet[N];
        for (int i = 0; i < N; i++) {
            rows[i] = new HashSet<>();
            cols[i] = new HashSet<>();
            boxes[i] = new HashSet<>();
        }
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                char val = board[r][c];
                // Skip the dot
                if (val == '.')
                    continue;
                // Check the row
                if (rows[r].contains(val))
                    return false;
                rows[r].add(val);

                // Check the column
                if (cols[c].contains(val))
                    return false;
                cols[c].add(val);

                /*
                Here is the KEY. Given row and column index, we need to figure out a way to assign the position(box) to
                one of the 9 boxes.
                Same logic as flattening a 2d matrix to linear array, (row_num * total_num_of_columns)+(col_num)

                Think about if you are at position x = 4, y = 4 (the very center cell). You are also in block 4.
                If you evaluate row/3 you get 1 which essentially means you skipped 1 row of blocks. A block row has
                3 blocks in it (thus *3). So in this scenario:
                (row/3)*3 = (4/3)*3 = 1*3 = 3 // you are skipping 3 blocks in the first row
                But how many blocks are you skipping in the second row?
                y/3 = 4/3 = 1 block
                So you have skipped over 4 blocks total, and you are on the 5th block, but it's indexed as 4 due to 0.
                 */
                int boxIdx = (r / 3) * 3 + c / 3;

                // Check the box
                if (boxes[boxIdx].contains(val))
                    return false;
                boxes[boxIdx].add(val);
            }
        }
        return true;
    }

    //Time Complexity: O(1). Space Complexity: O(1) Cuz the board is always 9x9, 81 cells.
    boolean isValidSudokuWithMap(String[][] board) {
        // init data
        Map<Integer, Integer>[] rows = new HashMap[9];
        Map<Integer, Integer>[] columns = new HashMap[9];
        Map<Integer, Integer>[] boxes = new HashMap[9];
        for (int i = 0; i < 9; i++) {
            rows[i] = new HashMap<>();
            columns[i] = new HashMap<>();
            boxes[i] = new HashMap<>();
        }

        // validate a board
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String num = board[i][j];
                if (!num.equals(".")) {
                    int n = Integer.parseInt(num);
                    /*
                    Here the KEY. Given row and column index, we need to figure out a way to assign the position(box) to
                    one of the 9 boxes.
                    Same logic as flattening a 2d matrix to linear array, (row_num * total_num_of_columns)+(col_num)

                    Think about if you are at position x = 4, y = 4 (the very center cell). You are also in block 4.
                    If you evaluate row/3 you get 1 which essentially means you skipped 1 row of blocks. A block row has
                    3 blocks in it (thus *3). So in this scenario:
                    (row/3)*3 = (4/3)*3 = 1*3 = 3 // you are skipping 3 blocks in the first row
                    But how many blocks are you skipping in the second row?
                    y/3 = 4/3 = 1 block
                    So you have skipped over 4 blocks total, and you are on the 5th block, but it's indexed as 4 due to 0.
                     */
                    int boxIndex = (i / 3) * 3 + j / 3;

                    // keep the current cell value
                    rows[i].put(n, rows[i].getOrDefault(n, 0) + 1);
                    columns[j].put(n, columns[j].getOrDefault(n, 0) + 1);
                    boxes[boxIndex].put(n, boxes[boxIndex].getOrDefault(n, 0) + 1);

                    // check if this value has been already seen before
                    if (rows[i].get(n) > 1 || columns[j].get(n) > 1 || boxes[boxIndex].get(n) > 1)
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Rotate Image/Matrix
     * https://leetcode.com/problems/rotate-image/solution/
     * EPI 5.19
     */
    @Test
    void testRotateMatrix() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        int[][] answer = {
                {7, 4, 1},
                {8, 5, 2},
                {9, 6, 3}
        };
        rotate(matrix);

        Assertions.assertTrue(Arrays.deepEquals(answer, matrix));
    }

    /**
     * The most elegant solution for rotating the matrix is to firstly reverse the matrix around the main diagonal,
     * and then reverse it from left to right. These operations are called transpose and reflect in linear algebra.
     * Time Complexity: O(N). Space Complexity: O(1)
     */
    void rotate(int[][] matrix) {
        transpose(matrix);
        reflect(matrix);
    }

    // Reverse the matrix around the main diagonal
    void transpose(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i; j < matrix.length; j++) { //TODO: Caution! j=i cuz we want to begin w/ diagonal
                var tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }
    }

    // Reverse each row in the matrix from left to right
    // TODO: MEMORIZE! Reverse array
    void reflect(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length / 2; j++) {
                var tmp = matrix[i][j];
                matrix[i][j] = matrix[i][matrix.length - 1 - j];
                matrix[i][matrix.length - 1 - j] = tmp;
            }
        }
    }

    /**
     * Set Matrix Zeroes
     * Given an m x n integer matrix matrix, if an element is 0, set its entire row and column to 0's.
     * You must do it in place.
     * <p>
     * Input: matrix = [[1,1,1],[1,0,1],[1,1,1]]
     * Output: [[1,0,1],[0,0,0],[1,0,1]]
     * <p>
     * Input: matrix = [[0,1,2,0],[3,4,5,2],[1,3,1,5]]
     * Output: [[0,0,0,0],[0,4,5,0],[0,3,1,0]]
     * <p>
     * https://leetcode.com/problems/set-matrix-zeroes/description/
     */
    @Test
    void testSetZeroes() {
        int[][] matrix = {
                {1, 1, 1},
                {1, 0, 1},
                {1, 1, 1}
        };
        int[][] answer = {
                {1, 0, 1},
                {0, 0, 0},
                {1, 0, 1}
        };
        setZeroes(matrix);
        assertThat(matrix).isEqualTo(answer);
    }

    /**
     * Use the first cell of every row and column to track the rows and columns that should be set to zero and update the matrix later
     * <p>
     * We don't want to use any additional memory so  we can use the first cell of every row and column as a flag.
     * This flag would determine whether a row or column has been set to zero.
     * if cell[i][j] == 0 {
     * cell[i][0] = 0
     * cell[0][j] = 0
     * }
     * These flags are used later to update the matrix. If the first cell of a row is set to zero this means the row
     * should be marked zero. If the first cell of a column is set to zero this means the column should be marked zero.
     * <p>
     * Algorithm
     * <p>
     * 1. We iterate over the matrix and we mark the first cell of a row i and first cell of a column j, if the condition
     * in the pseudo code above is satisfied. i.e. if cell[i][j] == 0.
     * <p>
     * 2. The first cell of row and column for the first row and first column is the same i.e. cell[0][0]. Hence,
     * we use an additional variable to tell us if the first column had been marked or not and the cell[0][0] would be
     * used to tell the same for the first row.
     * <p>
     * 3. Now, we iterate over the original matrix starting from second row and second column i.e. matrix[1][1] onwards.
     * For every cell we check if the row r or column c had been marked earlier by checking the respective first row cell
     * or first column cell. If any of them was marked, we set the value in the cell to 0. Note the first row and first
     * column serve as the row_set and column_set that we used in the first approach.
     * <p>
     * 4.We then check if cell[0][0] == 0, if this is the case, we mark the first row as zero.
     * <p>
     * 5.And finally, we check if the first column was marked, we make all entries in it as zeros.
     * <p>
     * Time Complexity : O(M×N)
     * Space Complexity : O(1)
     */
    void setZeroes(int[][] matrix) {
        boolean setFirstColZero = false;
        int rowNum = matrix.length, colNum = matrix[0].length;
        for (int i = 0; i < rowNum; i++) {
            // Since the first cell, i.e. matrix[0][0], for both first row and first column is the same
            // We need an additional variable for either the first row/column.
            // Here we are use setFirstColZero for the first column and matrix[0][0] for the first row.
            for (int j = 0; j < colNum; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0; // Set the first cell of the same row to zero
                    if (j == 0) // If we see zero at any cell in the 1st column, set the flag
                        setFirstColZero = true;
                    else
                        matrix[0][j] = 0; // Set the first cell of the same column to zero
                }
            }
        }
        // Iterate over the array once again(except for the first row and column) and use the info from the
        // first row and first column to update the elements.
        for (int i = 1; i < rowNum; i++) {
            for (int j = 1; j < colNum; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        // See if the first row needs to be set to zero as well
        if (matrix[0][0] == 0) {
            // First row should be set to zero
            for (int i = 0; i < colNum; i++)
                matrix[0][i] = 0;
        }
        // See if the first column needs to be set to zero as well
        if (setFirstColZero) {
            for (int i = 0; i < rowNum; i++)
                matrix[i][0] = 0;
        }
    }

    /**
     * Group Anagrams
     * Given an array of strings strs, group the anagrams together. You can return the answer in any order.
     * <p>
     * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase,
     * typically using all the original letters exactly once.
     * <p>
     * Input: strs = ["eat","tea","tan","ate","nat","bat"]
     * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
     * <p>
     * https://leetcode.com/problems/group-anagrams/editorial/
     */
    @Test
    void testGroupAnagrams() {
        String[] strs = new String[]{"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> result = groupAnagrams(strs);
        assertThat(result).containsOnly(List.of("bat"), List.of("tan", "nat"), List.of("eat", "tea", "ate"));
    }

    /**
     * Use Map(count of each char -> str list) for grouping
     * Similar to the concept used in the Anagram problem. Two strings are anagrams if and only if their
     * character counts (respective number of occurrences of each character) are the same.
     * <p>
     * We can transform each string s into a character count, consisting of 26 non-negative integers
     * representing the number of a's, b's, c's, etc. We use these counts as the key for our hash map.
     * <p>
     * The count will be a string delimited with '#' characters. For example, abbccc will be
     * #1#2#3#0#0#0...#0 where there are 26 entries total. Without the delimiter, in some edge case,
     * For example, aaab and abbb wil yield the same key as 111100...00
     * <p>
     * Note, another implementation is to sort each string and use it as the key of the map(sortedStr -> list)
     * theoretically this should have worse time complexity but on Leetcode it runs faster
     * and uses less memory.
     * <p>
     * Time Complexity: O(NK), where N is the length of strs, and K is the maximum length of
     * a string in strs. Counting each string is linear in the size of the string, and we
     * count every string.
     * <p>
     * Space Complexity: O(NK), the total information content stored in ans.
     */
    List<List<String>> groupAnagrams(String[] strs) {
        if (strs.length == 0) return new ArrayList<>();
        Map<String, List<String>> charCountToStrs = new HashMap<>();
        int[] lowercaseCharCount = new int[26];
        for (String str : strs) {
            Arrays.fill(lowercaseCharCount, 0);
            for (char c : str.toCharArray())
                ++lowercaseCharCount[c - 'a'];
            StringBuilder stb = new StringBuilder();
            for (int count : lowercaseCharCount)
                stb.append("#").append(count);
            charCountToStrs.putIfAbsent(stb.toString(), new ArrayList<>());
            charCountToStrs.get(stb.toString()).add(str);
        }
        return new ArrayList<>(charCountToStrs.values());
    }

    /**
     * Longest Substring Without Repeating Characters
     * Given a string s, find the length of the longest substring without repeating characters.
     * <p>
     * Input: s = "abcabcbb"
     * Output: 3
     * Explanation: The answer is "abc", with the length of 3.
     * <p>
     * Input: s = "bbbbb"
     * Output: 1
     * Explanation: The answer is "b", with the length of 1.
     * <p>
     * https://leetcode.com/problems/longest-substring-without-repeating-characters/description/
     */
    @Test
    void testLengthOfLongestSubstring() {
        assertThat(lengthOfLongestSubstring("abba")).isEqualTo(2);
        assertThat(lengthOfLongestSubstring("abcabcbb")).isEqualTo(3);
    }

    /**
     * Sliding Window and Map to track the last seen char and index
     * Use two ptrs to iterate the array and maintain a sliding window.
     * We use a Map to track the char we have seen and its last-seen index
     * Algo:
     * 1. Start iterating/expanding the right side of the window
     * 2. If this char is seen before(in the Map) and also within the current window range(idx >= left ptr)
     * 3. 	 Shrink the window ==> Move the left ptr to the last seen index + 1
     * 4. Compute the current max length
     * 5. Update the char and its index in the Map
     * <p>
     * Time complexity : O(n)
     * Space complexity : O(n), for the map
     */
    int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> charToIndex = new HashMap<>();// Value is the index of the char we saw last time
        int maxLength = 0;
        int length = s.toCharArray().length;
        for (int i = 0, j = 0; j < length; j++) { // i and j represent the left and right side of the sliding window
            char currChar = s.charAt(j);
            if (charToIndex.containsKey(currChar) && charToIndex.get(currChar) >= i)
                // Shrink the current window, i.e. move i ptr forward, ONLY when
                // 1. We saw the same char before
                // 2. Its last-seen index is inside the current window range, i.e. index >= i. ---> IMPORTANT!!
                //    We only move the i forward inside the current window range, any index val outside of if is useless.
                //    Ex. input: abba
                //        When the window is at "ba", i=2, j=3, the map still has a->0, so we should ignore it.
                i = charToIndex.get(currChar) + 1; // Move the i ptr. We need to move it one more position to skip this duplicate char
            maxLength = Math.max(maxLength, j - i + 1);
            charToIndex.put(currChar, j); // Update the last seen index for this char
        }
        return maxLength;
    }


    /**
     * Increasing Triplet Subsequence
     * Given an integer array nums, return true if there exists a triple of indices (i, j, k)
     * such that i < j < k and nums[i] < nums[j] < nums[k]. If no such indices exists, return false.
     * <p>
     * Input: nums = [2,1,5,0,4,6]
     * Output: true
     * Explanation: The triplet (3, 4, 5) is valid because nums[3] == 0 < nums[4] == 4 < nums[5] == 6.
     * <p>
     * Input: nums = [5,4,3,2,1]
     * Output: false
     * Explanation: No triplet exists.
     * <p>
     * https://leetcode.com/problems/increasing-triplet-subsequence/description/
     */
    @Test
    void testIncreasingTriplet() {
        assertThat(increasingTriplet(new int[]{1, 2, 3, 4, 5})).isTrue();
        // In this test case, when we are at 13, first is 5 and second is 12. Although this is not the triplet, the answer doesn't change.
        assertThat(increasingTriplet(new int[]{20, 100, 10, 12, 5, 13})).isTrue();
    }

    /**
     * Iterate array while updating the min and second min variables in if-else-if-else
     * The idea is to keep track of the first two numbers in increasing order and find the last number
     * which will be bigger than the first two numbers. Here, the first and second smallest numbers can
     * be updated with conditional checks while scanning nums.
     * Algo:
     * first_num = second_num = some very big number
     * for n in nums:
     * -     if n is the smallest number:
     * -         first_num = n
     * -     else if n is second smallest number:
     * -         second_num = n
     * -     else n is bigger than both first_num and second_num:
     * -         # We have found our triplet, return True
     * # After loop has terminated
     * # If we have reached this point, there is no increasing triplet, return False
     * <p>
     * Time complexity : O(N)
     * Space complexity : O(1)
     */
    boolean increasingTriplet(int[] nums) {
        if (nums.length < 3)
            return false;
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= first) // Need to include equal
                first = nums[i];
            else if (nums[i] <= second)
                second = nums[i];
            else
                return true;
        }
        return false;
    }

    /**
     * Missing Ranges
     * You are given an inclusive range [lower, upper] and a sorted unique integer array nums,
     * where all elements are within the inclusive range.
     * A number x is considered missing if x is in the range [lower, upper] and x is not in nums.
     * Return the shortest sorted list of ranges that exactly covers all the missing numbers.
     * That is, no element of nums is included in any of the ranges, and each missing number is covered by one of the ranges.
     * <p>
     * Input: nums = [0,1,3,50,75], lower = 0, upper = 99
     * Output: [[2,2],[4,49],[51,74],[76,99]]
     * Explanation: The ranges are:
     * [2,2]
     * [4,49]
     * [51,74]
     * [76,99]
     * <p>
     * Input: nums = [-1], lower = -1, upper = -1
     * Output: []
     * Explanation: There are no missing ranges since there are no missing numbers.
     * <p>
     * https://leetcode.com/problems/missing-ranges/description/
     */
    @Test
    void testFindMissingRanges() {
        assertThat(findMissingRanges(new int[]{0, 1, 3, 50, 75}, 0, 99))
                .containsExactly(List.of(2, 2), List.of(4, 49), List.of(51, 74), List.of(76, 99));
        assertThat(findMissingRanges(new int[]{-1}, -1, -1))
                .isEmpty();
    }

    /**
     * Iterate array and compare each element and next element to generate the missing range
     * We need to consider the edge cases
     * 1. When lower is less than the first element in array, we need to add [lower, nums[0] - 1]
     * 2. When uperr is larger than the last element in array, we need to add [[nums.length - 1], upper] in the end
     * Other than the above two ranges, when we iterate the arrary, if nums[i + 1] - nums[i] > 1, we need to generate
     * a missing range.
     * Time complexity : O(N)
     * Space complexity : O(1)
     */
    List<List<Integer>> findMissingRanges(int[] nums, int lower, int upper) {
        List<List<Integer>> results = new ArrayList<>();
        if (nums.length == 0) {
            results.add(List.of(lower, upper));
            return results;
        }

        // Check for any missing numbers between the lower bound and nums[0].
        if (lower < nums[0]) {
            results.add(List.of(lower, nums[0] - 1));
        }
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i + 1] - nums[i] > 1)
                results.add(List.of(nums[i] + 1, nums[i + 1] - 1));
        }
        // Check for any missing numbers between successive elements of nums.
        if (upper > nums[nums.length - 1])
            results.add(List.of(nums[nums.length - 1] + 1, upper));
        return results;
    }

    /**
     * Count and Say
     * The count-and-say sequence is a sequence of digit strings defined by the recursive formula:
     * <p>
     * countAndSay(1) = "1"
     * countAndSay(n) is the way you would "say" the digit string from countAndSay(n-1), which is then
     * converted into a different digit string.
     * To determine how you "say" a digit string, split it into the minimal number of substrings such
     * that each substring contains exactly one unique digit. Then for each substring, say the number
     * of digits, then say the digit. Finally, concatenate every said digit.
     * <p>
     * For example, the saying and conversion for digit string "3322251":
     * Two 3, three 2, one 5, and one 1 ==> 23 32 15 11
     * <p>
     * Input: n = 1
     * Output: "1"
     * Explanation: This is the base case.
     * <p>
     * Input: n = 4
     * Output: "1211"
     * Explanation:
     * countAndSay(1) = "1"
     * countAndSay(2) = say "1" = one 1 = "11"
     * countAndSay(3) = say "11" = two 1's = "21"
     * countAndSay(4) = say "21" = one 2 + one 1 = "12" + "11" = "1211"
     * <p>
     * https://leetcode.com/problems/count-and-say/description/
     */
    @Test
    void testCountAndSay() {
        assertThat(countAndSay(6)).isEqualTo("312211");
    }

    /**
     * Use an outer for loop as countAndSay(i) call, the inner for loop uses two ptrs to scan every section
     * of the same char and generate and concatenate "count"+"char" substrings
     * <p>
     * We have two for loops. The outer one starts from i=2, each iteration will produce a string for
     * the countAndSay(i) and this string is used for the next iteration until i=n.
     * In the inner loop, we use two ptr, one is moved forward until the next different char, then we use these
     * two ptr to compute the "count"+"char" substring. We continue to append the substring until it scan
     * the whole string.
     * <p>
     * Time Complexity: O(4^(n/3)
     * Each 3 iterations a single digit becomes 4 digits. If we treat every three iterations as a
     * recursion, since we have n iterations, we then have n/3 such recursions. During each recursion
     * a digit becomes fourfold, then after (n/3) recursions we have 4^(n/3) digits.
     * *Note: LeetCode has more detailed(complex) explanation
     * <p>
     * Space Complexity: O(4^(n/3)
     */
    String countAndSay(int n) {
        String currentString = "1";
        for (int i = 2; i <= n; i++) {
            // Each iteration in the outer loop means a "countAndSay(i)" sequence generation process. We start from TWO!
            StringBuilder nextString = new StringBuilder();
            for (int currCharIdx = 0, nextDiffCharIdx = 0; currCharIdx < currentString.length(); currCharIdx = nextDiffCharIdx) {
                // Each iteration we move nextDiffCharIdx until it encounters a different char, then generate a substring of the result
                // We set the currCharIdx to equal nextDiffCharIdx, so we can reset the substring generation logic for the next char
                while (nextDiffCharIdx < currentString.length()
                        && currentString.charAt(nextDiffCharIdx) == currentString.charAt(currCharIdx)) {
                    // Advancing the nextDiffCharIdx until it moves to the next different char
                    nextDiffCharIdx++;
                }
                // Generate a substring consist of "count" + "char"
                nextString.append(nextDiffCharIdx - currCharIdx) // count of the current char (All chars are the same in this range)
                        .append(currentString.charAt(currCharIdx)); // char we are currently checking
            }
            // Here is the end of one "countAndSay(i)" sequence, so we update currentString as input for next iteration
            currentString = nextString.toString();
        }
        return currentString;
    }

    /**
     * Majority Element
     * Given an array nums of size n, return the majority element.
     * <p>
     * The majority element is the element that appears more than ⌊n / 2⌋ times. You may assume that the majority element always exists in the array.
     * <p>
     * Input: nums = [3,2,3]
     * Output: 3
     * <p>
     * <p>
     * Input: nums = [2,2,1,1,1,2,2]
     * Output: 2
     * <p>
     * https://leetcode.com/problems/majority-element/description/
     */
    @Test
    void testMajorityElement() {
        assertThat(majorityElementUseMap(new int[]{3, 2, 3})).isEqualTo(3);
        assertThat(majorityElementUseMap(new int[]{2, 2, 1, 1, 1, 2, 2})).isEqualTo(2);
        assertThat(majorityElement(new int[]{3, 2, 3})).isEqualTo(3);
        assertThat(majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2})).isEqualTo(2);
    }

    /**
     * 1. Maintain two int vars, count and num, both init to 0.
     * 2. Iterate the array
     * -  Set the current element to num when count is 0
     * -  Increment the count when the cuurent element is the same as num, otherwise decrement it
     * 3. Return num after loop terminates
     * <p>
     * Whenever count equals 0, we effectively forget about everything in nums up to the current
     * index and consider the current number. This alog is Boyer-Moore Voting Algorithm
     * <p>
     * This algo is hard to understand, check NeetCode video
     * https://www.youtube.com/watch?v=7pnhv842keE&ab_channel=NeetCode
     * <p>
     * Time complexity : O(n)
     * Space complexity : O(1)
     */
    int majorityElement(int[] nums) {
        int count = 0, majorityNum = 0;
        for (int num : nums) {
            if (count == 0)
                majorityNum = num;
            if (num == majorityNum)
                ++count;
            else
                --count;
        }
        return majorityNum;
    }

    /**
     * Use a HashMap that maps elements to counts in order to count
     * occurrences in linear time by looping over nums. Then, we simply return the
     * key with maximum value.
     * Time complexity : O(n)
     * Space complexity : O(n)
     */
    int majorityElementUseMap(int[] nums) {
        Map<Integer, Integer> numToCount = new HashMap<>();
        for (int num : nums) {
            Integer count = numToCount.getOrDefault(num, 0);
            numToCount.put(num, count + 1);
        }
        int maxCount = Integer.MIN_VALUE;
        int maxNum = 0;

        for (Map.Entry<Integer, Integer> entry : numToCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxNum = entry.getKey();
            }
        }
        return maxNum;
    }

    /**
     * Task Scheduler
     * Given a characters array tasks, representing the tasks a CPU needs to do, where each
     * letter represents a different task. Tasks could be done in any order. Each task is
     * done in one unit of time. For each unit of time, the CPU could complete either one task or just be idle.
     * <p>
     * However, there is a non-negative integer n that represents the cooldown period between two same
     * tasks (the same letter in the array), that is that there must be at least n units of time between any
     * two same tasks.
     * <p>
     * Return the least number of units of times that the CPU will take to finish all the given tasks.
     * <p>
     * Input: tasks = ["A","A","A","B","B","B"], n = 2
     * Output: 8
     * Explanation:
     * A -> B -> idle -> A -> B -> idle -> A -> B
     * There is at least 2 units of time between any two same tasks.
     * <p>
     * Constraints:
     * <p>
     * 1 <= task.length <= 104
     * tasks[i] is upper-case English letter.
     * The integer n is in the range [0, 100].
     * <p>
     * https://leetcode.com/problems/task-scheduler/description/
     */
    @Test
    void testLeastInterval() {
        assertThat(leastInterval(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2)).isEqualTo(8);
    }

    /**
     * The solution is based on Math to generalize a formula.
     * The trick is we need to start from the most frequent job will have most impact, which is the most frequent
     * job. For example: [A, A, A, B, B, C] and cool down time is n = 2, then we can first ave the schedule like
     * A _ _ A _ _ A. We need 2 idle units and 1 unit for A for each (f-1) A, where f is frequency of A + another
     * 1 unit for last A. So, we found a formula to find number of units needed for most frequent job:
     * (n+1) * (f-1) + 1.
     * <p>
     * What if we have two type of tasks both are most frequent?[A, A, A, B, B, B, C] and n=2.
     * Now we have 3 Bs as well. As you can see that we can just fit the B in existing empty space. And the only
     * extra unit will be needed for last B which is placed at the end next to last A. So, if there are two most
     * frequent element the answer would be: (n+1)*(f-1) + 2.
     * <p>
     * Generalizing this if we have X number of most frequent jobs then answer would be (n+1)*(f-1) + X.
     * <p>
     * But what if we have two other jobs D and E? for example [A, A, A, B, B, C, C, D, E]
     * After scheduling most frequent one and then filling B and C, scheduling will look like A B C A B C A.
     * To put D and E we will extend it like below.
     * <p>
     * A B C _ A B C_ A
     * <p>
     * And put D and E on those empty space. A B C D A B C E A
     * Which means that either after scheduling we have some empty spaces left OR all empty spaces are filled and
     * some other empty spaces are created and filled.
     * <p>
     * The trick here is when we schedule tasks with the max count, e.g. A, in the beginning, the empty idle slots between
     * each other only means the minimum number required, and they can be extended if needed.
     * <p>
     * So final answer is: max ( (n+1)*(f-1) + X, TotalJobs)
     * <p>
     * https://medium.com/@satyem77/task-scheduler-leetcode-39d579f3440
     * <p>
     * Time Complexity: O(N), where N is a number of tasks to execute.
     * This time is needed to iterate over the input array tasks and to
     * compute the array frequencies.
     * Array frequencies contains 26 elements, and hence all operations
     * with it takes constant time.
     * <p>
     * Space Complexity: O(1), to keep the array frequencies
     * of 26 elements.
     */
    int leastInterval(char[] tasks, int n) {
        int[] countByTaskType = new int[26];
        // There are only 26 kinds of task [A-Z], we iterate the tasks and store the count of each
        // task in the array
        for (char task : tasks)
            countByTaskType[task - 'A']++;

        // Find the max count
        int maxCount = 0;
        for (int count : countByTaskType)
            maxCount = Math.max(count, maxCount);

        // Find the number of tasks having maxCount
        int maxCountTaskCount = 0;
        for (int count : countByTaskType) {
            if (count == maxCount)
                maxCountTaskCount++;
        }
        return Math.max((n + 1) * (maxCount - 1) + maxCountTaskCount, tasks.length);
    }

    /**
     * Daily Temperatures
     * Given an array of integers temperatures represents the daily temperatures, return an array answer
     * such that answer[i] is the number of days you have to wait after the ith day to get a warmer
     * temperature. If there is no future day for which this is possible, keep answer[i] == 0 instead.
     * <p>
     * Input: temperatures = [73,74,75,71,69,72,76,73]
     * Output: [1,1,4,2,1,1,0,0]
     * <p>
     * Input: temperatures = [30,40,50,60]
     * Output: [1,1,1,0]
     * <p>
     * https://leetcode.com/problems/daily-temperatures/description/
     */
    @Test
    void testDailyTemperatures() {
        assertThat(dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73})).containsExactly(1, 1, 4, 2, 1, 1, 0, 0);
        assertThat(dailyTemperaturesUseStack(new int[]{73, 74, 75, 71, 69, 72, 76, 73})).containsExactly(1, 1, 4, 2, 1, 1, 0, 0);
    }

    /**
     * Iterate reversely thru the array, and move forward to find the number of days until a warmer day by using
     * the number of days has been generated in the answer array.
     * The idea is when we iterate backward thru the array, the number of day in the answer array can be used for
     * searching for the next warmer day. We also need to track the hottest day in case there is no warmer day.
     * When we search for the next warmer day, we start from the next day temperature first, if it is hotter than 1
     * is the answer, otherwise, we add its number of day to next warmer day in the answer array to the numOfDays var
     * and check if the temperature on that day is warmer. We repeat this process until we find the warmer day in
     * the future and keep adding the number of day to the numOfDays.
     * <p>
     * Time complexity: O(N)
     * The nested while loop makes this algorithm look worse than O(N).
     * The total number of iterations in the while loop does not exceed N, which gives this algorithm
     * a time complexity of O(2⋅N) = O(N).
     * <p>
     * Space complexity: O(1)
     */
    int[] dailyTemperatures(int[] temperatures) {
        int hottest = 0;
        int[] answers = new int[temperatures.length];
        for (int i = temperatures.length - 1; i >= 0; i--) {
            int currentTemp = temperatures[i];
            if (currentTemp >= hottest) {
                // Check if the current day is the hottest one seen so far
                // Last day is always 0 in the answer array cuz there is no other future days
                hottest = currentTemp;
                // We are iterating from the "future"(end of array), so if it is hotter than any future days, its associated
                // answer must be zero, i.e. no warmer day. Therefore, skip and proceed to the next day (answer[i] remains 0)
                continue;
            }
            // There is a warmer day in the future we have seen
            int numOfDays = 1; // init to 1, cuz we start to compare w/ the next day first
            while (temperatures[i + numOfDays] <= currentTemp) {
                // Since the answer array has the info of the next warmer day for a given day, we use it to find the temperature of its
                // next future warmer day until it is indeed warmer than the current temperature. We also accumulate the number of days
                // while we search on this path. This process let us use the previous answer to skip many days to find the answer.
                numOfDays += answers[i + numOfDays]; // This effectively jumps directly to the next warmer day.
            }
            answers[i] = numOfDays;
        }
        return answers;
    }

    /**
     * Use a monotonic decreasing stack to hold temperatures. Monotonic decreasing means that the stack will
     * always be sorted in descending order. Because the problem is asking for the number of days, instead of
     * storing the temperatures themselves, we should store the indices of the days, and use temperatures[i]
     * to find the temperature of the ith day.
     * <p>
     * The idea is the stack holds all days which we haven't seen any warmer day yet while we iterate forward
     * thru the array. Therefore, if the current day's temperature is not warmer than the temperature on the
     * top of the stack or nothing in the stack, we can just push the current day onto the stack - since it
     * is not as warm (equal or smaller), this will maintain the sorted property.
     * On the other hand, if the current day's temperature is warmer than the temperature on top of
     * the stack.
     * It means that the current day is the first day with a warmer temperature than the day associated with
     * the temperature on top of the stack. When we find a warmer temperature, the number of days is the
     * difference between the current index and the index on the top of the stack.
     * <p>
     * When we find a warmer temperature, we can't stop after checking only one element at the top. Cuz as long
     * as there are more day in the stack, the current day can be the next warmer day of other previous day
     * (also remember the stack is in descending order). We should pop from the stack until the top of the stack
     * is no longer colder than the current temperature. Once that is the case, we can push the current day
     * onto the stack.
     * <p>
     * Time complexity: O(N)
     * At first glance, it may look like the time complexity of this algorithm should be O(N^2), because there
     * is a nested while loop inside the for loop. However, each element can only be added to the stack once,
     * which means the stack is limited to N pops. Every iteration of the while loop uses 1 pop, which means
     * the while loop will not iterate more than N times in total, across all iterations of the for loop.
     * <p>
     * An easier way to think about this is that in the worst case, every element will be pushed and popped once.
     * This gives a time complexity of O(2⋅N) = O(N).
     * <p>
     * Space complexity: O(N)
     * If the input was non-increasing, then no element would ever be popped from the stack, and the stack
     * would grow to a size of N elements at the end.
     */
    int[] dailyTemperaturesUseStack(int[] temperatures) {
        int[] answers = new int[temperatures.length];
        Deque<Integer> stack = new ArrayDeque<>(); // Stores the index of temperatures array
        for (int i = 0; i < temperatures.length; i++) {
            int currentTemp = temperatures[i];
            while (!stack.isEmpty() && currentTemp > temperatures[stack.peek()]) {
                // Today is warmer than the most recent previous day(top of the stack) which we haven't seen a warmer day
                // Days in the stack means they haven't seen any warmer day yet
                Integer prevDay = stack.pop();
                // The number of days is the index difference between today and previous day
                answers[prevDay] = i - prevDay;
            }
            stack.push(i);
            // Any days left in the stack after the loop terminates will remain the default 0 val in the answers array
        }
        return answers;
    }

    /**
     * Next Greater Element I
     * The next greater element of some element x in an array is the first greater element that is to the
     * right of x in the same array.
     * <p>
     * You are given two distinct 0-indexed integer arrays nums1 and nums2, where nums1 is a subset of nums2.
     * <p>
     * For each 0 <= i < nums1.length, find the index j such that nums1[i] == nums2[j] and determine the
     * next greater element of nums2[j] in nums2. If there is no next greater element, then the answer for
     * this query is -1.
     * <p>
     * Return an array ans of length nums1.length such that ans[i] is the next greater element as described
     * above.
     * <p>
     * Input: nums1 = [4,1,2], nums2 = [1,3,4,2]
     * Output: [-1,3,-1]
     * Explanation: The next greater element for each value of nums1 is as follows:
     * - 4 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
     * - 1 is underlined in nums2 = [1,3,4,2]. The next greater element is 3.
     * - 2 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
     * <p>
     * Input: nums1 = [2,4], nums2 = [1,2,3,4]
     * Output: [3,-1]
     * Explanation: The next greater element for each value of nums1 is as follows:
     * - 2 is underlined in nums2 = [1,2,3,4]. The next greater element is 3.
     * - 4 is underlined in nums2 = [1,2,3,4]. There is no next greater element, so the answer is -1.
     * <p>
     * https://leetcode.com/problems/next-greater-element-i/description/
     */
    @Test
    void testNextGreaterElement() {
        assertThat(nextGreaterElement(new int[]{4, 1, 2}, new int[]{1, 3, 4, 2})).containsExactly(-1, 3, -1);
        assertThat(nextGreaterElement(new int[]{2, 4}, new int[]{1, 2, 3, 4})).containsExactly(3, -1);
    }

    /**
     * Use decreasing Monotonic Stack to search for next greater number and record in a Map
     * Iterate over the nums2 array from the left to right. Before pushing the current element to the stack,
     * we first check if the stack is empty and the top element in the stack is smaller than the current
     * element, if so, we pop it out and this current element is the next greater element of the pop-out
     * element. We need to keep checking the top element on the stack until the top one is not smaller than
     * the current one. (This process is the standard process for decreasing Monotonic Stack, items on the
     * stack is descending from the bottom)
     * When we pop out from the stack, we put the (number, nextGreaterNumber) to the map and generate the
     * final result in the end
     * <p>
     * Time complexity: O(n). The entire nums2 array (of size n) is scanned only once.
     * Each of the stack's n elements are pushed and popped exactly once. The nums1 array
     * is also scanned only once. All together this requires O(n+n+m) time. Since nums1 must
     * be a subset of nums2, we know m must be less than or equal to n. Therefore, the time
     * complexity can be simplified to O(n).
     * <p>
     * Space complexity: O(n). map will store n key-value pairs while stack will contain
     * at most n elements at any given time.
     */
    int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> numToNextGreaterNum = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int num : nums2) {
            while (!stack.isEmpty() && stack.peek() < num) {
                // Popping out the number from the stack if it is smaller than the current num. In other words, we find
                // the next greater for it now.
                Integer prevNum = stack.pop();
                numToNextGreaterNum.put(prevNum, num);
            }
            stack.push(num);
        }
        while (!stack.isEmpty())
            // Number remaining on the stack means there is no greater number after it, so we assign -1 to them
            numToNextGreaterNum.put(stack.pop(), -1);

        int[] result = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            result[i] = numToNextGreaterNum.get(nums1[i]);
        }
        return result;
    }

    /**
     * Next Greater Element II
     * Given a circular integer array nums (i.e., the next element of nums[nums.length - 1] is nums[0]),
     * return the next greater number for every element in nums.
     * <p>
     * The next greater number of a number x is the first greater number to its traversing-order next
     * in the array, which means you could search circularly to find its next greater number. If it
     * doesn't exist, return -1 for this number. Note: There may be duplicate number in the input.
     * <p>
     * Input: nums = [1,2,1]
     * Output: [2,-1,2]
     * Explanation: The first 1's next greater number is 2;
     * The number 2 can't find next greater number.
     * The second 1's next greater number needs to search circularly, which is also 2.
     * <p>
     * Input: nums = [1,2,3,4,3]
     * Output: [2,3,4,-1,4]
     * <p>
     * https://leetcode.com/problems/next-greater-element-ii/description/
     */
    @Test
    void testNextGreaterElementII() {
        assertThat(nextGreaterElementsII(new int[]{1, 2, 1})).containsExactly(2, -1, 2);
        assertThat(nextGreaterElementsII(new int[]{1, 2, 3, 4, 3})).containsExactly(2, 3, 4, -1, 4);
    }

    /**
     * Iterate array and use decreasing Monotonic Stack algo to search for next greater number first,
     * then do another round w/ same algo but only popping out from the stack.
     * Implementation is similar to the Next Greater Element I question with tow different minor changes
     * <p>
     * 1. We need to push the index to the stack, because the number can be duplicate, and index can help us
     * set the target result to the right position in the result array. However, the rule to popping the
     * element from the stack is still based on its number
     * <p>
     * 2. Due to the circular array, we need to iterate the array again to search for the next greater number
     * which is in front of the number that doesn't have the next greater number at the first round. Those
     * numbers will still on the stack after the first loop, so we just apply the same algo, but this time
     * we don't push any number to the stack.
     * <p>
     * Time complexity : O(n). Only two traversals of the nums array are done. Further, at most 2n
     * elements are pushed and popped from the stack.
     * <p>
     * Space complexity : O(n). A stack of size n is used. resresres array of size n is used.
     */
    int[] nextGreaterElementsII(int[] nums) {
        int[] result = new int[nums.length];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < nums.length; i++) {
            // We store the index of the element in the stack, so we can easily put the greater number in the corresponding location
            // in the result array and also due to the requirement there can be duplicate number. However, the rule to determine
            // to pop from the stack is based on the number. This is the SOP of the decreasing Monotonic Stack implementation
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
                Integer prevNumIdx = stack.pop();
                result[prevNumIdx] = nums[i];
            }
            stack.push(i);
        }
        // This extra loop is meant for the circular array requirement. The first loop can only find the next greater number toward
        // the end of array. For the numbers still left on the stack, which doesn't find the next greater number after the first
        // loop, there may be greater number in front of them in the array. So we need to iterate the array again and apply the same
        // logic to pop from the stack. However, the purpose is to check if any number left to the number(from the array perspective)
        // on the stack can be its next greater number, so we do NOT push any element to the stack, only pop if the top element on
        // the stack is smaller.
        for (int num : nums) {
            while (!stack.isEmpty() && num > nums[stack.peek()])
                result[stack.pop()] = num;
            // NO push to the stack here
        }

        while (!stack.isEmpty())
            // Now the number still left on the stack indeed doesn't have any next greater number
            result[stack.pop()] = -1;

        return result;
    }

    /**
     * Trapping Rain Water
     * Given n non-negative integers representing an elevation map where the width of each bar is 1,
     * compute how much water it can trap after raining.
     * <p>
     * Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
     * Output: 6
     * Explanation: The above elevation map (black section) is represented by array [0,1,0,2,1,0,1,3,2,1,2,1].
     * In this case, 6 units of rain water (blue section) are being trapped.
     * <p>
     * Input: height = [4,2,0,3,2,5]
     * Output: 9
     * <p>
     * https://leetcode.com/problems/trapping-rain-water/description/
     */
    @Test
    void testTrap() {
        assertThat(trapWater(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1})).isEqualTo(6);
        assertThat(trapWater(new int[]{4, 2, 0, 3, 2, 5})).isEqualTo(9);
    }

    /**
     * Use Two Pointers(head/tail) to track the max height on left and right side and compute the water
     * amount as we move either one of pointers.
     * <p>
     * First, the maximum level of water it can trap after the rain, which is equal to the minimum of maximum height of bars
     * on both the sides minus its own height. So the water amount of a given position i is
     * <p>
     * min(left_max, right_max) − height[i]
     * *The left/right max is based on the current position i
     * <p>
     * However, we don't really need to know both left_max and right_max, we just need to know the value of the smaller
     * one. So we can have two pts starting from head and tail, and calculate the maxLeft and maxRight as we go.
     * <p>
     * How to decide to move left or move right?
     * If maxLeft < maxRight, it means the water level is based on the left side (the left bar is smaller)
     * then move left side:
     * - Move left by left += 1
     * - Update the leftMax first
     * - Compute the amount of trap water, which is maxLeft - height[left].
     * <p>
     * Else if maxLeft > maxRight, it means the water level is based on the right side (the right bar is smaller) then move right side:
     * - Move right by right -= 1.
     * - Update the maxRight first
     * - Compute the amount of trap water, which is maxRight - height[right].
     * <p>
     * Time complexity: O(n)
     * Space complexity: O(1)
     */
    int trapWater(int[] height) {
        if (height.length < 2)
            return 0;
        int leftMax = height[0], rightMax = height[height.length - 1];
        int leftPtr = 0, rightPtr = height.length - 1;
        int ans = 0;
        while (leftPtr < rightPtr) {
            // The side with the smaller max height dominates the calculation of trapping water amount, i.e. min(leftMax,rightMax)−height[i]
            // So we just keep moving the ptr on the side with the smaller max value and add up the water amount.
            if (leftMax < rightMax) {
                leftPtr++; // Move the ptr first cuz it starts at the 0 index and there is no trapping water for the first position

                // The order of the following two lines matters! Cuz height[leftPtr] can be greater than leftMax and will cause
                // the following subtraction calculation negative. Here we update the max from these two first, so we don't need
                // to check that condition. In this case(height[left] > maxLeft), it will become ans += 0, which means there is no trap water
                leftMax = Math.max(leftMax, height[leftPtr]);
                ans += leftMax - height[leftPtr]; // Calculate the trap water amount
            } else {
                rightPtr--; // Move the ptr first cuz it starts at the length-1 index and there is no trapping water for the last position
                rightMax = Math.max(rightMax, height[rightPtr]);
                ans += rightMax - height[rightPtr];
            }
        }
        return ans;
    }

    /**
     * Largest Rectangle in Histogram
     * Given an array of integers heights representing the histogram's bar height where the width of
     * each bar is 1, return the area of the largest rectangle in the histogram.
     * <p>
     * Input: heights = [2,1,5,6,2,3]
     * Output: 10
     * Explanation: The above is a histogram where width of each bar is 1.
     * The largest rectangle is shown in the red area, which has an area = 10 units.
     * <p>
     * Input: heights = [2,4]
     * Output: 4
     * <p>
     * https://leetcode.com/problems/largest-rectangle-in-histogram/description/
     */
    @Test
    void testLargestRectangleArea() {
        assertThat(largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3})).isEqualTo(10);
        assertThat(largestRectangleArea(new int[]{2, 4})).isEqualTo(4);
    }

    /**
     * Iterate array and use increasing Monotolic stack to find the previous smaller element and next
     * smaller bar as we go, so we can compute the max rectangle for each bar.
     * <p>
     * Key observation:
     * 1. The height of the maximal rectangle will be equal to the shortest bar included in it.
     * Otherwise, the rectangle wouldn’t be valid.
     * <p>
     * 2. Our goal is for each bar, compute the maximal rectangle which includes that bar and the
     * height of the rectangle is equal to the bar’s height (i.e. it is the shortest bar included).
     * <p>
     * 3. To compute the max rectangle for the aforementioned bar(say with height H), first we
     * compute how many bars on the immediate left are taller than H, let’s say that is L. Similarly,
     * compute how many bars in the immediate right are taller than H, let’s say that is R.
     * Then, the height of the maximal rectangle with height H that includes this bar is equal to
     * H * (R + L + 1).
     * <p>
     * 4. Finding above L and R for a bar X, is equivalent to finding the index of the first element
     * to the left of X that is smaller than X (say lIndex). Similarly, finding R is equivalent to
     * finding the index of the first element to the right of X that is larger than X (say rIndex).
     * <p>
     * Algo:
     * 1. Iterate array and compute the max rectangle which includes that bar as the shortest bar
     * (the rectangle height is equal to bar's height)
     * <p>
     * 2. Maintain a stack such that heights are always in increasing order. But we push each bar's
     * index as the value to the stack, so it is easier to compute the rectangle width.
     * <p>
     * 3. We will use dummyLeftSmallerIdx and dummyRightSmallerIdx as the virtual lIndex and rIndex
     * for the bars next to the boundary on each end and also for the smallest bar in the array or
     * all descending/ascending bars scenario
     * <p>
     * 4. When we iterate from left to right, our goal is to find the lIndex and rIndex for each
     * bar, so we can compute its rectangle area. Before we push the bar to the stack,
     * when we see a bar shorter than the top one on the stack. In this case, it means we
     * find a rIndex(next shorter bar) and cuz this is an increasing(height) stack, the bar right
     * beneath it is the lIndex(previous shorter bar) Therefore, we pop the top bar from the stack
     * and compute the rectangle area.
     * rectangle area = (rIndex - lIndex - 1) * heights[i]
     * (Refer to the comment in the code for the detailed explanation)
     * <p>
     * 5. After we finish the iteration, the stack should still contain some bars which we haven't
     * computed the rectangle yet. They are the last bar in the array and any bar that doesn't have
     * a smaller bar to its right. We need to pop them out and compute their rectangle using the
     * dummyRightSmallerIdx as the rIndex.
     * <p>
     * Time complexity: O(n).
     * We iterate n-sized array, while the total number of times of pushing and popping element
     * from the stack is exact n. So it is still O(n)
     * <p>
     * Space complexity: O(n). Stack is used.
     * <p>
     * https://medium.com/algorithms-digest/largest-rectangle-in-histogram-234004ecd15a
     * https://myleetcodejourney.blogspot.com/2021/01/leet-code-84-largest-rectangle-in.html
     */
    int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();// This is an increasing(bar height) monotolic stack containing the index of heights array
        int maxArea = 0;
        // The two dummy numbers we choose are hand-crafted to suit our algorithm of calculating the rectangle width.
        // width =  dummyRightSmallerIdx - dummyLeftSmallerIdx - 1 = heights.length
        // For the smallest bar in the array, the rectangle spans cross all bars so every bar is included. This is also
        // the reason to push the dummyLeftSmallerIdx to the stack first, so it can be used as the previous smaller bar on the left(lIdx)
        // for the first bar and the smallest bar in the array.
        int dummyLeftSmallerIdx = -1, dummyRightSmallerIdx = heights.length;
        stack.push(dummyLeftSmallerIdx);
        for (int i = 0; i < heights.length; i++) {
            // We use <= so that means, the stack will always be strictly decreasing - because elements are popped when they are equal
            // so equal elements will never stay in the stack
            while (stack.peek() != dummyLeftSmallerIdx && heights[i] <= heights[stack.peek()]) {
                int rectHeight = heights[stack.pop()];
                // idx of the next smaller bar on the right(rIdx)
                // --> current index, i. (We are here cuz we just encounter a smaller bar when iterating to the right)
                // idx of the previous smaller bar on the left(lIdx)
                // --> top element on the stack (Remember this is an increasing monotolic stack, so it is smaller than the one just popped out)
                // Hence, the max rectangle which includes the current bar as the shortest bar can include the bars w/ the
                // index in the range of [lIdx+1, rIdx-1] inclusive, so (rIdx-1)-(lIdx+1)+1 = rIdx-lIdx-1
                int rectWidth = i - stack.peek() - 1;
                maxArea = Math.max(rectHeight * rectWidth, maxArea); // Compute the area and update the maxArea
            }
            stack.push(i);
        }
        // There are two type of bars still remaining on the stack
        // 1. The last bar in the array
        // 2. Any bar that doesn't have a smaller bar to its right
        // We need to pop them out and compute their rectangle as well
        while (stack.peek() != dummyLeftSmallerIdx) {
            int rectHeight = heights[stack.pop()];
            // We use the dummyRightSmallerIdx as the next smaller on the right(rIdx)
            int rectWidth = dummyRightSmallerIdx - stack.peek() - 1;
            maxArea = Math.max(rectHeight * rectWidth, maxArea);
        }
        return maxArea;
    }

    /**
     * Sliding Window Maximum
     * You are given an array of integers nums, there is a sliding window of size k which is moving from
     * the very left of the array to the very right. You can only see the k numbers in the window. Each
     * time the sliding window moves right by one position.
     * <p>
     * Return the max sliding window.
     * <p>
     * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
     * Output: [3,3,5,5,6,7]
     * Explanation:
     * Window position                Max
     * ---------------               -----
     * [1  3  -1] -3  5  3  6  7       3
     * 1 [3  -1  -3] 5  3  6  7       3
     * 1  3 [-1  -3  5] 3  6  7       5
     * 1  3  -1 [-3  5  3] 6  7       5
     * 1  3  -1  -3 [5  3  6] 7       6
     * 1  3  -1  -3  5 [3  6  7]      7
     * <p>
     * https://leetcode.com/problems/sliding-window-maximum/description/
     */
    @Test
    void testMaxSlidingWindow() {
        assertThat(maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)).containsExactly(3, 3, 5, 5, 6, 7);
        assertThat(maxSlidingWindow(new int[]{1, -1}, 1)).containsExactly(1, -1);
        assertThat(maxSlidingWindow(new int[]{7, 2, 4}, 2)).containsExactly(7, 4);
        assertThat(maxSlidingWindow(new int[]{1, 3, 1, 2, 0, 5}, 3)).containsExactly(3, 3, 2, 5);
    }

    /**
     * Maintaining a Monotonic Decreasing Queue(Deque) to keep the first element the index of the max element of current window
     * by removing the smaller elements from the end before adding new element to the deque.
     * <p>
     * Observation:
     * 1. In a window, the elements that come before the largest element will never be selected as the largest element of
     * any future windows. So they are useless.
     * <p>
     * 2. However, we cannot ignore the items that follow the largest element. Cuz they can become the new max when the
     * window moves and the current max item may be dropped when it falls out of the range of new window.
     * <p>
     * 3. In general, whenever we encounter a new element x, we want to discard all elements that are less than x
     * before adding x. Let's say we currently have [63, 15, 8, 3] and we encounter 12. Any future window with 8 or 3
     * will also contain 12, so we can discard them. After discarding them and adding 12, we have [63, 15, 12].
     * Therefore, we keep elements in descending order.
     * <p>
     * In this case, we want a monotonic decreasing queue, which means that the elements in the queue are always
     * sorted descending. When we want to add a new element x, we maintain the monotonic property by removing all
     * elements less than x before adding x.
     * <p>
     * By maintaining the monotonic decreasing property, the largest element in the window must always be the first
     * element in the deque, which is nums[dq[0]].
     * <p>
     * We use deque of integers to store the index of the element because
     * 1. Deque supports constant time to remove and add on both ends
     * 2. Need to detect when elements leave the window(index falls out of range of window) when window is moved.
     * <p>
     * Algo:
     * Iterate the array by moving the right ptr by one
     * 1. First check if the head of dq is outside the window. If so, remove it.
     * 2. If the head is smaller than the current element, continue to remove the LAST element.
     * (This is where we maintain monotonic decreasing property)
     * 3. Add the current element index to the END of dq.
     * 4. Add the head, i.e. the max element, to the result list when we form a K-sized window
     * <p>
     * Time complexity: O(n).
     * In the worst case, every element will be pushed and popped once. This gives a time complexity of O(2⋅n)=O(n).
     * <p>
     * Space complexity: O(k).
     * The max size of the deque is k.
     */
    int[] maxSlidingWindow(int[] nums, int k) {
        List<Integer> ans = new ArrayList<>(nums.length - k + 1); // total n-k+1 windows
        Deque<Integer> dq = new ArrayDeque<>(); // contains index, so it will be easier to compare w/ window position
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            // When the window is moved, the head of the dq, i.e. the max in the previous window, may fall out of range of
            // the new window. We need to remove it first cuz we can't consider it anymore.
            if (!dq.isEmpty() && left > dq.peekFirst())
                dq.removeFirst();

            // Need to maintain the monotonic decreasing, the head of the dq must be the max in the current window
            // So we need to remove the smaller elements from the end before adding the new element to dq
            while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[right])
                dq.removeLast();

            dq.addLast(right);

            if (right - left + 1 == k) {
                // Output the max once the window reaches size k. Moving the left ptr ONLY after a window is formed
                ans.add(nums[dq.peekFirst()]);
                ++left;
            }
        }
        int[] r = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++)
            r[i] = ans.get(i);
        return r;
        //return ans.stream().mapToInt(i->i).toArray();
    }

    /**
     * Container With Most Water
     * You are given an integer array height of length n. There are n vertical lines drawn such that the
     * two endpoints of the ith line are (i, 0) and (i, height[i]).
     * <p>
     * Find two lines that together with the x-axis form a container, such that the container contains
     * the most water.
     * <p>
     * Return the maximum amount of water a container can store.
     * Notice that you may not slant the container.
     * <p>
     * Input: height = [1,8,6,2,5,4,8,3,7]
     * Output: 49
     * Explanation: The above vertical lines are represented by array [1,8,6,2,5,4,8,3,7]. In this case,
     * the max area of water (blue section) the container can contain is 49.
     * <p>
     * Input: height = [1,1]
     * Output: 1
     * <p>
     * https://leetcode.com/problems/container-with-most-water/description/
     */
    @Test
    void testMaxArea() {
        assertThat(maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7})).isEqualTo(49);
    }

    /**
     * Use two pointers at the beginning and end and keep track of the max area while moving the ptr on the shorter line.
     * <p>
     * Observation:
     * 1. The problem can be rephrased as finding the max area that can be formed between the vertical lines using the
     * shorter line as length and the distance between the lines as the width of the rectangle forming the area.
     * 2. The area will always be limited by the height of the shorter line. The further the two lines apart(wider),
     * the more area.
     * <p>
     * We take two pointers, one at the beginning and one at the end of the array. As long as two ptrs are not cross
     * each other, we compute the area between two lines and keep track of the max value so far. Then move the ptr
     * pointing to the shorter line towards the other end by one step. The idea when moving the ptr, we reduce the width
     * so we hope the next line will be higher than the current two lines to compensate or even make a larger area.
     * Thus, we move the ptr on the shorter line.
     * <p>
     * Time complexity: O(n)
     * <p>
     * Space complexity: O(1)
     */
    int maxArea(int[] height) {
        int maxArea = Integer.MIN_VALUE;
        int left = 0, right = height.length - 1;
        while (left < right) {
            int area = (right - left) * Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, area);
            if (height[left] <= height[right])
                left++;
            else
                right--;
        }
        return maxArea;
    }

    /**
     * Product of Array Except Self
     * <p>
     * Given an integer array nums, return an array answer such that answer[i] is equal to the
     * product of all the elements of nums except nums[i].
     * <p>
     * The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
     * <p>
     * You must write an algorithm that runs in O(n) time and without using the division operation.
     * <p>
     * Input: nums = [1,2,3,4]
     * Output: [24,12,8,6]
     * Example 2:
     * <p>
     * Input: nums = [-1,1,0,-3,3]
     * Output: [0,0,9,0,0]
     * <p>
     * https://leetcode.com/problems/product-of-array-except-self/description/
     */
    @Test
    void testProductExceptSelf() {
        assertThat(productExceptSelf(new int[]{1, 2, 3, 4})).containsExactly(24, 12, 8, 6);
    }

    /**
     * Iterate the nums array forward to compute the left side product, then iterate backward to compute the right product
     * and multiply it to the previous left side product
     * <p>
     * Observation:
     * 1. If we can compute the product of all the numbers to the left and all the numbers to the right of the index i.
     * Then multiplying these two individual products would give us the value of the answer[i].
     * <p>
     * 2. If we compute the left and right side part products while iterating the nums array, it will take O(N^2) and obviously
     * it iterate the same elements repeatedly when computing the product.
     * <p>
     * 3. We can precompute the left and right side product and store them at two arrays, then just iterate and multiply them
     * to get the answer. This takes extra space for two arrays
     * <p>
     * Algo:
     * Instead of using two additional arrays to precompute the left and right side product, we can just use the final ans array
     * to store the intermediate result and update it while iterating the nums array forward and backward.
     * At the first loop, we compute the left side product for each element and store at ans array.
     * Then at the second loop, we go from the end of array and compute the right side product and multiply it to the value
     * at the ans array
     * <p>
     * Time complexity : O(N) where N represents the number of elements in the input array.
     * <p>
     * Space complexity : O(1)
     */
    int[] productExceptSelf(int[] nums) {
        int[] ans = new int[nums.length];
        // 1st loop to compute the product of all elements to the left, store the result in the ans array
        ans[0] = 1; // There is no left element for the nums[0], so set it to 1
        for (int i = 1; i < nums.length; i++) {
            // ans[i - 1] already contains the product of elements to the left of 'i - 1'
            // Simply multiplying it with nums[i - 1] would give the product of all
            // elements to the left of index 'i'
            ans[i] = ans[i - 1] * nums[i - 1];
        }
        // Now ans array has the left side product of every element, we need to multiply it w/ right side product

        // rightProd contains the product of all the elements to the right
        // Note: for the element at index 'length - 1', there are no elements to the right,
        // so the rightProd would be 1
        int rightProd = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            // ans[nums.length-1], i.e. the last element, has no right elements, so no need to update the product,
            // so we start at nums.length - 2
            // Continue multiply rightProd w/ the last nums, so it carries the product of all elements to the right of index 'i'
            rightProd *= nums[i + 1];
            // Multiply the rightProd to the ans[i] so it gets the right side of the product
            ans[i] = ans[i] * rightProd;
        }
        return ans;
    }

    /**
     * Next Permutation
     * A permutation of an array of integers is an arrangement of its members into a sequence or linear order.
     * <p>
     * For example, for arr = [1,2,3], the following are all the permutations of arr: [1,2,3], [1,3,2],
     * [2, 1, 3], [2, 3, 1], [3,1,2], [3,2,1].
     * The next permutation of an array of integers is the next lexicographically greater permutation of
     * its integer. More formally, if all the permutations of the array are sorted in one container according
     * to their lexicographical order, then the next permutation of that array is the permutation that follows
     * it in the sorted container. If such arrangement is not possible, the array must be rearranged as the
     * lowest possible order (i.e., sorted in ascending order).
     * <p>
     * For example, the next permutation of arr = [1,2,3] is [1,3,2].
     * Similarly, the next permutation of arr = [2,3,1] is [3,1,2].
     * While the next permutation of arr = [3,2,1] is [1,2,3] because [3,2,1] does not have a lexicographical
     * larger rearrangement.
     * Given an array of integers nums, find the next permutation of nums.
     * <p>
     * The replacement must be in place and use only constant extra memory.
     * <p>
     * Input: nums = [1,2,3]
     * Output: [1,3,2]
     * Example 2:
     * <p>
     * Input: nums = [3,2,1]
     * Output: [1,2,3]
     * Example 3:
     * <p>
     * Input: nums = [1,1,5]
     * Output: [1,5,1]
     * <p>
     * https://leetcode.com/problems/next-permutation/
     */
    @Test
    void testNextPermutation() {
        int[] input = {1, 2, 3};
        nextPermutation(input);
        assertThat(input).containsExactly(1, 3, 2);
        input = new int[]{1, 1, 5};
        nextPermutation(input);
        assertThat(input).containsExactly(1, 5, 1);
        input = new int[]{6, 2, 1, 5, 4, 3, 0};
        nextPermutation(input);
        assertThat(input).containsExactly(6, 2, 3, 0, 1, 4, 5);
    }

    /**
     * Observation:
     * 1. The question asks for the next lexicographically greater permutation. So we need to first find the pattern
     * of the permutation sequence. Given the array [0,1,2], the first/smallest permutation is [0, 1, 2] and the last/max
     * permutation is [2, 1, 0].
     * We can see a pattern that if the list is in descending order, it is the greatest/last possible permutation. Similarly,
     * a list in ascending order is the first/least possible permutation, because the greatest place(left-most) is represented
     * by the smallest number, and so on.
     * <p>
     * 2. The above pattern also can be found in the specific section in the permutation, so it can help us to determine how
     * the next permutation may look like. The section in a strict descending order implies that it is the last permutation
     * after the last number before it. And this number(pivot) is our starting point to figure out the next permutation.
     * Ex: Input: [6, 2, 1, 5, 4, 3, 0]
     * [5, 4, 3, 0] is the max/last permutation after 1(pivot).
     * <p>
     * 3. Once we find the "pivot", we need to figure out what the next greater permutation is. We already know that the section
     * following the pivot point is the last permutation, so the next permutation must have a new and greater number at the pivot
     * position. Cuz the permutation sequence is lexicographical, we look for the candidate in the right of pivot, which means
     * the descending section, and it must be the smallest one greater than the pivot.
     * <p>
     * 4. After we find this candidate, we swap the pivot with it. So now we have a new pivot to start a new permutation from
     * it. But the section after it should also begin as the smallest/first permutation, therefore, we need to turn this
     * section into ascending order.
     * <p>
     * Algo:
     * 1. Iterate from the end of array, we check each pair(nums[i], nums[i+1])of elements until nums[i] < nums[i+1], and
     * the i is the pivot.
     * <p>
     * 1-1. If we found the pivot, we start to iterate the section after the pivot and search for the next greater element.
     * Then we swap them.
     * <p>
     * 2. Regardless the pivot is found, we reverse the order of the section after the pivot, so it becomes ascending order.
     * In the case of no pivot found, it means the original permutation is the last one and reversing the whole array means it
     * becomes the first/smallest permutation.
     * <p>
     * Time complexity : O(n)
     * <p>
     * Space complexity : O(1)*
     */
    void nextPermutation(int[] nums) {
        int pivot = nums.length - 2;
        while (pivot >= 0 && nums[pivot] >= nums[pivot + 1])
            // Search for the first number before the descending section
            pivot--;
        // pivot should be -1 if there is no pivot found

        if (pivot >= 0) {
            // we found the pivot from the last step
            // Find next greater number after pivot
            int i = nums.length - 1;
            while (nums[i] <= nums[pivot])
                i--;
            swap(nums, pivot, i);
        }
        // Reverse the descending section to ascending
        int left = pivot + 1, right = nums.length - 1;
        while (left < right) {
            swap(nums, left, right);
            ++left;
            --right;
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * First Missing Positive
     * Given an unsorted integer array nums, return the smallest missing positive integer.
     * You must implement an algorithm that runs in O(n) time and uses O(1) auxiliary space.
     * <p>
     * Input: nums = [1,2,0]
     * Output: 3
     * Explanation: The numbers in the range [1,2] are all in the array.
     * <p>
     * Input: nums = [3,4,-1,1]
     * Output: 2
     * Explanation: 1 is in the array but 2 is missing.
     * <p>
     * Input: nums = [7,8,9,11,12]
     * Output: 1
     * Explanation: The smallest positive integer 1 is missing.
     * <p>
     * https://leetcode.com/problems/first-missing-positive/description/
     */
    @Test
    void testFirstMissingPositive() {
        assertThat(firstMissingPositive(new int[]{1, 2, 0})).isEqualTo(3);
        assertThat(firstMissingPositive(new int[]{3, 4, -1, 1})).isEqualTo(2);
        assertThat(firstMissingPositive(new int[]{7, 8, 9, 11, 12})).isEqualTo(1);
    }

    /**
     * Use the "Index as a hash key" approach to use the existing array to store the result of existence of [1..n] by changing
     * the sign of original number. The answer is the smallest index of the positive number.
     * <p>
     * Observation:
     * 1. To find the first missing positive number manually, we start to look from 1, 2, 3..k and check if it exists in
     * the input array and the first one not found is the answer. However, given a n-sized array, we can sure the answer
     * must be in the set of [1,2 ... n+1]. If the array is a continuous sequence like [1,2..n], the answer will be the
     * n+1. Otherwise, the answer must fall within the [1..n] range.
     * <p>
     * 2. If there is no O(1) space constraint, the easiest solution is to put the array into a HashSet, then try each num
     * in [1,2...n+1] to check existence in the HashSet and the first missing one is the answer.
     * <p>
     * 3. We can't use the extra memory, so we need to leverage the input array itself. Since the array has index[0..n-1],
     * we can use the existing slot in array to map the result of the number existence from the sequence [1,2...n+1].
     * In other words, we use the index of the array as key, which represents the number from the sequence. And we embed the
     * info of existence of the corresponding number in the slot by modifying the existing element. This approach is called
     * "Index as a hash key"
     * <p>
     * 4. Cuz we want to map the number in array to the index, we must first clean up/normalize the array otherwise the
     * idx out of bound issue may occur. We can convert the original number to the range of [1-n] so it can be mapped to the
     * index [0 - n-1].
     * <p>
     * Algo:
     * 1. We want to use '1' to replace those numbers in the array thant fall outside the [1-n]. So we must first check if
     * 1 is already present in the array before altering the array. If not, we're done and 1 is the answer.(Remember the answer
     * set starts from 1)
     * <p>
     * 2. Replace negative numbers, zeros, and numbers larger than n by 1s
     * 3. Iterate the array. Change the sign of v-th element if you meet number v. If v is equal to n, we use the first element
     * (index:0). We also need to take the duplicate number into account, so we don't flip the sign of number multiple times.
     * <p>
     * For example, negative sign of nums[2] element means that number 2 is present in nums. The positive sign of nums[3]
     * element means that number 3 is not present (missing) in nums.
     * <p>
     * 4. Iterate the array from index 1. Return the index of the first positive element, which means we didn't see that number
     * at last iteration.
     * <p>
     * 5. Check the first element, i.e. existence of number n. If > 0, this is the answer.
     * <p>
     * 6. If on the previous step we didn't find the positive element in nums, that means that the answer is n + 1.
     * <p>
     * Time complexity: O(N)
     * <p>
     * Space complexity: O(1)
     */
    int firstMissingPositive(int[] nums) {
        int n = nums.length;
        // Check if 1 is present in the array. If not, 1 is the answer
        boolean foundOne = false;
        for (int num : nums) {
            if (num == 1) {
                foundOne = true;
                break;
            }
        }
        if (!foundOne)
            return 1;

        // Replace negative numbers, zeros, and numbers larger than n by 1s.
        // After this conversion nums will contain only positive numbers.
        for (int i = 0; i < n; i++) {
            if (nums[i] <= 0 || nums[i] > n)
                nums[i] = 1;
        }

        // Use index as a hash key and number sign as a presence detector. Ex, if nums[1] is negative that means
        // that number `1` is present in the array. If nums[2] is positive - number 2 is missing.
        for (int i = 0; i < n; i++) {
            int val = Math.abs(nums[i]);
            // If you meet number val in the array - change the sign of val-th element.
            if (val == n)
                // Use index 0 to store the info of the number n to avoid out of bound
                nums[0] = -Math.abs(nums[0]);
            else
                nums[val] = -Math.abs(nums[val]);
        }
        // Return the index of the first positive element, which is the first missing positive
        for (int i = 1; i < n; i++) {
            if (nums[i] > 0)
                return i;
        }

        // If we don't find any from the above, check the first element
        if (nums[0] > 0)
            return n;

        // Otherwise, the next positive is n+1. This is the case when the nums contains exactly [1,2,3...n]
        return n + 1;
    }

    /**
     * Subarray Sum Equals K
     * Given an array of integers nums and an integer k, return the total number of subarrays
     * whose sum equals to k.
     * <p>
     * A subarray is a contiguous non-empty sequence of elements within an array.
     * <p>
     * Input: nums = [1,1,1], k = 2
     * Output: 2
     * <p>
     * Input: nums = [1,2,3], k = 3
     * Output: 2
     * <p>
     * https://leetcode.com/problems/subarray-sum-equals-k/description/
     */
    @Test
    void testSubarraySum() {
        assertThat(subarraySum(new int[]{1, 1, 1}, 2)).isEqualTo(2);
        assertThat(subarraySum(new int[]{1, 2, 3}, 3)).isEqualTo(2);
    }

    /**
     * Iterate the array and use a Map to store the current prefix sum and count so far. We check if the current prefix
     * sum - k can be found in the Map, if so, add its count to the result.
     * <p>
     * Observation
     * 1. If we use the brute force approach, we can iterate each number, and for each number, we start from next number
     * as the sub-array and check if the sum is equal to k, and extending the sub-array to find all of them until the end.
     * We can see for some prefix sums, they are repeatedly computed.
     * <p>
     * 2. We want to reuse the prefix sum if possible. Say if we are at index i, there are two cases that we can have
     * sub-array(s) w/ sum equal to k at this position.
     * (1) The sum from index 0 to i is k
     * (2) The difference between the sum of a previous index j, (j < i) and the sum of index i is k. In other words,
     * sum[i] − sum[j] = k
     * <p>
     * To generalize this. If we can find a sum[j] such that sum[i] − k = sum[j], then we find a eligible sub-array.
     * For the first case, we can always assume a sum[j] equal to 0 always exists.
     * <p>
     * Algo:
     * 1. We use a HashMap to store the prefixSum to its count/number of occurrences. We first insert the (prefixSum: 0, count: 1)
     * into the map to cover the first case.
     * <p>
     * 2. Iterate the array and for each number,
     * - Accumulate the current sum
     * - Check if the currentPrefixSum - k exists in the map(This is to find the sum[j] above)
     * - If so, we add the count to the result. (One count here means one sub-array)
     * - Update/Insert the count of the current sum into the map.
     * <p>
     * Time complexity : O(n)
     * Space complexity : O(n)
     */
    int subarraySum(int[] nums, int k) {
        int result = 0;
        int currentPrefixSum = 0;
        // The value in the map, i.e. count, basically is the number of sub-arrays with the given sum
        Map<Integer, Integer> prefixSumToCount = new HashMap<>();
        // We need this dummy prefix sum: 0 for the use case of currentPrefixSum is equal to k
        prefixSumToCount.put(0, 1);
        for (int num : nums) {
            currentPrefixSum += num;
            int complementPrefixSum = currentPrefixSum - k;
            // Check if we have a prefix sum that makes currentPrefixSum + complementPrefixSum = k
            if (prefixSumToCount.containsKey(complementPrefixSum))
                result += prefixSumToCount.get(complementPrefixSum);
            // Update the count of the currentPrefixSum in the map
            prefixSumToCount.put(currentPrefixSum, prefixSumToCount.getOrDefault(currentPrefixSum, 0) + 1);
        }
        return result;
    }

    /**
     * Longest Consecutive Sequence
     * Given an unsorted array of integers nums, return the length of the longest consecutive
     * elements sequence.
     * <p>
     * You must write an algorithm that runs in O(n) time.
     * <p>
     * Input: nums = [100,4,200,1,3,2]
     * Output: 4
     * Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its
     * length is 4.
     * <p>
     * Input: nums = [0,3,7,2,5,8,4,6,0,1]
     * Output: 9
     * <p>
     * https://leetcode.com/problems/longest-consecutive-sequence/description/
     */
    @Test
    void testLongestConsecutive() {
        assertThat(longestConsecutive(new int[]{100, 4, 200, 1, 3, 2})).isEqualTo(4);
        assertThat(longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1})).isEqualTo(9);
    }

    /**
     * Put all numbers into the HashSet. Then for each number that doesn't have a preceding number, use it as the
     * starting element of a sequence and iteratively check if the next number exists in the HashSet to extend
     * the sequence.
     * <p>
     * Observation:
     * 1. To start a sequence, the first number must not have any preceding number in the array, otherwise it can
     * only be in the middle/end of a sequence or just an one-element sequence itself.
     * <p>
     * Algo:
     * 1. Put all numbers in a HashSet
     * 2. For each number in the array which doesn't have preceding number(num - 1) in the HashSet, start a while
     * loop to check the next number(num + 1) in the HashSet and increment the sequence length until next number
     * is unavailable and then update the max sequence length so far.
     * <p>
     * Time complexity : O(n)
     * Space complexity : O(n)
     */
    int longestConsecutive(int[] nums) {
        int maxLength = 0;
        Set<Integer> numbers = new HashSet<>();
        for (int num : nums)
            numbers.add(num);

        for (int num : nums) {
            if (!numbers.contains(num - 1)) {
                // There is no number preceding num in the set, so We can start a sequence from num
                int nextNum = num + 1;
                int sequenceLength = 1;
                // Keep checking if the next number exists in the set so the sequence can be extended
                while (numbers.contains(nextNum)) {
                    nextNum++;
                    sequenceLength++;
                }
                maxLength = Math.max(maxLength, sequenceLength);
            }
        }
        return maxLength;
    }
}