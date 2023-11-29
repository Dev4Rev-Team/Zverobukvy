package ru.gb.zverobukvy.di

import dagger.Component
import ru.gb.zverobukvy.data.view_rating_provider.ViewRatingProviderFactory
import ru.gb.zverobukvy.di.modules.AppModule
import ru.gb.zverobukvy.di.modules.ContextModule
import ru.gb.zverobukvy.presentation.LoadingDataViewModelImpl
import ru.gb.zverobukvy.presentation.main_menu.MainMenuViewModelImpl
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ContextModule::class])
interface AppComponent {

    val settingsScreenViewModel: MainMenuViewModelImpl

    val loadingDataViewModel: LoadingDataViewModelImpl

    fun getAnimalLettersGameSubcomponentFactory(): AnimalLettersGameSubcomponent.Factory

    fun getViewRatingProviderFactory(): ViewRatingProviderFactory
}