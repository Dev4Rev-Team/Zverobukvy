package ru.gb.zverobukvy.presentation.main_menu.preferences

import ru.gb.zverobukvy.domain.entity.TypeCards

interface SharedPreferencesForGame {
    fun readTypesCardsSelectedForGame(): List<TypeCards>

    fun updateTypesCardsSelectedForGame(newTypesCardsSelectedForGame: List<TypeCards>)

    fun readNamesPlayersSelectedForGame(): List<String>

    fun updateNamesPlayersSelectedForGame(newNamesPlayersSelectedForGame: List<String>)

    fun savePreferencesForGame()
}