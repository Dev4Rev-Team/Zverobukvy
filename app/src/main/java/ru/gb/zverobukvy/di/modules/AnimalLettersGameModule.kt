package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.di.AnimalLettersGameScope
import ru.gb.zverobukvy.domain.use_case.AnimalLettersInteractor
import ru.gb.zverobukvy.domain.use_case.AnimalLettersInteractorImpl
import ru.gb.zverobukvy.presentation.game_zverobukvy.GameZverobukvyViewModel
import ru.gb.zverobukvy.presentation.game_zverobukvy.GameZverobukvyViewModelImpl

@Module(includes = [GameStopwatchModule::class, RepositoryModule::class])
interface AnimalLettersGameModule {

    @Binds
    @AnimalLettersGameScope
    fun bindAnimalLettersInteractor(interactor: AnimalLettersInteractorImpl): AnimalLettersInteractor

    @Binds
    @AnimalLettersGameScope
    fun bindAnimalLettersViewModel(interactor: GameZverobukvyViewModelImpl): GameZverobukvyViewModel
}