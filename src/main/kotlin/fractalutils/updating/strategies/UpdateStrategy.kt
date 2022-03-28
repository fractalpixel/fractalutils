package fractalutils.updating.strategies

import fractalutils.time.Time
import fractalutils.updating.Updating

/**
 * Encapsulates some build over time.
 * Note that this strategy may store state information related to things like timesteps,
 * so it should only be used to build one simulation.
 */
interface UpdateStrategy {

    /**
     * Updates the specified simulation with the specified time, using this UpdateStrategy.
     * @param simulation simulation to build.
     * @param externalTime time to use when updating.
     */
    fun update(simulation: Updating, externalTime: Time)

}
