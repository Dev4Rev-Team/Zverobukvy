package ru.dev4rev.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.zoobukvy.presentation.sound.SoundEffectPlayer
import ru.dev4rev.zoobukvy.presentation.sound.SoundEffectPlayerImpl
import javax.inject.Singleton

@Module
interface SoundEffectModule {

    @Singleton
    @Binds
    fun bindSoundEffectPlayer(soundEffectPlayer: SoundEffectPlayerImpl): SoundEffectPlayer
}