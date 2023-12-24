package ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.game_is_over_dialog

import androidx.lifecycle.ViewModel
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import ru.dev4rev.kids.zoobukvy.domain.repository.ChangeRatingRepository
import javax.inject.Inject

class GameIsOverDialogViewModelImpl @Inject constructor(private val changeRatingRepository: ChangeRatingRepository) :
    ViewModel(),
    GameIsOverDialogViewModel {
    override fun getPlayersBeforeGame(): List<Player.HumanPlayer> {
        return changeRatingRepository.getPlayersBeforeGame()
    }

    override fun getPlayersAfterGame(): List<Player.HumanPlayer> {
        return changeRatingRepository.getPlayersAfterGame()
    }
}