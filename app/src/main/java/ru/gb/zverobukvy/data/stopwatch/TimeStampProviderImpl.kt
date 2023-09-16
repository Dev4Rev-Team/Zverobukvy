package ru.gb.zverobukvy.data.stopwatch

import javax.inject.Inject

class TimeStampProviderImpl @Inject constructor() : TimeStampProvider {

    override fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }
}
