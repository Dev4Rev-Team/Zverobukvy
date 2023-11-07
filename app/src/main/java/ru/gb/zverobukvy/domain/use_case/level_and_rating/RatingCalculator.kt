package ru.gb.zverobukvy.domain.use_case.level_and_rating

import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.entity.card.WordCard

interface RatingCalculator {
    fun updateRating(player: Player.HumanPlayer, guessedWordCard: WordCard)

    fun getUpdatedPlayers(): List<Player.HumanPlayer>
}