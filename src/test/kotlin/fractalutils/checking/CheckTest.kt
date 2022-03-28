package fractalutils.checking

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import fractalutils.checking.Check.contained
import fractalutils.checking.Check.empty
import fractalutils.checking.Check.equal
import fractalutils.checking.Check.greater
import fractalutils.checking.Check.greaterOrEqual
import fractalutils.checking.Check.identifier
import fractalutils.checking.Check.inRange
import fractalutils.checking.Check.inRangeInclusive
import fractalutils.checking.Check.inRangeZeroToOne
import fractalutils.checking.Check.instanceOf
import fractalutils.checking.Check.less
import fractalutils.checking.Check.lessOrEqual
import fractalutils.checking.Check.negative
import fractalutils.checking.Check.negativeOrZero
import fractalutils.checking.Check.nonEmptyString
import fractalutils.checking.Check.normalNumber
import fractalutils.checking.Check.notContained
import fractalutils.checking.Check.notEmpty
import fractalutils.checking.Check.notInstanceOf
import fractalutils.checking.Check.notZero
import fractalutils.checking.Check.positive
import fractalutils.checking.Check.positiveOrZero
import fractalutils.checking.Check.strictIdentifier

import java.util.Arrays
import java.util.HashMap

class CheckTest {

    @Test
    @Throws(Exception::class)
    fun testInvariant() {
        Check.invariant(true, "should work")

        try {
            Check.invariant(false, "should fail")
            Assertions.fail()
        } catch (e: java.lang.IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testEqual() {
        Check.equals("abc", "foo", "abc" + "", "bar")
        Check.equals(2, "foo", 1 + 1, "bar")
        Check.equalRef(2, "foo", 1 + 1, "bar")

        val s = "asdf"
        Check.equalRef(s, "foo", s, "bar")

        try {
            Check.equals("abc", "foo", "abc" + "def", "bar")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Check.equals("abc", "foo", "ABC", "bar")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Check.equalRef("asdf", "foo", "ghjk", "bar")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testStrings() {
        nonEmptyString("1", "foo")
        nonEmptyString("abc", "foo")
        nonEmptyString(" as \n d  ", "foo")

        try {
            nonEmptyString("", "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            nonEmptyString("\n", "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            nonEmptyString(" ", "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            nonEmptyString(" \t \r\n  ", "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testIdentifier() {
        identifier("JavaIdentifier123", "id")
        identifier("x", "id")
        identifier("_3", "id")
        identifier("x_3", "id")
        identifier("\$foo\$bar$$$$", "id")
        identifier("Ångström", "id")

        strictIdentifier("x", "id")
        strictIdentifier("_x", "id")
        strictIdentifier("xxx3x3", "id")
        strictIdentifier("___sdf__", "id")

        try {
            identifier(null, "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier("?sdf", "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier("x^", "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier("Asdf+5", "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }


        try {
            identifier("+-*/", "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier(" ", "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier("", "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier("3x", "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }


        try {
            strictIdentifier("sd\$f", "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            strictIdentifier("Ångström", "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            strictIdentifier("", "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            strictIdentifier(null, "id")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testCollections() {
        val l = Arrays.asList("a", "b", "c", "d")
        val l2 = Arrays.asList(null, "asdf")
        val l3 = Arrays.asList<String>()
        val m = HashMap<String, String>()
        val m2 = HashMap<String?, String>()
        m.put("abc", "123")
        m.put("def", "456")
        m2.put(null, "asdf")

        contained("a", l, "l")
        contained("c", l, "l")
        contained(null, l2, "l2")

        contained("abc", m, "m")
        contained(null, m2, "m2")

        notEmpty(l, "l")
        empty(l3, "l3")

        notContained(null, l, "l")
        notContained("g", l, "l")

        notContained("geh", m, "m")
        notContained(null, m, "m")

        try {
            contained("g", l, "l")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            contained(null, l, "l")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            contained("geh", m, "m")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            contained(null, m, "m")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notContained("a", l, "l")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notContained(null, l2, "l2")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notContained("abc", m, "m")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notContained(null, m2, "m2")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            empty(l, "l")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notEmpty(l3, "l3")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testInstanceOf() {
        instanceOf("abc", "foo", String::class.java)
        instanceOf("abc", "foo", Any::class.java)
        notInstanceOf("abc", "foo", Int::class.java)

        try {
            instanceOf("abc", "foo", Int::class.java)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notInstanceOf("abc", "foo", Any::class.java)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }


        try {
            notInstanceOf("abc", "foo", String::class.java)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testFail() {
        try {
            Check.fail("Some reason to fail")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Check.fail("abc", "foo", "should be imaginary string")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testNormalNumber() {
        normalNumber(0.0, "foo")
        normalNumber(-1.0, "foo")
        normalNumber(1f, "foo")
        normalNumber(java.lang.Double.MIN_NORMAL, "foo")
        normalNumber(-java.lang.Double.MIN_NORMAL, "foo")

        try {
            normalNumber(java.lang.Double.POSITIVE_INFINITY, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            normalNumber(java.lang.Double.NEGATIVE_INFINITY, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            normalNumber(java.lang.Float.NEGATIVE_INFINITY, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            normalNumber(java.lang.Double.NaN, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testSignChecks() {
        positive(1, "foo")
        positive(2309.23, "foo")
        positive(0.00001, "foo")
        positive(Integer.MAX_VALUE, "foo")

        positiveOrZero(0, "foo")
        positiveOrZero(0.0, "foo")
        positiveOrZero(0.00001, "foo")
        positiveOrZero(1, "foo")
        positiveOrZero(4234, "foo")
        positiveOrZero(4234.231f, "foo")

        negative(-0.001f, "foo")
        negative(-123123.123, "foo")
        negative(-1, "foo")
        negative(Integer.MIN_VALUE, "foo")

        negativeOrZero(0, "foo")
        negativeOrZero(0.0, "foo")
        negativeOrZero(-0.00001f, "foo")
        negativeOrZero(-1, "foo")
        negativeOrZero(-1123.123, "foo")
        negativeOrZero(Integer.MIN_VALUE, "foo")

        notZero(1, "foo")
        notZero(-1, "foo")
        notZero(Integer.MIN_VALUE, "foo")
        notZero(Integer.MAX_VALUE, "foo")
        notZero(0.001, "foo", 0.0001)
        notZero(-0.001, "foo", 0.0001)
        notZero(1.0, "foo", 0.0001)
        notZero(-1.0f, "foo", 0.0001f)

        try {
            positive(0, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            positive(-1, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            positive(-0.0001, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            positiveOrZero(-0.0001, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            positiveOrZero(-1, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negative(0, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negative(0f, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negative(0.00001, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negative(1, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negativeOrZero(0.0012, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negativeOrZero(1, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notZero(0, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notZero(0.000f, "foo", 0.00001f)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testComparisons() {

        // Ints
        greater(3, "foo", 0)

        greater(3, "foo", 0, "limit")
        greater(-4, "foo", -5, "limit")

        greaterOrEqual(3, "foo", 0, "limit")
        greaterOrEqual(3, "foo", 3, "limit")
        greaterOrEqual(-4, "foo", -5, "limit")
        greaterOrEqual(-4, "foo", -4, "limit")

        less(-4, "foo", -3, "limit")
        less(1000, "foo", 1001, "limit")

        lessOrEqual(1000, "foo", 1001, "limit")
        lessOrEqual(1000, "foo", 1000, "limit")
        lessOrEqual(-1, "foo", 0, "limit")

        equal(0, "foo", 0, "target")
        equal(-42, "foo", -42, "target")
        equal(Integer.MAX_VALUE, "foo", Integer.MAX_VALUE, "target")

        // Floats
        greater(3f, "foo", 0f, null)
        greater(3.0f, "foo", 2.99f)
        greater(3f, "foo", 2.99f)

        greaterOrEqual(3f, "foo", 2.99f, "limit")
        greaterOrEqual(3f, "foo", 3.0f, "limit")
        greaterOrEqual(3.0f, "foo", 3f, "limit")
        greaterOrEqual(3.01f, "foo", 3f, "limit")

        lessOrEqual(3f, "foo", 3.1f, "limit")
        lessOrEqual(3.0f, "foo", 3f, "limit")

        less(3.0, "foo", 3.000000001, "limit")
        less(-0.1f, "foo", 0f, "limit")

        equal(3.14f, "foo", 3.14f, "limit", 0.00001f)
        equal(3f, "foo", 4f, "limit", 1f)

        // Test failures
        try {
            Check.greaterOrEqual(3.14f, "param", Math.PI.toFloat(), "Pi")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            equal(3.14f, "param", Math.PI.toFloat(), "Pi", 0.0001f)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            less(3, "param", 2, "limit")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            less(3, "param", 3, "limit")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            less(3f, "param", 3.0f, "limit")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            greater(3f, "param", 3.1f, "limit")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            greater(-1, "param", 0, "limit")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            equal(1, "param", 0, "limit")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            equal(0f, "param", 0.000001f, "limit", 0.00000001f)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

    }


    @Test
    @Throws(Exception::class)
    fun testRanges() {
        inRange(0, "foo", -1, 1)
        inRange(0.1, "foo", -1.0, 1.0)
        inRange(0f, "foo", -1.0f, 1f)

        inRange(43.0, "foo", 42.5, 43.01)
        inRange(43, "foo", 42, 44)
        inRange(43, "foo", 43, 44)

        inRangeInclusive(43, "foo", 42, 43)
        inRangeInclusive(43.0, "foo", 42.5, 43.0)

        inRangeZeroToOne(0.0, "foo")
        inRangeZeroToOne(0f, "foo")
        inRangeZeroToOne(0.34, "foo")
        inRangeZeroToOne(1.0, "foo")
        inRangeZeroToOne(1f, "foo")

        try {
            inRange(43.0, "foo", 42.5, 43.0)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRange(43, "foo", 42, 43)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRange(41, "foo", 42, 43)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRangeInclusive(43f, "foo", 41f, 42f)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRangeInclusive(40, "foo", 41, 42)
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRangeZeroToOne(1.001, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRangeZeroToOne(-0.001, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRangeZeroToOne(2f, "foo")
            Assertions.fail()
        } catch (e: IllegalArgumentException) {
        }

    }
}
