package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForUserFeedback
import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForSoundStatus
import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForMainMenu
import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesImpl
import javax.inject.Singleton

@Module
interface SharedPreferencesModule {

    @Singleton
    @Binds
    fun bindSharedPreferencesForGame(preferences: SharedPreferencesImpl): SharedPreferencesForSoundStatus

    @Singleton
    @Binds
    fun bindSharedPreferencesForMainMenu(preferences: SharedPreferencesImpl): SharedPreferencesForMainMenu

    @Singleton
    @Binds
    fun bindSharedPreferencesForFeedback(preferences: SharedPreferencesImpl): SharedPreferencesForUserFeedback
}