package no.bakkenbaeck.mpp.mobile

sealed class HoursOfOperation(
    val hours: List<Hours>
) {

    class Always: HoursOfOperation(DayOfWeek.values().map { day ->
        Hours(day, 0.0f, 24.0f)
    }.toList())

    class Weekdays(fromHour: Float, toHour: Float): HoursOfOperation( DayOfWeek.weekdays.map { day ->
        Hours(day, fromHour, toHour)
    }.toList())

    class Weekends(fromHour: Float, toHour: Float): HoursOfOperation(DayOfWeek.weekendDays.map { day ->
        Hours(day, fromHour, toHour)
    }.toList())

    class Variable(hours: List<Hours>): HoursOfOperation(hours)

    fun isOpen(onDay: DayOfWeek, atHour: Float): Boolean {
        if (atHour > 24.0f) {
            throw RuntimeException("Cannot check greater than 24 hours on any given day")
        }

        val dayHours = hours.filter { it.forDay == onDay }
        val todayHours = dayHours.firstOrNull()

        return if (todayHours != null) {
            (atHour <= todayHours.toHour && atHour >= todayHours.fromHour)
        } else {
            false
        }
    }

    fun isOpenText(onDay: DayOfWeek, atHour: Float): String {
        val time = atHour.toHourString()
        val prefix = "Is open ${onDay.name} at $time?: "

        return if (isOpen(onDay, atHour)) {
            "$prefix YES"
        } else {
            "$prefix NO"
        }
    }

    override fun toString(): String {
        return hours.map { it.toString() }.joinToString("\n")
    }
}