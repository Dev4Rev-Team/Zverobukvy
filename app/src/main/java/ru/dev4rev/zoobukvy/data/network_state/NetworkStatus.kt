package ru.dev4rev.zoobukvy.data.network_state

import kotlinx.coroutines.flow.StateFlow

interface NetworkStatus {

    fun registerNetworkCallback():StateFlow<Boolean>

    fun unregisterNetworkCallback()

}