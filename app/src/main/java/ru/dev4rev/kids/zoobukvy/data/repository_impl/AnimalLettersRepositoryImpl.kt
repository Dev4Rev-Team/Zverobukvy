package ru.dev4rev.kids.zoobukvy.data.repository_impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.dev4rev.kids.zoobukvy.data.data_source.LocalDataSource
import ru.dev4rev.kids.zoobukvy.data.data_source.RemoteDataSource
import ru.dev4rev.kids.zoobukvy.data.mapper.extract_helpers.ExtractTypesCardsHelper
import ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.card.CardsSetMapperToDomain
import ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.card.LetterCardMapperToDomain
import ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.card.SharedPreferencesTypeCardsMapper
import ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.card.WordCardMapperToDomain
import ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.player.AvatarApiMapper
import ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.player.AvatarRoomMapper
import ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.player.PlayerMapperToData
import ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.player.PlayerMapperToDomain
import ru.dev4rev.kids.zoobukvy.data.network_state.NetworkStatusImpl
import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForMainMenu
import ru.dev4rev.kids.zoobukvy.domain.entity.card.CardsSet
import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.kids.zoobukvy.domain.entity.card.WordCard
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import ru.dev4rev.kids.zoobukvy.domain.repository.LoadingDataRepository
import ru.dev4rev.kids.zoobukvy.domain.repository.animal_letter_game.AnimalLettersGameRepository
import ru.dev4rev.kids.zoobukvy.domain.repository.main_menu.MainMenuRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalLettersRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferencesForMainMenu: SharedPreferencesForMainMenu,
    private val networkStatus: NetworkStatusImpl
) : AnimalLettersGameRepository, MainMenuRepository, LoadingDataRepository {
    private val letterCardMapperToDomain = LetterCardMapperToDomain()

    private val wordCardMapperToDomain = WordCardMapperToDomain()

    private val sharedPreferencesTypeCardsMapper = SharedPreferencesTypeCardsMapper()

    private val cardsSetMapperToDomain = CardsSetMapperToDomain()

    private val playersMapperDomain = PlayerMapperToDomain()

    private val playersMapperData = PlayerMapperToData()

    private val avatarRoomMapper = AvatarRoomMapper()

    private val avatarApiMapper = AvatarApiMapper()

    private var letterCards = listOf<LetterCard>()

    private var wordCards = listOf<WordCard>()

    private var players: MutableList<Player>? = null

    override var isOnline: Boolean = false

    private val repositoryCoroutineScope = CoroutineScope(
        Dispatchers.IO
    )

    init {
        registerNetworkStatus()
    }

    override suspend fun getLetterCards(): List<LetterCard> =
        letterCards.ifEmpty {
            withContext(Dispatchers.IO) {
                localDataSource.getLetterCards().map {
                    letterCardMapperToDomain.mapToDomain(it)
                }
            }
        }

    override suspend fun getWordCards(): List<WordCard> =
        wordCards.ifEmpty {
            withContext(Dispatchers.IO) {
                localDataSource.getWordCards().map {
                    wordCardMapperToDomain.mapToDomain(it)
                }
            }
        }

    override suspend fun getCardsSet(typeCards: TypeCards): List<CardsSet> =
        withContext(Dispatchers.IO) {
            localDataSource.getCardsSetByColor(ExtractTypesCardsHelper.extractColor(typeCards))
                .map {
                    cardsSetMapperToDomain.mapToDomain(it)
                }
        }

    override suspend fun getPlayers(): List<Player> =
        players ?: withContext(Dispatchers.IO) {
            localDataSource.getPlayers().map {
                playersMapperDomain.mapToDomain(it)
            }
        }

    override suspend fun deletePlayer(player: Player) {
        withContext(Dispatchers.IO) {
            localDataSource.deletePlayer(playersMapperData.mapToData(player))
        }
        players?.remove(player)
    }

    override suspend fun insertPlayer(player: Player): Long {
        val playerId = withContext(Dispatchers.IO) {
            localDataSource.insertPlayer(playersMapperData.mapToData(player))
        }
        players?.add(player.apply {
            id = playerId
        })
        return playerId
    }

    override suspend fun updatePlayer(player: Player) {
        withContext(Dispatchers.IO) {
            localDataSource.updatePlayer(playersMapperData.mapToData(player))
        }
    }

    override suspend fun loadingData() {
        if (players == null)
            withContext(Dispatchers.IO){
                players = withContext(Dispatchers.IO) {
                    localDataSource.getPlayers().map {
                        playersMapperDomain.mapToDomain(it)
                    }
                }.toMutableList()
            }
    }

    override fun getTypesCardsSelectedForGame(): List<TypeCards> =
        sharedPreferencesForMainMenu.readTypesCardsSelectedForGame().map {
            sharedPreferencesTypeCardsMapper.mapToDomain(it)
        }

    override fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCards>) =
        sharedPreferencesForMainMenu.saveTypesCardsSelectedForGame(
            typesCardsSelectedForGame.map {
                sharedPreferencesTypeCardsMapper.mapToData(it)
            }
        )

    override fun getNamesPlayersSelectedForGame(): List<String> =
        sharedPreferencesForMainMenu.readNamesPlayersSelectedForGame()

    override fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>) =
        sharedPreferencesForMainMenu.saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame)

    override fun isFirstLaunch(): Boolean =
       sharedPreferencesForMainMenu.isFirstLaunch()

    override suspend fun getAvatarsFromLocalDataSource(): List<Avatar> =
        withContext(Dispatchers.IO) {
            localDataSource.getAvatars().map {
                avatarRoomMapper.mapToDomain(it)
            }
        }

    override suspend fun getAvatarsFromRemoteDataSource(quantities: Int): List<Avatar> =
        withContext(Dispatchers.IO) {
            remoteDataSource.getRandomAvatars(quantities).map {
                avatarApiMapper.mapToDomain(it)
            }
        }

    override suspend fun insertAvatar(avatar: Avatar): Long =
        withContext(Dispatchers.IO) {
            localDataSource.insertAvatar(avatarRoomMapper.mapToData(avatar))
        }

    private fun registerNetworkStatus() {
        repositoryCoroutineScope.launch {
            withContext(Dispatchers.Default) {
                networkStatus.registerNetworkCallback()
                    .collect {
                        isOnline = it
                    }
            }
        }
    }
}
