package ru.gb.zverobukvy.presentation.main_menu.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gb.zverobukvy.data.resources_provider.ResourcesProvider
import ru.gb.zverobukvy.data.resources_provider.StringEnum
import ru.gb.zverobukvy.domain.app_state.SettingsScreenState
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.PlayerInSettings
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.repository.NamesPlayersSelectedForGameRepository
import ru.gb.zverobukvy.domain.repository.PlayersRepository
import ru.gb.zverobukvy.domain.repository.TypesCardsSelectedForGameRepository
import ru.gb.zverobukvy.presentation.SingleEventLiveData
import timber.log.Timber

class SettingsScreenViewModelImpl(
    private val playersRepository: PlayersRepository,
    private val typesCardsSelectedForGameRepository: TypesCardsSelectedForGameRepository,
    private val namesPlayersSelectedForGameRepository: NamesPlayersSelectedForGameRepository,
    private val resourcesProvider: ResourcesProvider,
) :
    SettingsScreenViewModel, ViewModel() {
    private val typesCardsSelectedForGame: MutableList<TypeCards> = mutableListOf()

    private val namesPlayersSelectedForGame: MutableList<String> = mutableListOf()
    private val players: MutableList<PlayerInSettings?> = mutableListOf()
    private var lastEditablePlayer: PlayerInSettings? = null


    private val liveDataPlayersScreenState =
        MutableLiveData<SettingsScreenState.PlayersScreenState>()

    private val liveDataScreenState = SingleEventLiveData<SettingsScreenState.ScreenState>()

    override fun onLaunch() {
        Timber.d("onLaunch")
        loadTypeCardsSelectedForGame()
        liveDataScreenState.value =
            SettingsScreenState.ScreenState.TypesCardsState(typesCardsSelectedForGame)

        loadPlayersSelectedForGame()

        viewModelScope.launch {
            players.clear()
            players.addAll(
                playersRepository.getPlayers().map {
                    mapToPlayerInSettings(it).apply {
                        if (namesPlayersSelectedForGame.contains(player.name)) {
                            isSelectedForGame = true
                        }
                    }
                })
            players.add(null)
            liveDataPlayersScreenState.value =
                SettingsScreenState.PlayersScreenState.PlayersState(players)
        }
    }

    private fun loadPlayersSelectedForGame() {
        namesPlayersSelectedForGame.clear()
        namesPlayersSelectedForGame.addAll(
            namesPlayersSelectedForGameRepository.getNamesPlayersSelectedForGame()
        )
    }

    private fun loadTypeCardsSelectedForGame() {
        typesCardsSelectedForGame.clear()
        typesCardsSelectedForGame.addAll(typesCardsSelectedForGameRepository.getTypesCardsSelectedForGame())
        if (typesCardsSelectedForGame.size == 0) {
            typesCardsSelectedForGame.add(TypeCards.ORANGE)
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
        closeEditablePlayer()
        players[positionPlayer]?.apply {
            isSelectedForGame = !isSelectedForGame
        }

        players[positionPlayer]?.player?.let { namesPlayersSelectedForGame.add(it.name) }

        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.ChangedPlayerState(
                players,
                positionPlayer
            )
    }

    override fun onRemovePlayer(positionPlayer: Int) {
        viewModelScope.launch {
            players[positionPlayer]?.player?.let { playersRepository.deletePlayer(it) }
        }

        players.removeAt(positionPlayer)

        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.RemovePlayerState(players, positionPlayer)
    }

    override fun onQueryChangedPlayer(positionPlayer: Int) {
        closeEditablePlayer()
        openEditablePlayer(positionPlayer)
    }

    override fun onChangedPlayer(positionPlayer: Int, newNamePlayer: String) {
        closeEditablePlayer()
        players[positionPlayer]?.apply {
            player.name = newNamePlayer
        }
        viewModelScope.launch {
            players[positionPlayer]?.let { playersRepository.updatePlayer(it.player) }
        }

        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.ChangedPlayerState(
                players,
                positionPlayer
            )
    }

    override fun onCancelChangedPlayer(positionPlayer: Int) {
        closeEditablePlayer()
    }

    override fun onAddPlayer() {
        closeEditablePlayer()

        viewModelScope.launch {
            createAndSavePlayer()
            val newPosition = players.size - 1
            players.add(newPosition, loadPlayerInSettings())

            liveDataPlayersScreenState.postValue(
                SettingsScreenState.PlayersScreenState.AddPlayerState(
                    players,
                    players.size - 2
                )
            )
        }
    }

    private suspend fun loadPlayerInSettings(): PlayerInSettings {
        val playersDB = playersRepository.getPlayers()
        return PlayerInSettings(
            playersDB[playersDB.size - 1],
            isSelectedForGame = true
        )
    }

    private suspend fun createAndSavePlayer() {
        val player = PlayerInSettings(
            Player("new ${players.size}"),
            isSelectedForGame = true
        )
        playersRepository.insertPlayer(player.player)
        namesPlayersSelectedForGame.add(player.player.name)
    }

    override fun onClickTypeCards(typeCards: TypeCards) {
        closeEditablePlayer()
        if (typesCardsSelectedForGame.contains(typeCards)) {
            typesCardsSelectedForGame.remove(typeCards)
        } else {
            typesCardsSelectedForGame.add(typeCards)
        }
    }

    override fun onStartGame() {
        Timber.d("onStartGame")

        closeEditablePlayer()
        val playersForGame = findPlayersForGame()

        if (typesCardsSelectedForGame.size == 0) {
            sendError(StringEnum.MAIN_MENU_FRAGMENT_NO_CARD_SELECTED)
        } else if (playersForGame.size == 0) {
            sendError(StringEnum.MAIN_MENU_FRAGMENT_NO_PLAYER_SELECTED)
        } else {
            liveDataScreenState.postValue(
                SettingsScreenState.ScreenState.StartGame(typesCardsSelectedForGame, playersForGame)
            )
            typesCardsSelectedForGameRepository.saveTypesCardsSelectedForGame(
                typesCardsSelectedForGame
            )
            namesPlayersSelectedForGameRepository.saveNamesPlayersSelectedForGame(
                namesPlayersSelectedForGame
            )

        }
    }

    private fun findPlayersForGame(): MutableList<PlayerInGame> {
        val playersForGame: MutableList<PlayerInGame> = mutableListOf()
        players.forEach {
            if (it != null && it.isSelectedForGame)
                playersForGame.add(PlayerInGame(it.player.name))
        }
        return playersForGame
    }

    private fun sendError(stringEnum: StringEnum) {
        liveDataScreenState.value =
            SettingsScreenState.ScreenState.ErrorState(
                resourcesProvider.getString(stringEnum)
            )
    }

    private fun openEditablePlayer(positionPlayer: Int) {
        closeEditablePlayer()

        players[positionPlayer]?.apply {
            inEditingState = true
            this@SettingsScreenViewModelImpl.lastEditablePlayer = this
        }

        liveDataPlayersScreenState.value =
            SettingsScreenState.PlayersScreenState.ChangedPlayerState(
                players,
                positionPlayer
            )
    }

    private fun closeEditablePlayer() {
        lastEditablePlayer?.let {
            it.inEditingState = false
            liveDataPlayersScreenState.value =
                SettingsScreenState.PlayersScreenState.ChangedPlayerState(
                    players, players.indexOf(it)
                )

        }
        lastEditablePlayer = null
    }

    companion object {
        fun mapToPlayerInSettings(player: Player) = PlayerInSettings(player)
    }
}