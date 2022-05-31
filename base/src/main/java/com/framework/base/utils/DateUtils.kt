package com.framework.base.utils

import android.annotation.SuppressLint
import android.content.Context
import com.framework.base.R
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author yu
 * @version 1.0
 * @date 2021/4/16
 * @description 时间日期工具类
 */
@SuppressLint("SimpleDateFormat")
object DateUtils {
    const val YYYY = "yyyy"
    const val YYYY_MM = "yyyy-MM"
    const val YYYY_MM_DD = "yyyy-MM-dd"
    const val YYYY_MM_DD_HH_MM = "yyyyMMddHHmm"
    const val YYYYMMDDHHMMSS = "yyyyMMddHHmmss"
    const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
    const val MM_DD_HH_MM = "MM-dd HH:mm"
    private val parsePatterns = arrayOf(
        "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
        "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
        "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
    )
    private val format by lazy {
        SimpleDateFormat(YYYY_MM_DD_HH_MM_SS)
    }

    fun format(date: Date?, pattern: String?): String? {
        var returnValue = ""
        if (date != null) {
            format.applyPattern(pattern)
            returnValue = format.format(date)
        }
        return returnValue
    }

    fun formatTimeForSeconds(
        timeInSeconds: Long,
        formatPattern: String = YYYY_MM_DD_HH_MM_SS
    ): String? {
        return formatTimeForMilliSecond(timeInSeconds * 1000, formatPattern)
    }

    fun formatTimeForMilliSecond(
        timeInMilliSeconds: Long,
        formatPattern: String = YYYY_MM_DD_HH_MM_SS
    ): String? {
        format.applyPattern(formatPattern)
        return format.format(Date(timeInMilliSeconds))
    }

    fun timeStringToDate(timeString: String?, formatPattern: String = YYYY_MM_DD_HH_MM_SS): Date? {
        if (timeString.isNullOrEmpty()) {
            return null
        }
        format.applyPattern(formatPattern)
        return format.parse(timeString)
    }

    fun timeStringToTimeStamp(
        timeString: String?,
        formatPattern: String = YYYY_MM_DD_HH_MM_SS
    ): Long? {
        return timeStringToDate(timeString, formatPattern)?.time ?: 0
    }

    fun isSameDay(
        firstTimeInMillis: Long,
        secondTimeInMillis: Long
    ): Boolean {
        format.applyPattern(YYYY_MM_DD)
        val firstTimeString = format.format(Date(firstTimeInMillis))
        val secondTimeString = format.format(Date(secondTimeInMillis))
        return firstTimeString == secondTimeString
    }

    /**
     * 得到当前日期前后多少天，月，年的日期字符串
     * @param pattern 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     * @param amont 数量，前为负数，后为正数
     * @param type 类型，可参考Calendar的常量(如：Calendar.HOUR、Calendar.MINUTE、Calendar.SECOND)
     * @return
     */
    fun getDate(pattern: String = YYYY_MM_DD_HH_MM_SS, amont: Int, type: Int): String? {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(type, amont)
        return formatTimeForMilliSecond(calendar.time.time, pattern)
    }

    /**
     * 得到当前时间字符串
     */
    fun getTime(pattern: String = YYYY_MM_DD_HH_MM_SS): String? {
        format.applyPattern(pattern)
        return format.format(Date())
    }

    /**
     * 获取过去的天数
     * @param date
     * @return
     */
    fun pastDays(date: Date): Long {
        return pastMillis(date) / (24 * 60 * 60 * 1000)
    }


    /**
     * 获取过去的小时
     * @param date
     * @return
     */
    fun pastHour(date: Date): Long {
        return pastMillis(date) / (60 * 60 * 1000)
    }

    /**
     * 获取过去的分钟
     * @param date
     * @return
     */
    fun pastMinutes(date: Date): Long {
        return pastMillis(date) / (60 * 1000)
    }

    /**
     * 获取过去的秒
     * @param date
     * @return
     */
    fun pastSecond(date: Date): Long {
        return pastMillis(date) / 1000
    }

    /**
     * 获取过去的毫秒秒
     * @param date
     * @return
     */
    fun pastMillis(date: Date): Long {
        return System.currentTimeMillis() - date.time
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    fun getDistanceOfTwoDate(firstTimeInMillis: Long, secondTimeInMillis: Long): Double {
        return ((secondTimeInMillis - firstTimeInMillis) / (1000 * 60 * 60 * 24)).toDouble()
    }

    /**
     * 获取某月有几天
     * @param date 日期
     * @return 天数
     */
    fun getMonthHasDays(date: Date): Int {
        format.applyPattern("yyyyMM")
        val yyyyMM: String = format.format(date)
        val year = yyyyMM.substring(0, 4)
        val month = yyyyMM.substring(4, 6)
        val day31 = ",01,03,05,07,08,10,12,"
        val day30 = "04,06,09,11"
        var day = 0
        day = if (day31.contains(month)) {
            31
        } else if (day30.contains(month)) {
            30
        } else {
            val y = year.toInt()
            if (y % 4 == 0 && y % 100 != 0 || y % 400 == 0) {
                29
            } else {
                28
            }
        }
        return day
    }

    /**
     * 获取日期是当年的第几周
     * @param date
     * @return
     */
    fun getWeekOfYear(date: Date): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal[Calendar.WEEK_OF_YEAR]
    }

    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    fun getTimeFormatText(context: Context,date: Date?): String? {
        if (date == null) {
            return null
        }
        val calendar = Calendar.getInstance()
        val currentDayIndex = calendar[Calendar.DAY_OF_YEAR]
        val currentYear = calendar[Calendar.YEAR]
        calendar.time = date
        val msgYear = calendar[Calendar.YEAR]
        val msgDayIndex = calendar[Calendar.DAY_OF_YEAR]
        val msgMinute = calendar[Calendar.MINUTE]
        var msgTimeStr = calendar[Calendar.HOUR_OF_DAY].toString() + ":"
        msgTimeStr = if (msgMinute < 10) {
            msgTimeStr + "0" + msgMinute
        } else {
            msgTimeStr + msgMinute
        }
        val msgDayInWeek = calendar[Calendar.DAY_OF_WEEK]
        msgTimeStr = if (currentDayIndex == msgDayIndex) {
            return msgTimeStr
        } else {
            if (currentDayIndex - msgDayIndex == 1 && currentYear == msgYear) {
                context.getString(R.string.date_yesterday) + msgTimeStr
            } else if (false /*currentDayIndex - msgDayIndex > 1 && currentYear == msgYear*/) { //本年消息,注释掉统一按照 "年/月/日" 格式显示
                //不同周显示具体月，日，注意函数：calendar.get(Calendar.MONTH) 一月对应0，十二月对应11
                Integer.valueOf(calendar[Calendar.MONTH] + 1)
                    .toString() + "/" + calendar[Calendar.DAY_OF_MONTH] + " " + msgTimeStr + " "
                //msgTimeStr = (Integer.valueOf(calendar.get(Calendar.MONTH) + 1)) + context.getString(R.string.date_month_short) + " "+ calendar.get(Calendar.DAY_OF_MONTH) + context.getString(R.string.date_day_short) + " " + msgTimeStr + " ";
            } else { // 1、非正常时间，如currentYear < msgYear，或者currentDayIndex < msgDayIndex
                //2、非本年消息（currentYear > msgYear），如：历史消息是2018，今年是2019，显示年、月、日
                msgYear.toString() + "/" + Integer.valueOf(calendar[Calendar.MONTH] + 1) + "/" + calendar[Calendar.DAY_OF_MONTH] + " " + msgTimeStr + " "
                //msgTimeStr = msgYear + context.getString(R.string.date_year_short) + (Integer.valueOf(calendar.get(Calendar.MONTH) + 1)) + context.getString(R.string.date_month_short) + calendar.get(Calendar.DAY_OF_MONTH) + context.getString(R.string.date_day_short) + msgTimeStr + " ";
            }
        }
        return msgTimeStr
    }

    fun formatSeconds(context: Context,seconds: Long): String? {
        var timeStr: String = "$seconds" + context.getString(R.string.date_second_short)
        if (seconds > 60) {
            val second = seconds % 60
            var min = seconds / 60
            timeStr =
                "$min" + context.getString(R.string.date_minute_short) + second + context.getString(R.string.date_second_short)
            if (min > 60) {
                min = seconds / 60 % 60
                var hour = seconds / 60 / 60
                timeStr =
                    "$hour" + context.getString(R.string.date_hour_short) + min + context.getString(R.string.date_minute_short) + second + context.getString(
                        R.string.date_second_short
                    )
                if (hour % 24 == 0L) {
                    val day = seconds / 60 / 60 / 24
                    timeStr = "$day" + context.getString(R.string.date_day_short)
                } else if (hour > 24) {
                    hour = seconds / 60 / 60 % 24
                    val day = seconds / 60 / 60 / 24
                    timeStr =
                        "$day" + context.getString(R.string.date_day_short) + hour + context.getString(
                            R.string.date_hour_short
                        ) + min + context.getString(R.string.date_minute_short) + second + context.getString(
                            R.string.date_second_short
                        )
                }
            }
        }
        return timeStr
    }

    fun formatSecondsTo00(timeSeconds: Int): String {
        val second = timeSeconds % 60
        val minuteTemp = timeSeconds / 60
        return if (minuteTemp > 0) {
            val minute = minuteTemp % 60
            val hour = minuteTemp / 60
            if (hour > 0) {
                ((if (hour >= 10) hour.toString() + "" else "0$hour") + ":" + (if (minute >= 10) minute.toString() + "" else "0$minute")
                        + ":" + if (second >= 10) second.toString() + "" else "0$second")
            } else {
                ((if (minute >= 10) minute.toString() + "" else "0$minute") + ":"
                        + if (second >= 10) second.toString() + "" else "0$second")
            }
        } else {
            "00:" + if (second >= 10) second.toString() + "" else "0$second"
        }
    }
}