package ru.gb.zverobukvy.data.data_source

import ru.gb.zverobukvy.data.room.AnimalLettersDatabase
import ru.gb.zverobukvy.data.room.entity.AvatarInDatabase
import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.data.room.entity.PlayerInDatabase
import ru.gb.zverobukvy.data.room.entity.PlayerWithAvatar
import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val animalLettersDatabase: AnimalLettersDatabase,
) : LocalDataSource {
    private val playersDao = animalLettersDatabase.playersDao()

    private val avatarsDao = animalLettersDatabase.avatarsDao()

    override suspend fun getPlayers(): List<PlayerWithAvatar> = playersDao.getPlayers()

    override suspend fun deletePlayer(player: PlayerInDatabase) {
        playersDao.deletePlayer(player)
    }

    override suspend fun insertPlayer(player: PlayerInDatabase): Long =
        playersDao.insertPlayer(player)

    override suspend fun updatePlayer(player: PlayerInDatabase) {
        playersDao.updatePlayer(player)
    }

    override suspend fun getLetterCards(): List<LetterCardInDatabase> =
        animalLettersDatabase.letterCardsDao().getLetterCards()

    override suspend fun getWordCards(): List<WordCardInDatabase> =
        animalLettersDatabase.wordCardsDao().getWordCards()

    override suspend fun getAvatars(): List<AvatarInDatabase> =
        avatarsDao.getAvatars()

    override suspend fun insertAvatar(avatar: AvatarInDatabase): Long =
       avatarsDao.insertAvatar(avatar)
}