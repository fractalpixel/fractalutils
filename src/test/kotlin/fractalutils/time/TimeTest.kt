package fractalutils.time

import fractalutils.time.ManualTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Test Time class.
 */
class TimeTest {

    private val EPSILON = 0.001

    @Test
    @Throws(Exception::class)
    fun testTime() {
        val time = ManualTime()

        assertEquals(0, time.stepDurationMilliseconds)
        assertEquals(0.0, time.stepDurationSeconds, EPSILON)
        assertEquals(0, time.stepCount)
        assertEquals(0.0, time.stepsPerSecond, EPSILON)
        assertEquals(0, time.millisecondsSinceStart)
        assertEquals(0.0, time.secondsSinceStart, EPSILON)
        assertEquals(0.0, time.currentStepElapsedSeconds, EPSILON)

        time.advanceTimeSeconds(0.1)

        assertEquals(0, time.stepDurationMilliseconds)
        assertEquals(0.0, time.stepDurationSeconds, EPSILON)
        assertEquals(0, time.stepCount)
        assertEquals(0.0, time.stepsPerSecond, EPSILON)
        assertEquals(0, time.millisecondsSinceStart)
        assertEquals(0.0, time.secondsSinceStart, EPSILON)
        assertEquals(0.1, time.currentStepElapsedSeconds, EPSILON)

        time.nextStep()

        assertEquals(100, time.stepDurationMilliseconds)
        assertEquals(0.1, time.stepDurationSeconds, EPSILON)
        assertEquals(1, time.stepCount)
        assertEquals(10.0, time.stepsPerSecond, EPSILON)
        assertEquals(100, time.millisecondsSinceStart)
        assertEquals(0.1, time.secondsSinceStart, EPSILON)
        assertEquals(0.0, time.currentStepElapsedSeconds, EPSILON)


        time.advanceTimeSeconds(0.0)
        time.nextStep()

        assertEquals(0, time.stepDurationMilliseconds)
        assertEquals(0.0, time.stepDurationSeconds, EPSILON)
        assertEquals(2, time.stepCount)
        assertEquals(0.0, time.stepsPerSecond, EPSILON)
        assertEquals(100, time.millisecondsSinceStart)
        assertEquals(0.1, time.secondsSinceStart, EPSILON)
        assertEquals(0.0, time.currentStepElapsedSeconds, EPSILON)

        time.advanceTimeSeconds(1.0)
        time.nextStep()
        time.advanceTimeSeconds(0.3)

        assertEquals(1000, time.stepDurationMilliseconds)
        assertEquals(1.0, time.stepDurationSeconds, EPSILON)
        assertEquals(3, time.stepCount)
        assertEquals(1.0, time.stepsPerSecond, EPSILON)
        assertEquals(1100, time.millisecondsSinceStart)
        assertEquals(1.1, time.secondsSinceStart, EPSILON)
        assertEquals(0.3, time.currentStepElapsedSeconds, EPSILON)

        time.reset()

        assertEquals(0, time.stepDurationMilliseconds)
        assertEquals(0.0, time.stepDurationSeconds, EPSILON)
        assertEquals(0, time.stepCount)
        assertEquals(0.0, time.stepsPerSecond, EPSILON)
        assertEquals(0, time.millisecondsSinceStart)
        assertEquals(0.0, time.secondsSinceStart, EPSILON)
        assertEquals(0.0, time.currentStepElapsedSeconds, EPSILON)

        time.reset(0.5, 11)

        assertEquals(0, time.stepDurationMilliseconds)
        assertEquals(0.0, time.stepDurationSeconds, EPSILON)
        assertEquals(11, time.stepCount)
        assertEquals(0.0, time.stepsPerSecond, EPSILON)
        assertEquals(500, time.millisecondsSinceStart)
        assertEquals(0.5, time.secondsSinceStart, EPSILON)
        assertEquals(0.0, time.currentStepElapsedSeconds, EPSILON)
    }
}
