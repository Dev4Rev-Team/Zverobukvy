package ru.dev4rev.kids.zoobukvy.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.dev4rev.kids.zoobukvy.data.room.AnimalLettersDatabase
import ru.dev4rev.kids.zoobukvy.data.room.CardsDatabase
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideAnimalLettersDatabase(context: Context): AnimalLettersDatabase {
        AnimalLettersDatabase.createInstanceDatabase(context)
        return AnimalLettersDatabase.getPlayersDatabase()
    }

    @Singleton
    @Provides
    fun provideCardsDatabase(context: Context): CardsDatabase {
        CardsDatabase.createInstanceDatabase(context)
        return CardsDatabase.getCardsDatabase()
    }
}