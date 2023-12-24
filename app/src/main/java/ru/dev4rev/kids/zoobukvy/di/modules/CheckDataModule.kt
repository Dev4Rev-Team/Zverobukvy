package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.check_data.CheckData
import ru.dev4rev.kids.zoobukvy.data.check_data.CheckDataImpl
import javax.inject.Singleton

@Module
interface CheckDataModule {

    @Singleton
    @Binds
    fun bindCheckData(checkData: CheckDataImpl): CheckData
}