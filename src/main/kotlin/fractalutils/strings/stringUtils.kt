package fractalutils.strings

import fractalutils.checking.Check
import fractalutils.math.max
import fractalutils.math.min
import fractalutils.symbol.Symbol

/**
 * Returns this string, or the specified alternative string if this string is null or empty.
 */
fun String?.orIfEmpty(alternativeString: String): String {
    return if (this == null || this.isEmpty()) alternativeString
    else this
}


/**
 * @param fillCharacter character to use instead of non-allowed identifier characters.  Defaults to underscore.
 * @param strictIdentifier if true, no unicode characters will be allowed, only ascii letters, underscores and (for non-first character) digits.  Defaults to false.
 * @return a valid java/kotlin camelCase identifier, generated from a string with space separated words.
 * If the identifier contains a non valid identifier character, it is replaced with the fillCharacter.
 * The identifier may contain some unicode characters.
 */
fun String.toIdentifier(fillCharacter: Char = '_', strictIdentifier: Boolean = false): String {

    fun isIdentifierStart(c: Char): Boolean = if (strictIdentifier) c.isAsciiLetterOrUnderscore() else c.isJavaIdentifierStart()
    fun isIdentifierPart(c: Char): Boolean = if (strictIdentifier) c.isAsciiLetterOrUnderscoreOrDigit() else c.isJavaIdentifierPart()

    if (!isIdentifierStart(fillCharacter) || !isIdentifierPart(fillCharacter)) throw IllegalArgumentException("The fillCharacter must be a valid identifier start & part, but it was '$fillCharacter'")

    var userReadableName = this

    // Remove extra whitespace
    userReadableName = userReadableName.trim { it <= ' ' }

    val sb = StringBuilder()
    var capitalizeNext = false
    var firstCreated = true
    for (c in userReadableName) {
        var nextChar = c

        // Skip spaces, capitalize character after space
        if (nextChar == ' ')
            capitalizeNext = true
        else {
            // Capitalize if needed
            if (capitalizeNext) {
                nextChar = Character.toUpperCase(nextChar)
                capitalizeNext = false
            }

            if (firstCreated) {
                // Handle first character
                if (isIdentifierStart(nextChar)) {
                    // Valid, just add it
                    sb.append(nextChar)
                } else {
                    // Invalid, add fill char instead
                    sb.append(fillCharacter)

                    // Add the character if it was a valid part but not start (e.g. number)
                    if (isIdentifierPart(nextChar)) sb.append(nextChar)
                }
                firstCreated = false
            } else {
                if (isIdentifierPart(nextChar))
                    sb.append(nextChar)
                else
                    sb.append(fillCharacter)
            }
        }
    }

    // Make sure it is at least one char long
    if (sb.length <= 0) sb.append(fillCharacter)

    return sb.toString()
}

/**
 * Converts a camel case word to a space separated string of words, capitalizing the first one if specified.
 */
fun String.toSpaceSeparated(capitalize: Boolean = true): String {

    var userReadableName = this

    // Remove extra whitespace
    userReadableName = userReadableName.replace('_', ' ').replace("  ", " ").trim { it <= ' ' }

    val sb = StringBuilder()
    var previousWasUpperCase = false
    for ((index, c) in userReadableName.withIndex()) {
        val nextIsUpperCase = index + 1 < userReadableName.length && userReadableName[index+1].isUpperCase()
        val nextIsSpace = index + 1 >= userReadableName.length || userReadableName[index+1] == ' '

        if (index > 0 && !previousWasUpperCase && c.isUpperCase()) {
            // "CamelCase" => "Camel Case"
            sb.append(' ')
        }

        if (c.isUpperCase() && (nextIsUpperCase || index == userReadableName.length-1)) {
            // "BFG" => "BFG"
            sb.append(c)
        }
        else if (index > 0 && !previousWasUpperCase && c.isUpperCase()) {
            // "CamelCase" => "Camel case"
            sb.append(c.toLowerCase())
        }
        else if (previousWasUpperCase && c.isUpperCase() && !nextIsUpperCase && !nextIsSpace ) {
            // "BFGEnabled" => "BFG enabled"
            sb.append(' ')
            sb.append(c.toLowerCase())
        }
        else {
            sb.append(c)
        }

        previousWasUpperCase = c.isUpperCase()
    }

    val result = sb.toString().replace("  ", " ").trim()

    return if (capitalize) result.capitalize() else result
}


/**
 * @return true if this is a valid Java style identifier (starts with unicode letter or underscore or $,
 * * contains unicode letters, underscores, numbers, or $).
 */
fun String.isJavaIdentifier(): Boolean {
    if (this.isEmpty())
        return false
    else {
        if (!this[0].isJavaIdentifierStart()) return false
        for (i in 1..this.length - 1) {
            if (!this[i].isJavaIdentifierPart()) return false
        }
        return true
    }
}

/**
 * @return true if this is a strict identifier (starts with a-z, A-Z, or _, contains a-z, A-Z, _, or 0-9).
 */
fun String.isStrictIdentifier(): Boolean {
    if (this.isEmpty())
        return false
    else {
        if (!this[0].isAsciiLetterOrUnderscore()) return false
        for (i in 1..this.length - 1) {
            if (!this[i].isAsciiLetterOrUnderscoreOrDigit()) return false
        }
        return true
    }
}


/**
 * @return true if the character is an upper or lower case ascii letter.
 */
fun Char.isAsciiLetter(): Boolean {
    return this in 'a'..'z' ||
           this in 'A'..'Z'
}

/**
 * @return true if the character is an ascii digit.
 */
fun Char.isAsciiDigit(): Boolean {
    return this in '1'..'9' || this == '0'
}

/**
 * @return true if the character is an ascii letter or underscore.
 */
fun Char.isAsciiLetterOrUnderscore(): Boolean {
    return (this in 'a'..'z') ||
            (this in 'A'..'Z') ||
            this == '_'
}

/**
 * @return true if the character is an ascii letter, digit, or underscore.
 */
fun Char.isAsciiLetterOrUnderscoreOrDigit(): Boolean {
    return  this in 'a'..'z' ||
            this in 'A'..'Z' ||
            this in '1'..'9' ||
            this == '0' ||
            this == '_'
}

/**
 * @return the symbol for this string.  Will throw an exception if this string is not a strict identifier
 * (starts with ascii letter or underscore, contains only ascii letters, ascii digits, and underscores).
 */
fun String.toSymbol(): Symbol = Symbol[this]

/**
 * @return the text that comes after the last occurrence of the specified character in this string (not including the character),
 * or an empty string if there is no such character, or if the character is last.
 * E.g. "filename.txt.zip".textAfter('.') would return "zip".
 */
fun String.textAfter(c: Char): String {
    val lastIndex = this.lastIndexOf(c)
    return if (lastIndex < 0 || lastIndex >= this.length - 1) ""
           else this.substring(lastIndex + 1)
}

/**
 * @return the text that comes before the first occurrence of the specified character in this string (not including the character),
 * or an empty string if there is no such character, or if the character is first.
 * E.g. "filename.txt.zip".textBefore('.') would return "filename".
 */
fun String.textBefore(c: Char): String {
    val firstIndex = this.indexOf(c)
    return if (firstIndex <= 0) {
        ""
    } else {
        this.substring(0, firstIndex)
    }
}


/**
 * @return a string representation of the number, at least numberOfDigitsToFillTo digits long, filling leading spaces with zeroes as needed.
 * If the number is negative the minus sign is added in front of the zeroes.
 */
fun Int.toStringWithLeadingZeroes(numberOfDigitsToFillTo: Int): String {
    return if (this < 0) {
        "-" + (-this).toStringWithLeadingZeroes(numberOfDigitsToFillTo)
    } else {
        var s = "" + this
        while (s.length < numberOfDigitsToFillTo) {
            s = "0" + s
        }
        s
    }
}


/**
 * Replaces escaped newlines, tabs, carriage returns and quote chars in the string with the actual characters.
 */
fun String.unEscape(escapeChar: Char = '\\',
                    allowNewlineEscape: Boolean = true,
                    allowTabEscape: Boolean = true,
                    allowCarriageReturnEscape: Boolean = true,
                    quoteChar1: Char? = '\"',
                    quoteChar2: Char? = null): String {
    var s = this
    if (allowNewlineEscape) s = s.replace(escapeChar + "n", "\n")
    if (allowTabEscape) s = s.replace(escapeChar + "t", "\t")
    if (allowCarriageReturnEscape) s = s.replace(escapeChar + "r", "\r")
    if (quoteChar1 != null) s = s.replace("" + escapeChar + quoteChar1, "" + quoteChar1)
    if (quoteChar1 != null) s = s.replace("" + escapeChar + quoteChar2, "" + quoteChar2)
    s = s.replace("" + escapeChar + escapeChar, "" + escapeChar)
    return s
}

/**
 * Replaces newlines, tabs, carriage returns and quote chars in the string with the escaped characters.
 */
fun String.escape(escapeChar: Char = '\\',
                  allowNewlineEscape: Boolean = true,
                  allowTabEscape: Boolean = true,
                  allowCarriageReturnEscape: Boolean = true,
                  quoteChar1: Char? = '\"',
                  quoteChar2: Char? = null): String {
    var s = this
    s = s.replace("" + escapeChar, "" + escapeChar + escapeChar)
    if (allowNewlineEscape) s = s.replace("\n", escapeChar + "n")
    if (allowTabEscape) s = s.replace("\t", escapeChar + "t")
    if (allowCarriageReturnEscape) s = s.replace("\r", escapeChar + "r")
    if (quoteChar1 != null) s = s.replace("" + quoteChar1, "" + escapeChar + quoteChar1)
    if (quoteChar1 != null) s = s.replace("" + quoteChar2, "" + escapeChar + quoteChar2)
    return s
}

/**
 * Converts the string to english plural from singular.
 * Does not currently handle all special cases and irregularities correctly.
 * @param count optionally specify the item count, and the correct plural form for it will be used.
 */
fun String.toPlural(count: Int? = null): String {
    return if (this.isBlank() || count == 1) this
    else {
        if (this.endsWith("y")) {
            if (this.length > 2 && this[this.length - 2].isVowel()) {
                // If the y has a vowel before it (i.e. toy -> toys), then just add the s.
                this + "s"
            } else {
                // If this ends in y with a consonant before it (fly), drop the y and add -ies to make it plural.
                this.dropLast(1) + "ies"
            }
        } else if (this.endsWith("us")) {
            // If this ends in us, replace the us with i (octopus -> octopi)
            this.dropLast(2) + "i"
        } else if (this.endsWith("ch") ||
            this.endsWith("sh") ||
            this.endsWith("x") ||
            this.endsWith("s")) {
            // If this ends in ch, sh, x, s, add -es to make it plural.
            this + "es"
        }
        else {
            // Anything else, just add s
            this + "s"
        }
    }
}


/**
 * Returns "a" or "an" depending on what letter this string starts with.
 */
fun String.getAnOrA() = if (this.isNotEmpty() && this.first().isVowel()) "an" else "a"

/**
 * @return true if this char is an english vowel.
 */
fun Char.isVowel(): Boolean = this in ENGLISH_VOWEL

val ENGLISH_VOWEL = listOf(
    'a', 'e', 'i', 'o', 'u', 'y',
    'A', 'E', 'I', 'O', 'U', 'Y')

/**
 * @return the row and column for the specified index in this string.
 * If the index is equal to the length of this string, the row and column at the end of the string will be returned.
 * The row and column is clamped to the start or end of the string, if it would go past it.
 */
fun String.rowAndColumnForIndex(index: Int): RowAndColumn {

    if (index <= 0) return RowAndColumn(0, 0)

    var row = 0
    var column = 0
    for (i in 0 until this.length) {

        // Check if we got to the index
        if (i == index) return RowAndColumn(row, column)

        // Update row and column
        val c = this[i]
        if (c == '\n') {
            row++
            column = 0
        }
        else {
            column++
        }
    }

    // Return last row and column if index past the end of the string.
    return RowAndColumn(row, column)
}

/**
 * @return the index of the specified row and column in this string.
 * If the row and column is larger than the end of this string, the length of the string will be returned.
 */
fun String.indexOfRowAndColumn(rowAndColumn: RowAndColumn): Int {

    Check.positiveOrZero(rowAndColumn.column, "column")

    if (rowAndColumn.row < 0) return 0

    var row = 0
    var column = 0
    for (i in 0 until this.length) {

        // Check if we got to the specified row and column
        if (row == rowAndColumn.row && column == rowAndColumn.column) return i

        // Update row and column
        val c = this[i]
        if (c == '\n') {
            // If the column is past the end of the line, return the position at the end of the line.
            if (row == rowAndColumn.row) return i

            row++
            column = 0
        }
        else {
            column++
        }
    }
    return this.length
}


/**
 * @return the substring between the two specified row and column pairs (inclusive),
 * or an empty string if the end is before the start
 */
fun String.substringByRowAndColumn(startInclusive: RowAndColumn,
                                   endInclusive: RowAndColumn
): String {
    if (endInclusive < startInclusive) return ""

    val startIndex = indexOfRowAndColumn(startInclusive)
    val endIndex = indexOfRowAndColumn(endInclusive)
    return substring(startIndex max 0, (endIndex + 1) min (length))
}

/**
 * Holds a row and a column for a location in some string.
 */
data class RowAndColumn(val row: Int,
                        val column: Int): Comparable<RowAndColumn> {

    override fun compareTo(other: RowAndColumn): Int {
        return if (row == other.row && column == other.column) 0
        else if (row > other.row) 1
        else if (row == other.row && column > other.column) 1
        else -1
    }

    override fun toString(): String {
        // When printing, use rows and columns that start from 1
        return "(${row + 1}:${column + 1})"
    }
}

/**
 * Return this string with the given [prefixToAdd] added to the start.
 * @param skipIfAlreadyPresent if true, will only add the prefix if it isn't already present
 *        (set [ignoreCase] to true if case should be ignored when checking for this).
 */
fun String.prefix(prefixToAdd: String, skipIfAlreadyPresent: Boolean = false, ignoreCase: Boolean = false): String {
    return if (skipIfAlreadyPresent && this.startsWith(prefixToAdd, ignoreCase)) this
           else prefixToAdd + this
}

/**
 * Return this string with the given [suffixToAdd] added to the end.
 * @param skipIfAlreadyPresent if true, will only add the suffix if it isn't already present
 *        (set [ignoreCase] to true if case should be ignored when checking for this).
 */
fun String.suffix(suffixToAdd: String, skipIfAlreadyPresent: Boolean = false, ignoreCase: Boolean = false): String {
    return if (skipIfAlreadyPresent && this.endsWith(suffixToAdd, ignoreCase)) this
           else this + suffixToAdd
}

