package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.data_source.RemoteDataSource
import ru.gb.zverobukvy.data.data_source_impl.LocalDataSourceImpl
import ru.gb.zverobukvy.data.data_source_impl.RemoteDataSourceImpl

@Module(includes = [RoomModule::class, RetrofitModule::class])
interface DataSourceModule {

    @Binds
    fun bindLocalDataSource(dataSource: LocalDataSourceImpl): LocalDataSource

    @Binds
    fun bindRemoteDataSource(dataSource: RemoteDataSourceImpl): RemoteDataSource
}