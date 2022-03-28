package fractalutils.collections.ringbuffer


/**
 * RingBuffer implementation with doubles that uses a backing array.
 * Not thread safe.
 * @param capacity capacity of the ringbuffer.
 */
class DoubleRingBuffer(capacity: Int) : RingBufferBase<Double>(capacity, Double.NaN) {

    private val buffer = DoubleArray(capacity, {emptyElement})

    override fun getBufferElement(index: Int): Double = buffer[index]
    override fun setBufferElement(index: Int, value: Double) {buffer[index] = value }
}
