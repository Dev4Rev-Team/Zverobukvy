package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.di.AnimalLettersGameSubcomponent
import ru.dev4rev.kids.zoobukvy.presentation.LoadingDataViewModel
import ru.dev4rev.kids.zoobukvy.presentation.LoadingDataViewModelImpl
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.MainMenuViewModel
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.MainMenuViewModelImpl

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