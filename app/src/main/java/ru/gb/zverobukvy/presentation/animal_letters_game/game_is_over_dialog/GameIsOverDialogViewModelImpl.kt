package ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog

import androidx.lifecycle.ViewModel
import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.repository.ChangeRatingRepository

class GameIsOverDialogViewModelImpl(private val changeRatingRepository: ChangeRatingRepository) :
    ViewModel(),
    GameIsOverDialogViewModel {
    override fun getPlayersBeforeGame(): List<Player.HumanPlayer> {
        return changeRatingRepository.getPlayersBeforeGame()
    }

    override fun getPlayersAfterGame(): List<Player.HumanPlayer> {
        return changeRatingRepository.getPlayersAfterGame()
    }
}