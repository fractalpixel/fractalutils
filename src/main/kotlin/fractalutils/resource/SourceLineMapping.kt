package fractalutils.resource

/**
 * Keeps track of ranges in a text that originate from different ranges in other sources.
 * Assumes that lines are unchanged.
 *
 * Text lines are considered to start from 1, and columns from 1.
 *
 * Mappings are appended incrementally using [appendLineMapping].
 */
class SourceLineMapping<T> {

    private val ranges = ArrayList<RangeMapping<T>>()

    /**
     * Adds a mapping from the next unused line number to the specified source and the line number in that source,
     * of the specified number of lines (defaults to 1).  Line counts less than one are ignored.
     */
    fun appendLineMapping(source: T, sourceLine: Int, lineCount: Int = 1) {
        if (lineCount > 0) {
            if (ranges.isEmpty() || ranges.last().source != source || ranges.last().nextSourceLine != sourceLine) {
                // Create a new range
                appendRange(source, lineCount, sourceLine)
            } else {
                // Append to existing range
                ranges.last().increaseLength(lineCount)
            }
        }
    }


    /**
     * Remove all current data.
     */
    fun clear() {
        ranges.clear()
    }

    /**
     * Returns the source and line in the source for the specified line in the target,
     * or null if the line is out of range.
     */
    fun getSourceAndLine(line: Int): Pair<T, Int>? {
        val range = getSourceRange(line)

        return if (range == null) null
        else Pair(range.source, range.mapLine(line))
    }

    /**
     * Returns the source for the specified line in the target,
     * or null if the line is out of range.
     */
    fun getSource(line: Int): T? = getSourceRange(line)?.source

    /**
     * Returns the line in the source for the specified line in the target,
     * or null if the line is out of range.
     */
    fun getSourceLine(line: Int): Int? = getSourceRange(line)?.mapLine(line)


    /**
     * Returns the range at the specified line, or null if out of range.
     */
    private fun getSourceRange(line: Int): RangeMapping<T>? {
        val rangeIndex = ranges.binarySearch { it.compareToLine(line) }
        return if (rangeIndex < 0) null
        else ranges[rangeIndex]
    }

    /**
     * Add a new source range to the end of the current target.
     */
    private fun appendRange(source: T, lineCount: Int, sourceStart: Int = 1) {
        ranges.add(RangeMapping(nextTargetLine, nextTargetLine + lineCount, sourceStart, source))
    }

    /**
     * The next unused target line.
     */
    private val nextTargetLine: Int get() = if (ranges.isEmpty()) 1 else ranges.last().targetEnd


    /**
     * Structure for storing mapping ranges from a target to a source.
     */
    private data class RangeMapping<T>(
        val targetStart: Int, // Inclusive
        var targetEnd: Int, // Exclusive
        val sourceStart: Int, // Inclusive
        val source: T
    ) : Comparable<RangeMapping<T>> {

        override fun compareTo(other: RangeMapping<T>): Int {
            return if (targetStart < other.targetStart) -1
            else if (targetStart > other.targetStart) 1
            else 0
        }

        fun compareToLine(line: Int): Int {
            return if (targetEnd <= line) -1
            else if (targetStart > line) 1
            else 0
        }

        fun mapLine(targetLine: Int): Int {
            return sourceStart + targetLine - targetStart
        }

        val length: Int get() = targetEnd - targetStart

        val nextSourceLine: Int get() = sourceStart + length

        fun increaseLength(lines: Int = 1) {
            targetEnd += lines
        }
    }


}