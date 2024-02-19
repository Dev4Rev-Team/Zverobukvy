package ru.dev4rev.kids.zoobukvy.presentation.awards_screen

import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Rating
import ru.dev4rev.kids.zoobukvy.domain.repository.ChangeRatingRepository
import javax.inject.Inject

class ChangeRatingRepositoryFakeImpl @Inject constructor(): ChangeRatingRepository {
    override fun getPlayersBeforeGame(): List<Player.HumanPlayer> {
        return listOf(
            Player.HumanPlayer(
                "Олег",
                12,
                rating = Rating()
            ),
            Player.HumanPlayer(
                "Александр",
                16,
                rating = Rating(
                    90,
                    98,
                    12,
                    12,
                )
            ),
            Player.HumanPlayer(
                "Павел",
                19,
                rating = Rating(
                    96,
                    98,
                    12,
                    12,
                )
            )
        )
    }

    override fun getPlayersAfterGame(): List<Player.HumanPlayer> {
        return listOf(
            Player.HumanPlayer(
                "Олег",
                12,
                rating = Rating(
                    13,
                    45,
                    57,
                    88
                )
            ),
            Player.HumanPlayer(
                "Александр",
                16,
                rating = Rating(
                    140,
                    240,
                    12,
                    12,
                )
            ),
            Player.HumanPlayer(
                "Павел",
                19,
                rating = Rating(
                    102,
                    98,
                    12,
                    12,
                )
            )
        )
    }

    override fun setPlayersBeforeGame(players: List<Player.HumanPlayer>) {

    }

    override fun setPlayersAfterGame(players: List<Player.HumanPlayer>) {

    }
}