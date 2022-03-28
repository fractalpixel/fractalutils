package fractalutils.geometry.volume

import fractalutils.geometry.double3.Double3
import fractalutils.geometry.double3.MutableDouble3
import fractalutils.geometry.intvolume.MutableIntVolume
import fractalutils.math.fastFloor
import fractalutils.math.round

/**
 * A volume in space.
 * May also be marked as empty (not occupying any spot).
 */
interface Volume {

    /**
     * @return true if the Volume represents no area, that is, it can not overlap with anything or contain anything.
     *              Used for uninitialized / cleared mutable Volumes.
     */
    val empty: Boolean

    val minX: Double
    val minY: Double
    val minZ: Double
    val maxX: Double
    val maxY: Double
    val maxZ: Double

    val sizeX: Double get() = maxX - minX
    val sizeY: Double get() = maxY - minY
    val sizeZ: Double get() = maxZ - minZ

    val centerX: Double get() = (minX + maxX) * 0.5
    val centerY: Double get() = (minY + maxY) * 0.5
    val centerZ: Double get() = (minZ + maxZ) * 0.5

    fun getMinCorner(out: MutableDouble3 = MutableDouble3()): MutableDouble3 {
        out.set(minX, minY, minZ)
        return out
    }

    fun getMaxCorner(out: MutableDouble3 = MutableDouble3()): MutableDouble3 {
        out.set(maxX, maxY, maxZ)
        return out
    }

    fun getCenter(out: MutableDouble3 = MutableDouble3()): MutableDouble3 {
        out.set(centerX, centerY, centerZ)
        return out
    }

    fun getSize(out: MutableDouble3 = MutableDouble3()): MutableDouble3 {
        out.set(sizeX, sizeY, sizeZ)
        return out
    }


    /**
     * @return true if the point is within the Volume.
     */
    operator fun contains(p: Double3): Boolean = contains(p.x, p.y, p.z)

    /**
     * @return true if the coordinate is within the Volume.
     */
    fun contains(x: Double, y: Double, z: Double): Boolean =
            !empty && (x in minX .. maxX &&
                       y in minY .. maxY &&
                       z in minZ .. maxZ)

    /**
     * @return true if the specified Volume is contained inside this Volume.
     */
    operator fun contains(volume: Volume): Boolean =
            !empty &&
            !volume.empty && (
                    volume.minX >= minX &&
                    volume.maxX <= maxX &&
                    volume.minY >= minY &&
                    volume.maxY <= maxY &&
                    volume.minZ >= minZ &&
                    volume.maxZ <= maxZ)

    /**
     * @return true if the specified Volume overlaps this Volume (including just an edge or corner).
     */
    fun intersects(volume: Volume): Boolean =
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
    infix fun intersection(other: Volume): Volume {
        val intersection = MutableVolume(this)
        intersection.setToIntersection(other)
        return intersection
    }

    /**
     * Returns a new Volume with the union of this Volume and the other Volume.
     */
    infix fun union(other: Volume): Volume {
        val union = MutableVolume(this)
        union.setToUnion(other)
        return union
    }

    /**
     * Project this volume to an integer volume, using the specified grid size and offset for the int grid.
     */
    fun projectToGrid(gridSize: Double3,
                      gridOffset: Double3 = Double3.ZEROES,
                      out: MutableIntVolume = MutableIntVolume()
    ): MutableIntVolume {
        out.set(((minX - gridOffset.x) / gridSize.x).fastFloor(),
                ((minY - gridOffset.y) / gridSize.y).fastFloor(),
                ((minZ - gridOffset.z) / gridSize.z).fastFloor(),
                ((maxX - gridOffset.x) / gridSize.x).fastFloor(),
                ((maxY - gridOffset.y) / gridSize.y).fastFloor(),
                ((maxZ - gridOffset.z) / gridSize.z).fastFloor(),
                empty)
        return out
    }

    /**
     * Returns a new mutable int volume based on this volume, with rounded sizes.
     */
    fun toIntVolume(): MutableIntVolume = MutableIntVolume(minX.round(), minY.round(), minZ.round(),
                                                           maxX.round(), maxY.round(), maxZ.round(),
                                                           empty)
}