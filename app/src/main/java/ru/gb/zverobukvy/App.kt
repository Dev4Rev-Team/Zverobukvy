package ru.gb.zverobukvy

import android.app.Application
import ru.gb.zverobukvy.data.data_source_impl.LocalDataSourceImpl

import ru.gb.zverobukvy.data.resources_provider.AssertsImageCashImpl
import ru.gb.zverobukvy.data.preferences.SharedPreferencesForGameImpl
import ru.gb.zverobukvy.data.repository_impl.AnimalLettersRepositoryImpl
import ru.gb.zverobukvy.data.room.AnimalLettersDatabase
import ru.gb.zverobukvy.domain.repository.AnimalLettersGameRepository
import ru.gb.zverobukvy.domain.repository.MainMenuRepository

import ru.gb.zverobukvy.presentation.customview.AssetsImageCash
import timber.log.Timber

class App : Application() {
    lateinit var animalLettersCardsRepository: AnimalLettersGameRepository
    lateinit var mainMenuRepository: MainMenuRepository
    lateinit var assetsImageCash: AssetsImageCash

    override fun onCreate() {
        super.onCreate()
        // устанавливаем DebugTree() для Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // создаем AnimalLettersDatabase
        AnimalLettersDatabase.createInstanceDatabase(this)
        // создаем репозиторий
        animalLettersCardsRepository = AnimalLettersRepositoryImpl(
            LocalDataSourceImpl(AnimalLettersDatabase.getPlayersDatabase()),
            SharedPreferencesForGameImpl(this)
        )
        mainMenuRepository = animalLettersCardsRepository as MainMenuRepository
        assetsImageCash = AssertsImageCashImpl(this, animalLettersCardsRepository)
    }

}