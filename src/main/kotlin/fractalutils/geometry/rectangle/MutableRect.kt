package fractalutils.geometry.rectangle

import fractalutils.checking.Check
import fractalutils.geometry.double2.Double2
import fractalutils.geometry.double2.ImmutableDouble2
import fractalutils.math.max
import fractalutils.math.min

/**
 * Rectangle implementation that can be changed.
 * Provides support for listening to changes.
 */
class MutableRect(x: Double,
                  y: Double,
                  w: Double,
                  h: Double,
                  empty: Boolean = false) : Rect {

    constructor(w: Double,
                h: Double,
                empty: Boolean = false) : this(0.0, 0.0, w, h, empty)

    constructor(size: Double2,
                empty: Boolean = false) : this(0.0, 0.0, size.x, size.y, empty)

    constructor(pos: Double2,
                size: Double2,
                empty: Boolean = false) : this(pos.x, pos.y, size.x, size.y, empty)

    constructor(empty: Boolean = false) : this(
            if (empty) 0.0 else 1.0,
            if (empty) 0.0 else 1.0,
            empty)

    constructor(rectangle: Rect) : this(rectangle.x, rectangle.y, rectangle.w, rectangle.h, rectangle.empty) {}

    private var listeners: MutableMap<RectListener<Any?>, Any?>? = null

    override var x: Double = x
        set(value) {
            if (field != value) {
                Check.normalNumber(value, "x")
                field = value
                notifyListenersAboutChange()
            }
        }

    override var y: Double = y
        set(value) {
            if (field != value) {
                Check.normalNumber(value, "y")
                field = value
                notifyListenersAboutChange()
            }
        }

    override var w: Double = w
        set(value) {
            if (field != value) {
                Check.positiveOrZero(value, "w")
                field = value
                notifyListenersAboutChange()
            }
        }

    override var h: Double = h
        set(value) {
            if (field != value) {
                Check.positiveOrZero(value, "h")
                field = value
                notifyListenersAboutChange()
            }
        }

    override var empty: Boolean = empty
        set(value) {
            if (field != value) {
                field = value
                notifyListenersAboutChange()
            }
        }

    override var minX: Double
        get() = x
        set(x) = setEdges(x, minY, maxX, maxY)

    override var maxX: Double
        get() = x + w
        set(x) = setEdges(minX, minY, x, maxY)

    override var minY: Double
        get() = y
        set(y) = setEdges(minX, y, maxX, maxY)

    override var maxY: Double
        get() = y + h
        set(y) = setEdges(minX, minY, minY, y)

    fun setXs(x1: Double, x2: Double) {
        setEdges(x1, minY, x2, maxY)
    }

    fun setYs(y1: Double, y2: Double) {
        setEdges(minX, y1, maxX, y2)
    }


    fun set(rectangle: Rect) {
        set(rectangle.x, rectangle.y, rectangle.w, rectangle.h)
        empty = rectangle.empty
    }

    fun set(pos: Double2, size: Double2) {
        set(pos.x, pos.y, size.x, size.y)
    }

    fun set(x: Double, y: Double, w: Double, h: Double) {
        if (x != this.x || y != this.y || w != this.w || h != this.h) {
            this.x = x
            this.y = y
            this.w = w
            this.h = h
            empty = false
        }
    }

    fun setEdges(pos1: Double2, pos2: Double2) {
        setEdges(pos1.x, pos1.y, pos2.x, pos2.y)
    }

    fun setEdges(x1: Double, y1: Double, x2: Double, y2: Double) {
        val minX = x1 min x2
        val minY = y1 min y2
        val maxX = x1 max x2
        val maxY = y1 max y2
        set(minX, minY, maxX-minX, maxY-minY)
    }

    /**
     * Moves the rectangle by the specified amount along the x and y axis.
     */
    fun move(deltaX: Double, deltaY: Double) {
        x += deltaX
        y += deltaY
    }

    /**
     * Moves the rectangle by the specified amount along the x and y axis.
     */
    fun move(delta: Double2) {
        x += delta.x
        y += delta.y
    }

    /**
     * Resizes the rectangle with the specified amount.
     * @param deltaSize units to add to the width and height
     * @param center if true the center of the rectangle will not move, if false the minX and minY of of the rectangle will not move.
     */
    fun resize(deltaSize: Double2, center: Boolean = false) {
        setSize(w + deltaSize.x, h + deltaSize.y, center)
    }

    /**
     * Resizes the rectangle with the specified amount.
     * @param deltaXSize units to add to the width
     * @param deltaYSize units to add to the height
     * @param center if true the center of the rectangle will not move, if false the minX and minY of of the rectangle will not move.
     */
    fun resize(deltaXSize: Double, deltaYSize: Double, center: Boolean = false) {
        setSize(w + deltaXSize, h + deltaYSize, center)
    }

    /**
     * Scales the rectangle with the specified factor.
     * @param scale units to multiply sizeX and sizeY with
     * @param center if true the center of the rectangle will not move, if false the minX and minY of of the rectangle will not move.
     */
    fun scale(scale: Double2, center: Boolean = false) {
        if (center) {
            scale(scale.x, scale.y, 0.5, 0.5)
        } else {
            scale(scale.x, scale.y, 0.0, 0.0)
        }
    }

    /**
     * Scales the rectangle with the specified factor.
     * @param xScale units to multiply sizeX with
     * @param yScale units to multiply sizeY with
     * @param center if true the center of the rectangle will not move, if false the minX and minY of of the rectangle will not move.
     */
    fun scale(xScale: Double, yScale: Double, center: Boolean = false) {
        if (center) {
            scale(xScale, yScale, 0.5, 0.5)
        } else {
            scale(xScale, yScale, 0.0, 0.0)
        }
    }

    /**
     * Scales the rectangle with the specified factor, using the specified relative position as the unmoving center of the scaling.
     * @param scale units to multiply size with
     * @param relativeCenter relative position within the rectangle of the center of the scaling (0..1, can be outside the rectangle as well).
     */
    fun scale(scale: Double2, relativeCenter: Double2) {
        scale(scale.x, scale.y, relativeCenter.x, relativeCenter.y)
    }

    /**
     * Scales the rectangle with the specified factor, using the specified relative position as the unmoving center of the scaling.
     * @param xScale units to multiply sizeX with
     * @param yScale units to multiply sizeY with
     * @param relativeCenterX relative position within the rectangle of the center of the scaling (0..1, can be outside the rectangle as well).
     * @param relativeCenterY relative position within the rectangle of the center of the scaling (0..1, can be outside the rectangle as well).
     */
    fun scale(xScale: Double, yScale: Double, relativeCenterX: Double, relativeCenterY: Double) {
        setSize(w * xScale, h * yScale, relativeCenterX, relativeCenterY)
    }

    /**
     * Sets the size of the rectangle to the specified size.
     * @param size new width and height of the rectangle
     * @param center if true the center of the rectangle will not move, if false the minX and minY of of the rectangle will not move.
     */
    fun setSize(size: Double2, center: Boolean = false) {
        setSize(size.x, size.y, center)
    }

    /**
     * Sets the size of the rectangle to the specified size.
     * @param width new width of the rectangle
     * @param height new height of the rectangle
     * @param center if true the center of the rectangle will not move, if false the minX and minY of of the rectangle will not move.
     */
    fun setSize(width: Double, height: Double, center: Boolean = false) {
        if (center) {
            setSize(width, height, 0.5, 0.5)
        } else {
            setSize(width, height, 0.0, 0.0)
        }
    }

    /**
     * Sets the size of the rectangle to the specified size.
     * @param width new width of the rectangle
     * @param height new height of the rectangle
     * @param relativeCenterX relative position within the rectangle of the center of the scaling (0..1, can be outside the rectangle as well).
     * @param relativeCenterY relative position within the rectangle of the center of the scaling (0..1, can be outside the rectangle as well).
     */
    fun setSize(width: Double, height: Double, relativeCenterX: Double, relativeCenterY: Double) {
        val cx = getMappedX(relativeCenterX)
        val cy = getMappedY(relativeCenterY)
        setEdges(cx - 0.5 * width, cy - 0.5 * height,
                 cx + 0.5 * width, cy + 0.5 * height)
    }

    /**
     * Sets the position of the rectangle.
     * @param pos world position.
     * @param relativePos relative position (0..1) inside the rectangle that should be at the specified world position.  Defaults to center.
     */
    fun setPosition(pos: Double2, relativePos: Double2 = CENTER) {
        setPosition(pos.x, pos.y, relativePos.x, relativePos.y)
    }

    /**
     * Sets the position of the rectangle.
     * @param x world position.
     * @param y world position.
     * @param relativeX relative position (0..1) inside the rectangle that should be at the specified world position.
     * @param relativeY relative position (0..1) inside the rectangle that should be at the specified world position.
     */
    fun setPosition(x: Double, y: Double, relativeX: Double = 0.5, relativeY: Double = 0.5) {
        val dx = x - getMappedX(relativeX)
        val dy = y - getMappedX(relativeY)
        move(dx, dy)
    }


    override fun <T> addListener(listener: RectListener<T>, listenerData: T) {
        if (listeners == null) listeners = HashMap()
        @Suppress("UNCHECKED_CAST")
        listeners?.put(listener as RectListener<Any?>, listenerData as Any?)
    }

    override fun <T> removeListener(listener: RectListener<T>) {
        @Suppress("UNCHECKED_CAST")
        listeners?.remove(listener as RectListener<Any?>)
    }

    /**
     * Sets area to zero and location to origo.
     */
    fun clear() {
        set(0.0, 0.0, 0.0, 0.0)
        empty = true
    }

    /**
     * Set this Rectangle to the intersection of itself and the other Rectangle.
     * If there was no overlap, clears the Rectangle.
     * @return true if an intersection was found.
     */
    fun setToIntersection(other: Rect): Boolean {
        if (empty) {
            return false
        }
        else if (other.empty) {
            empty = true
            return false
        } else {
            val newMinX = minX max other.minX
            val newMinY = minY max other.minY
            val newMaxX = maxX min other.maxX
            val newMaxY = maxY min other.maxY

            if (newMaxX < newMinX || newMaxY < newMinY) {
                clear()
                return false
            } else {
                setEdges(newMinX, newMinY, newMaxX, newMaxY)
                return true
            }
        }
    }

    /**
     * Set this Rectangle to the union of itself and the other Rectangle.
     */
    fun setToUnion(other: Rect) {
        if (empty) {
            set(other)
        }
        else if (!other.empty) {
            val newMinX = minX min other.minX
            val newMinY = minY min other.minY
            val newMaxX = maxX max other.maxX
            val newMaxY = maxY max other.maxY
            setEdges(newMinX, newMinY, newMaxX, newMaxY)
        }
    }

    /**
     * Modifies this Rectangle to include the specified bounds.
     */
    fun include(other: Rect) = setToUnion(other)

    private fun notifyListenersAboutChange() {
        val ls = listeners
        if (ls != null) {
            for (entry in ls.entries) {
                val listener = entry.key
                listener.onChanged(this, entry.value)
            }
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is MutableRect) return false

        if (other.x.compareTo(x) != 0) return false
        if (other.y.compareTo(y) != 0) return false
        if (other.w.compareTo(w) != 0) return false
        if (other.h.compareTo(h) != 0) return false
        if (other.empty != empty) return false

        return true
    }

    override fun hashCode(): Int {
        var result: Int
        var temp: Long
        temp = if (x != +0.0) java.lang.Double.doubleToLongBits(x) else 0L
        result = (temp xor temp.ushr(32)).toInt()
        temp = if (y != +0.0) java.lang.Double.doubleToLongBits(y) else 0L
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        temp = if (w != +0.0) java.lang.Double.doubleToLongBits(w) else 0L
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        temp = if (h != +0.0) java.lang.Double.doubleToLongBits(h) else 0L
        result = 31 * result + (temp xor temp.ushr(32)).toInt()
        result = result xor empty.hashCode()
        return result
    }


    companion object {
        private var CENTER = ImmutableDouble2(0.5, 0.5)
    }

    override fun toString(): String {
        return "MutableRect(x=$x, y=$y, w=$w, h=$h, empty=$empty)"
    }
}
