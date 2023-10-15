package playground;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

/*
 * TODO Tips:
 *  1. To retrieve the right-most bit in an integer n, one could either apply the modulo operation (i.e. n % 2) or the bit AND operation (i.e. n & 1)
 *  2. To add the binary result to another binary, use | (OR) operator
 *  3. Java bit-wise operators: https://www.baeldung.com/java-bitwise-operators
 *  4. More about Java integer type:
 *      Java uses two's complement to store integers.
        The negative numbers are represented by inverting 1's to 0's and vice versa for all
        of the bits in a value, then adding 1 to the result.
        For example, -42 is represented by inverting all of the bits in 42, or 00101010,
        which yields 11010101, then adding 1, which results in 11010110, or -42.
        To decode a negative number, first invert all of the bits, then add 1.
        For example, -42, or 11010110 inverted, yields 00101001, or 41, so when you add 1 you get 42.
 */
public class BitManipulate {
    /**
     * Number of 1 Bits
     * https://leetcode.com/problems/number-of-1-bits/editorial/
     */
    @Test
    void testHammingWeight() {
        Assertions.assertThat(hammingWeight(128)).isEqualTo(1);
        Assertions.assertThat(hammingWeight(11)).isEqualTo(3);
    }

    /**
     * We can check the ith bit of a number using a bit mask. We start with a mask m=1, because the binary representation of 1 is,
     * <p>
     * 0000 0000 0000 0000 0000 0000 0000 0001
     * <p>
     * Clearly, a logical AND between any number and the mask 1 gives us the least significant bit of this number.
     * To check the next bit, we shift the mask to the left by one.
     * <p>
     * 0000 0000 0000 0000 0000 0000 0000 0010
     * <p>
     * And so on.
     * Time Complexity: O(1). Space Complexity: O(1)
     */
    int hammingWeight(int n) {
        int bits = 0;
        int mask = 1;
        for (int i = 0; i < 32; i++) {
            if ((n & mask) != 0)
                // mask only has one bit of '1', so if the corresponding bit from n is '1', it will yield non-0 output
                bits++;
            mask <<= 1;
        }
        return bits;
    }

    /**
     * Hamming Distance
     * https://leetcode.com/problems/hamming-distance/description/
     */
    @Test
    void testHammingDistance() {
        Assertions.assertThat(hammingDistance(1, 4)).isEqualTo(2);
    }

    /**
     * Built-in BitCounting Functions
     * First do the XOR for two integer(1s in the output meant the input bit is different), then use bitCount method
     * to get the count of bit 1
     * Time Complexity: O(1). Space Complexity: O(1)
     */
    int hammingDistance(int x, int y) {
        return Integer.bitCount(x ^ y);
    }


    /**
     * Reverse Bits
     * https://leetcode.com/problems/reverse-bits/description/
     */
    @Test
    void testReverseBits() {
        Assertions.assertThat(reverseBits(43261596)).isEqualTo(964176192);
    }

    /**
     * Traverse till 32 bits and find the last digit of n by using n&1. After this we will add this value in ans,
     * then right shift the value of n and repeat till last item
     * Time Complexity: O(1). Space Complexity: O(1)
     */
    int reverseBits(int n) {
        int ans = 0;
        for (int i = 0; i < 32; i++) { // Traverse 32 bits
            // left shift one bit for the ans so we don't overwrite the ouput from last iteration
            // (ans begins at 0, so it doesn't matter for the first iteration)
            ans = ans << 1;
            // n & 1 finds the last digit
            // | operator adds the result, i.e. last digit, to the ans
            ans = ans | (n & 1);
            // right shift one bit for the n for next digit
            n = n >> 1;
        }
        return ans;
    }

    /**
     * Add Binary
     * https://leetcode.com/problems/add-binary/solution/
     */
    @Test
    void testAddBinary() {
        String a = "11", b = "1";


        Assertions.assertThat(addBinary(a, b)).isEqualTo("100");
        a = "1010";
        b = "1011";
        Assertions.assertThat(addBinary(a, b)).isEqualTo("10101");

    }

    /*
    Sum up w/o using addition operation
    https://www.youtube.com/watch?v=qq64FrA2UXQ&ab_channel=BackToBackSWE

    Time complexity: O(N+M), where N and M are lengths of the input strings a and b.
    Space complexity: O(max(N,M)) to keep the answer.

    1. Sum without considering carry: x ^ y (XOR). This simulates summing of two binaries WITHOUT taking carry into account.
    2. Get the carry bits: x & y (AND).
    3. Shift carry bits 1 bit left: x & y << 1. so that carry is applied to the right position.
       Step 2 & 3 is to compute the real carry that will be used for the XOR operation(binary addition) in the next iteration
    4. By above step. x + y actually means "sum without carries" + "all the carries"
    5. we repeat 1 - 3 in the loop. "sum without carries" + "all the carries" until carries becomes 0.
    6. When carries = 0, "sum without carries" is the actual sum.
    Note: The reason we need the loop is that when the carry is non-zero(Step 2 & 3) in each iteration, that means there is carry implicitly
          yielded from the binary addition(XOR) operation in this iteration, so we need to take this carry and do XOR with the XORed binary
          in the next iteration to make it be injected into the result. This process continues until carry becomes zero, which means
          no more carry, and we get the final answer.
          In nutshell, the binary addition is consist of two operations here, XOR and the carry calculation(AND w/ << 1). And we need to
          keep injecting the non-zero carry to do XOR w/ the previous XORed binary until no more carry.

     */
    String addBinary(String a, String b) {
        BigInteger x = new BigInteger(a, 2);
        BigInteger y = new BigInteger(b, 2);
        BigInteger zero = new BigInteger("0", 2);
        BigInteger carry, answer;
        while (y.compareTo(zero) != 0) {
            answer = x.xor(y);
            carry = x.and(y).shiftLeft(1);
            x = answer;
            y = carry;
        }
        return x.toString(2);
    }

    // TODO: Need to check this when revisit
    String addBinaryTwo(String a, String b) {
        int n = a.length(), m = b.length();
        if (n < m)
            return addBinaryTwo(b, a);
        int L = Math.max(n, m);

        StringBuilder sb = new StringBuilder();
        int carry = 0, j = m - 1;
        for (int i = L - 1; i > -1; --i) {
            if (a.charAt(i) == '1') ++carry;
            if (j > -1 && b.charAt(j--) == '1') ++carry;

            if (carry % 2 == 1) sb.append('1');
            else sb.append('0');

            carry /= 2;
        }
        if (carry == 1) sb.append('1');
        sb.reverse();

        return sb.toString();
    }

    /**
     * Sum of Two Integers
     * Given two integers a and b, return the sum of the two integers without using the
     * operators + and -.
     * <p>
     * Input: a = 1, b = 2
     * Output: 3
     * <p>
     * Input: a = 2, b = 3
     * Output: 5
     * <p>
     * https://leetcode.com/problems/sum-of-two-integers/description/
     */
    @Test
    void testGetSum() {
        Assertions.assertThat(getSum(1, 2)).isEqualTo(3);
        Assertions.assertThat(getSum(2, 3)).isEqualTo(5);
    }

    /**
     * Do x XOR y for sum w/o carry, and (x & y) << 1 to calculate carry, and take carry to do
     * AND w/ the previous XOR result until carry == 0
     * <p>
     * Algo:
     * - While carry is nonzero: y != 0:
     * -	- Current answer without carry is XOR of x and y: answer = x ^ y.
     * -   - Current carry is left-shifted AND of x and y: carry = (x & y) << 1.
     * -	- Job is done, prepare the next loop: x = answer, y = carry.
     * - Return x
     * <p>
     * Java integer type uses two's complement to store signed integer, so all management of
     * negative numbers, signs, and subtractions are taken care of by default.
     * <p>
     * Time complexity: O(1).
     * <p>
     * Space complexity: O(1)
     *
     * @param a
     * @param b
     * @return
     */
    int getSum(int a, int b) {
        while (b != 0) {
            int answer = a ^ b;
            int carry = (a & b) << 1;
            a = answer;
            b = carry;
        }
        return a;
    }

    
}
