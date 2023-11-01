package ru.gb.zverobukvy.data.data_source

import ru.gb.zverobukvy.data.room.entity.AvatarInDatabase
import ru.gb.zverobukvy.data.room.entity.CardsSetInDatabase
import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.data.room.entity.PlayerInDatabase
import ru.gb.zverobukvy.data.room.entity.PlayerWithAvatar
import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase

interface LocalDataSource {
    suspend fun getPlayers(): List<PlayerWithAvatar>

    suspend fun deletePlayer(player: PlayerInDatabase)

    suspend fun insertPlayer(player: PlayerInDatabase): Long

    suspend fun updatePlayer(player: PlayerInDatabase)

    suspend fun getLetterCards(): List<LetterCardInDatabase>

    suspend fun getWordCards(): List<WordCardInDatabase>

    suspend fun getAvatars(): List<AvatarInDatabase>

    suspend fun insertAvatar(avatar: AvatarInDatabase): Long

    suspend fun getCardsSetByColor(color: String): List<CardsSetInDatabase>
}