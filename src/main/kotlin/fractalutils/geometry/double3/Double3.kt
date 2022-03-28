package fractalutils.geometry.double3

import fractalutils.geometry.double2.MutableDouble2
import fractalutils.geometry.int2.MutableInt2
import fractalutils.geometry.int3.MutableInt3
import fractalutils.math.fastFloor
import fractalutils.math.round


/**
 * 3D point or vector with double coordinate values.
 */
interface Double3 {

    val x: Double
    val y: Double
    val z: Double


    operator fun plus(other: Double3): MutableDouble3 =
            MutableDouble3(x + other.x,
                    y + other.y,
                    z + other.z)

    operator fun minus(other: Double3): MutableDouble3 =
            MutableDouble3(x - other.x,
                    y - other.y,
                    z - other.z)

    operator fun times(other: Double3): MutableDouble3 =
            MutableDouble3(x * other.x,
                    y * other.y,
                    z * other.z)

    operator fun div(other: Double3): MutableDouble3 =
            MutableDouble3(x / other.x,
                    y / other.y,
                    z / other.z)

    operator fun times(scalar: Double): MutableDouble3 =
            MutableDouble3(x * scalar,
                    y * scalar,
                    z * scalar)

    operator fun div(scalar: Double): MutableDouble3 =
            MutableDouble3(x / scalar,
                    y / scalar,
                    z / scalar)

    /**
     * Can be used to calculate the grid cell coordinates for a point, given the grid size.
     */
    fun floorDiv(gridSize: Double3,
                 gridOffset: Double3 = ZEROES,
                 out: MutableInt3 = MutableInt3()
    ): MutableInt3 {
        out.set(((x - gridOffset.x) / gridSize.x).fastFloor(),
                ((y - gridOffset.y) / gridSize.y).fastFloor(),
                ((z - gridOffset.z) / gridSize.z).fastFloor())
        return out
    }

    /**
     * Can be used to calculate the origin of the grid cell that a point is in, given the grid size.
     */
    fun gridOrigo(gridSize: Double3,
                  gridOffset: Double3 = ZEROES,
                  out: MutableDouble3 = MutableDouble3()
    ): MutableDouble3 {
        out.set(((x - gridOffset.x) / gridSize.x).fastFloor() * gridSize.x,
                ((y - gridOffset.y) / gridSize.y).fastFloor() * gridSize.y,
                ((z - gridOffset.z) / gridSize.z).fastFloor() * gridSize.z)
        return out
    }

    /**
     * True if this point is in the range 0,0,0 (inclusive) to ends (exclusive)
     */
    fun inRange(ends: Double3): Boolean = inRange(endX = ends.x,
                                                  endY = ends.y,
                                                  endZ = ends.z)

    /**
     * True if this point is in the specified range (inclusive start, exclusive end)
     */
    fun inRange(starts: Double3, ends: Double3): Boolean = inRange(
            starts.x, starts.y, starts.z,
            ends.x, ends.y, ends.z)

    /**
     * True if this point is in the specified range (inclusive start, exclusive end)
     */
    fun inRange(startX: Double = 0.0,
                startY: Double = 0.0,
                startZ: Double = 0.0,
                endX: Double = 0.0,
                endY: Double = 0.0,
                endZ: Double = 0.0): Boolean =
            x >= startX && x < endX &&
            y >= startY && y < endY &&
            z >= startZ && z < endZ

    /**
     * Scales each coordinate with the specified scale and returns the sum.
     */
    fun scaleSum(xScale: Double = 1.0,
                 yScale: Double = 1.0,
                 zScale: Double = 1.0): Double = x * xScale +
                                                 y * yScale +
                                                 z * zScale

    /**
     * Adds the specified offset to each coordinate and multiplies them together.
     */
    fun multiplyAll(xOffset: Double = 0.0,
                    yOffset: Double = 0.0,
                    zOffset: Double = 0.0): Double = (x+xOffset) *
                                                     (y+yOffset) *
                                                     (z+zOffset)

    fun toInt3(): MutableInt3 = MutableInt3(x.toInt(), y.toInt(), z.toInt())
    fun toInt2(): MutableInt2 = MutableInt2(x.toInt(), y.toInt())
    fun toDouble2(): MutableDouble2 = MutableDouble2(x, y)

    fun floor(): MutableInt3 = MutableInt3(x.fastFloor(), y.fastFloor(), z.fastFloor())

    fun round(): MutableInt3 = MutableInt3(x.round(), y.round(), z.round())

    fun length(): Double = Math.sqrt(x * x + y * y + z * z)
    fun lengthSquared(): Double = x * x + y * y + z * z

    fun distanceTo(other: Double3): Double {
        val dx = other.x - x
        val dy = other.y - y
        val dz = other.z - z
        return Math.sqrt(dx*dx + dy*dy + dz*dz)
    }

    fun distanceSquaredTo(other: Double3): Double {
        val dx = other.x - x
        val dy = other.y - y
        val dz = other.z - z
        return dx*dx + dy*dy + dz*dz
    }

    companion object {
        val ZEROES = ImmutableDouble3(0.0, 0.0, 0.0)
        val HALFS = ImmutableDouble3(0.5, 0.5, 0.5)
        val ONES = ImmutableDouble3(1.0, 1.0, 1.0)
        val MINUS_ONES = ImmutableDouble3(-1.0, -1.0, -1.0)
        val X_AXIS = ImmutableDouble3(1.0, 0.0, 0.0)
        val Y_AXIS = ImmutableDouble3(0.0, 1.0, 0.0)
        val Z_AXIS = ImmutableDouble3(0.0, 0.0, 1.0)
        val MINUS_X_AXIS = ImmutableDouble3(-1.0, 0.0, 0.0)
        val MINUS_Y_AXIS = ImmutableDouble3(0.0, -1.0, 0.0)
        val MINUS_Z_AXIS = ImmutableDouble3(0.0, 0.0, -1.0)
        val NEGATIVE_INFINITY = ImmutableDouble3(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
        val POSITIVE_INFINITY = ImmutableDouble3(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
    }
}
