package ru.dev4rev.zoobukvy.di.modules

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.dev4rev.zoobukvy.data.retrofit.RandomAvatarService
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): RandomAvatarService {
        return Retrofit.Builder()
            .baseUrl("https://api.dicebear.com/7.x/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(RandomAvatarService::class.java)
    }
}