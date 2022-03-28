package fractalutils.updating.strategies

import fractalutils.checking.Check
import fractalutils.time.ManualTime
import fractalutils.time.Time
import fractalutils.updating.Updating

/**
 * A UpdateStrategy that calls the enclosed simulation at a specific fixed rate, with a specific max number of calls.
 *
 * For each build call to the strategy, the simulation may be called zero or more times, depending on how long
 * time has elapsed since the last call to the simulation.
 *
 * The Time instance provided in the build method is used to determine times, so the provided time does not have to
 * agree with real time.
 *
 * @param timestepSeconds the time in seconds between build calls to the simulation.  Defaults to 0.01
 * @param maxUpdatesPerCall maximum number of times that the delegate build function can be called for one build of this strategy.
 *                          If this number of times is reached, any remaining time is discarded.
 *                          Defaults to 3.
 */
class CappedFixedTimestepStrategy(timestepSeconds: Double = 0.01, val maxUpdatesPerCall: Int = 3, val updateWithRemaining: Boolean = false) : UpdateStrategyWithLocalTimeBase() {

    /**
     * The time in seconds between build calls to the simulation.
     */
    var timestepSeconds: Double = timestepSeconds
        set(timestepSeconds) {
            Check.positive(timestepSeconds, "timestepSeconds")
            this.timestepSeconds = timestepSeconds
        }

    private var timeSinceLastUpdateCall_seconds = 0.0

    override fun doUpdate(simulation: Updating, localTime: ManualTime, externalTime: Time) {
        // Add elapsed time since the last build call to the backlog
        timeSinceLastUpdateCall_seconds += externalTime.stepDurationSeconds

        val timestepSeconds = this.timestepSeconds
        var allowedUpdatesLeft = maxUpdatesPerCall
        while (allowedUpdatesLeft > 0 && timeSinceLastUpdateCall_seconds > timestepSeconds) {
            allowedUpdatesLeft--

            // Decrease built up time backlog by one simulation step size
            timeSinceLastUpdateCall_seconds -= timestepSeconds

            // Update local time
            localTime.advanceTimeSeconds(timestepSeconds)
            localTime.nextStep()

            // Update simulation
            simulation.update(localTime)
        }

        if (updateWithRemaining && timeSinceLastUpdateCall_seconds > 0 && timeSinceLastUpdateCall_seconds <= timestepSeconds) {
            // Update simulation with remaining time
            localTime.advanceTimeSeconds(timeSinceLastUpdateCall_seconds)
            timeSinceLastUpdateCall_seconds = 0.0
            localTime.nextStep()
            simulation.update(localTime)
        }

        // If we used more than our allotted time steps, just discard any overflow time
        if (allowedUpdatesLeft <= 0) {
            timeSinceLastUpdateCall_seconds = 0.0
        }
    }

}
