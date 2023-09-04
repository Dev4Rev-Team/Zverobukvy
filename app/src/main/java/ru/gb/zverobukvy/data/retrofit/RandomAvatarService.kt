package ru.gb.zverobukvy.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface RandomAvatarService {

    @GET("adventurer/jpg")
    suspend fun getSvgImageEntityBySeed(@Query("seed") seed: String): AvatarApi
}