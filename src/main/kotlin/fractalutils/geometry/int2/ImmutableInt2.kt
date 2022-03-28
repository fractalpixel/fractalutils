package fractalutils.geometry.int2

/**
 * Immutable Int2.  Can't be changed after creation.
 */
class ImmutableInt2(override val x: Int,
                    override val y: Int): Int2Base() {

    constructor(other: Int2): this(other.x, other.y)

}