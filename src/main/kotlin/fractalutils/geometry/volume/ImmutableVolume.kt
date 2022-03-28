package fractalutils.geometry.volume

import fractalutils.geometry.double3.Double3
import fractalutils.math.max
import fractalutils.math.min

/**
 * Volume that can not be changed after creation.
 */
class ImmutableVolume(x1: Double = 0.0,
                      y1: Double = 0.0,
                      z1: Double = 0.0,
                      x2: Double = 0.0,
                      y2: Double = 0.0,
                      z2: Double = 0.0,
                      override val empty: Boolean = false): VolumeBase() {

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

    override val minX: Double = x1 min x2
    override val minY: Double = y1 min y2
    override val minZ: Double = z1 min z2
    override val maxX: Double = x1 max x2
    override val maxY: Double = y1 max y2
    override val maxZ: Double = z1 max z2

}