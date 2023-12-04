package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.di.AnimalLettersGameSubcomponent
import ru.gb.zverobukvy.presentation.LoadingDataViewModel
import ru.gb.zverobukvy.presentation.LoadingDataViewModelImpl
import ru.gb.zverobukvy.presentation.main_menu.MainMenuViewModel
import ru.gb.zverobukvy.presentation.main_menu.MainMenuViewModelImpl

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