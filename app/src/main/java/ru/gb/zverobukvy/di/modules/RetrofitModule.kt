package ru.gb.zverobukvy.di.modules

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.gb.zverobukvy.data.retrofit.RandomAvatarService

@Module
class RetrofitModule {

    @Provides
    fun provideRetrofitService(): RandomAvatarService {
        return Retrofit.Builder()
            .baseUrl("https://api.dicebear.com/7.x/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RandomAvatarService::class.java)
    }
}