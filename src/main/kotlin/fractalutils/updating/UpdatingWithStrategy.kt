package fractalutils.updating

import fractalutils.time.Time
import fractalutils.updating.strategies.UpdateStrategy

/**
 * An Updating simulation, using a doUpdate method to run the simulation,
 * along with an UpdateStrategy that is used to modify the build time and frequency.
 * Used as a base class for Updating implementations that can have some strategy
 * @param updateStrategy strategy to use when updating, or null to not use any special strategy,
 *                       just build the simulation directly with the provided time.
 */
abstract class UpdatingWithStrategy(var updateStrategy: UpdateStrategy? = null) : Updating {

    private val thisUpdating by lazy { object : Updating {
            override fun update(time: Time) {
                doUpdate(time)
            }
        }
    }

    final override fun update(time: Time) {
        val strategy = updateStrategy
        if (strategy != null) strategy.update(thisUpdating, time)
        else doUpdate(time)
    }

    /**
     * Run the build.
     */
    protected abstract fun doUpdate(time: Time)
}
