package playground;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * TODO Note:
 *  1. Basic knowledge
 *      Permutation: n! (n factorial) total solution space
 *      Combination: C(n, k) = n! / k!(n-l)! ===> "C n取k, n choose k"
 *      Subset: 2^n  ===> because binomial theorem(二項式定理)
 *           (1+1)^n = C(n,0) + C(n,1) + C(n,2) + ... + C(n,n) ==> 2^n  http://www.math.nsysu.edu.tw/eprob/PerComb/Binomial/
 */
@Slf4j
public class Recursion {


    /**
     * Backtracking template
     *
     * def backtrack(candidate):
     *     if find_solution(candidate):
     *         output(candidate)
     *         return
     *
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

    /**
     * Remove Invalid Parentheses
     * https://leetcode.com/problems/remove-invalid-parentheses/solution/
     */
    @Test
    void testRemoveNthFromEnd() {
        List<String> result = removeInvalidParentheses("()");
        Assertions.assertThat(result).containsAll(Arrays.asList("()"));
        result = removeInvalidParentheses("(()");
        Assertions.assertThat(result).containsAll(Arrays.asList("()"));
        result = removeInvalidParentheses("()())()");
        Assertions.assertThat(result).containsAll(Arrays.asList("(())()", "()()()"));
        result = removeInvalidParentheses("(a)())()");
        Assertions.assertThat(result).containsAll(Arrays.asList("(a())()", "(a)()()"));
        result = removeInvalidParentheses(")(");
        Assertions.assertThat(result).containsAll(Arrays.asList(""));
    }

    private final Set<String> validExpressions = new HashSet<>();
    private int minimumRemoved;

    List<String> removeInvalidParentheses(String s) {
        validExpressions.clear();
        minimumRemoved = Integer.MAX_VALUE;
        recurse(s, 0, 0, 0, new StringBuilder(), 0);
        return new ArrayList<>(this.validExpressions);
    }

    /**
     * Say the input is "()", the call flow will be like the following (We always do delete recursively all the way to the end, then after each recursion call returns,
     * we check if the condition of keeping this char makes sense, and continue to recursively check the next char
     * Start at "("
     * delete "(" ---> recurse(...) call
     * delete ")" ---> recurse(...) call
     * reach the end, get the first answer, empty(delete all) ---> recurse(...) call
     * check & keep ")" ? --->  No, can't keep this cuz there is no corresponding "(" ATM. (We are under the condition that the previous "(" is deleted)
     * check & keep "(" ? ---> Yes, we always want to consider this condition of NOT deleting "("
     * delete ")" ---> recurse(...) call
     * reach the end, NOT a valid answer cuz we have (left: 1, right:0) in expression ---> recurse(...) call
     * check & keep ")" ? --->  Yes, we have a "(" ATM, so let's keep ")" and continue to check next (We are under the condition that the previous "(" is NOT deleted)
     * reach the end, get a valid answer "()" ---> recurse(...) call
     */

    private void recurse(
            String s,
            int index,
            int leftCount, // number of ( added to the expression till now
            int rightCount, // number of ) added to the expression till now
            StringBuilder expression, // possible answer
            int removedCount) {

        // If we have reached the end of string.
        // This is END condition of the recursion
        if (index == s.length()) {

            // If the current expression is valid.
            if (leftCount == rightCount) {

                // If the current count of removed parentheses is <= the current minimum count
                if (removedCount <= this.minimumRemoved) {

                    // Convert StringBuilder to a String. This is an expensive operation.
                    // So we only perform this when needed.
                    String possibleAnswer = expression.toString();

                    // If the current count beats the overall minimum we have till now
                    if (removedCount < this.minimumRemoved) {// The first run will always add "" to the set ---> removing everything
                        this.validExpressions.clear();
                        this.minimumRemoved = removedCount;
                    }
                    this.validExpressions.add(possibleAnswer);
                }
            }
        } else {

            char currentCharacter = s.charAt(index);
            int length = expression.length();

            // If the current character is neither an opening bracket nor a closing one,
            // simply recurse further by adding it to the expression StringBuilder
            if (currentCharacter != '(' && currentCharacter != ')') {
                expression.append(currentCharacter);
                this.recurse(s, index + 1, leftCount, rightCount, expression, removedCount);
                expression.deleteCharAt(length);
            } else {

                // Recursion where we delete the current character and move forward
                // This means lets try to delete this character, ( or )
                this.recurse(s, index + 1, leftCount, rightCount, expression, removedCount + 1);
                // expression is more like a work stack. Adding the current char to it means that after the above recursive call returns from the previous line,
                // we need to determine if we want to keep this char at this level and continue to make recursive call for the next char
                expression.append(currentCharacter);

                // If it's an opening parenthesis, consider it (keep this char as part of answer) and recurse (continue to explore next char)
                if (currentCharacter == '(') {
                    this.recurse(s, index + 1, leftCount + 1, rightCount, expression, removedCount);
                } else if (rightCount < leftCount) { // This is ) and there are more ( than )
                    // For a closing parenthesis, only recurse if right < left
                    // Only keep looking further only if we have more ( than ) so far
                    // We ONLY want to consider(keep it in the answer) this ')' if there is matching '(' in the expression
                    this.recurse(s, index + 1, leftCount, rightCount + 1, expression, removedCount);
                }

                // Undoing the append operation for other recursions.
                // At this point, we've done checking this char and other char(s) behind it and this char might already become the part of the answers
                // Removing it from expression so after this recursive call returns and the previous char can be checked later
                // This step is important in recursion backtrack cuz we need to revert the thing we do at this round before return.
                expression.deleteCharAt(length);
            }
        }
    }

    /**
     * Given two integers n and k, return all possible combinations of k numbers out of the range [1, n].
     * You may return the answer in any order.
     * https://leetcode.com/problems/combinations/solution/
     */
    @Test
    void testCombinations() {
        List<List<Integer>> result = combine(4, 2);
        Assertions.assertThat(combine(4, 2)).containsOnly(List.of(1, 2), List.of(1, 3), List.of(1, 4), List.of(2, 3), List.of(2, 4), List.of(3, 4));
        log.info("{}", result); // [[1, 2], [1, 3], [1, 4], [2, 3], [2, 4], [3, 4]]
    }

    int n, k;

    /**
     * DFS decision tree + Backtracking
     * Think of the solution space as decision tree. Each node is a potential element added to the permutation.
     * Basically we traverse the tree starting from each item in nums and explore all child node(DFS) until
     * reaching the leaf node. For each num that isn't already in tmp, we add it to tmp and then make a recursive
     * call passing tmp. Modifying tmp and making a recursive call is equivalent to "traversing" to a child node in the tree.
     * <p>
     * Think of each call to the recursive function as being a node in the tree. In each call, we need to iterate over
     * the numbers GREATER than the label of the current node. We can pass an argument start representing the first number
     * we should start iterating from. This is cuz we don't want duplicate, i.e. (1,2) and (2,1) are the same
     * <p>
     * When we return from a function call, it's equivalent to moving back up the tree (exactly like in a DFS).
     * When we moved from a parent to a child, we added an element to tmp. When we move from a child back to its parent,
     * we need to remove the element we added from tmp. This is the "backtracking" step.
     * <p>
     * *There is an optimization that cut out the path to the nodes which can't lead to any solution
     * Check out the source code of method findCombinationsOpt
     * <p>
     * Time complexity: O(k * C(n,k))
     * The number of combinations of length k from a set of n elements is C(n,k)
     * Each path to a leaf has k nodes. Therefore the upper bound is the product of this two
     * <p>
     * Space complexity: O(k)
     * We don't count the answer as part of the space complexity. The extra space we use here is for tmp
     * and the recursion call stack. The depth of the call stack is equal to the length of tmp, which is limited to k.
     */
    List<List<Integer>> combine(int n, int k) {
        this.n = n;
        this.k = k;
        List<List<Integer>> result = new ArrayList<>();
        findCombinations(1, new ArrayList<>(), result);
        return result;
    }

    void findCombinations(int start, List<Integer> tmp, List<List<Integer>> ans) {
        if (tmp.size() == k) {
            ans.add(new ArrayList<>(tmp));
            return;
        }
        for (int i = start; i <= n; i++) {
            tmp.add(i);
            // Increment the start when doing the recursive call, so we add the number greater than current node to the tmp
            // at the child. This also makes us always look forward, so we never generate [[1,2],[2,1]] in the result
            findCombinations(i + 1, tmp, ans);
            tmp.remove(tmp.size() - 1);
        }
    }

    void findCombinationsOpt(int start, List<Integer> tmp, List<List<Integer>> ans) {
        if (tmp.size() == k) {
            ans.add(new ArrayList<>(tmp));
            return;
        }

        // Here is the optimization part to cut out the path to the nodes which can't lead to any solution
        int need = k - tmp.size();
        int remain = n - start + 1;
        int available = remain - need;

        for (int num = start; num <= start + available; num++) {
            tmp.add(num);
            findCombinationsOpt(num + 1, tmp, ans);
            tmp.remove(tmp.size() - 1);
        }
    }

    /**
     * Combination Sum
     * Given an array of distinct integers candidates and a target integer target, return a list of all unique combinations of candidates where the chosen numbers sum to target. You may return the combinations in any order.
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
     * DFS decision tree + Backtracking
     * Very similar to the combination problem besides the following key points
     * 1. We need a "remain" var passed in every recursive call to keep track of how many is left
     * after adding the number from the candidate list. This is also used for the base case(remain == 0)
     * 2. Also need to handle the case when remain is less than zero.
     * 3. We still need a start index to prevent us from visitng the node(number) already visited,
     * however, we do NOT increment the start index when making recursive call, cuz every number can be reused as
     * many as we want.
     * Time Complexity: O(N^T/M + 1), where N be the number of candidates, T be the target value, and M be the minimal value among the candidates.
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
     * Given an array nums of distinct integers, return all the possible permutations. You can return the answer in any order.
     * https://leetcode.com/problems/permutations/
     */
    @Test
    void testPermutations() {
        int[] input = {1, 2, 3};
        Assertions.assertThat(permute(input)).containsOnly(List.of(1, 2, 3), List.of(1, 3, 2), List.of(2, 1, 3), List.of(2, 3, 1), List.of(3, 1, 2), List.of(3, 2, 1));
    }

    /**
     * DFS decision tree + Backtracking
     * Think of the solution space as decision tree. Each node is a potential element added to the permutation.
     * Basically we traverse the tree starting from each item in nums and explore all child node(DFS) until
     * reaching the leaf node. For each num that isn't already in tmp, we add it to tmp and then make a recursive
     * call passing tmp. Modifying tmp and making a recursive call is equivalent to "traversing" to a child node in the tree.
     * When we return from a function call, it's equivalent to moving back up the tree (exactly like in a DFS).
     * When we moved from a parent to a child, we added an element to tmp.
     * When we move from a child back to its parent, we need to remove the element we added from tmp.
     * This is the "backtracking" step
     */
    List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        findPermutations(new ArrayList<>(), ans, nums);
        return ans;
    }

    /**
     * Time Complexity: O(n*n!)
     * Given a set of length n, the number of permutations is n!.
     * For each of the n! permutations, we need O(n)work to copy curr into the answer. This gives us O(n⋅n!)work.
     * <p>
     * Space Complexity: O(n)
     * The extra space we use here is for tmp and the recursion call stack.
     * The depth of the call stack is equal to the length of tmp, which is limited to n.
     */
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
     * DFS decision tree + Backtracking
     * The solution is very similar to the permutation question. The key diiference are
     * 1. We need to pre-build a map(number -> count) cuz the number can be duplicate and when we decide to
     * pick the next nubmer, we consider the unique number and wheter it is still availabe to use.
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
        subsets = new ArrayList<>();
        input = new int[]{1, 2, 3, 4};
        subsets(input);
        log.info("{}", subsets); //[[], [1], [1, 2], [1, 2, 3], [1, 2, 3, 4], [1, 2, 4], [1, 3], [1, 3, 4], [1, 4], [2], [2, 3], [2, 3, 4], [2, 4], [3], [3, 4], [4]]
    }

    List<List<Integer>> subsets = new ArrayList<>();

    /**
     * DFS decision tree + Backtracking
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
     * Time Complexity: O(n⋅2^n).==> There are total 2^n combinations(for each number we either include or not) and we iterate all of n numbers to generate combination
     * The height of tree is also n
     * Space Complexity: O(n)
     */
    List<List<Integer>> subsets(int[] nums) {
        findSubsets(0, new ArrayList<>(), nums);
        return subsets;
    }

    void findSubsets(int start, ArrayList<Integer> currList, int[] nums) {
        subsets.add(new ArrayList<>(currList));
        for (int i = start; i < nums.length; i++) {
            // The for loop serves two purposes
            // 1. At the top level, each iteration finds all combination starting with num[i] w/o duplicate(Cuz the start index keep incrementing, we won't look back the number already visited)
            // 2. Inside the recursive call, the start index forwards everytime, so we keep adding new number and generates new subset.
            //    It is also used as the terminate condition for the recursive call(base case) to signal we reach the leaf node and exhaust combination
            currList.add(nums[i]);
            // Calling findSubsets recursively is basically perform DFS from the subset(currList). We explore new subset in the next level(currList length + 1) after adding the new number
            // by calling the findSubsets method w/ INCREMENTING the start index by 1
            // (Forwarding the start index means we move toward the leaf node in the decision tree to generate new combination w/ next number)
            findSubsets(i + 1, currList, nums);
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
        Arrays.sort(nums);
        findSubsetsWithDup(0, new ArrayList<>(), results, nums);
        return results;
    }

    /**
     * DFS decision tree + Backtracking
     * The solution is almost the same as the subset problems besides the following key difference:
     * 1. Cuz the input has duplicate numbers, when we decide to include a number to generate a new subset, we need to
     * know if such subset has already created. Ex, given num = (1,2,2), when we already had (1,2), we shouldn't generate
     * another (1,2) when we do DFS from the node (1), so we need to add an extra condition check
     * 2. All nodes at the same level from the same parent node always has the same startIdx value(we always iterate from the startIdx)
     * Therefore, when startIdx == i, it means this is the first child node we visit at this level from this parent,
     * so we don't need to do duplication check. Otherwise, we need to check if it is the same number as the last one just added
     * to the sibling node.(We have sorted the array, so we can do this way) If it is the same, we need to skip it,
     * or it will generate the duplicate node value.
     * Ex, Given [1, 2, 2], at the child level from the node 1(startIdx == 1), when we already had (1, 2),
     * and are at (startIdx == 1 && i == 2), we don't want to create another (1,2) so we skip it.
     * Time Complexity: O(n⋅2^n).==> There are total 2^n combinations(for each number we either include or not) and we iterate all of n numbers to generate combination
     * The height of tree is also n
     * Space Complexity: O(n)
     */
    void findSubsetsWithDup(int start, List<Integer> tmp, List<List<Integer>> results, int[] nums) {
        results.add(new ArrayList<>(tmp));
        for (int i = start; i < nums.length; i++) {
            if (i != start && nums[i] == nums[i - 1])
                // we do duplication check against last element(sibling node) ONLY when we are not at the first child node
                // from the same parent(Child nodes from the same parent has the same start val)
                continue;
            tmp.add(nums[i]);
            findSubsetsWithDup(i + 1, tmp, results, nums);
            tmp.remove(tmp.size() - 1);
        }
    }

    /**
     * Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent. Return the answer in any order.
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
     * DFS decision tree + Backtracking
     * The solution is very similar to the Combination problem. The only difference is instead of iterating a sequence,
     * we will have a static lookup map, and iterate the string chars associated with the next digit from the input.
     * For the base case, if our current combination of letters is the same length as the input digits, that means we
     * have a complete combination.
     * Otherwise, get all the letters that correspond with the current digit we are looking at, map[index].
     * Loop through these letters. For each letter, add the letter to our current tempStr, and call backtrack again, but
     * move on to the next digit by incrementing index by 1. Then remove the letter from tempStr for the backtracking.
     * <p>
     * Time Complexity: O(4^n n)
     * -- n is the length of digits. Note that 4 in this expression is referring to the maximum value length in the hash map, and not to the length of the input.
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
     * Backtracking, Keep Candidate Valid
     * Recursively building strings of length 2n and checking their validity as we go.
     * In case the current string is invalid, we will not continue the recursive process on it. Instead, we will backtrack to the previous valid string on the recursive path.
     * To ensure that the current string is always valid during the backtracking process, we need two variables left_count and right_count that record the number of left and right parentheses in it, respectively.
     * The function adds more parentheses to cur_string only when certain conditions are met:
     * 1. If left_count < n, it suggests that a left parenthesis can still be added, so we add one left parenthesis to cur_string, creating a new string new_string = cur_string + (, and then call backtracking(new_string, left_count + 1, right_count).
     * 2. If left_count > right_count, it suggests that a right parenthesis can be added to match a previous unmatched left parenthesis, so we add one right parenthesis to cur_string, creating a new string new_string = cur_string + ), and then call backtracking(new_string, left_count, right_count + 1).
     * <p>
     * This function ensures that the generated string of length 2n is valid, and adds it directly to the answer.
     * Time Complexity: O(4^N / square root N). Space Complexity: O(N)
     */
    List<String> generateParenthesis(int n) {
        List<String> answer = new ArrayList<>();
        backtrack(answer, new StringBuilder(), 0, 0, n);
        return answer;
    }

    void backtrack(List<String> answer, StringBuilder curStr, int leftCount, int rightCount, int n) {
        // base case
        if (curStr.length() == 2 * n) {
            // Got a valid combination here, add to the answer and return
            answer.add(curStr.toString());
            return;
        }

        if (leftCount < n) {
            // ( can still be added, and we wanna always place the ( first
            curStr.append("(");
            backtrack(answer, curStr, leftCount + 1, rightCount, n);
            // Do the backtracking here, so need to revert the previous decision/ops
            curStr.deleteCharAt(curStr.length() - 1);
        }
        if (leftCount > rightCount) {
            // a right parenthesis can be added to match a previous unmatched left parenthesis
            curStr.append(")");
            backtrack(answer, curStr, leftCount, rightCount + 1, n);
            curStr.deleteCharAt(curStr.length() - 1);
        }
    }

    /**
     * Word Search
     * Given an m x n grid of characters board and a string word, return true if word exists in the grid.
     * The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring. The same letter cell may not be used more than once.
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
     * DFS decision tree + Backtracking
     * We would walk around the 2D grid, and at each step, we mark our choice before jumping into the next step.
     * And at the end of each step, we would also revert our mark so that we will have a clean slate to try another direction. In addition, the exploration is done via the DFS strategy, where we go as far as possible before we try the next direction.
     * Key points:
     * 1. The recursive method should have an index var and get increment by 1 when it is called,
     * so we can use it as base case(compare to the length of the word) to determine if we find the whole word
     * 2. We need to check the row and col boundary besides the cell value against the char in the word
     * 3. When doing DFS recursive call, we need to call it for four adjacent cells, which means passing different row and col four times.
     * 4. We need to mark the current visiting cell(setting val to a special char), so it won't be visited again.
     * We also need to revert it back once recursive call returns.
     * <p>
     * Time Complexity:O(N⋅3^L), where N is the number of cells in the board and L is the length of the word to be matched.
     * 1. Besides the very first move, we can have at most 4 directions to move, the choices are reduced to 3. So it is basically
     * 3-ary tree, each of the branches represent a potential exploration in the corresponding direction.
     * Therefore, in the worst case, the total number of invocation would be the number of nodes in a full 3-nary tree, which is about 3^L
     * 2. We iterate through the board for backtracking, i.e. there could be N times invocation for the backtracking function in the worst case.
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
}
