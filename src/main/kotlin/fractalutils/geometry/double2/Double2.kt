package fractalutils.geometry.double2

import fractalutils.geometry.double3.Double3
import fractalutils.geometry.double3.MutableDouble3
import fractalutils.geometry.int2.MutableInt2
import fractalutils.geometry.int3.MutableInt3
import fractalutils.math.fastFloor
import fractalutils.math.round


/**
 * Simple 2D point or vector with double coordinate values.
 */
interface Double2 {

    val x: Double
    val y: Double

    operator fun plus(other: Double2): MutableDouble2 =
            MutableDouble2(x + other.x,
                    y + other.y)

    operator fun minus(other: Double2): MutableDouble2 =
            MutableDouble2(x - other.x,
                    y - other.y)

    operator fun times(other: Double2): MutableDouble2 =
            MutableDouble2(x * other.x,
                    y * other.y)

    operator fun div(other: Double2): MutableDouble2 =
            MutableDouble2(x / other.x,
                    y / other.y)

    operator fun times(scalar: Double): MutableDouble2 =
            MutableDouble2(x * scalar,
                    y * scalar)

    operator fun div(scalar: Double): MutableDouble2 =
            MutableDouble2(x / scalar,
                    y / scalar)


    /**
     * Can be used to calculate the grid cell coordinates for a point, given the grid size.
     */
    fun floorDiv(gridSize: Double2,
                 gridOffset: Double2 = ZEROES,
                 out: MutableInt2 = MutableInt2()
    ): MutableInt2 {
        out.set(((x - gridOffset.x) / gridSize.x).fastFloor(),
                ((y - gridOffset.y) / gridSize.y).fastFloor())
        return out
    }

    /**
     * Can be used to calculate the origin of the grid cell that a point is in, given the grid size.
     */
    fun gridOrigo(gridSize: Double2,
                  gridOffset: Double2 = ZEROES,
                  out: MutableDouble2 = MutableDouble2()
    ): MutableDouble2 {
        out.set(((x - gridOffset.x) / gridSize.x).fastFloor() * gridSize.x,
                ((y - gridOffset.y) / gridSize.y).fastFloor() * gridSize.y)
        return out
    }

    /**
     * True if this point is in the range 0,0 (inclusive) to ends (exclusive)
     */
    fun inRange(ends: Double2): Boolean = inRange(endX = ends.x, endY = ends.y)

    /**
     * True if this point is in the specified range (inclusive start, exclusive end)
     */
    fun inRange(starts: Double2, ends: Double2): Boolean = inRange(
            starts.x, starts.y,
            ends.x, ends.y)

    /**
     * True if this point is in the specified range (inclusive start, exclusive end)
     */
    fun inRange(startX: Double = 0.0,
                startY: Double = 0.0,
                endX: Double = 0.0,
                endY: Double = 0.0): Boolean =
            x >= startX && x < endX &&
            y >= startY && y < endY

    /**
     * Scales each coordinate with the specified scale and returns the sum.
     */
    fun scaleSum(xScale: Double = 1.0, yScale: Double = 1.0): Double = x * xScale + y * yScale

    /**
     * Adds the specified offset to each coordinate and multiplies them together.
     */
    fun multiplyAll(xOffset: Double = 0.0, yOffset: Double = 0.0): Double = (x+xOffset) * (y+yOffset)

    fun toInt3(z: Int = 0): MutableInt3 = MutableInt3(x.toInt(), y.toInt(), z)
    fun toInt2(): MutableInt2 = MutableInt2(x.toInt(), y.toInt())
    fun toDouble3(z: Double = 0.0): MutableDouble3 = MutableDouble3(x, y, z)

    fun floor(): MutableInt2 = MutableInt2(x.fastFloor(), y.fastFloor())

    fun round(): MutableInt2 = MutableInt2(x.round(), y.round())

    fun length(): Double = Math.sqrt(x * x + y * y)
    fun lengthSquared(): Double = x * x + y * y


    fun distanceTo(other: Double3): Double {
        val dx = other.x - x
        val dy = other.y - y
        return Math.sqrt(dx*dx + dy*dy)
    }

    fun distanceSquaredTo(other: Double3): Double {
        val dx = other.x - x
        val dy = other.y - y
        return dx*dx + dy*dy
    }


    companion object {
        val ZEROES = ImmutableDouble2(0.0, 0.0)
        val HALFS = ImmutableDouble2(0.5, 0.5)
        val ONES = ImmutableDouble2(1.0, 1.0)
        val X_AXIS = ImmutableDouble2(1.0, 0.0)
        val Y_AXIS = ImmutableDouble2(0.0, 1.0)
    }

}
