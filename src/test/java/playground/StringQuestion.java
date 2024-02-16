package playground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/* TODO: Useful tips
    - char --> int
        1. Use Character.getNumericValue(c) --> -1 if the c has no numeric value, -2 if the c is negative integer
        2. If c is any char of 0-9, we can just do: int idx = c - '0';
            See https://stackoverflow.com/questions/46343616/how-can-i-convert-a-char-to-int-in-java
            and https://www.asciitable.com/
        3. int i = Integer.parseInt(String.valueOf(c));
        4. Sometimes we want to store the key-value pair and key is single char either of a-z or A-Z, instead of using map,
           we can can map a-z to the index of array of size 26, we can just do myChar - 'a'. To get the above mapped char
           from the index of array, do char myChar = (char) (idx + 'a')
           Java char arithmetic: http://www.java2s.com/Tutorials/Java/Java_Language/2040__Java_char.htm
    - char[] --> String
        String str = String.valueOf(char data[])
    - int --> String
        String intStr = String.valueOf(i)
    - String --> int
        int y = Integer.parseInt(str);
        Integer x = Integer.valueOf(str);
    - Check char is digit or letter
        Character.isLetter('c')
        Character.isDigit('1')
        Character.isLetterOrDigit('x')
    - substring tips:
      return empty string when beginIndex == endIndex
      return empty string when calling str.sbustring(str.length)
    - Sort a String by character
        char tempArray[] = inputString.toCharArray();
        Arrays.sort(tempArray)
        return new String(tempArray);
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
     * First Unique Character in a String
     * Given a string s, find the first non-repeating character in it and return its index.
     * If it does not exist, return -1.
     * <p>
     * Input: s = "leetcode"
     * Output: 0
     * <p>
     * Input: s = "loveleetcode"
     * Output: 2
     * <p>
     * Input: s = "aabb"
     * Output: -1
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
     * Use the Map or Array[26] to keep track of the count of each char. If two string has different length,
     * return false first. Otherwise, iterate both string at the same time and increment the count of char
     * of s, and decrement count of char of t in the map/array. Finally, iterate the map/array, return false
     * if any count is not zero.
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
        assertThat(findAnagrams("abab", "ab")).containsExactly(0, 1, 2);
        assertThat(findAnagrams("cbaebabacd", "abc")).containsExactly(0, 6);
    }

    /**
     * First populate the charToCountP Map for str p. Maintain a ptr j(0) for left side of sliding
     * window. Iterate str s, for each char at s, update the 2nd map(charToCountS). When the current
     * sliding window size(i-j+1) == p.length, if two maps are equal, add j to result. Then update the
     * charToCountS map by dropping the entry or decrement the court for char point by ptr j.
     * Finally, advance j ptr.
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
                // Drop the left-most char from the map before we advance the left ptr
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
     * Permutation in String
     * Given two strings s1 and s2, return true if s2 contains a permutation
     * of s1, or false otherwise.
     * <p>
     * In other words, return true if one of s1's permutations is the substring
     * of s2.
     * <p>
     * Input: s1 = "ab", s2 = "eidbaooo"
     * Output: true
     * Explanation: s2 contains one permutation of s1 ("ba").
     * Example 2:
     * <p>
     * Input: s1 = "ab", s2 = "eidboaoo"
     * Output: false
     */
    @Test
    void testCheckInclusion() {
        assertThat(checkInclusion("ab", "eidbaooo")).isTrue();
        assertThat(checkInclusion("ab", "eidboaoo")).isFalse();
    }

    /**
     * First populate the charToCountS1 Map for str. Maintain the left ptr(init: 0) for left side of
     * sliding window. Iterate str s2 w/ right ptr, for each char at s2, update the charToCountWin
     * map. When the current sliding window size(i-j+1) == s1.length, if two maps are equal, return
     * true. Then update the charToCountWin map by dropping the entry or decrement the court for char
     * point by ptr left, then advance left ptr.
     * <p>
     * Let l1 be the length of string s1 and l2 be the length of string s2s.
     * Time complexity: O(l1 + 26*(l2 - l1))
     * Space complexity: O(1). Constant space is used.
     */
    boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) {
            return false;
        }
        Map<Character, Integer> charToCountS1 = new HashMap<>();
        for (int i = 0; i < s1.length(); i++) {
            char c = s1.charAt(i);
            charToCountS1.put(c, charToCountS1.getOrDefault(c, 0) + 1);
        }
        Map<Character, Integer> charToCountWin = new HashMap<>();
        int winSize = s1.length(); // Sliding window size
        int left = 0;
        for (int right = 0; right < s2.length(); right++) {
            char c = s2.charAt(right);
            charToCountWin.put(c, charToCountWin.getOrDefault(c, 0) + 1);
            if (right - left + 1 == winSize) {
                // Compare two map
                if (charToCountS1.equals(charToCountWin))
                    return true;
                // Drop the left-most char from the map before we advance the left ptr
                char leftChar = s2.charAt(left);
                int leftCharCount = charToCountWin.get(leftChar);
                if (leftCharCount == 1)
                    charToCountWin.remove(leftChar);
                else
                    charToCountWin.put(leftChar, leftCharCount - 1);
                left++;
            }
        }
        return false;
    }

    /**
     * Valid Palindrome
     * A phrase is a palindrome if, after converting all uppercase letters into lowercase letters
     * and removing all non-alphanumeric characters, it reads the same forward and backward.
     * Alphanumeric characters include letters and numbers.
     * <p>
     * Given a string s, return true if it is a palindrome, or false otherwise.
     * <p>
     * Input: s = "A man, a plan, a canal: Panama"
     * Output: true
     * Explanation: "amanaplanacanalpanama" is a palindrome.
     * <p>
     * Input: s = "race a car"
     * Output: false
     * Explanation: "raceacar" is not a palindrome.
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

    /**
     * Use two ptr(i:0, j:tail) to iterate the str from two ends. First iteratively move i and j to the
     * first letter or digit char while maintaining i < j. Then compare lowercase char both ptrs at.
     * Return false if they are not equal, otherwise move i and j until i cross j.
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    boolean isPalindrome(String s) {
        int i = 0, j = s.length() - 1;
        while (i < j) {
            while (i < j && !Character.isLetterOrDigit(s.charAt(i))) {
                i++;
            }
            while (i < j && !Character.isLetterOrDigit(s.charAt(j))) {
                j--;
            }

            if (Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s.charAt(j))) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    /**
     * Valid Palindrome II
     * Given a string s, return true if the s can be palindrome after deleting at
     * most one character from it.
     * <p>
     * Input: s = "aba"
     * Output: true
     * <p>
     * Input: s = "abca"
     * Output: true
     * Explanation: You could delete the character 'c'.
     * <p>
     * Input: s = "abc"
     * Output: false
     * <p>
     * https://leetcode.com/problems/valid-palindrome-ii/description/
     */
    @Test
    void testValidPalindrome() {
        assertThat(validPalindrome("aguokepatgbnvfqmgmlcupuufxoohdfpgjdmysgvhmvffcnqxjjxqncffvmhvgsymdjgpfdhooxfuupuculmgmqfvnbgtapekouga")).isTrue();
        assertThat(validPalindrome("aba")).isTrue();
        assertThat(validPalindrome("abca")).isTrue();
    }

    /**
     * Use two ptr(i:0, j:tail) to iterate the str from two ends. If two char are different, then try
     * check if the substring of [i+1, j] is palindrome, if not, check the substring of [i, j-1].
     * <p>
     * Note: Need a helper method taking start and end index to check if given range of substring
     * is palindrome
     * <p>
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    boolean validPalindrome(String s) {
        int i = 0, j = s.length() - 1;
        while (i < j) {
            if (s.charAt(i) != s.charAt(j)) {
                return (checkPalindrome(i + 1, j, s)) || checkPalindrome(i, j - 1, s);
            }
            i++;
            j--;
        }
        return true;
    }

    boolean checkPalindrome(int start, int end, String s) {
        while (start < end) {
            if (s.charAt(start) != s.charAt(end))
                return false;
            start++;
            end--;
        }
        return true;
    }

    /**
     * String to Integer (atoi)
     * Implement the myAtoi(string s) function, which converts a string to a 32-bit signed integer
     * (similar to C/C++'s atoi function).
     * <p>
     * The algorithm for myAtoi(string s) is as follows:
     * <p>
     * 1. Read in and ignore any leading whitespace.
     * <p>
     * 2. Check if the next character (if not already at the end of the string) is '-' or '+'. Read
     * this character in if it is either. This determines if the final result is negative or positive
     * respectively. Assume the result is positive if neither is present.
     * <p>
     * 3. Read in next the characters until the next non-digit character or the end of the input is
     * reached. The rest of the string is ignored.
     * <p>
     * 4. Convert these digits into an integer (i.e. "123" -> 123, "0032" -> 32). If no digits were
     * read, then the integer is 0. Change the sign as necessary (from step 2).
     * <p>
     * 5. If the integer is out of the 32-bit signed integer range [-231, 231 - 1], then clamp the
     * integer so that it remains in the range. Specifically, integers less than -231 should be
     * clamped to -231, and integers greater than 231 - 1 should be clamped to 231 - 1.
     * <p>
     * 6. Return the integer as the final result.
     * <p>
     * Note:
     * Only the space character ' ' is considered a whitespace character.
     * Do not ignore any characters other than the leading whitespace or the rest of the string
     * after the digits.
     * <p>
     * Example:
     * <p>
     * Input: s = "42"
     * Output: 42
     * <p>
     * Input: s = "   -42"
     * Output: -42
     * <p>
     * Input: s = "4193 with words"
     * Output: 4193
     * https://leetcode.com/problems/string-to-integer-atoi/solution/
     */
    @Test
    void testAtoi() {
        assertThat(atoi("0042")).isEqualTo(42);
        assertThat(atoi("42")).isEqualTo(42);
        assertThat(atoi("-42")).isEqualTo(-42);
        assertThat(atoi("4193 with words")).isEqualTo(4193);
        assertThat(atoi("words and 987")).isEqualTo(0);
        assertThat(atoi("   +0 123")).isEqualTo(0);
    }

    /**
     * Discard all the whitespaces at the beginning of the string.
     * There could be an optional sign of a numerical value +/-+/−. It should be noted that the integer is positive by
     * default if there is no sign present and there could be at most one sign character. Build the result using
     * the above algorithm until there exists a non-whitespace character that is a number (0 to 9).
     * Simultaneously, check for overflow/underflow conditions at each step.
     * Time Complexity: O(N)
     * Space Complexity: O(1)
     */
    int atoi(String str) {
        int i = 0;
        int sign = 1;
        if (str.isEmpty())
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
                2. The current number is equal to Integer.MAX_VALUE / 10, and the appending digit is greater than 7,
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
     * Find the Index of the First Occurrence in a String/Implement strStr()
     * Given two strings needle and haystack, return the index of the first occurrence of
     * needle in haystack, or -1 if needle is not part of haystack.
     * <p>
     * Constraints:
     * haystack and needle consist of ONLY lowercase English characters.
     * <p>
     * Input: haystack = "sadbutsad", needle = "sad"
     * Output: 0
     * Input: haystack = "leetcode", needle = "leeto"
     * Output: -1
     * https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/description/
     */
    @Test
    void testStrStr() {
        assertThat(strStr("abc", "c")).isEqualTo(2);
        assertThat(strStr("hello", "ll")).isEqualTo(2);
        assertThat(strStr("aaaaa", "bba")).isEqualTo(-1);
        assertThat(strStr("hello", "ll")).isEqualTo(2);
        assertThat(strStr("", "")).isEqualTo(-1);
    }

    /**
     * Iterate the haystack str and use two ptr to maintain a sliding window of size of str needle.
     * Once two ptr are apart from each other as window size, iterate str needle and compare each char
     * in the current window on str hasystack, if matched, return the left ptr, otherwise, advance
     * left ptr and continue.
     * <p>
     * Time Complexity: O(n⋅m)
     * n as length of needle, m as length of haystack
     * For every window, we may have to iterate at most m times.
     * There are n−m+1 window position. Thus, it is O((n−m+1)⋅m), which is O(n⋅m).
     * Space Complexity: O(1)
     */
    int strStr(String haystack, String needle) {
        int winSize = needle.length();
        int l = 0;
        for (int r = 0; r < haystack.length(); r++) {
            if (r - l + 1 == winSize) {
                // When left & right ptr are moved apart as window size
                boolean matched = true;
                for (int i = l, j = 0; j < winSize; i++, j++) {
                    // Compare each char in needle string and str in the window
                    if (haystack.charAt(i) != needle.charAt(j)) {
                        matched = false;
                        break;
                    }
                }
                if (matched)
                    return l;
                l++; // Move window left ptr for next iteration
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
     * Write a function to find the longest common prefix string amongst an array
     * of strings. If there is no common prefix, return an empty string "".
     * strs[i] consists of only lowercase English letters.
     * <p>
     * Input: strs = ["flower","flow","flight"]
     * Output: "fl"
     * <p>
     * <p>
     * Input: strs = ["dog","racecar","car"]
     * Output: ""
     * Explanation: There is no common prefix among the input strings.
     * https://leetcode.com/problems/longest-common-prefix/solution/
     */
    @Test
    void testLongestCommonPrefix() {
        assertThat(longestCommonPrefix(new String[]{"d", "d", "dcd"})).isEqualTo("d");
        assertThat(longestCommonPrefix(new String[]{"flower", "flow", "flight"})).isEqualTo("fl");
        assertThat(longestCommonPrefix(new String[]{"dog", "racecar", "car"})).isEqualTo("");
        assertThat(longestCommonPrefix(new String[]{"flo", "flower", "fl"})).isEqualTo("fl");

    }

    /**
     * For each char, c, in the first string in array, iterate the remaining strings in the array. For each
     * string if its char at the same index is not equal to c or the ptr on the outer loop is out of bound
     * of the current string, return the string of the StringBuilder. Append the current checking char to
     * the StringBuilder after inner loop ends, which means every other string has the same char
     * <p>
     * Algo:
     * Compare characters from top to bottom on the same column (same character index of the strings)
     * before moving on to the next column.
     * 1. Use the first string item as the starting point to start to scan and check its char one by one.
     * 2. Iterate from the 2nd string in the list
     * -   Check the char at the same index of other strings. Break the loop and return the current prefix when
     * -   1. The index is equal to the length of the string, which means this string is the shortest
     * -      one in the array, and we can't check the next prefix char candidate here otherwise index will be
     * -      out of bound
     * -   2. The char at this index of the string is not equal to the current prefix char
     * -   In either one of above condition, this is the longest prefix
     * 3. Append the current char from the 1st string to the string builder
     * <p>
     * Time Complexity: O(S), where S is the sum of all characters in all strings.
     * Even though the worst case is still the same as HorizontalScan, in the best case there are at most n*minLen comparisons
     * where minLen is the length of the shortest string in the array.
     * Space Complexity: O(1). We only used constant extra space.
     */
    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        StringBuilder stb = new StringBuilder();
        String firstStr = strs[0];
        for (int i = 0; i < firstStr.length(); i++) {
            // Use the first string item as the starting point then start to scan and check its char one by one
            char c = firstStr.charAt(i);
            for (int j = 1; j < strs.length; j++) {
                // Check the char at the same index of other strings. Break the loop and return the current stb when
                //  1. The i ptr on the first string is equal to the length of the current comparing string, which means
                //  the current string is the shortest one in the array, so we can't compare its char anymore
                //  2. The char at this index of the string is not equal to the current prefix char
                // In either one of above condition, this is the longest prefix
                if (i == strs[j].length() || strs[j].charAt(i) != c) {
                    return stb.toString();
                }
            }
            stb.append(c);
        }
        return stb.toString();
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
     * Minimum Remove to Make Valid Parentheses
     * Given a string s of '(' , ')' and lowercase English characters.
     * <p>
     * Your task is to remove the minimum number of parentheses ( '(' or ')', in any positions )
     * so that the resulting parentheses string is valid and return any valid string.
     * <p>
     * Formally, a parentheses string is valid if and only if:
     * <p>
     * It is the empty string, contains only lowercase characters, or
     * It can be written as AB (A concatenated with B), where A and B are valid strings, or
     * It can be written as (A), where A is a valid string.
     * <p>
     * Input: s = "lee(t(c)o)de)"
     * Output: "lee(t(c)o)de"
     * Explanation: "lee(t(co)de)" , "lee(t(c)ode)" would also be accepted.
     * <p>
     * Input: s = "a)b(c)d"
     * Output: "ab(c)d"
     * <p>
     * Input: s = "))(("
     * Output: ""
     * Explanation: An empty string is also valid.
     * https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/description/
     */
    @Test
    void testMinRemoveToMakeValid() {
        assertThat(minRemoveToMakeValid("lee(t(c)o)de)")).isEqualTo("lee(t(c)o)de");
        assertThat(minRemoveToMakeValid("a)b(c)d")).isEqualTo("ab(c)d");
        assertThat(minRemoveToMakeValid("))((")).isEqualTo("");
    }

    /**
     * For each char in the string, if "(", push its index to the stack, if ")", pop the stack if it is
     * not empty, otherwise, add its index to the invalidIdx Set. If the stack is not empty after the loop,
     * pop all index from the stack and add them to the invalidIdx set. Then iterate the string and use
     * the StringBuilder to append the char whose index is not in the set
     * <p>
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    String minRemoveToMakeValid(String s) {
        Set<Integer> invalidIdx = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(i);
            } else if (c == ')') {
                if (!stack.isEmpty()) {
                    stack.pop();
                } else {
                    invalidIdx.add(i);
                }
            }
        }

        if (!stack.isEmpty()) {
            while (!stack.isEmpty()) {
                invalidIdx.add(stack.pop());
            }
        }

        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (!invalidIdx.contains(i)) {
                stb.append(s.charAt(i));
            }
        }
        return stb.toString();
    }

    /**
     * Longest Valid Parentheses
     * Given a string containing just the characters '(' and ')', return the length of the longest
     * valid (well-formed) parentheses substring.
     * <p>
     * Input: s = "(()"
     * Output: 2
     * Explanation: The longest valid parentheses substring is "()".
     * <p>
     * Input: s = ")()())"
     * Output: 4
     * Explanation: The longest valid parentheses substring is "()()".
     * <p>
     * Input: s = ""
     * Output: 0
     * <p>
     * https://leetcode.com/problems/longest-valid-parentheses/description/
     */
    @Test
    void testLongestValidParentheses() {
        assertThat(longestValidParentheses("(()()")).isEqualTo(4);
        assertThat(longestValidParentheses("(()")).isEqualTo(2);
        assertThat(longestValidParentheses(")()())")).isEqualTo(4);
        assertThat(longestValidParentheses("()(()")).isEqualTo(2);
        assertThat(longestValidParentheses("(()(((()")).isEqualTo(2); //"))((",
    }

    /**
     * Iterate the str and update the maxLength when left and right parenthesis count are equal. Reset both counts
     * when right > left. Iterate again but in reverse and update the maxLength when left and right parenthesis count
     * are equal. But reset both counts when left > right.
     * <p>
     * Observation:
     * 1. We can iterate the string and keep track the count of valid pair of parentheses. When we have the equal
     * number of left and right parentheses, increment the length by 2. If we encounter an extra right parenthesis,
     * which makes right more than left. It means the current valid parentheses sequence has to stop, and the count
     * should also be reset. On the other hand, we want to extend the sequence when we have more left parenthesis
     * than the right. Cuz we may encounter right parenthesis later to be paired w/ these left parenthesis, thus
     * making the sequence longer.
     * <p>
     * 2. However, the above approach may be too optimal at certain cases. In a case like "(((()))", it won't detect
     * the valid parentheses sequence after the first redundant left parenthesis, cuz the number of left and right
     * is never equal. Therefore, we need something more to compensate the logic. If we iterate the string in reversed
     * order and this time we still apply similar logic but we naturally change to accept more right parenthesis
     * than left. This will help us to detect the aforementioned parentheses sequence.
     * <p>
     * Algo:
     * 1. Use left and right to track the total count of left and right parenthesis
     * 2. Iterate the string and update the corresponding count
     * - If left == right, update the maxLength
     * - If right > left, reset both left and right count
     * 3. Reset both left and right count after the first iteration
     * 4. Iterate the string in REVERSED order and update the corresponding count
     * - If left == right, update the maxLength
     * - If left > right , reset both left and right count
     * 5. return the maxLength
     * <p>
     * Time complexity: O(n)
     * Space complexity: O(1)
     */
    int longestValidParentheses(String s) {
        int left = 0, right = 0, maxlength = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(')
                left++;
            else
                right++;
            if (left == right)
                maxlength = Math.max(maxlength, left * 2);
            else if (right > left) {
                // Trigger the reset when we have more right than left
                // When we scan from left to right and have more right parenthesis than left parenthesis, e.g. "())"
                // there is no way we can extend the current parenthesis pair length. However, it is possible if we have
                // more left than right, cuz we may encounter one or more right parentheses later.
                left = right = 0;
            }
        }
        // In the first iteration we naively hope we will eventually find the right parenthesis to pair the extra left one(s).
        // So it is possible that a true longest parentheses string preceding a lonely left parenthesis will never be detected,
        // cuz left != right and reset condition is not triggered either.
        // Ex: For "()((())", maxlength will be 2 after the first iteration.
        // So we need to iterate in reversed direction to detect the longest parentheses and use the opposite condition to reset.
        left = right = 0; // Reset the left and right before the 2nd reverse iteration
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '(')
                left++;
            else
                right++;
            if (left == right)
                maxlength = Math.max(maxlength, left * 2);
            else if (left > right) {
                // Trigger the reset when we have more left than right(This is DIFFERENT from the above iteration)
                // When we scan from right to left and have more left parenthesis than right parenthesis, e.g. "(()"
                // there is no way we can extend the current parenthesis pair length. However, it is possible if we have
                // more right than left, cuz we may encounter one or more left parentheses later.
                // This condition is the OPPOSITE to the one when iterating from left to right.
                left = right = 0;
            }
        }
        return maxlength;
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
     * Remove Duplicate Letters
     * Given a string s, remove duplicate letters so that every letter appears once and only once.
     * You must make sure your result is
     * the smallest in lexicographical order among all possible results.
     * <p>
     * Input: s = "bcabc"
     * Output: "abc"
     * <p>
     * Input: s = "cbacdcbc"
     * Output: "acdb"
     * <p>
     * https://leetcode.com/problems/remove-duplicate-letters/description/
     */
    @Test
    void testRemoveDuplicateLetters() {
        assertThat(removeDuplicateLetters("bcabc")).isEqualTo("abc");
        assertThat(removeDuplicateLetters("cbacdcbc")).isEqualTo("acdb");
        assertThat(removeDuplicateLetters("abacb")).isEqualTo("abc");
    }

    /**
     * Build the map of char to index of its last occurrence in the str. Use a set to track the char in the
     * stack. Iterate the str if the current char is not in the set, first pop out char(s) in the stack if it is
     * larger than the current char and will occur later. Push the current char to the stack and update
     * the set. Finally build the answer from char on the stack in reverse order.
     * <p>
     * Observation:
     * 1. Which string is greater depends on the comparison between the first unequal corresponding character
     * in the two strings. As a result any string beginning with a will always be less than any string beginning
     * with b, regardless of the ends of both strings. Hence, the optimal solution will have the smallest
     * characters as early as possible. Ex: azyx is smaller than bacd in lexicographical order.
     * <p>
     * 2. If there are multiple smallest letters, then we pick the leftmost one simply because it gives us
     * more options and more chance to get a optimal solution.
     * <p>
     * 3. As we iterate over our string, if character[i] is greater than character[i-1] and another occurrence
     * of character[i-1] exists later in the string, deleting character i will always lead to the optimal solution.
     * Ex: input: bcabc
     * Say when we iterate to 'a', we had "bc" in our building string. We can see we will encounter 'b' and 'c'
     * again in the later iteration. Cuz 'a' is smaller than 'b' and 'c', we should drop them and put 'a' as the
     * first char. 'b' and 'c' will be added later regardless, and the order of them being inserted doesn't really
     * matter. Cuz as long as 'a' is at the first char, it will beat other string beginning with the bigger char.
     * <p>
     * 4. When we need to look backward or check the previous visited elements while iterating, and the computed
     * result also depends on that decision. Stack is a good data structure candidate in this case.
     * <p>
     * Algo:
     * 1. Build the map of char to index of its last occurrence in the string
     * 2. Use the Stack to store the building string, and a Set to store the char added to the stack.
     * 3. Iterate the string char
     * - If current char is in the set, skip it.
     * - If current char is smaller than top char on the stack, and we will encounter the same char at later iteration
     * (checking the charToLastIdx map), keep popping such char from the stack. Also update the Set.
     * - Push the current char to the stack and update the stack.
     * 4 Build the final answer by iterating the stack in reverse order.
     * <p>
     * Time complexity : O(N)
     * Space complexity : O(N)
     */
    String removeDuplicateLetters(String s) {
        // Keep track of what char is already added to the stack, so we can know this in O(1) to avoid duplicate char
        // inserted to the stack.
        Set<Character> charOnStack = new HashSet<>();
        // This helps us know if the given char will be seen again while iterating the array
        Map<Character, Integer> charToLastIdx = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            charToLastIdx.put(s.charAt(i), i);
        }
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);
            if (charOnStack.contains(currentChar))
                // current char is on the stack, we can't have duplicate so skip it
                continue;
            // Before we push the current char to the stack, we need to remove the top char on the stack if both condition meet
            // 1. Current char is smaller than top char. Cuz we want to put the smaller char in the front if possible.
            //    Ex: "ab" is better than "ba"
            // 2. We can only remove the top char for the case (1) ONLY if we know we will see this char again. Every
            //    unique char must be added to the stack at once in the end per requirement.
            while (!stack.isEmpty() && stack.peek() > currentChar // current char is smaller
                    && charToLastIdx.get(stack.peek()) > i) { // we will see the top char later
                Character topChar = stack.pop();
                charOnStack.remove(topChar);
            }
            stack.push(currentChar);
            charOnStack.add(currentChar);
        }

        StringBuilder stb = new StringBuilder();
        // Build the str by iterating the stack in reversed order
        while (!stack.isEmpty())
            stb.append(stack.removeLast());
        return stb.toString();
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
     * Use two ptrs(l, r) to iterate the array and maintain a sliding window. Also use a map to track the
     * char and last seen index. For each char, if it is found at map and its index(i) is bigger than l ptr,
     * set the l ptr to i+1. Update the max length to the current window size, and put current char and index
     * to the map.
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
        for (int left = 0, right = 0; right < length; right++) { // left and right represent the left and right side of the sliding window
            char currChar = s.charAt(right);
            if (charToIndex.containsKey(currChar) && charToIndex.get(currChar) >= left)
                // Shrink the current window, left.e. move left ptr forward, ONLY when
                // 1. We saw the same char before
                // 2. Its last-seen index is inside the current window range, left.e. index >= left. ---> IMPORTANT!!
                //    We only move the left forward inside the current window range, any index val outside of if is useless.
                //    Ex. input: abba
                //        When the window is at "ba", left=2, right=3, the map still has a->0, so we should ignore it.
                left = charToIndex.get(currChar) + 1; // Move the left ptr. We need to move it one more position to skip this duplicate char
            maxLength = Math.max(maxLength, right - left + 1);
            charToIndex.put(currChar, right); // Update the last seen index for this char
        }
        return maxLength;
    }

    /**
     * Longest Repeating Character Replacement
     * You are given a string s and an integer k. You can choose any character of
     * the string and change it to any other uppercase English character. You can
     * perform this operation at most k times.
     * <p>
     * Return the length of the longest substring containing the same letter you can
     * get after performing the above operations.
     * <p>
     * Input: s = "ABAB", k = 2
     * Output: 4
     * Explanation: Replace the two 'A's with two 'B's or vice versa.
     * <p>
     * Input: s = "AABABBA", k = 1
     * Output: 4
     * Explanation: Replace the one 'A' in the middle with 'B' and form "AABBBBA".
     * The substring "BBBB" has the longest repeating letters, which is 4.
     * There may exists other ways to achieve this answer too.
     * https://leetcode.com/problems/longest-repeating-character-replacement/description/
     */
    @Test
    void testCharacterReplacement() {
        assertThat(characterReplacement("ABAB", 2)).isEqualTo(4);
        assertThat(characterReplacement("AABABBA", 1)).isEqualTo(4);
        assertThat(characterReplacement("BAAAB", 2)).isEqualTo(5);
    }

    /**
     * Maintain a charToCount map and use sliding window to iterate the string. Each time we move
     * right ptr, check if the current window is valid(winSize - max count in the map <= k). If so,
     * update the max length, otherwise, move the left ptr and decrement the left char in the map
     * <p>
     * Observation:
     * If we maintain a sliding window over the string s. The substring in the window is valid
     * if the count of the most occurring char + k = size of the substring. In other words,
     * the remaining k chars in the substring can be replaced w/ the most occurring char.
     * Ex: k=2, AABC, AABB, ABAB are all valid substring
     * <p>
     * 1. Maintain the left ptr for the sliding window
     * 2. Maintain a char to count map
     * 3. Maintain max length of valid window/substring
     * 4. Iterate the str w/ right ptr
     * - Increment the char count in the map
     * - Get the count of the most occurring char from the map(Collections.max(charToCount.values()))
     * - If the window is valid (window size - maxCount) <= k
     * --  Update the max length of valid window
     * - Else
     * --  Drop/Decrement count of the left ptr char in the map
     * --  left ptr ++ (The window will move to the right by 1 at next iteration)
     * <p>
     * Time complexity : O(n)
     * Map has max 26 entries, so finding max is O(1)
     * Space complexity : O(m), m is 26
     */
    int characterReplacement(String s, int k) {
        Map<Character, Integer> charToCount = new HashMap<>();
        int left = 0;
        int maxLen = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            charToCount.put(c, charToCount.getOrDefault(c, 0) + 1);
            int winSize = right - left + 1;
            // window is valid if window size - MAX char count in the window <= k
            // It means there are enough replacement quota to replace reaming chars
            boolean isWindowValid = winSize - Collections.max(charToCount.values()) <= k;
            if (!isWindowValid) {
                // Window is invalid now, so move the left ptr ONCE. Then at the next iteration the window w/ the same
                // size will technically shift right by one. Although it is possible that the shifted window will
                // still be invalid, it is ok cuz we search for the max valid window. Making a valid window equal or
                // smaller than the previous max window we've seen won't be helpful, so we don't keep moving the left
                // ptr here to get a valid window. We just shift the window a bit and see if it will become valid and
                // expand further afterward.
                char leftChar = s.charAt(left);
                Integer count = charToCount.get(leftChar);
                // Drop the leftChar count in the map
                if (count == 1)
                    charToCount.remove(leftChar);
                else
                    charToCount.put(leftChar, count - 1);
                // Advance left ptr
                left++;
            } else {
                // Window is valid, update the max win size
                maxLen = Math.max(maxLen, winSize);
            }
        }
        return maxLen;
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
        assertThat(minWindow("cabwefgewcwaefgcf", "cae")).isEqualTo("cwae");
        assertThat(minWindow("ADOBECODEBANC", "ABC")).isEqualTo("BANC");
        assertThat(minWindow("a", "a")).isEqualTo("a");
        assertThat(minWindow("aaaaaaaaaaaabbbbbcdd", "abcdd")).isEqualTo("abbbbbcdd");
    }


    /**
     * Use Sliding window to iterate the string s. We need to keep two Maps(char->count) for the
     * str t and str in the current window. When iterating the str s, first keep moving the right
     * ptr to expand the window until the window has all the desired characters from str t, then
     * we update the min window size and min substring. Then start to shrink the window(move the
     * left ptr) and update the window map until it doesn't satisfy the required characters.
     * <p>
     * Note:
     * To determine if the charToCountWin map satisfy the charToCount map (from string t),
     * the naive way is to iterate charToCount and check if charToCountWin has the key and has
     * equal or greater count. But this is slow.
     * <p>
     * More efficient way is to maintain a matchEntryCount(init: 0). After we update the
     * charToCountWin for the current char, if the count of current char in charToCountWin equals
     * charToCount, we increment matchEntryCount. When the matchEntryCount equals the size
     * or charToCount, charToCountWin satisfies charToCount.
     * When we advance the left ptr, we need to check after updating the charToCountWin, if
     * the left ptr char count inh charToCountWin becomes less than charToCount, we need to
     * decrement matchEntryCount.
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
     * For each char in the string, first use it as center and two pointers at that index to expand toward
     * both sides to find the max length of palindrome. We need to do the similar thing to cover the
     * even-sized palindrome, so this time we have two pointers starting at two adjacent index. Finally,
     * the max palindrome is the greater of two.
     * <p>
     * The idea is to check for palindrome, we use two ptr, right and left, to move toward two ends of the
     * string at the same time.
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
     * Iterate the str and build the char->count HashMap/array[128], then sum up the count number that
     * can be paired in the map. If the sum is less than str length, which means not all chars in str are
     * completely paired, we add one to the sum to account for the center unique char in the palindrome str.
     * <p>
     * After we make the charToCount Map, the first thing we need to know is the total number of pairs of
     * the same char. This will first give us an even-length palindrome string. When we iterate the map,
     * one trick we can use is to use the formula: count / 2 * 2.
     * Cuz we do the integer division, for the odd count, this is the same as count-1. For the even count,
     * it has no effect. Alternative way is to use mod operation to check if it is odd or even.
     * <p>
     * Ex: if we have 'aaaaa', then we could have 'aaaa' partnered, which is 5 / 2 * 2 = 4 letters partnered
     * <p>
     * However, to maximize the palindrome length, if there is char occurring odd number times, we can include
     * it in the center of the palindrome. We can simply compare the total we just came up w/ the length
     * of str. If they are not equal, it means the chars in str are not completely paired, and there must
     * be odd number char count. So we can just add one to the sum.(We don't care what char is)
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
            // For Even number count: no effect, Odd number count: take count - 1
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

    /**
     * Check Whether Two Strings are Almost Equivalent
     * Two strings word1 and word2 are considered almost equivalent if the differences between
     * the frequencies of each letter from 'a' to 'z' between word1 and word2 is at most 3.
     * <p>
     * Given two strings word1 and word2, each of length n, return true if word1 and word2 are
     * almost equivalent, or false otherwise.
     * <p>
     * The frequency of a letter x is the number of times it occurs in the string.
     * <p>
     * Input: word1 = "aaaa", word2 = "bccb"
     * Output: false
     * Explanation: There are 4 'a's in "aaaa" but 0 'a's in "bccb".
     * The difference is 4, which is more than the allowed 3.
     * <p>
     * Input: word1 = "abcdeef", word2 = "abaaacc"
     * Output: true
     * Explanation: The differences between the frequencies of each letter in word1 and word2
     * are at most 3:
     * - 'a' appears 1 time in word1 and 4 times in word2. The difference is 3.
     * - 'b' appears 1 time in word1 and 1 time in word2. The difference is 0.
     * - 'c' appears 1 time in word1 and 2 times in word2. The difference is 1.
     * - 'd' appears 1 time in word1 and 0 times in word2. The difference is 1.
     * - 'e' appears 2 times in word1 and 0 times in word2. The difference is 2.
     * - 'f' appears 1 time in word1 and 0 times in word2. The difference is 1.
     * https://leetcode.com/problems/check-whether-two-strings-are-almost-equivalent/description/
     */
    @Test
    void testCheckAlmostEquivalent() {
        assertThat(checkAlmostEquivalent("aaaa", "bccb")).isFalse();
        assertThat(checkAlmostEquivalent("abcdeef", "abaaacc")).isTrue();
    }

    /**
     * Iterate the both strings and store the char count at an 26-sized array or charToCount Map,
     * For the char of the word1, increment the count, while for word2, decrement the count.
     * Finally, iterate the array or map and if the absolute value of count > 3, return false,
     * otherwise, return true in the end.
     * Time Complexity: O(N), where N is the length of w1/w2. We need to count each letter.
     * Space Complexity: O(1), the space for our count, as the alphabet size of s is fixed.
     */
    boolean checkAlmostEquivalent(String word1, String word2) {
        int[] charCount = new int[26];
        for (int i = 0; i < word1.length(); i++) {
            charCount[word1.charAt(i) - 'a'] += 1;
            charCount[word2.charAt(i) - 'a'] -= 1;
        }
        for (int count : charCount) {
            if (Math.abs(count) > 3) {
                return false;
            }
        }
        return true;
    }

    /**
     * Remove Colored Pieces if Both Neighbors are the Same Color
     * There are n pieces arranged in a line, and each piece is colored either by 'A' or by 'B'.
     * You are given a string colors of length n where colors[i] is the color of the ith piece.
     * <p>
     * Alice and Bob are playing a game where they take alternating turns removing pieces from
     * the line. In this game, Alice moves first.
     * <p>
     * Alice is only allowed to remove a piece colored 'A' if both its neighbors are also
     * colored 'A'. She is not allowed to remove pieces that are colored 'B'.
     * Bob is only allowed to remove a piece colored 'B' if both its neighbors are also colored
     * 'B'. He is not allowed to remove pieces that are colored 'A'.
     * <p>
     * Alice and Bob cannot remove pieces from the edge of the line.
     * If a player cannot make a move on their turn, that player loses and the other player wins.
     * Assuming Alice and Bob play optimally, return true if Alice wins, or return false if
     * Bob wins.
     * <p>
     * Input: colors = "AAABABB"
     * Output: true
     * Explanation:
     * AAABABB -> AABABB
     * Alice moves first.
     * She removes the second 'A' from the left since that is the only 'A' whose neighbors are both 'A'.
     * <p>
     * Now it's Bob's turn.
     * Bob cannot make a move on his turn since there are no 'B's whose neighbors are both 'B'.
     * Thus, Alice wins, so return true.
     * <p>
     * Input: colors = "AA"
     * Output: false
     * Explanation:
     * Alice has her turn first.
     * There are only two 'A's and both are on the edge of the line, so she cannot move on her turn.
     * Thus, Bob wins, so return false.
     * <p>
     * Input: colors = "ABBBBBBBAAA"
     * Output: false
     * Explanation:
     * ABBBBBBBAAA -> ABBBBBBBAA
     * Alice moves first.
     * Her only option is to remove the second to last 'A' from the right.
     * <p>
     * ABBBBBBBAA -> ABBBBBBAA
     * Next is Bob's turn.
     * He has many options for which 'B' piece to remove. He can pick any.
     * <p>
     * On Alice's second turn, she has no more pieces that she can remove.
     * Thus, Bob wins, so return false.
     */
    @Test
    void testWinnerOfGame() {
        assertThat(winnerOfGame("AAABABB")).isTrue();
        assertThat(winnerOfGame("AA")).isFalse();
    }

    /**
     * Iterate the string from index 1, and for each char, check if colors[i - 1] == colors[i]
     * == colors[i + 1]. If so, then increment either Alice or Bob's available moves depend on
     * the char. Alice wins if her move is greater than Bob's.
     * <p>
     * Observation:
     * 1. When one player removes a letter, it will never create a new removal opportunity for
     * the other player. Cuz the rule says only the middle char in a sequence of three same char
     * can be removed, so it won't create new removable possibility for the other char after it
     * is removed. For example, "BA[A]AB". This means at the start of the game, all moves are
     * already available to both players.
     * <p>
     * 2. The order in which the removals happen is irrelevant. Given the first observation,
     * we know each player's move is independent and won't affect the other. However, we know
     * Alice must go first, so she must need to be able to make one more move than Bob to win.
     * <p>
     * Time complexity: O(n)
     * Space complexity: O(1)
     */
    boolean winnerOfGame(String colors) {
        int aliceMove = 0;
        int bobMove = 0;
        for (int i = 1; i < colors.length() - 1; i++) {
            if (colors.charAt(i - 1) == colors.charAt(i)
                    && colors.charAt(i) == colors.charAt(i + 1)) {
                if (colors.charAt(i) == 'A') {
                    aliceMove++;
                } else {
                    bobMove++;
                }
            }
        }
        // Alice goes first, so she needs to have at least one more move available to win. If both have the same
        // number of moves, Bob wins.
        return aliceMove > bobMove;
    }
}
