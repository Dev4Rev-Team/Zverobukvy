package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.view_rating_provider.ViewRatingProviderFactory
import ru.gb.zverobukvy.data.view_rating_provider.ViewRatingProviderFactoryImpl
import javax.inject.Singleton

@Module
interface ViewRatingProviderModule {

    @Singleton
    @Binds
    fun bindViewRatingProviderFactory(factory: ViewRatingProviderFactoryImpl): ViewRatingProviderFactory
}