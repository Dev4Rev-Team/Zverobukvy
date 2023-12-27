package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.ViewRatingProviderFactory
import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.ViewRatingProviderFactoryImpl
import javax.inject.Singleton

@Module
interface ViewRatingProviderModule {

    @Singleton
    @Binds
    fun bindViewRatingProviderFactory(factory: ViewRatingProviderFactoryImpl): ViewRatingProviderFactory
}