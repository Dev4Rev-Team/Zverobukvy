package ru.gb.zverobukvy

import android.app.Application
import android.content.Context
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import ru.gb.zverobukvy.di.AppComponent
import ru.gb.zverobukvy.di.DaggerAppComponent
import ru.gb.zverobukvy.di.modules.ContextModule
import timber.log.Timber
import javax.inject.Inject


class App : Application(), HasAndroidInjector {
    lateinit var appComponent: AppComponent

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>


    override fun onCreate() {
        appComponent = DaggerAppComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
            .also { appComponent -> appComponent.inject(this) }

        super.onCreate()
        // устанавливаем DebugTree() для Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

}

val Context.appComponent: AppComponent
    get() = if (this is App) appComponent
    else this.applicationContext.appComponent

val Context.injector: AndroidInjector<Any>
    get() = if (this is App) dispatchingAndroidInjector
    else this.applicationContext.injector
