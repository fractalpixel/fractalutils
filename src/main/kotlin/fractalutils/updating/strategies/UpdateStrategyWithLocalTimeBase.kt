package fractalutils.updating.strategies

import fractalutils.time.ManualTime
import fractalutils.time.Time
import fractalutils.updating.Updating

/**
 * Common functionality for UpdateStrategies that use a localTime Time instance to keep track of simulation time.
 */
abstract class UpdateStrategyWithLocalTimeBase : UpdateStrategy {

    private var localTime: ManualTime? = null

    override fun update(simulation: Updating, externalTime: Time) {
        // Initialize local time if needed
        if (localTime == null) {
            localTime = createLocalTime(externalTime)
        }

        doUpdate(simulation, localTime!!, externalTime)
    }

    /**
     * @param simulation simulation to build
     * @param localTime local time managed by this UpdateStrategy, and usually passed to the simulation
     * @param externalTime time passed in as a parameter when the build on this UpdateStrategy was called.
     */
    protected abstract fun doUpdate(simulation: Updating, localTime: ManualTime, externalTime: Time)

    /**
     * @param externalTime time passed in as a parameter when the build on this UpdateStrategy was called the first time.
     * @return a new ManualTime instance, initialized based on the specified external time.
     */
    protected fun createLocalTime(externalTime: Time): ManualTime {
        return ManualTime(externalTime)
    }
}
