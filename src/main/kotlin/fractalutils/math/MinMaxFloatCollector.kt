package fractalutils.math

/**
 * Simple helper class used for collecting the min and max value for some elements.
 * @param defaultMin min value used when no values have been recorded.
 * @param defaultMax max value used when no values have been recorded.
 * @param ignoreInfinites do not record any infinite values.
 */
class MinMaxFloatCollector(val defaultMin: Float = 0f,
                           val defaultMax: Float = 1f,
                           val ignoreInfinites: Boolean = true) {

    var min: Float = defaultMin
    var max: Float = defaultMax

    private var reset = true

    /**
     * Resets the collector, removing current min and max values
     */
    fun reset() {
        reset = true
        min = defaultMin
        max = defaultMax
    }

    /**
     * Update the min and max value to include the specified range.
     * Does not include NaN or infinite values.
     */
    fun recordRange(min: Float, max: Float) {
        recordValue(min)
        recordValue(max)
    }

    /**
     * Update the min and max value to include the specified value.
     * Does not include NaN (also ignores infinite values if [ignoreInfinites] is true).
     */
    fun recordValue(value: Float) {
        if (!value.isNaN()) {
            if (value.isFinite() || !ignoreInfinites) {
                if (reset) {
                    min = value
                    max = value
                    reset = false
                } else {
                    if (value < min) min = value
                    if (value > max) max = value
                }
            }
        }
    }

    /**
     * Update the min and max value to include the values in the specified collection,
     * in addition to the current min and max values
     */
    fun recordValues(values: Iterable<Float>) {
        for (value in values) {
            recordValue(value)
        }
    }

}
