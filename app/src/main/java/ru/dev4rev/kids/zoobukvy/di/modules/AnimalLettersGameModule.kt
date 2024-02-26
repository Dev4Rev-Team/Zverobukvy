package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.repository_impl.ChangeRatingRepositoryImpl
import ru.dev4rev.kids.zoobukvy.di.AnimalLettersGameScope
import ru.dev4rev.kids.zoobukvy.domain.repository.ChangeRatingRepository
import ru.dev4rev.kids.zoobukvy.domain.use_case.interactor.AnimalLettersGameInteractor
import ru.dev4rev.kids.zoobukvy.domain.use_case.interactor.AnimalLettersGameInteractorImpl
import ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.AnimalLettersGameViewModel
import ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.AnimalLettersGameViewModelImpl
import ru.dev4rev.kids.zoobukvy.presentation.awards_screen.ChangeRatingRepositoryFakeImpl

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
    fun bindChangeRatingRepository(changeRatingRepository: ChangeRatingRepositoryFakeImpl): ChangeRatingRepository
}