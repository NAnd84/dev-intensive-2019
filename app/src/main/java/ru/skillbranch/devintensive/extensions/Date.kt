package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Period
import java.time.temporal.ChronoUnit
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateformat = SimpleDateFormat(pattern, Locale("ru"))
    return dateformat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {

    val difference =  date.time - this.time

    val seconds = difference / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    when(seconds){
        in -75..-46 -> return "через минуту"
        in -45..-2 -> return "через несколько секунд"
        in -1..1 -> return "только что"
        in 2..45 -> return "несколько секунд назад"
        in 46..75 -> return "минуту назад"
    }

    when(minutes){
        in -75..-46 -> return "через час"
        in -45..-1 -> return "через ${-minutes} минут(ы)"
        in 1..45 -> return "$minutes минут(ы) назад"
        in 46..75 -> return "час назад"
    }

    when(hours){
        in -26..-23 -> return "через день"
        in -22..-1 -> return "через ${-hours} час(ов)"
        in 1..22 -> return "$hours час(ов) назад"
        in 23..26 -> return "день назад"
    }

    when(days){
        in -360..-1 -> return "через ${-days} дней"
        in 1..360 -> return "$days дней назад"
    }

    return if(days > 360) "больше года назад"
    else "через год"

}