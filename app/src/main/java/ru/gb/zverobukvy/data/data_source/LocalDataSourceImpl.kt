package ru.gb.zverobukvy.data.data_source

import ru.gb.zverobukvy.data.room.AnimalLettersDatabase
import ru.gb.zverobukvy.data.room.entity.card.CardsSetInDatabase
import ru.gb.zverobukvy.data.room.entity.card.LetterCardInDatabase
import ru.gb.zverobukvy.data.room.entity.card.WordCardInDatabase
import ru.gb.zverobukvy.data.room.entity.player.AvatarInDatabase
import ru.gb.zverobukvy.data.room.entity.player.PlayerInDatabase
import ru.gb.zverobukvy.data.room.entity.player.PlayerWithAvatar
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

    override suspend fun getCardsSetByColor(color: String): List<CardsSetInDatabase> =
        animalLettersDatabase.cardsSetDao().getCardsSetByColor(color)
}