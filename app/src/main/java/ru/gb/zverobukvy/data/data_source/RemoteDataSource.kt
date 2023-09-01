package ru.gb.zverobukvy.data.data_source

import ru.gb.zverobukvy.data.retrofit.AvatarImageEntity

interface RemoteDataSource {

    suspend fun getRandomAvatar(): AvatarImageEntity

    suspend fun getRandomAvatars(quantity: Int): List<AvatarImageEntity>
}