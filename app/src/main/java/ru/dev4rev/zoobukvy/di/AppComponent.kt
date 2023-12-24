package ru.dev4rev.zoobukvy.di

import dagger.Component
import ru.dev4rev.zoobukvy.data.view_rating_provider.ViewRatingProviderFactory
import ru.dev4rev.zoobukvy.di.modules.AppModule
import ru.dev4rev.zoobukvy.di.modules.ContextModule
import ru.dev4rev.zoobukvy.presentation.LoadingDataViewModelImpl
import ru.dev4rev.zoobukvy.presentation.main_menu.MainMenuViewModelImpl
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ContextModule::class])
interface AppComponent {

    val settingsScreenViewModel: MainMenuViewModelImpl

    val loadingDataViewModel: LoadingDataViewModelImpl

    fun getAnimalLettersGameSubcomponentFactory(): AnimalLettersGameSubcomponent.Factory

    fun getViewRatingProviderFactory(): ViewRatingProviderFactory
}