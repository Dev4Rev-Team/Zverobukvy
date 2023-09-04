package ru.gb.zverobukvy.presentation.main_menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.gb.zverobukvy.data.resources_provider.ResourcesProvider
import ru.gb.zverobukvy.data.resources_provider.StringEnum
import ru.gb.zverobukvy.domain.entity.Avatar
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.repository.MainMenuRepository
import ru.gb.zverobukvy.utility.ui.SingleEventLiveData
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

class MainMenuViewModelImpl @Inject constructor(
    private val mainMenuRepository: MainMenuRepository,
    private val resourcesProvider: ResourcesProvider,
) :
    MainMenuViewModel, ViewModel() {
    private val typesCardsSelectedForGame: MutableList<TypeCards> = mutableListOf()

    private val namesPlayersSelectedForGame: MutableList<String> = mutableListOf()
    private val players: MutableList<PlayerInSettings?> = mutableListOf()
    private var lastEditablePlayer: PlayerInSettings? = null
    private var saveEditablePlayer = Player("")
    private var maxIdPlayer = 0L
    private val avatarList = mutableListOf<Avatar>()
    private var isClickAvatar = false


    private val liveDataPlayersScreenState =
        MutableLiveData<MainMenuState.PlayersScreenState>()

    private val liveDataScreenState = SingleEventLiveData<MainMenuState.ScreenState>()

    private val liveDataShowInstructionScreenState =
        SingleEventLiveData<MainMenuState.ShowInstructionsScreenState>()

    private val liveDataAvatarsScreenState = MutableLiveData<MainMenuState.AvatarsScreenState>()

    init {
        loadTypeCardsSelectedForGame()
        loadPlayersSelectedForGame()
        loadPlayersFromRepository()
    }

    private suspend fun loadAvatarsFromRepositoryLocal(): MutableList<Avatar> {
        val avatars = withContext(Dispatchers.IO) {
            mainMenuRepository.getAvatarsFromLocalDataSource()
        }
        avatarList.addAll(avatars)
        return avatarList
    }

    private suspend fun loadAvatarsFromRepositoryRemote(): MutableList<Avatar> {
        val avatar = withContext(Dispatchers.IO) {
            mainMenuRepository.getAvatarsFromRemoteDataSource(1)
        }
        avatarList.addAll(avatar)
        return avatarList
    }

    override fun onLaunch() {
        Timber.d("onLaunch")
        showInstruction()
        liveDataScreenState.value =
            MainMenuState.ScreenState.TypesCardsState(typesCardsSelectedForGame)
        liveDataPlayersScreenState.value =
            MainMenuState.PlayersScreenState.PlayersState(players)
    }

    private fun showInstruction() {
        if (mainMenuRepository.isFirstLaunch()) {
            liveDataShowInstructionScreenState.value = MainMenuState.ShowInstructionsScreenState
        }
    }

    private fun loadPlayersFromRepository() {
        viewModelScope.launch {
            players.addAll(
                mainMenuRepository.getPlayers().map {
                    mapToPlayerInSettings(it).apply {
                        if (namesPlayersSelectedForGame.contains(player.name)) {
                            isSelectedForGame = true
                        }
                    }
                })

            if (namesPlayersSelectedForGame.size == 0 && players.size > 0) {
                players[0]?.isSelectedForGame = true
            }

            if (players.size > 0) {
                maxIdPlayer = players.last()?.player?.id
                    ?: throw IllegalStateException("В базе данных некорректный игрок ${players.last()}")
            }
            players.add(ADD_PLAYER_BUTTON)

            liveDataPlayersScreenState.value =
                MainMenuState.PlayersScreenState.PlayersState(players)
        }
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
        liveDataScreenState.value =
            MainMenuState.ScreenState.TypesCardsState(typesCardsSelectedForGame)
    }

    override fun getLiveDataPlayersScreenState(): LiveData<MainMenuState.PlayersScreenState> {
        Timber.d("getLiveDataPlayersScreenState")
        return liveDataPlayersScreenState
    }

    override fun getLiveDataScreenState(): SingleEventLiveData<MainMenuState.ScreenState> {
        Timber.d("getLiveDataPlayersScreenState")
        return liveDataScreenState
    }

    override fun getLiveDataShowInstructionScreenState(): SingleEventLiveData<MainMenuState.ShowInstructionsScreenState> {
        Timber.d("getLiveDataShowInstructionScreenState")
        return liveDataShowInstructionScreenState
    }

    override fun getLiveDataAvatarsScreenState(): LiveData<MainMenuState.AvatarsScreenState> {
        Timber.d("getLiveDataAvatarsScreenState")
        return liveDataAvatarsScreenState
    }

    override fun onChangedSelectingPlayer(positionPlayer: Int) {
        closeEditablePlayer(true)
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
        closeEditablePlayer(false)
        viewModelScope.launch {
            players[positionPlayer]?.player?.let { mainMenuRepository.deletePlayer(it) }
        }

        players.removeAt(positionPlayer)

        liveDataPlayersScreenState.value =
            MainMenuState.PlayersScreenState.RemovePlayerState(players, positionPlayer)
    }

    override fun onQueryChangedPlayer(positionPlayer: Int) {
        closeEditablePlayer(true)
        openEditablePlayer(positionPlayer)
    }

    override fun onClickAvatar() {
        Timber.d("onClickAvatar")
        if (!isClickAvatar) {
            isClickAvatar = true
            viewModelScope.launch {
                if (avatarList.size == 0) {
                    loadAvatarsFromRepositoryLocal().apply {
                        avatarList.add(Avatar.ADD_AVATAR)
                    }
                }
                liveDataAvatarsScreenState.value =
                    MainMenuState.AvatarsScreenState.ShowAvatarsState(avatarList)
            }
        }
    }

    override fun onQueryChangedAvatar(positionAvatar: Int) {
        Timber.d("onQueryChangedAvatar")
        isClickAvatar = false
        liveDataAvatarsScreenState.value = MainMenuState.AvatarsScreenState.HideAvatarsState

        lastEditablePlayer?.player?.avatar = avatarList[positionAvatar]

        liveDataPlayersScreenState.value =
            MainMenuState.PlayersScreenState.ChangedPlayerState(
                players,
                players.indexOf(lastEditablePlayer)
            )
    }

    override fun onQueryAddAvatars() {
        Timber.d("onQueryAddAvatars")
        avatarList.removeLast()
        val quantities = avatarList.size
        viewModelScope.launch {
            loadAvatarsFromRepositoryRemote()
            avatarList.add(Avatar.ADD_AVATAR)
            liveDataAvatarsScreenState.value =
                MainMenuState.AvatarsScreenState.ShowAvatarsState(avatarList, quantities)
        }
    }


    override fun onChangedPlayer() {
        closeEditablePlayer(true)
    }

    override fun onCancelChangedPlayer() {
        closeEditablePlayer(false)
    }

    override fun onEditNamePlayer(newNamePlayer: String) {
        Timber.d("onEditNamePlayer")
        lastEditablePlayer?.apply {
            player.name = newNamePlayer
        }
    }

    override fun onAddPlayer() {
        maxIdPlayer += 1

        closeEditablePlayer(true)

        viewModelScope.launch {
            val name = createAndSavePlayer(maxIdPlayer)
            val newPosition = players.lastIndex
            players.add(newPosition, loadPlayerInSettings(name))

            liveDataPlayersScreenState.postValue(
                MainMenuState.PlayersScreenState.AddPlayerState(
                    players,
                    players.size - SHIFT_LAST_PLAYER
                )
            )
        }
    }

    private suspend fun loadPlayerInSettings(name: String): PlayerInSettings {
        val playersDB = mainMenuRepository.getPlayers()
        val newPlayerDB = playersDB.first {
            it.name == name
        }
        return PlayerInSettings(
            newPlayerDB,
            isSelectedForGame = true
        )
    }

    private suspend fun createAndSavePlayer(nameID: Long): String {
        val name = newNamePlayer(nameID)
        val player = PlayerInSettings(
            Player(name),
            isSelectedForGame = true
        )
        mainMenuRepository.insertPlayer(player.player)
        namesPlayersSelectedForGame.add(name)
        return name
    }

    private fun newNamePlayer(nameID: Long): String {
        return resourcesProvider.getString(StringEnum.MAIN_MENU_FRAGMENT_NEW_PLAYER)
            .format(nameID)
    }

    override fun onClickTypeCards(typeCards: TypeCards) {
        closeEditablePlayer(true)
        if (typesCardsSelectedForGame.contains(typeCards)) {
            typesCardsSelectedForGame.remove(typeCards)
        } else {
            typesCardsSelectedForGame.add(typeCards)
        }
    }

    override fun onStartGame() {
        Timber.d("onStartGame")

        closeEditablePlayer(true)
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

    override fun onClickScreen() {
        Timber.d("onClickScreen")
        closeEditablePlayer(true)
    }

    override fun onQueryShowInstruction() {
        Timber.d("onQueryInstruction")
        liveDataShowInstructionScreenState.value = MainMenuState.ShowInstructionsScreenState
    }

    private fun findPlayersForGame(): MutableList<PlayerInGame> {
        val playersForGame: MutableList<PlayerInGame> = mutableListOf()
        players.forEach {
            if (it != null && it.isSelectedForGame)
                playersForGame.add(PlayerInGame(it.player))
        }
        return playersForGame
    }

    private fun sendError(stringEnum: StringEnum) {
        liveDataScreenState.value =
            MainMenuState.ScreenState.ErrorState(
                resourcesProvider.getString(stringEnum)
            )
    }

    @Suppress("SameParameterValue")
    private fun sendError(stringEnum: StringEnum, name: String) {
        liveDataScreenState.value =
            MainMenuState.ScreenState.ErrorState(
                resourcesProvider.getString(stringEnum).format(name)
            )
    }

    private fun openEditablePlayer(positionPlayer: Int) {
        closeEditablePlayer(true)

        lastEditablePlayer = players[positionPlayer]?.apply {
            inEditingState = true
            this@MainMenuViewModelImpl.saveEditablePlayer.name = player.name
            this@MainMenuViewModelImpl.saveEditablePlayer.avatar = player.avatar
        }


        liveDataPlayersScreenState.value =
            MainMenuState.PlayersScreenState.ChangedPlayerState(
                players,
                positionPlayer
            )
    }

    private fun closeEditablePlayer(isSave: Boolean) {
        lastEditablePlayer?.let {
            var isValidate = true
            it.inEditingState = false
            it.player.name = it.player.name.trim()
            if (it.player.name.isEmpty()) {
                isValidate = false
                sendError(StringEnum.MAIN_MENU_FRAGMENT_THE_NAME_FIELD_IS_EMPTY)
            } else {
                players.forEach { item ->
                    if (item != it && item?.player?.name.equals(it.player.name)) {
                        isValidate = false
                        sendError(
                            StringEnum.MAIN_MENU_FRAGMENT_A_PLAYER_WITH_THE_SAME_NAME_ALREADY_EXISTS,
                            it.player.name
                        )
                    }
                }
            }
            if (isSave && isValidate) {
                viewModelScope.launch {
                    players[players.indexOf(it)]?.let { item ->
                        mainMenuRepository.updatePlayer(item.player)
                    }
                }
            } else {
                it.player.name = saveEditablePlayer.name
                it.player.avatar = saveEditablePlayer.avatar
            }
            liveDataPlayersScreenState.value =
                MainMenuState.PlayersScreenState.ChangedPlayerState(
                    players,
                    players.indexOf(it)
                )

            if (isClickAvatar) {
                isClickAvatar = false
                liveDataAvatarsScreenState.value =
                    MainMenuState.AvatarsScreenState.HideAvatarsState
            }

        }

        lastEditablePlayer = null
    }

    companion object {
        private val ADD_PLAYER_BUTTON = null
        private const val SHIFT_LAST_PLAYER = 2
        fun mapToPlayerInSettings(player: Player) = PlayerInSettings(player)
    }
}