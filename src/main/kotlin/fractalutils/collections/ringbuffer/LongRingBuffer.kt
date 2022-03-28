package fractalutils.collections.ringbuffer


/**
 * RingBuffer implementation with longs that uses a backing array.
 * Not thread safe.
 * @param capacity capacity of the ringbuffer.
 */
class LongRingBuffer(capacity: Int) : RingBufferBase<Long>(capacity, 0) {

    private val buffer = LongArray(capacity, {emptyElement})

    override fun getBufferElement(index: Int): Long = buffer[index]
    override fun setBufferElement(index: Int, value: Long) {buffer[index] = value }
}
