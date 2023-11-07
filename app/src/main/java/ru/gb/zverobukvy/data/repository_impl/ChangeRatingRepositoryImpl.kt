package ru.gb.zverobukvy.data.repository_impl

import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.repository.ChangeRatingRepository
import javax.inject.Inject

class ChangeRatingRepositoryImpl @Inject constructor() : ChangeRatingRepository {

    private val playersBeforeGame: MutableList<Player.HumanPlayer> = mutableListOf()

    private val playersAfterGame: MutableList<Player.HumanPlayer> = mutableListOf()

    override fun getPlayersBeforeGame(): List<Player.HumanPlayer> =
        playersBeforeGame

    override fun getPlayersAfterGame(): List<Player.HumanPlayer> =
        playersAfterGame

    override fun setPlayersBeforeGame(players: List<Player.HumanPlayer>) {
        playersBeforeGame.addAll(players)
    }

    override fun setPlayersAfterGame(players: List<Player.HumanPlayer>) {
        playersAfterGame.addAll(players)
    }
}