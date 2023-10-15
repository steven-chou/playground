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
     * https://leetcode.com/problems/reverse-string/solution/*
     */
    @Test
    void testReverseString() {
        char[] charArray = {'h', 'e', 'l', 'l', 'o'};
        reverseString(charArray);
        Assertions.assertArrayEquals(new char[]{'o', 'l', 'l', 'e', 'h'}, charArray);
    }

    /**
     * Time Complexity: O(N). Space Complexity: O(1)
     */
    void reverseString(char[] chars) {
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

        if (s.length() != t.length()) {
            return false;
        }
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
            if (count != 0) {
                return false;
            }
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
     * Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
     * https://leetcode.com/problems/valid-parentheses/description/
     */
    @Test
    void testIsValidParentheses() {
        Assertions.assertTrue(isValidParentheses("()[]{}"));
        Assertions.assertFalse(isValidParentheses("(]"));
    }

    private final Map<Character, Character> mappings = Map.of('(', ')', '{', '}', '[', ']');

    /**
     * Use Stacks
     * <p>
     * 1. Initialize a stack S, and maintain a open -> close parentheses lookup map
     * 2. Process each bracket of the expression one at a time.
     * 3. If we encounter an opening bracket, we simply push it onto the stack. This means we will process it later
     * 4. Else we pop the top element from the stack and check if the associated closed parenthese from the map is matched. If yes, continue, otherwise, this implies an invalid expression.
     * 5. In the end, if we are left with a stack still having elements, then this implies an invalid expression.
     * Time Complexity: O(n). Space Complexity: O(n)
     */
    boolean isValidParentheses(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (mappings.containsKey(c))
                stack.push(c);
            else {
                if (stack.isEmpty())
                    // Edge case: no open parentheses left
                    return false;

                Character open = stack.pop();
                if (mappings.get(open) != c)
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
}
