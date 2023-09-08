package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.resources_provider.ResourcesProvider
import ru.gb.zverobukvy.data.resources_provider.ResourcesProviderImpl
import javax.inject.Singleton

@Module
interface ResourcesProviderModule {

    @Singleton
    @Binds
    fun bindResourcesProvider(provider: ResourcesProviderImpl): ResourcesProvider
}