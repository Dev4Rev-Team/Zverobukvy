package ru.gb.zverobukvy.domain.repository.main_menu.shared_preferences

interface NamesPlayersSelectedForGameRepository {
    fun getNamesPlayersSelectedForGame(): List<String>

    fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>)
}