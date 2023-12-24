package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForGame
import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForGameImpl
import javax.inject.Singleton

@Module
interface SharedPreferencesModule {

    @Singleton
    @Binds
    fun bindSharedPreferencesForGame(preferences: SharedPreferencesForGameImpl): SharedPreferencesForGame
}