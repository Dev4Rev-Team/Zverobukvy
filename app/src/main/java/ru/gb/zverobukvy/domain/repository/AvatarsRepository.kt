package ru.gb.zverobukvy.domain.repository

import ru.gb.zverobukvy.domain.entity.Avatar

interface AvatarsRepository {
    suspend fun getAvatars(): List<Avatar>
}