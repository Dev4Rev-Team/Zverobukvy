package ru.gb.zverobukvy.domain.repository

import ru.gb.zverobukvy.domain.entity.player.Player

interface ChangeRatingRepository {
    fun getPlayersBeforeGame(): List<Player.HumanPlayer>

    fun getPlayersAfterGame(): List<Player.HumanPlayer>

    fun setPlayersBeforeGame(players: List<Player.HumanPlayer>)

    fun setPlayersAfterGame(players: List<Player.HumanPlayer>)
}