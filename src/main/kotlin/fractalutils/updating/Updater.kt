package fractalutils.updating

import fractalutils.time.Time
import fractalutils.updating.strategies.UpdateStrategy

/**
 * An Updating simulation, along with an UpdateStrategy that is used for it.
 * @param simulation simulation to build. If null, does noting
 * @param updateStrategy strategy to use when updating, or null to not use any special strategy,
 * *                       just build the simulation directly with the provided time.
 */
class Updater(var simulation: Updating?, updateStrategy: UpdateStrategy? = null) : UpdatingWithStrategy(updateStrategy) {

    override fun doUpdate(time: Time) {
        simulation?.update(time)
    }

}
