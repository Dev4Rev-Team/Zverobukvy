package ru.gb.zverobukvy.data.preferences

interface SharedPreferencesForGame {
    fun readTypesCardsSelectedForGame(): List<TypeCardsInSharedPreferences>

    fun saveTypesCardsSelectedForGame(typesCardsSelectedForGame: List<TypeCardsInSharedPreferences>)

    fun readNamesPlayersSelectedForGame(): List<String>

    fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>)

}