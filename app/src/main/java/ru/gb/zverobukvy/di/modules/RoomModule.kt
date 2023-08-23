package ru.gb.zverobukvy.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.gb.zverobukvy.data.room.AnimalLettersDatabase

@Module
class RoomModule {

    @Provides
    fun provideAnimalLettersDatabase(context: Context): AnimalLettersDatabase {
        AnimalLettersDatabase.createInstanceDatabase(context)
        return AnimalLettersDatabase.getPlayersDatabase()
    }
}