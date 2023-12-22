package ru.gb.zverobukvy

import android.app.Application
import android.content.Context
import ru.gb.zverobukvy.di.AnimalLettersGameSubcomponent
import ru.gb.zverobukvy.di.AppComponent
import ru.gb.zverobukvy.di.DaggerAppComponent
import ru.gb.zverobukvy.di.modules.ContextModule
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.domain.entity.player.PlayerInGame
import timber.log.Timber

class App : Application(), AnimalLettersGameSubcomponentContainer {
    lateinit var appComponent: AppComponent

    private var subcomponent: AnimalLettersGameSubcomponent? = null

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

    override fun createAnimalLettersGameSubcomponent(
        typesCards: List<TypeCards>,
        players: List<PlayerInGame>,
    ): AnimalLettersGameSubcomponent {
        return appComponent.getAnimalLettersGameSubcomponentFactory().create(
            typesCards, players
        ).also {
            subcomponent = it
        }
    }

    override fun getAnimalLettersGameSubcomponent(): AnimalLettersGameSubcomponent {
        return if (subcomponent != null) subcomponent as AnimalLettersGameSubcomponent
        else throw IllegalStateException("AnimalLettersGameSubcomponent не инициализирован")
    }

    override fun deleteAnimalLettersGameSubcomponent() {
        subcomponent = null
    }

}

val Context.appComponent: AppComponent
    get() = if (this is App) appComponent
    else this.applicationContext.appComponent

val Context.animalLettersGameSubcomponentContainer: AnimalLettersGameSubcomponentContainer
    get() = if (this is App) this
    else this.applicationContext.animalLettersGameSubcomponentContainer

interface AnimalLettersGameSubcomponentContainer {

    fun createAnimalLettersGameSubcomponent(
        typesCards: List<TypeCards>,
        players: List<PlayerInGame>,
    ): AnimalLettersGameSubcomponent

    fun getAnimalLettersGameSubcomponent(): AnimalLettersGameSubcomponent

    fun deleteAnimalLettersGameSubcomponent()
}
