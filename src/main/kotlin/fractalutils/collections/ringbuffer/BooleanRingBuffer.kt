package fractalutils.collections.ringbuffer

import java.util.*


/**
 * RingBuffer implementation with booleans that uses a backing BitSet.
 * Not thread safe.
 * @param capacity capacity of the ringbuffer.
 */
class BooleanRingBuffer(capacity: Int) : RingBufferBase<Boolean>(capacity, false) {

    private val buffer = BitSet(capacity)

    override fun getBufferElement(index: Int): Boolean = buffer[index]
    override fun setBufferElement(index: Int, value: Boolean) {buffer[index] = value }
}
