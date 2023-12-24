package ru.dev4rev.zoobukvy.presentation.sound

interface SoundEffectPlayer {
    fun play(soundEnum: SoundEnum)
    fun play(key: String)
    fun setEnable(enable:Boolean)
}