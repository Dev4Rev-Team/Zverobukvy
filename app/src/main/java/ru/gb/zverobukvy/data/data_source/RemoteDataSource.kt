package ru.gb.zverobukvy.data.data_source

import androidx.annotation.IntRange
import ru.gb.zverobukvy.data.retrofit.AvatarApi

interface RemoteDataSource {

    suspend fun getRandomAvatar(@IntRange(0, 200) scale: Int): String

    suspend fun getRandomAvatars(quantity: Int): List<AvatarApi>
}