package ru.dev4rev.kids.zoobukvy.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val context: Context) {

    @Singleton
    @Provides
    fun appContext() = context
}