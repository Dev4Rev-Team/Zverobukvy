package ru.gb.zverobukvy.presentation.main_menu.preferences

import android.content.SharedPreferences
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.presentation.MainActivity

class SharedPreferencesForGameImpl: SharedPreferencesForGame {
    private var colorsTypesCardsSelectedForGame: Set<String> = setOf()
    private var namesPlayersSelectedForGame: Set<String> = setOf()
    private val sharedPreferencesForGame: SharedPreferences = MainActivity.sharedPreferences

    override fun readTypesCardsSelectedForGame(): List<TypeCards> =
        (sharedPreferencesForGame.getStringSet(KEY_TYPES_CARDS, null)
            ?: colorsTypesCardsSelectedForGame).toList().map {
            mapTypeCardFromColor(it)
        }

    override fun updateTypesCardsSelectedForGame(newTypesCardsSelectedForGame: List<TypeCards>) {
        colorsTypesCardsSelectedForGame = newTypesCardsSelectedForGame.map{
            mapColorFromTypeCard(it)
        }.toSet()
    }

    override fun readNamesPlayersSelectedForGame(): List<String> =
        (sharedPreferencesForGame.getStringSet(KEY_NAMES_PLAYERS, null)
            ?: namesPlayersSelectedForGame).toList()

    override fun updateNamesPlayersSelectedForGame(newNamesPlayersSelectedForGame: List<String>) {
        namesPlayersSelectedForGame = newNamesPlayersSelectedForGame.toSet()
    }

    override fun savePreferencesForGame() {
        sharedPreferencesForGame.edit()
            .putStringSet(KEY_TYPES_CARDS, colorsTypesCardsSelectedForGame)
            .putStringSet(KEY_NAMES_PLAYERS, namesPlayersSelectedForGame)
            .apply()
    }

    private fun mapTypeCardFromColor(color: String): TypeCards =
        when (color) {
            COLOR_ORANGE -> TypeCards.ORANGE
            COLOR_GREEN -> TypeCards.GREEN
            COLOR_VIOLET -> TypeCards.VIOLET
            COLOR_BLUE -> TypeCards.BLUE
            else -> TypeCards.ORANGE
        }

    private fun mapColorFromTypeCard(typeCard: TypeCards): String =
        when (typeCard) {
            TypeCards.ORANGE -> COLOR_ORANGE
            TypeCards.GREEN -> COLOR_GREEN
            TypeCards.VIOLET -> COLOR_VIOLET
            TypeCards.BLUE -> COLOR_BLUE
        }

    companion object {
        private const val KEY_TYPES_CARDS = "KeyTypesCards"
        private const val KEY_NAMES_PLAYERS = "KeyNamesPlayers"
        private const val COLOR_ORANGE = "orange"
        private const val COLOR_GREEN = "green"
        private const val COLOR_VIOLET = "violet"
        private const val COLOR_BLUE = "blue"
    }
}