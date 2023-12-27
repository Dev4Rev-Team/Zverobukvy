package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.network_state.NetworkStatus
import ru.dev4rev.kids.zoobukvy.data.network_state.NetworkStatusImpl
import javax.inject.Singleton

@Module
interface NetworkStatusModule {

    @Singleton
    @Binds
    fun bindsNetworkStatusModule(networkStatus: NetworkStatusImpl): NetworkStatus
}