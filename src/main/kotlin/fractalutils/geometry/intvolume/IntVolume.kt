package fractalutils.geometry.intvolume

import fractalutils.geometry.double3.Double3
import fractalutils.geometry.double3.MutableDouble3
import fractalutils.geometry.int3.Int3
import fractalutils.geometry.int3.MutableInt3
import fractalutils.geometry.volume.MutableVolume

/**
 * A volume in space with integer coordinates.
 * May also be marked as empty (not occupying any spot).
 */
interface IntVolume {


    /**
     * @return true if the Volume represents no area, that is, it can not overlap with anything or contain anything.
     *              Used for uninitialized / cleared mutable Volumes.
     */
    val empty: Boolean

    val minX: Int
    val minY: Int
    val minZ: Int
    val maxX: Int
    val maxY: Int
    val maxZ: Int

    val sizeX: Int get() = maxX - minX
    val sizeY: Int get() = maxY - minY
    val sizeZ: Int get() = maxZ - minZ

    val centerX: Double get() = (minX + maxX) * 0.5
    val centerY: Double get() = (minY + maxY) * 0.5
    val centerZ: Double get() = (minZ + maxZ) * 0.5

    fun getMinCorner(out: MutableInt3 = MutableInt3()): MutableInt3 {
        out.set(minX, minY, minZ)
        return out
    }

    fun getMaxCorner(out: MutableInt3 = MutableInt3()): MutableInt3 {
        out.set(maxX, maxY, maxZ)
        return out
    }

    fun getCenter(out: MutableDouble3 = MutableDouble3()): MutableDouble3 {
        out.set(centerX, centerY, centerZ)
        return out
    }

    fun getSize(out: MutableInt3 = MutableInt3()): MutableInt3 {
        out.set(sizeX, sizeY, sizeZ)
        return out
    }

    /**
     * Calls the visitor for each coordinate that exists in this volume,
     * if this volume is not empty.
     * Visited in ascending order, z loops slowest, x fastest.
     */
    fun forEach(visitor: (Int3) -> Unit,
                tempPos: MutableInt3 = MutableInt3()
    ) {
        if (!empty) {
            for (z in minZ .. maxZ) {
                for (y in minY .. maxY) {
                    for (x in minX .. maxX) {
                        tempPos.set(x, y, z)
                        visitor(tempPos)
                    }
                }
            }
        }
    }

    /**
     * @return true if the point is within the Volume.
     */
    operator fun contains(p: Int3): Boolean = contains(p.x, p.y, p.z)

    /**
     * @return true if the coordinate is within the Volume.
     */
    fun contains(x: Int, y: Int, z: Int): Boolean =
            !empty && (x in minX .. maxX &&
                       y in minY .. maxY &&
                       z in minZ .. maxZ)


    /**
     * @return true if the specified Volume is contained inside this Volume.
     */
    operator fun contains(volume: IntVolume): Boolean =
            !empty &&
            !volume.empty && (
                    volume.minX >= minX &&
                    volume.maxX <= maxX &&
                    volume.minY >= minY &&
                    volume.maxY <= maxY &&
                    volume.minZ >= minZ &&
                    volume.maxZ <= maxZ)

    /**
     * @return true if the coordinate is within the Volume expanded by the specified amount.
     */
    fun containsExpanded(pos: Int3, expand: Int): Boolean =
            containsExpanded(pos.x, pos.y, pos.z, expand)

    /**
     * @return true if the coordinate is within the Volume expanded by the specified amount.
     */
    fun containsExpanded(pos: Int3, expand: Int3): Boolean =
            containsExpanded(pos.x, pos.y, pos.z,
                             expand.x, expand.y, expand.z)

    /**
     * @return true if the coordinate is within the Volume expanded by the specified amount.
     */
    fun containsExpanded(x: Int, y: Int, z: Int,
                         expand: Int): Boolean =
            containsExpanded(x, y, z, expand, expand, expand)

    /**
     * @return true if the coordinate is within the Volume expanded by the specified amount.
     */
    fun containsExpanded(x: Int, y: Int, z: Int,
                         expandX: Int, expandY: Int, expandZ: Int): Boolean =
            !empty && (x in minX-expandX .. maxX+expandX &&
                       y in minY-expandY .. maxY+expandY &&
                       z in minZ-expandZ .. maxZ+expandZ)

    /**
     * @return true if the specified Volume overlaps this Volume (including just an edge or corner).
     */
    fun intersects(volume: IntVolume): Boolean =
            !empty &&
            !volume.empty && (
                    volume.minX <= maxX &&
                    volume.maxX >= minX &&
                    volume.minY <= maxY &&
                    volume.maxY >= minY &&
                    volume.minZ <= maxZ &&
                    volume.maxZ >= minZ)

    /**
     * Returns a new Volume with the intersection of this Volume and the other Volume.
     */
    infix fun intersection(other: IntVolume): IntVolume {
        val intersection = MutableIntVolume(this)
        intersection.setToIntersection(other)
        return intersection
    }

    /**
     * Returns a new Volume with the union of this Volume and the other Volume.
     */
    infix fun union(other: IntVolume): IntVolume {
        val union = MutableIntVolume(this)
        union.setToUnion(other)
        return union
    }

    /**
     * Project this int volume to an volume, using the specified grid size and offset
     */
    fun projectToWorld(gridSize: Double3,
                       gridOffset: Double3 = Double3.ZEROES,
                       out: MutableVolume = MutableVolume()
    ): MutableVolume {
        out.set(minX * gridSize.x + gridOffset.x,
                minY * gridSize.y + gridOffset.y,
                minZ * gridSize.z + gridOffset.z,
                maxX * gridSize.x + gridOffset.x,
                maxY * gridSize.y + gridOffset.y,
                maxZ * gridSize.z + gridOffset.z,
                empty)
        return out
    }

    /**
     * Get an index for the specified grid pos, assuming this volume corresponds to grid cells stored in a one dimensional array,
     * with z major, x minor order, or null if the specified position is outside this volume
     */
    fun getIndex(gridPos: Int3): Int? {
        return if (contains(gridPos)) {
            (gridPos.z - minZ) * sizeX * sizeY +
            (gridPos.y - minY) * sizeX +
            (gridPos.x - minX)
        } else null
    }

    /**
     * @return new mutable double based Volume, using this volume as the initial values.
     */
    fun toVolume(): MutableVolume = MutableVolume(minX.toDouble(), minY.toDouble(), minZ.toDouble(),
                                                  maxX.toDouble(), maxY.toDouble(), maxZ.toDouble(),
                                                  empty)


}