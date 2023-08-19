package ru.gb.zverobukvy.data.data_source

import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase
import ru.gb.zverobukvy.domain.entity.Player

interface LocalDataSource {
    suspend fun getPlayers(): List<Player>

    suspend fun deletePlayer(player: Player)

    suspend fun insertPlayer(player: Player)

    suspend fun updatePlayer(player: Player)

    suspend fun getLetterCards(): List<LetterCardInDatabase>

    suspend fun getWordCards(): List<WordCardInDatabase>
}