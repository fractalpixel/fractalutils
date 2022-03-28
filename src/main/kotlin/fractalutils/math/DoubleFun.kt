package fractalutils.math

/**
 * Returns a specified type of value for a double number.
 * Typically, defined for the 0..1 range, but should not throw errors outside it either.
 */
interface DoubleFun<T>: (Double) -> T {

    operator fun get(pos: Double): T

    override fun invoke(pos: Double): T = get(pos)
}
