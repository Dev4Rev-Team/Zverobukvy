package ru.dev4rev.kids.zoobukvy.data.repository_impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.dev4rev.kids.zoobukvy.data.data_source.LocalDataSource
import ru.dev4rev.kids.zoobukvy.data.room.entity.best_time.BestTimeInDatabase
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.kids.zoobukvy.domain.repository.BestTimeRepository
import javax.inject.Inject

class BestTimeRepositoryImpl @Inject constructor(private val localDataSource: LocalDataSource,): BestTimeRepository {
    override suspend fun getBestTime(typesCards: List<TypeCards>): Pair<Long, String>? =
        withContext(Dispatchers.IO) {
            localDataSource.getBestTimeById(extractTypesCardsId(typesCards))?.let {
                it.time to it.playersName
            }
        }

    override suspend fun saveBestTime(typesCards: List<TypeCards>, bestTime: Pair<Long, String>) {
        withContext(Dispatchers.IO) {
            localDataSource.insertBestTime(
                BestTimeInDatabase(
                    extractTypesCardsId(typesCards),
                    bestTime.second,
                    bestTime.first
                )
            )
        }
    }

    private fun extractTypesCardsId(typesCards: List<TypeCards>): Int {
        var id = 0
        typesCards.forEach {
            id += it.id
        }
        return id
    }
}