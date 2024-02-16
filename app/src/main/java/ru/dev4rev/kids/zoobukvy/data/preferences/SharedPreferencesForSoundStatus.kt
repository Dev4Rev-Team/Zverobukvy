package ru.dev4rev.kids.zoobukvy.data.preferences

interface SharedPreferencesForSoundStatus {
    fun readSoundStatus(): Boolean

    fun saveSoundStatus(soundStatus: Boolean)

    fun readVoiceActingStatus(): String?

    fun saveVoiceActingStatus(voiceActingStatus: String)

}