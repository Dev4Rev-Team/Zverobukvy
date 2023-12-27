package ru.dev4rev.kids.zoobukvy.data.data_source

import ru.dev4rev.kids.zoobukvy.data.room.entity.player.AvatarInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.CardsSetInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.LetterCardInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.PlayerInDatabase
import ru.dev4rev.kids.zoobukvy.data.room.entity.player.PlayerWithAvatar
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.WordCardInDatabase

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