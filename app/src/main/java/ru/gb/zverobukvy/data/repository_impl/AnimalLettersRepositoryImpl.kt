package ru.gb.zverobukvy.data.repository_impl

import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.mapper.AvatarMapper
import ru.gb.zverobukvy.data.mapper.LetterCardMapperToDomain
import ru.gb.zverobukvy.data.mapper.PlayerMapperToData
import ru.gb.zverobukvy.data.mapper.PlayerMapperToDomain
import ru.gb.zverobukvy.data.mapper.TypeCardsMapper
import ru.gb.zverobukvy.data.mapper.WordCardMapperToDomain
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
    private val sharedPreferencesForGame: SharedPreferencesForGame,
) : AnimalLettersGameRepository, MainMenuRepository {
    private val letterCardMapperToDomain = LetterCardMapperToDomain()

    private val wordCardMapperToDomain = WordCardMapperToDomain()

    private val typeCardsMapper = TypeCardsMapper()

    private val playersMapperDomain = PlayerMapperToDomain()

    private val playersMapperData = PlayerMapperToData()

    private val avatarsMapper = AvatarMapper()

    override suspend fun getLetterCards(): List<LetterCard> =
        localDataSource.getLetterCards().map {
            letterCardMapperToDomain.mapToDomain(it)
        }

    override suspend fun getWordCards(): List<WordCard> =
        localDataSource.getWordCards().map {
            wordCardMapperToDomain.mapToDomain(it)
        }

    override suspend fun getPlayers(): List<Player> =
        localDataSource.getPlayers().map {
            playersMapperDomain.mapToDomain(it)
        }

    override suspend fun deletePlayer(player: Player) {
        localDataSource.deletePlayer(playersMapperData.mapToData(player))
    }

    override suspend fun insertPlayer(player: Player) {
        localDataSource.insertPlayer(playersMapperData.mapToData(player))
    }

    override suspend fun updatePlayer(player: Player) {
        localDataSource.updatePlayer(playersMapperData.mapToData(player))
    }

    override fun getTypesCardsSelectedForGame(): List<TypeCards> =
        sharedPreferencesForGame.readTypesCardsSelectedForGame().map {
            typeCardsMapper.mapToDomain(it)
        }

    override fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCards>) =
        sharedPreferencesForGame.saveTypesCardsSelectedForGame(
            typesCardsSelectedForGame.map {
                typeCardsMapper.mapToData(it)
            }
        )

    override fun getNamesPlayersSelectedForGame(): List<String> =
        sharedPreferencesForGame.readNamesPlayersSelectedForGame()

    override fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>) =
        sharedPreferencesForGame.saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame)

    override fun isFirstLaunch(): Boolean =
        sharedPreferencesForGame.isFirstLaunch()

    override suspend fun getAvatars(): List<Avatar> =
        localDataSource.getAvatars().map{
            avatarsMapper.mapToDomain(it)
        }
}