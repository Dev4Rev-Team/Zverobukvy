package ru.dev4rev.zoobukvy.di.modules

import dagger.Module

@Module(
    includes = [
        MainMenuModule::class,
        SharedPreferencesModule::class,
        NetworkStatusModule::class,
        SoundEffectModule::class,
        AssetImageCashModule::class,
        ViewRatingProviderModule::class,
        CheckDataModule::class,
        ThemeProviderModule::class
    ]
)
interface AppModule