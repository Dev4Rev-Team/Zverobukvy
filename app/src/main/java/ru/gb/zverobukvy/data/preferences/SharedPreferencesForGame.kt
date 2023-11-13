package ru.gb.zverobukvy.data.preferences

interface SharedPreferencesForGame {
    fun readTypesCardsSelectedForGame(): List<TypeCardsInSharedPreferences>

    fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCardsInSharedPreferences>)

    fun readNamesPlayersSelectedForGame(): List<String>

    fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>)

    fun isFirstLaunch(): Boolean

    fun readSoundStatus(): Boolean

    fun saveSoundStatus(isSoundOn: Boolean)
}