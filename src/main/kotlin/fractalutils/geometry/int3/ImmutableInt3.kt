package fractalutils.geometry.int3

import fractalutils.geometry.int2.Int2

/**
 * Immutable Int3.  Can't be changed after creation.
 */
class ImmutableInt3(override val x: Int = 0,
                    override val y: Int = 0,
                    override val z: Int = 0): Int3Base() {

    constructor(other: Int3): this(other.x, other.y, other.z)
    constructor(other: Int2, z: Int = 0): this(other.x, other.y, z)

}