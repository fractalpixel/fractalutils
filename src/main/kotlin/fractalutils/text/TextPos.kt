package fractalutils.text

/**
 * Represents a position in a text, described by the line and column.
 *
 * @param line line in the text, 1 is the first line.
 * @param column column in the text, 1 is the first character on a line.
 */
data class TextPos(var line: Int, var column: Int): Comparable<TextPos> {

    override fun toString(): String {
        return "line: $line, column: $column"
    }

    /**
     * Returns a compact textual representation of this position, in the form "line:column".
     */
    fun toCompactString(): String {
        return "$line:$column"
    }

    override fun compareTo(other: TextPos): Int {
        return if (line == other.line && column == other.column) 0
        else if (line < other.line || (line == other.line && column < other.column)) -1
        else 1
    }
}