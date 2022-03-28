package fractalutils.math

import fractalutils.math.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail

/**
 * Unit test for MathUtils.
 */
class MathUtilsTest {


    @Test
    @Throws(Exception::class)
    fun testWrap0to1() {
        shouldEqual(0.0, 0.0.wrap0To1())
        shouldEqual(0.0, 1.0.wrap0To1())
        shouldEqual(0.0, 2.0.wrap0To1())
        shouldEqual(0.0, (-1.0).wrap0To1())
        shouldEqual(0.1, 0.1.wrap0To1())
        shouldEqual(0.9, (-0.1).wrap0To1())
        shouldEqual(0.1, 1.1.wrap0To1())
        shouldEqual(0.1, (-123.9).wrap0To1())
    }

    @Test
    @Throws(Exception::class)
    fun testWrap() {
        shouldEqual(0.0, 0.0.wrapTo(0.0, 1.0))
        shouldEqual(0.3, 0.3.wrapTo(0.0, 1.0))
        shouldEqual(0.0, 1.0.wrapTo(0.0, 1.0))

        shouldEqual(35.0, wrap(35.0, 20.0, 100.0))
        shouldEqual((100 - 5).toDouble(), 15.0.wrapTo(20.0, 100.0))
        shouldEqual(100 - 20 - 4.toDouble(), (-4.0).wrapTo(20.0, 100.0))
        shouldEqual((20 + 15).toDouble(), 115.0.wrapTo(20.0, 100.0))

        shouldEqual(0.1, 0.1.wrapTo(0.0, 1.0))
        shouldEqual(0.1, 1.1.wrapTo(0.0, 1.0))
        shouldEqual(0.1, 1242.1.wrapTo(0.0, 1.0))

        shouldEqual(0.4, wrap(-0.6, 0.0, 1.0))
        shouldEqual(0.4, (-1.6).wrapTo(0.0, 1.0))
        shouldEqual(0.4, (-213.6).wrapTo(0.0, 1.0))

        shouldEqual(-1.5 + 0.3, (-1.5 + 0.3).wrapTo(-1.5, 1.5))
        shouldEqual(-1.5 + 0.3, (-1.5 - 3 * 2 + 0.3).wrapTo(-1.5, 1.5))
        shouldEqual(-1.5 + 0.3, (-1.5 + (3 * 3).toDouble() + 0.3).wrapTo(-1.5, 1.5))

    }

    @Test
    @Throws(Exception::class)
    fun testRound() {
        assertEquals(5, 5.round(1))
        assertEquals(10, 11.round(1))
        assertEquals(20, 15.round(1))
        assertEquals(20, 19.round(1))
        assertEquals(20, 21.round(1))
        assertEquals(15, 15.round(2))
        assertEquals(80, 78.round(1))
        assertEquals(123000, 123456.round(3))
        assertEquals(-123000, (-123456).round(3))
        assertEquals(988000, 987654.round(3))
        assertEquals(-988000, (-987654).round(3))
        assertEquals(1000000, 999999.round(3))
        assertEquals(-1000000, (-999999).round(3))
        assertEquals(-20, (-15).round(1))
        assertEquals(-20, (-19).round(1))
        assertEquals(-10, (-11).round(1))
        val epsilon = 0.000001
        assertEquals(120.0, (123.0).roundToNDigits(2), epsilon)
        assertEquals(130.0, (126.0).roundToNDigits(2), epsilon)
        assertEquals(-120.0, (-123.0).roundToNDigits(2), epsilon)
        assertEquals(-100.0, (-123.0).roundToNDigits(1), epsilon)
        assertEquals(-123.0, (-123.0).roundToNDigits(3), epsilon)
        assertEquals(0.012, (0.0123).roundToNDigits(2), epsilon)
        assertEquals(0.02, (0.0156).roundToNDigits(1), epsilon)
        assertEquals(-0.012, (-0.0123).roundToNDigits(2), epsilon)
        assertEquals(-0.013, (-0.01251).roundToNDigits(2), epsilon)
        assertEquals(0.012f, (0.0123f).roundToNDigits(2), epsilon.toFloat())
        assertEquals(0.0001, (0.000123).roundToNDigits(1), epsilon)
    }

    private fun shouldEqual(result: Double, actual: Double) {
        assertEquals(result, actual, 0.0001)
    }
}

