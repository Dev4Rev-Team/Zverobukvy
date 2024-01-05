package ru.dev4rev.kids.zoobukvy.data.repository_impl

import ru.dev4rev.kids.zoobukvy.data.preferences.SharedPreferencesForGame
import ru.dev4rev.kids.zoobukvy.domain.entity.sound.SoundStatus
import ru.dev4rev.kids.zoobukvy.domain.repository.SoundStatusRepository
import javax.inject.Inject

class SoundStatusRepositoryImpl @Inject constructor(
    private val sharedPreferencesForGame: SharedPreferencesForGame
) : SoundStatusRepository {
    override fun getSoundStatus(): SoundStatus =
        when (sharedPreferencesForGame.readSoundStatus()) {
            SoundStatus.ON.name -> SoundStatus.ON
            SoundStatus.ON_OFF.name -> SoundStatus.ON_OFF
            SoundStatus.OFF.name -> SoundStatus.OFF
            else -> SoundStatus.ON
        }

    override fun saveSoundStatus(soundStatus: SoundStatus) =
        sharedPreferencesForGame.saveSoundStatus(soundStatus.name)
}