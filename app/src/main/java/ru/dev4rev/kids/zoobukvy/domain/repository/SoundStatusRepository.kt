package ru.dev4rev.kids.zoobukvy.domain.repository

import ru.dev4rev.kids.zoobukvy.domain.entity.sound.SoundStatus
import ru.dev4rev.kids.zoobukvy.domain.entity.sound.VoiceActingStatus

interface SoundStatusRepository {
    fun getSoundStatus(): SoundStatus

    fun saveSoundStatus(soundStatus: SoundStatus)

    fun getVoiceActingStatus(): VoiceActingStatus

    fun saveVoiceActingStatus(voiceActingStatus: VoiceActingStatus)
}