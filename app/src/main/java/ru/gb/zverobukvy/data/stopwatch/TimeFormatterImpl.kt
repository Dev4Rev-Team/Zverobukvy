package ru.gb.zverobukvy.data.stopwatch

import ru.gb.zverobukvy.domain.use_case.stopwatch.TimeFormatter
import javax.inject.Inject

class TimeFormatterImpl @Inject constructor(): TimeFormatter {

    override fun format(timeInMillisecond: Long): String {

        val seconds = timeInMillisecond / 1000
        val secondsFormat = (seconds % 60).toString().padStart(2, '0')

        val minutes = seconds / 60
        val minutesFormat = (minutes % 60).toString().padStart(2, '0')

        val hours = minutes / 60
        val hoursFormat = hours.toString().padStart(2, '0')

        return if (hours > 0) {
            "$hoursFormat:$minutesFormat:$secondsFormat"
        } else {
            "$minutesFormat:$secondsFormat"
        }
    }
}