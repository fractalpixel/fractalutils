package fractalutils.random

/*
  Original Copyright notice:
  ---
  Written in 2016 by David Blackman and Sebastiano Vigna (vigna@acm.org)

  To the extent possible under law, the author has dedicated all copyright
  and related and neighboring rights to this software to the public domain
  worldwide. This software is distributed without any warranty.

  See <http://creativecommons.org/publicdomain/zero/1.0/>.
*/

import java.lang.Long.rotateLeft

/**
 * This is the successor to xorshift128+. It is the fastest full-period
 * generator passing BigCrush without systematic failures, but due to the
 * relatively short period it is acceptable only for applications with a
 * mild amount of parallelism; otherwise, use a xorshift1024* generator.
 *
 * Beside passing BigCrush, this generator passes the PractRand test suite
 * up to (and included) 16TB, with the exception of binary rank tests,
 * which fail due to the lowest bit being an LFSR; all other bits pass all
 * tests. We suggest to use a sign test to extract a random Boolean value.
 *
     * See http://xorshift.di.unimi.it/
*/
class XoroShiro(seed: Long = System.nanoTime()) : BaseRand(seed) {

    override fun getStateSize(): Int = 2

    override fun nextLong(): Long {

        val result = state[0] + state[1]

        val t1 = state[1] xor state[0]
        state[0] = rotateLeft(state[0], 55) xor t1 xor (t1 shl 14) // a, b
        state[1] = rotateLeft(t1, 36) // c

        return result
    }

    override fun nextRand(): Rand {
        return XoroShiro(nextLong())
    }

}
