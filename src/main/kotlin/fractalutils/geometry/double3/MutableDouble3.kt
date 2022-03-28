package fractalutils.geometry.double3

import fractalutils.geometry.double2.Double2
import fractalutils.geometry.int3.Int3

/**
 * Mutable implementation of Double3
 */
class MutableDouble3(override var x: Double = 0.0,
                     override var y: Double = 0.0,
                     override var z: Double = 0.0): Double3Base() {

    constructor(other: Double3): this(other.x, other.y, other.z)
    constructor(other: Double2, z: Double = 0.0): this(other.x, other.y, z)

    fun zero(): MutableDouble3 {
        x = 0.0
        y = 0.0
        z = 0.0
        return this
    }

    fun set(x: Double = this.x,
            y: Double = this.y,
            z: Double = this.z): MutableDouble3 {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    fun set(other: Double3): MutableDouble3 {
        this.x = other.x
        this.y = other.y
        this.z = other.z
        return this
    }

    fun set(other: Int3): MutableDouble3 {
        this.x = other.x.toDouble()
        this.y = other.y.toDouble()
        this.z = other.z.toDouble()
        return this
    }

    fun add(other: Double3): MutableDouble3 {
        x += other.x
        y += other.y
        z += other.z
        return this
    }

    fun add(otherX: Double,
            otherY: Double,
            otherZ: Double): MutableDouble3 {
        x += otherX
        y += otherY
        z += otherZ
        return this
    }

    fun addScaled(other: Double3, scale: Double): MutableDouble3 {
        x += other.x * scale
        y += other.y * scale
        z += other.z * scale
        return this
    }

    fun addScaled(other: Double3, scale: Double3): MutableDouble3 {
        x += other.x * scale.x
        y += other.y * scale.y
        z += other.z * scale.z
        return this
    }

    fun sub(other: Double3): MutableDouble3 {
        x -= other.x
        y -= other.y
        z -= other.z
        return this
    }

    fun sub(otherX: Double,
            otherY: Double,
            otherZ: Double): MutableDouble3 {
        x -= otherX
        y -= otherY
        z -= otherZ
        return this
    }

    fun scale(scale: Double): MutableDouble3 {
        x *= scale
        y *= scale
        z *= scale
        return this
    }

    fun scale(other: Double3): MutableDouble3 {
        x *= other.x
        y *= other.y
        z *= other.z
        return this
    }

    fun scale(otherX: Double,
              otherY: Double,
              otherZ: Double): Double3 {
        x *= otherX
        y *= otherY
        z *= otherZ
        return this
    }

    fun divide(d: Double): Double3 {
        x /= d
        y /= d
        z /= d
        return this
    }

    fun divide(other: Double3): Double3 {
        x /= other.x
        y /= other.y
        z /= other.z
        return this
    }

    fun divide(otherX: Double,
               otherY: Double,
               otherZ: Double): Double3 {
        x /= otherX
        y /= otherY
        z /= otherZ
        return this
    }



}
