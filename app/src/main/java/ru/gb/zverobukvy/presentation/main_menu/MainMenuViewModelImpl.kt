package ru.gb.zverobukvy.presentation.main_menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gb.zverobukvy.data.resources_provider.ResourcesProvider
import ru.gb.zverobukvy.data.resources_provider.StringEnum
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.repository.MainMenuRepository
import ru.gb.zverobukvy.utility.ui.SingleEventLiveData
import timber.log.Timber

class MainMenuViewModelImpl(
    private val mainMenuRepository: MainMenuRepository,
    private val resourcesProvider: ResourcesProvider,
) :
    MainMenuViewModel, ViewModel() {
    private val typesCardsSelectedForGame: MutableList<TypeCards> = mutableListOf()

    private val namesPlayersSelectedForGame: MutableList<String> = mutableListOf()
    private val players: MutableList<PlayerInSettings?> = mutableListOf()
    private var lastEditablePlayer: PlayerInSettings? = null


    private val liveDataPlayersScreenState =
        MutableLiveData<MainMenuState.PlayersScreenState>()

    private val liveDataScreenState = SingleEventLiveData<MainMenuState.ScreenState>()

    init {
        loadTypeCardsSelectedForGame()
        loadPlayersSelectedForGame()
        viewModelScope.launch {
            players.clear()
            players.addAll(
                mainMenuRepository.getPlayers().map {
                    mapToPlayerInSettings(it).apply {
                        if (namesPlayersSelectedForGame.contains(player.name)) {
                            isSelectedForGame = true
                        }
                    }
                })
            players.add(null)
            liveDataPlayersScreenState.value =
                MainMenuState.PlayersScreenState.PlayersState(players)
        }
    }
    override fun onLaunch() {
        Timber.d("onLaunch")
        liveDataScreenState.value =
            MainMenuState.ScreenState.TypesCardsState(typesCardsSelectedForGame)
        liveDataPlayersScreenState.value =
            MainMenuState.PlayersScreenState.PlayersState(players)
    }

    private fun loadPlayersSelectedForGame() {
        namesPlayersSelectedForGame.clear()
        namesPlayersSelectedForGame.addAll(
            mainMenuRepository.getNamesPlayersSelectedForGame()
        )
    }

    private fun loadTypeCardsSelectedForGame() {
        typesCardsSelectedForGame.clear()
        typesCardsSelectedForGame.addAll(mainMenuRepository.getTypesCardsSelectedForGame())
        if (typesCardsSelectedForGame.size == 0) {
            typesCardsSelectedForGame.add(TypeCards.ORANGE)
        }
    }

    override fun getLiveDataPlayersScreenState(): LiveData<MainMenuState.PlayersScreenState> {
        Timber.d("getLiveDataPlayersScreenState")
        return liveDataPlayersScreenState
    }

    override fun getLiveDataScreenState(): SingleEventLiveData<MainMenuState.ScreenState> {
        Timber.d("getLiveDataPlayersScreenState")
        return liveDataScreenState
    }

    override fun onChangedSelectingPlayer(positionPlayer: Int) {
        closeEditablePlayer()
        players[positionPlayer]?.apply {
            isSelectedForGame = !isSelectedForGame

        }

        players[positionPlayer]?.let {
            if (it.isSelectedForGame) {
                namesPlayersSelectedForGame.add(it.player.name)
            } else {
                namesPlayersSelectedForGame.remove(it.player.name)
            }
        }

        liveDataPlayersScreenState.value =
            MainMenuState.PlayersScreenState.ChangedPlayerState(
                players,
                positionPlayer
            )
    }

    override fun onRemovePlayer(positionPlayer: Int) {
        viewModelScope.launch {
            players[positionPlayer]?.player?.let { mainMenuRepository.deletePlayer(it) }
        }

        players.removeAt(positionPlayer)

        liveDataPlayersScreenState.value =
            MainMenuState.PlayersScreenState.RemovePlayerState(players, positionPlayer)
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
            players[positionPlayer]?.let { mainMenuRepository.updatePlayer(it.player) }
        }

        liveDataPlayersScreenState.value =
            MainMenuState.PlayersScreenState.ChangedPlayerState(
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
                MainMenuState.PlayersScreenState.AddPlayerState(
                    players,
                    players.size - 2
                )
            )
        }
    }

    private suspend fun loadPlayerInSettings(): PlayerInSettings {
        val playersDB = mainMenuRepository.getPlayers()
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
        mainMenuRepository.insertPlayer(player.player)
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
                MainMenuState.ScreenState.StartGame(typesCardsSelectedForGame, playersForGame)
            )
        }
    }

    override fun onViewPause() {
        Timber.d("onViewPause")
        mainMenuRepository.saveTypesCardsSelectedForGame(
            typesCardsSelectedForGame
        )
        mainMenuRepository.saveNamesPlayersSelectedForGame(
            namesPlayersSelectedForGame
        )
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
            MainMenuState.ScreenState.ErrorState(
                resourcesProvider.getString(stringEnum)
            )
    }

    private fun openEditablePlayer(positionPlayer: Int) {
        closeEditablePlayer()

        players[positionPlayer]?.apply {
            inEditingState = true
            this@MainMenuViewModelImpl.lastEditablePlayer = this
        }

        liveDataPlayersScreenState.value =
            MainMenuState.PlayersScreenState.ChangedPlayerState(
                players,
                positionPlayer
            )
    }

    private fun closeEditablePlayer() {
        lastEditablePlayer?.let {
            it.inEditingState = false
            liveDataPlayersScreenState.value =
                MainMenuState.PlayersScreenState.ChangedPlayerState(
                    players, players.indexOf(it)
                )

        }
        lastEditablePlayer = null
    }

    companion object {
        fun mapToPlayerInSettings(player: Player) = PlayerInSettings(player)
    }
}