package ru.gb.zverobukvy.domain.use_case

import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards

class LevelCalculatorImpl(players: List<Player>, typesCards: List<TypeCards>) :
    LevelCalculator {

    private val mostDifficultTypeCards = extractMostDifficultTypeCards(typesCards)

    private val humanPlayersWithLevelInGame = HashMap<Player.HumanPlayer, Pair<Int, Int>>().apply {
        extractHumanPlayers(players).forEach {
            put(it, 0 to 0)
        }
    }

    override fun updateLettersGuessingLevel(player: Player, isCorrectStep: Boolean) {
        if (player is Player.HumanPlayer)
            humanPlayersWithLevelInGame[player]?.let {
                humanPlayersWithLevelInGame[player] = calculateLevelInGame(it, isCorrectStep)
            }
    }

    override fun getPlayersWithActualLevel(): List<Player.HumanPlayer> {
        when (mostDifficultTypeCards) {
            TypeCards.ORANGE -> humanPlayersWithLevelInGame.forEach {
                it.key.lettersGuessingLevel.orangeLevel =
                    calculateGeneralLevel(it.value, it.key.lettersGuessingLevel.orangeLevel)
            }

            TypeCards.GREEN -> humanPlayersWithLevelInGame.forEach {
                it.key.lettersGuessingLevel.greenLevel =
                    calculateGeneralLevel(it.value, it.key.lettersGuessingLevel.greenLevel)
            }

            TypeCards.BLUE -> humanPlayersWithLevelInGame.forEach {
                it.key.lettersGuessingLevel.blueLevel =
                    calculateGeneralLevel(it.value, it.key.lettersGuessingLevel.blueLevel)
            }

            TypeCards.VIOLET -> humanPlayersWithLevelInGame.forEach {
                it.key.lettersGuessingLevel.violetLevel =
                    calculateGeneralLevel(it.value, it.key.lettersGuessingLevel.violetLevel)
            }
        }
        return humanPlayersWithLevelInGame.keys.toList()
    }


    private fun calculateLevelInGame(
        currentLevelInGame: Pair<Int, Int>,
        isCorrectStep: Boolean
    ): Pair<Int, Int> =
        Pair(
            if (isCorrectStep)
                currentLevelInGame.first + 1
            else
                currentLevelInGame.first, currentLevelInGame.second + 1
        )

    private fun calculateGeneralLevel(
        levelInGame: Pair<Int, Int>,
        generalLevel: Float
    ): Float =
        (1 - LEVEL_RATIO) * generalLevel + LEVEL_RATIO * (levelInGame.first) / levelInGame.second

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
                if (it is Player.HumanPlayer)
                    add(it)
            }
        }

    companion object {
        private const val LEVEL_RATIO = 0.2F
    }
}