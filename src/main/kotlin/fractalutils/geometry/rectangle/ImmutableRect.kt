package fractalutils.geometry.rectangle

/**
 * Creates a new immutable, inclusive rectangle.
 * @param x Corner of the rectangle
 * @param y Corner of the rectangle
 * @param w Width of the rectangle
 * @param h Height of the rectangle.
 * @param empty if true, the rectangle will be empty and will not match any point or rectangle on contain checks.  Defaults to false.
 */
data class ImmutableRect(override val x: Double,
                         override val y: Double,
                         override val w: Double,
                         override val h: Double,
                         override val empty: Boolean = false) : Rect {

    constructor(w: Double,
                h: Double,
                empty: Boolean = false) : this(0.0, 0.0, w, h, empty)

    constructor(empty: Boolean = false) : this(
            if (empty) 0.0 else 1.0,
            if (empty) 0.0 else 1.0,
            empty)

    /**
     * Creates a new empty rectangle
     */
    constructor() : this(0.0, 0.0, 0.0, 0.0, true)

    /**
     * Creates a new copy of the specified rectangle
     */
    constructor(rect: Rect) : this(rect.x, rect.y, rect.w, rect.h, rect.empty)

    override fun <T> addListener(listener: RectListener<T>, listenerData: T) {
        // No listeners needed for immutable Rectangles.
    }

    override fun <T> removeListener(listener: RectListener<T>) {
        // No listeners needed for immutable Rectangles.
    }

    override fun toString(): String {
        return "ImmutableRect(x=$x, y=$y, w=$w, h=$h, empty=$empty)"
    }

    companion object {
        val ZERO_TO_ONE = ImmutableRect(0.0, 0.0, 1.0, 1.0)
        val MINUS_ONE_TO_ONE = ImmutableRect(-1.0, -1.0, 2.0, 2.0)
    }

}