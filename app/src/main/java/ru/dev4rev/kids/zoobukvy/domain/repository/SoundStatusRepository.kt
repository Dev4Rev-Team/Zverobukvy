package ru.dev4rev.kids.zoobukvy.domain.repository

import ru.dev4rev.kids.zoobukvy.domain.entity.sound.VoiceActingStatus

interface SoundStatusRepository {
    fun getSoundStatus(): Boolean

    fun saveSoundStatus(soundStatus: Boolean)

    fun getVoiceActingStatus(): VoiceActingStatus

    fun saveVoiceActingStatus(voiceActingStatus: VoiceActingStatus)
}