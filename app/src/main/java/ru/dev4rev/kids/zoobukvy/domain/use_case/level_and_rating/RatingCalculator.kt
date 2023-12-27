package ru.dev4rev.kids.zoobukvy.domain.use_case.level_and_rating

import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import ru.dev4rev.kids.zoobukvy.domain.entity.card.WordCard

interface RatingCalculator {
    fun updateRating(player: Player.HumanPlayer, guessedWordCard: WordCard)

    fun getUpdatedPlayers(): List<Player.HumanPlayer>
}