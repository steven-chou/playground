package playground;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MathETC {
    /**
     * Greatest common divisor of two numbers
     * http://javarevisited.blogspot.com/2016/07/how-to-calculate-gcf-and-lcm-of-two-numbers-in-java-example.html
     */
    @Test
    void GCD() {
        System.out.println(gcdRecursive(40, 24));
        System.out.println(gcdIterative(40, 24));
        System.out.println(gcdJDK(40, 24));
    }

    /**
     * a > b > 0
     */
    private long gcdRecursive(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return (gcdRecursive(b, a % b));
        }
    }

    /**
     * a > b > 0
     */
    private long gcdIterative(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    private int gcdJDK(int a, int b) {
        BigInteger gcd = BigInteger.valueOf(a).gcd(BigInteger.valueOf((b)));
        return gcd.intValue();
    }

    /**
     * Lowest common multiplier of two numbers
     * http://javarevisited.blogspot.com/2016/07/how-to-calculate-gcf-and-lcm-of-two-numbers-in-java-example.html
     */
    @Test
    void LCM() {
        System.out.println(lcm(40, 24));
    }

    /**
     * a > b > 0
     */
    private long lcm(long a, long b) {
        return (a * b) / gcdRecursive(a, b);
    }


    /**
     * https://www.mkyong.com/java/how-to-determine-a-prime-number-in-java/
     */
    @Test
    void checkPrimeNumber() {
        System.out.println(isPrime(13));
    }

    //checks whether an int is prime or not.
    boolean isPrime(int n) {
        //check if n is a multiple of 2
        if (n % 2 == 0) return false;
        //if not, then just check the odds
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    @Test
    void findAllPrimeNumberInRange() {
        System.out.println(findAllPrimeNumber(30));
    }

    /**
     * The sieve of Eratosthenes
     * https://www.mkyong.com/java/how-to-determine-a-prime-number-in-java/
     */
    private List<Integer> findAllPrimeNumber(int range) {
        boolean[] primes = new boolean[range];
        //set up the primesieve
        Arrays.fill(primes, true);        // assume all integers are prime.
        primes[0] = primes[1] = false;       // we know 0 and 1 are not prime.

        for (int i = 2; i < primes.length; i++) {
            //if the number is prime,
            //then go through all its multiples and make their values false.
            if (primes[i]) {
                for (int j = 2; i * j < primes.length; j++) {
                    primes[i * j] = false;
                }
            }
        }
        List<Integer> primeNumbers = new ArrayList<>();
        for (int i = 0; i < primes.length; i++) {
            if (primes[i])
                primeNumbers.add(i);
        }

        return primeNumbers;
    }

    @Test
    void testDuration() throws InterruptedException {

        Duration maxDuration = Duration.ofSeconds(30L);

        Instant start = Instant.now();

        Instant end = Instant.now();
        do {

            System.out.println("...");
            Thread.sleep(1000L);
            end = Instant.now();

        } while (maxDuration.compareTo(Duration.between(start, end)) > 0);

        System.out.println("done");


    }
}
