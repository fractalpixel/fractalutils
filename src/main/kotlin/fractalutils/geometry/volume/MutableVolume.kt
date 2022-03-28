package fractalutils.geometry.volume

import fractalutils.geometry.double3.Double3
import fractalutils.math.average
import fractalutils.math.max
import fractalutils.math.min

/**
 * A volume that can be changed.
 */
class MutableVolume(x1: Double = 0.0,
                    y1: Double = 0.0,
                    z1: Double = 0.0,
                    x2: Double = 0.0,
                    y2: Double = 0.0,
                    z2: Double = 0.0,
                    empty: Boolean = false): VolumeBase() {

    constructor(corner1: Double3, corner2: Double3, empty: Boolean = false): this(
            corner1.x min corner2.x,
            corner1.y min corner2.y,
            corner1.z min corner2.z,
            corner1.x max corner2.x,
            corner1.y max corner2.y,
            corner1.z max corner2.z,
            empty)

    constructor(other: Volume): this(other.minX,
                                     other.minY,
                                     other.minZ,
                                     other.maxX,
                                     other.maxY,
                                     other.maxZ,
                                     other.empty)

    override var empty: Boolean = empty
        private set
    override var minX: Double = x1 min x2
        private set
    override var minY: Double = y1 min y2
        private set
    override var minZ: Double = z1 min z2
        private set
    override var maxX: Double = x1 max x2
        private set
    override var maxY: Double = y1 max y2
        private set
    override var maxZ: Double = z1 max z2
        private set


    fun set(x1: Double = 0.0,
            y1: Double = 0.0,
            z1: Double = 0.0,
            x2: Double = 0.0,
            y2: Double = 0.0,
            z2: Double = 0.0,
            empty: Boolean = false) {
        minX = x1 min x2
        minY = y1 min y2
        minZ = z1 min z2
        maxX = x1 max x2
        maxY = y1 max y2
        maxZ = z1 max z2
        this.empty = empty
    }

    fun set(corner1: Double3, corner2: Double3,
            empty: Boolean = false) {
        set(corner1.x, corner1.y, corner1.z,
            corner2.x, corner2.y, corner2.z,
            empty)
    }

    fun set(other: Volume) {
        minX = other.minX
        minY = other.minY
        minZ = other.minZ
        maxX = other.maxX
        maxY = other.maxY
        maxZ = other.maxZ
        empty = other.empty
    }

    fun setByCenter(center: Double3, size: Double3, empty: Boolean = false) {
        minX = center.x - 0.5 * size.x
        minY = center.y - 0.5 * size.y
        minZ = center.z - 0.5 * size.z
        maxX = center.x + 0.5 * size.x
        maxY = center.y + 0.5 * size.y
        maxZ = center.z + 0.5 * size.z

        this.empty = empty || size.x < 0 || size.y < 0 || size.z < 0
    }


    /**
     * Sets volume to zero and location to origo.
     */
    fun clear() {
        set(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, true)
    }


    /**
     * Set this Volume to the intersection of itself and the other Volume.
     * If there was no overlap, clears the Volume.
     * @return true if an intersection was found.
     */
    fun setToIntersection(other: Volume): Boolean {
        if (empty) {
            return false
        }
        else if (other.empty) {
            empty = true
            return false
        } else {
            val newMinX = minX max other.minX
            val newMinY = minY max other.minY
            val newMinZ = minZ max other.minZ

            val newMaxX = maxX min other.maxX
            val newMaxY = maxY min other.maxY
            val newMaxZ = maxZ min other.maxZ

            if (newMaxX < newMinX ||
                newMaxY < newMinY ||
                newMaxZ < newMinZ) {
                clear()
                return false
            } else {
                set(newMinX, newMinY, newMinZ,
                    newMaxX, newMaxY, newMaxZ)
                return true
            }
        }
    }

    /**
     * Set this Volume to the union of itself and the other Volume.
     */
    fun setToUnion(other: Volume) {
        if (empty) {
            set(other)
        }
        else if (!other.empty) {
            val newMinX = minX min other.minX
            val newMinY = minY min other.minY
            val newMinZ = minZ min other.minZ

            val newMaxX = maxX max other.maxX
            val newMaxY = maxY max other.maxY
            val newMaxZ = maxZ max other.maxZ

            set(newMinX, newMinY, newMinZ,
                newMaxX, newMaxY, newMaxZ)
        }
    }

    /**
     * Expands (or contracts if negative) the volume by moving all sides away from the center by the specified number of units.
     */
    fun expand(amount: Double) {
        expand(amount, amount, amount)
    }

    /**
     * Expands (or contracts if negative) the volume by moving all sides away from the center by the specified number of units along each axis.
     */
    fun expand(amount: Double3) {
        expand(amount.x, amount.y, amount.z)
    }

    /**
     * Expands (or contracts if negative) the volume by moving all sides away from the center by the specified number of units along each axis.
     */
    fun expand(amountX: Double, amountY: Double, amountZ: Double) {
        // If empty, change to zero-sized at center.
        if (empty) {
            contractToCenter()
        }

        maxX += amountX
        minX -= amountZ
        maxY += amountY
        minY -= amountY
        maxZ += amountZ
        minZ -= amountZ

        // Check if now empty
        if (maxX < minX || maxY < minY || maxZ < minZ) {
            // Change to zero-sized
            contractToCenter()
            empty = true
        }
    }

    /**
     * Sets the volume to a zero-sized non-empty volume located at the current center.
     */
    fun contractToCenter() {
        maxX = average(maxX, minX)
        maxY = average(maxY, minY)
        maxZ = average(maxZ, minZ)
        minX = maxX
        minY = maxY
        minZ = maxZ
        empty = false
    }


}