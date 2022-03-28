package fractalutils.geometry.double3

import fractalutils.geometry.double2.Double2

/**
 * Double3 that can not be changed
 */
class ImmutableDouble3(override val x: Double = 0.0,
                       override val y: Double = 0.0,
                       override val z: Double = 0.0): Double3Base() {

    constructor(other: Double3): this(other.x, other.y, other.z)
    constructor(other: Double2, z: Double = 0.0): this(other.x, other.y, z)

}