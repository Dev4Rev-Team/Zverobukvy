package ru.dev4rev.zoobukvy.data.resources_provider

import android.content.Context
import javax.inject.Inject

class ResourcesProviderImpl @Inject constructor(context: Context) : ResourcesProvider {
    private val applicationContext: Context

    init {
        applicationContext = context.applicationContext
    }

    override fun getString(string: StringEnum): String {
        return applicationContext.resources.getString(string.id)
    }
}