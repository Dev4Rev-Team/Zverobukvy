package ru.dev4rev.kids.zoobukvy.data.preferences

interface SharedPreferencesForGame {
    fun readTypesCardsSelectedForGame(): List<TypeCardsInSharedPreferences>

    fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCardsInSharedPreferences>)

    fun readNamesPlayersSelectedForGame(): List<String>

    fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>)

    fun isFirstLaunch(): Boolean

    fun readSoundStatus(): Boolean

    fun saveSoundStatus(soundStatus: Boolean)

    fun readVoiceActingStatus(): String?

    fun saveVoiceActingStatus(voiceActingStatus: String)
}