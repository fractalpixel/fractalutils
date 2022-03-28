package fractalutils.random

import java.lang.Long.rotateLeft

/*
  Original Copyright notice:
  ---
  Written in 2019 by David Blackman and Sebastiano Vigna (vigna@acm.org)

  To the extent possible under law, the author has dedicated all copyright
  and related and neighboring rights to this software to the public domain
  worldwide. This software is distributed without any warranty.

  See <http://creativecommons.org/publicdomain/zero/1.0/>.
*/

/**
 * This is xoshiro256++ 1.0, one of our all-purpose, rock-solid generators.
 * It has excellent (sub-ns) speed, a state (256 bits) that is large
 * enough for any parallel application, and it passes all tests we are
 * aware of.
 *
 * For generating just floating-point numbers, xoshiro256+ is even faster.
 *
 *
 * The state must be seeded so that it is not everywhere zero. If you have
 * a 64-bit seed, we suggest to seed a splitmix64 generator and use its
 * output to fill s.
 *
 * ----
 * See https://prng.di.unimi.it/xoshiro256plusplus.c  and  https://prng.di.unimi.it/ or http://xorshift.di.unimi.it/
*/
class XoroShiro256PP(seed: Long = System.nanoTime()) : BaseRand(seed) {

    override fun getStateSize(): Int = 4

    override fun nextLong(): Long {
        val result = rotateLeft(state[0] + state[3], 23) + state[0]
        val t = state[1] shl 17

        state[2] = state[2] xor state[0]
        state[3] = state[3] xor state[1]
        state[1] = state[1] xor state[2]
        state[0] = state[0] xor state[3]

        state[2] = state[2] xor t

        state[3] = rotateLeft(state[3], 45)

        return result;
    }

    override fun nextRand(): Rand {
        return XoroShiro256PP(nextLong())
    }
}
