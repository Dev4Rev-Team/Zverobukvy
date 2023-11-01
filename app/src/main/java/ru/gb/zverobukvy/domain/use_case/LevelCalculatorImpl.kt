package ru.gb.zverobukvy.domain.use_case

import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards

class LevelCalculatorImpl(players: List<Player>, typesCards: List<TypeCards>) :
    LevelCalculator {

    private val mostDifficultTypeCards = extractMostDifficultTypeCards(typesCards)

    private val humanPlayer = extractHumanPlayers(players)

    override fun updateLettersGuessingLevel(idPlayer: Long, isCorrectStep: Boolean) {
        humanPlayer.find { it.id == idPlayer }?.let {
                when (mostDifficultTypeCards) {
                    TypeCards.ORANGE -> it.lettersGuessingLevel.orangeLevel =
                        calculateLevel(it.lettersGuessingLevel.orangeLevel, isCorrectStep)

                    TypeCards.GREEN -> it.lettersGuessingLevel.greenLevel =
                        calculateLevel(it.lettersGuessingLevel.greenLevel, isCorrectStep)

                    TypeCards.BLUE -> it.lettersGuessingLevel.blueLevel =
                        calculateLevel(it.lettersGuessingLevel.blueLevel, isCorrectStep)

                    TypeCards.VIOLET -> it.lettersGuessingLevel.violetLevel =
                        calculateLevel(it.lettersGuessingLevel.violetLevel, isCorrectStep)
                }
        }
    }

    override fun getPlayersWithActualLevel(): List<Player.HumanPlayer> = humanPlayer

    private fun calculateLevel(
        currentLevel: Pair<Int, Int>,
        isCorrectStep: Boolean
    ): Pair<Int, Int> =
        Pair(
            if (isCorrectStep)
                currentLevel.first + 1
            else
                currentLevel.first, currentLevel.second + 1
        )

    private fun extractMostDifficultTypeCards(typesCards: List<TypeCards>) =
        if (typesCards.contains(TypeCards.VIOLET))
            TypeCards.VIOLET
        else if (typesCards.contains(TypeCards.BLUE))
            TypeCards.BLUE
        else if (typesCards.contains(TypeCards.GREEN))
            TypeCards.GREEN
        else
            TypeCards.ORANGE

    private fun extractHumanPlayers(players: List<Player>): List<Player.HumanPlayer> =
        mutableListOf<Player.HumanPlayer>().apply {
            players.forEach {
                if(it is Player.HumanPlayer)
                    add(it)
            }
        }
}