package playground;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/* TODO- Useful Tips:
     - Take each digit of an integer from the last digit one-by-one
        (1) Use mod
        int num = 9876;
        while (num != 0) {
            int lastDigit = num % 10; // mod gives us last digit
            num = num / 10; // divide by 10 drops the last digit
            // Loop will end when num < 10 and divide by 10 --> num = 0
        }
        If we want the same order as the original integer, we can push the lastDigit to a stack first, and pop it out
        after the while loop
        (2) Use built-in API
        int[] array = String.valueOf(num).chars().map(Character::getNumericValue).toArray();
        List<Integer> list = String.valueOf(num).chars().map(Character::getNumericValue).boxed().collect(Collectors.toList());
     - Append an int digit to the end of an integer, e.g. Append 2 to 1 --> 12
       int myInt = 1, digit = 2;
       myInt = myInt * 10 + digit; // myInt: 12
     - Checking if adding a digit to an integer will cause overflow/underflow
       When myInt is positive:
       myInt > Integer.MAX_VALUE / 10 || (myInt == Integer.MAX_VALUE / 10 && lastDigit > Integer.MAX_VALUE % 10)
       When myInt is negative:
       myInt < Integer.MIN_VALUE / 10 || (myInt == Integer.MIN_VALUE / 10 && lastDigit < Integer.MIN_VALUE % 10)
       This technique is used in the problem "String to Integer (atoi)" and "Reverse Integer"
 *
 */
public class MathQuestion {

    /**
     * Fizz Buzz
     * https://leetcode.com/problems/fizz-buzz/description/
     */
    @Test
    void testFizzBuzz() {
        List<String> output = fizzBuzz(5);
        Assertions.assertThat(output).containsExactly("1", "2", "Fizz", "4", "Buzz");
    }

    /**
     * Iterate i from 1 to n, init str to "",
     * if i is divisible by 3 (i%3 == 0), append Fizz
     * if i is divisible by 5 (i%5 == 0), append Buzz
     * if str is still empty, append i
     * Add str to the answer list.
     * Time Complexity: O(n). Space Complexity: O(1)
     */
    List<String> fizzBuzz(int n) {
        List<String> ans = new ArrayList<>();
        for (int num = 1; num <= n; num++) {
            boolean divisibleBy3 = (num % 3 == 0);
            boolean divisibleBy5 = (num % 5 == 0);

            String numAnsStr = "";
            if (divisibleBy3) {
                // Divides by 3, add Fizz
                numAnsStr += "Fizz";
            }
            if (divisibleBy5) {
                // Divides by 5, add Buzz
                numAnsStr += "Buzz";
            }
            if (numAnsStr.isEmpty()) {
                // Not divisible by 3 or 5, add the number
                numAnsStr += Integer.toString(num);
            }

            // Append the current answer str to the ans list
            ans.add(numAnsStr);
        }
        return ans;
    }

    /**
     * Reverse Integer
     * Given a signed 32-bit integer x, return x with its digits reversed. If reversing x causes
     * the value to go outside the signed 32-bit integer range [-2^31, 2^31 - 1], then return 0.
     * Assume the environment does not allow you to store 64-bit integers (signed or unsigned).
     * <p>
     * Input: x = 123
     * Output: 321
     * <p>
     * Input: x = -123
     * Output: -321
     * <p>
     * Input: x = 120
     * Output: 21
     * https://leetcode.com/problems/reverse-integer/solution/
     * EPI 4.8
     */
    @Test
    void testReverseInt() {
        assertThat(reverseInt(123)).isEqualTo(321);
        assertThat(reverseInt(-123)).isEqualTo(-321);
        assertThat(reverseInt(120)).isEqualTo(21);
    }

    /**
     * While x is not equal 0,
     * - do x % 10 to get the last digit from x
     * - x = x/10 to drop the right most digit
     * - If adding the last digit to current answer will cause overflow, return 0
     * -- ans > Integer.MAX_VALUE / 10
     * --	or (ans == Integer.MAX_VALUE / 10 && lastDigit > Integer.MAX_VALUE % 10)
     * - If adding the last digit to current answer will cause underflow, return 0
     * -- ans < Integer.MIN_VALUE / 10
     * --  or (ans == Integer.MIN_VALUE / 10 && lastDigit < Integer.MIN_VALUE % 10)
     * - ans = ans * 10 + last digit
     * <p>
     * In summary, we repeatedly "pop" the last digit off of X and "push" it to the back
     * of the ans. In the end, ans will be the reverse of the X.
     * <p>
     * Time Complexity: O(log(x)). There are roughly log10(x) digits in x.
     * Space Complexity: O(1)
     */
    int reverseInt(int x) {
        int ans = 0;
        while (x != 0) {
            int lastDigit = x % 10; // mod operation gives us the right most digit
            x /= 10; // int div drops the right most digit, and the loop continues until it becomes zero
            /*
            MAX_VALUE = 2147483647, MIN_VALUE = -2147483648
            To explain the following check, lets assume that ans is positive.
                1. If temp = ans⋅10 + lastDigit causes overflow, then it must be that ans ≥ INT_MAX/10
                2. If ans > INT_MAX/10, then temp = ans⋅10 + lastDigit is guaranteed to overflow.
                3. If ans == INT_MAX/10, then temp = ans⋅10 + lastDigit will overflow if and only if lastDigit > 7
            Similar logic can be applied when ans is negative.
             */
            // Check overflow
            if (ans > Integer.MAX_VALUE / 10 || (ans == Integer.MAX_VALUE / 10 && lastDigit > Integer.MAX_VALUE % 10))
                return 0;
            // Check underflow
            if (ans < Integer.MIN_VALUE / 10 || (ans == Integer.MIN_VALUE / 10 && lastDigit < Integer.MIN_VALUE % 10))
                return 0;
            ans = ans * 10 + lastDigit; // This can cause an overflow, so we need above check
        }
        return ans;
    }

    /**
     * Plus One
     * You are given a large integer represented as an integer array digits, where each digits[i]
     * is the ith digit of the integer. The digits are ordered from most significant to least
     * significant in left-to-right order. The large integer does not contain any leading 0's.
     * <p>
     * Increment the large integer by one and return the resulting array of digits.
     * <p>
     * Input: digits = [1,2,3]
     * Output: [1,2,4]
     * Input: digits = [9]
     * Output: [1,0]
     * <p>
     * Input: digits = [9]
     * Output: [1,0]
     * Explanation: The array represents the integer 9.
     * Incrementing by one gives 9 + 1 = 10.
     * Thus, the result should be [1,0].
     * https://leetcode.com/problems/plus-one/solution/
     */
    @Test
    void testPlusOne() {
        int[] intArray1 = new int[]{4, 3, 2, 1};
        int[] intArray2 = new int[]{1, 2, 9};
        int[] intArray3 = new int[]{9, 9};
        assertThat(plusOne(intArray1)).containsExactly(4, 3, 2, 2);
        assertThat(plusOne(intArray2)).containsExactly(1, 3, 0);
        assertThat(plusOne(intArray3)).containsExactly(1, 0, 0);
    }

    /**
     * Iterate backward thru array and set the digit to 0 if it is 9, otherwise, increment the digit by 1 and
     * return the array. If the loop ends and not return yet, we return a new array w/ prepending 1.
     * <p>
     * Observation:
     * 1. When the rightmost digit is not 9, adding one to it won't result in any carry
     * 2. When the rightmost digit is 9, adding one to it will result in carry, and the carry will make the
     * preceding digit zero and result in another carry if it is also 9. Otherwise, the 1 will be just added
     * to that digit.
     * 3. Therefore, we can generalize that 1 will be added to the rightmost digit, which is not equal to 9,
     * and all its subsequent digits of nine should be set to 0.
     * <p>
     * Algo:
     * Iterate from the end of array.
     * If it is 9, set it to 0. (We want to set every 9 to 0 before we encounter any non-nine digit)
     * Else increase this rightmost not-nine by 1, then return the array.(We are done)
     * If it doesn't return after the iteration, that means all the digits were equal to nine.
     * So they have all been set to zero.
     * We then append the digit 1 in front of the other digits and return the result.
     * For example, 999 ---> 1000
     * Time Complexity: O(n).
     * Space Complexity: O(n)
     */
    int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            // Move from the end
            if (digits[i] == 9)
                // We set every 9 to 0 before we encounter any non-nine digit
                digits[i] = 0;
            else {
                // Here we encounter the rightmost not-nine digit, so we increase this by 1
                digits[i]++;
                // We are done here!
                return digits;
            }
        }
        // We're here because all the digits were equal to nine, and now they have all been set to zero.
        // So we need to append the digit 1 in front of the digits and return the result.
        // For example, 999 ---> 1000
        digits = new int[digits.length + 1]; // Make a new array, all default to 0
        digits[0] = 1;
        return digits;
    }

    /**
     * Count Primes
     * https://leetcode.com/problems/count-primes/editorial/
     */
    @Test
    void testCountPrimes() {
        Assertions.assertThat(countPrimes(10)).isEqualTo(4);
    }

    /**
     * Sieve of Eratosthenes algo
     * 1. Create a list of consecutive integers from 2 through n: (2, 3, 4, ..., n).
     * 2. Let p be the variable we use in the outer loop that iterates from 2 to square root n. Initially, let p equal 2, the smallest prime number.
     * 3. Enumerate the multiples of p by counting in increments of p from p*p to n, and mark them in the list (these will be p*p, p*p + p, p*p + 2*p, ...; p itself should be prime).
     * 4. Find the smallest number in the list greater than p that is not marked. If there was no such number, stop. Otherwise, let p now equal this new number (which is the next prime), and repeat from step 3.
     * When the algorithm terminates, all of the remaining numbers that are not marked are prime.
     */
    int countPrimes(int n) {
        if (n <= 2) // Checking 0 & 1
            return 0;
        boolean[] composites = new boolean[n]; // true: composite, false: prime
        for (int i = 2; i <= (int) Math.sqrt(n); i++) { // loop ends at square root of n
            if (!composites[i]) {
                // Mark all the multiples of i as true
                // The first index to be flipped to true is i*i
                for (int j = i * i; j < n; j += i) {
                    composites[j] = true;
                }
            }
        }

        int numberOfPrimes = 0;
        for (int i = 2; i < n; i++) {
            if (!composites[i])
                ++numberOfPrimes;
        }

        return numberOfPrimes;
    }

    /**
     * Power of Three
     * https://leetcode.com/problems/count-primes/editorial/
     */
    @Test
    void testIsPowerOfThree() {
        Assertions.assertThat(isPowerOfThree(27)).isTrue();
    }

    /**
     * To out if a number n is a power of a number b is to keep dividing n by b as long as the remainder is 0.
     * Hence it should be possible to divide n by b x times, every time with a remainder of 0 and the end result to be 1.
     * Time Complexity: O(log n). Space Complexity: O(1)
     */
    boolean isPowerOfThree(int n) {
        if (n < 1)
            return false;
        while (n % 3 == 0)
            n /= 3;

        return n == 1;
    }

    /**
     * Roman to Integer
     * https://leetcode.com/problems/roman-to-integer/editorial/
     */
    @Test
    void testRomanToInt() {
        Assertions.assertThat(romanToInt("III")).isEqualTo(3);
    }

    static Map<String, Integer> values = new HashMap<>();

    // total of 13 unique symbols
    static {
        values.put("I", 1);
        values.put("V", 5);
        values.put("X", 10);
        values.put("L", 50);
        values.put("C", 100);
        values.put("D", 500);
        values.put("M", 1000);
        values.put("IV", 4);
        values.put("IX", 9);
        values.put("XL", 40);
        values.put("XC", 90);
        values.put("CD", 400);
        values.put("CM", 900);
    }

    /**
     * Make a Map of String -> Integer with the 13 "symbols", Scan the string from left to right.
     * Firstly checking if we're at a length-2 symbol, and if not, then treating it as a length-1 symbol.
     * Time Complexity: O(1). Space Complexity: O(1)
     */
    int romanToInt(String s) {
        int sum = 0, i = 0;
        while (i < s.length()) {
            if (i < s.length() - 1) {
                String doubleSymbol = s.substring(i, i + 2);
                if (values.containsKey(doubleSymbol)) {
                    sum += values.get(doubleSymbol);
                    i += 2;
                    continue;
                }
            }
            String singleSymbol = s.substring(i, i + 1);
            sum += values.get(singleSymbol);
            i++;
        }
        return sum;
    }

    /**
     * Integer to Roman
     * https://leetcode.com/problems/integer-to-roman/description/
     */
    @Test
    void testIntToRoman() {
        Assertions.assertThat(intToRoman(58)).isEqualTo("LVIII");
    }

    private static final int[] vals = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    /**
     * Time Complexity: O(1). Space Complexity: O(1)
     */
    String intToRoman(int num) {
        StringBuilder sb = new StringBuilder();
        // Loop through each symbol, stopping if num becomes 0.
        for (int i = 0; i < vals.length && num > 0; i++) {
            // Repeat while the current symbol still fits into num. So we can use the same symbol multiple times as long
            // as it fits
            while (vals[i] <= num) {
                num -= vals[i];
                sb.append(symbols[i]);
            }
        }
        return sb.toString();
    }

    /**
     * Happy Number
     * Write an algorithm to determine if a number n is happy.
     * <p>
     * A happy number is a number defined by the following process:
     * <p>
     * Starting with any positive integer, replace the number by the sum of the squares of its digits.
     * Repeat the process until the number equals 1 (where it will stay), or it loops endlessly in a cycle which does not include 1.
     * Those numbers for which this process ends in 1 are happy.
     * Return true if n is a happy number, and false if not.
     * <p>
     * Input: n = 19
     * Output: true
     * Explanation:
     * 12 + 92 = 82
     * 82 + 22 = 68
     * 62 + 82 = 100
     * 12 + 02 + 02 = 1
     * <p>
     * Input: n = 2
     * Output: false
     */
    @Test
    void testIsHappy() {
        Assertions.assertThat(isHappy(19)).isTrue();
        Assertions.assertThat(isHappy(2)).isFalse();
        Assertions.assertThat(isHappyUseCycleFinding(19)).isTrue();
    }

    /**
     * Use "picking digits off one-by-one" to generate the sum of square digit and HashSet to detect the cycles
     * Alog needs to solve two problems
     * 1. Given a number n, what is its next number?
     * - Use the division and modulus operators to repeatedly take digits off the number until none remain,
     * and then squaring each removed digit and adding them together.
     * <p>
     * 2. Follow a chain of numbers and detect if we've entered a cycle.
     * - (1): Add each generated number above to HashSet and check it when we have a new one
     * - (2): Use the same approach of solving the problem of detecting if linked list has cycle.
     * -      At each step of the algorithm, the slow runner goes forward by 1 number in the chain, and the fast runner
     * -      goes forward by 2 numbers (nested calls to the getNext(n) function).
     * <p>
     * Time complexity : O(243⋅3 + log n + loglog n + logloglog n)... = O(log n).
     * Summing the digits of a number is O(log N), cuz it boilds down to the number of digits we have.
     * Let d be the number of digits in N, and we have the inequalities 10^(d-1) <= N < 10^d. After taking
     * logarithm we will have log(N) < d <= log(N) + 1, therefore O(log(N)).
     * https://stackoverflow.com/questions/50261364/explain-why-time-complexity-for-summing-digits-in-a-number-of-length-n-is-ologn/50262470#50262470
     * <p>
     * For the amount of time it takes for a number to reach 243 is O(log N) + O(log log N) + ..., so it can
     * be simplified to O(log n)
     * Once a number reaches the <= 243 threshold, the amount of type it takes to either a. discover a cycle or b. get to 1.
     * The worst case is O(3), cuz 243 calls of the getNextNumber, and we just replace it with the number of digits
     * Given above, time complexity is O(2⋅logn) = O(log n)
     * <p>
     * Space complexity: O(log n). If using the slow/fast runner solution, O(1)
     */
    boolean isHappy(int n) {
        Set<Integer> seenNumbers = new HashSet<>();
        while (n != 1 && !seenNumbers.contains(n)) { // until 1 or we see the same number again
            seenNumbers.add(n);
            n = getNextNumber(n);
        }
        return n == 1;
    }

    int getNextNumber(int num) {
        int sum = 0;
        while (num > 0) {
            int lastDigit = num % 10; // mod gives us last digit
            num = num / 10; // divide by 10 drops the last digit
            sum += lastDigit * lastDigit;
            // Loop will end when num < 10 and divide by 10 --> num = 0
        }
        return sum;
    }

    boolean isHappyUseCycleFinding(int n) {
        int slowRunner = n;
        int fastRunner = getNextNumber(n);
        while (fastRunner != 1 && slowRunner != fastRunner) {
            slowRunner = getNextNumber(slowRunner);
            fastRunner = getNextNumber(getNextNumber(fastRunner));
        }
        return fastRunner == 1;
    }

    /**
     * Factorial Trailing Zeroes
     * Given an integer n, return the number of trailing zeroes in n!.
     * <p>
     * Note that n! = n * (n - 1) * (n - 2) * ... * 3 * 2 * 1.
     * <p>
     * Input: n = 3
     * Output: 0
     * Explanation: 3! = 6, no trailing zero.
     * <p>
     * Input: n = 5
     * Output: 1
     * Explanation: 5! = 120, one trailing zero.
     * <p>
     * https://leetcode.com/problems/factorial-trailing-zeroes/editorial/
     */
    @Test
    void testTrailingZeroes() {
        Assertions.assertThat(trailingZeroes(3)).isEqualTo(0);
        Assertions.assertThat(trailingZeroes(5)).isEqualTo(1);
    }

    /**
     * Counting Factors of 5 by keep dividing n by 5 each time and add it to a running count
     * This is a PURE tricky math problem. The idea is instead of computing the factorial, we only need
     * to check the pairs of 2 and 5 cuz only 2*5 produces 0 in the end. However, there are always more 2 than
     * 5 in any factorial, so number of 5 will dominate the number of 10 we can have.
     * Thus we jsut need to look for the 5 factors.
     * <p>
     * To do this, the count of multiple of 5 up to n is
     * <p>
     * n/5 + n/25 + n/125 + n/625 + ...
     * <p>
     * We're using integer division. Eventually, the denominator will be larger than n, and so all the
     * terms from there will be 0. Therefore, we can stop once the term is 0.
     * And this formula can be rewritten as
     * <p>
     * n/5 + (n/5)/5 + ((n/5)/5)/5 + ...
     * <p>
     * So in the code we can divide n itself by 5 each time, and then adding that to a running fives count.
     * <p>
     * Time complexity : O(log n).
     * In this approach, we divide n by each power of 5. By definition, there are log n (base 5)
     * powers of 5 less-than-or-equal-to n.
     * <p>
     * Space complexity : O(1)
     */
    int trailingZeroes(int n) {
        int zeroCount = 0;
        while (n > 0) {
            n /= 5;
            zeroCount += n;
        }
        return zeroCount;
    }

    /**
     * Pow(x, n)
     * Implement pow(x, n), which calculates x raised to the power n (i.e., xn).
     * <p>
     * <p>
     * Input: x = 2.00000, n = 10
     * Output: 1024.00000
     * <p>
     * Input: x = 2.10000, n = 3
     * Output: 9.26100
     * <p>
     * Input: x = 2.00000, n = -2
     * Output: 0.25000
     * Explanation: 2-2 = 1/22 = 1/4 = 0.25
     * <p>
     * https://leetcode.com/problems/powx-n/description/
     */
    @Test
    void testMyPow() {
        Assertions.assertThat(myPowRecursive(2, 10)).isEqualTo(1024.00000);
        Assertions.assertThat(myPowIterative(2, 5)).isEqualTo(32.0);
    }

    /**
     * Use "exponentiating by squaring" to recursively divide the exponent by 2 and squaring the x
     * Binary exponentiation, also known as exponentiation by squaring, is a technique for efficiently
     * computing the power of a number. By repeatedly squaring x and halving n, we can quickly compute x^n
     * using a logarithmic number of multiplications. (https://en.wikipedia.org/wiki/Exponentiation_by_squaring)
     * <p>
     * The basic idea here is to use the fact that x^n can be expressed as:
     * <p>
     * (x^2)^n/2 if n is even
     * x⋅(x^2)^(n-1)/2 if n is odd (we separate out one x, then n−1 will become even)
     * <p>
     * Example: when calculating 2^10 (x=2, n=10)
     * The recursive call path is like
     * n=10   n=5    n=2       n=1       n=0
     * 2^10 = 4^5 = 4⋅16^2 = 4⋅256^1 = 4⋅256⋅65536^0
     * <p>
     * The recursive implementation is more intuitive to write and memorize than iterative one.
     * The idea for both is we want to get the result by finding the square of a BIG number and multiply
     * some numbers so we can significantly reduce the number fo multiplication.
     * <p>
     * Time complexity: O(log n)
     * At each recursive call we reduce n by half, so we will make only log n number of calls for the
     * squareAndMultiply function, and the multiplication of two numbers is considered as a constant
     * time operation. Thus, it will take overall O(log n) time.
     * <p>
     * Space complexity: O(log n)
     * The recursive stack can use at most O(log n) space at any time.
     */
    double myPowRecursive(double x, int n) {
        return squareAndMultiply(x, n);
    }

    double squareAndMultiply(double x, long n) {
        if (n == 0) // Base case
            return 1;
        if (x < 0) // Handle case where, n < 0.
            return 1 / squareAndMultiply(x, Math.abs(n));
        if (n % 2 != 0) // If 'n' is odd we perform Binary Exponentiation on 'n - 1' and multiply result with 'x'.
            return x * squareAndMultiply(x * x, (n - 1) / 2);
        else // Otherwise we calculate result by performing Binary Exponentiation on 'n'.
            return squareAndMultiply(x * x, n / 2);
    }

    /**
     * We will use a while loop which will continue until n reaches 0.
     * If n is odd then we will multiply x once with the result, so that we can reduce n by 1
     * to make it even. Now, n will be even, thus, we now square the x and reduce n by half,
     * i.e. x^n => (x^2)^(n/2)
     * <p>
     * Also, remember if n<0, then we need to find 1/x^(−n), thus we, multiply 1/x with each other not x.
     * <p>
     * Time complexity: O(log n)
     * At each iteration, we reduce n by half, thus it means we will make only (log n) number of
     * iterations using a while loop.
     * Thus, it will take overall O(log n) time.
     * <p>
     * Space complexity: O(1)
     * We don't use any additional space.
     */
    double myPowIterative(double x, long n) {
        if (n == 0)
            return 1;

        // Handle case where, n < 0.
        if (n < 0) {
            n = -1 * n;
            x = 1.0 / x;
        }

        // Perform Binary Exponentiation.
        double result = 1;
        while (n != 0) {
            // If 'n' is odd we multiply result with 'x' and reduce 'n' by '1'.
            if (n % 2 == 1) {
                // result var is only used here cuz
                // 1. The loop will always reach here at the last iteration, cuz we keep divide it by 2. Thus n will become 1
                //    eventually and after decrement it by one, n becomes zero then the loop terminates. So we update the result here.
                // 2. We keep squaring the x, so we need to use a var to store the running multiplication when n is odd and the final result.
                result = result * x;
                n -= 1;
            }
            // We square 'x' and reduce 'n' by half, x^n => (x^2)^(n/2).
            x = x * x;
            n = n / 2;
        }
        return result;
    }

    /**
     * Sqrt(x)
     * Given a non-negative integer x, return the square root of x rounded down to the nearest integer.
     * The returned integer should be non-negative as well.
     * <p>
     * You must not use any built-in exponent function or operator.
     * <p>
     * Input: x = 4
     * Output: 2
     * Explanation: The square root of 4 is 2, so we return 2.
     * <p>
     * Input: x = 8
     * Output: 2
     * Explanation: The square root of 8 is 2.82842..., and since we round it down to the nearest integer, 2 is returned.
     * <p>
     * https://leetcode.com/problems/sqrtx/description/
     */
    @Test
    void testMySqrt() {
        Assertions.assertThat(mySqrt(4)).isEqualTo(2);
        Assertions.assertThat(mySqrt(8)).isEqualTo(2);
        Assertions.assertThat(mySqrt(2)).isEqualTo(1);
        Assertions.assertThat(mySqrtWithTemplate(4)).isEqualTo(2);
        Assertions.assertThat(mySqrtWithTemplate(8)).isEqualTo(2);
        Assertions.assertThat(mySqrtWithTemplate(2)).isEqualTo(1);
        Assertions.assertThat(mySqrtWithTemplate(1)).isEqualTo(1);
        Assertions.assertThat(mySqrtWithTemplate(0)).isEqualTo(0);
        Assertions.assertThat(mySqrtWithTemplate(2147483647)).isEqualTo(46340);
    }

    /**
     * Use standard "Find first true" binary search template
     * The condition is for any number i squared >= x, i.e. mid^2 >= x
     */
    int mySqrt(int x) {
        int left = 0, right = x;
        int result = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            long midSquare = (long) mid * mid; // Cast to long in case of integer overflow
            if (midSquare >= x) {
                result = mid; // Keep searching the left side cuz there may be a better ans
                right = mid - 1;
            } else
                left = mid + 1;
        }
        // The result is either the exact square root of x or the ceiling of the square root of x
        return result * result == x ? result : result - 1;
    }

    /**
     * Use the special "Find first true" binary search template
     */
    int mySqrtWithTemplate(int x) {
        // Find first true
        int left = -1, right = x;
        while (left + 1 < right) {
            //int mid = left + (right - left) / 2; Do NOT use this. This will cause overflow.
            int mid = (left + right) >>> 1;
            long midSquare = (long) mid * mid; // Cast to long in case of integer overflow
            if (midSquare >= x)
                right = mid;
            else
                left = mid;
        }
        return (right * right == x) ? right : right - 1;
    }

    /**
     * Excel Sheet Column Number
     * Given a string columnTitle that represents the column title as appears in an Excel sheet,
     * return its corresponding column number.
     * <p>
     * For example:
     * <p>
     * A -> 1
     * B -> 2
     * C -> 3
     * ...
     * Z -> 26
     * AA -> 27
     * AB -> 28
     * ...
     * <p>
     * Input: columnTitle = "A"
     * Output: 1
     * <p>
     * Input: columnTitle = "AB"
     * Output: 28
     * <p>
     * https://leetcode.com/problems/excel-sheet-column-number/description/
     */
    @Test
    void testTitleToNumber() {
        Assertions.assertThat(titleToNumber("A")).isEqualTo(1);
        Assertions.assertThat(titleToNumberLeftToRight("AB")).isEqualTo(28);
    }

    /**
     * Scan from Right to Left (base-26 system)
     * Here this is the base-26 system, but we can apply the same calculation method as decimal or binary system.
     * So if we iterate from right-most char to the left, we can have a for loop with two ptr, one from the end
     * for the char, the other from zero for the exponent of 26.
     * <p>
     * The sequence and result of a n-lengh input: (idx is the mapping index[1-26] for A-Z)
     * (idx⋅26^0) + (idx⋅26^1) + (idx⋅26^2) + (idx⋅26^n-1) + (idx⋅26^n)
     * <p>
     * Time complexity: O(n)
     * Space complexity: O(1)
     */
    int titleToNumber(String columnTitle) {
        int result = 0;
        for (int i = columnTitle.length() - 1, x = 0; i >= 0; i--, x++) {
            int charIdx = columnTitle.charAt(i) - 'A' + 1; // 1-base
            result += (charIdx) * Math.pow(26, x);
        }
        return result;
    }

    /**
     * Scan from Left to Right
     * In a decimal system, to get the decimal value of string "1337", we can iteratively find the result
     * by scanning the string from left to right as follows:
     * '1' = 1
     * '13' = (1 x 10) + 3 = 13
     * '133' = (13 x 10) + 3 = 133
     * '1337' = (133 x 10) + 7 = 1337
     * <p>
     * Here we have the base-26 system, For "BCM", it would be (2 x 26 x 26) + (3 x 26) + (13)
     * When prcoessing from left to right, we get
     * "B" = 2
     * "BC" = (2)26 + 3
     * "BCM" = (2(26) + 3)26 + 13
     * <p>
     * Time complexity: O(n)
     * Space complexity: O(1)
     */
    int titleToNumberLeftToRight(String columnTitle) {
        // Scan left to right
        int result = 0;
        for (int i = 0; i < columnTitle.length(); i++) {
            result = result * 26;
            // subtracting 'A' gives us val from 0 (for A) to 25 (for Z). We want 1-index so plus 1
            result += columnTitle.charAt(i) - 'A' + 1;
        }
        return result;
    }

    /**
     * Divide Two Integers
     * Given two integers dividend and divisor, divide two integers without using multiplication,
     * division, and mod operator.
     * <p>
     * The integer division should truncate toward zero, which means losing its fractional part.
     * For example, 8.345 would be truncated to 8, and -2.7335 would be truncated to -2.
     * <p>
     * Return the quotient after dividing dividend by divisor.
     * <p>
     * Note: Assume we are dealing with an environment that could only store integers within
     * the 32-bit signed integer range: [−2^31, 2^31 − 1]. For this problem, if the quotient is
     * strictly greater than 2^31 - 1, then return 2^31 - 1, and if the quotient is strictly less
     * than -2^31, then return -2^31.
     * <p>
     * Input: dividend = 10, divisor = 3
     * Output: 3
     * Explanation: 10/3 = 3.33333.. which is truncated to 3.
     * <p>
     * Input: dividend = 7, divisor = -3
     * Output: -2
     * Explanation: 7/-3 = -2.33333.. which is truncated to -2.
     * <p>
     * https://leetcode.com/explore/interview/card/top-interview-questions-medium/113/math/820/
     */
    @Test
    void testDivide() {
        Assertions.assertThat(divide(10, 3)).isEqualTo(3);
        Assertions.assertThat(divide(7, -3)).isEqualTo(-2);
    }

    int divide(int dividend, int divisor) {
        // TODO:
        return 0;
    }

    /**
     * Evaluate Reverse Polish Notation
     * You are given an array of strings tokens that represents an arithmetic expression in a
     * Reverse Polish Notation.
     * <p>
     * Evaluate the expression. Return an integer that represents the value of the expression.
     * <p>
     * Note that:
     * <p>
     * The valid operators are '+', '-', '*', and '/'.
     * Each operand may be an integer or another expression.
     * The division between two integers always truncates toward zero.
     * There will not be any division by zero.
     * The input represents a valid arithmetic expression in a reverse polish notation.
     * The answer and all the intermediate calculations can be represented in a 32-bit integer.
     * <p>
     * Input: tokens = ["2","1","+","3","*"]
     * Output: 9
     * Explanation: ((2 + 1) * 3) = 9
     * <p>
     * <p>
     * Input: tokens = ["4","13","5","/","+"]
     * Output: 6
     * Explanation: (4 + (13 / 5)) = 6
     * <p>
     * https://leetcode.com/problems/evaluate-reverse-polish-notation/description/
     */
    @Test
    void testEvalRPN() {
        Assertions.assertThat(evalRPN(new String[]{"2", "1", "+", "3", "*"})).isEqualTo(9);
        Assertions.assertThat(evalRPN(new String[]{"4", "13", "5", "/", "+"})).isEqualTo(6);
    }

    /**
     * Iterate the list and push the number to the Stack, and when encountering any operator in the list, pop
     * the top two numbers on the stack to apply the operator and push it back to the stack.
     * <p>
     * Reverse Polish Notation rules:
     * While there are operators remaining in the list, find the left-most operator. Apply it to the
     * 2 numbers immediately before it, and replace all 3 tokens (the operator and 2 numbers) with the result.
     * As long as the input was valid, this rule will always work and leave a single number that should be returned.
     * <p>
     * <p>
     * Time Complexity : O(n)
     * <p>
     * Space Complexity : O(n)
     */
    int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : tokens) {
            if (!"+-*/".contains(token)) {
                stack.push(Integer.valueOf(token));
                continue;
            }
            // for division and subtraction, we need to reverse them before applying the operator
            int num2 = stack.pop();
            int num1 = stack.pop();
            int result = 0;
            switch (token) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    result = num1 / num2;
                    break;
            }
            stack.push(result);
        }
        return stack.pop(); // RPN assures there is only one number left on the stack.
    }

    /**
     * Basic Calculator II
     * Given a string s which represents an expression, evaluate this expression and return its value.
     * The integer division should truncate toward zero.
     * You may assume that the given expression is always valid. All intermediate results will be in the
     * range of [-231, 231 - 1].
     * <p>
     * Note: You are not allowed to use any built-in function which evaluates strings as mathematical expressions, such as eval().
     * <p>
     * Input: s = "3+2*2"
     * Output: 7
     * <p>
     * Input: s = " 3/2 "
     * Output: 1
     * <p>
     * Input: s = " 3+5 / 2 "
     * Output: 5
     * <p>
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 3 * 105
     * s consists of integers and operators ('+', '-', '*', '/') separated by some number of spaces.
     * s represents a valid expression.
     * All the integers in the expression are non-negative integers in the range [0, 231 - 1].
     * The answer is guaranteed to fit in a 32-bit integer.
     * <p>
     * https://leetcode.com/problems/basic-calculator-ii/description/
     */
    @Test
    void testCalculatorII() {
        Assertions.assertThat(calculateII("3+5 / 2*2-1")).isEqualTo(6);
    }

    /**
     * Use the Basic Calculator III solution as the basis but remove the usage of Queue and parentheses condition cuz we
     * don't need to support parentheses
     */
    int calculateII(String s) {
        int num = 0;
        char prevOperator = '+';
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);
            if (Character.isDigit(currentChar)) {
                num = 10 * num + (currentChar - '0');
            } else if ("+-*/".indexOf(currentChar) != -1) {
                eval(stack, num, prevOperator);
                num = 0;
                prevOperator = currentChar;
            }
        }
        eval(stack, num, prevOperator);
        return stack.stream().mapToInt(i -> i).sum();
    }

    /**
     * Iterate the string and once the next char is [+ - * /], we evaluate the current operand and the last
     * "evaluated" operand and keep adding the evaluated result to final result.
     * <p>
     * For example:
     * 2               [+ - * /]     3
     * lastEvalOperand            currentNum
     * <p>
     * The key is we need to keep track of the lastEvalOperand and update it accordingly.
     * <p>
     * lastEvalOperand is updated at two type of scenarios
     * 1. If the operation is Addition (+) or Subtraction (-), lastEvalOperand is just updated to the
     * currentNum or negative currentNum, and is subsequently added to the final result.
     * <p>
     * 2. If the operation is Multiplication (*) or Division (/), it is updated to the result of
     * lastEvalOperand [multiply/divide] currentNum, then it is added to the final result.
     * This is the KEY difference cuz these two operations take higher precedence so must be evaluated first.
     * Besides, cuz our logic is processing from left to right w/o respecting any operator precedence,
     * for operations(* and /), we need to first subtract the lastEvalOperand from the final result, then update
     * lastEvalOperand to the evaluation result of lastEvalOperand and current num w/ the operation(* or /).
     * When we have continuous * or / in a row, e.g. 2 * 3 / 4 * 5, this subtraction logic still holds true, cuz the
     * lastEvalOperand will have accumulated result in the end and added to the final result. The above sample is
     * literally processed like (((2 * 3) / 4) * 5). The parentheses kinda depicts how the lastEvalOperand
     * evaluation is expanded
     * <p>
     * Time Complexity: O(n), where n is the length of the string s.
     * Space Complexity: O(1)
     * <p>
     * Note: Another solution uses the stack, but it has worse space complexity, O(n)
     */
    int calculateIIVer2(String s) {
        int result = 0;
        int currentNum = 0;
        int lastEvalOperand = 0;
        char operation = '+'; // Default to +, so the first number will be added to result first
        for (int i = 0; i < s.length(); i++) {
            char currentChar = s.charAt(i);
            if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/') {
                // Set the operation first, so we can do the evaluation once we get the second operand
                operation = currentChar;
            } else if (Character.isDigit(currentChar)) {
                // Build up the number from every single digit char
                currentNum = currentNum * 10 + (currentChar - '0');

                if (i == s.length() - 1 || !Character.isDigit(s.charAt(i + 1))) {
                    // Next char is NOT digit OR we are at the last char, so we can evaluate the two operands and operation
                    switch (operation) {
                        case '+':
                            lastEvalOperand = currentNum;
                            break;
                        case '-':
                            lastEvalOperand = -currentNum;
                            break;
                        case '*':
                            result -= lastEvalOperand;
                            lastEvalOperand *= currentNum;
                            break;
                        case '/':
                            result -= lastEvalOperand;
                            lastEvalOperand /= currentNum;
                            break;
                    }
                    result += lastEvalOperand;
                    currentNum = 0; // Need to reset it once we finish an evaluation
                }
            }
        }
        return result;
    }

    /**
     * Basic Calculator
     * <p>
     * Given a string s representing a valid expression, implement a basic calculator to evaluate it, and return the result of the evaluation.
     * <p>
     * Note: You are not allowed to use any built-in function which evaluates strings as mathematical expressions, such as eval().
     * <p>
     * Input: s = "1 + 1"
     * Output: 2
     * <p>
     * Input: s = " 2-1 + 2 "
     * Output: 3
     * <p>
     * Input: s = "(1+(4+5+2)-3)+(6+8)"
     * Output: 23
     * <p>
     * Constraints:
     * <p>
     * 1 <= s.length <= 3 * 105
     * s consists of digits, '+', '-', '(', ')', and ' '.
     * s represents a valid expression.
     * '+' is not used as a unary operation (i.e., "+1" and "+(2 + 3)" is invalid).
     * '-' could be used as a unary operation (i.e., "-1" and "-(2 + 3)" is valid).
     * There will be no two consecutive operators in the input.
     * Every number and running calculation will fit in a signed 32-bit integer.
     * <p>
     * https://leetcode.com/problems/basic-calculator/description/
     */
    @Test
    void testCalculatorI() {
        Assertions.assertThat(calculateI("6-(2+3)+1")).isEqualTo(2);
        Assertions.assertThat(calculateI("(1+(4+5+2)-3)+(6+8)")).isEqualTo(23);
    }

    /**
     * Use the Basic Calculator III solution as basis and remove the support of "* /"
     * Use the existing eval method here, but in fact we can remove the switch condition for * and /
     */
    int calculateI(String s) {
        Queue<Character> queue = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++)
            queue.offer(s.charAt(i));
        return calI(queue);
    }

    int calI(Queue<Character> queue) {
        int num = 0;
        char prevOperator = '+';
        Deque<Integer> stack = new ArrayDeque<>();
        while (!queue.isEmpty()) {
            Character currentChar = queue.poll();
            if (Character.isDigit(currentChar)) {
                num = 10 * num + (currentChar - '0');
            } else if ("+-".indexOf(currentChar) != -1) {
                eval(stack, num, prevOperator);
                num = 0;
                prevOperator = currentChar;
            } else if (currentChar.equals('(')) {
                num = calI(queue);
            } else if (currentChar.equals(')')) {
                break;
            }
        }
        eval(stack, num, prevOperator);
        return stack.stream().mapToInt(i -> i).sum();
    }

    /**
     * This is another implementation before I found the template working for all Basic Calculator problem.
     * <p>
     * Push/pop a "sign" var(1 or -1) to/from the stack when encountering '(' or ')'. Sign is updated when encountering
     * '+' or '-'. Use it to track the sign before every parenthesis, and multiply it to every term inside parentheses,
     * so we can just add every term to the result as we go.
     * <p>
     * There are two ideas to simplify the problem.
     * 1. We can treat minus as plus negative number, so we only need to deal with addition operation
     * Ex, A−B−C could be re-written as A+(−B)+(−C)
     * <p>
     * 2. We want to get rid of the parentheses by applying the sign in front of the open parenthesis
     * to the sign of each term in the parentheses.
     * Ex, 6-(2+3)+1 => 6-2-3+1
     * <p>
     * <p>
     * When we encounter an operator(+ or -),
     * For the (1), we maintain a sign int variable, which is either 1 or -1, to represent + or - operation.
     * For the (2), we use a stack to track the sign state when we enter parentheses, we push it to the
     * stack when encountering the (, pop it out when encountering the ).
     * When we encounter + or -, we always peek the sign before the current parentheses on the stack, then
     * multiply it to the current + or -, i.e. 1 or -1
     * <p>
     * Algo:
     * 1. Start from +1 sign and scan s from left to right;
     * 2. If c == digit: This number = Last digit * 10 + This digit;
     * -  If this is the last char or the next char is NOT digit, Add num * sign to result;
     * 3. if c == '+': This sign = signBeforeParentheses * 1; clear num;
     * 4. if c == '-': This sign = signBeforeParentheses * -1; clear num;
     * 5. if c == '(': Push the current sign to stack; (sign before the open parenthesis)
     * 6. if c == ')': Pop sign before the current parentheses, and we come back to the outer parentheses;
     * <p>
     * Time Complexity: O(N), where N is the length of the string.
     * Space Complexity: O(N), where N is the length of the string.
     */
    int calculateIVer2(String s) {
        if (s == null) return 0;
        int result = 0;
        int sign = 1;// we use 1 for +, -1 for -
        int num = 0;

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(sign);// We need to push 1 to the stack first, so it can work for scenario when "-" is the first char

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                // Build up the number from every single digit char
                num = num * 10 + (c - '0');
                if (i == s.length() - 1 || !Character.isDigit(s.charAt(i + 1))) {
                    // Next char is NOT digit OR we are at the last char, so we can add num to result
                    // Need to multiply the sign first before adding to result
                    result += sign * num;
                }
            } else if (c == '+' || c == '-') {
                // To determine the sign which will be used for the next number, we need to get the sign in front of the
                // current parentheses from the top of the stack and multiply the current sign char we encounter
                int signBeforeParentheses = stack.peek();
                sign = signBeforeParentheses * (c == '+' ? 1 : -1);
                num = 0; // Need to reset the num first before processing the next digit char
            } else if (c == '(') {
                // Push the sign in front of the parenthesis, so we will use it to multiply to the sign of each term in the parenthesis
                stack.push(sign);
            } else if (c == ')') {
                // Finished one pair of parentheses, so we can throw the signBeforeParentheses on the top of stack away
                stack.pop();
            }
        }
        return result;
    }

    /**
     * Basic Calculator III
     * Implement a basic calculator to evaluate a simple expression string.
     * <p>
     * The expression string contains only non-negative integers, '+', '-', '*', '/' operators,
     * and open '(' and closing parentheses ')'. The integer division should truncate toward zero.
     * <p>
     * You may assume that the given expression is always valid. All intermediate results will be
     * in the range of [-231, 231 - 1].
     * <p>
     * Note: You are not allowed to use any built-in function which evaluates strings as
     * mathematical expressions, such as eval().
     * <p>
     * Input: s = "1+1"
     * Output: 2
     * <p>
     * Input: s = "6-4/2"
     * Output: 4
     * <p>
     * Input: s = "2*(5+5*2)/3+(6/2+8)"
     * Output: 21
     * <p>
     * https://leetcode.com/problems/basic-calculator-iii/description/
     */
    @Test
    void testCalculatorIII() {
        Assertions.assertThat(calculateIII("6-(2+3)+1")).isEqualTo(2);
        Assertions.assertThat(calculateIII("(1+(4+5+2)-3)+(6+8)")).isEqualTo(23);
        Assertions.assertThat(calculateIII("2*(5+5*2)/3+(6/2+8)")).isEqualTo(21);
    }

    /**
     * Add all char to the queue first and remove each one at a time. When encountering +-* /, evaluate the last number
     * w/ last operator, and push the result to the stack. The stack contains the number or multiplication/division
     * result. If encountering (, recursively call the same method to evaluate the expression in the parentheses.
     * <p>
     * All Basic Calculator problems have similar requirement.
     * Basic Calculator I: Must support (, ), +, -, non-negative int
     * Basic Calculator II: Must support +, -, *, /, non-negative int
     * Basic Calculator III: Must support (, ), +, -, *, /, non-negative int
     * <p>
     * Basic Calculator III includes everything from I and II. We just need to remove certain functions for the
     * solution of I and II.
     * <p>
     * Observation:
     * 1. When we iterate the expression str, we need to first evaluate the sub-expression enclosed by the most inner
     * parentheses, and take its result to evaluate its the outer sub-expression if any. This kind of execution path
     * is similar to the DFS, which we can recursively go down to the most inner sub-expression and iterate each term
     * there and return the evaluated result and return to its enclosed parentheses and so on, until the top level
     * expression.
     * <p>
     * 2. We will still continue to iterate the char in str even when we process the expression in the parentheses at
     * the recursion. But it will be easier if putting every char in the queue and keep removing the char from it to
     * iterate the str, so we don't need to pass or use a global pointer when we make recursive call.
     * <p>
     * 3. To easier access the previous operand or the evaluated sub-expression result from the parentheses, we can push
     * them to the stack. Instead of updating a expression evaluation result in the iteration, we can just keep adding
     * the number or the result of multiplication and division operation to the stack and we just sum up the every
     * number in the stack in the end. This will make us to be able to evaluate the multiplication and division first,
     * and leave the addition in the end. For the subtraction, we just negate the number and add it to the stack.
     * <p>
     * Algo:
     * 1. Add all char to the queue
     * 2. Pass the queue to the "cal" method, and the previous operator is default to +
     * 3. For each char removed from the queue
     * - Build up all digit char and set to a var num
     * - If it is "+-* /", we evaluate the num w/ the previous operator and push the result to the stack.(May involve
     * the top element on the stack for * and /)
     * - If it is "(", recursively call "cal" method. We need to first evaluate the segment of expression in the
     * parentheses. The returned result is set to num.
     * - If it is ")", we break the loop. This condition is for the end of one segment of expression in the parentheses.
     * 4. We need to evaluate the num, prevOp one more time. This is for the last number in the sub-expression enclosed
     * by the parentheses or the input expression str.
     * 5. Sum up all numbers on the stack and this is the result.
     * <p>
     * Reference:
     * https://leetcode.com/problems/basic-calculator-iii/solutions/1727380/java-this-simple-template-can-be-used-for-basic-calculator-i-ii-iii/
     * <p>
     * Time Complexity : O(n)
     * Space Complexity : O(n)
     */
    int calculateIII(String s) {
        if (s == null)
            return 0;
        // Put all chars to the queue, so it is easier to track the reaming chars when doing the recursive call
        Queue<Character> q = new ArrayDeque<>();
        for (char c : s.toCharArray())
            q.offer(c);
        return cal(q);
    }

    private int cal(Queue<Character> q) {
        // it can be set to the last number we encountered, or the evaluated result from the last enclosed parentheses we just exit
        int num = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        char prevOp = '+';
        while (!q.isEmpty()) {
            char currentChar = q.poll();
            if (Character.isDigit(currentChar)) {
                num = 10 * num + currentChar - '0'; // Number can be multi-digit, so need to keep building it up.
            } else if (currentChar == '(') {
                // Recursively call cal method to evaluate every term enclosed by it and the close parenthesis first.
                // It may make another recursive call internally depending on if there are any nested parentheses
                num = cal(q);
            } else if ("+-*/".indexOf(currentChar) != -1) {
                // Trigger the evaluation logic encounter the operator.
                // Evaluate the num and the top element on the stack(* or /) w/ PREVIOUS operator and push the result to
                // stack
                eval(stack, num, prevOp);
                // Rest the num and update the previous operator to current char
                num = 0;
                prevOp = currentChar;
            } else if (currentChar == ')') {
                // We finish a segment of a sub-expression inside the parentheses
                // This is also the base case for the recursion to terminate
                break;
            }
        }
        // Trigger eval after the loop for two reasons
        // 1. When we have processed every char in the expression. But the last number in the expression or the evaluated
        // result of the last parentheses is not evaluated w/ the num yet.
        // 2. When We break the loop after exiting a closed parenthesis, we need to evaluate the last number, so we
        // can sum up the result on the stack and return.
        eval(stack, num, prevOp);
        return stack.stream().mapToInt(a -> a).sum(); // sum up all number on the stack
    }

    private void eval(Deque<Integer> stack, int num, char op) {
        // We need to first compute operands involving the "*" and "/" operation first cuz they have higher precedence.
        // The idea is we will leave the addition at the very end so the final result of the expression will be computed by
        // summing up every number(not necessary the exact number in the original expression) on the stack. For the
        // subtraction, we negate the number and push it to the stack.
        switch (op) {
            case '+':
                stack.push(num); // just push the number to the stack
                break;
            case '-':
                stack.push(-num); // flip the sign of the number and push to the stack
                break;
            case '*':
                // Multiply the last number(top element on the stack) by num and push result to the stack
                stack.push(stack.pop() * num);
                break;
            case '/':
                // Divide the last number(top element on the stack) by num and push result to the stack
                stack.push(stack.pop() / num);
                break;
            default:
                break;
        }
    }

}
