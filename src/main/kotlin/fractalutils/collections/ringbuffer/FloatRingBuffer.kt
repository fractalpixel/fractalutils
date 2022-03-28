package fractalutils.collections.ringbuffer


/**
 * RingBuffer implementation with floats that uses a backing array.
 * Not thread safe.
 * @param capacity capacity of the ringbuffer.
 */
class FloatRingBuffer(capacity: Int) : RingBufferBase<Float>(capacity, Float.NaN) {

    private val buffer = FloatArray(capacity, {emptyElement})

    override fun getBufferElement(index: Int): Float = buffer[index]
    override fun setBufferElement(index: Int, value: Float) {buffer[index] = value }
}
