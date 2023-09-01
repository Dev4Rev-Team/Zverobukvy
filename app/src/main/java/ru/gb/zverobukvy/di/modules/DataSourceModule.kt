package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.data_source_impl.LocalDataSourceImpl

@Module(includes = [RoomModule::class])
interface DataSourceModule {

    @Binds
    fun bindLocalDataSource(dataSource: LocalDataSourceImpl): LocalDataSource
}