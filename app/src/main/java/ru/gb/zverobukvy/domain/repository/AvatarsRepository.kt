package ru.gb.zverobukvy.domain.repository

import ru.gb.zverobukvy.domain.entity.Avatar

interface AvatarsRepository {
    suspend fun getAvatarsFromLocalDataSource(): List<Avatar>

    suspend fun getAvatarsFromRemoteDataSource(quantities: Int): List <Avatar>
}