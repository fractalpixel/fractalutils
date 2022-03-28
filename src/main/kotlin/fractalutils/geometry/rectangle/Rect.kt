package fractalutils.geometry.rectangle

import fractalutils.geometry.double2.Double2
import fractalutils.geometry.double2.MutableDouble2
import fractalutils.interpolation.Easing
import fractalutils.math.distance
import fractalutils.math.distanceSquared
import fractalutils.math.map

/**
 * Rectangular axis aligned bounding area (inclusive).
 * Has a concept for empty rectangles, that do not contain any other Rectangles, and are not contained in any other Rectangles.
 * Has mutable and immutable implementations.  The mutable implementation provides support for listening to changes.
 */
interface Rect {

    val x: Double
    val y: Double
    val w: Double
    val h: Double

    /**
     * @return true if the Rectangle represents no area, that is, it can not collide with anything or contain anything.
     * * Used for uninitialized / cleared mutable Rectangles.
     */
    val empty: Boolean

    val width: Double get() = w
    val height: Double get() = h

    /**
     * Average of current width and height
     */
    val widthHeightAverage: Double get() = (w + h) * 0.5

    val minX: Double get() = x
    val maxX: Double get() = x + w
    val minY: Double get() = y
    val maxY: Double get() = y + h

    val centerX: Double get() = x + w * 0.5
    val centerY: Double get() = y + h * 0.5

    /**
     * @return average of width and height
     */
    val sizeAverage: Double get() = 0.5 * (w + h)

    /**
     * @return area of the rectangle (w * h)
     */
    val area: Double get() = w * h

    /**
     * @return true if the point is within the rectangle.
     */
    operator fun contains(p: Double2): Boolean = contains(p.x, p.y)

    /**
     * @return true if the coordinate is within the rectangle.
     */
    fun contains(x: Double, y: Double): Boolean =
            !empty && (x in minX .. maxX &&
                       y in minY .. maxY)

    /**
     * @return true if the specified rectangle is contained inside this rectangle.
     */
    operator fun contains(rectangle: Rect): Boolean =
            !empty &&
            !rectangle.empty && (
                    rectangle.minX >= minX &&
                    rectangle.maxX <= maxX &&
                    rectangle.minY >= minY &&
                    rectangle.maxY <= maxY)

    /**
     * @return true if the specified rectangle overlaps this rectangle (including just an edge).
     */
    fun intersects(rectangle: Rect): Boolean =
            !empty &&
            !rectangle.empty && (
                rectangle.minX <= maxX &&
                rectangle.maxX >= minX &&
                rectangle.minY <= maxY &&
                rectangle.maxY >= minY)

    /**
     * Returns a new Rect with the intersection of this Rect and the other Rect.
     */
    infix fun intersection(other: Rect): Rect {
        val intersection = MutableRect(this)
        intersection.setToIntersection(other)
        return intersection
    }

    /**
     * @return true if this rectangle overlaps or touches the specified circle.
     */
    fun intersectsCircle(x: Double, y: Double, radius: Double): Boolean {
        if (empty) return false

        // Inside
        if (contains(x, y)) return true
        if (radius <= 0.0) return false

        // Edges
        if (x in minX .. maxX && y in minY - radius .. maxY + radius) return true
        if (x in minX - radius .. maxX + radius && y in minY .. maxY) return true

        // Corners
        val squareRadius = radius * radius
        return distanceSquared(x, y, minX, minY) <= squareRadius ||
                distanceSquared(x, y, maxX, minY) <= squareRadius ||
                distanceSquared(x, y, minX, maxY) <= squareRadius ||
                distanceSquared(x, y, maxX, maxY) <= squareRadius
    }

    /**
     * Returns a new Rect with the union of this Rect and the other Rect.
     */
    infix fun union(other: Rect): Rect {
        val union = MutableRect(this)
        union.setToUnion(other)
        return union
    }

    /**
     * Add a listener that is notified if the Rectangle changes.
     * If the Rectangle implementation is immutable, this can be just a stub that ignores the listener.
     * @param listener a listener that will be called when the Rectangle changes dimensions or location.
     * @param listenerData a data object that should be passed to the listener when called.
     */
    fun <T> addListener(listener: RectListener<T>, listenerData: T)

    /**
     * Remove a listener.
     * If the Rectangle implementation is immutable, this can be just a stub that ignores the listener.
     * @param listener the listener to remove.
     */
    fun <T> removeListener(listener: RectListener<T>)

    /**
     * @return the distance around the Rectangle, when walking along the edges (so width * 2 + height * 2);
     */
    val circumference: Double get() = 2.0 * width + 2.0 * height

    /**
     * @return squared distance from the center of this bound to the specified coordinate.
     */
    fun getSquaredCenterDistanceTo(x: Double, y: Double): Double = distanceSquared(x, y, centerX, centerY)

    /**
     * @return distance from the center of this bound to the specified coordinate.
     */
    fun getCenterDistanceTo(x: Double, y: Double): Double = distance(x, y, centerX, centerY)

    /**
     * @param t a value from 0 to 1, where 0 corresponds to min x and 1 to max x.
     * @param clamp if true, the value will be clamped to minX..maxX.  Defaults to false.
     * @return the parameter mapped to the rectangle, minX when t == 0, and maxX when t == 1.
     *         if t is outside 0..1, the returned value will be outside the rectangle.
     *         Returns zero if the rectangle is empty and has no location.
     */
    fun getMappedX(t: Double, clamp: Boolean = false): Double = if (empty) 0.0 else map(t, 0.0, 1.0, minX, maxX, clamp)

    /**
     * @param t a value from 0 to 1, where 0 corresponds to min y and 1 to max y.
     * @param clamp if true, the value will be clamped to minY..maxY.  Defaults to false.
     * @return the parameter mapped to the rectangle, minY when t == 0, and maxY when t == 1.
     *         if t is outside 0..1, the returned value will be outside the rectangle.
     *         Returns zero if the rectangle is empty and has no location.
     */
    fun getMappedY(t: Double, clamp: Boolean = false): Double = if (empty) 0.0 else map(t, 0.0, 1.0, minY, maxY, clamp)

    /**
     * Maps the specified position, that is in the source range (zero to one by default), to the range of this rectangle.
     * @param sourcePos the position in the source range.
     * @param sourceRange range that the source pos is relative to.  Defaults to 0..1 range for both coordinates.
     * @param outputPos the position to write the result to.  Defaults to sourcePos.
     * @param clamp if true, the value will be clamped to minY..maxY.  Defaults to false.
     * @param easing if provided, apply this easing function to coordinate values after they have been scaled to
     *               the 0..1 range, and before they are scaled to the range of this rectangle.
     * @return outputPos
     */
    fun map(sourcePos: MutableDouble2,
            sourceRange: Rect = ImmutableRect.ZERO_TO_ONE,
            outputPos: MutableDouble2 = sourcePos,
            clamp: Boolean = false,
            easing: Easing? = null): Double2 {
        outputPos.x = map(sourcePos.x, sourceRange.minX, sourceRange.maxX, minX, maxX, clamp, easing)
        outputPos.y = map(sourcePos.y, sourceRange.minY, sourceRange.maxY, minY, maxY, clamp, easing)
        return outputPos
    }

    /**
     * @param x an x coordinate value.
     * @param clamp if true, the value will be clamped to 0..1.  Defaults to false.
     * @return 0 if x equals minX, 1 if x equals maxX, and a linearily interpolated
     *         value in between and beyond.
     *         Returns zero if the rectangle is empty and has no location.
     */
    fun getRelativeX(x: Double, clamp: Boolean = false): Double = if (empty) 0.0 else map(x, minX, maxX, 0.0, 1.0, clamp)

    /**
     * @param y an y coordinate value.
     * @param clamp if true, the value will be clamped to 0..1.  Defaults to false.
     * @return 0 if y equals minY, 1 if y equals maxY, and a linearily interpolated
     *         value in between and beyond.
     *         Returns zero if the rectangle is empty and has no location.
     */
    fun getRelativeY(y: Double, clamp: Boolean = false): Double = if (empty) 0.0 else map(x, minY, maxY, 0.0, 1.0, clamp)

}
