package playground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ArrayQuestion {

    /**
     * Remove Duplicates from Sorted Array
     */
    @Test
    void removeDuplicateFromArray() {
        int[] intArray = new int[]{1, 1, 2, 3, 3, 3, 4, 5, 5, 6};
        System.out.println(removeDuplicates(intArray));
    }

//    public int removeDuplicates(int[] nums) {
//        if (nums.length == 0) return 0;
//        int i = 0;
//        for (int j = 1; j < nums.length; j++) {
//            if (nums[j] != nums[i]) {
//                i++;
//                nums[i] = nums[j];
//            }
//        }
//        return i + 1;
//    }

//    public int removeDuplicates(int[] nums) {
//        int i = 0;
//        for (int n : nums)
//            if (i == 0 || n > nums[i-1])
//                nums[i++] = n;
//        return i;
//    }

//    public int removeDuplicates(int[] A) {
//        if (A.length==0) return 0;
//        int j=0;
//        for (int i=0; i<A.length; i++)
//            if (A[i]!=A[j])
//                A[++j]=A[i];
//        return ++j;
//    }

    int removeDuplicates(int[] nums) {
        // Return, if array is empty
        // or contains a single element
        int n = nums.length;
        if (n == 0 || n == 1)
            return n;

        int[] temp = new int[n];

        // Start traversing elements
        int j = 0;
        for (int i = 0; i < n - 1; i++)
            // If current element is not equal
            // to next element then store that
            // current element
            if (nums[i] != nums[i + 1])
                temp[j++] = nums[i];

        // Store the last element as whether
        // it is unique or repeated, it hasn't
        // stored previously
        temp[j++] = nums[n - 1];

        // Modify original array
        for (int i = 0; i < j; i++)
            nums[i] = temp[i];

        return j;
    }


    /**
     * Best Time to Buy and Sell Stock II
     */
    @Test
    void maxProfitToBuySellStock() {
        int[] intArray = new int[]{7, 1, 5, 3, 6, 4};
//        int[] intArray = new int[]{1, 7, 2, 3, 6, 7, 6, 7};
        System.out.println(maxProfitTwo(intArray));
    }

    int maxProfit(int[] prices) { // Better
        int maxprofit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1])
                maxprofit += prices[i] - prices[i - 1];
        }
        return maxprofit;
    }

    int maxProfitTwo(int[] prices) {
        int i = 0;
        int valley = prices[0];
        int peak = prices[0];
        int maxprofit = 0;
        while (i < prices.length - 1) {
            while (i < prices.length - 1 && prices[i] >= prices[i + 1])
                i++;
            valley = prices[i];
            while (i < prices.length - 1 && prices[i] <= prices[i + 1])
                i++;
            peak = prices[i];
            maxprofit += peak - valley;
        }
        return maxprofit;
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
     * https://leetcode.com/problems/contains-duplicate/solution/
     */
    @Test
    void containsDuplicate() {
        int[] intArray = new int[]{1, 1, 3, 4, 5, 6};
        Assertions.assertTrue(checkDuplicate(intArray));
    }

    boolean checkDuplicate(int[] nums) { // O(n Log n), Space: O(1)
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                return true;
            }
        }
        return false;
    }

    boolean checkDuplicateSet(int[] nums) { // O(n) , Space: O(n)
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
     * https://leetcode.com/problems/single-number/solution/
     */
    @Test
    void singleNumber() {
        int[] intArray = new int[]{1, 1, 3, 4, 4};
        //Assertions.assertEquals(3, checkSingleNumber(intArray));
        Assertions.assertArrayEquals(new int[]{6, 7, 1, 2, 3, 4, 5}, intArray);
    }

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
    void findIntersect() {// TODO: Need to revisit the non built-in solution
        int[] intArray1 = new int[]{1, 2, 2, 1};
        int[] intArray2 = new int[]{2, 2};

        Assertions.assertArrayEquals(new int[]{2}, intersectUnique(intArray1, intArray2));

    }

    //Time Complexity: O(n + m). Space Complexity: O(n+m)
    int[] intersectUnique(int[] nums1, int[] nums2) {
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

//        Assertions.assertArrayEquals(new int[]{4, 9}, intersect(intArray1, intArray2));
        Assertions.assertArrayEquals(new int[]{9, 4}, intersectWithMap(intArray1, intArray2));
    }

    //Time Complexity: O(nlog n + mlog m). Space Complexity: O(n+m)
    int[] intersect(int[] nums1, int[] nums2) {
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

    //Time Complexity: O(n + m). Space Complexity: O(min(n,m))
    int[] intersectWithMap(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length)
            intersectWithMap(nums2, nums1);
        Map<Integer, Integer> valToCount = new HashMap<>();
        for (int i : nums1) {
            valToCount.put(i, valToCount.getOrDefault(i, 0) + 1);
        }
        int k = 0;
        for (int i : nums2) {
            int count = valToCount.getOrDefault(i, 0);
            if (count > 0) {
                nums1[k++] = i;
                valToCount.put(i, --count);
            }
        }
        return Arrays.copyOfRange(nums1, 0, k);
    }

    /**
     * Plus One
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
            if (digits[i] == 9)
                digits[i] = 0;
            else {
                digits[i]++;
                return digits;
            }
        }
        /*
        We're here because all the digits were equal to nine. Now they have all been set to zero.
        We then append the digit 1 in front of the other digits and return the result.
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

    //Time Complexity: O(n). Space Complexity: O(1)
    void moveZeroes(int[] nums) {
        int lastNonZeroFoundAt = 0;
        // If the current element is not 0, then we need to
        // append it just in front of last non 0 element we found.
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[lastNonZeroFoundAt++] = nums[i];
            }
        }
        // After we have finished processing new elements,
        // all the non-zero elements are already at beginning of array.
        // We just need to fill remaining array with 0's.
        for (int i = lastNonZeroFoundAt; i < nums.length; i++) {
            nums[i] = 0;
        }
    }

    /**
     * Two Sum
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

    //Time Complexity: O(n). Space Complexity: O(n)
    int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> valToIdx = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            var complement = target - nums[i];
            if (valToIdx.containsKey(complement)) {
                return new int[]{valToIdx.get(complement), i};
            }
            valToIdx.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }

}
