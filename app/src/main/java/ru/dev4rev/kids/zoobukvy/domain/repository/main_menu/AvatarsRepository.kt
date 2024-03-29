package ru.dev4rev.kids.zoobukvy.domain.repository.main_menu

import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar

interface AvatarsRepository {
    suspend fun getAvatarsFromLocalDataSource(): List<Avatar>

    suspend fun getAvatarsFromRemoteDataSource(quantities: Int): List <Avatar>

    suspend fun insertAvatar(avatar: Avatar): Long
}