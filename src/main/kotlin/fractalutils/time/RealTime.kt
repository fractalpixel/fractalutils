package fractalutils.time

import fractalutils.checking.Check


/**
 * Time class that uses real time passed as gametime.
 * @param secondsSinceStart what to initialize elapsed time to.
 * @param stepCount what to initialize elapsed steps to.
 * @param speedupFactor factor for converting real time to gametime.
 * *                      1 -> 1:1, 2 -> gametime twice as fast as real time, 0.5 -> gametime half as fast as real time.
 */
class RealTime(secondsSinceStart: Double = 0.0, stepCount: Long = 0, speedupFactor: Double = 1.0) : TimeBase(secondsSinceStart, stepCount) {

    private var startTime: Long = System.currentTimeMillis()
    private var currentTimeAtStart: Double = 0.0

    /**
     * Creates a new RealTime based on the specified time.
     * @param time time to initialize secondsSinceStart and stepCount from
     */
    constructor(time: Time) : this(time.secondsSinceStart, time.stepCount) {}

    /**
     * SpeedupFactor applied to time.
     *   1: 1 real second == 1 game second,
     *   2: 1 real second == 2 game seconds,
     *   0.5: 1 real second == 0.5 game seconds,
     *   etc.
     */
    var speedupFactor: Double = speedupFactor
        set(speedupFactor) {
            Check.positive(speedupFactor, "speedupFactor")

            // Ensure that currentTimeSeconds does not jump if the speedupFactor is changed
            currentTimeAtStart = currentTimeSeconds
            startTime = System.currentTimeMillis()

            this.speedupFactor = speedupFactor
        }

    override val currentTimeSeconds: Double
        get() {
            // Count time from last speedupFactor change
            val elapsedTimeMs = System.currentTimeMillis() - startTime
            return currentTimeAtStart + elapsedTimeMs * speedupFactor / 1000.0
        }

}
