package fractalutils.time

import fractalutils.checking.Check
import fractalutils.math.orZeroIfNegative

/**
 * Base class for common functionality of Time implementations.
 * @param secondsSinceStart what to initialize elapsed time to.
 * @param stepCount what to initialize elapsed steps to.
 */
abstract class TimeBase(override var secondsSinceStart: Double = 0.0,
                        override var stepCount: Long = 0) : Time {

    // Temporary calculated values
    private var lastStepTimeStamp: Double = 0.toDouble()
    override var stepDurationSeconds: Double = 0.0

    /**
     * @param time used to get the seconds since start and step count to initialize this time to.
     */
    protected constructor(time: Time) : this(time.secondsSinceStart, time.stepCount) {}

    init {
        reset(secondsSinceStart, stepCount)
    }

    override fun reset(secondsSinceStart: Double, stepCount: Long) {
        Check.positiveOrZero(secondsSinceStart, "secondsSinceStart")
        Check.positiveOrZero(stepCount, "stepCount")

        this.secondsSinceStart = secondsSinceStart
        this.stepCount = stepCount

        lastStepTimeStamp = currentTimeSeconds
        stepDurationSeconds = 0.0
    }

    override fun nextStep() {
        val time = currentTimeSeconds
        stepCount++
        stepDurationSeconds = (time - lastStepTimeStamp).orZeroIfNegative()
        secondsSinceStart += stepDurationSeconds
        lastStepTimeStamp = time
    }

    /**
     * @return current time as seconds since some arbitrary epoch that does not change.
     */
    protected abstract val currentTimeSeconds: Double

    override val currentStepElapsedSeconds: Double
        get() = currentTimeSeconds - lastStepTimeStamp

    override fun toString(): String {
        return "${javaClass.simpleName} { " +
                "secondsSinceStart: ${secondsSinceStart}, " +
                "stepCount: ${stepCount}, " +
                "stepDurationSeconds: ${stepDurationSeconds}, " +
                "currentStepElapsedSeconds: ${currentStepElapsedSeconds} }"
    }
}
