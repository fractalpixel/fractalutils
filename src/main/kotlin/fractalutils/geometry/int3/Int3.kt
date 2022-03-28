package fractalutils.geometry.int3

import fractalutils.geometry.double2.MutableDouble2
import fractalutils.geometry.double3.Double3
import fractalutils.geometry.double3.MutableDouble3
import fractalutils.geometry.int2.MutableInt2


/**
 * Simple 3D integer point / vector
 */
interface Int3 {

    val x: Int
    val y: Int
    val z: Int


    operator fun plus(other: Int3): MutableInt3 =
            MutableInt3(x + other.x,
                    y + other.y,
                    z + other.z)

    operator fun minus(other: Int3): MutableInt3 =
            MutableInt3(x - other.x,
                    y - other.y,
                    z - other.z)

    operator fun times(other: Int3): MutableInt3 =
            MutableInt3(x * other.x,
                    y * other.y,
                    z * other.z)

    operator fun div(other: Int3): MutableInt3 =
            MutableInt3(x / other.x,
                    y / other.y,
                    z / other.z)

    operator fun times(scalar: Int): MutableInt3 =
            MutableInt3(x * scalar,
                    y * scalar,
                    z * scalar)

    operator fun div(scalar: Int): MutableInt3 =
            MutableInt3(x / scalar,
                    y / scalar,
                    z / scalar)

    fun gridOrigo(gridSize: Int3): MutableInt3 {
        val v = floorDiv(gridSize)
        v.scale(gridSize)
        return v
    }

    fun floorDiv(other: Int3): MutableInt3 {
        fun floorDiv(v: Int, size: Int): Int = (v / size) - if (v < 0) 1 else 0
        return MutableInt3(floorDiv(x, other.x),
                floorDiv(y, other.y),
                floorDiv(z, other.z))
    }

    fun project(scale: Double3,
                offset: Double3,
                out: MutableDouble3 = MutableDouble3()
    ): MutableDouble3 {
        out.set(this)
        out.scale(scale)
        out.add(offset)
        return out
    }

    fun inRange(ends: Int3): Boolean = inRange(endX = ends.x,
                                               endY = ends.y,
                                               endZ = ends.z)

    fun inRange(starts: Int3, ends: Int3): Boolean = inRange(
            starts.x, starts.y, starts.z,
            ends.x, ends.y, ends.z)

    fun inRange(startX: Int = 0,
                startY: Int = 0,
                startZ: Int = 0,
                endX: Int = 0,
                endY: Int = 0,
                endZ: Int = 0): Boolean =
            x >= startX && x < endX &&
            y >= startY && y < endY &&
            z >= startZ && z < endZ


    fun scaleSum(xScale: Int = 1, yScale: Int = 1, zScale: Int = 1): Int = x * xScale + y * yScale + z * zScale

    fun multiplyAll(xOffset: Int = 0, yOffset: Int = 0, zOffset: Int = 0): Int = (x+xOffset) * (y+yOffset) * (z+zOffset)

    fun toInt2(): MutableInt2 = MutableInt2(x, y)
    fun toDouble2(): MutableDouble2 = MutableDouble2(x.toDouble(), y.toDouble())
    fun toDouble3(): MutableDouble3 = MutableDouble3(x.toDouble(), y.toDouble(), z.toDouble())


    /**
     * Treat this as a position in a 3D integer volume, return an index in a one dimensional array assuming grid cells
     * are stored with z major, x minor order.  Returns null if the specified position is outside this volume.
     */
    fun toIndex(volumeSize: Int3): Int? {
        return if (inRange(volumeSize)) {
            z * volumeSize.x * volumeSize.y +
            y * volumeSize.x +
            x
        }
        else null
    }


    companion object {
        val ZEROES = ImmutableInt3(0, 0, 0)
        val ONES = ImmutableInt3(1, 1, 1)
        val X_AXIS = ImmutableInt3(1, 0, 0)
        val Y_AXIS = ImmutableInt3(0, 1, 0)
        val Z_AXIS = ImmutableInt3(0, 0, 1)
    }
}

/*
fun Vector3.toInt3Rounded(): Int3 =
        Int3(MathUtils.round(this.x),
                MathUtils.round(this.y),
                MathUtils.round(this.z))

fun Vector3.toInt3Floored(): Int3 =
        Int3(MathUtils.fastFloor(this.x),
                MathUtils.fastFloor(this.y),
                MathUtils.fastFloor(this.z))
*/

