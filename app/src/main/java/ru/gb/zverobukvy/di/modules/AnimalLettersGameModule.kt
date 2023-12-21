package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.repository_impl.ChangeRatingRepositoryImpl
import ru.gb.zverobukvy.di.AnimalLettersGameScope
import ru.gb.zverobukvy.domain.repository.ChangeRatingRepository
import ru.gb.zverobukvy.domain.use_case.interactor.AnimalLettersGameInteractor
import ru.gb.zverobukvy.domain.use_case.interactor.AnimalLettersGameInteractorImpl
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameViewModel
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameViewModelImpl
import ru.gb.zverobukvy.presentation.awards_screen.ChangeRatingRepositoryFakeImpl

@Module(
    includes = [
        GameStopwatchModule::class,
        AwardsScreenModule::class,
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