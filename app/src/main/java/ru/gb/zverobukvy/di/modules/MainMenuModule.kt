package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.presentation.main_menu.viewModel.SettingsScreenViewModel
import ru.gb.zverobukvy.presentation.main_menu.viewModel.SettingsScreenViewModelImpl
import javax.inject.Singleton

@Module(includes = [ResourcesProviderModule::class, RepositoryModule::class])
interface MainMenuModule {

    @Binds
    @Singleton
    fun bindSettingsScreenViewModel(viewModel: SettingsScreenViewModelImpl): SettingsScreenViewModel
}