package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.presentation.sound.SoundEffectPlayer
import ru.gb.zverobukvy.presentation.sound.SoundEffectPlayerImpl
import javax.inject.Singleton

@Module
interface SoundEffectModule {

    @Singleton
    @Binds
    fun bindSoundEffectPlayer(soundEffectPlayer: SoundEffectPlayerImpl): SoundEffectPlayer
}