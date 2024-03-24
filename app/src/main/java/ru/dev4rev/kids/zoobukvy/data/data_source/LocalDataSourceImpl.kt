package ru.dev4rev.kids.zoobukvy.data.data_source

import ru.dev4rev.kids.zoobukvy.data.room.AnimalLettersDatabase
import ru.dev4rev.kids.zoobukvy.data.room.CardsDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.best_time.BestTimeInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.CardsSetInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.LetterCardInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.WordCardInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.AvatarInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.PlayerInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.PlayerWithAvatar
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    animalLettersDatabase: AnimalLettersDatabase,
    private val cardsDatabase: CardsDatabase
) : LocalDataSource {
    private val playersDao = animalLettersDatabase.playersDao()

    private val avatarsDao = animalLettersDatabase.avatarsDao()

    private val bestTimeDao = animalLettersDatabase.bestTimeDao()

    override suspend fun getPlayers(): List<PlayerWithAvatar> = playersDao.getPlayers()
    override suspend fun getPlayersNameById(playersId: Long): String? =
        playersDao.getPlayersNameById(playersId)

    override suspend fun deletePlayer(player: PlayerInDatabase) {
        playersDao.deletePlayer(player)
    }

    override suspend fun insertPlayer(player: PlayerInDatabase): Long =
        playersDao.insertPlayer(player)

    override suspend fun updatePlayer(player: PlayerInDatabase) {
        playersDao.run {
            val oldPlayersName = getPlayersNameById(player.idPlayer)
            if(oldPlayersName!=player.name && oldPlayersName!=null)
                bestTimeDao.updatePlayersNameById(oldPlayersName, player.name)
            updatePlayer(player)
        }
    }

    override suspend fun getLetterCards(): List<LetterCardInDatabase> =
        cardsDatabase.letterCardsDao().getLetterCards()

    override suspend fun getWordCards(): List<WordCardInDatabase> =
        cardsDatabase.wordCardsDao().getWordCards()

    override suspend fun getAvatars(): List<AvatarInDatabase> =
        avatarsDao.getAvatars()

    override suspend fun insertAvatar(avatar: AvatarInDatabase): Long =
       avatarsDao.insertAvatar(avatar)

    override suspend fun getCardsSetByColor(color: String): List<CardsSetInDatabase> =
        cardsDatabase.cardsSetDao().getCardsSetByColor(color)

    override suspend fun getBestTimeById(typesCardsId: Int): BestTimeInDatabase? =
        bestTimeDao.getBestTimeById(typesCardsId)

    override suspend fun insertBestTime(bestTime: BestTimeInDatabase) {
        bestTimeDao.insertBestTime(bestTime)
    }
}