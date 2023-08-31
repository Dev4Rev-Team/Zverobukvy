package ru.gb.zverobukvy.data.data_source_impl

import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.room.AnimalLettersDatabase
import ru.gb.zverobukvy.data.room.entity.AvatarInDatabase
import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.data.room.entity.PlayerInDatabase
import ru.gb.zverobukvy.data.room.entity.PlayerWithAvatar
import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase
import ru.gb.zverobukvy.domain.entity.Player
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val playersDatabase: AnimalLettersDatabase,
) : LocalDataSource {
    private val playersDao = playersDatabase.playersDao()


    override suspend fun getPlayers(): List<PlayerWithAvatar> = playersDao.getPlayers()

    override suspend fun deletePlayer(player: PlayerInDatabase) {
        playersDao.deletePlayer(player)
    }

    override suspend fun insertPlayer(player: PlayerInDatabase) {
        playersDao.insertPlayer(player)
    }

    override suspend fun updatePlayer(player: PlayerInDatabase) {
        playersDao.updatePlayer(player)
    }

    override suspend fun getLetterCards(): List<LetterCardInDatabase> =
        animalLettersDatabase.letterCardsDao().getLetterCards()

    override suspend fun getWordCards(): List<WordCardInDatabase> =
        animalLettersDatabase.wordCardsDao().getWordCards()

    override suspend fun getAvatars(): List<AvatarInDatabase> =
        animalLettersDatabase.avatarsDao().getAvatars()

}