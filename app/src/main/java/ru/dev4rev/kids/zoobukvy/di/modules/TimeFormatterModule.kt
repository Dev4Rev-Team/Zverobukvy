package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.stopwatch.TimeFormatter
import ru.dev4rev.kids.zoobukvy.data.stopwatch.TimeFormatterImpl

@Module
interface TimeFormatterModule {
    @Binds
    fun bindTimeFormatter(formatter: TimeFormatterImpl): TimeFormatter
}