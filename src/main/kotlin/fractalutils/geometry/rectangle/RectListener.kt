package fractalutils.geometry.rectangle

/**
 * Listener that is notified about changes to a Rectangle.
 */
interface RectListener<T> {

    /**
     * Called when the specified Rectangle is changed.
     * Note that this will be called once for every x, y, width or height that changes in the Rect.
     * @param rectangle Rectangle that changed.
     * @param listenerData data object specified when the listener was added to the Rectangle.
     */
    fun onChanged(rectangle: Rect, listenerData: T)

}
