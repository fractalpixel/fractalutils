package fractalutils.updating.strategies

import fractalutils.checking.Check
import fractalutils.time.ManualTime
import fractalutils.time.Time
import fractalutils.updating.Updating


/**
 * Simple UpdateStrategy that just scales the amount of time elapsed with a constant.
 * @param timeScale scale for passing time.
 *                   2 = double the amount of local time will pass for a given amount of external time,
 *                   0.5 = half the amount of local time will pass for a given amount of external time,
 *                   etc.
 */
class ScaledTimestepStrategy(timeScale: Double = 1.0) : UpdateStrategyWithLocalTimeBase() {

    /**
     * scale for passing time.
     *                  2 = double the amount of local time will pass for a given amount of external time,
     *                  0.5 = half the amount of local time will pass for a given amount of external time,
     *                  etc.
     */
    var timeScale: Double = timeScale
        set(timeScale) {
            Check.positive(timeScale, "timeScale")
            field = timeScale
        }

    override fun doUpdate(simulation: Updating, localTime: ManualTime, externalTime: Time) {
        // Cap the elapsed time
        val elapsedTime = externalTime.stepDurationSeconds * timeScale

        // Update local time
        localTime.advanceTimeSeconds(elapsedTime)
        localTime.nextStep()

        // Update simulation
        simulation.update(localTime)
    }
}
