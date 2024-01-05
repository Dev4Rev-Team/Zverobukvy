package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.repository_impl.AnimalLettersRepositoryImpl
import ru.dev4rev.kids.zoobukvy.data.repository_impl.SoundStatusRepositoryImpl
import ru.dev4rev.kids.zoobukvy.domain.repository.LoadingDataRepository
import ru.dev4rev.kids.zoobukvy.domain.repository.SoundStatusRepository
import ru.dev4rev.kids.zoobukvy.domain.repository.animal_letter_game.AnimalLettersGameRepository
import ru.dev4rev.kids.zoobukvy.domain.repository.main_menu.MainMenuRepository
import javax.inject.Singleton

@Module(includes = [SharedPreferencesModule::class, DataSourceModule::class, NetworkStatusModule::class])
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindsAnimalLettersGameRepository(repository: AnimalLettersRepositoryImpl): AnimalLettersGameRepository

    @Singleton
    @Binds
    fun bindsMainMenuRepository(repository: AnimalLettersRepositoryImpl): MainMenuRepository

    @Singleton
    @Binds
    fun bindsLoadingDataRepository(repository: AnimalLettersRepositoryImpl): LoadingDataRepository

    @Singleton
    @Binds
    fun bindsSoundStatusRepository(repository: SoundStatusRepositoryImpl): SoundStatusRepository
}