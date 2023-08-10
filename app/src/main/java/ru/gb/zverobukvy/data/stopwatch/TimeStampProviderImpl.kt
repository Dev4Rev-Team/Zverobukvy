package ru.gb.zverobukvy.data.stopwatch

import ru.gb.zverobukvy.domain.use_case.stopwatch.TimeStampProvider

class TimeStampProviderImpl() : TimeStampProvider {

    override fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }
}
