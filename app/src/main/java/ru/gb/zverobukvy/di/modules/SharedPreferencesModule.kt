package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.preferences.SharedPreferencesForGame
import ru.gb.zverobukvy.data.preferences.SharedPreferencesForGameImpl

@Module
interface SharedPreferencesModule {

    @Binds
    fun bindSharedPreferencesForGame(preferences: SharedPreferencesForGameImpl): SharedPreferencesForGame
}