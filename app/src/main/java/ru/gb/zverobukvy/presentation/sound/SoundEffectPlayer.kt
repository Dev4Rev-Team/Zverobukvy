package ru.gb.zverobukvy.presentation.sound

interface SoundEffectPlayer {
    fun loadSound(assetPath: Set<String>)
    fun removeSound()
    fun play(soundEnum: SoundEnum)
    fun play(key: String)
}