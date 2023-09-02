package ru.gb.zverobukvy.data.stopwatch

import ru.gb.zverobukvy.domain.use_case.stopwatch.TimeStampProvider
import javax.inject.Inject

class TimeStampProviderImpl @Inject constructor() : TimeStampProvider {

    override fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }
}
