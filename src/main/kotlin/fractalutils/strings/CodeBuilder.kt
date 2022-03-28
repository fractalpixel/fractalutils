package fractalutils.strings

import java.lang.StringBuilder
import kotlin.math.max

/**
 * Thin wrapper around StringBuilder that adds the concept of indent.
 */
class CodeBuilder(var indent: Int = 0,
                  val stringBuilder: StringBuilder = StringBuilder(),
                  val indentString: String = "    ",
                  val newLine: String = "\n") {

    private var isAtNewLine = true

    /**
     * Appends the specified content directly.
     * If the given parameter implements CodeBuilding, then the buildCode function of it will be called with this CodeBuilder as parameter.
     */
    fun append(s: Any): CodeBuilder {
        if (s is CodeBuilding) s.buildCode(this)
        else stringBuilder.append(s)

        isAtNewLine = false
        return this
    }

    /**
     * Append the specified code producing object at a higher indent level by default.
     */
    fun appendCode(c: CodeBuilding, additionalIndent: Int = 1): CodeBuilder {
        c.buildCode(this.indented(additionalIndent))
        return this
    }

    /**
     * Appends the specified content on its own line.
     */
    fun appendLine(s: Any): CodeBuilder {
        if (!isAtNewLine) lineBreak()
        append(s)
        lineBreak()
        return this
    }

    /**
     * Adds a new line and indent.
     */
    fun lineBreak(): CodeBuilder {
        stringBuilder.append(newLine)
        indent(indent)
        isAtNewLine = true
        return this
    }

    /**
     * Appends the specified content directly.
     */
    operator fun plusAssign(s: Any) {
        append(s)
    }

    /**
     * Return a new CodeBuilder with the indent level increased by the specified amount (by default 1).
     * Keeps appending to the same shared StringBuilder though.
     */
    fun indented(amount: Int = 1): CodeBuilder {
        if (isAtNewLine) indent(amount)
        return CodeBuilder(indent + amount, stringBuilder, indentString, newLine)
    }

    override fun toString(): String {
        return stringBuilder.toString()
    }

    private fun indent(amount: Int) {
        stringBuilder.append(indentString.repeat(max(0, amount)))
    }

}
