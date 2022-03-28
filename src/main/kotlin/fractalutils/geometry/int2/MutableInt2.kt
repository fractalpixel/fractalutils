package fractalutils.geometry.int2

import fractalutils.geometry.double2.Double2
import fractalutils.math.round


/**
 * Mutable Int2
 */
// TODO: Add listener support?
class MutableInt2(override var x: Int = 0,
                  override var y: Int = 0): Int2Base() {

    constructor(other: Int2): this(other.x, other.y)

    fun zero(): MutableInt2 {
        x = 0
        y = 0
        return this
    }

    fun set(other: Int2): MutableInt2 {
        this.x = other.x
        this.y = other.y
        return this
    }

    /**
     * Rounds the Double2 input to the closest integer.
     */
    fun set(other: Double2): MutableInt2 {
        this.x = other.x.round()
        this.y = other.y.round()
        return this
    }

    fun set(x: Int = this.x, y: Int = this.y): MutableInt2 {
        this.x = x
        this.y = y
        return this
    }

    fun add(other: Int2): MutableInt2 {
        x += other.x
        y += other.y
        return this
    }

    fun sub(other: Int2): MutableInt2 {
        x -= other.x
        y -= other.y
        return this
    }

    fun scale(i: Int): MutableInt2 {
        x *= i
        y *= i
        return this
    }

    fun scale(other: Int2): MutableInt2 {
        x *= other.x
        y *= other.y
        return this
    }

    fun divide(i: Int): MutableInt2 {
        x /= i
        y /= i
        return this
    }

    fun divide(other: Int2): MutableInt2 {
        x /= other.x
        y /= other.y
        return this
    }


}

