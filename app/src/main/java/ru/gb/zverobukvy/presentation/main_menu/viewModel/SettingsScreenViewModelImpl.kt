package ru.gb.zverobukvy.presentation.main_menu.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.zverobukvy.domain.app_state.SettingsScreenState
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.PlayerInSettings
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.repository.PlayersRepository
import ru.gb.zverobukvy.presentation.SingleEventLiveData
import timber.log.Timber

class SettingsScreenViewModelImpl(private val playersRepository: PlayersRepository) :
    SettingsScreenViewModel, ViewModel() {
    private val typesCardsSelectedForGame: MutableList<TypeCards> = mutableListOf()

    private val playersSelectedForGame: MutableList<PlayerInSettings?> = mutableListOf()

    private val liveDataPlayersScreenState =
        MutableLiveData<SettingsScreenState.PlayersScreenState>()

    private val liveDataScreenState = SingleEventLiveData<SettingsScreenState.ScreenState>()

    override fun onLaunch(
        typesCardsSelectedForGameFromPreference: List<TypeCards>,
        namesPlayersSelectedForGameFromPreference: List<String>
    ) {
        Timber.d("onLaunch")
        typesCardsSelectedForGame.addAll(typesCardsSelectedForGameFromPreference)
        //TODO реализовать формирование List<PlayerInSettings> с использованием
        // namesPlayersSelectedForGameFromPreference и с запросом к репозиторию
        playersSelectedForGame.addAll(
            listOf(
                PlayerInSettings(
                    Player("Игрок 1"),
                    isSelectedForGame = true,
                    inEditingState = false
                ),
                PlayerInSettings(
                    Player("Игрок 2"),
                    isSelectedForGame = false,
                    inEditingState = false
                ),
                PlayerInSettings(
                    Player("Игрок 3"),
                    isSelectedForGame = false,
                    inEditingState = false
                ),
                null
            )
        )
        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.PlayersState(playersSelectedForGame)
    }

    override fun getLiveDataPlayersScreenState(): LiveData<SettingsScreenState.PlayersScreenState> {
        Timber.d("getLiveDataPlayersScreenState")
        return liveDataPlayersScreenState
    }

    override fun getLiveDataScreenState(): SingleEventLiveData<SettingsScreenState.ScreenState> {
        Timber.d("getLiveDataPlayersScreenState")
        return liveDataScreenState
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
        Timber.d("onStart")
        val players: MutableList<PlayerInGame> = mutableListOf()
        //TODO сделать рефакторинг
        playersSelectedForGame.forEach {
            if(it!=null)
                players.add(PlayerInGame(it.player.name))
        }
        liveDataScreenState.value =
            SettingsScreenState.ScreenState.StartGame(typesCardsSelectedForGame, players)
    }
}