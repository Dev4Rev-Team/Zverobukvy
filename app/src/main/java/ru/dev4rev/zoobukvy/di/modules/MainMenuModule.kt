package ru.dev4rev.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.zoobukvy.di.AnimalLettersGameSubcomponent
import ru.dev4rev.zoobukvy.presentation.LoadingDataViewModel
import ru.dev4rev.zoobukvy.presentation.LoadingDataViewModelImpl
import ru.dev4rev.zoobukvy.presentation.main_menu.MainMenuViewModel
import ru.dev4rev.zoobukvy.presentation.main_menu.MainMenuViewModelImpl

@Module(
    subcomponents = [AnimalLettersGameSubcomponent::class],
    includes = [
        ResourcesProviderModule::class,
        RepositoryModule::class
    ]
)
interface MainMenuModule {

    @Binds
    fun bindSettingsScreenViewModel(viewModel: MainMenuViewModelImpl): MainMenuViewModel

    @Binds
    fun bindMainViewModel(viewModel: LoadingDataViewModelImpl): LoadingDataViewModel
}