package ru.gb.zverobukvy.data.mapper.extract_helpers

object ExtractLettersHelper {
    private const val DELIMITER = ' '

    fun extractLetters(letters: String): List<Char> {
        val chars = letters.toMutableList()
        chars.removeIf { it == DELIMITER }
        return chars
    }
}