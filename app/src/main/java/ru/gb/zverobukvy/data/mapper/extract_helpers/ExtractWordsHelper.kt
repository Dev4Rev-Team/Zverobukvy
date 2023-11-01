package ru.gb.zverobukvy.data.mapper.extract_helpers

object ExtractWordsHelper {
    private const val DELIMITER = " "

    fun extractWords(words: String): List<String> =
        words.split(DELIMITER).toList()
}