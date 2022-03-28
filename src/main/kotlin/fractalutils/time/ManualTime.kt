package fractalutils.time

import fractalutils.checking.Check


/**
 * A Time implementation where the current gametime is manually incremented.
 * Useful for testing or time classes that should not be bound to wall clock.
 * @param secondsSinceStart what to initialize elapsed time to.
 * @param stepCount what to initialize elapsed steps to.
 */
class ManualTime(secondsSinceStart: Double = 0.0,
                 stepCount: Long = 0) : TimeBase(secondsSinceStart, stepCount) {

    /**
     * Current manually set time
     */
    override var currentTimeSeconds = 0.0

    /**
     * Creates a new ManualTime.
     * @param time time to initialize secondsSinceStart and stepCount from
     */
    constructor(time: Time) : this(time.secondsSinceStart, time.stepCount) {}

    /**
     * Advances the time by the specified number of seconds.
     */
    fun advanceTimeSeconds(secondsToAdd: Double) {
        Check.positiveOrZero(secondsToAdd, "secondsToAdd")
        currentTimeSeconds += secondsToAdd
    }

    /**
     * Advances the time by the specified number of milliseconds.
     */
    fun advanceTimeMilliseconds(millisecondsToAdd: Long) {
        Check.positiveOrZero(millisecondsToAdd, "millisecondsToAdd")
        currentTimeSeconds += millisecondsToAdd / 1000.0
    }

}
