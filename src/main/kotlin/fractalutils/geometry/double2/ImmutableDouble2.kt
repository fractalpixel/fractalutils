package fractalutils.geometry.double2


/**
 * Double2 that can not be modified after instantiation.
 */
class ImmutableDouble2(override val x: Double = 0.0,
                       override val y: Double = 0.0) : Double2Base() {

    constructor(other: Double2) : this(other.x, other.y)
}