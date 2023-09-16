package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.stopwatch.GameStopwatchImpl
import ru.gb.zverobukvy.data.stopwatch.TimeFormatterImpl
import ru.gb.zverobukvy.data.stopwatch.TimeStampProviderImpl
import ru.gb.zverobukvy.data.stopwatch.GameStopwatch
import ru.gb.zverobukvy.data.stopwatch.TimeFormatter
import ru.gb.zverobukvy.data.stopwatch.TimeStampProvider

@Module
interface GameStopwatchModule {

    @Binds
    fun bindGameStopwatch(stopwatch: GameStopwatchImpl): GameStopwatch

    @Binds
    fun bindTimeFormatter(formatter: TimeFormatterImpl): TimeFormatter

    @Binds
    fun bindTimeStampProvider(timeStampProvider: TimeStampProviderImpl): TimeStampProvider
}