package ru.gb.zverobukvy.data.resources_provider

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesProviderImpl @Inject constructor(context: Context) : ResourcesProvider {
    private val applicationContext: Context

    init {
        applicationContext = context.applicationContext
    }

    override fun getString(string: StringEnum): String {
        return applicationContext.resources.getString(string.id)
    }
}