package ru.gb.zverobukvy.data.repository_impl

import ru.gb.zverobukvy.data.data_source.LocalDataSource
import ru.gb.zverobukvy.data.mapper.EntitiesMapper
import ru.gb.zverobukvy.data.room.entity.LetterCardInDatabase
import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.WordCard
import ru.gb.zverobukvy.domain.repository.AnimalLettersCardsRepository
import ru.gb.zverobukvy.domain.repository.PlayersRepository

class AnimalLettersCardsRepositoryImpl (
    private val localDataSource: LocalDataSource,
    private val letterCardsMapper: EntitiesMapper<List<LetterCard>, List<LetterCardInDatabase>>,
    private val wordCardsMapper: EntitiesMapper<List <WordCard>, List<WordCardInDatabase> >
) : AnimalLettersCardsRepository, PlayersRepository {
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
}