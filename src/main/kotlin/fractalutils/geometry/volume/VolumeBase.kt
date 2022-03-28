package fractalutils.geometry.volume


/**
 * Includes equals and hashcode.
 * Note that an immutable and mutable Volume with the same bounds will equal each other.
 */
abstract class VolumeBase: Volume {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Volume) return false

        if (empty != other.empty) return false
        if (minX != other.minX) return false
        if (minY != other.minY) return false
        if (minZ != other.minZ) return false
        if (maxX != other.maxX) return false
        if (maxY != other.maxY) return false
        if (maxZ != other.maxZ) return false

        return true
    }

    override fun hashCode(): Int {
        var result = empty.hashCode()
        result = 31 * result + minX.hashCode()
        result = 31 * result + minY.hashCode()
        result = 31 * result + minZ.hashCode()
        result = 31 * result + maxX.hashCode()
        result = 31 * result + maxY.hashCode()
        result = 31 * result + maxZ.hashCode()
        return result
    }

    override fun toString(): String {
        return "Volume ($minX, $minY, $minZ) to ($maxX, $maxY, $maxZ)"
    }
}