package ru.gb.zverobukvy.presentation.main_menu.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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

    private val playersSelectedForGame: MutableList<String> = mutableListOf()
    private val players: MutableList<PlayerInSettings?> = mutableListOf()

    private val liveDataPlayersScreenState =
        MutableLiveData<SettingsScreenState.PlayersScreenState>()

    private val liveDataScreenState = SingleEventLiveData<SettingsScreenState.ScreenState>()

    override fun onLaunch(
        typesCardsSelectedForGameFromPreference: List<TypeCards>,
        namesPlayersSelectedForGameFromPreference: List<String>,
    ) {
        Timber.d("onLaunch")
        typesCardsSelectedForGame.addAll(typesCardsSelectedForGameFromPreference)
        //TODO реализовать формирование List<String> с использованием
        // namesPlayersSelectedForGameFromPreference и с запросом к репозиторию
        playersSelectedForGame.addAll(listOf("Игрок 1", "Игрок 2", "Игрок 3"))

        viewModelScope.launch {
            players.addAll(
                playersRepository.getPlayers(listOf()).map {
                    mapToPlayerInSettings(it).apply {
                        if (playersSelectedForGame.contains(player.name)) {
                            isSelectedForGame = true
                        }
                    }
                })

            liveDataPlayersScreenState.value =
                SettingsScreenState.PlayersScreenState.PlayersState(players)
        }
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
        players[positionPlayer]?.apply {
            isSelectedForGame = !isSelectedForGame
        }

        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.ChangedPlayerState(
                players,
                positionPlayer
            )
    }

    override fun onRemovePlayer(positionPlayer: Int) {
        players.removeAt(positionPlayer)

        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.RemovePlayerState(players, positionPlayer)
    }

    override fun onQueryChangedPlayer(positionPlayer: Int) {
        players[positionPlayer]?.apply {
            inEditingState = true
        }

        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.ChangedPlayerState(
                players,
                positionPlayer
            )
    }

    override fun onChangedPlayer(positionPlayer: Int, newNamePlayer: String) {
        players[positionPlayer]?.apply {
            //TODO player = Player(newNamePlayer)
            inEditingState = false
        }

        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.ChangedPlayerState(
                players,
                positionPlayer
            )
    }

    override fun onCancelChangedPlayer(positionPlayer: Int) {
        players[positionPlayer]?.apply {
            inEditingState = false
        }

        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.ChangedPlayerState(
                players,
                positionPlayer
            )
    }

    override fun onAddPlayer() {
        val player = PlayerInSettings(
            Player("new"),
            isSelectedForGame = true,
            inEditingState = true
        )
        players.add(player)

        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.AddPlayerState(
                players,
                players.size - 1
            )
    }

    override fun onClickTypeCards(typeCards: TypeCards) {
        if (typesCardsSelectedForGame.contains(typeCards)) {
            typesCardsSelectedForGame.remove(typeCards)
        } else {
            typesCardsSelectedForGame.add(typeCards)
        }
    }

    override fun onStartGame() {
        Timber.d("onStart")
        val playersForGame: MutableList<PlayerInGame> = mutableListOf()
        //TODO сделать рефакторинг
        players.forEach {
            if (it != null && it.isSelectedForGame)
                playersForGame.add(PlayerInGame(it.player.name))
        }
        liveDataScreenState.value =
            SettingsScreenState.ScreenState.StartGame(typesCardsSelectedForGame, playersForGame)
    }

    companion object {
        fun mapToPlayerInSettings(player: Player) = PlayerInSettings(player)
    }
}