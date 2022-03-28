package fractalutils.geometry.double2

import fractalutils.geometry.int2.Int2


/**
 * Modifiable Double2
 */
class MutableDouble2(override var x: Double = 0.0,
                     override var y: Double = 0.0): Double2Base() {

    constructor(other: Double2) : this(other.x, other.y)

    fun zero(): MutableDouble2 {
        x = 0.0
        y = 0.0
        return this
    }

    fun set(x: Double = this.x, y: Double = this.y): MutableDouble2 {
        this.x = x
        this.y = y
        return this
    }

    fun set(other: Double2): MutableDouble2 {
        this.x = other.x
        this.y = other.y
        return this
    }

    fun set(other: Int2): MutableDouble2 {
        this.x = other.x.toDouble()
        this.y = other.y.toDouble()
        return this
    }

    fun add(other: Double2): MutableDouble2 {
        x += other.x
        y += other.y
        return this
    }

    fun add(otherX: Double, otherY: Double): MutableDouble2 {
        x += otherX
        y += otherY
        return this
    }

    fun addScaled(other: Double2, scale: Double): MutableDouble2 {
        x += other.x * scale
        y += other.y * scale
        return this
    }

    fun addScaled(other: Double2, scale: Double2): MutableDouble2 {
        x += other.x * scale.x
        y += other.y * scale.y
        return this
    }

    fun sub(other: Double2): MutableDouble2 {
        x -= other.x
        y -= other.y
        return this
    }

    fun sub(otherX: Double, otherY: Double): MutableDouble2 {
        x -= otherX
        y -= otherY
        return this
    }

    fun scale(scale: Double): MutableDouble2 {
        x *= scale
        y *= scale
        return this
    }

    fun scale(other: Double2): MutableDouble2 {
        x *= other.x
        y *= other.y
        return this
    }

    fun scale(otherX: Double, otherY: Double): MutableDouble2 {
        x *= otherX
        y *= otherY
        return this
    }

    fun divide(d: Double): MutableDouble2 {
        x /= d
        y /= d
        return this
    }

    fun divide(other: Double2): MutableDouble2 {
        x /= other.x
        y /= other.y
        return this
    }

    fun divide(otherX: Double, otherY: Double): MutableDouble2 {
        x /= otherX
        y /= otherY
        return this
    }


}

