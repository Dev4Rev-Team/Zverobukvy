package ru.gb.zverobukvy.data.repository_impl

import ru.gb.zverobukvy.domain.entity.player.LettersGuessingLevel
import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.entity.player.Rating
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
        players.forEach {
            // игроков с состоянием до начала игры сохраняем отдельными объектами,
            // чтобы их состояния не изменялись в процессе игры
            playersBeforeGame.add(
                it.copy(
                    rating = Rating(
                        it.rating.orangeRating,
                        it.rating.greenRating,
                        it.rating.blueRating,
                        it.rating.violetRating
                    ),
                    lettersGuessingLevel = LettersGuessingLevel(
                        it.lettersGuessingLevel.orangeLevel,
                        it.lettersGuessingLevel.greenLevel,
                        it.lettersGuessingLevel.blueLevel,
                        it.lettersGuessingLevel.violetLevel
                    )
                )
            )
        }
    }

    override fun setPlayersAfterGame(players: List<Player.HumanPlayer>) {
        playersAfterGame.addAll(players)
    }
}