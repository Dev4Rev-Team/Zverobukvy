package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.stopwatch.GameStopwatchImpl
import ru.gb.zverobukvy.data.stopwatch.TimeFormatterImpl
import ru.gb.zverobukvy.data.stopwatch.TimeStampProviderImpl
import ru.gb.zverobukvy.domain.use_case.stopwatch.GameStopwatch
import ru.gb.zverobukvy.domain.use_case.stopwatch.TimeFormatter
import ru.gb.zverobukvy.domain.use_case.stopwatch.TimeStampProvider

@Module
interface GameStopwatchModule {

    @Binds
    fun bindGameStopwatch(stopwatch: GameStopwatchImpl): GameStopwatch

    @Binds
    fun bindTimeFormatter(formatter: TimeFormatterImpl): TimeFormatter

    @Binds
    fun bindTimeStampProvider(timeStampProvider: TimeStampProviderImpl): TimeStampProvider
}