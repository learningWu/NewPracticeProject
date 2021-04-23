package com.chulan.newtestproject.util

import java.text.DateFormat
import java.util.*

class Day {
    var hour: Int = 0
    var minute: Int = 0
    var second: Int = 0
    var calendar = Calendar.getInstance(Locale.getDefault())
    fun updateTime(timeMills: Long){
        calendar.clear()
        calendar.timeInMillis = timeMills
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        second = calendar.get(Calendar.SECOND)
    }
}