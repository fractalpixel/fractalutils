package fractalutils.collections.ringbuffer



/**
 * RingBuffer implementation with shorts that uses a backing array.
 * Not thread safe.
 * @param capacity capacity of the ringbuffer.
 */
class ShortRingBuffer(capacity: Int) : RingBufferBase<Short>(capacity, 0) {

    private val buffer = ShortArray(capacity, {emptyElement})

    override fun getBufferElement(index: Int): Short = buffer[index]
    override fun setBufferElement(index: Int, value: Short) {buffer[index] = value }
}
