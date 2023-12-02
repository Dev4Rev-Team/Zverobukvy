package ru.gb.zverobukvy.presentation.awards_screen

import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.entity.player.Rating
import ru.gb.zverobukvy.domain.repository.ChangeRatingRepository
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
                    340,
                    98,
                )
            )
        )
    }

    override fun setPlayersBeforeGame(players: List<Player.HumanPlayer>) {

    }

    override fun setPlayersAfterGame(players: List<Player.HumanPlayer>) {

    }
}