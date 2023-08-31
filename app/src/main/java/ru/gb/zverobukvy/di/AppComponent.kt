package ru.gb.zverobukvy.di

import dagger.Component
import ru.gb.zverobukvy.App
import ru.gb.zverobukvy.di.modules.AppModule
import ru.gb.zverobukvy.di.modules.ContextModule
import ru.gb.zverobukvy.presentation.main_menu.MainMenuViewModelImpl
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ContextModule::class])
interface AppComponent {

    fun inject(app: App)

    fun getSettingsScreenViewModel(): MainMenuViewModelImpl

    fun getAnimalLettersGameSubcomponentFactory(): AnimalLettersGameSubcomponent.Factory
}