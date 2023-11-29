package ru.gb.zverobukvy.domain.repository

interface LoadingDataRepository {
    suspend fun loadingData()
}