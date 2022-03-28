package fractalutils.collections.ringbuffer


/**
 * RingBuffer implementation with ints that uses a backing array.
 * Not thread safe.
 * @param capacity capacity of the ringbuffer.
 */
class IntRingBuffer(capacity: Int) : RingBufferBase<Int>(capacity, 0) {

    private val buffer = IntArray(capacity, {emptyElement})

    override fun getBufferElement(index: Int): Int = buffer[index]
    override fun setBufferElement(index: Int, value: Int) {buffer[index] = value }
}
