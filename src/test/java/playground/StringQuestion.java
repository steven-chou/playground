package playground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            int pop = x % 10;
            x /= 10;
            // MAX_VALUE = 2147483647, MIN_VALUE = -2147483648
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
     * https://leetcode.com/problems/valid-anagram/solution/
     */
    @Test
    void testIsAnagram() {
        Assertions.assertTrue(isAnagramUnicode("dog", "god"));
        Assertions.assertTrue(isAnagram("anagram", "nagaram"));
        Assertions.assertFalse(isAnagram("rat", "car"));
    }


    //Time Complexity: O(N). Space Complexity: O(1)
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

    //Time Complexity: O(N). Space Complexity: O(1)
    /*
    Set two pointers, one at each end of the input string
    If the input is palindromic, both the pointers should point to equivalent characters, at all times.
    If this condition is not met at any point of time, we break and return early.
    We can simply ignore non-alphanumeric characters by continuing to traverse further.
    Continue traversing inwards until the pointers meet in the middle.
     */
    boolean isPalindrome(String s) {
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            while (i < j && !Character.isLetterOrDigit(s.charAt(i))) {
                i++;
            }
            while (j > i && !Character.isLetterOrDigit(s.charAt(j))) {
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
        Assertions.assertEquals(42, atoi("42"));
        Assertions.assertEquals(-42, atoi("-42"));
        Assertions.assertEquals(4193, atoi("4193 with words"));
        Assertions.assertEquals(0, atoi("words and 987"));
    }


    //Time Complexity: O(N). Space Complexity: O(1)
    /*
    Discard all the whitespaces at the beginning of the string.
    There could be an optional sign of a numerical value +/-+/−. It should be noted that the integer is positive by default if there is no sign present and there could be at most one sign character.
    Build the result using the above algorithm until there exists a non-whitespace character that is a number (00 to 99). Simultaneously, check for overflow/underflow conditions at each step.
     */
    int atoi(String str) {
        int i = 0;
        int sign = 1;
        int result = 0;
        if (str.length() == 0)
            return 0;

        //Discard whitespaces in the beginning
        while (i < str.length() && str.charAt(i) == ' ')
            i++;

        // Check if optional sign if it exists
        if (i < str.length() && (str.charAt(i) == '+' || str.charAt(i) == '-'))
            sign = (str.charAt(i++) == '-') ? -1 : 1;

        // Build the result and check for overflow/underflow condition
        while (i < str.length() && Character.isDigit(str.charAt(i))) {
            if (result > Integer.MAX_VALUE / 10 ||
                    (result == Integer.MAX_VALUE / 10 && str.charAt(i) - '0' > Integer.MAX_VALUE % 10)) {
                return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            result = result * 10 + (str.charAt(i++) - '0');
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
        Assertions.assertEquals(0, strStr("", ""));

        Assertions.assertEquals(2, rabinKarpStrStr("hello", "ll"));
    }

    // Time Complexity: O((N-L)L), where N is a length of haystack and L is a length of needle. We compute a substring of length L in a loop, which is executed (N - L) times.
    // Space Complexity: O(1)
    int strStr(String haystack, String needle) {
        int subStrLen = needle.length(), strLen = haystack.length();

        for (int start = 0; start < strLen - subStrLen + 1; start++) {
            if (haystack.substring(start, start + subStrLen).equals(needle)) {
                return start;
            }
        }
        return -1;
    }

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
        Assertions.assertEquals("fl", longestCommonPrefixHorizontalScan(new String[]{"flower", "flow", "flight"}));
        Assertions.assertEquals("", longestCommonPrefixHorizontalScan(new String[]{"dog", "racecar", "car"}));
        Assertions.assertEquals("fl", longestCommonPrefixVerticalScan(new String[]{"flo", "flower", "fl"}));
    }

    // Time Complexity: O(S), where S is the sum of all characters in all strings.
    // Even though the worst case is still the same as HorizontalScan, in the best case there are at most n*minLen comparisons where minLen is the length of the shortest string in the array.
    // Space Complexity: O(1). We only used constant extra space.
    String longestCommonPrefixVerticalScan(String[] strs) { // ---> Better
        if (strs == null || strs.length == 0)
            return "";
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (i == strs[j].length() || strs[j].charAt(i) != c)
                    return strs[0].substring(0, i);
            }
        }
        return strs[0];
    }

    // Time Complexity: O(S), where S is the sum of all characters in all strings.
    // Space Complexity: O(1). We only used constant extra space.
    String longestCommonPrefixHorizontalScan(String[] strs) {
        if (strs.length == 0)
            return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++)
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty())
                    return "";
            }
        return prefix;
    }


}
