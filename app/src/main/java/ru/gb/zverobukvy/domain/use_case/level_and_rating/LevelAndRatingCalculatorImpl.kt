package ru.gb.zverobukvy.domain.use_case.level_and_rating

import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.domain.entity.card.WordCard

class LevelAndRatingCalculatorImpl(players: List<Player.HumanPlayer>, typesCards: List<TypeCards>) :
    LevelCalculator, RatingCalculator {

    private val mostDifficultTypeCards = extractMostDifficultTypeCards(typesCards.toSet())

    private val humanPlayersWithLevelInGame = HashMap<Player.HumanPlayer, Pair<Int, Int>>().apply {
        players.forEach {
            put(it, 0 to 0)
        }
    }

    override fun updateLettersGuessingLevel(player: Player.HumanPlayer, isCorrectStep: Boolean) {
            humanPlayersWithLevelInGame[player]?.let {
                humanPlayersWithLevelInGame[player] = calculateLevelInGame(it, isCorrectStep)
            }
    }

    override fun updateRating(player: Player.HumanPlayer, guessedWordCard: WordCard) {
            humanPlayersWithLevelInGame.keys.find {it == player }?.let {
                when(extractMostDifficultTypeCards(guessedWordCard.typesCards)){
                    TypeCards.ORANGE -> it.rating.orangeRating++
                    TypeCards.GREEN -> it.rating.greenRating++
                    TypeCards.BLUE -> it.rating.blueRating++
                    TypeCards.VIOLET -> it.rating.violetRating++
                }
            }
    }

    override fun getUpdatedPlayers(): List<Player.HumanPlayer> {
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

    private fun extractMostDifficultTypeCards(typesCards: Set<TypeCards>) =
        if (typesCards.contains(TypeCards.VIOLET))
            TypeCards.VIOLET
        else if (typesCards.contains(TypeCards.BLUE))
            TypeCards.BLUE
        else if (typesCards.contains(TypeCards.GREEN))
            TypeCards.GREEN
        else
            TypeCards.ORANGE

    companion object {
        private const val LEVEL_RATIO = 0.2F
    }
}