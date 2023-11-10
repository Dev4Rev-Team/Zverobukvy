package ru.gb.zverobukvy.di.modules

import dagger.Module

@Module(
    includes = [
        MainMenuModule::class,
        SharedPreferencesModule::class,
        NetworkStatusModule::class,
        SoundEffectModule::class,
        AssetImageCashModule::class,
        ViewRatingProviderModule::class
    ]
)
interface AppModule