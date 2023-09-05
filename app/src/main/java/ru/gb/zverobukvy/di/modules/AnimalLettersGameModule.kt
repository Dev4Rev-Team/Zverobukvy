package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.di.AnimalLettersGameScope
import ru.gb.zverobukvy.domain.use_case.AnimalLettersGameInteractor
import ru.gb.zverobukvy.domain.use_case.AnimalLettersGameInteractorImpl
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameViewModel
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameViewModelImpl

@Module(
    includes = [
        GameStopwatchModule::class,
        RepositoryModule::class,
        SoundEffectModule::class,
        AssetImageCashModule::class,
        ResourcesProviderModule::class,
    ]
)
interface AnimalLettersGameModule {

    @Binds
    @AnimalLettersGameScope
    fun bindAnimalLettersInteractor(interactor: AnimalLettersGameInteractorImpl): AnimalLettersGameInteractor

    @Binds
    @AnimalLettersGameScope
    fun bindAnimalLettersViewModel(interactor: AnimalLettersGameViewModelImpl): AnimalLettersGameViewModel
}