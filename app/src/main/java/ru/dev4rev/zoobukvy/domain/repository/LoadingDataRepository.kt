package ru.dev4rev.zoobukvy.domain.repository

interface LoadingDataRepository {
    suspend fun loadingData()
}