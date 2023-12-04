package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.domain.use_case.check_data.CheckData
import ru.gb.zverobukvy.domain.use_case.check_data.CheckDataImpl
import javax.inject.Singleton

@Module
interface CheckDataModule {

    @Singleton
    @Binds
    fun bindCheckData(checkData: CheckDataImpl): CheckData
}