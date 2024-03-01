package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.stopwatch.GameStopwatch
import ru.dev4rev.kids.zoobukvy.data.stopwatch.GameStopwatchImpl
import ru.dev4rev.kids.zoobukvy.data.stopwatch.TimeStampProvider
import ru.dev4rev.kids.zoobukvy.data.stopwatch.TimeStampProviderImpl

@Module
interface GameStopwatchModule {

    @Binds
    fun bindGameStopwatch(stopwatch: GameStopwatchImpl): GameStopwatch

    @Binds
    fun bindTimeStampProvider(timeStampProvider: TimeStampProviderImpl): TimeStampProvider
}