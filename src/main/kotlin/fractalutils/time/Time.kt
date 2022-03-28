package fractalutils.time

/**
 * For keeping track of time in a simulation / game / etc. where time is advanced
 * in distinct time steps.
 *
 * Keeps track of time passed in the simulation and in the real world,
 * number of timesteps handled, and relation of simulation time to real time.
 */
interface Time {

    /**
     * Call this every frame / simulation step to advance the timestep.
     */
    fun nextStep()

    /**
     * @return number of seconds that the most recent full step lasted, in gametime.
     * Returns zero if this is the first tick.
     */
    val stepDurationSeconds: Double

    /**
     * @return number of seconds that the most recent full step lasted, in gametime, as a float.
     * Returns zero if this is the first tick.
     */
    val stepDurationSecondsAsFloat: Float
        get() = stepDurationSeconds.toFloat()

    /**
     * @return number of milliseconds that the most recent full step lasted, in gametime.
     * Returns zero if this is the first tick.
     */
    val stepDurationMilliseconds: Long
            get() = (stepDurationSeconds * SECONDS_TO_MILLISECONDS).toLong()

    /**
     * @return number of seconds that has elapsed sine the start of this step, in gametime.
     * Returns seconds since reset if this is the first tick.
     */
    val currentStepElapsedSeconds: Double

    /**
     * @return number of milliseconds that has elapsed sine the start of this step, in gametime.
     * Returns milliseconds since reset if this is the first tick.
     */
    val currentStepElapsedMilliSeconds: Long
        get() = (currentStepElapsedSeconds * SECONDS_TO_MILLISECONDS).toLong()

    /**
     * @return number of steps per second, based on the last value recorded, in gametime.
     * Returns zero if this is the first tick or lastStepDuration was zero.
     */
    val stepsPerSecond: Double
        get() = if (stepDurationSeconds <= 0.0) 0.0 else 1.0 / stepDurationSeconds

    /**
     * @return number of seconds since the Time was created, or since the last call to reset, in gametime.
     */
    val secondsSinceStart: Double

    /**
     * @return number of milliseconds since the Time was created, or since the last call to reset, in gametime.
     */
    val millisecondsSinceStart: Long
        get() = (secondsSinceStart * SECONDS_TO_MILLISECONDS).toLong()

    /**
     * @return number of steps since Time was created, or since the last call to reset.
     */
    val stepCount: Long

    /**
     * Restarts the time and stepcount from zero.
     */
    fun reset() = reset(0.0, 0L)

    /**
     * Reset to a specific state.
     * @param secondsSinceStart number of seconds since start in gametime.
     * @param stepCount number of steps since start.
     */
    fun reset(secondsSinceStart: Double, stepCount: Long)

    /**
     * Sleeps the current thread for the remaining time of the target steps per second speed,
     * if we have not used that much time so far in this step.
     * If we are slower than the targetStepsPerSecond, do nothing.
     */
    fun sleepUntilTargetUpdatesPerSecond(targetStepsPerSecond: Double) {
        sleepUntilTargetStepDurationSeconds(1.0 / targetStepsPerSecond)
    }

    /**
     * Sleeps the current thread for the remaining time of the target step duration,
     * if we have not used that much time so far in this step.
     * If hve used more time than the target step duration, do nothing.
     */
    fun sleepUntilTargetStepDurationSeconds(targetStepDurationSeconds: Double) {
        val surplusSeconds = targetStepDurationSeconds - currentStepElapsedSeconds
        val sleepTimeMs = (surplusSeconds * 1000).toLong()
        if (sleepTimeMs > 0) Thread.sleep(sleepTimeMs)
    }

    companion object  {
        private val SECONDS_TO_MILLISECONDS = 1000.0
    }

}
