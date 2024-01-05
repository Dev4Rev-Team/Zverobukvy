package ru.dev4rev.kids.zoobukvy.domain.repository

import ru.dev4rev.kids.zoobukvy.domain.entity.sound.SoundStatus

interface SoundStatusRepository {
    fun getSoundStatus(): SoundStatus

    fun saveSoundStatus(soundStatus: SoundStatus)
}