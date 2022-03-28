package fractalutils.strings

/**
 * Something that can be converted to code.
 */
interface CodeBuilding {

    /**
     * Stores the source code for this object in the specified builder.
     */
    fun buildCode(builder: CodeBuilder)

    /**
     * Converts this object to a source code string using the buildCode method.
     */
    fun toCode(indent: Int = 0,
               indentString: String = "   ",
               newLine: String = "\n"): String {
        val builder = CodeBuilder(indent, indentString = indentString, newLine = newLine)
        buildCode(builder)
        return builder.toString()
    }

}