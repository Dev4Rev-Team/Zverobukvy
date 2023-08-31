package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.resources_provider.ResourcesProvider
import ru.gb.zverobukvy.data.resources_provider.ResourcesProviderImpl

@Module
interface ResourcesProviderModule {

    @Binds
    fun bindResourcesProvider(provider: ResourcesProviderImpl): ResourcesProvider
}