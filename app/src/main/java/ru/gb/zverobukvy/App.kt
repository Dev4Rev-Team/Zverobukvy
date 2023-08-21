package ru.gb.zverobukvy

import android.app.Application
import ru.gb.zverobukvy.data.data_source_impl.LocalDataSourceImpl
import ru.gb.zverobukvy.data.preferences.SharedPreferencesForGameImpl
import ru.gb.zverobukvy.data.repository_impl.AnimalLettersRepositoryImpl
import ru.gb.zverobukvy.data.room.AnimalLettersDatabase
import timber.log.Timber

class App : Application() {
    lateinit var animalLettersRepository:  AnimalLettersRepositoryImpl

    override fun onCreate() {
        super.onCreate()
        // устанавливаем DebugTree() для Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // создаем AnimalLettersDatabase
        AnimalLettersDatabase.createInstanceDatabase(this)
        // создаем репозиторий
        animalLettersRepository = AnimalLettersRepositoryImpl(
            LocalDataSourceImpl(AnimalLettersDatabase.getPlayersDatabase()),
            SharedPreferencesForGameImpl(this)
        )
    }

}