package fractalutils.geometry.intvolume

import fractalutils.geometry.int3.Int3
import fractalutils.math.max
import fractalutils.math.min

/**
 * An IntVolume that can be changed.
 */
class MutableIntVolume(x1: Int = 0,
                       y1: Int = 0,
                       z1: Int = 0,
                       x2: Int = 0,
                       y2: Int = 0,
                       z2: Int = 0,
                       empty: Boolean = false): IntVolumeBase() {

    constructor(corner1: Int3, corner2: Int3, empty: Boolean = false): this(
            corner1.x min corner2.x,
            corner1.y min corner2.y,
            corner1.z min corner2.z,
            corner1.x max corner2.x,
            corner1.y max corner2.y,
            corner1.z max corner2.z,
            empty)

    constructor(other: IntVolume): this(other.minX,
                                     other.minY,
                                     other.minZ,
                                     other.maxX,
                                     other.maxY,
                                     other.maxZ,
                                     other.empty)

    override var empty: Boolean = empty
        private set
    override var minX: Int = x1 min x2
        private set
    override var minY: Int = y1 min y2
        private set
    override var minZ: Int = z1 min z2
        private set
    override var maxX: Int = x1 max x2
        private set
    override var maxY: Int = y1 max y2
        private set
    override var maxZ: Int = z1 max z2
        private set


    fun set(x1: Int = 0,
            y1: Int = 0,
            z1: Int = 0,
            x2: Int = 0,
            y2: Int = 0,
            z2: Int = 0,
            empty: Boolean = false) {
        minX = x1 min x2
        minY = y1 min y2
        minZ = z1 min z2
        maxX = x1 max x2
        maxY = y1 max y2
        maxZ = z1 max z2
        this.empty = empty
    }

    fun set(corner1: Int3, corner2: Int3,
            empty: Boolean = false) {
        set(corner1.x, corner1.y, corner1.z,
            corner2.x, corner2.y, corner2.z,
            empty)
    }

    fun set(other: IntVolume) {
        minX = other.minX
        minY = other.minY
        minZ = other.minZ
        maxX = other.maxX
        maxY = other.maxY
        maxZ = other.maxZ
        empty = other.empty
    }

    /**
     * Sets the volume position by specifying one corner and the delta (size) along each axis to the opposite corner.
     * A delta coordinate may be negative as well.
     */
    fun setByCorner(corner: Int3, delta: Int3, empty: Boolean = false) {
        setByCorner(corner.x, corner.y, corner.z,
                    delta.x, delta.y, delta.z,
                    empty)
    }

    /**
     * Sets the volume position by specifying one corner and the delta (size) along each axis to the opposite corner.
     * A delta may be negative as well.
     */
    fun setByCorner(cornerX: Int, cornerY: Int, cornerZ: Int,
                    deltaX: Int, deltaY: Int, deltaZ: Int,
                    empty: Boolean = false) {
        set(cornerX, cornerY, cornerZ,
            cornerX + deltaX, cornerY + deltaY, cornerZ + deltaZ,
            empty)
    }


    /**
     * Sets the volume position by specifying the center and the size.
     * If a size coordinate is negative, the volume will be marked as empty.
     */
    fun setByCenter(center: Int3, size: Int3, empty: Boolean = false) {
        setByCenter(center.x, center.y, center.z,
                    size.x, size.y, size.z,
                    empty)
    }

    /**
     * Sets the volume position by specifying the center and the size.
     * If a size coordinate is negative, the volume will be marked as empty.
     */
    fun setByCenter(centerX: Int, centerY: Int, centerZ: Int,
                    sizeX: Int, sizeY: Int, sizeZ: Int,
                    empty: Boolean = false) {
        minX = centerX - sizeX / 2
        minY = centerY - sizeY / 2
        minZ = centerZ - sizeZ / 2
        maxX = minX + sizeX
        maxY = minY + sizeY
        maxZ = minZ + sizeZ

        this.empty = empty || sizeX < 0 || sizeY < 0 || sizeZ < 0
    }

    /**
     * Moves this volume by the specified delta along each axis.
     */
    fun move(dx: Int, dy: Int, dz: Int) {
        minX += dx
        maxX += dx
        minY += dy
        maxY += dy
        minZ += dz
        maxZ += dz
    }


    /**
     * Sets volume to zero and location to origo.
     */
    fun clear() {
        set(0, 0, 0, 0, 0, 0, true)
    }


    /**
     * Set this Volume to the intersection of itself and the other Volume.
     * If there was no overlap, clears the Volume.
     * @return true if an intersection was found.
     */
    fun setToIntersection(other: IntVolume): Boolean {
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
    fun setToUnion(other: IntVolume) {
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
    fun expand(amount: Int) {
        expand(amount, amount, amount)
    }

    /**
     * Expands (or contracts if negative) the volume by moving all sides away from the center by the specified number of units.
     */
    fun expand(amount: Int3) {
        expand(amount.x, amount.y, amount.z)
    }

    /**
     * Expands (or contracts if negative) the volume by moving all sides away from the center by the specified number of units.
     */
    fun expand(amountX: Int, amountY: Int, amountZ: Int) {
        // If empty, change to zero-sized at center.
        if (empty) {
            contractToCenter()
        }

        maxX += amountX
        minX -= amountX
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
        maxX = (maxX + minX) / 2
        maxY = (maxY + minY) / 2
        maxZ = (maxZ + minZ) / 2
        minX = maxX
        minY = maxY
        minZ = maxZ
        empty = false
    }
}