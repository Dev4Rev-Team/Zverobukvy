package ru.dev4rev.kids.zoobukvy

import android.app.Application
import android.content.Context
import ru.dev4rev.kids.zoobukvy.di.AnimalLettersGameSubcomponent
import ru.dev4rev.kids.zoobukvy.di.AppComponent
import ru.dev4rev.kids.zoobukvy.di.DaggerAppComponent
import ru.dev4rev.kids.zoobukvy.di.modules.ContextModule
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.kids.zoobukvy.domain.entity.player.PlayerInGame
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
