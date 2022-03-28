package fractalutils.collections.ringbuffer

import java.util.*

/**
 * Contains common functionality for RingBuffers.
 */
abstract class RingBufferBase<T>(override val capacity: Int, val emptyElement: T) : AbstractList<T>(), RingBuffer<T> {

    // Points to first element
    protected var indexOfFirst = 0

    // Points to one after last element
    protected var indexOfLast = 0

    // Number of elements
    protected var size_ = 0

    /**
     * @return number of elements in the ringbuffer.
     */
    override val size: Int get() = size_

    override val hasElements: Boolean get() = size_ > 0
    override val isFull: Boolean get() = size_ >= capacity

    /**
     * @return true if the ringbuffer is empty.
     */
    override fun isEmpty(): Boolean {
        return size_ <= 0
    }

    /**
     * Empties the buffer, and removes references to all stored elements.
     * The capacity remains unchanged, the new size is zero.
     */
    override fun clear() {
        // Initialize pointers
        indexOfFirst = 0
        indexOfLast = 0

        // Initialize size
        size_ = 0

        clearBufferContents()
    }



    /**
     * @param index index of the element to get, 0 == first element.
     * @return the index:th element from the start of the ringbuffer, 0 = first element.
     * @throws IndexOutOfBoundsException if the index if out of the buffer bounds.
     */
    override fun get(index: Int): T {
        if (index < 0 || index >= size) throw IndexOutOfBoundsException("The position $index is outside the bounds of the ringbuffer (it has size $size)")

        return getBufferElement(wrappedIndex(indexOfFirst + index))
    }

    /**
     * Sets the value of the i:th element from the start of the ringbuffer (0 = first element), discarding the previous value at that location.
     * @throws IndexOutOfBoundsException if i is larger than the current size of the buffer.
     * @return value previously at the location.
     */
    override fun set(index: Int, element: T): T {
        if (index < 0 || index >= size) throw IndexOutOfBoundsException("The position $index is outside the bounds of the ringbuffer (it has size $size)")

        val i = wrappedIndex(indexOfFirst + index)
        val old = getBufferElement(i)
        setBufferElement(i, element)
        return old
    }

    /**
     * @return the i:th element from the end of the ringbuffer, 0 = last element, 1 = next to last element, etc.
     * @throws IndexOutOfBoundsException if i is larger than the current size of the buffer.
     */
    override fun getFromEnd(i: Int): T {
        if (i < 0 || i >= size) throw IndexOutOfBoundsException("The position $i counting from the end is outside the bounds of the ringbuffer (it has size $size)")

        return getBufferElement(wrappedIndex(indexOfLast - 1 - i))
    }

    /**
     * First element of the ringbuffer.
     * @throws IndexOutOfBoundsException if there are no elements in the buffer.
     */
    override var first: T
        get() = get(0)
        set(value: T) { set(0, value) }

    /**
     * Last element of the ringbuffer.
     * @throws IndexOutOfBoundsException if there are no elements in the buffer.
     */
    override var last: T
        get() = getFromEnd(0)
        set(value: T) { set(size - 1, value) }

    /**
     * Adds a new element to the start of the ringbuffer.
     * If the buffer is at full capacity, the last element will be overwritten.
     */
    override fun addFirst(element: T) {
        // Move first to point to one before the first
        indexOfFirst = prevIndex(indexOfFirst)

        // Write new first element
        setBufferElement(wrappedIndex(indexOfFirst), element)

        // Bump last back if the buffer is full
        if (size >= capacity)
            indexOfLast = prevIndex(indexOfLast)
        else
            size_++
    }

    /**
     * Adds a new element to the end of the ringbuffer.
     * If the buffer is at full capacity, the first element will be overwritten.
     */
    override fun addLast(element: T) {
        // Write to one past the last
        setBufferElement(wrappedIndex(indexOfLast), element)

        // Move last to point to one past the last
        indexOfLast = nextIndex(indexOfLast)

        // Bump first forward if the buffer is full
        if (size >= capacity)
            indexOfFirst = nextIndex(indexOfFirst)
        else
            size_++
    }

    override fun add(element: T): Boolean {
        addLast(element)
        return true
    }

    /**
     * Removes the first element from the buffer, and returns it.
     * @throws IndexOutOfBoundsException if there are no elements in the buffer.
     */
    override fun removeFirst(): T {
        if (size <= 0) throw IndexOutOfBoundsException("The ringbuffer is empty, can not remove first element.")

        // Get first element
        val firstElement = getBufferElement(indexOfFirst)

        // Clear removed element reference from the buffer
        setBufferElement(indexOfFirst, emptyElement)

        // Move first to point to the next element
        indexOfFirst = nextIndex(indexOfFirst)

        // Decrease size
        size_--

        return firstElement
    }

    /**
     * Removes the last element from the buffer, and returns it.
     * @throws IndexOutOfBoundsException if there are no elements in the buffer.
     */
    override fun removeLast(): T {
        if (size <= 0) throw IndexOutOfBoundsException("The ringbuffer is empty, can not remove last element.")

        // Get last element
        val lastIndex = wrappedIndex(indexOfLast - 1)
        val lastElement = getBufferElement(lastIndex)

        // Clear removed element reference from the buffer
        setBufferElement(lastIndex, emptyElement)

        // Move last to point to the previous element
        indexOfLast = prevIndex(indexOfLast)

        // Decrease size
        size_--

        return lastElement
    }

    /**
     * Fill the buffer with empty elements.
     */
    protected open fun clearBufferContents() {
        for (i in 0..capacity-1) {
            setBufferElement(i, emptyElement)
        }
    }

    protected fun nextIndex(i: Int): Int {
        var index = (i + 1) % capacity
        if (index < 0) index += capacity
        return index
    }

    protected fun prevIndex(i: Int): Int {
        var index = (i - 1) % capacity
        if (index < 0) index += capacity
        return index
    }

    protected fun wrappedIndex(i: Int): Int {
        var index = i % capacity
        if (index < 0) index += capacity
        return index
    }





    protected abstract fun getBufferElement(index: Int): T
    protected abstract fun setBufferElement(index: Int, value: T)
}
