package ru.gb.zverobukvy.data.resources_provider

import android.content.Context

class ResourcesProvider(context: Context) {
    private val applicationContext: Context

    init {
        applicationContext = context.applicationContext
    }

    fun getString(string: StringEnum):String{
        return applicationContext.resources.getString(string.id)
    }
}