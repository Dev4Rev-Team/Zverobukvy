package ru.dev4rev.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.zoobukvy.data.repository_impl.ChangeRatingRepositoryImpl
import ru.dev4rev.zoobukvy.di.AnimalLettersGameScope
import ru.dev4rev.zoobukvy.domain.repository.ChangeRatingRepository
import ru.dev4rev.zoobukvy.domain.use_case.interactor.AnimalLettersGameInteractor
import ru.dev4rev.zoobukvy.domain.use_case.interactor.AnimalLettersGameInteractorImpl
import ru.dev4rev.zoobukvy.presentation.animal_letters_game.AnimalLettersGameViewModel
import ru.dev4rev.zoobukvy.presentation.animal_letters_game.AnimalLettersGameViewModelImpl

@Module(
    includes = [
        GameStopwatchModule::class,
    ]
)
interface AnimalLettersGameModule {

    @Binds
    @AnimalLettersGameScope
    fun bindAnimalLettersInteractor(interactor: AnimalLettersGameInteractorImpl): AnimalLettersGameInteractor

    @Binds
    @AnimalLettersGameScope
    fun bindAnimalLettersViewModel(interactor: AnimalLettersGameViewModelImpl): AnimalLettersGameViewModel

    @Binds
    @AnimalLettersGameScope
    fun bindChangeRatingRepository(changeRatingRepository: ChangeRatingRepositoryImpl): ChangeRatingRepository
}