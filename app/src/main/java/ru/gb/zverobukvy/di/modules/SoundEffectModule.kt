package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.presentation.sound.SoundEffectPlayer
import ru.gb.zverobukvy.presentation.sound.SoundEffectPlayerImpl

@Module
interface SoundEffectModule {

    @Binds
    fun bindSoundEffectPlayer(soundEffectPlayer: SoundEffectPlayerImpl): SoundEffectPlayer
}