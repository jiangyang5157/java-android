package com.gmail.jiangyang5157.java_math;

import java.util.Random;

/**
 * Mersenne.java
 * Created on: June 03, 2011
 * <p>
 * Author: David Jolly
 * <jollyd@onid.oregonstate.edu>
 * <p>
 * Based off of the Mersenne Twist algorithm.
 * See M. Matsumoto and T. Nishimura,
 * "Mersenne Twister:
 * A 623-Dimensionally Equidistributed Uniform Pseudo-Random Number Generator",
 * ACM Transactions on Modeling and Computer Simulation,
 * Vol. 8, No. 1, January 1998, pp 3--30.
 */
public class Mersenne {

    private static final int M = 624;
    private static final int N = 397;

    private int index = 0;
    private long value = 0;
    private long[] table;

    public Mersenne() {
        this(new Random().nextLong());
    }

    /**
     * @param seed used to initialize the mersenne twister
     */
    public Mersenne(final long seed) {
        index = 0;
        value = 0;

        table = new long[M];
        table[0] = seed;

        for (int i = 1; i < M; i++) {
            table[i] = 0xffffffffL & (0x6c078965L * (table[i - 1] ^ (table[i - 1] >> 30)) + i);
        }

        generate();
    }

    /**
     * Generates a table of random values
     */
    private void generate() {
        for (int i = 0; i < M; i++) {
            value = (0x80000000L & table[i])
                    + (0x7fffffffL & table[(i + 1) % M]);
            table[i] = table[(i + N) % M] ^ (value >> 1);
            if ((value & 1) == 1) {
                table[i] ^= 0x9908b0dfL;
            }
        }
    }

    /**
     * @return a pseudo-random long value
     */
    public long nextLong() {
        if (index == 0) {
            generate();
        }

        value = table[index];
        value ^= (value >> 11);
        value ^= (value << 7) & 0x9d2c5680L;
        value ^= (value << 15) & 0xefc60000L;
        value ^= (value >> 18);
        index = (index + 1) % M;
        return value;
    }

    /**
     * @param n [0, n)
     * @return a pseudo-random long value
     */
    public long nextLong(final int n) {
        return n == 0 ? 0 : nextLong() % n;
    }

    /**
     * @return a pseudo-random integer value
     */
    public int nextInt() {
        return (int) (nextLong() >> 16);
    }

    /**
     * @param n [0, n)
     * @return a pseudo-random integer value
     */
    public int nextInt(final int n) {
        return n == 0 ? 0 : nextInt() % n;
    }

    /**
     * Test case
     */
    public static void main(String[] args) {
        Mersenne mersenne = new Mersenne();
        for (int i = 0; i < 20; i++) {
            System.out.println("mersenne.nextLong(4) = " + mersenne.nextLong(4));
        }
        for (int i = 0; i < 20; i++) {
            System.out.println("mersenne.nextInt(4) = " + mersenne.nextInt(4));
        }
    }
}
