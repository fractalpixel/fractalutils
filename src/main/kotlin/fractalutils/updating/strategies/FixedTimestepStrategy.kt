package fractalutils.updating.strategies

import fractalutils.checking.Check
import fractalutils.time.ManualTime
import fractalutils.time.Time
import fractalutils.updating.Updating

/**
 * A UpdateStrategy that calls the enclosed simulation at a specific fixed rate.
 *
 * For each build call to the strategy, the simulation may be called zero or more times, depending on how long
 * time has elapsed since the last call to the simulation.
 *
 * The Time instance provided in the build method is used to determine times, so the provided time does not have to
 * agree with real time.
 * @param timestepSeconds the time in seconds between build calls to the simulation.  Defaults to 10 milliseconds.
 */
class FixedTimestepStrategy(timestepSeconds: Double = 0.01) : UpdateStrategyWithLocalTimeBase() {

    /**
     * The time in seconds between build calls to the simulation.
     */
    var timestepSeconds: Double = timestepSeconds
        set(timestepSeconds) {
            Check.positive(timestepSeconds, "timestepSeconds")
            field = timestepSeconds
        }

    private var timeSinceLastUpdateCall_seconds = 0.0

    override fun doUpdate(simulation: Updating, localTime: ManualTime, externalTime: Time) {
        // Add elapsed time since the last build call to the backlog
        timeSinceLastUpdateCall_seconds += externalTime.stepDurationSeconds

        while (timeSinceLastUpdateCall_seconds > timestepSeconds) {
            // Decrease built up time backlog by one simulation step size
            timeSinceLastUpdateCall_seconds -= timestepSeconds

            // Update local time
            localTime.advanceTimeSeconds(timestepSeconds)
            localTime.nextStep()

            // Update simulation
            simulation.update(localTime)
        }
    }
}
