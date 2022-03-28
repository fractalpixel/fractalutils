package fractalutils.collections

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import fractalutils.collections.ringbuffer.ArrayRingBuffer

/**
 * Unit tests for RingBuffer
 */
class RingBufferTest {
    @Test
    @Throws(Exception::class)
    fun testModulus() {
        assertEquals(-3, (-13 % 10).toLong())

    }

    @Test
    @Throws(Exception::class)
    fun testBuffer() {
        val ringBuffer = ArrayRingBuffer<String>(4)
        assertGetThrowsException(ringBuffer, 0)
        assertGetThrowsException(ringBuffer, 1)
        assertGetThrowsException(ringBuffer, -1)
        assertGetFromEndThrowsException(ringBuffer, 0)
        assertGetFromEndThrowsException(ringBuffer, 1)
        assertGetFromEndThrowsException(ringBuffer, -1)
        assertGetFirstThrowsException(ringBuffer)
        assertGetLastThrowsException(ringBuffer)
        assertEquals(0, ringBuffer.size)
        assertEquals(4, ringBuffer.capacity)
        assertEquals(true, ringBuffer.isEmpty())
        assertEquals(false, ringBuffer.isFull)

        ringBuffer.addLast("1")

        assertEquals("1", ringBuffer.get(0))
        assertGetThrowsException(ringBuffer, 1)
        assertGetThrowsException(ringBuffer, 4)
        assertGetFromEndThrowsException(ringBuffer, 1)
        assertEquals("1", ringBuffer.getFromEnd(0))
        assertEquals("1", ringBuffer.first)
        assertEquals("1", ringBuffer.last)
        assertEquals(1, ringBuffer.size)
        assertEquals(4, ringBuffer.capacity)
        assertEquals(false, ringBuffer.isEmpty())
        assertEquals(false, ringBuffer.isFull)

        ringBuffer.addLast("2")

        assertEquals("1", ringBuffer.get(0))
        assertEquals("2", ringBuffer.get(1))
        assertEquals("1", ringBuffer.getFromEnd(1))
        assertEquals("2", ringBuffer.getFromEnd(0))
        assertEquals("1", ringBuffer.first)
        assertEquals("2", ringBuffer.last)
        assertEquals(2, ringBuffer.size)
        assertEquals(4, ringBuffer.capacity)

        ringBuffer.addFirst("0")

        assertEquals("0", ringBuffer.get(0))
        assertEquals("1", ringBuffer.get(1))
        assertEquals("2", ringBuffer.get(2))
        assertEquals("2", ringBuffer.getFromEnd(0))
        assertEquals("1", ringBuffer.getFromEnd(1))
        assertEquals("0", ringBuffer.getFromEnd(2))
        assertEquals("0", ringBuffer.first)
        assertEquals("2", ringBuffer.last)
        assertEquals(3, ringBuffer.size)
        assertEquals(4, ringBuffer.capacity)

        ringBuffer.addFirst("-1")

        assertEquals("-1", ringBuffer.get(0))
        assertEquals("0", ringBuffer.get(1))
        assertEquals("1", ringBuffer.get(2))
        assertEquals("2", ringBuffer.get(3))
        assertEquals("2", ringBuffer.getFromEnd(0))
        assertEquals("1", ringBuffer.getFromEnd(1))
        assertEquals("0", ringBuffer.getFromEnd(2))
        assertEquals("-1", ringBuffer.getFromEnd(3))
        assertEquals("-1", ringBuffer.first)
        assertEquals("2", ringBuffer.last)
        assertEquals(4, ringBuffer.size)
        assertEquals(4, ringBuffer.capacity)

        ringBuffer.addLast("3")

        assertEquals("0", ringBuffer.get(0))
        assertEquals("1", ringBuffer.get(1))
        assertEquals("2", ringBuffer.get(2))
        assertEquals("3", ringBuffer.get(3))
        assertEquals("3", ringBuffer.getFromEnd(0))
        assertEquals("2", ringBuffer.getFromEnd(1))
        assertEquals("1", ringBuffer.getFromEnd(2))
        assertEquals("0", ringBuffer.getFromEnd(3))
        assertEquals("0", ringBuffer.first)
        assertEquals("3", ringBuffer.last)
        assertEquals(4, ringBuffer.size)
        assertEquals(4, ringBuffer.capacity)

        ringBuffer.addFirst("-2")

        assertEquals("-2", ringBuffer.get(0))
        assertEquals("0", ringBuffer.get(1))
        assertEquals("1", ringBuffer.get(2))
        assertEquals("2", ringBuffer.get(3))
        assertEquals("2", ringBuffer.getFromEnd(0))
        assertEquals("1", ringBuffer.getFromEnd(1))
        assertEquals("0", ringBuffer.getFromEnd(2))
        assertEquals("-2", ringBuffer.getFromEnd(3))
        assertEquals("-2", ringBuffer.first)
        assertEquals("2", ringBuffer.last)
        assertEquals(4, ringBuffer.size)
        assertEquals(4, ringBuffer.capacity)

        for (i in 11 .. 100) {
            ringBuffer.addLast("#" + i)
        }

        assertGetThrowsException(ringBuffer, -1)
        assertGetThrowsException(ringBuffer, 4)
        assertGetFromEndThrowsException(ringBuffer, -1)
        assertGetFromEndThrowsException(ringBuffer, -4)
        assertGetFromEndThrowsException(ringBuffer, -3)
        assertGetFromEndThrowsException(ringBuffer, 4)
        assertEquals("#97", ringBuffer.get(0))
        assertEquals("#98", ringBuffer.get(1))
        assertEquals("#99", ringBuffer.get(2))
        assertEquals("#100", ringBuffer.get(3))
        assertEquals("#100", ringBuffer.getFromEnd(0))
        assertEquals("#99", ringBuffer.getFromEnd(1))
        assertEquals("#98", ringBuffer.getFromEnd(2))
        assertEquals("#97", ringBuffer.getFromEnd(3))
        assertEquals("#97", ringBuffer.first)
        assertEquals("#100", ringBuffer.last)
        assertEquals(4, ringBuffer.size)
        assertEquals(4, ringBuffer.capacity)
        assertEquals(false, ringBuffer.isEmpty())
        assertEquals(true, ringBuffer.isFull)

        for (i in 13 .. 100) {
            ringBuffer.addFirst("$" + i)
        }

        assertEquals("$100", ringBuffer.get(0))
        assertEquals("$99", ringBuffer.get(1))
        assertEquals("$98", ringBuffer.get(2))
        assertEquals("$97", ringBuffer.get(3))
        assertEquals("$97", ringBuffer.getFromEnd(0))
        assertEquals("$98", ringBuffer.getFromEnd(1))
        assertEquals("$99", ringBuffer.getFromEnd(2))
        assertEquals("$100", ringBuffer.getFromEnd(3))
        assertEquals("$100", ringBuffer.first)
        assertEquals("$97", ringBuffer.last)
        assertEquals(4, ringBuffer.size)
        assertEquals(4, ringBuffer.capacity)

        // Test setters

        ringBuffer.set(2, "EditedValue")

        assertEquals("$100", ringBuffer.get(0))
        assertEquals("$99", ringBuffer.get(1))
        assertEquals("EditedValue", ringBuffer.get(2))
        assertEquals("$97", ringBuffer.get(3))

        ringBuffer.first = "EditedValue2"

        assertEquals("EditedValue2", ringBuffer.get(0))
        assertEquals("$99", ringBuffer.get(1))
        assertEquals("EditedValue", ringBuffer.get(2))
        assertEquals("$97", ringBuffer.get(3))

        ringBuffer.last = "EditedValue3"

        assertEquals("EditedValue2", ringBuffer.get(0))
        assertEquals("$99", ringBuffer.get(1))
        assertEquals("EditedValue", ringBuffer.get(2))
        assertEquals("EditedValue3", ringBuffer.get(3))

        // Test clear

        ringBuffer.clear()

        assertGetThrowsException(ringBuffer, 0)
        assertGetThrowsException(ringBuffer, 1)
        assertGetThrowsException(ringBuffer, -1)
        assertGetFromEndThrowsException(ringBuffer, 0)
        assertGetFromEndThrowsException(ringBuffer, 1)
        assertGetFromEndThrowsException(ringBuffer, -1)
        assertGetFirstThrowsException(ringBuffer)
        assertGetLastThrowsException(ringBuffer)
        assertEquals(0, ringBuffer.size)
        assertEquals(4, ringBuffer.capacity)
        assertEquals(true, ringBuffer.isEmpty())
        assertEquals(false, ringBuffer.isFull)
    }

    @Test
    @Throws(Exception::class)
    fun testRemove() {
        val ringBuffer = ArrayRingBuffer<String>(5)
        assertRemoveFirstThrowsException(ringBuffer)
        assertRemoveLastThrowsException(ringBuffer)

        ringBuffer.addLast("1")
        ringBuffer.addLast("2")
        ringBuffer.addLast("3")
        ringBuffer.addLast("4")
        assertEquals("1", ringBuffer.first)
        assertEquals("2", ringBuffer.get(1))
        assertEquals("4", ringBuffer.last)
        assertEquals(4, ringBuffer.size)
        assertEquals(5, ringBuffer.capacity)

        assertEquals("1", ringBuffer.removeFirst())
        assertEquals("2", ringBuffer.first)
        assertEquals("3", ringBuffer.get(1))
        assertEquals("4", ringBuffer.last)
        assertEquals(3, ringBuffer.size)
        assertEquals(5, ringBuffer.capacity)

        assertEquals("4", ringBuffer.removeLast())
        assertEquals("2", ringBuffer.first)
        assertEquals("3", ringBuffer.get(1))
        assertEquals("3", ringBuffer.last)
        assertEquals(2, ringBuffer.size)
        assertEquals(5, ringBuffer.capacity)

        ringBuffer.addLast("5")
        ringBuffer.addLast("6")
        ringBuffer.addLast("7")
        ringBuffer.addLast("8")
        assertEquals("3", ringBuffer.first)
        assertEquals("5", ringBuffer.get(1))
        assertEquals("8", ringBuffer.last)
        assertEquals(5, ringBuffer.size)
        assertEquals(5, ringBuffer.capacity)

        assertEquals("8", ringBuffer.removeLast())
        assertEquals("3", ringBuffer.first)
        assertEquals("5", ringBuffer.get(1))
        assertEquals("7", ringBuffer.last)
        assertEquals(4, ringBuffer.size)
        assertEquals(5, ringBuffer.capacity)

        assertEquals("3", ringBuffer.removeFirst())
        assertEquals("5", ringBuffer.removeFirst())
        assertEquals("6", ringBuffer.removeFirst())

        assertEquals("7", ringBuffer.first)
        assertEquals("7", ringBuffer.last)
        assertEquals(1, ringBuffer.size)
        assertEquals(5, ringBuffer.capacity)

        assertEquals("7", ringBuffer.removeLast())
        assertEquals(0, ringBuffer.size)
        assertEquals(5, ringBuffer.capacity)
        assertRemoveFirstThrowsException(ringBuffer)
        assertRemoveLastThrowsException(ringBuffer)
    }

    @Test
    @Throws(Exception::class)
    fun testIterator() {
        val ringBuffer = ArrayRingBuffer<String>(5)
        ringBuffer.addLast("foo")
        ringBuffer.addLast("bar")
        ringBuffer.addLast("zap")

        var sum = ""
        for (s in ringBuffer) {
            sum += s
        }
        assertEquals("foobarzap", sum)
    }

    private fun assertGetThrowsException(ringBuffer: ArrayRingBuffer<String>, index: Int) {
        try {
            ringBuffer.get(index)

            fail("Should have thrown an IndexOutOfBoundsException")
        } catch (e: IndexOutOfBoundsException) {
            // Expected
        }

    }

    private fun assertGetFromEndThrowsException(ringBuffer: ArrayRingBuffer<String>, index: Int) {
        try {
            ringBuffer.getFromEnd(index)

            fail("Should have thrown an IndexOutOfBoundsException")
        } catch (e: IndexOutOfBoundsException) {
            // Expected
        }

    }

    private fun assertGetFirstThrowsException(ringBuffer: ArrayRingBuffer<String>) {
        try {
            ringBuffer.first

            fail("Should have thrown an IndexOutOfBoundsException")
        } catch (e: IndexOutOfBoundsException) {
            // Expected
        }

    }

    private fun assertGetLastThrowsException(ringBuffer: ArrayRingBuffer<String>) {
        try {
            ringBuffer.last

            fail("Should have thrown an IndexOutOfBoundsException")
        } catch (e: IndexOutOfBoundsException) {
            // Expected
        }

    }

    private fun assertRemoveFirstThrowsException(ringBuffer: ArrayRingBuffer<String>) {
        try {
            ringBuffer.removeFirst()

            fail("Should have thrown an IndexOutOfBoundsException")
        } catch (e: IndexOutOfBoundsException) {
            // Expected
        }

    }

    private fun assertRemoveLastThrowsException(ringBuffer: ArrayRingBuffer<String>) {
        try {
            ringBuffer.removeLast()

            fail("Should have thrown an IndexOutOfBoundsException")
        } catch (e: IndexOutOfBoundsException) {
            // Expected
        }

    }
}
