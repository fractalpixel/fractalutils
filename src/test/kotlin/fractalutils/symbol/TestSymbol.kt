package fractalutils.symbol

import fractalutils.symbol.Symbol
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TestSymbol {
    @Test
    @Throws(Exception::class)
    fun testSymbol() {
        val foo1 = Symbol["foo"]
        val foo2 = Symbol[String(charArrayOf('f', 'o', 'o'))]
        val bar = Symbol["bar"]

        assertEquals(foo1, foo2)
        assertNotEquals(foo1, bar)
        assertTrue(foo1 == foo2)
        assertTrue(foo1 === foo2)
        assertTrue(foo1 != bar)
        assertTrue(foo1 !== bar)


        assertEquals("foo", foo1.string)
        assertEquals("foo", foo2.string)


        fun assertLegalSymbol(s: String) {
            Symbol[s]
        }

        fun assertIllegalSymbol(s: String) {
            try {
                Symbol[s]
                fail()
            } catch (_: IllegalArgumentException) {
            }
        }

        assertLegalSymbol("_ahab")
        assertLegalSymbol("abc123")
        assertLegalSymbol("abc__123")
        assertLegalSymbol("x")
        assertLegalSymbol("X1")
        assertLegalSymbol("_")

        assertIllegalSymbol("")
        assertIllegalSymbol(" ")
        assertIllegalSymbol("\t")
        assertIllegalSymbol("\r")
        assertIllegalSymbol("\n")
        assertIllegalSymbol("  \n ")
        assertIllegalSymbol(" asdf")
        assertIllegalSymbol("a sdf")
        assertIllegalSymbol("asdf ")
        assertIllegalSymbol("1asd")
        assertIllegalSymbol("Ångström")
        assertIllegalSymbol("Engström")
        assertIllegalSymbol("asd\$asd")
        assertIllegalSymbol("-+asd23")

    }
}
