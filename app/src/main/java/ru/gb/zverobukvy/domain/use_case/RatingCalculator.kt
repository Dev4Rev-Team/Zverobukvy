package ru.gb.zverobukvy.domain.use_case

import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.WordCard

interface RatingCalculator {
    fun updateRating(player: Player, guessedWordCard: WordCard)

    fun getUpdatedPlayers(): List<Player.HumanPlayer>
}