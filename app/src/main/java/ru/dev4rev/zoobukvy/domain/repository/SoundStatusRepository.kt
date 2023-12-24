package ru.dev4rev.zoobukvy.domain.repository

interface SoundStatusRepository {
    fun getSoundStatus(): Boolean

    fun saveSoundStatus(isSoundOn: Boolean)
}