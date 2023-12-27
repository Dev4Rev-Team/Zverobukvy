package ru.dev4rev.kids.zoobukvy.domain.repository

interface SoundStatusRepository {
    fun getSoundStatus(): Boolean

    fun saveSoundStatus(isSoundOn: Boolean)
}