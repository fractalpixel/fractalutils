package fractalutils.geometry.intvolume

import fractalutils.geometry.int3.Int3
import fractalutils.math.max
import fractalutils.math.min

/**
 * IntVolume that can not be changed after creation.
 */
class ImmutableIntVolume(x1: Int = 0,
                         y1: Int = 0,
                         z1: Int = 0,
                         x2: Int = 0,
                         y2: Int = 0,
                         z2: Int = 0,
                         override val empty: Boolean = false): IntVolumeBase() {

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

    override val minX: Int = x1 min x2
    override val minY: Int = y1 min y2
    override val minZ: Int = z1 min z2
    override val maxX: Int = x1 max x2
    override val maxY: Int = y1 max y2
    override val maxZ: Int = z1 max z2

}