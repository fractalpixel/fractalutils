package fractalutils.collections.ringbuffer

/**
 * Fixed size buffer that can be added to at one end, and discards values at the other.
 * Can be accessed at any point.
 * Note that the add(index, element) and remove(index) methods of the list are not supported at the moment.
 * Not thread safe.
 */
interface RingBuffer<T> : List<T> {

    /**
     * the maximum capacity of the ringbuffer.
     */
    val capacity: Int

    /**
     * true if the ringbuffer has reached capacity, and any new elements added will drop out an element at the other end.
     */
    val isFull: Boolean

    /**
     * true if the ringbuffer is not empty.
     */
    val hasElements: Boolean

    /**
     * @return the i:th element from the end of the ringbuffer, 0 = last element, 1 = next to last element, etc.
     * @throws IndexOutOfBoundsException if i is larger than the current size of the buffer.
     */
    fun getFromEnd(i: Int): T

    /**
     * First element of the ringbuffer.
     * @throws IndexOutOfBoundsException if there are no elements in the buffer.
     */
    var first: T

    /**
     * Last element of the ringbuffer.
     * @throws IndexOutOfBoundsException if there are no elements in the buffer.
     */
    var last: T

    /**
     * Adds a new element to the start of the ringbuffer.
     * If the buffer is at full capacity, the last element will be overwritten.
     */
    fun addFirst(element: T)

    /**
     * Adds a new element to the end of the ringbuffer.
     * If the buffer is at full capacity, the first element will be overwritten.
     */
    fun addLast(element: T)

    /**
     * Removes the first element from the buffer, and returns it.
     * @throws IndexOutOfBoundsException if there are no elements in the buffer.
     */
    fun removeFirst(): T

    /**
     * Removes the last element from the buffer, and returns it.
     * @throws IndexOutOfBoundsException if there are no elements in the buffer.
     */
    fun removeLast(): T
}
