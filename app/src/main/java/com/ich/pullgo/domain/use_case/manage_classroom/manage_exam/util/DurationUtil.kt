package com.ich.pullgo.domain.use_case.manage_classroom.manage_exam.util

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

class DurationUtil {
    companion object{
        fun translateISO8601Format(time: String): String{
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.KOREAN)
            val date = format.parse(time)
            val formatter = SimpleDateFormat("MM/dd HH:mm", Locale.KOREAN)
            return formatter.format(date!!)
        }

        fun translateDurToMinute(time: String): String{
            return Duration.parse(time).toMinutes().toString()
        }

        fun MillToDate(mills: Long): String? {
            val pattern = "yyyy-MM-dd"
            val formatter = SimpleDateFormat(pattern)
            return formatter.format(Timestamp(mills))
        }

        fun translateDurToMillis(time: String): Long{
            return Duration.parse(time).toMillis()
        }
    }
}