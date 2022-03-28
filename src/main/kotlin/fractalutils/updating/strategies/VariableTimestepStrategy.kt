package fractalutils.updating.strategies

import fractalutils.time.Time
import fractalutils.updating.Updating


/**
 * Simple UpdateStrategy that simply uses the provided time for updating.
 *
 * If the provided time is e.g. updated every frame, it leads to a variable timestep strategy where the
 * timestep is the duration of the last frame.
 *
 * It is not very suitable for most physics simulations, but will suffice well for non-time sensitive things.
 */
class VariableTimestepStrategy : UpdateStrategy {

    override fun update(simulation: Updating, externalTime: Time) {
        simulation.update(externalTime)
    }

}
