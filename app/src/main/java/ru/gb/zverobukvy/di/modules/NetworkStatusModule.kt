package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.network_state.NetworkStatus
import ru.gb.zverobukvy.data.network_state.NetworkStatusImpl

@Module
interface NetworkStatusModule {
    @Binds
    fun bindsNetworkStatusModule(networkStatus: NetworkStatusImpl): NetworkStatus
}