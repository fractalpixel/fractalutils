package fractalutils.updating.strategies

import fractalutils.checking.Check
import fractalutils.math.mix
import fractalutils.time.ManualTime
import fractalutils.time.Time
import fractalutils.updating.Updating


/**
 * UpdateStrategy that smooths the elapsed time by averaging it over time.
 * @param smoothingFactor how much to smooth the timeSteps.  Defaults to 0.5.
 *                        0 = no smoothing, use external time steps directly,
 *                        0.5 = average external time step and previous smoothed time step,
 *                        approaching 1 = use almost completely previous smoothed time step.
 */
class SmoothedTimestepStrategy(smoothingFactor: Double = 0.5) : UpdateStrategyWithLocalTimeBase() {

    private var previousSmoothedDuration: Double = 0.0
    private var firstStep = true

    /**
     * how much to smooth the timeSteps.
     *                        0 = no smoothing, use external time steps directly,
     *                        0.5 = average external time step and previous smoothed time step,
     *                        approaching 1 = use almost completely previous smoothed time step.
     */
    var smoothingFactor: Double = smoothingFactor
        set(smoothingFactor) {
            Check.inRange(smoothingFactor, "smoothingFactor", 0.0, 1.0)
            this.smoothingFactor = smoothingFactor
        }

    override fun doUpdate(simulation: Updating, localTime: ManualTime, externalTime: Time) {
        // Smooth
        val externalElapsedTime = externalTime.stepDurationSeconds
        val smoothedElapsedTime = if (firstStep) externalElapsedTime else mix(smoothingFactor, externalElapsedTime, previousSmoothedDuration)
        previousSmoothedDuration = smoothedElapsedTime
        firstStep = false

        // Update local time
        localTime.advanceTimeSeconds(smoothedElapsedTime)
        localTime.nextStep()

        // Update simulation
        simulation.update(localTime)
    }
}
