package ru.dev4rev.kids.zoobukvy.data.stopwatch

import javax.inject.Inject
import kotlin.time.Duration

class TimeFormatterImpl @Inject constructor(): TimeFormatter {

    override fun formatToString(duration: Duration): String =
        duration.toComponents { hours, minutes, seconds, _ ->
            if (hours == 0L)
                "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
            else
                "$hours:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
        }

}