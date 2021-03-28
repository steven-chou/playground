package playground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

public class BitManipulate {

    /**
     * Add Binary
     * https://leetcode.com/problems/add-binary/solution/
     */
    @Test
    void testAddBinary() {
        String a = "11", b = "1";

        Assertions.assertEquals("100", addBinary(a, b));
        a = "1010";
        b = "1011";
        Assertions.assertEquals("10101", addBinary(a, b));
    }

    /*
    Sum up w/o using addition operation
    https://www.youtube.com/watch?v=qq64FrA2UXQ&ab_channel=BackToBackSWE

    Time complexity: O(max(N,M)), where NN and MM are lengths of the input strings a and b.
    Space complexity: O(max(N,M)) to keep the answer.

    1. sum without considering carry: x ^ y (XOR)
    2. get the carry bits: x & y (AND)
    3. shift carry bits 1 bit left: x & y << 1. so that carry is applied to the right position
    4. By above step. x + y becomes "sum without carries" + "all the carries"
    5. we repeat 1 - 3 in the loop. "sum without carries" + "all the carries" until carries becomes 0.
    6. When carries = 0, "sum without carries" is the actual sum.
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
        for(int i = L - 1; i > -1; --i) {
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
}
