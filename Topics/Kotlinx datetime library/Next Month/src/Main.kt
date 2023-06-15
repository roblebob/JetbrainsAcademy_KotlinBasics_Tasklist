import kotlinx.datetime.*

fun nextMonth(date: String): String = date.toInstant().plus(DateTimePeriod(months = 1), TimeZone.UTC).toString()

fun main() {
    val date = readln()
    println(nextMonth(date))
}