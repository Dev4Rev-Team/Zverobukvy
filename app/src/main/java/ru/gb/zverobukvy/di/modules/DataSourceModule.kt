package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.data_source.RemoteDataSource
import ru.gb.zverobukvy.data.data_source.LocalDataSourceImpl
import ru.gb.zverobukvy.data.data_source.RemoteDataSourceImpl
import javax.inject.Singleton

@Module(includes = [RoomModule::class, RetrofitModule::class])
interface DataSourceModule {

    @Singleton
    @Binds
    fun bindLocalDataSource(dataSource: LocalDataSourceImpl): LocalDataSource

    @Singleton
    @Binds
    fun bindRemoteDataSource(dataSource: RemoteDataSourceImpl): RemoteDataSource
}