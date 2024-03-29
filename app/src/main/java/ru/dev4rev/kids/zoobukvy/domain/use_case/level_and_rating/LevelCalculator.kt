package ru.dev4rev.kids.zoobukvy.domain.use_case.level_and_rating

import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player

interface LevelCalculator {

    fun updateLettersGuessingLevel(player: Player.HumanPlayer, isCorrectStep: Boolean)

    fun getUpdatedPlayers(): List<Player.HumanPlayer>
}