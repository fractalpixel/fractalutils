package fractalutils.text

/**
 * A mapping from a position in a text to some relevant object for the position
 *
 * Useful e.g. when includes are used to compose a source file, but an error in the resulting file should
 * be traced to the original source files.
 */
interface TextMapping<T> {

    /**
     * Returns the object at the specified position,
     * or null if the specified position is out of range.
     * @param line line in the text to look up
     * @param column column in the text to look up (defaults to 1)
     */
    operator fun get(line: Int, column: Int = 1): T?

    /**
     * Returns the object at the specified position,
     * or null if the specified position is out of range.
     * @param pos position in the text to look up
     */
    operator fun get(pos: TextPos): T? = get(pos.line, pos.column)

}