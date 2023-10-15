package playground;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * TODO: DP tips
 *  1. DP problems are either implemented in Bottom-up(Tabulation w/ recursion) and Top-down(Memoization)
 *  2. Top-down is implemented with a recursive function and hash map, whereas bottom-up is implemented with nested for loops and an array.
 *      When designing this function or array, we also need to decide on state variables to pass as arguments.
 */
public class DynamicProgramming {

    @Test
    void testHammingDistance() {
        Assertions.assertThat(generate(5).get(2)).containsExactly(1, 2, 1);
        Assertions.assertThat(generate(5).get(3)).containsExactly(1, 3, 3, 1);
        Assertions.assertThat(generate(1).get(0)).containsExactly(1);
    }

    /**
     * Dynamic Programming
     * The iterative approach to constructing Pascal's triangle can be classified as dynamic programming
     * because we construct each row based on the previous row.
     * For each row, the first and last element are always 0, the rest of them is constructed from the sum of
     * the elements above-and-to-the-left and above-and-to-the-right row
     * Time Complexity: O(numRows^2). Space Complexity: O(1)
     */
    List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();
        triangle.add(new ArrayList<>());
        // Base case; first row is always [1].
        triangle.get(0).add(1);

        for (int rowNum = 1; rowNum < numRows; rowNum++) {
            List<Integer> row = new ArrayList<>();
            List<Integer> prevRow = triangle.get(rowNum - 1);
            // First element is always 1
            row.add(1);

            for (int i = 1; i < rowNum; i++) { // <-- terminate when less than rowNum
                // currRow[i] = prevRow[i-1] + prevRow[i]
                // Each triangle element (other than the first and last of each row) is equal to the sum of the elements
                // above-and-to-the-left and above-and-to-the-right.
                row.add(prevRow.get(i - 1) + prevRow.get(i));
            }

            // Last element is always 1
            row.add(1);

            triangle.add(row);
        }
        return triangle;
    }

    /**
     * Climbing Stairs
     * <p>
     * You are climbing a staircase. It takes n steps to reach the top.
     * Each time you can either climb 1 or 2 steps. In how many distinct ways
     * can you climb to the top?
     * <p>
     * Input: n = 2
     * Output: 2
     * Explanation: There are two ways to climb to the top.
     * 1. 1 step + 1 step
     * 2. 2 steps
     * <p>
     * Input: n = 3
     * Output: 3
     * Explanation: There are three ways to climb to the top.
     * 1. 1 step + 1 step + 1 step
     * 2. 1 step + 2 steps
     * 3. 2 steps + 1 step
     * https://leetcode.com/problems/climbing-stairs/editorial/
     */
    @Test
    void testClimbStairs() {
        Assertions.assertThat(climbStairs(2)).isEqualTo(2);
        Assertions.assertThat(climbStairs(3)).isEqualTo(3);
    }

    /**
     * Dynamic Programming
     * The problem can be broken into sub-problems and since we can only either do 1 or 2 steps,
     * so this leads us to generalize the solution of the sub-problem:
     * To reach the ith stair, we either come from the i-1 th or i-2 th stair.
     * Let dp[i] denotes the number of ways to reach on ith step:
     * <p>
     * dp[i] = dp[i−1] + dp[i−2]
     * <p>
     * And we only need the state of the last two steps, all we need is to cache them at the variables
     * (Similar to Fibonacci Number implementation)
     * Time Complexity: O(n). Space Complexity: O(1)
     */
    int climbStairs(int n) {
        if (n == 1)
            return 1;
        int first = 1;
        int second = 2;
        for (int i = 3; i <= n; i++) {
            int third = first + second;
            first = second;
            second = third;
        }
        return second;
    }

    /**
     * House Robber
     * You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed,
     * the only constraint stopping you from robbing each of them is that adjacent houses have security systems connected and
     * it will automatically contact the police if two adjacent houses were broken into on the same night.
     * <p>
     * Given an integer array nums representing the amount of money of each house, return the maximum amount of money
     * you can rob tonight without alerting the police.
     * <p>
     * Input: nums = [1,2,3,1]
     * Output: 4
     * Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
     * Total amount you can rob = 1 + 3 = 4.
     * https://leetcode.com/problems/house-robber/editorial/
     */
    @Test
    void testRob() {
        int[] houses = new int[]{1, 2, 3, 1};
        Assertions.assertThat(rob(houses)).isEqualTo(4);
        houses = new int[]{2, 7, 9, 3, 1};
        Assertions.assertThat(rob(houses)).isEqualTo(12);
    }

    /**
     * Dynamic Programming
     * If we are at some house, logically, we have 2 options: we can choose to rob this house, or we can choose to not rob this house.
     * If we decide not to rob the house, then we don't gain any money. Whatever money we had from the previous house
     * is how much money we will have at this house - which is opt(i - 1).
     * If we decide to rob the house, then we gain nums[i] money. However, this is only possible if we did not rob the
     * previous house. This means the money we had when arriving at this house is the money we had from the previous
     * house without robbing it, which would be however much money we had 2 houses ago, opt(i - 2). After robbing the current house,
     * we will have opt(i - 2) + nums[i] money.
     * <p>
     * Therefore the recurrence equation:
     * opt(i) = max(opt(i-1), opt(i-2) + nums[i])
     * <p>
     * And the base cases are
     * opt(0) = nums[0] --> rob 1st house
     * opt(1) = max(nums[0], nums[1]) --> rob either 1st or 2nd house
     * Time Complexity: O(n). Space Complexity: O(1)
     */
    int rob(int[] nums) {
        if (nums.length == 1)
            return nums[0];

        // base case
        int lastLastMaxProfit = nums[0];
        int lastMaxProfit = Math.max(nums[0], nums[1]);

        for (int i = 2; i < nums.length; i++) {
            int currentMaxProfit = Math.max(lastLastMaxProfit + nums[i], lastMaxProfit);
            lastLastMaxProfit = lastMaxProfit;
            lastMaxProfit = currentMaxProfit;
        }
        return lastMaxProfit;
    }

    /**
     * Maximum Subarray
     * Given an integer array nums, find the subarray with the largest sum, and return its sum.
     * https://leetcode.com/problems/maximum-subarray/editorial/
     */
    @Test
    void testMaxSubArray() {
        int[] nums = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};
        Assertions.assertThat(maxSubArray(nums)).isEqualTo(6);
        nums = new int[]{5, 4, -1, 7, 8};
        Assertions.assertThat(maxSubArray(nums)).isEqualTo(23);
    }

    /**
     * Dynamic Programming
     * To generalize the solution to the sub-problem
     * When we are at nums[i], we should add nums[i] to the sub-arrary ONLY if the new sum is greater than
     * the nums[i] itself.
     * <p>
     * For example:
     * subArraySum = -10
     * nums[i] = 5
     * --> we should just reset the sub-array to index i
     * <p>
     * subArraySum = 10
     * nums[i] = 5
     * --> include the index i in sub-array and add 5 to the subArraySum
     * <p>
     * Therefore, the recurrence equation is
     * opt(i) = max(opt(i-1) + nums[i], nums[i])
     * Time Complexity: O(n). Space Complexity: O(1)
     */
    int maxSubArray(int[] nums) {
        // base case
        int currentSubSum = nums[0]; // Running sum of the sub array
        int maxSubSum = nums[0]; // Keep track of the max sub array sum we see so far
        for (int i = 1; i < nums.length; i++) {
            // When we are at nums[i], if adding nums[i] to the current sub array makes the sum less than nums[i] itself,
            // we will just update the current sub array sum to nums[i], in other words, the sub array is reset to start
            // at nums[i]
            currentSubSum = Math.max(currentSubSum + nums[i], nums[i]);
            maxSubSum = Math.max(maxSubSum, currentSubSum);
        }
        return maxSubSum;
    }

    /**
     * Unique Paths
     * There is a robot on an m x n grid. The robot is initially located at the top-left corner (i.e., grid[0][0]). The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). The robot can only move either down or right at any point in time.
     * Given the two integers m and n, return the number of possible unique paths that the robot can take to reach the bottom-right corner.
     */
    @Test
    void testUniquePaths() {

        Assertions.assertThat(uniquePaths(3, 7)).isEqualTo(28);

    }

    /**
     * Dynamic Programming
     * Base case is opt[0][0] = 1
     * <p>
     * Recurrence relation:
     * opt[row][col] = opt[row - 1][col] + opt[row][col - 1]
     * where opt[row][0] and opt[0][col] are 1 in case of out of bound
     * Time Complexity: O(mn). Space Complexity: O(mn)
     */
    int uniquePaths(int m, int n) {
        int[][] grid = new int[m][n];
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                if (col == 0 || row == 0)
                    grid[row][col] = 1;
                else
                    grid[row][col] = grid[row - 1][col] + grid[row][col - 1];
            }
        }
        return grid[m - 1][n - 1];
    }

    /**
     * Unique Paths II
     * You are given an m x n integer array grid. There is a robot initially located at the top-left corner (i.e., grid[0][0]). The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). The robot can only move either down or right at any point in time.
     * An obstacle and space are marked as 1 or 0 respectively in grid. A path that the robot takes cannot include any square that is an obstacle.
     * Return the number of possible unique paths that the robot can take to reach the bottom-right corner.
     */
    @Test
    void testUniquePathsWithObstacles() {
        int[][] input = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        Assertions.assertThat(uniquePathsWithObstacles(input)).isEqualTo(2);
    }

    /**
     * Dynamic Programming
     * We use the input, obstacleGrid array to store the final result so we don't need extra space
     * 1. If the first cell i.e. obstacleGrid[0,0] contains 1, this means there is an obstacle in the first cell. Hence the robot won't be able to make any move and we would return the number of ways as 0.
     * 2. Otherwise, if obstacleGrid[0,0] has a 0 originally we set it to 1 and move ahead.
     * 3. Iterate the first row. If a cell originally contains a 1, this means the current cell has an obstacle and shouldn't contribute to any path. Hence, set the value of that cell to 0. Otherwise, set it to the value of previous cell i.e. obstacleGrid[i,j] = obstacleGrid[i,j-1]
     * 4. Iterate the first column. If a cell originally contains a 1, this means the current cell has an obstacle and shouldn't contribute to any path. Hence, set the value of that cell to 0. Otherwise, set it to the value of previous cell i.e. obstacleGrid[i,j] = obstacleGrid[i-1,j]
     * 5. Now, iterate through the array starting from cell obstacleGrid[1,1]. If a cell originally doesn't contain any obstacle then the number of ways of reaching that cell would be the sum of number of ways of reaching the cell above it and number of ways of reaching the cell to the left of it.
     * obstacleGrid[i,j] = obstacleGrid[i-1,j] + obstacleGrid[i,j-1]
     * 6. If a cell contains an obstacle set it to 0 and continue. This is done to make sure it doesn't contribute to any other path.
     * Time Complexity: O(mn). Space Complexity: O(1)
     */
    int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int rowLen = obstacleGrid.length;
        int colLen = obstacleGrid[0].length;
        if (obstacleGrid[0][0] == 1)
            return 0;
        // base case
        obstacleGrid[0][0] = 1;
        // Fill the first column
        for (int row = 1; row < rowLen; row++) {
            if (obstacleGrid[row][0] == 0 && obstacleGrid[row - 1][0] == 1)
                // When the original value is 0(not obstacle) and the above cell is 1
                obstacleGrid[row][0] = 1;
            else
                obstacleGrid[row][0] = 0;
        }
        // Fill the first row
        for (int col = 1; col < colLen; col++) {
            if (obstacleGrid[0][col] == 0 && obstacleGrid[0][col - 1] == 1)
                // When the original value is 0(not obstacle) and the left cell is 1
                obstacleGrid[0][col] = 1;
            else
                obstacleGrid[0][col] = 0;
        }
        for (int row = 1; row < rowLen; row++) {
            for (int col = 1; col < colLen; col++) {
                if (obstacleGrid[row][col] == 1)
                    obstacleGrid[row][col] = 0;
                else
                    obstacleGrid[row][col] = obstacleGrid[row - 1][col] + obstacleGrid[row][col - 1];
            }
        }
        return obstacleGrid[rowLen - 1][colLen - 1];
    }

    /**
     * Coin Change
     * https://leetcode.com/problems/coin-change/editorial/
     * You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.
     * <p>
     * Return the fewest number of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return -1.
     * <p>
     * You may assume that you have an infinite number of each kind of coin.
     */
    @Test
    void testCoinChange() {
        int[] coins = {1, 2, 5};
        Assertions.assertThat(coinChange(coins, 11)).isEqualTo(3);
        coins = new int[]{2};
        Assertions.assertThat(coinChange(coins, 3)).isEqualTo(-1);
    }

    /**
     * Dynamic Programming
     * F(S) - minimum NUMBER of coins needed to make change for amount S using coin denominations [C0…Cn−1]
     * <p>
     * Let's assume that we know F(S) where some change val1,val2,…
     * for S which is optimal and the last coin's denomination is C.
     * Then the following equation should be true because of optimal substructure of the problem:
     * <p>
     * F(S) = F(S−C) + 1
     * # Plus 1 because no matter what coin denominations we chose for F(S), we always add one coin to the number of coins being used
     * <p>
     * But we don't know which is the denomination of the last coin C. We compute F(S−Ci) for each possible denomination C0,C1,C2…Cn−1 and choose the minimum among them.
     * Therefore the recurrence relation is
     * <p>
     * F(S) = MIN i=0...n−1 F(S−Ci)+1, subject to S−Ci ≥ 0
     * <p>
     * For the base case
     * <p>
     * F(S)=0 ,when S = 0 (No coin needed when the amount is 0)
     * F(S)=−1 ,when n = 0
     * Time complexity : O(S*n), S is the amount and n is the number of coin type
     * On each step the algorithm finds the next F(i) in n iterations, where 1≤ i ≤S. Therefore in total the iterations are S∗n.
     * Space complexity : O(S)
     */
    int coinChange(int[] coins, int amount) {
        int max = amount + 1; // dummy val so we know it is an empty state -> no way to do coin change for the given amount
        // dp[i] stores the min num of the coins needed for amount i
        // index of array denotes the amount
        int[] dp = new int[amount + 1]; // add one for the amount 0
        Arrays.fill(dp, max);
        // base case
        dp[0] = 0;
        for (int i = 1; i < dp.length; i++) {
            for (int coin : coins) {
                // Iterate each coin type to find the min num of coins needed for the amount, i.
                if (coin <= i) // We only check coin type when its denominator is less or equal to the current amount
                    // Keep updating dp[i] so we can keep comparing for each coin to search for min
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
        return dp[amount] == max ? -1 : dp[amount]; // if dp[amount] remains max, means it wasn't updated(no coin change possible)
    }

    /**
     * Longest Increasing Subsequence
     * https://leetcode.com/problems/longest-increasing-subsequence/description/
     * Given an integer array nums, return the length of the longest strictly increasing subsequence.
     * <p>
     * Input: nums = [10,9,2,5,3,7,101,18]
     * Output: 4
     * Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
     * <p>
     * Input: nums = [0,1,0,3,2,3]
     * Output: 4
     */
    @Test
    void testLengthOfLIS() {
        int[] input = {10, 9, 2, 5, 3, 7, 101, 18};
        Assertions.assertThat(lengthOfLIS(input)).isEqualTo(4);
        input = new int[]{0, 1, 0, 3, 2, 3};
        Assertions.assertThat(lengthOfLIS(input)).isEqualTo(4);
        Assertions.assertThat(lengthOfLISOpt(input)).isEqualTo(4);
    }

    /**
     * Dynamic Programming
     * base case:
     * dp[i] initial value is 1, since every element on its own is technically an increasing subsequence.
     * recurrence relation:
     * dp[i] = max(dp[j] + 1) for all j where nums[j] < nums[i] and j < i.
     * # Plus 1 because we add the current element to the subsequence
     * Time Complexity: O(n^2). Space Complexity: O(n)
     */
    int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        int maxLength = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j])
                    dp[i] = Math.max(dp[j] + 1, dp[i]);
            }
            maxLength = Math.max(dp[i], maxLength);
        }
        Arrays.binarySearch(dp, 1);
        return maxLength;
    }

    /**
     * Intelligently Build a Subsequence(DP + Binary Search)
     * Let nums=[3,4,1,2,8,5,6]
     * For two subsequences of the same length[1,2,8] and [3,4,5],
     * [3,4,5] is better cuz the last element 5 is smaller than 8 which has more chance to expand,
     * e.g. [3,4,5,6] but not [1,2,8,6].
     * What about [1,2,5] and [3,4,5]? They are the same to the following numbers at length 3.
     * [1,2] is better than [3,4] at length 2. ONLY the last number matters!
     * Algo:
     * dp[i]: the smallest ending number of a subsequence that has length i+1
     * 1. Initialize an ArrayList dp which contains the first element of nums.
     * 2. Iterate through the input, starting from the second element. For each element num:
     * If num is greater than any element in dp, then add num to dp. This means we extend the longest subsequence
     * Otherwise, use binary search in dp to find the smallest element that is greater than or equal to num. Replace that element with num to generate a better subsequence
     * 3. Return the length of dp
     * Time Complexity: O(Nlog(N)). Space Complexity: O(n)
     */
    int lengthOfLISOpt(int[] nums) {
        ArrayList<Integer> dp = new ArrayList<>();
        dp.add(nums[0]);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > dp.get(dp.size() - 1))
                // If the current number is greater than the last element of the dp list, it means we have found a
                // longer increasing subsequence. Hence, we append the current number.
                dp.add(nums[i]);
            else {
                // Use binary search to find the smallest element in the dp list that is greater than or equal to the current number.
                int j = Collections.binarySearch(dp, nums[i]);
                // update the element at the found position with the current number.
                // Note: binarySearch returns (-(insertion point) - 1) when key isn't found
                dp.set((j < 0) ? -j - 1 : j, nums[i]);
            }
        }
        return dp.size();
    }

    /**
     * Jump Game
     * You are given an integer array nums. You are initially positioned at the array's first index, and each element
     * in the array represents your maximum jump length at that position.
     * Return true if you can reach the last index, or false otherwise.
     * <p>
     * Input: nums = [2,3,1,1,4]
     * Output: true
     * Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.
     * Example 2:
     * <p>
     * Input: nums = [3,2,1,0,4]
     * Output: false
     * Explanation: You will always arrive at index 3 no matter what. Its maximum jump length is 0, which makes it
     * impossible to reach the last index.
     * https://leetcode.com/problems/jump-game/description/
     */
    @Test
    void testCanJump() {
        int[] input = {12, 3, 1, 1, 4};
        Assertions.assertThat(canJump(input)).isTrue();
        input = new int[]{3, 2, 1, 0, 4};
        Assertions.assertThat(canJump(input)).isFalse();
    }

    /**
     * Greedy
     * Define a var, goal, initialized to the last index
     * Iterating from the end of input array, if there is a potential jump that reaches the goal from the current index to the goal,
     * i.e. currentIndex i + nums[i] >= goal index, we make the current index as new goal.
     * Therefore, if we can continue to move the goal to the first index, we returns true, otherwise, false.
     * It doesn't matter if we can make a BIG jump from any position. All we need is just one way.
     * Time Complexity: O(n). Space Complexity: O(1)
     */
    boolean canJump(int[] nums) {
        int goalIndx = nums.length - 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (i + nums[i] >= goalIndx)
                goalIndx = i;
        }
        return goalIndx == 0;
    }

    /**
     * Word Break
     * Given a string s and a dictionary of strings wordDict, return true if s can be segmented into
     * a space-separated sequence of one or more dictionary words.
     * <p>
     * Note that the same word in the dictionary may be reused multiple times in the segmentation.
     * <p>
     * Input: s = "leetcode", wordDict = ["leet","code"]
     * Output: true
     * Explanation: Return true because "leetcode" can be segmented as "leet code".
     * <p>
     * Input: s = "applepenapple", wordDict = ["apple","pen"]
     * Output: true
     * Explanation: Return true because "applepenapple" can be segmented as "apple pen apple".
     * Note that you are allowed to reuse a dictionary word.
     * <p>
     * https://leetcode.com/problems/word-break/description/
     */
    @Test
    void testWordBreak() {
        List<String> dict = List.of("leet", "code");
        Assertions.assertThat(wordBreak("leetcode", dict)).isTrue();
    }

    /**
     * Bottom-Up Dynamic Programming
     * First define our dp[i]: is it possible to form s up to a length of i from dictionary words?
     * We will iterate over all substrings that end before index i. If we find one of these
     * substrings is in wordDict and we can form the string prior to the substring, then dp[i] = true.
     * <p>
     * recurrence relation:
     * dp[i] = any{ dp[j] && dictionary.contains(w) }
     * i is [0..n+1] n is the length of the input word
     * w is the substring[0..i-1] of the input word
     * j is [0..i-1]
     * <p>
     * Algo:
     * 1. Convert wordsDict to a set words.
     * 2. Initialize an array dp of length n + 1 with all values set to false.
     * 3. Iterate i from 1 until and including n. Here, i represents the length of the string starting from 1, one
     * -  character long. This outer loop finds the answer for one from [d[1]...dp[n]] at each iteration, where n
     * -  is the length of input word
     * -- 	 Iterate j from 0 until i. Here, j represents the first index of the substring we are checking.
     * --   If dp[j] is true AND the substring s[j:i] is in words, set dp[i] = true and break.
     * --	 Note that s[j:i] represents the substring starting at j and ending at i - 1.
     * 4. Return dp[n].
     * <p>
     * The following shows the code flow and how the dp[] is used with the input from the test case
     * Each block is one iteration of outer loop and the first line is the dp[] it tries to resolve(outer loop)
     * and beneath it are what happens at each iteration in the inner loop
     * <p>
     * == dp[0]: length: 0, "" ==
     * true
     * <p>
     * == dp[1]: length: 1, "l" ==
     * dp[0] & dict.contains("l")
     * <p>
     * == dp[2]: length: 2, "le" ==
     * dp[0] & dict.contains("le")
     * dp[1] & dict.contains("e")
     * <p>
     * == dp[3]: length: 3, "lee" ==
     * dp[0] & dict.contains("lee")
     * dp[1] & dict.contains("ee")
     * dp[2] & dict.contains("e")
     * <p>
     * == dp[4]: length: 4, "leet" ==
     * dp[0] & dict.contains("leet") ==> true ==> dp[4]
     * <p>
     * ...
     * <p>
     * == dp[8]: length: 8, "leetcode" ==
     * ...
     * dp[4] & dict.contains("code") ==> true ==> dp[8]
     * <p>
     * Given n as the length of s, m as the length of wordDict, and k as the average length of the words in wordDict,
     * <p>
     * Time complexity: O(n^3 + m⋅k)
     * First, we spend O(m⋅k) to convert wordDict into a set. Then we have a nested loop over n,
     * which iterates O(n^2)times. For each iteration, we have a substring operation which could cost up to O(n).
     * Thus this nested loop costs O(n^3).
     * <p>
     * Space complexity: O(n+m⋅k)
     * <p>
     * The dp array takes O(n) space. The set words takes up O(m⋅k) space.
     */
    boolean wordBreak(String s, List<String> wordDict) {
        int n = s.length();
        Set<String> words = new HashSet<>(wordDict);
        // dp[i]: is it possible to form s up to a LENGTH of i from dictionary words
        boolean[] dp = new boolean[n + 1]; // n+1 cuz we start from zero-length s
        dp[0] = true; // When s has 0 length, true cuz no need to do word break for empty string

        // Iterate over all substrings that end before index i.  If we find one of these substrings is in wordDict
        // and we can form the string prior to the substring, then dp[i] = true.
        for (int i = 1; i <= n; i++) { // end ptr, start from dp[1] to dp[s.length], i represents the length of the string starting from one char
            for (int j = 0; j < i; j++) { // start ptr. need to use all the word breaks beginning from the base case to dp[i-1] to calculate dp[i]
                // j represents the first index of the substring we are checking.
                if (dp[j] && words.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n];
    }
}
