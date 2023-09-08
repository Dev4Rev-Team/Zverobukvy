package ru.gb.zverobukvy.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface RandomAvatarService {
    @Headers("Content-Type: image/svg+xml")
    @GET("adventurer/svg")
    suspend fun getSvgImageEntityBySeed(@QueryMap options: Map<String, String>): String
}