package ru.dev4rev.kids.zoobukvy.data.repository_impl

import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForGame
import ru.dev4rev.kids.zoobukvy.domain.entity.sound.VoiceActingStatus
import ru.dev4rev.kids.zoobukvy.domain.repository.SoundStatusRepository
import javax.inject.Inject

class SoundStatusRepositoryImpl @Inject constructor(
    private val sharedPreferencesForGame: SharedPreferencesForGame
) : SoundStatusRepository {
    override fun getSoundStatus(): Boolean =
        sharedPreferencesForGame.readSoundStatus()

    override fun saveSoundStatus(soundStatus: Boolean) =
        sharedPreferencesForGame.saveSoundStatus(soundStatus)

    override fun getVoiceActingStatus(): VoiceActingStatus =
        when (sharedPreferencesForGame.readVoiceActingStatus()) {
            VoiceActingStatus.SOUND.name -> VoiceActingStatus.SOUND
            VoiceActingStatus.LETTER.name -> VoiceActingStatus.LETTER
            VoiceActingStatus.OFF.name -> VoiceActingStatus.OFF
            else -> VoiceActingStatus.SOUND
        }

    override fun saveVoiceActingStatus(voiceActingStatus: VoiceActingStatus) {
        sharedPreferencesForGame.saveVoiceActingStatus(voiceActingStatus.name)
    }
}