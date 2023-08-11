package ru.gb.zverobukvy.presentation.main_menu.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.gb.zverobukvy.domain.app_state.SettingsScreenState
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.repository.PlayersRepository
import ru.gb.zverobukvy.presentation.SingleEventLiveData

class SettingsScreenViewModelImpl(private val playersRepository: PlayersRepository): SettingsScreenViewModel, ViewModel() {
    override fun onLaunch(
        typesCardsSelectedForGameFromPreference: List<TypeCards>,
        namesPlayersSelectedForGameFromPreference: List<String>
    ) {
        TODO("Not yet implemented")
    }

    override fun getLiveDataPlayersScreenState(): LiveData<SettingsScreenState.PlayersScreenState> {
        TODO("Not yet implemented")
    }

    override fun getLiveDataScreenState(): SingleEventLiveData<SettingsScreenState.ScreenState> {
        TODO("Not yet implemented")
    }

    override fun onChangedSelectingPlayer(positionPlayer: Int) {
        TODO("Not yet implemented")
    }

    override fun onRemovePlayer(positionPlayer: Int) {
        TODO("Not yet implemented")
    }

    override fun onQueryChangedPlayer(positionPlayer: Int) {
        TODO("Not yet implemented")
    }

    override fun onChangedPlayer(positionPlayer: Int, newNamePlayer: String) {
        TODO("Not yet implemented")
    }

    override fun onCancelChangedPlayer(positionPlayer: Int) {
        TODO("Not yet implemented")
    }

    override fun onAddPlayer() {
        TODO("Not yet implemented")
    }

    override fun onClickTypeCards(typeCards: TypeCards) {
        TODO("Not yet implemented")
    }

    override fun onStartGame() {
        TODO("Not yet implemented")
    }
}