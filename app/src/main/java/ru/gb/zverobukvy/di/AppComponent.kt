package ru.gb.zverobukvy.di

import dagger.Component
import ru.gb.zverobukvy.data.resources_provider.AssertsImageCashImpl
import ru.gb.zverobukvy.di.modules.AppModule
import ru.gb.zverobukvy.di.modules.ContextModule
import ru.gb.zverobukvy.presentation.main_menu.viewModel.SettingsScreenViewModelImpl
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ContextModule::class])
interface AppComponent {

    fun getAssetsImageCash(): AssertsImageCashImpl

    fun getSettingsScreenViewModel(): SettingsScreenViewModelImpl

    fun getAnimalLettersGameSubcomponentFactory(): AnimalLettersGameSubcomponent.Factory
}