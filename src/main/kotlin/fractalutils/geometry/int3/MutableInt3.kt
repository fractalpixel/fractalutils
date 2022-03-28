package fractalutils.geometry.int3

import fractalutils.geometry.double3.Double3
import fractalutils.geometry.int2.Int2
import fractalutils.math.round

/**
 * Mutable Int3
 */
class MutableInt3(override var x: Int = 0,
                  override var y: Int = 0,
                  override var z: Int = 0): Int3Base() {

    constructor(other: Int3): this(other.x, other.y, other.z)
    constructor(other: Int2, z: Int = 0): this(other.x, other.y, z)

    fun zero(): MutableInt3 {
        x = 0
        y = 0
        z = 0
        return this
    }

    fun set(other: Int3): MutableInt3 {
        this.x = other.x
        this.y = other.y
        this.z = other.z
        return this
    }

    /**
     * Rounds the Double3 input to the closest integer.
     */
    fun set(other: Double3): MutableInt3 {
        this.x = other.x.round()
        this.y = other.y.round()
        this.z = other.z.round()
        return this
    }

    fun set(x: Int = this.x, y: Int = this.y, z: Int = this.z): MutableInt3 {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    fun add(other: Int3): MutableInt3 {
        x += other.x
        y += other.y
        z += other.z
        return this
    }

    fun sub(other: Int3): MutableInt3 {
        x -= other.x
        y -= other.y
        z -= other.z
        return this
    }

    fun scale(value: Int): MutableInt3 {
        x *= value
        y *= value
        z *= value
        return this
    }

    fun scale(other: Int3): MutableInt3 {
        x *= other.x
        y *= other.y
        z *= other.z
        return this
    }

    fun divide(i: Int): MutableInt3 {
        x /= i
        y /= i
        z /= i
        return this
    }

    fun divide(other: Int3): MutableInt3 {
        x /= other.x
        y /= other.y
        z /= other.z
        return this
    }

}

