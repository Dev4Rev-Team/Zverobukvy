package ru.dev4rev.kids.zoobukvy.data.repository_impl

import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForSoundStatus
import ru.dev4rev.kids.zoobukvy.domain.entity.sound.VoiceActingStatus
import ru.dev4rev.kids.zoobukvy.domain.repository.SoundStatusRepository
import javax.inject.Inject

class SoundStatusRepositoryImpl @Inject constructor(
    private val sharedPreferencesForSoundStatus: SharedPreferencesForSoundStatus
) : SoundStatusRepository {
    override fun getSoundStatus(): Boolean =
        sharedPreferencesForSoundStatus.readSoundStatus()

    override fun saveSoundStatus(soundStatus: Boolean) =
        sharedPreferencesForSoundStatus.saveSoundStatus(soundStatus)

    override fun getVoiceActingStatus(): VoiceActingStatus =
        when (sharedPreferencesForSoundStatus.readVoiceActingStatus()) {
            VoiceActingStatus.SOUND.name -> VoiceActingStatus.SOUND
            VoiceActingStatus.LETTER.name -> VoiceActingStatus.LETTER
            VoiceActingStatus.OFF.name -> VoiceActingStatus.OFF
            else -> VoiceActingStatus.SOUND
        }

    override fun saveVoiceActingStatus(voiceActingStatus: VoiceActingStatus) {
        sharedPreferencesForSoundStatus.saveVoiceActingStatus(voiceActingStatus.name)
    }
}