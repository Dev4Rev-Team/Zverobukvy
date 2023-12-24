package ru.dev4rev.kids.zoobukvy.di

import dagger.Component
import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.ViewRatingProviderFactory
import ru.dev4rev.kids.zoobukvy.di.modules.AppModule
import ru.dev4rev.kids.zoobukvy.di.modules.ContextModule
import ru.dev4rev.kids.zoobukvy.presentation.LoadingDataViewModelImpl
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.MainMenuViewModelImpl
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ContextModule::class])
interface AppComponent {

    val settingsScreenViewModel: MainMenuViewModelImpl

    val loadingDataViewModel: LoadingDataViewModelImpl

    fun getAnimalLettersGameSubcomponentFactory(): AnimalLettersGameSubcomponent.Factory

    fun getViewRatingProviderFactory(): ViewRatingProviderFactory
}