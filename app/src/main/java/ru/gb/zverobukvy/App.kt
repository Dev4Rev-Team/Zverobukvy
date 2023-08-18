package ru.gb.zverobukvy

import android.app.Application
import ru.gb.zverobukvy.data.room.PlayersDatabase
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // устанавливаем DebugTree() для Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // создаем PlayersDatabase
        PlayersDatabase.createInstanceDatabase(this)
    }
}