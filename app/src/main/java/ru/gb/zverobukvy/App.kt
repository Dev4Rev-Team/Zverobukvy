package ru.gb.zverobukvy

import android.app.Application
import android.content.Context
import ru.gb.zverobukvy.di.AppComponent
import ru.gb.zverobukvy.di.DaggerAppComponent
import ru.gb.zverobukvy.di.modules.ContextModule
import timber.log.Timber

class App : Application() {
    lateinit var appComponent: AppComponent


    override fun onCreate() {
        appComponent = DaggerAppComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()

        super.onCreate()
        // устанавливаем DebugTree() для Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}

val Context.appComponent: AppComponent
    get() = if (this is App) appComponent
    else this.applicationContext.appComponent
