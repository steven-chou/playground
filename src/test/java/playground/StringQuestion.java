package playground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/* TODO: Useful tips
    - Convert char to int
        1. Use Character.getNumericValue(c) --> -1 if the c has no numeric value, -2 if the c is negative integer
        2. If c is any char of 0-9, we can just do: int idx = c - '0';
            See https://stackoverflow.com/questions/46343616/how-can-i-convert-a-char-to-int-in-java
            and https://www.asciitable.com/
        3. int i = Integer.parseInt(String.valueOf(c));
        4. Sometimes we want to store the key-value pair and key is single char either of a-z or A-Z, instead of using map,
           we can can map a-z to the index of array of size 26, we can just do myChar - 'a'. To get the above mapped char
           from the index of array, do char myChar = (char) (idx + 'a')
           Java char arithmetic: http://www.java2s.com/Tutorials/Java/Java_Language/2040__Java_char.htm
    - Convert char[] to String
        1.  String.valueOf(char data[])
    - Check char is digit or letter
        Character.isLetter('c')
        Character.isDigit('1')
    - substring tips:
      return empty string when beginIndex == endIndex
      return empty string when calling str.sbustring(str.length)
-

 */
public class StringQuestion {
    /**
     * Reverse String
     * Write a function that reverses a string. The input string is given as an array of characters s.
     * You must do this by modifying the input array in-place with O(1) extra memory.
     * <p>
     * Input: s = ["h","e","l","l","o"]
     * Output: ["o","l","l","e","h"]
     * <p>
     * Input: s = ["H","a","n","n","a","h"]
     * Output: ["h","a","n","n","a","H"]
     * https://leetcode.com/problems/reverse-string/description/
     */
    @Test
    void testReverseString() {
        char[] charArray = {'h', 'e', 'l', 'l', 'o'};
        reverseString(charArray);
        assertThat(charArray).containsExactly('o', 'l', 'l', 'e', 'h');
    }

    /**
     * Use two pointers, left and right to iterate the array from two ends and swap the element until
     * they are cross each other
     * Time Complexity: O(N). Space Complexity: O(1)
     */
    void reverseString(char[] chars) {
        int left = 0, right = chars.length - 1;
        while (left < right) {
            char tmp = chars[left];
            chars[left] = chars[right];
            chars[right] = tmp;
            ++left;
            --right;
        }
    }

    /**
     * Another implementation using one pointer
     * Time Complexity: O(N). Space Complexity: O(1)
     */
    void reverseStringII(char[] chars) {
        for (int i = 0; i < chars.length / 2; i++) {
            var tmp = chars[i];
            chars[i] = chars[chars.length - 1 - i];
            chars[chars.length - 1 - i] = tmp;
        }
    }

    /**
     * Reverse Integer
     * https://leetcode.com/problems/reverse-integer/solution/
     * EPI 4.8
     */
    @Test
    void testReverseInt() {
        int num = 123;
        Assertions.assertEquals(321, reverseInt(num));
        num = -123;
        Assertions.assertEquals(-321, reverseInt(num));
        num = 120;
        Assertions.assertEquals(21, reverseInt(num));
    }

    //Time Complexity: O(log(x)). There are roughly log10(x) digits in x. Space Complexity: O(1)
    int reverseInt(int x) {
        int ans = 0;
        while (x != 0) {
            int pop = x % 10; // mod operation gives us the right most digit
            x /= 10; // int div drops the right most digit, and the loop continues until it becomes zero
            /*
            MAX_VALUE = 2147483647, MIN_VALUE = -2147483648
            To explain the following check, lets assume that ans is positive.
                1. If temp = ans⋅10 + pop causes overflow, then it must be that ans ≥ INT_MAX/10
                2. If ans > INT_MAX/10, then temp = ans⋅10 + pop is guaranteed to overflow.
                3. If ans == INT_MAX/10, then temp = ans⋅10 + pop will overflow if and only if pop > 7
            Similar logic can be applied when ans is negative.
             */
            if (ans > Integer.MAX_VALUE / 10 || (ans == Integer.MAX_VALUE / 10 && pop > 7))
                return 0;
            if (ans < Integer.MIN_VALUE / 10 || (ans == Integer.MIN_VALUE / 10 && pop < -8))
                return 0;
            ans = ans * 10 + pop; // This can cause an overflow, so we need above check
        }
        return ans;
    }

    /**
     * First Unique Character in a String
     * https://leetcode.com/problems/first-unique-character-in-a-string/solution/
     */
    @Test
    void testFirstUniqueCharInString() {
        Assertions.assertEquals(0, firstUniqChar("leetcode"));
        Assertions.assertEquals(2, firstUniqChar("loveleetcode"));
        Assertions.assertEquals(-1, firstUniqChar("aabb"));
    }

    //Time Complexity: O(N). Space Complexity: O(1)
    int firstUniqChar(String s) {
        Map<Character, Integer> charToCount = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            charToCount.put(c, charToCount.getOrDefault(c, 0) + 1);
        }
        for (int i = 0; i < s.length(); i++) {
            if (charToCount.get(s.charAt(i)) == 1)
                return i;
        }
        return -1;
    }

    /**
     * Valid Anagram
     * Given two strings s and t, return true if t is an anagram of s, and false otherwise.
     * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase,
     * typically using all the original letters exactly once.
     * <p>
     * Input: s = "anagram", t = "nagaram"
     * Output: true
     * <p>
     * Input: s = "rat", t = "car"
     * Output: false
     * https://leetcode.com/problems/valid-anagram/solution/
     */
    @Test
    void testIsAnagram() {
        Assertions.assertTrue(isAnagramUnicode("dog", "god"));
        Assertions.assertTrue(isAnagram("anagram", "nagaram"));
        Assertions.assertFalse(isAnagram("rat", "car"));
    }

    /**
     * Character Frequency Counter - put char count in array of size 26 and compare
     * To examine if t is a rearrangement of sss, we can count occurrences of each letter in the two
     * strings and compare them. We could use a hash table to count the frequency of each letter, however,
     * since both s and t only contain letters from aaa to zzz, a simple array of size 26 will suffice.
     * <p>
     * We can increment the count for each letter in s and decrement the count for each letter in t,
     * and then check if the count for every character is zero.
     * Time Complexity: O(N).
     * Space Complexity: O(1) cuz the array size is fixed
     */
    boolean isAnagram(String s, String t) {// Assumption: s and t consist of lowercase English letters
        if (s.length() != t.length())
            return false;
        int[] counter = new int[26]; // TODO: MEMORIZE
        // increment the counter for each letter in ss and decrement the counter for each letter in tt, then check if the counter reaches back to zero.
        for (int i = 0; i < s.length(); i++) {
            // " -'a' " is to calculate the correct index for the hash counter.
            // Since the counter is of size 26, the range of values for the index is 0-25, inclusive, but a lowercase 'a' has an ascii value of 97,
            // thus we have to subtract 'a' from whatever the current character is in the string to get within 0-25.
            counter[s.charAt(i) - 'a']++;
            counter[t.charAt(i) - 'a']--;
        }
        for (int count : counter) {
            if (count != 0)
                return false;
        }
        return true;
    }

    //Time Complexity: O(N). Space Complexity: O(N)
    boolean isAnagramUnicode(String s, String t) { // inputs may contain Unicode characters
        Map<Character, Integer> charToCount = new HashMap<>();
        if (s.length() != t.length()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            charToCount.put(s.charAt(i), charToCount.getOrDefault(s.charAt(i), 0) + 1);
            charToCount.put(t.charAt(i), charToCount.getOrDefault(t.charAt(i), 0) - 1);
        }
        for (char c : charToCount.keySet()) {
            if (charToCount.get(c) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find All Anagrams in a String
     * Given two strings s and p, return an array of all the start indices of p's anagrams in s.
     * You may return the answer in any order.
     * <p>
     * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase,
     * typically using all the original letters exactly once.
     * <p>
     * Input: s = "cbaebabacd", p = "abc"
     * Output: [0,6]
     * Explanation:
     * The substring with start index = 0 is "cba", which is an anagram of "abc".
     * The substring with start index = 6 is "bac", which is an anagram of "abc".
     * <p>
     * Input: s = "abab", p = "ab"
     * Output: [0,1,2]
     * Explanation:
     * The substring with start index = 0 is "ab", which is an anagram of "ab".
     * The substring with start index = 1 is "ba", which is an anagram of "ab".
     * The substring with start index = 2 is "ab", which is an anagram of "ab".
     * <p>
     * https://leetcode.com/problems/find-all-anagrams-in-a-string/description/
     */
    @Test
    void testFindAnagrams() {
        assertThat(findAnagrams("cbaebabacd", "abc")).containsExactly(0, 6);
    }

    /**
     * Use Sliding window w/ HashMap to iterate the string s and dynamically build the charToCount map given the chars in the
     * window and compare it to the charToCount map built from the string p.
     * <p>
     * This is the extension of the "Valid Anagram" problem
     * <p>
     * Time complexity: O(Ns), Ns and Np be the length of s and p respectively. Let K be the maximum possible
     * number of distinct characters. In this problem, K equals 26 because s and p consist of lowercase English letters.
     * We perform one pass along each string when Ns ≥ Np which costs O(Ns+Np) time. Since we only perform this step
     * when Ns ≥ Np the time complexity simplifies to O(Ns).
     * <p>
     * Space complexity: O(K)
     * pCount and sCount will contain at most K elements each. Since K is fixed at 26 for this problem,
     * this can be considered as O(1) space.
     */
    List<Integer> findAnagrams(String s, String p) {
        Map<Character, Integer> charToCountP = new HashMap<>();
        Map<Character, Integer> charToCountS = new HashMap<>();
        for (int i = 0; i < p.length(); i++) {
            Integer count = charToCountP.getOrDefault(p.charAt(i), 0);
            charToCountP.put(p.charAt(i), count + 1);
        }
        List<Integer> result = new ArrayList<>();
        int winSize = p.length(); // Sliding window size
        int j = 0; // ptr of window left side
        for (int i = 0; i < s.length(); i++) {
            Integer count = charToCountS.getOrDefault(s.charAt(i), 0);
            charToCountS.put(s.charAt(i), count + 1);
            if (i - j + 1 == winSize) { // Reach the window size
                // Compare two map
                if (charToCountS.equals(charToCountP))
                    result.add(j);
                Integer leftCharCount = charToCountS.get(s.charAt(j));
                // Update/Drop the left-most char before we advance the left ptr
                if (leftCharCount == 1)
                    charToCountS.remove(s.charAt(j));
                else
                    charToCountS.put(s.charAt(j), leftCharCount - 1);
                ++j;
            }
        }
        return result;
    }

    /**
     * Valid Palindrome
     * A palindrome is a word, phrase, or sequence that reads the same backwards as forwards. e.g. madam
     * A palindrome, and its reverse, are identical to each other.)
     * https://leetcode.com/problems/valid-palindrome/solution/
     */
    @Test
    void testIsPalindrome() {
        Assertions.assertTrue(isPalindrome("A man, a plan, a canal: Panama"));
        Assertions.assertFalse(isPalindrome("race a car"));
    }

    /*
    Set two pointers, one at each end of the input string
    If the input is palindromic, both the pointers should point to equivalent characters, at all times.
    If this condition is not met at any point of time, we break and return early.
    We can simply ignore non-alphanumeric characters by continuing to traverse further.
    Continue traversing inwards until the pointers meet in the middle.
    Time Complexity: O(N). Space Complexity: O(1)
     */
    boolean isPalindrome(String s) {
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            while (i < j && !Character.isLetterOrDigit(s.charAt(i))) {
                // keep going forward until it is alpha char, or it passes the tail ptr
                i++;
            }
            while (j > i && !Character.isLetterOrDigit(s.charAt(j))) {
                // keep going backward until it is alpha char, or it passes the head ptr
                j--;
            }
            if (Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s.charAt(j))) {
                return false;
            }
        }
        return true;
    }

    /**
     * String to Integer (atoi)
     * https://leetcode.com/problems/string-to-integer-atoi/solution/
     */
    @Test
    void testAtoi() {
        Assertions.assertEquals(42, atoi("0042"));
        Assertions.assertEquals(42, atoi("42"));
        Assertions.assertEquals(-42, atoi("-42"));
        Assertions.assertEquals(4193, atoi("4193 with words"));
        Assertions.assertEquals(0, atoi("words and 987"));
    }


    //Time Complexity: O(N). Space Complexity: O(1)
    /*
    Discard all the whitespaces at the beginning of the string.
    There could be an optional sign of a numerical value +/-+/−. It should be noted that the integer is positive by
    default if there is no sign present and there could be at most one sign character. Build the result using
    the above algorithm until there exists a non-whitespace character that is a number (0 to 9).
    Simultaneously, check for overflow/underflow conditions at each step.
     */
    int atoi(String str) {
        int i = 0;
        int sign = 1;
        if (str.length() == 0)
            return 0;

        // Discard whitespaces in the beginning
        while (i < str.length() && str.charAt(i) == ' ')
            i++;

        // Check if optional sign exists
        if (i < str.length() && (str.charAt(i) == '+' || str.charAt(i) == '-')) {
            sign = (str.charAt(i) == '-') ? -1 : 1;
            i++;
        }

        int result = 0;
        // Iterate to build the result and check for overflow/underflow condition and stop if it is not a digit
        // INT_MAX = 2147483647, INT_MIN = -2147483648
        while (i < str.length() && Character.isDigit(str.charAt(i))) {
            int digit = str.charAt(i) - '0';
            if (result > Integer.MAX_VALUE / 10 ||
                    (result == Integer.MAX_VALUE / 10 && digit > Integer.MAX_VALUE % 10)) {
                /*
                overflow/underflow will happen after appending the digit that meet any one of following conditions
                1. The current number is greater than Integer.MAX_VALUE / 10, i.e. 214748364
                2. The current number is equal to Integer.MAX_VALUE / 10, and the appending digit is greater than7,
                   i.e. Integer.MAX_VALUE % 10. When appending 8 and the current number is -214748364, it will become
                   -2147483648 and not underflow, but the code still returns Integer.MIN_VALUE as expected

                If the sign is +, it will overflow, so return 2^31-1, otherwise underflow, return -2^31
                */
                return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            // Append current digit to the result. (leading 0 will produce 0)
            result = result * 10 + digit;
            i++;
        }
        return result * sign;
    }

    /**
     * Implement strStr() (indexOf)
     * https://leetcode.com/problems/implement-strstr/solution/
     */
    @Test
    void testStrStr() {
        Assertions.assertEquals(2, strStr("hello", "ll"));
        Assertions.assertEquals(-1, strStr("aaaaa", "bba"));

        Assertions.assertEquals(2, rabinKarpStrStr("hello", "ll"));
        Assertions.assertEquals(0, rabinKarpStrStr("", ""));
    }

    /**
     * Sliding Window approach
     * Time Complexity: O(nm), For every window_start, we may have to iterate at most m times.
     * There are n−m+1 such window_start's. Thus, it is O((n−m+1)⋅m), which is O(nm).
     * Space Complexity: O(1)
     */
    int strStr(String haystack, String needle) {
        int nLen = needle.length(), hLen = haystack.length();

        for (int windowStart = 0; windowStart <= hLen - nLen; windowStart++) {
            for (int i = 0; i < nLen; i++) {
                if (needle.charAt(i) != haystack.charAt(windowStart + i)) {
                    break;
                }
                if (i == nLen - 1) {
                    return windowStart;
                }
            }
        }
        return -1;
    }

    // This implementation uses built-in substring method so it is less performant
    // Time Complexity: O((N-L)L), where N is a length of haystack and L is a length of needle. We compute a substring of length L in a loop, which is executed (N - L) times.
    // Space Complexity: O(1)
    int strStrBuiltIn(String haystack, String needle) {
        int subStrLen = needle.length(), strLen = haystack.length();

        for (int windowStart = 0; windowStart <= strLen - subStrLen; windowStart++) {
            if (haystack.substring(windowStart, windowStart + subStrLen).equals(needle)) {
                return windowStart;
            }
        }
        return -1;
    }


    // TODO: Need to revisit this. Leetcode has newer/different implementation using Rabin-Karp algorithm (Double Hash)
    // Time Complexity: O(N), one computes the reference hash of the needle string in O(L) time, and then runs a loop of (N−L) steps with constant time operations in it.
    // Space Complexity: O(1)
    int rabinKarpStrStr(String haystack, String needle) { // TODO: MEMORIZE
        int needleLength = needle.length(), haystackLength = haystack.length();
        if (needleLength > haystackLength)
            return -1;

        // base value for the rolling hash function
        int base = 26;
        // modulus value for the rolling hash function to avoid overflow
        long modulus = (long) Math.pow(2, 31);

        // compute the hash of strings haystack[:needleLength], needle[:needleLength]
        long hHash = 0, nHash = 0;
        for (int i = 0; i < needleLength; ++i) {
            hHash = (hHash * base + charToInt(i, haystack)) % modulus;
            nHash = (nHash * base + charToInt(i, needle)) % modulus;
        }
        if (hHash == nHash)
            return 0;

        // const value to be used often : base**needleLength % modulus
        long aL = 1;
        for (int i = 1; i <= needleLength; ++i)
            aL = (aL * base) % modulus;

        for (int start = 1; start < haystackLength - needleLength + 1; ++start) {
            // compute rolling hash in O(1) time
            hHash = (hHash * base - charToInt(start - 1, haystack) * aL
                    + charToInt(start + needleLength - 1, haystack)) % modulus;
            if (hHash == nHash)
                return start;
        }
        return -1;
    }

    int charToInt(int idx, String s) {
        return (int) s.charAt(idx) - (int) 'a';
    }

    /**
     * Count and Say
     * https://leetcode.com/problems/count-and-say/solution/
     */
    @Test
    void testCountAndSay() {
        Assertions.assertEquals("1211", countAndSay(4));
        Assertions.assertEquals("312211", countAndSay(6));
    }

    /* Reference:
        https://www.baeldung.com/regular-expressions-java
        https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/regex/Pattern.html
        https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/regex/Matcher.html
     */
    // Time Complexity: O(2^n), where n is the index of the desired sequence.
    // Space Complexity: O(2^n-1)
    String countAndSay(int n) { // TODO: MEMORIZE
        String currSeq = "1";

        // Pattern to match the repetitive digits
        String regexPattern = "(.)\\1*";
        Pattern pattern = Pattern.compile(regexPattern);

        for (int i = 1; i < n; i++) {
            Matcher m = pattern.matcher(currSeq);
            StringBuilder nextSeq = new StringBuilder();

            // each group contains identical and adjacent digits
            while (m.find()) {
                nextSeq.append(m.group().length()).append(m.group().charAt(0));
            }
            // prepare for the next iteration
            currSeq = nextSeq.toString();
        }

        return currSeq;
    }

    /**
     * Longest Common Prefix
     * https://leetcode.com/problems/longest-common-prefix/solution/
     */
    @Test
    void testLongestCommonPrefix() {
        Assertions.assertEquals("d", longestCommonPrefixVerticalScan(new String[]{"d", "d", "dcd"}));
        Assertions.assertEquals("fl", longestCommonPrefixHorizontalScan(new String[]{"flower", "flow", "flight"}));
        Assertions.assertEquals("", longestCommonPrefixVerticalScan(new String[]{"dog", "racecar", "car"}));
        Assertions.assertEquals("fl", longestCommonPrefixVerticalScan(new String[]{"flo", "flower", "fl"}));
    }

    // Compare characters from top to bottom on the same column (same character index of the strings) before moving on to the next column.
    // Time Complexity: O(S), where S is the sum of all characters in all strings.
    // Even though the worst case is still the same as HorizontalScan, in the best case there are at most n*minLen comparisons
    // where minLen is the length of the shortest string in the array.
    // Space Complexity: O(1). We only used constant extra space.
    String longestCommonPrefixVerticalScan(String[] strs) { // ---> Better
        if (strs == null || strs.length == 0)
            return "";
        for (int i = 0; i < strs[0].length(); i++) {
            // Use the first string item as the starting point to start to scan and check its char one by one
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                // Check the char at the same index of other strings. Break the loop and return the current prefix when
                //  1. The index is equal to the length of the string, which means this string is the shortest one in the
                //  array, and we can't check the next prefix char candidate here otherwise index will be out of bound
                //  2. The char at this index of the string is not equal to the current prefix char
                // In either one of above condition, use the substring method to return the prefix
                if (i == strs[j].length() || strs[j].charAt(i) != c)
                    return strs[0].substring(0, i); // substring exclude the val of index i, substring w/ 0 to 0 returns ""
            }
        }
        // We are here cuz the first string is the shortest and is also the common prefix(otherwise it would return in the above loop)
        return strs[0];
    }

    // Time Complexity: O(S), where S is the sum of all characters in all strings.
    // Space Complexity: O(1). We only used constant extra space.
    String longestCommonPrefixHorizontalScan(String[] strs) {
        if (strs.length == 0)
            return "";
        // Take the first item as prefix
        String prefix = strs[0];
        // Loop thru all of the items in the list
        for (int i = 1; i < strs.length; i++)
            while (strs[i].indexOf(prefix) != 0) {
                // Remove the last letter from the prefix if we don't find the matched prefix
                prefix = prefix.substring(0, prefix.length() - 1);
                // If we removed every letter it means that there is no common prefix
                if (prefix.isEmpty())
                    return "";
            }
        return prefix;
    }

    String flongestCommonPrefixVerticalScan(String[] strs) { // ---> Better
        if (strs == null || strs.length == 0)
            return "";
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (i == strs[j].length() || strs[j].charAt(i) != c) {
                    return strs[0].substring(0, i);
                }
            }
        }
        return strs[0];
    }

    /**
     * Valid Parentheses
     * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']',
     * determine if the input string is valid.
     * <p>
     * An input string is valid if:
     * <p>
     * Open brackets must be closed by the same type of brackets.
     * Open brackets must be closed in the correct order.
     * Every close bracket has a corresponding open bracket of the same type.
     * <p>
     * Input: s = "()"
     * Output: true
     * <p>
     * Input: s = "()[]{}"
     * Output: true
     * <p>
     * Input: s = "(]"
     * Output: false
     * https://leetcode.com/problems/valid-parentheses/description/
     */
    @Test
    void testIsValidParentheses() {
        Assertions.assertTrue(isValidParentheses("()[]{}"));
        Assertions.assertFalse(isValidParentheses("(]"));
    }

    private final Map<Character, Character> leftToRight = Map.of('(', ')', '{', '}', '[', ']');

    /**
     * First we build the left parenthesis to right parenthesis char map. Iterating the str if is left parenthesis,
     * push to the stack. Otherwise, pop the element from stack and check if the mapped right parenthesis is the same
     * as this one. If the stack is not empty in the then it is also invalid
     * <p>
     * Observation:
     * Ideally we want to check the valid parenthesis pair from the innermost and expanding outward. We can use stack
     * to help us process in the similar way. If we push the parenthesis to the stack as we iterate, the top element
     * is the innermost parenthesis.
     * <p>
     * Algo:
     * 1. Initialize a stack S, and maintain a open parenthesis -> close parenthesis lookup map
     * 2. Process each bracket of the expression one at a time.
     * 3. If we encounter an opening bracket, we simply push it onto the stack. This means we will process it later
     * 4. Else we pop the top element from the stack and check if the associated closed parenthesis from the map
     * is matched. If yes, continue, otherwise, this implies an invalid expression.
     * 5. In the end, if we are left with a stack still having elements, then this implies an invalid expression.
     * Time Complexity: O(n). Space Complexity: O(n)
     */
    boolean isValidParentheses(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (leftToRight.containsKey(c))
                // Left parenthesis
                stack.push(c);
            else {
                // Right parenthesis
                if (stack.isEmpty())
                    // Edge case: no open parentheses left
                    return false;

                Character open = stack.pop();
                if (leftToRight.get(open) != c)
                    return false;
            }
        }
        return stack.isEmpty(); // non-empty stack means there are open parentheses w/o closed
    }

    /**
     * Decode String
     * Given an encoded string, return its decoded string.
     * <p>
     * The encoding rule is: k[encoded_string], where the encoded_string inside the square brackets is
     * being repeated exactly k times. Note that k is guaranteed to be a positive integer.
     * <p>
     * You may assume that the input string is always valid; there are no extra white spaces, square
     * brackets are well-formed, etc. Furthermore, you may assume that the original data does not contain
     * any digits and that digits are only for those repeat numbers, k. For example, there will not be
     * input like 3a or 2[4].
     * <p>
     * The test cases are generated so that the length of the output will never exceed 105.
     * <p>
     * Input: s = "3[a]2[bc]"
     * Output: "aaabcbc"
     * <p>
     * Input: s = "3[a2[c]]"
     * Output: "accaccacc"
     * <p>
     * Input: s = "2[abc]3[cd]ef"
     * Output: "abcabccdcdcdef"
     * <p>
     * https://leetcode.com/problems/decode-string/description/
     */
    @Test
    void testDecodeString() {
        assertThat(decodeString("3[a]2[bc]")).isEqualTo("aaabcbc");
        assertThat(decodeString("2[abc]3[cd]ef")).isEqualTo("abcabccdcdcdef");
    }

    /**
     * Push char to the stack and start decoding one segment of string when encountering ']', every inner-segment
     * decoded string is push back to the stack so it is included when decoding the outer segment.
     * <p>
     * Algo:
     * The input can contain an alphabet (a-z), digit (0-9), opening braces [ or closing braces ].
     * Start traversing string s and process each character based on the following rules:
     * <p>
     * Case 1: Current character is not a closing bracket ].
     * - Push the current character to stack.
     * <p>
     * Case 2: Current character is a closing bracket ].
     * - Start decoding the last traversed string by popping the string decodedString and number k from
     * the top of the stack.
     * -- 1. Pop from the stack while the next character is not an opening bracket [ and append each character
     * --    (a-z) to the decodedString.
     * -- 2. Pop opening bracket [ from the stack.
     * -- 3. Pop from the stack while the next character is a digit (0-9) and build the number k.
     * <p>
     * Now that we have k and decodedString , decode the pattern k[decodedString] by pushing the decodedString
     * to stack k times.
     * Once the entire string is traversed, pop the result from stack and return.
     * <p>
     * Note:
     * Remember we want to keep the decoded string in the stack in the right order from bottom to top
     * <p>
     * Time Complexity: O(maxK^countK⋅n), where maxK is the maximum value of k, countK is
     * the count of nested k values and n is the maximum length of encoded string.
     * Example, for s = 20[a10[bc]], maxK is 20, countK is 2 as there are 2 nested k values
     * (20 and 10) . Also, there are 2 encoded strings a and bc with maximum length of
     * encoded string ,n as 2
     * <p>
     * The worst case scenario would be when there are multiple nested patterns.
     * Let's assume that all the k values (maxK) are 10 and all encoded string(n) are of size 2.
     * <p>
     * For, s = 10[ab10[cd]]10[ef], time complexity would be roughly equivalent to
     * 10 ∗ cd ∗ 10 ∗ ab + 10 ∗ 2 = 10^2 ∗2
     * <p>
     * Hence, for an encoded pattern of form maxK[nmaxK[n]], the time complexity to decode
     * the pattern can be given as, O(maxK^countK⋅n)
     * <p>
     * Space Complexity: O(sum(maxK^countK⋅n))
     * where maxK is the maximum value of k, countK is the count of nested k values and n
     * is the maximum length of encoded string. The maximum stack size would be
     * equivalent to the sum of all the decoded strings in the form maxK[nmaxK[n]]
     */
    String decodeString(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != ']')
                // Keep pushing char to stack until we see ']'
                stack.push(c);
            else {
                // Now we see ']', start to decode string for this part
                StringBuilder sb = new StringBuilder();
                while (!stack.isEmpty() && !Character.isDigit(stack.peek())) {
                    // build the letter part
                    Character currentChar = stack.pop();
                    if (currentChar != '[') { // Ignore '['
                        sb.append(currentChar);
                    }
                }
                // Now we built the string in reversed order
                String str = sb.toString();

                // Start to build the repeat number (it can be multi-digit)
                StringBuilder countSb = new StringBuilder();
                while (!stack.isEmpty() && Character.isDigit(stack.peek())) {
                    countSb.append(stack.pop()); // digit popped from stack is in reversed order
                }
                int count = Integer.parseInt(countSb.reverse().toString());

                while (count > 0) {
                    // Build the string w/ repeated (count) times
                    for (int j = str.length() - 1; j >= 0; j--) {
                        // The str we built is in reversed order, so we need to push each char to stack
                        // in reverse order, so it will be in the right order from bottom to top in the stack
                        stack.push(str.charAt(j));
                    }
                    count--;
                }
            }
        }
        StringBuilder decodedSb = new StringBuilder();
        while (!stack.isEmpty()) {
            // When we pop from the stack and append, the constructed string is in reversed order
            decodedSb.append(stack.pop());
        }
        return decodedSb.reverse().toString(); // reversed str is our answer
    }

    /**
     * Ransom Note
     * Given two strings ransomNote and magazine, return true if ransomNote can be constructed by
     * using the letters from magazine and false otherwise.
     * <p>
     * Each letter in magazine can only be used once in ransomNote.
     * <p>
     * Input: ransomNote = "a", magazine = "b"
     * Output: false
     * Example 2:
     * <p>
     * Input: ransomNote = "aa", magazine = "ab"
     * Output: false
     * Example 3:
     * <p>
     * Input: ransomNote = "aa", magazine = "aab"
     * Output: true
     * <p>
     * https://leetcode.com/problems/ransom-note/description/
     */
    @Test
    void testCanConstruct() {
        assertThat(canConstruct("aa", "ab")).isFalse();
        assertThat(canConstruct("aa", "aab")).isTrue();
    }

    /**
     * Construct the HashMap of char to count from magazine string, and then subtract characters from
     * the ransom note from the count in the map. If any char is not found or count is zero then it is
     * false, otherwise true.
     * <p>
     * Time Complexity : O(m), m is the length of the magazine
     * Space Complexity : O(k), k is never more than 26, which is a constant
     */
    boolean canConstruct(String ransomNote, String magazine) {
        Map<Character, Integer> magazineCharToCount = new HashMap<>();
        for (int i = 0; i < magazine.length(); i++) {
            Integer count = magazineCharToCount.computeIfAbsent(magazine.charAt(i), k -> 0);
            magazineCharToCount.put(magazine.charAt(i), ++count);
        }
        for (int i = 0; i < ransomNote.length(); i++) {
            char c = ransomNote.charAt(i);
            if (magazineCharToCount.containsKey(c)) {
                Integer count = magazineCharToCount.get(c);
                if (count == 0)
                    return false;
                magazineCharToCount.put(c, --count);
            } else
                return false;
        }
        return true;
    }

    /**
     * Minimum Window Substring
     * Given two strings s and t of lengths m and n respectively, return the minimum window
     * substring of s such that every character in t (including duplicates) is included
     * in the window. If there is no such substring, return the empty string "".
     * <p>
     * Input: s = "ADOBECODEBANC", t = "ABC"
     * Output: "BANC"
     * Explanation: The minimum window substring "BANC" includes 'A', 'B', and 'C' from string t.
     * <p>
     * Input: s = "a", t = "a"
     * Output: "a"
     * Explanation: The entire string s is the minimum window.
     * <p>
     * Input: s = "a", t = "aa"
     * Output: ""
     * Explanation: Both 'a's from t must be included in the window.
     * Since the largest window of s only has one 'a', return empty string.
     * <p>
     * https://leetcode.com/problems/minimum-window-substring/description/
     */
    @Test
    void testMinWindow() {
        assertThat(minWindow("ADOBECODEBANC", "ABC")).isEqualTo("BANC");
        assertThat(minWindow("a", "a")).isEqualTo("a");
        assertThat(minWindow("aaaaaaaaaaaabbbbbcdd", "abcdd")).isEqualTo("abbbbbcdd");
    }

    /**
     * Use Sliding window to iterate the string s. We need to keep two HashMaps(char->count) for the str t and str in the
     * current window. We keep expanding the window by moving the right ptf. When the window has all the desired characters,
     * we save the smallest window till now and keep shrinking the window(moving the left ptr) until it doesn't
     * satisfied the required characters.
     * <p>
     * Time Complexity: O(S+T) where S and T represent the lengths of strings S and T.
     * <p>
     * Space Complexity: O(S+T) where S and T represent the size of unique char of strings S and T.
     */
    String minWindow(String s, String t) {
        if (s.length() == 0 || t.length() == 0)
            return "";
        // Maps to store the character -> count for str t and the str in the range of current sliding window
        Map<Character, Integer> charToCountT = new HashMap<>();
        Map<Character, Integer> charToCountWin = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            Integer count = charToCountT.getOrDefault(t.charAt(i), 0);
            charToCountT.put(t.charAt(i), count + 1);
        }
        String result = "";
        int left = 0;
        // To track if the str in the current window satisfies the characters from str t, so we don't need to iterate
        // the map everytime when moving the window
        int matchEntryCount = 0;
        int minMatchWinSize = Integer.MAX_VALUE;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            Integer count = charToCountWin.getOrDefault(c, 0);
            charToCountWin.put(c, count + 1);
            if (charToCountWin.get(c).equals(charToCountT.get(c)))
                ++matchEntryCount; // increment when the char count in the window is the same as the one in str t
            // Once the chars at windows satisfied the required chars, i.e. matchEntryCount == charToCountT.size(),
            // we can save the result and start to shrink the window from the left
            while (left <= right && matchEntryCount == charToCountT.size()) {
                // Get the string in the current window & update the min window size
                if (right - left + 1 < minMatchWinSize) {
                    minMatchWinSize = right - left + 1;
                    result = s.substring(left, right + 1);
                }
                // shrink the window from the left until it doesn't keep the match count
                char leftC = s.charAt(left);
                // Drop the count of the left ptr char
                charToCountWin.put(leftC, charToCountWin.get(leftC) - 1);
                if (charToCountT.containsKey(leftC) && charToCountWin.get(leftC) < (charToCountT.get(leftC)))
                    // Decrement the matchEntryCount ONLY when the count of this char is less than the one in the str t
                    --matchEntryCount;
                ++left;
            }
        }
        return result;
    }

    /**
     * Longest Palindromic Substring
     * Given a string s, return the longest palindromic substring in s.
     * <p>
     * Input: s = "babad"
     * Output: "bab"
     * Explanation: "aba" is also a valid answer.
     * <p>
     * Input: s = "cbbd"
     * Output: "bb"
     * <p>
     * https://leetcode.com/problems/longest-palindromic-substring/description/
     */
    @Test
    void testLongestPalindromeSubStr() {
        assertThat(longestPalindromeSubStr("babad")).isEqualTo("bab");
        assertThat(longestPalindromeSubStr("cbbd")).isEqualTo("bb");
    }

    /**
     * For each char in the string, use it as center and expand on both sides to find max length of palindrome
     * The idea is to check for palindrome, we use two index, right and left, to move toward two ends of the string at the same time.
     * For example,
     * Say if we look for an odd-length palindrome, bacab, while we are checking the char 'c' in the string,
     * we set both right and left at index of 'c', and start to move them one index at a time but different direction.
     * <p>
     * However, if we look for an even-length palindrome, e.g. baccab, while we are checking the char 'c' in the string,
     * we set left at index of 'c' and right at its next index and start to move them one index at a time but different direction.
     * <p>
     * We need to do this palindrome exploration on each char in the input string, and also update the max-length palindrome
     * we have found at each iteration.
     * <p>
     * Time complexity: O(n^2)
     * Space complexity: O(1)
     */
    String longestPalindromeSubStr(String s) {
        String maxPalindrome = "";
        int length = s.length();
        if (length < 2)
            return s; //if 0 or 1 chars, return the input string, no palindrome to be checked for
        for (int i = 0; i < length; i++) {
            // Now we start to explore the palindrome by expanding out(left and right) from this position
            // Assume we will find an odd-length palindrome, so the left & right ptr will be both start at the same index,
            // e.g. "abcba", left and right will both start at "c" and move back/forward from there
            String oddPalindrome = findPalindrome(s, i, i);
            // Assume we will find an even-length palindrome, so the left & right ptr will start at i and i+1,
            // e.g. "abccba" left will be at the first "c", right at the 2nd "c", then move back/forward from there
            String evenPalindrome = findPalindrome(s, i, i + 1);
            // Check if either oddPalindrome or evenPalindrome is longer than the current max, if so, update it.
            if (oddPalindrome.length() > maxPalindrome.length())
                maxPalindrome = oddPalindrome;
            if (evenPalindrome.length() > maxPalindrome.length())
                maxPalindrome = evenPalindrome;
        }
        return maxPalindrome;
    }

    String findPalindrome(String str, int left, int right) {
        // check if the char at indexes left and right are the same (condition for palindrome) and not over the boundary
        // if they are equal, expand the range by decrementing the left ptr and incrementing the right ptr
        while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
            left--;
            right++;
        }
        return str.substring(left + 1, right); // Need + 1 cuz when the above while loop breaks, left and right are both one index behind/ahead
    }

    /**
     * Longest Palindrome
     * Given a string s which consists of lowercase or uppercase letters, return the length of the longest
     * palindrome that can be built with those letters.
     * <p>
     * Letters are case sensitive, for example, "Aa" is not considered a palindrome here.
     * <p>
     * Input: s = "abccccdd"
     * Output: 7
     * Explanation: One longest palindrome that can be built is "dccaccd", whose length is 7.
     * <p>
     * Input: s = "a"
     * Output: 1
     * Explanation: The longest palindrome that can be built is "a", whose length is 1.
     * <p>
     * https://leetcode.com/problems/longest-palindrome/description/
     */
    @Test
    void testLongestPalindrome() {
        assertThat(longestPalindrome("abccccdd")).isEqualTo(7);
        assertThat(longestPalindromeII("abccccdd")).isEqualTo(7);
    }

    /**
     * Iterate the str and build the char->count array/HashMap then sum up the number of "pair" in the count.
     * Increment by 1 if the sum is less than str length to account for the center unique char.
     * <p>
     * The key is the formual to compute (the number of pair) X 2 for a given char, say a letter occurs x times, we will have
     * x / 2 * 2
     * Ex: if we have 'aaaaa', then we could have 'aaaa' partnered, which is 5 / 2 * 2 = 4 letters partnered
     * <p>
     * This will include all pair count for both odd or even number count. To maximize the palindrome lenght, if there is char
     * occurring odd number times, it needs to be included in the center. Comparing the running sum w/ the length of input
     * str tells us if there is still any single char left, if so, just add one to the sum.
     * <p>
     * Time Complexity: O(N), where N is the length of s. We need to count each letter.
     * <p>
     * Space Complexity: O(1), the space for our count, as the alphabet size of s is fixed.
     */
    int longestPalindrome(String s) {
        int[] charCount = new int[128]; // ASCII basic table has 128 entries
        for (char c : s.toCharArray())
            charCount[c]++;
        int ans = 0;
        for (int count : charCount) {
            // This formula computes the number of "pair" X 2
            // Even count: take the count. Odd: take count - 1
            ans += count / 2 * 2;
        }
        if (ans < s.length())
            // Cuz we already count all "paired" number above, if there is any char w/ odd number count, the ans must be less than
            // total number of chars. We just need to add one more for that center character(We will place the char repeated odd
            // number times to maximize the palindrome, however, we don't care what char is actually placed there, we just
            // need to know there is such char in the center, and we need to take it into account)
            ++ans;
        return ans;
    }

    /**
     * First attempt implementation. Use unnecessary modulo operation to distinguish odd and even count
     */
    int longestPalindromeII(String s) {
        Map<Character, Integer> charToCount = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            Integer count = charToCount.getOrDefault(s.charAt(i), 0);
            charToCount.put(s.charAt(i), count + 1);
        }
        int result = 0;
        int maxOddCharCount = 0;
        for (Integer count : charToCount.values()) {
            if (count % 2 == 0)
                result += count;
            else {
                if (count > maxOddCharCount) {
                    if (maxOddCharCount > 1)
                        result += maxOddCharCount - 1;
                    maxOddCharCount = count;
                } else if (count > 1)
                    result += count - 1;
            }
        }
        return result + maxOddCharCount;
    }
}