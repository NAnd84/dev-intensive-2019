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
    SECOND{
        override fun plural(value:Int):String{
            return unitStrPlural(value, "секунду", "секунды", "секунд")
        }
    },
    MINUTE{
        override fun plural(value:Int):String{
            return unitStrPlural(value, "минуту", "минуты", "минут")
        }
    },
    HOUR{
        override fun plural(value:Int):String{
            return unitStrPlural(value, "час", "часа", "часов")
        }
    },
    DAY{
        override fun plural(value:Int):String{
            return unitStrPlural(value, "день", "дня", "дней")
        }
    };

    abstract fun plural(value:Int):String
    protected fun unitStrPlural(value:Int, unitStr1:String, unitStr2:String, unitStr3:String): String {
        if(value in 11..14) return "$value $unitStr3"
        val unitStr = when(value % 10){
            1 -> unitStr1
            in 2..4  -> unitStr2
            else -> unitStr3
        }
        return "$value $unitStr"
    }

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
        in -45..-1 -> return "через ${TimeUnits.MINUTE.plural(-minutes.toInt())}"
        in 1..45 -> return "${TimeUnits.MINUTE.plural(minutes.toInt())} назад"
        in 46..75 -> return "час назад"
    }

    when(hours){
        in -26..-23 -> return "через день"
        in -22..-1 -> return "через ${TimeUnits.HOUR.plural(-hours.toInt())}"
        in 1..22 -> return "${TimeUnits.HOUR.plural(hours.toInt())} назад"
        in 23..26 -> return "день назад"
    }

    when(days){
        in -360..-1 -> return "через ${TimeUnits.DAY.plural(-days.toInt())}"
        in 1..360 -> return "${TimeUnits.DAY.plural(days.toInt())} назад"
    }

    return if(days > 360) "более года назад"
    else "более чем через год"

}