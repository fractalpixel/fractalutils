package fractalutils.geometry.int2


/**
 * Common functionality for Int2
 */
abstract class Int2Base: Int2 {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Int2) return false

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 1009 * result + y  // Disperse x and y hashCode a bit more than just multiplying with 31, as this is used e.g. in large grid hashmaps.
        return result
    }

    override fun toString(): String {
        return "($x, $y)"
    }


}