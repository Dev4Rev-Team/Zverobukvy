package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.check_data.CheckData
import ru.gb.zverobukvy.data.check_data.CheckDataImpl
import javax.inject.Singleton

@Module
interface CheckDataModule {

    @Singleton
    @Binds
    fun bindCheckData(checkData: CheckDataImpl): CheckData
}