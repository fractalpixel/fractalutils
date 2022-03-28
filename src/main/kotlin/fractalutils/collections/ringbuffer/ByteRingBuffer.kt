package fractalutils.collections.ringbuffer


/**
 * RingBuffer implementation with bytes that uses a backing array.
 * Not thread safe.
 * @param capacity capacity of the ringbuffer.
 */
class ByteRingBuffer(capacity: Int) : RingBufferBase<Byte>(capacity, 0) {

    private val buffer = ByteArray(capacity, {emptyElement})

    override fun getBufferElement(index: Int): Byte = buffer[index]
    override fun setBufferElement(index: Int, value: Byte) {buffer[index] = value }
}
