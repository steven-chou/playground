package playground;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionTestsOne {


    /**
     * PowerSet - all the subsets of a given set.
     * Ex: {"A", "B", "C"}, the power set are : {{"A"}, {"A", "B"}, {"B"}, {"A", "C"}, {"A", "B", "C"}, {"B", "C"}, {"C"}, {}}
     * https://stackoverflow.com/questions/24365954/how-to-generate-a-power-set-of-a-given-set
     */
    @Test
    void powerSet() {
        System.out.println(powerSet(List.of("A", "B", "C")));
    }

    private static <T> List<List<T>> powerSet(List<T> input) {
        List<List<T>> results = new ArrayList<>();
        for (T element : input) {

            ListIterator<List<T>> resultsIterator = results.listIterator();
            while (resultsIterator.hasNext()) {
                // Create a new List with copied List and add a new item
                List<T> newSet = new ArrayList<>(resultsIterator.next());
                newSet.add(element);
                resultsIterator.add(newSet);
            }

//            for (ListIterator<List<T>> setsIterator = results.resultsIterator(); setsIterator.hasNext(); ) {
//                List<T> newSet = new ArrayList<>(setsIterator.next());
//                newSet.add(element);
//                setsIterator.add(newSet);
//            }
            // Add the single item set {"A"}, {"B"}, {"C"}
            results.add(new ArrayList<>(List.of(element)));
        }
        results.add(new ArrayList<>());
        return results;
    }

    /**
     * Write an algorithm that prints all numbers between 1 and n, replacing multiples
     * of 3 with the String Fizz, multiples of 5 with Buzz, and multiples of 15 with
     * FizzBuzz.
     */
    @Test
    void fizzBuzz() {
        System.out.println(solveFizzBuzz(5));
    }

    private static List<String> solveFizzBuzz(final int n) {
        final List<String> result = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            final String word = toWord(3, i, "Fizz") + toWord(5, i, "Buzz");
            if (StringUtils.isEmpty(word)) {
                result.add(Integer.toString(i));
            } else {
                result.add(word);
            }
        }
        return result;
    }

    private static String toWord(final int divisor, final int value, final String word) {
        return value % divisor == 0 ? word : "";
    }

    /**
     * Returns a Fibonacci sequence from 1 to n.
     * The Fibonacci sequence is a list of numbers, where the next value in the sequence is the sum of the
     * previous two.
     */
    @Test
    void fibonacciSequence() {
        System.out.println(fibonacciList(8));
        // Expected (0, 1, 1, 2, 3, 5, 8, 13)
    }

    public static List<Integer> fibonacciList(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be less than zero");
        }
        if (n == 0) {
            return new ArrayList<>();
        }
        if (n == 1) {
            return List.of(0);
        }
        if (n == 2) {
            return List.of(0, 1);
        }

        final List<Integer> seq = new ArrayList<>(n);
        seq.add(0);
        n = n - 1;
        seq.add(1);
        n = n - 1;
        while (n > 0) {
            int a = seq.get(seq.size() - 1);
            int b = seq.get(seq.size() - 2);
            seq.add(a + b);
            n = n - 1;
        }
        return seq;
    }

    /**
     * === Dynamic Programming ===
     * Return the nth value of a Fibonacci sequence
     * Time Complexity: O(n)
     * Memory Complexity: O(1)
     */
    @Test
    void fibonacciNumber() {
        System.out.println(calculateFibonacciNumber(7));
    }

    private int calculateFibonacciNumber(int n) {
        if (n == 0)
            return 0;
        int a = 0;
        int b = 1;
        for (int i = 2; i < n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return a + b;
    }


    /**
     * Get factorial
     */
    @Test
    void factorial() {
        System.out.println(calculateFactorial(5));
    }

    private long calculateFactorial(int n) {
        long result = 1L;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Given two strings s and t, write a function to determine if t is an anagram of s.
     */
    @Test
    void checkAnagram() {
        System.out.println(isAnagram("dog", "god"));
    }

    private boolean isAnagram(String s, String t) {

        s = s.toLowerCase();
        t = t.toLowerCase();

        if (s.length() != t.length()) {
            return false;
        }
        int[] table = new int[26]; // Or maybe use a Map instead??
        for (int i = 0; i < s.length(); i++) {
            // " -'a' " is to calculate the correct index for the hash table.
            // Since the table is of size 26, the range of values for the index is 0-25, inclusive, but a lowercase 'a' has an ascii value of 97,
            // thus we have to subtract 'a' from whatever the current character is in the string to get within 0-25.
            table[s.charAt(i) - 'a']++;
        }
        for (int i = 0; i < t.length(); i++) {
            table[t.charAt(i) - 'a']--;
            if (table[t.charAt(i) - 'a'] < 0) {
                return false;
            }
        }
        return true;
    }

    int atoi(String str) {
        int idx = 0;
        int result = 0;
        int sign = 1;
        if (str.length() == 0)
            return 0;
        // Remove leading space
        while (idx < str.length() && str.charAt(idx) == ' ') {
            idx++;
        }
        if (str.charAt(idx) == '+' || str.charAt(idx) == '-') {
            sign = str.charAt(idx) == '+' ? 1 : -1;
        }
        while (idx < str.length()) {
            result = result * 10 + str.charAt(idx) - '0';
            ++idx;
        }
        return result * sign;

    }

}
