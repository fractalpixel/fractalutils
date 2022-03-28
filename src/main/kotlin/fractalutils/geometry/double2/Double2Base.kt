package fractalutils.geometry.double2


/**
 * Common functionality for Double2.
 */
abstract class Double2Base: Double2 {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Double2) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }

    override fun toString(): String {
        return "($x, $y)"
    }

}