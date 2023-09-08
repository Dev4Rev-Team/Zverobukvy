package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.di.AnimalLettersGameSubcomponent
import ru.gb.zverobukvy.presentation.main_menu.MainMenuViewModel
import ru.gb.zverobukvy.presentation.main_menu.MainMenuViewModelImpl
import javax.inject.Singleton

@Module(
    subcomponents = [AnimalLettersGameSubcomponent::class],
    includes = [
        ResourcesProviderModule::class,
        RepositoryModule::class
    ]
)
interface MainMenuModule {

    @Binds
    @Singleton
    fun bindSettingsScreenViewModel(viewModel: MainMenuViewModelImpl): MainMenuViewModel
}