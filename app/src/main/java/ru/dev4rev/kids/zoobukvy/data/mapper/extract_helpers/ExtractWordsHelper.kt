package ru.dev4rev.kids.zoobukvy.data.mapper.extract_helpers

object ExtractWordsHelper {
    private const val DELIMITER = " "

    fun extractWords(words: String): List<String> =
        words.split(DELIMITER).toList()
}