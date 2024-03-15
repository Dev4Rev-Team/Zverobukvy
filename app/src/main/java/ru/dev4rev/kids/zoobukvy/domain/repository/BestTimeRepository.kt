package ru.dev4rev.kids.zoobukvy.domain.repository

import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards

interface BestTimeRepository {
    suspend fun getBestTime(typesCards: List<TypeCards>): Pair<Long, String>?

    suspend fun saveBestTime(typesCards: List<TypeCards>, bestTime: Pair<Long, String>)
}