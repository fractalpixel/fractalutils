package fractalutils.random

/**
 * XorShift random number generator.
 *
 * Uses the xorshift variant xorshift128+ described in http://xorshift.di.unimi.it/
 * See http://xorshift.di.unimi.it/xorshift128plus.c for original c code.
 */
class XorShift(seed: Long = System.nanoTime()) : BaseRand(seed) {

    override fun getStateSize(): Int = 2

    override fun nextLong(): Long {
        var s1 = state[0]
        val s0 = state[1]

        state[0] = s0
        s1 = s1 xor (s1 shl 23)
        state[1] = s1 xor s0 xor s1.ushr(17) xor s0.ushr(26)
        return state[1] + s0
    }

    override fun nextRand(): Rand {
        return XorShift(nextLong())
    }

}
