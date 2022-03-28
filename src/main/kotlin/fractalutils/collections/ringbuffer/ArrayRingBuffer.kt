package fractalutils.collections.ringbuffer



/**
 * RingBuffer implementation with object references that uses a backing array.
 * Not thread safe.
 * @param capacity capacity of the ringbuffer.
 * @param emptyElement element to use for buffer positions with no value in the implementation.
 *        Typically null, but if the type T is non-nullable, give some empty value here.
 */
class ArrayRingBuffer<T: Any?>(capacity: Int, @Suppress("UNCHECKED_CAST") emptyElement: T = null as T) : RingBufferBase<T>(capacity, emptyElement) {

    // Array to store values in
    @Suppress("UNCHECKED_CAST")
    private val buffer: Array<T> = Array<Any?>(capacity) {emptyElement} as Array<T>

    override fun getBufferElement(index: Int): T = buffer[index]
    override fun setBufferElement(index: Int, value: T) { buffer[index] = value }
}
