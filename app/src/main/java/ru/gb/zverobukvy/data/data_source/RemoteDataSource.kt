package ru.gb.zverobukvy.data.data_source

import ru.gb.zverobukvy.data.retrofit.AvatarApi

interface RemoteDataSource {

    suspend fun getRandomAvatar(): String

    suspend fun getRandomAvatars(quantity: Int): List<AvatarApi>
}