package ru.dev4rev.kids.zoobukvy.presentation.main_menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.dev4rev.kids.zoobukvy.data.resources_provider.ResourcesProvider
import ru.dev4rev.kids.zoobukvy.data.resources_provider.StringEnum
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import ru.dev4rev.kids.zoobukvy.domain.entity.player.PlayerInGame
import ru.dev4rev.kids.zoobukvy.domain.repository.main_menu.MainMenuRepository
import ru.dev4rev.kids.zoobukvy.utility.ui.SingleEventLiveData
import timber.log.Timber
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
    private var saveEditablePlayer = Player.HumanPlayer("")
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
        createComputer()
        loadPlayersFromRepository()
    }

    private fun createComputer() {
        val computer: Player = Player.ComputerPlayer
        computer.name = resourcesProvider.getString(StringEnum.MAIN_MENU_FRAGMENT_NAME_COMPUTER)
        val isSelectedForGame = namesPlayersSelectedForGame.contains(computer.name)
        players.add(PlayerInSettings(computer, isSelectedForGame))
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
            mainMenuRepository.getAvatarsFromRemoteDataSource(QUANTITIES_AVATAR)
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

            if (namesPlayersSelectedForGame.size == 0 && players.size > ONE_PLAYER) {
                players[ONE_PLAYER]?.isSelectedForGame = true
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
        if (!checkEditablePlayer()) return
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
        if (!checkEditablePlayer()) return
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
        if (mainMenuRepository.isOnline) {
            avatarList.removeLast()

            val exceptionHandler =
                CoroutineExceptionHandler { _, _ ->
                    liveDataScreenState.value =
                        MainMenuState.ScreenState.ErrorState(
                            resourcesProvider.getString(StringEnum.MAIN_MENU_FRAGMENT_NO_SERVER_CONNECTION)
                        )
                }
            viewModelScope.launch(exceptionHandler) {
                loadAvatarsFromRepositoryRemote()
                avatarList.add(Avatar.ADD_AVATAR)
                val quantities = avatarList.lastIndex
                liveDataAvatarsScreenState.value =
                    MainMenuState.AvatarsScreenState.ShowAvatarsState(avatarList, quantities)
            }
        } else {
            liveDataScreenState.value =
                MainMenuState.ScreenState.ErrorState(
                    resourcesProvider.getString(
                        StringEnum.MAIN_MENU_FRAGMENT_NO_INTERNET_CONNECTION
                    )
                )
            liveDataAvatarsScreenState.value =
                MainMenuState.AvatarsScreenState.ShowAvatarsState(avatarList, avatarList.lastIndex)
        }

    }


    override fun onChangedPlayer() {
        if (!checkEditablePlayer()) return
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

        if (players.size > MAX_PLAYER) {
            liveDataScreenState.value =
                MainMenuState.ScreenState.ErrorState(
                    resourcesProvider.getString(StringEnum.MAIN_MENU_FRAGMENT_MAX_PLAYERS)
                )
            return
        }
        maxIdPlayer += 1

        if (!checkEditablePlayer()) return
        closeEditablePlayer(true)

        viewModelScope.launch {
            val player = createAndSavePlayer(maxIdPlayer)
            val newPosition = players.lastIndex
            players.add(newPosition, player)

            liveDataPlayersScreenState.postValue(
                MainMenuState.PlayersScreenState.AddPlayerState(
                    players,
                    players.size - SHIFT_LAST_PLAYER
                )
            )
        }
    }

    private suspend fun createAndSavePlayer(nameID: Long): PlayerInSettings {
        val name = newNamePlayer(nameID)
        val player = PlayerInSettings(
            Player.HumanPlayer(name),
            isSelectedForGame = false
        )
        player.player.id = mainMenuRepository.insertPlayer(player.player)
        namesPlayersSelectedForGame.add(name)
        return player
    }

    private fun newNamePlayer(nameID: Long): String {
        return resourcesProvider.getString(StringEnum.MAIN_MENU_FRAGMENT_NEW_PLAYER)
            .format(nameID)
    }

    override fun onClickTypeCards(typeCards: TypeCards) {
        if (checkEditablePlayer()) {
            closeEditablePlayer(true)
            if (typesCardsSelectedForGame.contains(typeCards)) {
                typesCardsSelectedForGame.remove(typeCards)
            } else {
                typesCardsSelectedForGame.add(typeCards)
            }
        }
        liveDataScreenState.value =
            MainMenuState.ScreenState.TypesCardsState(typesCardsSelectedForGame)
    }

    override fun onStartGame() {
        Timber.d("onStartGame")
        if (!checkEditablePlayer()) return
        closeEditablePlayer(true)
        val playersForGame = findPlayersForGame()

        if (typesCardsSelectedForGame.size == 0) {
            sendError(StringEnum.MAIN_MENU_FRAGMENT_NO_CARD_SELECTED)
        } else if (playersForGame.size == 0 || (playersForGame.size == COUNT_COMPUTER
                    && players[0]?.isSelectedForGame == true)
        ) {
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
        if (isClickAvatar) {
            isClickAvatar = false
            liveDataAvatarsScreenState.value = MainMenuState.AvatarsScreenState.HideAvatarsState
        } else {
            if (!checkEditablePlayer()) return
            closeEditablePlayer(true)
        }
    }

    override fun onBackPressed() {
        Timber.d("onBackPressed")
        if (lastEditablePlayer != null) {
            if (!checkEditablePlayer()) return
            closeEditablePlayer(true)
        } else {
            liveDataScreenState.value = MainMenuState.ScreenState.CloseAppState
        }
    }

    override fun onQueryShowInstruction() {
        Timber.d("onQueryInstruction")
        if (!checkEditablePlayer()) return
        closeEditablePlayer(true)
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
        if (!checkEditablePlayer()) return
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

    private fun checkEditablePlayer(): Boolean {
        lastEditablePlayer?.let {
            if (it.player.name.isEmpty()) {
                sendError(StringEnum.MAIN_MENU_FRAGMENT_THE_NAME_FIELD_IS_EMPTY)
                return false
            } else {
                players.forEach { item ->
                    if (item != it && item?.player?.name.equals(it.player.name)) {
                        sendError(
                            StringEnum.MAIN_MENU_FRAGMENT_A_PLAYER_WITH_THE_SAME_NAME_ALREADY_EXISTS,
                            it.player.name
                        )
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun closeEditablePlayer(isSave: Boolean) {
        lastEditablePlayer?.let {
            it.inEditingState = false
            it.player.name = it.player.name.trim()
            if (isSave) {
                viewModelScope.launch {
                    players[players.indexOf(it)]?.let { item ->
                        val avatar = item.player.avatar
                        if (!avatar.isStandard) {
                            val id = mainMenuRepository.insertAvatar(avatar)
                            avatar.id = id
                        }
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
        private const val QUANTITIES_AVATAR = 7
        private const val MAX_PLAYER = 15
        private const val ONE_PLAYER = 1
        private const val COUNT_COMPUTER = 1
        fun mapToPlayerInSettings(player: Player) = PlayerInSettings(player)
    }
}