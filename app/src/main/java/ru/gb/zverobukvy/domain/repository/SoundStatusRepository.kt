package ru.gb.zverobukvy.domain.repository

interface SoundStatusRepository {
    fun getSoundStatus(): Boolean

    fun saveSoundStatus(isSoundOn: Boolean)
}