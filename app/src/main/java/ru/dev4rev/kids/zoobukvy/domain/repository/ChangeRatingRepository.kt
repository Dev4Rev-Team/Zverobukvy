package ru.dev4rev.kids.zoobukvy.domain.repository

import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player

interface ChangeRatingRepository {
    fun getPlayersBeforeGame(): List<Player.HumanPlayer>

    fun getPlayersAfterGame(): List<Player.HumanPlayer>

    fun setPlayersBeforeGame(players: List<Player.HumanPlayer>)

    fun setPlayersAfterGame(players: List<Player.HumanPlayer>)
}