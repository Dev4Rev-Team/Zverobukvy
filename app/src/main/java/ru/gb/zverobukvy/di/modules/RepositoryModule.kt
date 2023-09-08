package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.repository_impl.AnimalLettersRepositoryImpl
import ru.gb.zverobukvy.domain.repository.AnimalLettersGameRepository
import ru.gb.zverobukvy.domain.repository.MainMenuRepository
import javax.inject.Singleton

@Module(includes = [SharedPreferencesModule::class, DataSourceModule::class, NetworkStatusModule::class])
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindsAnimalLettersGameRepository(repository: AnimalLettersRepositoryImpl): AnimalLettersGameRepository

    @Singleton
    @Binds
    fun bindsMainMenuRepository(repository: AnimalLettersRepositoryImpl): MainMenuRepository
}