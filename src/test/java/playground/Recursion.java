package playground;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO: Recursion Note:
 *  1. Basic knowledge
 *      Permutation: n! (n factorial) total solution space
 *      Combination: C(n, k) = n! / k!(n-l)! ===> "C n取k, n choose k"
 *      Subset: 2^n  ===> because binomial theorem(二項式定理)
 *           (1+1)^n = C(n,0) + C(n,1) + C(n,2) + ... + C(n,n) ==> 2^n  http://www.math.nsysu.edu.tw/eprob/PerComb/Binomial/
 *  2. When we want to update or accumulate the result of a primitive type such as integer count in the recursive function
 *     at the base case mostly. Besides making the recursive function to return the piece of result and collect and update
 *     it at the caller side, a simpler way is to use AtomicInteger type and pass it around so it can be updated at base case.
 *     result.set(result.intValue() + 2));
 *     or result.getAndIncrement();
 *     or result.getAndAdd(10)
 *   3.Backtracking template
 *     def backtrack(candidate):
 *       if find_solution(candidate):
 *          output(candidate)
 *          return
 *     # iterate all possible candidates.
 *     for next_candidate in list_of_candidates:
 *         if is_valid(next_candidate):
 *             # try this partial candidate solution
 *             place(next_candidate)
 *             # given the candidate, explore further.
 *             backtrack(next_candidate)
 *             # backtrack
 *             remove(next_candidate)
 */
@Slf4j
public class Recursion {
    /**
     * Combinations
     * Given two integers n and k, return all possible combinations of k numbers out
     * of the range [1, n].
     * <p>
     * Input: n = 4, k = 2
     * Output: [[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
     * Explanation: There are 4 choose 2 = 6 total combinations.
     * Note that combinations are unordered, i.e., [1,2] and [2,1] are considered to be the same combination.
     * <p>
     * Input: n = 1, k = 1
     * Output: [[1]]
     * Explanation: There is 1 choose 1 = 1 total combination.
     * <p>
     * https://leetcode.com/problems/combinations/description/
     */
    @Test
    void testCombinations() {
        List<List<Integer>> result = combine(4, 2);
        Assertions.assertThat(combine(4, 2)).containsOnly(List.of(1, 2), List.of(1, 3), List.of(1, 4), List.of(2, 3), List.of(2, 4), List.of(3, 4));
        log.info("{}", result); // [[1, 2], [1, 3], [1, 4], [2, 3], [2, 4], [3, 4]]
    }


    /**
     * Recursively generating one number in the combination each step. We maintain a startNum as the number
     * greater than the current one and pass it to the next call to find the next digit and backtrack before
     * return.
     * <p>
     * Observation:
     * Think of the solution space as decision tree. Each node is a potential element added to the permutation.
     * Basically we traverse the tree starting from each item in nums and explore all child node(DFS) until
     * reaching the leaf node. For each num that isn't already in tmp, we add it to tmp and then make a recursive
     * call passing tmp. Modifying tmp and making a recursive call is equivalent to "traversing" to a child
     * node in the tree.
     * <p>
     * Algo:
     * 1. Think of each call to the recursive function as being a node in the tree. In each call, we need to
     * iterate over the numbers GREATER than the value of the current node. We can pass an argument start
     * representing the first number we should start iterating from. This is cuz we don't want duplicate,
     * i.e. (1,2) and (2,1) are the same
     * <p>
     * 2. When we return from a function call, it's equivalent to moving back up the tree (exactly like in a DFS).
     * When we moved from a parent to a child, we added an element to tmp. When we move from a child back to
     * its parent, we need to remove the element we added from tmp. This is the "backtracking" step.
     * <p>
     * 3. When the tmp size is equal to maxDigit, k, the recursion terminates. This is our base case.
     * <p>
     * *There is an optimization that cut out the path to the nodes which can't lead to any solution
     * Check out the source code of method findCombinationsOpt
     * <p>
     * Time complexity: O(k * C(n,k))
     * The number of combinations of length k from a set of n elements is C(n,k)
     * Each path to a leaf has k nodes. Therefore, the upper bound is the product of this two
     * <p>
     * Space complexity: O(k)
     * We don't count the answer as part of the space complexity. The extra space we use here is for tmp
     * and the recursion call stack. The depth of the call stack is equal to the length of tmp, which is limited to k.
     */
    List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        findCombinations(1, new ArrayList<>(), result, k, n);
        return result;
    }

    void findCombinations(int start, List<Integer> tmp, List<List<Integer>> ans, int maxDigit, int maxNum) {
        if (tmp.size() == maxDigit) {
            ans.add(new ArrayList<>(tmp));
            return;
        }
        for (int i = start; i <= maxNum; i++) {
            tmp.add(i);
            // Increment the start when doing the recursive call, so we add the number greater than current node to the tmp
            // at the child. This also makes us always look forward, so we never generate [[1,2],[2,1]] in the result
            findCombinations(i + 1, tmp, ans, maxDigit, maxNum);
            tmp.remove(tmp.size() - 1);
        }
    }

    void findCombinationsOpt(int start, List<Integer> tmp, List<List<Integer>> ans, int maxDigit, int maxNum) {
        if (tmp.size() == maxDigit) {
            ans.add(new ArrayList<>(tmp));
            return;
        }

        // Here is the optimization part to cut out the path to the nodes which can't lead to any solution
        int need = maxDigit - tmp.size(); //the number of elements we still need to add.
        int remain = maxNum - start + 1; // the size of the range of the remaining numbers we are considering
        int available = remain - need; // the count of numbers available to us as children

        // We should only consider children in the range [start, start + available], If we moved to a child outside
        // of this range, like start + available + 1, then we will run out of numbers to use before reaching a length of k
        for (int num = start; num <= start + available; num++) {
            tmp.add(num);
            findCombinationsOpt(num + 1, tmp, ans, maxDigit, maxNum);
            tmp.remove(tmp.size() - 1);
        }
    }


    /**
     * Combination Sum
     * Given an array of distinct integers candidates and a target integer target, return a list of all
     * unique combinations of candidates where the chosen numbers sum to target. You may return the
     * combinations in any order.
     * <p>
     * The same number may be chosen from candidates an unlimited number of times.
     * Two combinations are unique if the frequency of at least one of the chosen numbers is different.
     * <p>
     * Input: candidates = [2,3,6,7], target = 7
     * Output: [[2,2,3],[7]]
     * <p>
     * Input: candidates = [2,3,5], target = 8
     * Output: [[2,2,2,2],[2,3,3],[3,5]]
     * <p>
     * Input: candidates = [2], target = 1
     * Output: []
     * <p>
     * https://leetcode.com/problems/combination-sum/
     */
    @Test
    void testCombinationSum() {
        Assertions.assertThat(combinationSum(new int[]{2, 3, 6, 7}, 7))
                .containsOnly(List.of(2, 2, 3), List.of(7));
    }

    List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> results = new ArrayList<>();
        findCombinationSum(target, 0, new ArrayList<>(), results, candidates);
        return results;
    }

    /**
     * Recursively taking one number from candidates list at each step. We maintain a remain var to track how many
     * we still need to reach the target sum and pass it along w/ a start index and the updated remain to the next call
     * and backtrack before return
     * <p>
     * Base case:
     * 1. The remaining sum to fulfill is zero
     * 2. The remaining sum to fulfill is negative
     * <p>
     * Very similar to the combination problem besides the following key points
     * 1. We need a "remain" var passed in every recursive call to keep track of how many is left
     * after adding the number from the candidate list. This is also used for the base case(remain == 0)
     * 2. Also need to handle the case when remain is less than zero.
     * 3. We still need a start index to prevent us from visiting the node(number) already visited(The number/node
     * is considered only once at the same level at the tree). However, we do NOT increment the start index when
     * making recursive call, cuz every number can be reused as many as we want at the next child level.
     * <p>
     * Time Complexity: O(N^T/M + 1), where N be the number of candidates, T be the target value, and M be the minimal value
     * among the candidates.
     * The loose upper bound is subject to the number of nodes
     * 1. The fan-out of each node would be bounded to N, i.e. the total number of candidates.
     * 2. The maximal depth of the tree, would be T/M, where we keep on adding the smallest element to the combination.
     * 3. As we know, the maximal number of nodes in N-ary tree of T\M height would be N^T/M + 1.
     * <p>
     * Space Complexity: O(T/M), where T be the target value, and M be the minimal value
     * The number of recursive calls can pile up to T/M, where we keep on adding the smallest element to the combination.
     */
    void findCombinationSum(int remain, int start, List<Integer> tmp, List<List<Integer>> results, int[] candidates) {
        // base case: when remain is zero, we found a valid combination
        if (remain == 0) {
            results.add(new ArrayList<>(tmp));
            return;
        } else if (remain < 0)
            return;
        for (int i = start; i < candidates.length; i++) {
            tmp.add(candidates[i]);
            findCombinationSum(remain - candidates[i], i, tmp, results, candidates);
            tmp.remove(tmp.size() - 1);
        }
    }

    /**
     * Combination Sum II
     * Given a collection of candidate numbers (candidates) and a target number (target),
     * find all unique combinations in candidates where the candidate numbers sum to target.
     * <p>
     * Each number in candidates may only be used once in the combination.
     * Note: The solution set must not contain duplicate combinations.
     * <p>
     * Input: candidates = [10,1,2,7,6,1,5], target = 8
     * Output:
     * [
     * [1,1,6],
     * [1,2,5],
     * [1,7],
     * [2,6]
     * ]
     * <p>
     * Input: candidates = [2,5,2,1,2], target = 5
     * Output:
     * [
     * [1,2,2],
     * [5]
     * ]
     * <p>
     * https://leetcode.com/problems/combination-sum-ii/description/
     */
    @Test
    void testCombinationSum2() {
        Assertions.assertThat(combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8))
                .containsOnly(List.of(1, 1, 6), List.of(1, 2, 5), List.of(1, 7), List.of(2, 6));
    }

    /**
     * First build the numToCount map and convert it to a list of pair(num, count). Then recursively taking one number
     * from the list at each step. We maintain a remain var to track how many we still need to reach the target sum.
     * Check if the count available before making recursive call w/ start index and the updated remain and count and
     * undo it before backtrack
     * <p>
     * Base case:
     * 1. The remaining sum to fulfill is zero
     * 2. The remaining sum to fulfill is negative
     * <p>
     * Observation
     * There are two differences between this problem and the Combination Sum:
     * 1. In this problem, each number in the input is NOT unique. The implication of this difference is that
     * we need some mechanism to avoid generating duplicate combinations.
     * <p>
     * 2. In this problem, each number can be used only once. The implication of this difference is that once
     * a number is chosen as a candidate in the combination, it will not appear again as a candidate later.
     * <p>
     * Algo:
     * 1. To addrress the two items above, we need to first build a number to count map and then convert
     * it to a List of Pair(number, count), so we can control how to iterate it during the recursion using index.
     * Because we have group the same number using count, this solves the duplication issue in the output.
     * <p>
     * 2. Before we do the recursive call for the next level, we need to check if the count is not zero. If so,
     * decrement the count and use the same start index and updated remain to make the call.
     * <p>
     * Time Complexity: O(2^N)
     * In the worst case, our algorithm will exhaust all possible combinations from the input array.
     * Assume that each number is unique. The number of combination for an array of size N would be 2^N,
     * i.e. each number is either included or excluded in a combination.
     * <p>
     * Space Complexity: O(N)
     * Map, current building str and the call stack
     */
    List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Map<Integer, Integer> numToCount = new HashMap<>();
        for (int num : candidates) {
            // Prebuild the map of count by number
            Integer count = numToCount.getOrDefault(num, 0);
            numToCount.put(num, count + 1);
        }
        // Turn map into list of pair so we can iterate by index
        List<SimpleEntry<Integer, Integer>> numCountLists = numToCount.entrySet().stream()
                .map((e) -> new SimpleEntry<>(e.getKey(), e.getValue())).collect(Collectors.toList());
        findCombinationSum(target, 0, numCountLists, new ArrayList<>(), result);
        return result;
    }

    void findCombinationSum(int remain, int start, List<SimpleEntry<Integer, Integer>> numCountLists, List<Integer> tmp,
                            List<List<Integer>> results) {
        // Base case
        if (remain <= 0) {
            if (remain == 0)
                results.add(new ArrayList<>(tmp));
            return;
        }
        for (int i = start; i < numCountLists.size(); i++) {
            SimpleEntry<Integer, Integer> numCount = numCountLists.get(i);
            if (numCount.getValue() == 0)
                // If number count is exhausted, move to the next one
                continue;
            tmp.add(numCount.getKey());
            numCount.setValue(numCount.getValue() - 1);
            // Explore to the next number level. Keep the same start idx, so we consider the same number again
            findCombinationSum(remain - numCount.getKey(), i, numCountLists, tmp, results);
            // undo the changes for backtracking
            tmp.remove(tmp.size() - 1);
            numCount.setValue(numCount.getValue() + 1);
        }
    }

    /**
     * Permutations
     * Given an array nums of distinct integers, return all the possible permutations.
     * You can return the answer in any order.
     * <p>
     * Input: nums = [1,2,3]
     * Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
     * <p>
     * Input: nums = [0,1]
     * Output: [[0,1],[1,0]]
     * https://leetcode.com/problems/permutations/
     */
    @Test
    void testPermutations() {
        int[] input = {1, 2, 3};
        Assertions.assertThat(permute(input)).containsOnly(List.of(1, 2, 3), List.of(1, 3, 2), List.of(2, 1, 3), List.of(2, 3, 1), List.of(3, 1, 2), List.of(3, 2, 1));
    }

    /**
     * Recursively pick one number at each step. We iterate each number and if it is not in the current building
     * list, make recursive call for the next level and backtrack before return
     * <p>
     * Observation:
     * The difference between this problem and Combination porblem is here we want permutation so at each resursion level
     * , we need to iterate every number in the list(every number is still a candidate) unless it is already in the current
     * building list(Can't have duplicate number in the permutation)
     * <p>
     * Think of the solution space as decision tree. Each node is a potential element added to the permutation.
     * Basically we traverse the tree starting from each item in nums and explore all child node(DFS) until
     * reaching the leaf node. For each num that isn't already in tmp, we add it to tmp and then make a recursive
     * call passing tmp. Modifying tmp and making a recursive call is equivalent to "traversing" to a child node in the tree.
     * When we return from a function call, it's equivalent to moving back up the tree (exactly like in a DFS).
     * When we moved from a parent to a child, we added an element to tmp.
     * When we move from a child back to its parent, we need to remove the element we added from tmp.
     * This is the "backtracking" step
     * <p>
     * Time Complexity: O(n⋅n!)
     * Given a set of length n, the number of permutations is n!.
     * For each of the n! permutations, we need O(n) work to copy curr into the answer. This gives us O(n⋅n!)work.
     * <p>
     * Space Complexity: O(n)
     * The extra space we use here is for tmp and the recursion call stack.
     * The depth of the call stack is equal to the length of tmp, which is limited to n.
     */
    List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        findPermutations(new ArrayList<>(), ans, nums);
        return ans;
    }

    void findPermutations(List<Integer> tmp, List<List<Integer>> ans, int[] nums) {
        // base case: reach the leaf node and got a permutation
        if (tmp.size() == nums.length) {
            ans.add(new ArrayList<>(tmp)); // Need new ArrayList obj cuz tmp is updated constantly
            return;
        }
        for (int num : nums) {
            // We want permutation, says [[1,2],[2,1]], so we always iterate every item in nums (This is the key difference from finding combinations)
            // We don't want duplicate. This is needed when backtracking from the child node, so we don't visit the same child again
            if (!tmp.contains(num)) {
                tmp.add(num);
                findPermutations(tmp, ans, nums);
                tmp.remove(tmp.size() - 1);
            }
        }
    }

    /**
     * Permutations II
     * Given a collection of numbers, nums, that might contain duplicates, return all possible unique permutations in any order.
     * <p>
     * Input: nums = [1,1,2]
     * Output:
     * [[1,1,2], [1,2,1], [2,1,1]]
     * <p>
     * Input: nums = [1,2,3]
     * Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
     * <p>
     * https://leetcode.com/problems/permutations-ii/editorial/
     */
    @Test
    void testPermutationsTwo() {
        int[] input = {1, 1, 2};
        Assertions.assertThat(permuteUnique(input)).containsOnly(List.of(1, 1, 2), List.of(1, 2, 1), List.of(2, 1, 1));
    }

    /**
     * First build the numToCount map. Use a recursive method to iterate the map and if its count is greater than 0,
     * add it to the current permutation set and decrement the count and recurse to the next level, then undo the
     * changes before backtrack
     * The solution is very similar to the permutation question. The key difference are
     * 1. We need to pre-build a map(number -> count) cuz the number can be duplicate and when we decide to
     * pick the next number, we consider the unique number and whether it is still available to use.
     * 2. Base case is the same as permutation problem.
     * 3. We iterate the map for each entry that count is not 0,
     * -- Add the number to the tmp
     * -- Decrement its count by 1
     * -- Call same method recursively
     * -- Put  the original count to the original entry
     * -- Remove the last number from tmp
     * Time Complexity: O(N⋅N!)
     * It takes N steps to generate a single permutation. Since there are in total N! possible permutations,
     * at most it would take us N⋅N! steps to generate all permutations.
     * <p>
     * Space Complexity: O(n)
     * The extra space we use here is for the map, tmp and the recursion call stack.
     * The depth of the call stack is equal to the length of tmp, which is limited to n.
     */
    List<List<Integer>> permuteUnique(int[] nums) {
        Map<Integer, Integer> numToCount = new HashMap<>();
        for (int num : nums) {
            Integer count = numToCount.getOrDefault(num, 0);
            numToCount.put(num, count + 1);
        }
        List<List<Integer>> results = new ArrayList<>();
        findPermutations(nums.length, new ArrayList<>(), results, numToCount);
        return results;
    }

    void findPermutations(int targetLen, List<Integer> tmp, List<List<Integer>> results, Map<Integer, Integer> numToCount) {
        // Base case
        if (tmp.size() == targetLen) {
            results.add(new ArrayList<>(tmp));
            return;
        }

        numToCount.forEach((k, v) -> {
            if (v != 0) {
                tmp.add(k);
                numToCount.put(k, v - 1);
                findPermutations(targetLen, tmp, results, numToCount);
                // revert the change we made
                numToCount.put(k, v);
                tmp.remove(tmp.size() - 1);
            }
        });
    }


    /**
     * Given an integer array nums of unique elements, return all possible subsets (the power set).
     * The solution set must not contain duplicate subsets. Return the solution in any order.
     * https://leetcode.com/problems/subsets/
     */
    @Test
    void testSubsets() {
        int[] input = {1, 2, 3};
        Assertions.assertThat(subsets(input)).containsOnly(List.of(), List.of(1), List.of(2), List.of(1, 2), List.of(1, 2, 3), List.of(1, 3), List.of(2, 3), List.of(3));
    }


    /**
     * Use a recursive method to always add the current building list to the subsets at each call and then
     * iterate each number from the given start index(begin from 0) and recurse to next level w/ start index + 1
     * and undo the changes before backtrack.
     * We have a recursive method findSubsets(int start, ArrayList<Integer> currList, int[] nums)
     * Calls the method w/ start equal to 0
     * - Add the currList to the result subsets
     * - Iterate the nums from the start index
     * -- Add integer nums[i] into the current combination currList.
     * -- Perform DFS by calling the findSubsets recursively to add more integers into the combination : findSubsets(i + 1, currList, nums).
     * -- Backtrack by removing the last integer from currList
     * Given the num:{1,2,3}, the traversal will be in DFS over the decision tree below
     * _____________{}
     * _________/___|____\
     * ______1______2_____3
     * ___/___\______\
     * _(1,2)__(1,3)__(2,3)
     * __|
     * _(1,2,3)
     * Time Complexity: O(n⋅2^n).==> There are total 2^n combinations(for each number we either include or not) and
     * we iterate all of n numbers to generate combination
     * The height of tree is also n
     * Space Complexity: O(n)
     */
    List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> subsets = new ArrayList<>();
        findSubsets(0, new ArrayList<>(), nums, subsets);
        return subsets;
    }

    void findSubsets(int start, ArrayList<Integer> currList, int[] nums, List<List<Integer>> subsets) {
        subsets.add(new ArrayList<>(currList));
        for (int i = start; i < nums.length; i++) {
            // The for loop serves two purposes
            // 1. At the top level, each iteration finds all combination starting with num[i] w/o duplicate(Cuz the start
            //    index keep incrementing, we won't look back the number already visited)
            // 2. Inside the recursive call, the start index forwards everytime, so we keep adding new number and generates new subset.
            //    It is also used as the terminate condition for the recursive call(base case) to signal we reach the leaf node and exhaust combination
            currList.add(nums[i]);
            // Calling findSubsets recursively is basically perform DFS from the subset(currList). We explore new subset in the
            // next level(currList length + 1) after adding the new number by calling the findSubsets method w/
            // INCREMENTING the start index by 1
            // (Forwarding the start index means we move toward the leaf node in the decision tree to generate new combination w/ next number)
            findSubsets(i + 1, currList, nums, subsets);
            // When recursive call returns, it means we explored all new subsets from the subset at last level(length-1), so we do backtrack to move
            // back to the upper level by undoing/removing the subset we just added
            currList.remove(currList.size() - 1);
        }
    }

    /**
     * Subsets II
     * Given an integer array nums that may contain duplicates, return all possible subsets(the power set).
     * The solution set must NOT contain duplicate subsets. Return the solution in any order.
     * <p>
     * Input: nums = [1,2,2]
     * Output: [[],[1],[1,2],[1,2,2],[2],[2,2]]
     * <p>
     * Input: nums = [0]
     * Output: [[],[0]]
     * <p>
     * https://leetcode.com/problems/subsets-ii/description/
     */
    @Test
    void testSubsetsTwo() {
        int[] input = {1, 2, 2};
        Assertions.assertThat(subsetsWithDup(input)).containsOnly(List.of(), List.of(1), List.of(2), List.of(1, 2), List.of(1, 2, 2), List.of(2, 2));
        input = new int[]{0};
        Assertions.assertThat(subsetsWithDup(input)).containsOnly(List.of(), List.of(0));

    }

    List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> results = new ArrayList<>();
        Arrays.sort(nums); // Sort the nums first so we can detect duplicate number later
        findSubsetsWithDup(0, new ArrayList<>(), results, nums);
        return results;
    }

    /**
     * First sort the nums array. Then use a recursive method to always add the current building list to the
     * subsets at each call and then iterate each number from the given start index(begin from 0) and recurse
     * to next level w/ start index + 1 ONLY if it is not the same number as previous one, and undo the changes
     * before backtrack.
     * <p>
     * Observation:
     * The solution is almost the same as the subset problems besides the following key difference:
     * 1. Cuz the input has duplicate numbers, when we decide to include a number to generate a new subset,
     * we need to know if such subset has already created. Ex, given num = (1,2,2), when we already had (1,2),
     * we shouldn't generate another (1,2) when we do DFS from the node (1), so we need an extra condition check
     * <p>
     * 2. If we can sort the input array first, since at each recursion we are given the current index of nums
     * when at recursion call. This index also represents the current order at the level from the same parent.
     * So we can use it to access the previous element, i.e. sibling node, to know if the same number was
     * processed already. However, when we process the first node at the level, i.e. startIdx == i, we don't
     * need to do this check, cuz there is no previous element to check.
     * For other nodes, we check if it is the same as the last element, if so, skip it.
     * Ex, Given [1, 2, 2], at the child level from the parent node 1(startIdx == 1), when we already had (1, 2),
     * and are at the 2nd iteration(startIdx == 1 && i == 2), we don't want to create another (1,2) so we skip it.
     * <p>
     * Algo:
     * It is similar to the Subset implementation besides
     * 1. We need to sort the nums array before starting the recursion.
     * 2. Need to do the duplication check in the for loop before recursing to the next number.
     * <p>
     * Time Complexity: O(n⋅2^n)
     * There are total 2^n combinations(for each number we either include or not) and we iterate all of n
     * numbers to generate combination
     * The height of tree is also n
     * Space Complexity: O(n)
     */
    void findSubsetsWithDup(int start, List<Integer> tmp, List<List<Integer>> results, int[] nums) {
        results.add(new ArrayList<>(tmp));
        for (int i = start; i < nums.length; i++) {
            if (i != start && nums[i] == nums[i - 1])
                // we do duplication check against last element(sibling node) ONLY when we are NOT at the first child node
                // from the same parent(Child nodes from the same parent has the same start val)
                continue;
            tmp.add(nums[i]);
            findSubsetsWithDup(i + 1, tmp, results, nums);
            tmp.remove(tmp.size() - 1);
        }
    }

    /**
     * Given a string containing digits from 2-9 inclusive, return all possible letter combinations
     * that the number could represent.
     * Return the answer in any order.
     * <p>
     * Input: digits = "23"
     * Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
     * <p>
     * Input: digits = ""
     * Output: []
     * <p>
     * Input: digits = "2"
     * Output: ["a","b","c"]
     * https://leetcode.com/problems/letter-combinations-of-a-phone-number/
     */
    @Test
    void testLetterCombinations() {
        Assertions.assertThat(letterCombinations("23")).containsOnly("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf");
    }

    private final Map<Character, String> digitToLetters = Map.of(
            '2', "abc", '3', "def", '4', "ghi", '5', "jkl",
            '6', "mno", '7', "pqrs", '8', "tuv", '9', "wxyz");

    /**
     * First build the digitToLetters map. Start from the first digit, find the associated str in map and for each char,
     * add it to the current building str and recursively call itself w/ next index to process next digit char then backtrack.
     * <p>
     * The solution is very similar to the Combination problem. The only difference is instead of iterating a sequence,
     * we will have a static lookup map, and iterate the string chars associated with the digit from the input.
     * For the base case, if our current combination of letters is the same length as the input digits, that means we
     * have a complete combination.
     * Otherwise, get all the letters that correspond with the current digit we are looking at, map[index].
     * Loop through these letters. For each letter, add the letter to our current tempStr, and call backtrack again, but
     * move on to the next digit by incrementing index by 1. Then remove the letter from tempStr for the backtracking.
     * <p>
     * Time Complexity: O(4^n n)
     * -- n is the length of digits. Note that 4 in this expression is referring to the maximum value length in the
     * hash map, and not to the length of the input.
     * -- For each combination, it costs up to n to build the combination.
     * <p>
     * Space Complexity: O(n), where n is the length of digits
     */
    List<String> letterCombinations(String digits) {
        if (digits.length() == 0)
            return new ArrayList<>();
        List<String> combinations = new ArrayList<>();
        findLetterCombinations(0, new StringBuilder(), combinations, digits);
        return combinations;
    }

    void findLetterCombinations(int idx, StringBuilder tempStr, List<String> combinations, String digits) {
        // base case: If the tempStr has the same length as digits, we have a complete combination
        if (tempStr.length() == digits.length()) {
            combinations.add(tempStr.toString());
            return;
        }
        // Get the letters that the current digit maps to, and loop through them
        String letters = this.digitToLetters.get(digits.charAt(idx));
        for (char c : letters.toCharArray()) {
            tempStr.append(c);
            // Move on to the next digit and explore all possible chars
            findLetterCombinations(idx + 1, tempStr, combinations, digits);
            // Backtrack by removing the letter before moving onto the next
            tempStr.deleteCharAt(tempStr.length() - 1);
        }
    }

    /**
     * Generate Parentheses
     * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
     * <p>
     * Input: n = 3
     * Output: [""((()))"",""(()())"",""(())()"",""()(())"",""()()()""]
     * <p>
     * Input: n = 1
     * Output: [""()""]"
     * https://leetcode.com/problems/generate-parentheses/description/
     */
    @Test
    void testGenerateParenthesis() {
        Assertions.assertThat(generateParenthesis(1)).contains("()");
        Assertions.assertThat(generateParenthesis(3)).contains("((()))", "(()())", "(())()", "()(())", "()()()");
    }

    /**
     * Recursively building each char of the result string and backtrack to explore all combination. Considering "("
     * first, then ")" only when there is enough "("  to pair with in the string during the recursion.
     * <p>
     * Observation:
     * 1. The problem asks for all combination of results and involves the decision of using which parenthesis.
     * Thus satisfies the characteristics of recursive backtracking algo.
     * <p>
     * 2. When deciding the parenthesis, there are two general rules
     * - Left parenthesis should be considered first cuz right parenthesis can't be added if no paired left parenthesis
     * - Right parenthesis should be considered ONLY when there are enough left one in the current building string
     * <p>
     * 3. We need two vars, left_count and right_count to record the number of left and right parentheses in the
     * currently building string, so we will know if it is ok to add right parenthesis.
     * <p>
     * Algo:
     * The function adds more parentheses to cur_string only when certain conditions are met:
     * 1. If left_count < n, it suggests that a left parenthesis can still be added, so we add one left parenthesis to
     * cur_string, creating a new string new_string = cur_string + (, and then call backtracking(new_string, left_count + 1, right_count).
     * <p>
     * 2. If left_count > right_count, it suggests that a right parenthesis can be added to match a previous unmatched
     * left parenthesis, so we add one right parenthesis to cur_string, creating a new string new_string = cur_string + ),
     * and then call backtracking(new_string, left_count, right_count + 1).
     * <p>
     * This function ensures that the generated string of length 2*n is valid, and adds it directly to the answer.
     * Time Complexity: O(4^N / square root N).
     * Space Complexity: O(N) Call stack is 2n
     */
    List<String> generateParenthesis(int n) {
        List<String> answer = new ArrayList<>();
        backtrack(answer, new StringBuilder(), 0, 0, n);
        return answer;
    }

    void backtrack(List<String> answer, StringBuilder curStr, int leftCount, int rightCount, int max) {
        // base case
        if (curStr.length() == 2 * max) {
            // Got a valid combination here, add to the answer and return
            answer.add(curStr.toString());
            return;
        }

        if (leftCount < max) { // This limits the total left parenthesis
            // ( can still be added, and we wanna always place the ( first
            curStr.append("(");
            backtrack(answer, curStr, leftCount + 1, rightCount, max);
            // Do the backtracking here, so need to revert the previous decision/ops
            curStr.deleteCharAt(curStr.length() - 1);
        }
        if (leftCount > rightCount) {
            // a right parenthesis can be added to match a previous unmatched left parenthesis
            curStr.append(")");
            backtrack(answer, curStr, leftCount, rightCount + 1, max);
            curStr.deleteCharAt(curStr.length() - 1);
        }
    }

    /**
     * Remove Invalid Parentheses
     * Given a string s that contains parentheses and letters, remove the minimum number of invalid
     * parentheses to make the input string valid.
     * <p>
     * Return a list of unique strings that are valid with the minimum number of removals. You may
     * return the answer in any order.
     * <p>
     * Input: s = "()())()"
     * Output: ["(())()","()()()"]
     * <p>
     * Input: s = "(a)())()"
     * Output: ["(a())()","(a)()()"]
     * <p>
     * Input: s = ")("
     * Output: [""]
     * https://leetcode.com/problems/remove-invalid-parentheses/description/
     */
    @Test
    void testRemoveInvalidParentheses() {
        List<String> result = removeInvalidParentheses("()");
        Assertions.assertThat(result).containsOnly("()");
        result = removeInvalidParentheses("(()");
        Assertions.assertThat(result).containsOnly("()");
        result = removeInvalidParentheses("()())()");
        Assertions.assertThat(result).containsOnly("(())()", "()()()");
        result = removeInvalidParentheses("(a)())()");
        Assertions.assertThat(result).containsOnly("(a())()", "(a)()()");
        result = removeInvalidParentheses(")(");
        Assertions.assertThat(result).containsOnly("");
    }

    private final Set<String> result = new HashSet<>();

    /**
     * Consider each char in the string as a node in a tree. Perform DFS traversal and decide if we should include
     * a parenthesis node during the traversal, and check if we get the valid answer at leaf node and backtrack for
     * other decision branches.
     * <p>
     * Observation:
     * 1. The problem contains some characteristics which can lead us to use backtracking algorithm.
     * - It asks to find all possible strings w/ valid parentheses, whcih implies we may need to explore every
     * combination by removing certain parentheses
     * - We can generate an answer after iterating the whole string, which means the answer can only be determined at
     * the leaf node if we consider each character is a node.
     * - When iterating the string, we need to decide whether we want to include a left/right parenthesis, and the
     * decision may lead to a valid or invalid answer in the end.
     * <p>
     * 2. Given the first observation, we can consider each char in the string a node in a tree. We start the traversal
     * from the first char and when we encounter a left/right parenthesis, we need to make decision whether we should
     * include it in the final answer or skip it. When we decide to not include a node(left/right parenthesis), this
     * creates another branch, i.e. include decision, and we will need to explore this path after we finish the
     * current path. In other words, we first keep exploring in DFS way until reaching the leaf node, i.e. last char,
     * and we check if we get a valid answer. Then we backtrack to the last branching node and explore the other path,
     * and so on.
     * <p>
     * Algo:
     * 1. It requires to remove the minimum number of invalid parentheses. So first we compute the difference of the
     * total count of left and right parenthesis respectively to derive the number of left and right parenthesis we
     * need to remove. This will be the numbers of parentheses we must remove in each valid answer
     * <p>
     * 2. We will have a recursive method processing one char in the string at each call starting from the first char.
     * - The base case is when we are at the last char, we check if there is no more left/right parentheses need to
     * remove. If so, we got a valid answer.
     * - When the current char is parenthesis, and the corresponding remaining counts is not zero, we first decide
     * to not include it(skip) and proceed to the next char by recursively calling the same method w/ index+1 and
     * decrement the corresponding remaining count
     * - Otherwise, we include this parenthesis or other char and continue the current path w/ updated parenthesis
     * count by recursively calling same method.
     * -- When including the right parenthesis, we also need to check if we have enough left parenthesis to pair so far.
     * - Finally, we need to remove the char added to the tempStr before we backtrack to the previous node
     * <p>
     * Time Complexity : O(2^N)
     * since in the worst case we will have only left parentheses in the input and for every parenthesis we will
     * have two options i.e. whether to remove it or consider it.
     * <p>
     * Space Complexity : O(N)
     * We have to go to a maximum recursion depth of N before hitting the base case.
     */
    List<String> removeInvalidParentheses(String s) {
        result.clear();
        int left = 0, right = 0;
        // First, we find out the number of misplaced left and right parentheses.
        for (int i = 0; i < s.length(); i++) {
            // Simply record the left one.
            if (s.charAt(i) == '(') {
                left++;
            } else if (s.charAt(i) == ')') {
                if (left > 0)
                    // Decrement count of left parentheses if still available
                    left--;
                else
                    right++;
            }
        }
        recurse(s, 0, 0, 0, left, right, new StringBuilder());
        return new ArrayList<>(this.result);
    }

    private void recurse(String s, int index,
                         int leftCount, // the number of left parentheses that have been added to the tempStr we are building
                         int rightCount, // the number of right parentheses that have been added to the tempStr we are building
                         int leftRem, // the number of left parentheses that remain to be removed
                         int rightRem, // the number of right parentheses that remain to be removed
                         StringBuilder tempStr) {
        // BASE case: we reached the end of the string, we must check if there is any left or right
        // parentheses that are still not removed yet before adding it to the valid results. It is possible that we have more
        // left parentheses than right one at tempStr at this point, e.g. "(()"
        if (index == s.length()) {
            if (leftRem == 0 && rightRem == 0)
                result.add(tempStr.toString());
            return;
        }
        char character = s.charAt(index);

        // Here are the cases that we have left/right parentheses shall be removed, and we encounter left/right parenthesis now,
        // so we won't include this and move on
        if (character == '(' && leftRem > 0) {
            // It is '(' and we have more left parentheses needed to be removed, so skip and check next char
            recurse(s, index + 1, leftCount, rightCount, leftRem - 1, rightRem, tempStr);
        } else if (character == ')' && rightRem > 0) {
            // It is ')' and we have more right parentheses needed to be removed, so skip and check next char
            recurse(s, index + 1, leftCount, rightCount, leftRem, rightRem - 1, tempStr);
        }

        // When the discard/skip path branch out above and recursive call returns. It will be backtracking here and next
        // we will explore another path by including the previously discarded left/right parenthesis
        tempStr.append(character);

        // Continue to check the next char if the current character is not a parenthesis.
        if (character != '(' && character != ')') {
            recurse(s, index + 1, leftCount, rightCount, leftRem, rightRem, tempStr);
        } else if (character == '(') {
            // This left parenthesis was added to the tempStr, so increment the leftCount and continue to check next char
            recurse(s, index + 1, leftCount + 1, rightCount, leftRem, rightRem, tempStr);
        } else if (rightCount < leftCount) {
            // This right parenthesis was added to the tempStr, However, we continue the current path ONLY if we have more
            // left parenthesis than right at this moment. Even if we don't have any left and right parenthesis shall be
            // removed, adding this right parenthesis to the tempStr before its paired left one will become an issue.
            // For example, we may get a result like ()())(
            recurse(s, index + 1, leftCount, rightCount + 1, leftRem, rightRem, tempStr);
        }
        // Delete the last char before backtracking to the last char node
        tempStr.deleteCharAt(tempStr.length() - 1);
    }


    /**
     * Word Search
     * Given an m x n grid of characters board and a string word, return true if word exists in the grid.
     * The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or
     * vertically neighboring. The same letter cell may not be used more than once.
     * <p>
     * board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
     * Output: true
     * <p>
     * https://leetcode.com/problems/word-search/description/
     */
    @Test
    void testExist() {
        char[][] board = {{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
        Assertions.assertThat(exist(board, "ABCCED")).isTrue();
    }

    int rowNum, colNum;

    /**
     * Iterates each cell in the grid. For each cell, call a function which first check if the current state is valid
     * and has the matching char. If so, mark the cell visited and recursively explore four adjacent cells for the next
     * char. Undo the changes before return and backtracking.
     * <p>
     * Base case: the index of matched char found is equal to the length of target string
     * <p>
     * We would walk around the 2D grid, and at each step, we mark our choice before jumping into the next step.
     * And at the end of each step, we would also revert our mark so that we will have a clean slate to try another
     * direction. In addition, the exploration is done via the DFS strategy, where we go as far as possible before we
     * try the next direction.
     * Key points:
     * 1. The recursive method should have an index var and get increment by 1 when it is called,
     * so we can use it as base case(compare to the length of the word) to determine if we find the whole word
     * 2. We need to check the row and col boundary besides the cell value against the char in the word
     * 3. When doing DFS recursive call, we need to call it for four adjacent cells, which means passing different row
     * and col four times.
     * 4. We need to mark the current visiting cell(setting val to a special char), so it won't be visited again.
     * We also need to revert it back once recursive call returns.
     * <p>
     * Time Complexity:O(N⋅3^L), where N is the number of cells in the board and L is the length of the word to be matched.
     * 1. Besides the very first move, we can have at most 4 directions to move, the choices are reduced to 3. So it is
     * basically 3-ary tree, each of the branches represent a potential exploration in the corresponding direction.
     * Therefore, in the worst case, the total number of invocation would be the number of nodes in a full 3-nary tree,
     * which is about 3^L
     * 2. We iterate through the board for backtracking, i.e. there could be N times invocation for the backtracking
     * function in the worst case.
     * <p>
     * Space Complexity: O(L) where L is the length of the word to be matched.
     * The maximum length of the call stack would be the length of the word.
     * Therefore, the space complexity of the algorithm is O(L).
     */
    boolean exist(char[][] board, String word) {
        this.rowNum = board.length;
        this.colNum = board[0].length;
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                if (searchWord(row, col, 0, board, word))
                    return true;
            }
        }
        return false;
    }

    boolean searchWord(int row, int col, int idx, char[][] board, String word) {
        // base case:
        // idx is used for tracking the progress for searching each char in the word. It is incremented in each recursive call.
        // So if the recursive call reaches here w/ index equal to the word length, it means we successfully found the whole word
        // when traversing the grid
        if (idx == word.length())
            return true;

        // Check boundaries and whether we find a matched char
        if (row < 0 || row == rowNum || col < 0 || col == colNum || board[row][col] != word.charAt(idx))
            return false;

        boolean found = false;
        var orig = board[row][col];
        // mark the cell visited
        board[row][col] = '#';
        // Convenient arrays to move to 4 directions in the for loop below
        int[] rowOffsets = {0, 1, 0, -1};
        int[] colOffsets = {1, 0, -1, 0};

        for (int i = 0; i < 4; i++) {
            // search word in DFS at all four adjacent cells
            found = searchWord(row + rowOffsets[i], col + colOffsets[i], idx + 1, board, word);
            if (found)
                break;
        }
        // revert the changes just made
        board[row][col] = orig;
        return found;
    }

    /**
     * N-Queens
     * The n-queens puzzle is the problem of placing n queens on an n x n chessboard such that no
     * two queens attack each other.
     * <p>
     * Given an integer n, return all distinct solutions to the n-queens puzzle. You may return
     * the answer in any order.
     * <p>
     * Each solution contains a distinct board configuration of the n-queens' placement, where 'Q'
     * and '.' both indicate a queen and an empty space, respectively.
     * <p>
     * Input: n = 4
     * Output: [[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
     * Explanation: There exist two distinct solutions to the 4-queens puzzle as shown above
     * <p>
     * https://leetcode.com/problems/n-queens/description/
     */
    @Test
    void testSolveNQueens() {
        List<List<String>> result = solveNQueens(4);
        Assertions.assertThat(result.get(0)).containsExactly(".Q..", "...Q", "Q...", "..Q.");
        Assertions.assertThat(result.get(1)).containsExactly("..Q.", "Q...", "...Q", ".Q..");
    }

    /**
     * Use recursive function taking a row then iterate each column to place a queen and recursively explore the
     * next row to place another queen until the last row and backtrack.
     * <p>
     * Observation:
     * When placing a queen, we need to find the general rule to validate if that place is not attacked by others.
     * Say if we will iterate each row to place a queen on one of its square. We will need to check three properties:
     * Any queen at the same column
     * - We can use a set containing the column indices that we placed the queen
     * <p>
     * Any queen at the same diagonal(diagonal from left-top[0,0] to right-bottom[n,n])
     * - For each square on a given diagonal, the difference between the row and column indices (row - col)
     * will be constant.
     * - We can use a set containing all the index difference (row - col) of the square we placed the queen
     * <p>
     * Any queen at the same anti-diagonal(diagonal from right-top[0,n] to left-bottom[n,0])
     * - For each square on a given anti-diagonal, the sum of the row and column indexes (row + col)
     * will be constant.
     * - We can use a set containing all the index sum (row + col) of the square we placed the queen
     * <p>
     * Every time we place a queen, we should calculate the diagonal and the anti-diagonal value it belongs to.
     * Then update the above three sets.
     * <p>
     * Algo:
     * We will have a recursive funtion taking a given row index, iterate each column on this row and recursvively
     * check every row to see if we can place a queen.
     * <p>
     * 1. The base case of the recursive function is if the current row idx we are considering is equal to n,
     * which means out of bound and we have explored all rows and the caller is on the last row. So we have a finished
     * state and can save it to the solution list.
     * <p>
     * 2. Otherwise, we iterate through the columns of the current row. At each column, we will attempt to place a
     * queen at the square (row, col).
     * - Check if there is any queen placed in the same column, diagonal, or anti-diagonal. If not, move on and try next.
     * - If the square is good, add the queen to the board and update our 3 sets (cols, diagonals, and antiDiagonals),
     * and recursively call the function again but with row+1 to explore the next row
     * - Since we're done exploring that path, backtrack by removing the queen from the square - this includes removing
     * the values we added to our sets on top of removing the "Q" from the board.
     * <p>
     * Time complexity: O(N!)
     * <p>
     * Unlike the brute force approach, we will only place queens on squares that aren't under attack. For the first
     * queen, we have N options. For the next queen, we won't attempt to place it in the same column as the first queen,
     * and there must be at least one square attacked diagonally by the first queen as well. Thus, the maximum number
     * of squares we can consider for the second queen is N−2. For the third queen, we won't attempt to place it in 2
     * columns already occupied by the first 2 queens, and there must be at least two squares attacked diagonally
     * from the first 2 queens. Thus, the maximum number of squares we can consider for the third queen is N−4.
     * This pattern continues, resulting in an approximate time complexity of N!.
     * <p>
     * While it costs O(N^2) to build each valid solution, the amount of valid solutions S(N) does not grow nearly
     * as fast as N!, so O(N! + S(N) * N^2) = O(N!)
     * <p>
     * Space complexity: O(N^2)
     */
    List<List<String>> solveNQueens(int n) {
        char[][] board = new char[n][n];
        // Init the board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++)
                board[i][j] = '.';
        }
        List<List<String>> result = new ArrayList<>();
        placeQueen(0, new HashSet<>(), new HashSet<>(), new HashSet<>(), board, result, n);
        return result;
    }

    private void placeQueen(int row, Set<Integer> cols, Set<Integer> diagonals, Set<Integer> antiDiagonals,
                            char[][] state, List<List<String>> result, int size) {
        // Base case - row index out of bound. We have explored all rows to place the queens, so we have one valid state.
        if (row == size) {
            // For the 52. N-Queens II problem which doesn't ask for the complete board states but only the number of solutions.
            // We can just pass a AtomicInteger count and increment it here.
            saveBoardState(state, result);
            return;
        }
        for (int col = 0; col < size; col++) {
            // For each column on this row, we recursively check every row to see if we can place the queen
            int currentDiagonals = row - col;
            int currentAntiDiagonals = row + col;
            // Check if the queen is placeable on this cell
            if (cols.contains(col) || diagonals.contains(currentDiagonals) || antiDiagonals.contains(currentAntiDiagonals))
                continue;
            // We can add the queen to the board
            state[row][col] = 'Q';
            diagonals.add(currentDiagonals);
            antiDiagonals.add(currentAntiDiagonals);
            cols.add(col);

            // Move on to the next row with the updated board state
            placeQueen(row + 1, cols, diagonals, antiDiagonals, state, result, size);

            // Remove the queen from the board since we have already explored all valid paths using
            // the above function call. This is the backtracking part, so we will be able to move to the next column later
            // and explore other solutions
            state[row][col] = '.';
            diagonals.remove(currentDiagonals);
            antiDiagonals.remove(currentAntiDiagonals);
            cols.remove(col);
        }
    }

    private void saveBoardState(char[][] state, List<List<String>> result) {
        // Convert the state to the required output format
        List<String> board = new ArrayList<>();
        for (char[] row : state) {
            String rowStr = new String(row);
            board.add(rowStr);
        }
        result.add(board);
    }
}
