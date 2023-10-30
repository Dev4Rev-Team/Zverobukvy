package ru.gb.zverobukvy.data.repository_impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.data_source.RemoteDataSource
import ru.gb.zverobukvy.data.mapper.mapper_impl.AvatarApiMapper
import ru.gb.zverobukvy.data.mapper.mapper_impl.AvatarRoomMapper
import ru.gb.zverobukvy.data.mapper.mapper_impl.LetterCardMapperToDomain
import ru.gb.zverobukvy.data.mapper.mapper_impl.PlayerMapperToData
import ru.gb.zverobukvy.data.mapper.mapper_impl.PlayerMapperToDomain
import ru.gb.zverobukvy.data.mapper.mapper_impl.SharedPreferencesTypeCardsMapper
import ru.gb.zverobukvy.data.mapper.mapper_impl.WordCardMapperToDomain
import ru.gb.zverobukvy.data.network_state.NetworkStatusImpl
import ru.gb.zverobukvy.data.preferences.SharedPreferencesForGame
import ru.gb.zverobukvy.domain.entity.Avatar
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.entity.WordCard
import ru.gb.zverobukvy.domain.repository.AnimalLettersGameRepository
import ru.gb.zverobukvy.domain.repository.MainMenuRepository
import javax.inject.Inject

class AnimalLettersRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val sharedPreferencesForGame: SharedPreferencesForGame,
    private val networkStatus: NetworkStatusImpl
) : AnimalLettersGameRepository, MainMenuRepository {
    private val letterCardMapperToDomain = LetterCardMapperToDomain()

    private val wordCardMapperToDomain = WordCardMapperToDomain()

    private val sharedPreferencesTypeCardsMapper = SharedPreferencesTypeCardsMapper()

    private val playersMapperDomain = PlayerMapperToDomain()

    private val playersMapperData = PlayerMapperToData()

    private val avatarRoomMapper = AvatarRoomMapper()

    private val avatarApiMapper = AvatarApiMapper()

    override var isOnline: Boolean = false

    private val repositoryCoroutineScope = CoroutineScope(
    Dispatchers.IO
    )

    init{
        registerNetworkStatus()
    }

    override suspend fun getLetterCards(): List<LetterCard> =
        withContext(Dispatchers.IO) {
            localDataSource.getLetterCards().map {
                letterCardMapperToDomain.mapToDomain(it)
            }
        }

    override suspend fun getWordCards(): List<WordCard> =
        withContext(Dispatchers.IO){
            localDataSource.getWordCards().map {
                wordCardMapperToDomain.mapToDomain(it)
            }
        }

    override suspend fun getPlayers(): List<Player> =
        withContext(Dispatchers.IO){
            localDataSource.getPlayers().map {
                playersMapperDomain.mapToDomain(it)
            }
        }

    override suspend fun deletePlayer(player: Player) {
        withContext(Dispatchers.IO){
            localDataSource.deletePlayer(playersMapperData.mapToData(player))
        }
    }

    override suspend fun insertPlayer(player: Player): Long =
        withContext(Dispatchers.IO){
            localDataSource.insertPlayer(playersMapperData.mapToData(player))
        }

    override suspend fun updatePlayer(player: Player) {
        withContext(Dispatchers.IO){
            localDataSource.updatePlayer(playersMapperData.mapToData(player))
        }
    }

    override fun getTypesCardsSelectedForGame(): List<TypeCards> =
        sharedPreferencesForGame.readTypesCardsSelectedForGame().map {
            sharedPreferencesTypeCardsMapper.mapToDomain(it)
        }

    override fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCards>) =
        sharedPreferencesForGame.saveTypesCardsSelectedForGame(
            typesCardsSelectedForGame.map {
                sharedPreferencesTypeCardsMapper.mapToData(it)
            }
        )

    override fun getNamesPlayersSelectedForGame(): List<String> =
        sharedPreferencesForGame.readNamesPlayersSelectedForGame()

    override fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>) =
        sharedPreferencesForGame.saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame)

    override fun isFirstLaunch(): Boolean =
        sharedPreferencesForGame.isFirstLaunch()

    override suspend fun getAvatarsFromLocalDataSource(): List<Avatar> =
        withContext(Dispatchers.IO){
            localDataSource.getAvatars().map{
                avatarRoomMapper.mapToDomain(it)
            }
        }

    override suspend fun getAvatarsFromRemoteDataSource(quantities: Int): List<Avatar> =
        withContext(Dispatchers.IO){
            remoteDataSource.getRandomAvatars(quantities).map {
                avatarApiMapper.mapToDomain(it)
            }
        }

    override suspend fun insertAvatar(avatar: Avatar): Long =
        withContext(Dispatchers.IO){
            localDataSource.insertAvatar(avatarRoomMapper.mapToData(avatar))
        }

    private fun registerNetworkStatus() {
        repositoryCoroutineScope.launch {
            withContext(Dispatchers.Default){
                networkStatus.registerNetworkCallback()
                    .collect {
                        isOnline = it
                    }
            }
        }
    }
}
