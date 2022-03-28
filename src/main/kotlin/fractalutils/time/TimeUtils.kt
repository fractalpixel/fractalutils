package fractalutils.time

import fractalutils.strings.toStringWithLeadingZeroes
import fractalutils.thread.threadLocal
import java.util.*

private val MILLISECONDS = 1
private val SECONDS = 1000 * MILLISECONDS
private val MINUTES = 60 * SECONDS
private val HOURS = 60 * MINUTES
private val DAYS = 24 * HOURS

/**
 * Thread local calendar instance to avoid creating instances.
 */
private val tempGregorianCalendar = threadLocal { GregorianCalendar() }


/**
 * Year
 */
val GregorianCalendar.year: Int get() = this.get(GregorianCalendar.YEAR)

/**
 * Month. This is a calendar-specific value. The first month of
 * the year in the Gregorian and Julian calendars is
 * <code>JANUARY</code> which is 0; the last depends on the number
 * of months in a year.
 */
val GregorianCalendar.month: Int get() = this.get(GregorianCalendar.MONTH)

/**
 * Month of year, starting with 1 for January.
 */
val GregorianCalendar.monthStartWithOne: Int get() = this.month + 1

/**
 * day of the month. This is a synonym for <code>DATE</code>.
 * The first day of the month has value 1.
 */
val GregorianCalendar.dayOfMonth: Int get() = this.get(GregorianCalendar.DAY_OF_MONTH)

/**
 * Hour of the day. <code>HOUR_OF_DAY</code> is used for the 24-hour clock.
 * E.g., at 10:04:15.250 PM the <code>HOUR_OF_DAY</code> is 22.
 */
val GregorianCalendar.hourOfDay: Int get() = this.get(GregorianCalendar.HOUR_OF_DAY)

/**
 * Minute within the hour.
 * E.g., at 10:04:15.250 PM the <code>MINUTE</code> is 4.
 */
val GregorianCalendar.minute: Int get() = this.get(GregorianCalendar.MINUTE)

/**
 * Second within the minute.
 * E.g., at 10:04:15.250 PM the <code>SECOND</code> is 15.
 */
val GregorianCalendar.second: Int get() = this.get(GregorianCalendar.SECOND)

/**
 * Millisecond within the second.
 * E.g., at 10:04:15.250 PM the <code>MILLISECOND</code> is 250.
 */
val GregorianCalendar.millisecond: Int get() = this.get(GregorianCalendar.MILLISECOND)


/**
 * @param separator separator to use between different periods.  E.g. "1 hour[separator]5 minutes"
 * @param unitSeparator separator to use between the number and the unit.  E.g. "5[unitSeparator]minutes"
 * @param forceSingular if true, units will be in singular even if they would be in plural, e.g. "5 hour".
 * @return a human readable string representation in english for this number of milliseconds.
 * * E.g. "1 day 5 hours 20 min 4s 100ms"
 */
fun Long.toStringAsTimeInterval(separator: String = " ", unitSeparator: String = " ", forceSingular: Boolean = false): String {
    var milliseconds = this
    val s = StringBuilder()

    // Append minus sign if interval is negative
    if (milliseconds < 0L) {
        s.append("-")
        milliseconds = -milliseconds
    }

    fun appendTimePeriod(periodLength: Long,
                         periodNameSingular: String,
                         periodNamePlural: String,
                         skipIfZero: Boolean) {
        if (milliseconds >= periodLength || !skipIfZero && s.length == 0) {

            if (s.length > 1) s.append(separator)

            // Append number of periods and period name
            val periods = milliseconds / periodLength
            s.append(periods)
                    .append(unitSeparator)
                    .append(if (periods == 1L || forceSingular) periodNameSingular else periodNamePlural)

            // Remove periods from time remaining
            milliseconds %= periodLength
        }
    }

    appendTimePeriod(DAYS.toLong(), "day", "days", true)
    appendTimePeriod(HOURS.toLong(), "hour", "hours", true)
    appendTimePeriod(MINUTES.toLong(), "min", "min", true)
    appendTimePeriod(SECONDS.toLong(), "s", "s", true)
    appendTimePeriod(MILLISECONDS.toLong(), "ms", "ms", false)

    // Should be none left
    assert(milliseconds == 0L)

    return s.toString()
}

/**
 * Converts a timestamp of the type used in java GregorianCalendar class to a standard string representation.
 * @return timestamp in the format "yyyy-mm-dd"[timeSeparator]"hh:mm:ss.sss" if includeTime is true, otherwise "yyyy-mm-dd".
 * @param includeDate if true, the date (yyyy-mm-dd) will be included.
 * @param includeTime if true, the time (hh:mm:ss.ss) will be included.
 * @param timeSeparator separator to use between date and time, only included if both time and date are included.
 */
fun Long.toStringAsTimestamp(includeTime: Boolean = true, timeSeparator: String = " ", includeDate: Boolean = true, includeMilliseconds: Boolean = true): String {
    val calendar = tempGregorianCalendar.get()
    calendar.timeInMillis = this
    val s = StringBuilder()

    if (includeDate) {
        s.append(calendar.year.toStringWithLeadingZeroes(4))
        s.append("-")
        s.append(calendar.monthStartWithOne.toStringWithLeadingZeroes(2))
        s.append("-")
        s.append(calendar.dayOfMonth.toStringWithLeadingZeroes(2))
    }

    if (includeTime && includeDate) {
        s.append(timeSeparator)
    }

    if (includeTime) {
        s.append(calendar.hourOfDay.toStringWithLeadingZeroes(2))
        s.append(":")
        s.append(calendar.minute.toStringWithLeadingZeroes(2))
        s.append(":")
        s.append(calendar.second.toStringWithLeadingZeroes(2))
        if (includeMilliseconds) {
            s.append(".")
            s.append(calendar.millisecond.toStringWithLeadingZeroes(3))
        }
    }

    return s.toString()
}


object TimeUtils {

    /**
     * @param monthOfYear 1 based, 1 = january.
     * @return the timestamp as milliseconds since epoch for the specified georgian calendar date and time.
     */
    fun dateToTimestamp(year: Int, monthOfYear: Int = 1, day: Int = 1, hour: Int = 0, minute: Int = 0, second: Int = 0, millisecond: Int = 0): Long {
        val calendar = tempGregorianCalendar.get()
        calendar.clear()
        calendar.isLenient = true
        calendar.timeZone = TimeZone.getTimeZone("GMT")
        calendar.set(year, monthOfYear - 1, day, hour, minute, second)
        calendar.set(GregorianCalendar.MILLISECOND, millisecond)
        return calendar.timeInMillis
    }

}