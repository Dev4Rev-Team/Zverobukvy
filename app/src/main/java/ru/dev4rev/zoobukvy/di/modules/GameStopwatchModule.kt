package ru.dev4rev.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.zoobukvy.data.stopwatch.GameStopwatchImpl
import ru.dev4rev.zoobukvy.data.stopwatch.TimeFormatterImpl
import ru.dev4rev.zoobukvy.data.stopwatch.TimeStampProviderImpl
import ru.dev4rev.zoobukvy.data.stopwatch.GameStopwatch
import ru.dev4rev.zoobukvy.data.stopwatch.TimeFormatter
import ru.dev4rev.zoobukvy.data.stopwatch.TimeStampProvider

@Module
interface GameStopwatchModule {

    @Binds
    fun bindGameStopwatch(stopwatch: GameStopwatchImpl): GameStopwatch

    @Binds
    fun bindTimeFormatter(formatter: TimeFormatterImpl): TimeFormatter

    @Binds
    fun bindTimeStampProvider(timeStampProvider: TimeStampProviderImpl): TimeStampProvider
}