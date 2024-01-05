package ru.dev4rev.kids.zoobukvy.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.dev4rev.kids.zoobukvy.data.room.AnimalLettersDatabase
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideAnimalLettersDatabase(context: Context): AnimalLettersDatabase {
        AnimalLettersDatabase.createInstanceDatabase(context)
        return AnimalLettersDatabase.getPlayersDatabase()
    }
}