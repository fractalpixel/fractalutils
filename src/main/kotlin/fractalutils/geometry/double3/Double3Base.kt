package fractalutils.geometry.double3


/**
 * Common functionality for Double3.
 */
abstract class Double3Base: Double3 {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Double3) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }
}