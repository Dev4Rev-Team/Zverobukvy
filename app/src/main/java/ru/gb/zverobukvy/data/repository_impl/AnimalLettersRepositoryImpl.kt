package ru.gb.zverobukvy.data.repository_impl

import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.mapper.EntitiesMapper
import ru.gb.zverobukvy.data.mapper.EntitiesMapperToDomain
import ru.gb.zverobukvy.data.mapper.LetterCardsMapperToDomain
import ru.gb.zverobukvy.data.mapper.TypeCardsMapper
import ru.gb.zverobukvy.data.mapper.WordCardsMapperToDomain
import ru.gb.zverobukvy.data.preferences.SharedPreferencesForGame
import ru.gb.zverobukvy.data.preferences.TypeCardsInSharedPreferences
import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.entity.WordCard
import ru.gb.zverobukvy.domain.repository.AnimalLettersGameRepository
import ru.gb.zverobukvy.domain.repository.MainMenuRepository

class AnimalLettersRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val sharedPreferencesForGame: SharedPreferencesForGame,
) : AnimalLettersGameRepository, MainMenuRepository {
    private val letterCardsMapper: EntitiesMapperToDomain<List<LetterCard>, List<LetterCardInDatabase>> =
        LetterCardsMapperToDomain()

    private val wordCardsMapper: EntitiesMapperToDomain<List<WordCard>, List<WordCardInDatabase>> =
        WordCardsMapperToDomain()

    private val typeCardsMapper: EntitiesMapper<List<TypeCards>, List<TypeCardsInSharedPreferences>> =
        TypeCardsMapper()

    override suspend fun getLetterCards(): List<LetterCard> =
        letterCardsMapper.mapToDomain(localDataSource.getLetterCards())

    override suspend fun getWordCards(): List<WordCard> =
        wordCardsMapper.mapToDomain(localDataSource.getWordCards())

    override suspend fun getPlayers(): List<Player> = localDataSource.getPlayers()

    override suspend fun deletePlayer(player: Player) {
        localDataSource.deletePlayer(player)
    }

    override suspend fun insertPlayer(player: Player) {
        localDataSource.insertPlayer(player)
    }

    override suspend fun updatePlayer(player: Player) {
        localDataSource.updatePlayer(player)
    }

    override fun getTypesCardsSelectedForGame(): List<TypeCards> =
        typeCardsMapper.mapToDomain(sharedPreferencesForGame.readTypesCardsSelectedForGame())

    override fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCards>) =
        sharedPreferencesForGame.saveTypesCardsSelectedForGame(
            typeCardsMapper.mapToData(
                typesCardsSelectedForGame
            )
        )

    override fun getNamesPlayersSelectedForGame(): List<String> =
        sharedPreferencesForGame.readNamesPlayersSelectedForGame()

    override fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>) =
        sharedPreferencesForGame.saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame)

    override fun isFirstLaunch(): Boolean =
        sharedPreferencesForGame.isFirstLaunch()
}