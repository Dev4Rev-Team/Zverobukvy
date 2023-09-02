package ru.gb.zverobukvy.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Path

interface RandomAvatarService {

    @GET("adventurer/jpg?seed={seed}")
    suspend fun getSvgImageEntityBySeed(@Path("seed") seed: String): AvatarApi
}