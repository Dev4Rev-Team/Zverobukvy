package ru.gb.zverobukvy.domain.repository

interface NamesPlayersSelectedForGameRepository {
    fun getNamesPlayersSelectedForGame(): List<String>

    fun saveNamesPlayersSelectedForGame(namesPlayersSelectedForGame: List<String>)
}