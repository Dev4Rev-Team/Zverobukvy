package ru.dev4rev.kids.zoobukvy.domain.repository

interface LoadingDataRepository {
    suspend fun loadingData()
}