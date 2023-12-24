package ru.dev4rev.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.zoobukvy.data.resources_provider.ResourcesProvider
import ru.dev4rev.zoobukvy.data.resources_provider.ResourcesProviderImpl
import javax.inject.Singleton

@Module
interface ResourcesProviderModule {

    @Singleton
    @Binds
    fun bindResourcesProvider(provider: ResourcesProviderImpl): ResourcesProvider
}