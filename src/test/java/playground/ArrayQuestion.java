package playground;

import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/*  TODO: Useful tips:
     - List ---> PRIMITIVE type array, e.g. List<Integer> --> int[]
        1. Iterate the List and add them to the array
             int[] array = new int[list.size()];
             for(int i = 0; i < list.size(); i++) array[i] = list.get(i);
        2. int[] array = myIntList.stream().mapToInt(i -> i).toArray() ==> more memory, but considering doing this to
           convert List to array cuz there will less code to type at the interview
     - List of List ---> PRIMITIVE type 2D array, e.g. List<List<Integer>> --> int[][]
         int[][] grid = list.stream().map(u -> u.stream().mapToInt(i -> i).toArray()).toArray(int[][]::new);
     - List of List ---> REFERENCE type 2D array, e.g. List<List<String>> --> String[][]
         String[][] grid = list.stream().map(u -> u.toArray(String[]::new)).toArray(String[][]::new);
     - List ---> REFERENCE type array, e.g. List<Foo> --> Foo[]
         Foo[] array = list.toArray(new Foo[0]); ==> new Foo[0] just means an empty array. More efficient in newer JVM
     - REFERENCE type array ---> List, e.g. String[] --> List<String>
       List<T> myList = Arrays.asList(T... a)
       Ex:
         String[] array = {"foo", "bar"};
         List<String> list = Arrays.asList(array);
         or list = Arrays.asList("foo", "bar");
     - PRIMITIVE type array ---> List, e.g. int[] --> List<Integer>
        1. Iterate the array and add them to the List
	        List<Integer> output = new ArrayList<>();
            for (int value : intArray) output.add(value);
        2. List<Integer> output = IntStream.of(intArray).boxed().collect(Collectors.toList());
     - Set ---> Array
        mySet.stream().mapToInt(i -> i).toArray()
     - ArrayList vs LinkedList in Java --- IMPORTANT!
         https://stackoverflow.com/questions/322715/when-to-use-linkedlist-over-arraylist-in-java
     - Map tips
       1. For Map<K, List<V>>, use computeIfAbsent(myKey, k -> new ArrayList<>()).add(myValue) to simplify the logic to check if
          the map contains myKey and init the ArrayList conditionally and add the myValue to the list. (Also apply to MultiMap use case)
          https://stackoverflow.com/questions/48183999/what-is-the-difference-between-putifabsent-and-computeifabsent-in-java-8-map
     - Creating subarray
       T[] copyOfRange(T[] original, int from, int to)
       Ex: int[] remainingArray = Arrays.copyOfRange(myArray, 2, myArray.length) --> Copy all element from idx 2 to the end
     - To iterate the array in circular way(iterate to the head after the last element)
       Let index = (index + 1) % array.length
     - Reverse the array in place (Also applies to reverse a string)
       void reverse(int[] array, int start, int end) {
           while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
      - MUST know array util functions
        -- Copies the specified range of the specified array into a new array
            Arrays.copyOfRange(int[] original, int from, int to)
}

 */
public class ArrayQuestion {
    void reverseInPlace(int[] array) {
        int left = 0, right = array.length - 1;
        while (left < right) {
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }

    /**
     * Remove Element
     * Given an integer array nums and an integer val, remove all occurrences of val in nums
     * in-place. The order of the elements may be changed. Then return the number of elements
     * in nums which are not equal to val.
     * <p>
     * Consider the number of elements in nums which are not equal to val be k, to get
     * accepted, you need to do the following things:
     * <p>
     * Change the array nums such that the first k elements of nums contain the elements
     * which are not equal to val. The remaining elements of nums are not important as well
     * as the size of nums.
     * <p>
     * Return k.
     * <p>
     * Input: nums = [3,2,2,3], val = 3
     * Output: 2, nums = [2,2,_,_]
     * Explanation: Your function should return k = 2, with the first two elements of nums
     * being 2. It does not matter what you leave beyond the returned k (hence they are
     * underscores).
     * <p>
     * Input: nums = [0,1,2,2,3,0,4,2], val = 2
     * Output: 5, nums = [0,1,4,0,3,_,_,_]
     * Explanation: Your function should return k = 5, with the first five elements of nums
     * containing 0, 0, 1, 3, and 4.
     * Note that the five elements can be returned in any order.
     * It does not matter what you leave beyond the returned k (hence they are underscores).
     * <p>
     * https://leetcode.com/problems/remove-element/description/
     */
    @Test
    void removeRemoveElement() {
        int[] intArray = new int[]{3, 2, 2, 3};
        assertThat(removeElement(intArray, 3)).isEqualTo(2);
        assertThat(removeElement(new int[]{0, 1, 2, 2, 3, 0, 4, 2}, 2)).isEqualTo(5);
    }

    /**
     * Maintain two ptr(i:0, j:0) and iterate array w/ ptr i, when its number is NOT
     * target number, we swap the number of ptr i with j, then advance j. Return j in
     * the end.
     * <p>
     * The idea is we want to push all non-target number to the front. Ptr j serves as the
     * position that we will write the next non-target number to.
     * Ptr i should run faster than j unless there are continuous non-target number in front
     * of array. In that case, i and j just keep swapping the same number at the same
     * position and advance together.
     * <p>
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    int removeElement(int[] nums, int val) {
        int insertIdx = 0; // track the position where we will put the next non-target number
        for (int i = 0; i < nums.length; i++) {
            // Do nothing when the number is equal to target
            if (nums[i] != val) {
                nums[insertIdx] = nums[i];
                insertIdx++;
            }
        }
        return insertIdx; // ptr insertIdx is at the next index of the last placed non-target number
    }

    /**
     * Remove Duplicates from Sorted Array
     * Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place
     * such that each unique element appears only once. The relative order of the elements should
     * be kept the same. Then return the number of unique elements in nums.
     * <p>
     * Consider the number of unique elements of nums to be k, to get accepted, you need to do
     * the following things:
     * <p>
     * - Change the array nums such that the first k elements of nums contain the unique elements
     * in the order they were present in nums initially. The remaining elements of nums are not
     * important as well as the size of nums.
     * <p>
     * - Return k.
     * <p>
     * Input: nums = [1,1,2]
     * Output: 2, nums = [1,2,_]
     * Explanation: Your function should return k = 2, with the first two elements of nums being
     * 1 and 2 respectively.
     * It does not matter what you leave beyond the returned k (hence they are underscores).
     * <p>
     * Input: nums = [0,0,1,1,1,2,2,3,3,4]
     * Output: 5, nums = [0,1,2,3,4,_,_,_,_,_]
     * Explanation: Your function should return k = 5, with the first five elements of nums being
     * 0, 1, 2, 3, and 4 respectively.
     * It does not matter what you leave beyond the returned k (hence they are underscores).
     * https://leetcode.com/problems/remove-duplicates-from-sorted-array/description/
     */
    @Test
    void removeDuplicateFromArray() {
        int[] intArray = new int[]{1, 1, 2, 3, 3, 3, 4, 5, 5, 6};
        assertThat(removeDuplicates(intArray)).isEqualTo(6);
        assertThat(intArray).containsExactly(1, 2, 3, 4, 5, 6, 4, 5, 5, 6);
    }

    /**
     * Maintain two ptr(i:0, insertIdx:0) to iterate the array w ptr i. when its number
     * is not target number, set the number at insertedIdx to current number, then advance
     * the insertedIdx. Return the insertedIdx in the end.
     * <p>
     * The idea is we want to push all non-target number to the front of array.
     * insertIdx is the ptr to track where we will put the next non-target number.
     * Ptr i should run faster than insertIdx unless there are continuous non-target number
     * in front of array. In that case, ptr i and insertIdx will at the same index and
     * we just set the number at its original index then advance both ptr together.
     * <p>
     * Time Complexity: O(N).
     * Space Complexity: O(1)
     */
    int removeDuplicates(int[] nums) {
        if (nums.length == 0 || nums.length == 1)
            return nums.length;
        int insertIdx = 1; // Idx where we will place the next unique number. Starts at 1
        for (int i = 1; i < nums.length; i++) {
            // i starts at the 2nd element
            if (nums[i - 1] != nums[i]) {
                // nums[i] is different from the previous number. Cuz the array is sorted, it means
                // this is first time we see it. So we want to push this number in the front of array,
                // insertedIdx.
                nums[insertIdx] = nums[i];
                insertIdx++;
            }
        }
        return insertIdx; // insertIdx is at the next index of the last unique element
    }

    /**
     * Best Time to Buy and Sell Stock I
     * Given an array prices where prices[i] is the price of a given stock on the ith day.
     * You want to maximize your profit by choosing a SINGLE day to buy one stock and choosing
     * a different day in the future to sell that stock.
     * <p>
     * Return the maximum profit you can achieve from this transaction. If you cannot achieve any
     * profit, return 0.
     * <p>
     * Input: prices = [7,1,5,3,6,4]
     * Output: 5
     * Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
     * Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
     * <p>
     * Input: prices = [7,6,4,3,1]
     * Output: 0
     * Explanation: In this case, no transactions are done and the max profit = 0.
     * https://leetcode.com/problems/best-time-to-buy-and-sell-stock/description/
     */
    @Test
    void maxProfitToBuySellStockOne() {
        int[] intArray = new int[]{7, 1, 5, 3, 6, 4};
        // Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
        // Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
        assertThat(maxProfitOneBuySell(intArray)).isEqualTo(5);
        intArray = new int[]{7, 6, 4, 3, 1};
        // no transactions are done and the max profit = 0
        assertThat(maxProfitOneBuySell(intArray)).isEqualTo(0);

    }

    /**
     * Iterate the array an keep track of the minPrice(init: 0) and maxProfit(init: INT_MAX). Update
     * minPrice if current price is smaller, and update the maxProfit if currentPrice - minPrice is bigger
     * <p>
     * Observation:
     * We need to find the largest price following each valley, which difference could be the max profit.
     * We can maintain two variables - minPrice and maxProfit corresponding to the smallest valley and maximum
     * profit (maximum difference between selling price and minPrice) obtained so far respectively.
     * We iterate the list in one pass
     * For each iteration we do either one of the two things
     * 1. Found a smaller minPrice: this new minPrice MAY produce the larger maxProfit later on(we do NOT do
     * anything w/ maxProfit here)
     * 2. Update maxProfit so far if the difference between current value and minPrice is bigger
     * Time Complexity: O(N).
     * Space Complexity: O(1)
     */
    int maxProfitOneBuySell(int[] prices) {
        int maxProfit = 0; // Keep track of the largest difference so far
        int minPrice = Integer.MAX_VALUE; // Keep track of the best buy day so far
        for (int price : prices) {
            minPrice = Math.min(price, minPrice);
            maxProfit = Math.max(maxProfit, price - minPrice);
        }
        return maxProfit;
    }

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
     * You are given an integer array prices where prices[i] is the price of a given stock on
     * the ith day.
     * On each day, you may decide to buy and/or sell the stock. You can only hold at most one
     * share of the stock at any time. However, you can buy it then immediately sell it on the same day.
     * Find and return the maximum profit you can achieve.
     * <p>
     * Input: prices = [7,1,5,3,6,4]
     * Output: 7
     * Explanation: Buy on day 2 (price = 1) and sell on day 3 (price = 5), profit = 5-1 = 4.
     * Then buy on day 4 (price = 3) and sell on day 5 (price = 6), profit = 6-3 = 3.
     * Total profit is 4 + 3 = 7.
     * <p>
     * Input: prices = [1,2,3,4,5]
     * Output: 4
     * Explanation: Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
     * Total profit is 4.
     * <p>
     * Input: prices = [7,6,4,3,1]
     * Output: 0
     * Explanation: There is no way to make a positive profit, so we never buy the stock to achieve
     * the maximum profit of 0.
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

    /**
     * Iterate the price array from 2nd day, if it is bigger than the previous day, add the price difference
     * to the maxProfit.
     * <p>
     * Observation:
     * We don't really care what day(s) to buy and sell. As long as the graph is ascending, we can make profit.
     * <p>
     * Algo:
     * Instead of looking for peak following a valley, we just iterate the list and keep on adding the profit,
     * i.e. positive difference between the consecutive numbers in the list, and the total sum in the end
     * is the max profit.
     * Time Complexity: O(N). Space Complexity: O(1)
     */
    int maxProfitTwo(int[] prices) {
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1])
                maxProfit += prices[i] - prices[i - 1];
        }
        return maxProfit;
    }

    /**
     * Best Time to Buy and Sell Stock III
     * You are given an array prices where prices[i] is the price of a given stock on
     * the ith day.
     * <p>
     * Find the maximum profit you can achieve. You may complete at most two transactions.
     * <p>
     * Note: You may not engage in multiple transactions simultaneously (i.e., you must
     * sell the stock before you buy again).
     * <p>
     * Input: prices = [3,3,5,0,0,3,1,4]
     * Output: 6
     * Explanation: Buy on day 4 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
     * Then buy on day 7 (price = 1) and sell on day 8 (price = 4), profit = 4-1 = 3.
     * <p>
     * Input: prices = [1,2,3,4,5]
     * Output: 4
     * Explanation: Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
     * Note that you cannot buy on day 1, buy on day 2 and sell them later, as you are
     * engaging multiple transactions at the same time. You must sell before buying again.
     * <p>
     * Input: prices = [7,6,4,3,1]
     * Output: 0
     * Explanation: In this case, no transaction is done, i.e. max profit = 0.
     * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/description/
     */
    @Test
    void maxProfitToBuySellStockThree() {
        int[] intArray = new int[]{8, 10, 3, 7, 4, 9, 2, 3};
        // Buy at 3, Sell at 7, Buy again at 4, sell at 9
        assertThat(maxProfitWithMaxTwoTransactions(intArray)).isEqualTo(9);
        intArray = new int[]{3, 3, 5, 0, 0, 3, 1, 4};
        assertThat(maxProfitWithMaxTwoTransactions(intArray)).isEqualTo(6);
        // Buy on day 4 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
        // Then buy on day 7 (price = 1) and sell on day 8 (price = 4), profit = 4-1 = 3.
        intArray = new int[]{1, 2, 3, 4, 5};
        assertThat(maxProfitWithMaxTwoTransactions(intArray)).isEqualTo(4);
        // Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
    }

    /**
     * Iterate the array and track the minPrice, maxProfitAfterFirstSell, maxProfitLeftAfterSecondBuy and
     * maxProfitAfterSecondSell. maxProfitAfterSecondSell is the answer to return.
     * <p>
     * Observation:
     * This is the extension of the FIRST Best Time to Buy and Sell Stock question. The difference is now
     * we also need to track the profit if we make the second buy-sell transaction.
     * <p>
     * Algo:
     * 1. Track the best profit after selling the first stock we can make so far by tracking the minPrice.
     * This part is the same as the first problem in this series.
     * <p>
     * 2. Track the best profit if we do the 2nd buy-sell
     * - If we buy the 2nd stock today, which means the first stock was sold before the current day, the
     * max profit(or the money we still have), maxProfitLeftAfterSecondBuy, we can have after the buy is
     * maxProfitAfterFirstSell - buy price of the current stock
     * - If we already bought the 2nd stock, the profit we can make when selling it now is to add the
     * current selling price to the maxProfitLeftAfterSecondBuy
     * <p>
     * 3. Return the maxProfitAfterSecondSell.
     * - In the case of one buy-sell maximizes the profit, maxProfitLeftAfterSecondBuy would be equal to
     * maxProfitAfterFirstSell.
     * <p>
     * Time Complexity: O(N).
     * Space Complexity: O(1)
     * <p>
     * https://cs.stackexchange.com/questions/60668/o1-space-on-complexity-algorithm-for-buy-and-sell-stock-twice-interview-que
     * LeetCode comment from aaronschwartzmessing
     */
    int maxProfitWithMaxTwoTransactions(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfitAfterFirstSell = 0;
        int maxProfitLeftAfterSecondBuy = Integer.MIN_VALUE;
        int maxProfitAfterSecondSell = 0;

        for (int p : prices) {
            // The max profit we can make from one buy-sell transaction. This is the same as the first problem in the
            // stock buy and sell problem series
            minPrice = Math.min(p, minPrice);
            maxProfitAfterFirstSell = Math.max(p - minPrice, maxProfitAfterFirstSell);
            // If we buy the 2nd stock today, which means the first stock was sold before the current day, the max
            // profit we can have after the buy is maxProfitAfterFirstSell - buy price of the current stock
            maxProfitLeftAfterSecondBuy = Math.max(maxProfitAfterFirstSell - p, maxProfitLeftAfterSecondBuy);
            // We already bought the 2nd stock, so the profit we can make when selling it is to add the current selling
            // price to the maxProfitLeftAfterSecondBuy
            maxProfitAfterSecondSell = Math.max(p + maxProfitLeftAfterSecondBuy, maxProfitAfterSecondSell);
            // Note: When looking at maxProfitLeftAfterSecondBuy and maxProfitAfterSecondSell together, we see it first
            // subtracts "p" from maxProfitAfterFirstSell in maxProfitLeftAfterSecondBuy, then adds p to
            // maxProfitLeftAfterSecondBuy in maxProfitAfterSecondSell. Cuz doing the 2nd buy-sell may not necessarily
            // maximize the profit. This is just to track "what if" we make the 2nd transaction, and this logic will
            // make the maxProfitAfterSecondSell equal to maxProfitAfterFirstSell(p in maxProfitLeftAfterSecondBuy and
            // maxProfitAfterSecondSell will be just canceled out) if one transaction yield the max profit.
        }
        return maxProfitAfterSecondSell;
    }

    /**
     * Best Time to Buy and Sell Stock IV
     * You are given an integer array prices where prices[i] is the price of a given stock
     * on the ith day, and an integer k.
     * <p>
     * Find the maximum profit you can achieve. You may complete at most k transactions:
     * i.e. you may buy at most k times and sell at most k times.
     * <p>
     * Note: You may not engage in multiple transactions simultaneously (i.e., you must
     * sell the stock before you buy again).
     * <p>
     * Input: k = 2, prices = [2,4,1]
     * Output: 2
     * Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.
     * <p>
     * Input: k = 2, prices = [3,2,6,5,0,3]
     * Output: 7
     * Explanation: Buy on day 2 (price = 2) and sell on day 3 (price = 6), profit = 6-2 = 4.
     * Then buy on day 5 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
     * <p>
     * https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/description/
     */
    @Test
    void maxProfitToBuySellStockFour() {
    }

    int maxProfitFour(int k, int[] prices) {
        // TBD
        return 0;
    }

    /**
     * Rotate Array
     * Given an integer array nums, rotate the array to the right by k steps, where k is non-negative.
     * Note: 0 <= k <= 10^5
     * <p>
     * Input: nums = [1,2,3,4,5,6,7], k = 3
     * Output: [5,6,7,1,2,3,4]
     * <p>
     * Input: nums = [-1,-100,3,99], k = 2
     * Output: [3,99,-1,-100]
     * https://leetcode.com/problems/rotate-array/solution/
     */
    @Test
    void rotateArray() {
        int[] intArray = new int[]{1, 2, 3, 4, 5, 6, 7};
        rotate(intArray, 2);
        Assertions.assertArrayEquals(new int[]{6, 7, 1, 2, 3, 4, 5}, intArray);
    }

    /**
     * Firstly reverse all the elements of the array. Then reverse the first K elements followed by reversing
     * the rest N−K elements. (Need to normalize K by K %= nums.length)
     * <p>
     * Observation:
     * When we rotate the array K times, K elements from the back end of the array come to the front and
     * the rest of the elements from the front shift backwards.
     * Ex: [1,2,3,4,5,6,7], k=3
     * ==> [5,6,7,1,2,3,4]
     * <p>
     * Algo:
     * 1. Cuz 0 <= k <= 10^5, we need to normalize k using k mod array.length
     * 2. Reverse all elements
     * 3. Reverse first k elements
     * 4. Reverse last n-k elements
     * Time Complexity: O(N). Space Complexity: O(1)
     */
    void rotate(int[] nums, int k) {
        k %= nums.length; // Need to do mod cuz the length may be greater than array length
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

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
     * Given an integer array nums, return true if any value appears at least twice in the array,
     * and return false if every element is distinct.
     * <p>
     * Input: nums = [1,2,3,1]
     * Output: true
     * <p>
     * Input: nums = [1,2,3,4]
     * Output: false
     * https://leetcode.com/problems/contains-duplicate/solution/
     */
    @Test
    void containsDuplicate() {
        int[] intArray = new int[]{1, 1, 3, 4, 5, 6};
        assertThat(checkDuplicate(intArray)).isTrue();
        assertThat(checkDuplicateUseSet(intArray)).isTrue();
    }

    /**
     * First sort the array, then iterate the array and compare each element with its subsequent
     * element.
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
     * Iterate array and add number to set, if add method returns false, duplicate is found
     * Time Complexity: O(n). Space Complexity: O(n)
     */
    boolean checkDuplicateUseSet(int[] nums) {
        Set<Integer> numSet = new HashSet<>(nums.length);
        for (Integer n : nums) {
            if (!numSet.add(n))
                return true;
            numSet.add(n);
        }
        return false;
    }


    /**
     * Intersection of Two Arrays
     * Given two integer arrays nums1 and nums2, return an array of their intersection.
     * Each element in the result must be unique and you may return the result in any order.
     * <p>
     * Input: nums1 = [1,2,2,1], nums2 = [2,2]
     * Output: [2]
     * <p>
     * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
     * Output: [9,4]
     * Explanation: [4,9] is also accepted.
     * https://leetcode.com/problems/intersection-of-two-arrays/solution/
     */
    @Test
    void findIntersect() {
        int[] intArray1 = new int[]{1, 2, 2, 1};
        int[] intArray2 = new int[]{2, 2};
        assertThat(intersectUnique(intArray1, intArray2)).containsOnly(2);
        // Another approach is using the Map solution of the findIntersectTwo question and
        // put the matched integer in a Set instead of the original array
    }

    /**
     * First turn two arrays to two Sets, then use retainAll method, i.e. set1.retainAll(set2). Then
     * convert set1 to array and return it.
     * Time Complexity: O(n + m). Space Complexity: O(n+m)
     */
    int[] intersectUnique(int[] nums1, int[] nums2) {
        Set<Integer> set1 = Arrays.stream(nums1).boxed().collect(Collectors.toSet());
        Set<Integer> set2 = Arrays.stream(nums2).boxed().collect(Collectors.toSet());
        set1.retainAll(set2); // set1 will be modified so that its value is the intersection of the two sets.
        return set1.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Intersection of Two Arrays II
     * Given two integer arrays nums1 and nums2, return an array of their intersection.
     * Each element in the result must appear as many times as it shows in both arrays and
     * you may return the result in any order.
     * <p>
     * Input: nums1 = [1,2,2,1], nums2 = [2,2]
     * Output: [2,2]
     * Example 2:
     * <p>
     * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
     * Output: [4,9]
     * Explanation: [9,4] is also accepted.
     * https://leetcode.com/problems/intersection-of-two-arrays-ii/solution/
     */
    @Test
    void findIntersectTwo() {
        int[] intArray1 = new int[]{4, 9, 5};
        int[] intArray2 = new int[]{9, 4, 9, 8, 4};
        assertThat(intersect(intArray1, intArray2)).containsOnly(9, 4);
    }

    /**
     * Iterate the smaller array and build the number to count Map. Then iterate the other array and check if
     * each number is in the map and count > 0, if so, add the num to the result and update the decrement
     * the count in the map
     * Time Complexity: O(n + m).
     * Space Complexity: O(min(n,m))
     */
    int[] intersect(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length)
            // check array sizes and use a hash map for the smaller array
            intersect(nums2, nums1);
        Map<Integer, Integer> valToCount = new HashMap<>();
        List<Integer> result = new ArrayList<>();
        for (int num : nums1)
            valToCount.put(num, valToCount.getOrDefault(num, 0) + 1);
        // Iterate num2 and check if it also exist in the Map, if so, add to the result and update the count in the Map
        for (int n : nums2) {
            Integer count = valToCount.getOrDefault(n, 0);
            if (count > 0) {
                result.add(n);
                valToCount.put(n, count - 1);
            }
        }
        return result.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Sort two arrays. Then use Two Pointers to iterate both arrays. Advance the ptr whose number is less.
     * If both point to the same number, add to the result and advance both.
     * This approach can be used if either the input arrays are sorted already or the result needs to be sorted
     * Time Complexity: O(n⋅log n + m⋅log m)
     * Space Complexity: O(n+m)
     */
    int[] intersectWithSort(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] < nums2[j])
                ++i;
            else if (nums1[i] > nums2[j])
                ++j;
            else {
                // nums1[i] == nums2[j]
                result.add(nums1[i]);
                i++;
                j++;
            }
        }
        return result.stream().mapToInt(n -> n).toArray();
    }


    /**
     * Move Zeroes
     * Given an integer array nums, move all 0's to the end of it while maintaining the
     * relative order of the non-zero elements.
     * <p>
     * Input: nums = [0,1,0,3,12]
     * Output: [1,3,12,0,0]
     * https://leetcode.com/problems/move-zeroes/solution/
     */
    @Test
    void testMoveZeroes() {
        int[] nums = new int[]{0, 1, 0, 3, 12};
        moveZeroes(nums);
        Assertions.assertArrayEquals(new int[]{1, 3, 12, 0, 0}, nums);
    }

    /**
     * Use extra pointer to track the next non-zero number should be placed(nextNonZeroNumInsertIdx: 0)
     * Then iterate the array, if current number is not zero, set it to nextNonZeroNumInsertIdx position,
     * and increment nextNonZeroNumInsertIdx. After the first loop, start the 2nd loop from the nextNonZeroNumInsertIdx
     * and set every element to zero until the end.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    void moveZeroes(int[] nums) {
        // To track the index where the next non-zero element should be placed.
        int nextNonZeroNumInsertIdx = 0;
        for (int num : nums) {
            if (num != 0) {
                // If the current element is non-zero, assign it to the position nums[i] in the array.
                // This moves the non-zero element to the left side of the array.
                nums[nextNonZeroNumInsertIdx] = num;
                nextNonZeroNumInsertIdx++;
            }
        }
        // At this point, we already moved all non-zero element to the left side.
        for (int i = nextNonZeroNumInsertIdx; i < nums.length; i++) {
            // Start from nextNonZeroNumInsertIdx and set every element to zero until the end
            nums[i] = 0;
        }
    }

    /**
     * String Compression
     * Given an array of characters chars, compress it using the following algorithm:
     * <p>
     * Begin with an empty string s. For each group of consecutive repeating characters in chars:
     * <p>
     * If the group's length is 1, append the character to s.
     * Otherwise, append the character followed by the group's length.
     * The compressed string s should not be returned separately, but instead, be stored in the
     * input character array chars. Note that group lengths that are 10 or longer will be split
     * into multiple characters in chars.
     * <p>
     * After you are done modifying the input array, return the new length of the array.
     * <p>
     * You must write an algorithm that uses only constant extra space.
     * <p>
     * Input: chars = ["a","a","b","b","c","c","c"]
     * Output: Return 6, and the first 6 characters of the input array should be: ["a","2","b","2","c","3"]
     * Explanation: The groups are "aa", "bb", and "ccc". This compresses to "a2b2c3".
     * <p>
     * Input: chars = ["a"]
     * Output: Return 1, and the first character of the input array should be: ["a"]
     * Explanation: The only group is "a", which remains uncompressed since it's a single character.
     * <p>
     * Input: chars = ["a","b","b","b","b","b","b","b","b","b","b","b","b"]
     * Output: Return 4, and the first 4 characters of the input array should be: ["a","b","1","2"].
     * Explanation: The groups are "a" and "bbbbbbbbbbbb". This compresses to "ab12".
     */
    @Test
    void testCompress() {
        char[] chars = new char[]{'a', 'a', 'b', 'b', 'c', 'c', 'c'};
        int ans = compress(chars);
        assertThat(ans).isEqualTo(6);
        assertThat(chars).containsExactly('a', '2', 'b', '2', 'c', '3', 'c');
        chars = new char[]{'a', 'b', 'c'};
        ans = compress(chars);
        assertThat(ans).isEqualTo(3);
        assertThat(chars).containsExactly('a', 'b', 'c');
    }

    /**
     * Use two pointers(left: 1, right: 0), left to track the next writing position for char and count
     * in front of the array . Use right to iterate the array, and maintain the last seen char and count.
     * If current char is different from last seen char, if count > 1, iteratively write each count char
     * to left ptr index and increment it. Then write the current char at left ptr and update the last
     * seen char to current char, and reset count to 1. We need to apply the same logic after the loop
     * ends for the last char in the array.
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    int compress(char[] chars) {
        if (chars.length == 1) {
            return 1;
        }
        int left = 1; // track the next writing position in front of the array
        int count = 0;
        char currentChar = chars[0];
        for (int right = 0; right < chars.length; right++) {
            char c = chars[right];
            if (c != currentChar) {
                if (count > 1) {
                    String countStr = Integer.toString(count);
                    for (int i = 0; i < countStr.length(); i++) {
                        chars[left++] = countStr.charAt(i);
                    }
                }
                chars[left++] = c; // write the current char in the front
                currentChar = c;
                count = 1;
            } else {
                count++;
            }
        }
        // Apply the same logic to the last char in the array
        if (count > 1) {
            String countStr = Integer.toString(count);
            for (int i = 0; i < countStr.length(); i++) {
                chars[left++] = countStr.charAt(i);
            }
        }
        return left;
    }

    /**
     * Solution from LeetCode
     */
    int compressLC(char[] chars) {
        int i = 0, res = 0;
        while (i < chars.length) {
            int groupLength = 1;
            while (i + groupLength < chars.length && chars[i + groupLength] == chars[i]) {
                groupLength++;
            }
            chars[res++] = chars[i];
            if (groupLength > 1) {
                for (char c : Integer.toString(groupLength).toCharArray()) {
                    chars[res++] = c;
                }
            }
            i += groupLength;
        }
        return res;
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
        assertThat(twoSum(nums, 9)).containsExactly(0, 1);
        nums = new int[]{3, 3};
        assertThat(twoSum(nums, 6)).containsExactly(0, 1);
        nums = new int[]{3, 2, 4};
        assertThat(twoSum(nums, 6)).containsExactly(1, 2);
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
     * Use Two Pointers, left(head) and right(tail). Start from two end, if sum of them is greater than target,
     * advance right, if less, advance left. Otherwise, return left and right index(we found the answer)
     * <p>
     * Algo:
     * Two Pointers at head and tail - move one of them depending on the comparison of their sum and target
     * We use two indices, initially pointing to the first and the last element, respectively.
     * Compare the sum of these two elements with target. If the sum is equal to target, we found the
     * exactly only solution. If it is less than target, we increase the smaller index by one. If it is
     * greater than target, we decrease the larger index by one. Move the indices and repeat the comparison
     * until the solution is found. This approach uses the fact of ascending order, moving to right will
     * increase the sum, while moving to left will decrease.
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
     * This is the extension of the 2 Sum question. This solution doesn't sort/change the input array, so it has
     * worse performance(check Leetcode for other solutions)
     * Algo:
     * Start iterating num array from beginning (Outer loop)
     * - First we check if we have visited the num before from the outer loop in a Set
     * - If not, this means now we pick the first number
     * - 	Start iterating num array from the next element of it
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
        Set<Integer> outerLoopVisited = new HashSet<>(); // Keep track of the number we visited in the outer loop
        for (int i = 0; i < nums.length; i++) { // Fix the first number
            if (outerLoopVisited.add(i)) { // skip the duplicate
                Set<Integer> innerLoopVisited = new HashSet<>(); // Keep track the num we've seen in the inner loop
                for (int j = i + 1; j < nums.length; j++) { // Inner loop starts form the next element
                    // Now we fix the 2nd number, then compute the 3rd number we need to form the triplet
                    int compliment = -nums[i] - nums[j]; // 0-(a+b) -> -a-b
                    if (innerLoopVisited.contains(compliment)) { // Check the Set to see if we have seen it
                        List<Integer> triplet = Arrays.asList(nums[i], nums[j], compliment);
                        triplet.sort(null); // Need to sort it to avoid same triplet but different order
                        results.add(triplet);
                    }
                    innerLoopVisited.add(nums[j]);
                }
            }
        }
        return new ArrayList<>(results);
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
        result = groupAnagramsWithoutSorting(strs);
        assertThat(result).containsOnly(List.of("bat"), List.of("tan", "nat"), List.of("eat", "tea", "ate"));
    }

    /**
     * Iterate the str array, for each str, get its char[] and sort it and use it as key and add the
     * sortedStr to associated list in the Map. Therefore, all anagram string will grouped under the
     * same sortedStr. Finally, iterate the map and add list to the result.
     * <p>
     * Time Complexity: O(NK⋅logK),
     * where N is the length of strs, and K is the maximum length of a string in strs
     * <p>
     * Space Complexity: O(N⋅K)
     * For array of strings, we store N strings of size K at most. So Space Complexity is O(K⋅N).
     */
    List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> strValToStrs = new HashMap<>();
        if (strs == null || strs.length == 0) {
            return new ArrayList<>();
        }
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            strValToStrs.computeIfAbsent(new String(chars), k -> new ArrayList<>()).add(str);
        }
        return new ArrayList<>(strValToStrs.values());
    }

    /**
     * Maintain charCount array(int[26]). Iterate strs, for each str, fill charCount array w/ 0 first,
     * iterate each char at str and increment the corresponding slot at charCount array. Then iterate
     * charCount and use StringBuilder to concatenate each count w/ delimiter '#'. Then use this
     * concatenated str as the key for the charCountToStrs map and add the str to the list.
     * <p>
     * Use Map(count of each chars -> str list) for grouping
     * Similar to the concept used in the Anagram problem. Two strings are anagrams if and only if their
     * character counts (respective number of occurrences of each character) are the same.
     * <p>
     * We can transform each string s into a character count, consisting of 26 non-negative integers
     * representing the number of a's, b's, c's, etc. We use these counts as the key for our hash map.
     * <p>
     * The count will be a string delimited with '#' characters. For example, abbccc will be
     * #1#2#3#0#0#0...#0 where there are 26 entries total.
     * Without the delimiter, in some edge case, such as aaaaaaaaaaab(11'a', 1'b') and abbbbbbbbbbb(1'a', 11'b')
     * will yield the same key as 111000...00
     * <p>
     * Time Complexity: O(NK), where N is the length of strs, and K is the maximum length of
     * a string in strs. Counting each string is linear in the size of the string, and we
     * count every string.
     * <p>
     * Space Complexity: O(NK), the total information content stored in ans.
     */
    List<List<String>> groupAnagramsWithoutSorting(String[] strs) {
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
        assertThat(increasingTriplet(new int[]{1, 2, 2, 1})).isFalse();
        assertThat(increasingKLongSequence(new int[]{20, 100, 10, 12, 5, 13})).isTrue();
    }

    /**
     * Maintain two vars(first & second, init to INT_MAX), and for each number in the array, if it is
     * less than or equal to first, set first to it. Else if less than or equal to second, set second
     * to it, otherwise we get a triplet, so return true
     * Algo:
     * first_num = second_num = some very big number
     * for n in nums:
     * -     if n <= first_num:
     * -         first_num = n
     * -     else if n <= second_num:
     * -         second_num = n
     * -     else
     * -         # n > second > first
     * -         # We have found our triplet, return True
     * # After loop has terminated
     * # If we have reached this point, there is no increasing triplet, return False
     * Note: In some test case, you may see first_num is updated to the number whose index is bigger
     * than second_num after the loop ends. This is possible but doesn't affect the result, cuz our
     * logic guarantee that as long as we have set the second_num, it means the first_num must be
     * set to a number before it.
     * Time complexity : O(N)
     * Space complexity : O(1)
     */
    boolean increasingTriplet(int[] nums) {
        if (nums.length < 3)
            return false;
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for (int num : nums) {
            // We must include equal when doing the comparison, cuz we need 3 ascending numbers. So we can only set the
            // second number only if the current number is greater than the first. Same thing also applies to the last
            // condition when the current number is greater than second. Ex, [1,1,1] and [1, 2, 2, 1]
            if (num <= first)
                first = num;
            else if (num <= second)
                second = num;
            else
                // num > second > first, so we get a triplet
                return true;
        }
        return false;
    }

    boolean increasingKLongSequence(int[] nums) {
        int[] mins = new int[3];
        Arrays.fill(mins, Integer.MAX_VALUE);
        for (int num : nums) {
            int idx = Arrays.binarySearch(mins, num);
            if (idx < 0) {
                idx = -idx - 1;
            }

            mins[idx] = num;
            if (idx == mins.length - 1) {
                return true;
            }
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
    String countAndSayV2(int n) {
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
     * The majority element is the element that appears more than ⌊n / 2⌋ times. You may assume that the majority element
     * always exists in the array.
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
     * Naive approach: (Not O(1) space complexity)
     * - Iterate array and build the numberToCount HashMap, then return the number w/ max count
     * - Sort the array and return the nums[nums.length/2] element. No matter odd or even-length
     * array, the majority must be overlap at the floor(n/2) element.
     * <p>
     * Ad hoc approach - Boyer-Moore Voting Algorithm
     * 1. Maintain two int vars, count and num, both init to 0.
     * 2. For each number x in the array
     * -    If x = num, increment the count
     * -    Else if count = 0, set num to x and increment count.
     * -    Else decrement the count
     * 3. Return num
     * <p>
     * (The condition check is changed a little bit, so it can be reused for problem "Majority Element II"
     * in the same way)
     * <p>
     * * Whenever count equals 0, we effectively forget about everything in nums up to the current
     * index and consider the current number. This alog is Boyer-Moore Voting Algorithm
     * <p>
     * This algo is hard to understand, check NeetCode video
     * https://www.youtube.com/watch?v=7pnhv842keE&ab_channel=NeetCode
     * <p>
     * Time complexity : O(n)
     * Space complexity : O(1)
     */
    int majorityElement(int[] nums) {
        int count = 0;
        Integer majorityNum = null;
        for (int num : nums) {
            if (majorityNum != null && num == majorityNum)
                ++count;
            else if (count == 0) {
                majorityNum = num;
                ++count;
            } else
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

    @Test
    void testMajorityElementII() {
        assertThat(majorityElementII(new int[]{3, 2, 3})).containsOnly(3);
        assertThat(majorityElementII(new int[]{1})).containsOnly(1);
        assertThat(majorityElementII(new int[]{1, 2})).containsOnly(1, 2);
        assertThat(majorityElementII(new int[]{2, 1, 1, 3, 1, 4, 5, 6})).containsOnly(1);
    }

    /**
     * This is the extension of the "Majority Element" problem.
     * The problem can still be solved using the numberToCount HashMap.
     * But we need to use Boyer-Moore Voting Algorithm to achieve O(1) space complexity.
     * <p>
     * First, given any array, there can be at most 2 elements that each of them has more than
     * floor(n/3) occurrences. Therefore, we can just use extra count and candidate variables
     * and apply the same algo.
     * <p>
     * 1. Maintain four int vars, count1 and count2, candidate1 and candidate2.
     * 2. For each number x in the array
     * -    If x = candidate1, increment the count1
     * -    Else if x = candidate2, increment the count2
     * -    Else if count1 = 0, set candidate1 to x and increment count1.
     * -    Else if count2 = 0, set candidate2 to x and increment count2.
     * -    Else decrement the count1 and count2
     * 3. Iterate the array again to compute the actual counts of candidate1 and candidate2.
     * -  (Cuz there can be at most two numbers having more than n/3 occurrences, it is possible only
     * -   one of the candidates is valid or some edge case. So we need to check them again.)
     * 4. Add the candidate1 and candidate2 to result ONLY if its actual count is greater than n/3
     * 5. Return result
     * <p>
     * Time complexity : O(n)
     * Space complexity : O(1)
     */
    List<Integer> majorityElementII(int[] nums) {
        // Given any array, there can be at most 2 elements that each of them has more than floor(n/3) occurrences.
        Integer candidate1 = null;
        Integer candidate2 = null;
        int count1 = 0;
        int count2 = 0;
        for (int num : nums) {
            if (candidate1 != null && num == candidate1) {
                count1++;
            } else if (candidate2 != null && num == candidate2) {
                count2++;
            } else if (count1 == 0) {
                candidate1 = num;
                count1++;
            } else if (count2 == 0) {
                candidate2 = num;
                count2++;
            } else {
                count1--;
                count2--;
            }
        }
        // Iterate the array again to compute the actual counts of candidate1 and candidate2.
        // Cuz there can be at most two numbers having more than n/3 occurrences, it is possible only one of
        // the candidate variables is valid. So we need to check them again.
        int realCount1 = 0;
        int realCount2 = 0;
        for (int num : nums) {
            if (num == candidate1)
                realCount1++;
            else if (candidate2 != null && num == candidate2)
                realCount2++;
        }
        List<Integer> result = new ArrayList<>();
        if (realCount1 > nums.length / 3)
            result.add(candidate1);
        if (realCount2 > nums.length / 3)
            result.add(candidate2);
        return result;
    }

    public List<Integer> majorityElementLC(int[] nums) {

        // 1st pass
        int count1 = 0;
        int count2 = 0;

        Integer candidate1 = null;
        Integer candidate2 = null;

        for (int n : nums) {
            if (candidate1 != null && candidate1 == n) {
                count1++;
            } else if (candidate2 != null && candidate2 == n) {
                count2++;
            } else if (count1 == 0) {
                candidate1 = n;
                count1++;
            } else if (count2 == 0) {
                candidate2 = n;
                count2++;
            } else {
                count1--;
                count2--;
            }
        }

        // 2nd pass
        List result = new ArrayList<>();

        count1 = 0;
        count2 = 0;

        for (int n : nums) {
            if (candidate1 != null && n == candidate1) count1++;
            if (candidate2 != null && n == candidate2) count2++;
        }

        int n = nums.length;
        if (count1 > n / 3) result.add(candidate1);
        if (count2 > n / 3) result.add(candidate2);

        return result;
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
        assertThat(leastIntervalOpt(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2)).isEqualTo(8);
        assertThat(leastInterval(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2)).isEqualTo(8);
    }

    /**
     * First use the map to build Task objects(count, nextRunnableTime). Add all the tasks to a MaxHeap
     * (Task.count). We also maintain a queue for the task just processed. While MaxHeap and queue is
     * not empty, first increment the time. If MaxHeap is empty, fast-forward the current time to the
     * top task's next runnable time in the queue. Then we pull all tasks in the queue whose next
     * runnable time is equal or less than current time, and add them to the MaxHeap. Then poll the
     * MaxHeap and decrement the task's count and update its next runnable time to current time
     * + cooling time + 1. If the task count is not 0, add it to the queue.
     * <p>
     * The idea here is to greedily select the most repeated task available to schedule every time.
     * In order to find the most repeated task, we use a priority queue to get the max repeated task
     * efficiently. We have to make sure only tasks available for schedule are in the priority queue.
     * For this, we add any scheduled task to a separate cool down queue. When top of this queue is
     * available to schedule, i.e. the current time is equal or pass its next runnable time,  we add
     * it back to priority queue.
     * <p>
     * Time complexity is O(nlog(m)) with n being the number of tasks given to schedule and m being
     * the number of unique tasks. The polling step in PriorityQueue is O(log(m)).
     * <p>
     * Space complexity is O(m) since the priority queue and cool down queue can at max have m items.
     */
    int leastInterval(char[] tasks, int n) {
        class Task {
            int count, nextRunnableTime;

            Task(int count) {
                this.count = count;
            }
        }

        // if n == 0 there will be no idle periods, so return length of tasks
        if (n == 0)
            return tasks.length;

        Map<Character, Task> idToTask = new HashMap<>();
        for (char c : tasks) {
            idToTask.putIfAbsent(c, new Task(0));
            idToTask.get(c).count++;
        }

        PriorityQueue<Task> pendingTaskMaxHeap = new PriorityQueue<>(Comparator.<Task>comparingInt(t -> t.count).reversed()); // Max Heap

        // Contains the tasks that were processed and wait to be selected when their next runnable time <= current time
        Queue<Task> coolingTaskQueue = new ArrayDeque<>();
        // In the beginning, add all the task to the max heap
        pendingTaskMaxHeap.addAll(idToTask.values());
        int time = 0;

        while (!pendingTaskMaxHeap.isEmpty() || !coolingTaskQueue.isEmpty()) {
            time++;

            // if no pending tasks, fast-forwarding the time to the next runnable time of the top task of coolingTaskQueue.
            // This basically implies we extend the time to idle until the first cooling task becomes available
            if (pendingTaskMaxHeap.isEmpty())
                time = coolingTaskQueue.peek().nextRunnableTime;

            // Add any tasks in cooling that just became available for scheduling
            while (!coolingTaskQueue.isEmpty() && coolingTaskQueue.peek().nextRunnableTime <= time) {
                pendingTaskMaxHeap.add(coolingTaskQueue.poll());
            }

            // Process the task w/ the most count from the max heap.
            Task t = pendingTaskMaxHeap.poll();
            t.nextRunnableTime = time + n + 1;
            t.count--;

            // Add the task back into cooling if there are more instances of it to schedule.
            if (t.count != 0)
                coolingTaskQueue.offer(t);
        }
        return time;
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
    int leastIntervalOpt(char[] tasks, int n) {
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
     * Iterate backwards thru the array, If current temp >= max temperature so far, update it and continue. Otherwise,
     * search for the next higher temp from the current day forwards using the "day to next warmer day" result stored
     * in the answer array. And save the result(accumulated days) in the answer array.
     * <p>
     * Algo:
     * The idea is when we iterate backward thru the array, the number of day in the answer array can be used for
     * searching for the next warmer day more quickly. We also track the hottest day so far while iterating so we can
     * quickly tell if we can skip this element, i.e. no warmer day.
     * <p>
     * When we search for the next warmer day, we start from the next day temperature first. If it is hotter, 1 day
     * is the answer, otherwise, we add its associated number of day to the next warmer day from the answer array to the
     * current numOfDays var, and check if the temperature on that day(temperatures[i + numOfDays]) is warmer.
     * We repeat this process until we find the warmer day in the future and keep adding the number of day to the numOfDays.
     * This logic lets us quickly jump to the next day having the next warmer day until the day hotter than the current
     * day is found. And the sum of all jumps we made is the answer.
     * <p>
     * Time complexity: O(N)
     * The nested while loop makes this algorithm look worse than O(N).
     * The total number of iterations in the while loop does not exceed N, which gives this algorithm
     * a time complexity of O(2⋅N) = O(N).
     * <p>
     * Space complexity: O(1)
     */
    int[] dailyTemperatures(int[] temperatures) {
        int maxTemp = 0;
        int[] answers = new int[temperatures.length];
        for (int i = temperatures.length - 1; i >= 0; i--) {
            int currentTemp = temperatures[i];
            if (currentTemp >= maxTemp) {
                // Check if the current day is the hottest one seen so far
                // Last day is always 0 in the answer array cuz there is no other future days
                maxTemp = currentTemp;
                // We are iterating from the "future"(end of array), so if it is hotter than any future days, its associated
                // answer must be zero, i.e. no warmer day. Therefore, skip and proceed to the next day (answer[i] remains 0)
                continue;
            }
            // currentTemp < maxTemp --> There is a warmer day in the future we have seen
            int numOfDays = 1; // init to 1, cuz we start to compare w/ the next day first
            // Start to search for the next higher temperature
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
     * Iterate the array and use monotonic decreasing stack to hold the index to find the next greater number(temperature).
     * The index difference between the current temp index and popped-out item is the number of days for next warmer day.
     * <p>
     * Monotonic decreasing means that the stack will always be sorted in descending order. Because the problem is
     * asking for the number of days, instead of storing the temperatures themselves, we should store the indices of
     * the days, and use temperatures[i] to find the temperature of the ith day.
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
        Deque<Integer> stack = new ArrayDeque<>(); // Stores the index of temperature array
        for (int i = 0; i < temperatures.length; i++) {
            int currentTemp = temperatures[i];
            while (!stack.isEmpty() && temperatures[stack.peek()] < currentTemp) {
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
     * Use decreasing Monotonic Stack to search for next greater number and save in a Map
     * Iterate over the nums2 array from the left to right. Before pushing the current element to the stack,
     * we first check if the stack is empty and the top element in the stack is smaller than the current
     * element, if so, we pop it out and this current element is the next greater element of the pop-out
     * element. We need to keep checking the top element on the stack until the top one is not smaller than
     * the current one. (This is the standard process for decreasing Monotonic Stack, items on the
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
                // Popping out the number from the stack if it is smaller than the current num. Cuz the current num comes
                // after every element in the stack, it is literally the next greater number in the array.
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
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
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
     * 132 Pattern
     * Given an array of n integers nums, a 132 pattern is a subsequence of three integers
     * nums[i], nums[j] and nums[k] such that i < j < k and nums[i] < nums[k] < nums[j].
     * <p>
     * Return true if there is a 132 pattern in nums, otherwise, return false.
     * <p>
     * Input: nums = [1,2,3,4]
     * Output: false
     * Explanation: There is no 132 pattern in the sequence.
     * <p>
     * Input: nums = [3,1,4,2]
     * Output: true
     * Explanation: There is a 132 pattern in the sequence: [1, 4, 2].
     * <p>
     * Input: nums = [-1,3,2,0]
     * Output: true
     * Explanation: There are three 132 patterns in the sequence: [-1, 3, 2], [-1, 3, 0] and
     * [-1, 2, 0].
     * <p>
     * https://leetcode.com/problems/132-pattern/description/
     */
    @Test
    void testFind132pattern() {
        assertThat(find132pattern(new int[]{1, 2, 3, 4})).isFalse();
        assertThat(find132pattern(new int[]{3, 1, 4, 2})).isTrue();
        assertThat(find132pattern(new int[]{3, 5, 0, 3, 4})).isTrue();
    }

    /**
     * For each number in the array, use decreasing monotonic stack to find the previous greater number,
     * nums[j], while also maintaining the previous minimum number for each nums[j] in the stack. So we can
     * compare the three numbers to determine if we find the pattern.
     * <p>
     * Observation:
     * 1. We can break down the problem into two pieces.
     * (1) Given the number, K(idx: k), we want to find the previous greater number for it, say J(idx: j)
     * (2) If J is found, we want to find the previous minimum element for J, say I(idx: i)
     * Finally we can check if these 3 numbers satisfies I < K < J
     * <p>
     * 2. We can use strict monotonic decreasing stack to find the previous greater number. For the previous
     * minimum element, we can maintain another var for each number to keep track of its previous minimum
     * value.
     * <p>
     * Algo:
     * 1. We maintain a strict monotonic decreasing stack. Each entry is a pair of number and the previous
     * minimum number.
     * 2. Iterate the array and popping the elements smaller or equal to the current number from the stack.
     * 3. If the stack is not empty, the top element, say T, is the previous greater number for the current
     * num, check its previous minimum number to see if we find the pattern, previous mini num of T <
     * current num < T. If so, return true.
     * (We keep previous greater num in the stack cuz it can still be the previous greater for the remaining
     * numbers we will visit later, e.g. 5, 3...4)
     * <p>
     * Time complexity : O(n)
     * We push and pop the N elements on the stack.
     * <p>
     * Space complexity : O(n)
     */
    boolean find132pattern(int[] nums) {
        // Pair[num, min value before num]
        Deque<Pair<Integer, Integer>> stack = new ArrayDeque<>();
        // For each number, we maintain its previous minimum number
        int currentMin = nums[0];
        // Each iteration we treat "num" as the nums[k]. Then we search for the previous greater number, i.e. nums[j],
        // and the previous minimum number for nums[j], i.e. nums[i]
        for (int num : nums) {
            // find previous greater element, enforce strict decreasing monotonic stack
            while (!stack.isEmpty() && stack.peek().getKey() <= num) {
                stack.pop();
            }
            // after the while loop, only the elements which are greater than the current element are left in stack
            if (!stack.isEmpty() && stack.peek().getValue() < num) {
                // If the previous greater element exists, i.e. nums[j], then we check if the min value before j,
                // i.e. nums[i], is less than the current number. If so, nums[i] < nums[k] < nums[j] is satisfied.
                return true;
            }
            stack.push(new Pair<>(num, currentMin));
            // Update the currentMin by taking the current num into account. We need this for the next iteration
            currentMin = Math.min(num, currentMin);
        }
        return false;
    }

    /**
     * Buildings With an Ocean View
     * There are n buildings in a line. You are given an integer array heights of size n that represents
     * the heights of the buildings in the line.
     * <p>
     * The ocean is to the right of the buildings. A building has an ocean view if the building can see
     * the ocean without obstructions. Formally, a building has an ocean view if all the buildings to its
     * right have a smaller height.
     * <p>
     * Return a list of indices (0-indexed) of buildings that have an ocean view, sorted in
     * increasing order.
     * <p>
     * Input: heights = [4,2,3,1]
     * Output: [0,2,3]
     * Explanation: Building 1 (0-indexed) does not have an ocean view because building 2 is taller.
     * <p>
     * Input: heights = [4,3,2,1]
     * Output: [0,1,2,3]
     * Explanation: All the buildings have an ocean view.
     * <p>
     * Input: heights = [1,3,2,4]
     * Output: [3]
     * Explanation: Only building 3 has an ocean view.
     * <p>
     * https://leetcode.com/problems/buildings-with-an-ocean-view/description/
     */
    @Test
    void testFindBuildings() {
        assertThat(findBuildingsUseStack(new int[]{4, 2, 3, 1})).containsExactly(0, 2, 3);
        assertThat(findBuildingsUseStack(new int[]{4, 3, 2, 1})).containsExactly(0, 1, 2, 3);
        assertThat(findBuildingsUseStack(new int[]{1, 3, 2, 4})).containsExactly(3);
        assertThat(findBuildingsUseStack(new int[]{2, 2, 2, 2})).containsExactly(3);
    }

    /**
     * Observation:
     * A building has ocean view if all buildings on its right are smaller than this building.
     * In other words, we want to find which of the buildings do NOT have next greater or equal element.
     * <p>
     * Algo:
     * Iterate the array and maintain a monotonic strictly decreasing stack to find the next greater
     * height. We pop out the buildings which have another building with equal or greater height
     * in view. Then push the current building to the stack. After the loop ends, the elements left in
     * the stack will be the ones which don't have any greater elements after them. Then pop each
     * item and insert to array in reverse order.
     * <p>
     * Time complexity: O(N)
     * Space complexity: O(N)
     */
    int[] findBuildingsUseStack(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        // Maintain monotonic strictly decreasing stack to find next greater or equal element
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] <= heights[i]) {
                stack.pop();
            }
            stack.push(i);
        }
        // The elements left in the stack are the ones don't have any greater or equal elements after them
        if (!stack.isEmpty()) {
            int[] temp = new int[stack.size()];
            // Need to iterate in reverse cuz we pop from the stack(Actually we can just iterate normally and use
            // removeLast since we use Deque)
            for (int i = temp.length - 1; i >= 0; i--) {
                temp[i] = stack.pop();
            }
            return temp;
        }
        return new int[0];
    }

    /**
     * Iterate the array from the end and keep track of the current max height. For any building higher
     * than the current max height, add it to the list and update the max height. In the end, get each
     * item from the list in reverse order and put it into the result array.
     * <p>
     * Time complexity: O(N)
     * Space complexity: O(N)
     */
    int[] findBuildings(int[] heights) {
        List<Integer> buildingIdxWithView = new ArrayList<>();
        // Keep track of the max height so far
        int maxHeight = -1;
        // Iterate from the end so we can track the max height
        for (int i = heights.length - 1; i >= 0; i--) {
            if (heights[i] > maxHeight) {
                // Higher than the current max height, so it has view
                buildingIdxWithView.add(i);
                maxHeight = heights[i];
            }
        }
        int[] result = new int[buildingIdxWithView.size()];
        for (int i = 0; i < result.length; i++) {
            // While we iterate the result array forward, we need to get the item from list in reversed order
            result[i] = buildingIdxWithView.get(buildingIdxWithView.size() - 1 - i);
        }
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
        assertThat(trapWaterUseStack(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1})).isEqualTo(6);
        assertThat(trapWaterUseStack(new int[]{3, 2, 0, 3, 2, 5})).isEqualTo(5);
        assertThat(trapWaterUseStack(new int[]{1, 1, 1})).isEqualTo(0);
        assertThat(trapWaterUseStack(new int[]{4, 2, 0, 3, 2, 5})).isEqualTo(9);
    }

    /**
     * Maintain Two Pointers(left/right) from both end. We keep track of the current max left/right bar height and advance
     * the ptr w/ the bigger max value and compute the water amount(leftMax/rightMax - height). Continue this until two ptr
     * are cross each other.
     * <p>
     * First, the maximum level of water it can trap after the rain, which is equal to the minimum of maximum height of bars
     * on both the sides minus its own height. So the water amount of a given position i is
     * <p>
     * min(left_max, right_max) − height[i]
     * *The left/right max is based on the current position i
     * <p>
     * However, we don't really need to know both left_max and right_max, we just need to know the value of the smaller
     * one. So we can have two pointers starting from head and tail, and calculate the maxLeft and maxRight as we go.
     * <p>
     * How to decide to move left or move right?
     * If maxLeft < maxRight, it means the water level is based on the left side (the left bar is smaller).
     * We move left side:
     * - Move left by left += 1
     * - Update the leftMax first
     * - Compute the amount of trap water, which is maxLeft - height[left].
     * <p>
     * Else (maxLeft >= maxRight), it means the water level is based on the right side (the right bar is smaller).
     * We move right side:
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
            // A taller bar exists on left pointer's right side
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
                // A taller bar exists on right pointer's left side
                rightPtr--; // Move the ptr first cuz it starts at the length-1 index and there is no trapping water for the last position
                rightMax = Math.max(rightMax, height[rightPtr]);
                ans += rightMax - height[rightPtr];
            }
        }
        return ans;
    }

    /**
     * Iterate the array and maintain the non-decreasing monotonic stack to look for all "valley bar", which has next
     * greater bar and a previous greater bar, and compute the water volume for each of them.
     * <p>
     * Observation:
     * 1. To be able to trap the water, we need two nonadjacent bars to save water, and there must be at least one
     * valley bar between them. For bar[i], if we can find bar[j] and bar[k] where bar[j] > bar[i], bar[k] >
     * bar[i] and j < i < k, then bar[i] is a valley bar.
     * <p>
     * 2. Hence, given the bar i, if it has a next greater bar, k(right bar), and a previous greater bar, j(left bar).
     * We can trap water at i. And we just need to find all valley bars and calculate the water volume.
     * <p>
     * Algo:
     * Iterate the array and maintain a non-decreasing monotonic stack(store index). (Equal element is allowed in the stack)
     * - Continuously pop out the element from the stack if it is less than the current bar height
     * -- Now the popped-out bar is the valley bar, and the current bar is the right bar of the valley.
     * -- Next we check if the stack still has element, if so, this is the left bar of the valley(The stack is
     * decreasing order so it must be greater than valley bar).
     * -- Compute the height, min(left bar, right bar) - valley bar, and width and finally the volume.
     * -- Push the current bar index to the stack.
     */
    int trapWaterUseStack(int[] height) {
        Deque<Integer> stack = new ArrayDeque<>(); // Hold the index of height array
        int ans = 0;
        for (int i = 0; i < height.length; i++) {
            int currentBarHeight = height[i];
            // We want to find the next greater bar, so it can form a valley then it can trap the water. A plateau doesn't
            // trap any water, so we don't pop equal bar from the stack. This maintains non-decreasing monotonic stack.
            while (!stack.isEmpty() && height[stack.peek()] < currentBarHeight) {
                // The current bar is the next greater element to the one at the stack top, which means current
                // bar will be the right bar of a valley
                int valleyHeight = height[stack.pop()]; // This is the bottom of a valley
                // At each iteration, we use the current bar as valley right bar and compute the water volume of this valley
                // if the previous greater bar of the bottom valley bar exists, i.e. left bar. We repeat this until currentBarHeight
                // is NOT greater than the top bar on the stack, which means no valley and can't trap the water
                if (!stack.isEmpty()) {
                    // If nothing left in the stack, that means this valley bar is one of the ascending bars in the beginning,
                    // so all of its previous smaller bars are already pop.
                    // To calculate the water trapped in this valley, we need to find out the taller bar on the left.
                    // When the stack is not empty, cuz we maintain non-decreasing monotonic stack, the top element at the stack
                    // is either greater or equal to the valleyHeight, we can use it as the left bar of a valley (If equal,
                    // the height will become zero, so it won't trap any water)
                    int leftBarHeight = height[stack.peek()];
                    // h (height) is the minimum of the previous greater(left bar) and the next greater(right bar) elements - valley bar height
                    int h = Math.min(leftBarHeight, currentBarHeight) - valleyHeight;
                    // w (width) is the space between next greater and previous greater element
                    int w = i - (stack.peek() + 1);
                    ans += h * w;
                }
            }
            stack.push(i);
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
        assertThat(largestRectangleAreaLeetCode(new int[]{2, 1, 5, 6, 2, 3})).isEqualTo(10);
        assertThat(largestRectangleAreaLeetCode(new int[]{2, 4})).isEqualTo(4);
        assertThat(largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3})).isEqualTo(10);
        assertThat(largestRectangleArea(new int[]{2, 4})).isEqualTo(4);
        assertThat(largestRectangleArea(new int[]{1, 2, 2, 2, 1})).isEqualTo(6);
    }

    /**
     * Iterate array and use increasing Monotonic stack to find the previous smaller bar and next
     * smaller bar for each one. Then iterate again and use those info to compute the rectangle for
     * each bar and keep track of the max one.
     * <p>
     * Observation:
     * 1. The height of the maximal rectangle will be equal to the shortest bar included in it.
     * Otherwise, the rectangle would not be valid.
     * <p>
     * 2. Our goal is for each bar, compute the maximal rectangle which uses it as the shortest bar.
     * Hence, the height of each rectangle is subject to the height of that bar.
     * <p>
     * 3. Now we will use the height(H) of each bar to compute its max area. We need to figure out the
     * width to max out the rectangle area. To include adjacent bar in the rectangle to make it wider,
     * its height must be equal or greater than H. Therefore, we can't include any bar shorter than H.
     * While we consider adjacent bar to expand the rectangle, the shorter bars on the left or right
     * side will become the boundary of the max rectangle we can get.
     * <p>
     * 4. Hence, for the i-th bar w/ height(H), the left bound of its max rectangle will be the first
     * bar before i and shorter than H. The right bound will be the first bar after i and shorter than X
     * <p>
     * Algo:
     * 1. Init nextSmallerIdx array using heights.length as value
     * - We will store the index of the next smaller bar of each bar here
     * - Need to make up a dummy smallest bar(idx: heights.length) beyond the right bound of array,
     * so there will be always a next smaller bar for every bar.
     * <p>
     * 2. Init prevSmallerIdx array using -1 as value
     * - We will store the index of the previous smaller bar of each bar here
     * - Need to make up a dummy smallest bar(idx: -1) beyond the left bound of array, so there will
     * be always a previous smaller bar for every bar.
     * <p>
     * 3. Iterate array and maintain increasing monotonic stack before pushing the bar index to the stack.
     * - We pop out any bar taller than the current bar. Then the current bar is literally their next
     * smaller bar, so we set this at the nextSmallerIdx array.
     * - If the stack is not empty, the top bar at the stack will be the previous smaller bar of the
     * current bar, so we set this at the prevSmallerIdx array.
     * - Push the current bar index to the stack
     * <p>
     * 4. Iterate the array again, for each bar i, use the current bar height and the width
     * (nextSmallerIdx[i] - prevSmallerIdx[i] - 1), to compute the area of the rectangle. We keep track
     * of the max rectangle area so far.
     * <p>
     * Time complexity: O(n)
     * Space complexity: O(n)
     */
    int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>(); // Hold the index of element in heights
        int[] nextSmallerIdx = new int[heights.length];
        int[] prevSmallerIdx = new int[heights.length];
        // Init the nextSmallerIdx to heights.length, which is a dummy bar after the last bar. For the smallest bar
        // or the last bar in the array, we will never find their next smaller in the stack/array. However, we still
        // need a right bound to compute the rectangle width. So we choose a dummy bar(index: heights.length), just like
        // there is a bar after the last bar and use it as the default value of nextSmallerIdx.
        Arrays.fill(nextSmallerIdx, heights.length);
        // Init the prevSmallerIdx to -1, which is a dummy bar before the first bar. For the smallest bar
        // or the first bar in the array, we will never find their previous smaller in the stack/array. However, we still
        // need a left bound to compute the rectangle width. So we choose a dummy bar(index: -1), just like there is a
        // bar before the first bar and use it as the default value of prevSmallerIdx.
        Arrays.fill(prevSmallerIdx, -1);

        for (int i = 0; i < heights.length; i++) {
            // enforce increasing monotonic stack
            while (!stack.isEmpty() && heights[stack.peek()] > heights[i]) {
                Integer prevTallerBarIdx = stack.pop();
                // The current bar is the next smaller bar for every bar popped out from the stack
                nextSmallerIdx[prevTallerBarIdx] = i;
            }
            // Previous while loop assures stack will remain monotonic increasing even after we push the current bar
            if (!stack.isEmpty()) {
                // At this point, elements on the stack must be smaller or equal to current height
                // the index at the stack top refers to the previous smaller element for `i`th bar
                prevSmallerIdx[i] = stack.peek();
            }
            stack.push(i);
        }
        int maxArea = 0;
        for (int i = 0; i < heights.length; i++) {
            int currentHeight = heights[i];
            // Num of bars in the range[left, right]: right - left - 1
            int width = nextSmallerIdx[i] - prevSmallerIdx[i] - 1;
            maxArea = Math.max(maxArea, currentHeight * width);
        }
        // Note: If bar j precedes bar i, and height[j] == height[i], prevSmallerIdx[i] will be j in this
        // case, which means the max rectangle using heights[i] as height will have wrong width.
        // (Supposedly bar j should also be included). However, this is fine because when there are consecutive
        // bars w/ the same height, the first one of them will always get the correct prevSmallerIdx, so its
        // max rectangle will be correct. Since we keep track of the max area so far, the wrong/smaller
        // rectangle computed from the rest of the bars doesn't matter.(In theory, they should all produce the
        // same rectangle, but this is not the case due to the logic that we consider previous bar w/ same height
        // as previous smaller one) See the test case [1, 2, 2, 2, 1].
        return maxArea;
    }

    /**
     * ---- This is the LeetCode official solution ----
     * The difference is it doesn't use extra array to store the previous smaller and next smaller bar.
     * It calculates the max rectangle when iterating, but the logic is harder to understand and remember.
     * <p>
     * Iterate array and use increasing Monotonic stack to find the previous smaller element and next
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
    int largestRectangleAreaLeetCode(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();// This is an increasing(bar height) monotonic stack containing the index of heights array
        int maxArea = 0;
        // The two dummy numbers we choose are hand-crafted to suit our algorithm of calculating the rectangle width.
        // For the smallest bar in the array, the rectangle spans cross all bars so every bar is included. This is also
        // the reason to push the dummyLeftSmallerIdx to the stack first, so it can be used as the previous smaller bar on the left(lIdx)
        // for the first bar and the smallest bar in the array.
        int dummyLeftSmallerIdx = -1, dummyRightSmallerIdx = heights.length;
        stack.push(dummyLeftSmallerIdx);
        for (int i = 0; i < heights.length; i++) {
            // We use <= so that means, the stack will always be strictly increasing - because elements are popped when they are equal
            // so equal elements will never stay in the stack
            //while (stack.peek() != dummyLeftSmallerIdx && heights[i] <= heights[stack.peek()]) {
            while (stack.peek() != dummyLeftSmallerIdx && heights[stack.peek()] >= heights[i]) {
                int rectHeight = heights[stack.pop()];
                // idx of the next smaller bar on the right(rIdx)
                // --> current index, i. (We are here cuz we just encounter a smaller bar when iterating to the right)
                // idx of the previous smaller bar on the left(lIdx)
                // --> top element on the stack (Remember this is an increasing monotonic stack, so it is smaller than the one just popped out)
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
     * Return the maximum element from each sliding window.
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
     * Maintaining a Monotonic Decreasing Queue(Deque) to keep the first element being the index of the
     * max element of every window. When adding new element to the deque, first remove the elements
     * smaller than it from the end of deque. Whenever the window size reaches k, put the head of deque
     * int the result list.
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
        // result list to array
        int[] r = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++)
            r[i] = ans.get(i);
        return r;
        //return ans.stream().mapToInt(i->i).toArray();
    }

    /**
     * Frequency of the Most Frequent Element
     * The frequency of an element is the number of times it occurs in an array.
     * <p>
     * You are given an integer array nums and an integer k. In one operation, you can choose
     * an index of nums and increment the element at that index by 1.
     * <p>
     * Return the maximum possible frequency of an element after performing at most k operations.
     * <p>
     * Input: nums = [1,2,4], k = 5
     * Output: 3
     * Explanation: Increment the first element three times and the second element two times to
     * make nums = [4,4,4]. 4 has a frequency of 3.
     * <p>
     * Input: nums = [1,4,8,13], k = 5
     * Output: 2
     * Explanation: There are multiple optimal solutions:
     * - Increment the first element three times to make nums = [4,4,8,13]. 4 has a frequency of 2.
     * - Increment the second element four times to make nums = [1,8,8,13]. 8 has a frequency of 2.
     * - Increment the third element five times to make nums = [1,4,13,13]. 13 has a frequency of 2.
     * <p>
     * Input: nums = [3,9,6], k = 2
     * Output: 1
     * <p>
     * https://leetcode.com/problems/frequency-of-the-most-frequent-element/description/
     */
    @Test
    void testMaxFrequency() {
        assertThat(maxFrequency(new int[]{1, 2, 4}, 5)).isEqualTo(3);
        assertThat(maxFrequency(new int[]{1, 4, 8, 13}, 5)).isEqualTo(2);
    }

    /**
     * Maintain a left ptr to use sliding window to iterate the array. We maintain the sum of numbers
     * in current window. When nums[right] * (right - left + 1) - winSum > k, keep shrinking the
     * window by dropping the left ptr number from the sum and move the left ptr. We update the max
     * window size after the window is valid. The max window size is the most frequency of the same
     * number after at most k increments.
     * <p>
     * Observation:
     * 1. We want to choose a target, which can increment other numbers to it using at most K times.
     * Since we can only do increment, sorting the array is necessary so when pick up the target,
     * we know what numbers in the array should be considered.
     * <p>
     * 2. We can use a sliding window to check the numbers. Once we set one of numbers in the window
     * as target, can we use no more than K increments to make others the same as target. As we know,
     * we can only do increment, so the most intuitive way is pick the rightmost number(max number)
     * in the window as target.
     * <p>
     * Algo:
     * Our goal is to find a window when using the rightmost number as the target, so we can use at
     * most K increments on the rest of numbers to make them the same as target. While we expand the
     * window by moving the right ptr, if the current window doesn't satisfy this condition, we need
     * to first shrink the window from left until it becomes valid. This is becuase the next right
     * ptr number is either equal or greater than the current one, it won't reduce the increment ops
     * we need if we move the right ptr further.
     * <p>
     * Therefore, the condition to determine if the window is "valid" if
     * target number * current window size - sum of numbers in window <= K
     * ==> (right ptr number) * (right - left + 1) - winSum <= k
     * When this condition is false, we want to move the left ptr to shrink the window.
     * Also, the left ptr shouldn't exceed right ptr.
     * <p>
     * When window is "valid", which means all number(s) in the window can be increment to the
     * target/max number in the window using no more than k increment ops. In other words, the size
     * of the window also means the number of occurrences/frequency of the same number after at most
     * k increments. So we update the max window size, and return it after the loop ends.
     * <p>
     * Time complexity: O(n⋅logn)
     * O(n⋅logn) Sort + O(n) Iterate the array
     * <p>
     * Space Complexity: O(n) JDK sort
     */
    int maxFrequency(int[] nums, int k) {
        Arrays.sort(nums);
        int left = 0;
        int maxWinSize = -1; // Max win is updated only when all numbers in it can be added
        long winSum = 0; // Track the sum of numbers in the current window
        for (int right = 0; right < nums.length; right++) {
            winSum += nums[right];
            // Cuz we sorted the array, nums[right] is the max number in the current window. Our alog is to find a
            // window when using the rightmost number as the target, we can use k increments on the rest of numbers to
            // make them the same as target. When the current window doesn't satisfy this condition, we need to first
            // shrink the window from left until it becomes valid. Cuz the next right ptr number is either equal or
            // greater than the current one, it won't reduce the increment ops we need.
            while (nums[right] * (right - left + 1) - winSum > k
                    && left < right) {
                // nums[right] * (right - left + 1) is the optimal target sum when making all numbers in the window
                // equal to the "max" number, i.e. the rightmost number.
                // On LeetCode, we need to use (right - left + 1) > (k + winTotal) / maxNum && left < right as the
                // condition of while loop to avoid integer overflow
                winSum -= nums[left];
                left++;
            }
            // Now window is "valid", which means all number(s) in the window can be increment to the target/max number
            // in the window using no more than k increment ops. In other words, the size of the window also means the
            // number of occurrences/frequency of the same number after at most k increments.
            maxWinSize = Math.max(maxWinSize, right - left + 1);
        }
        return maxWinSize;
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
     * Use two pointers (0, end idx) to iterate array and compute the area and keep track of the max area.
     * Move the pointer on the shorter heigh until two ptr cross each other.
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
     * Iterate the nums array from the second number forward to compute the left side product
     * for each number and store at the ans array (ans[0] == 1, ans[i] = ans[i-1] * nums[i-1])
     * Then let rightProd = 1, iterate backward from nums[nums.length - 2] to compute the
     * right product of each number and multiply its left product in ans array.
     * (rightProd *= nums[i+1], ans[i] = ans[i] * rightProd)
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

    /**
     * Subarray Product Less Than K
     * Given an array of integers nums and an integer k, return the number of contiguous
     * subarrays where the product of all the elements in the subarray is strictly less
     * than k.
     * <p>
     * Input: nums = [10,5,2,6], k = 100
     * Output: 8
     * Explanation: The 8 subarrays that have product less than 100 are:
     * [10], [5], [2], [6], [10, 5], [5, 2], [2, 6], [5, 2, 6]
     * Note that [10, 5, 2] is not included as the product of 100 is not strictly less
     * than k.
     * <p>
     * Input: nums = [1,2,3], k = 0
     * Output: 0
     * <p>
     * https://leetcode.com/problems/subarray-product-less-than-k/description/
     */
    @Test
    void testNumSubarrayProductLessThanK() {
        assertThat(numSubarrayProductLessThanK(new int[]{10, 5, 2, 6}, 100)).isEqualTo(8);
        assertThat(numSubarrayProductLessThanK(new int[]{1, 2, 3}, 0)).isEqualTo(0);
    }

    /**
     * Iterate the nums and use the sliding window from (left:0, right:0) to track the
     * running product in the window. If the product in the current window >= k, shrink
     * the window and drop the left number from the product until it is < k or
     * left == right. Then we add the total number of sub-arrays in this window,
     * (right-left+1) to the result
     * <p>
     * When computing the number of sub-arrays from a given range [j,i] inclusive.
     * We use i - j + 1. This gives us the total number of sub-arrays that ends at i
     * and starts at j or later (j = 0,1,2,...i).
     * <p>
     * For example: for [1,2,3,4]
     * [j,i] = [0,0] ---> [1], 1 sub-array
     * [j,i] = [0,1] ---> [2], [1,2], 2 sub-array
     * [j,i] = [0,2] ---> [3], [2,3], [1,2,3], 3 sub-array
     * [j,i] = [0,3] ---> [4], [3,4], [2,3,4], [1,2,3,4], 4 sub-array
     * ...
     * Cuz we keep accumulating all such total of sub-array when expanding and moving
     * the sliding window, therefore we count all possible sub-arrays
     * <p>
     * Time complexity : O(n)
     * Space complexity : O(1)
     */
    int numSubarrayProductLessThanK(int[] nums, int k) {
        int product = 1;
        int result = 0;
        for (int left = 0, right = 0; right < nums.length; right++) {
            product *= nums[right];
            while (product >= k && left < right) {
                // Keep shrinking the window and drop the left number from the product when the product
                // in the window >= k. However, end the loop when left == right
                product /= nums[left++];
            }
            if (product < k) {
                // It is possible loop ends due to left == right and the product is still invalid
                result += right - left + 1;
            }
        }
        return result;
    }

    /**
     * Count Subarrays With Score Less Than K
     * The score of an array is defined as the product of its sum and its length.
     * <p>
     * For example, the score of [1, 2, 3, 4, 5] is (1 + 2 + 3 + 4 + 5) * 5 = 75.
     * Given a positive integer array nums and an integer k, return the number of
     * non-empty subarrays of nums whose score is strictly less than k.
     * <p>
     * A subarray is a contiguous sequence of elements within an array.
     * <p>
     * Input: nums = [2,1,4,3,5], k = 10
     * Output: 6
     * Explanation:
     * The 6 subarrays having scores less than 10 are:
     * - [2] with score 2 * 1 = 2.
     * - [1] with score 1 * 1 = 1.
     * - [4] with score 4 * 1 = 4.
     * - [3] with score 3 * 1 = 3.
     * - [5] with score 5 * 1 = 5.
     * - [2,1] with score (2 + 1) * 2 = 6.
     * Note that subarrays such as [1,4] and [4,3,5] are not considered because their
     * scores are 10 and 36 respectively, while we need scores strictly less than 10.
     * <p>
     * Input: nums = [1,1,1], k = 5
     * Output: 5
     * Explanation:
     * Every subarray except [1,1,1] has a score less than 5.
     * [1,1,1] has a score (1 + 1 + 1) * 3 = 9, which is greater than 5.
     * Thus, there are 5 subarrays having scores less than 5.
     * <p>
     * https://leetcode.com/problems/count-subarrays-with-score-less-than-k/description/
     */
    @Test
    void testCountSubarrays() {
        assertThat(countSubarrays(new int[]{2, 1, 4, 3, 5}, 10)).isEqualTo(6);
        assertThat(countSubarrays(new int[]{1, 1, 1}, 5)).isEqualTo(5);
    }

    /**
     * Iterate the nums and use the sliding window (left:0, right:0) to track the
     * running sum in the window. If the score in the current window >= k, shrink the
     * window and drop the left number from the sum until the score < k. Then we add the
     * total number of sub-arrays in this window, (right-left+1), to the result.
     * <p>
     * When computing the number of sub-arrays from a given range [j,i] inclusive.
     * We use i - j + 1. This gives us the total number of sub-arrays that ends at i
     * and starts at j or later (j = 0,1,2,...i).
     * <p>
     * For example: for [1,2,3,4]
     * [j,i] = [0,0] ---> [1], 1 sub-array
     * [j,i] = [0,1] ---> [2], [1,2], 2 sub-array
     * [j,i] = [0,2] ---> [3], [2,3], [1,2,3], 3 sub-array
     * [j,i] = [0,3] ---> [4], [3,4], [2,3,4], [1,2,3,4], 4 sub-array
     * ...
     * Cuz we keep accumulating all such total of sub-array when expanding and moving
     * the sliding window, therefore we count all possible sub-arrays
     * <p>
     * Time complexity : O(n)
     * Space complexity : O(1)
     */
    long countSubarrays(int[] nums, long k) {
        long sum = 0, result = 0;
        for (int right = 0, left = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum * (right - left + 1) >= k)
                // sub-array score is too big, so shrink the window and drop the left-most number from sum
                // K >= 1, so when left = right + 1, score will be zero then the loop ends. The following number of
                // sub-array will also be 0
                sum -= nums[left++];
            // Now adds the total number of sub-arrays that ends at right and starts at left or later.
            // Check the comment at method javadoc for explanation
            result += right - left + 1;
        }
        return result;
    }

}