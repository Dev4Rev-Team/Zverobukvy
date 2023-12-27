package ru.dev4rev.kids.zoobukvy.data.mapper.extract_helpers

import android.os.Build

object ExtractLettersHelper {
    private const val DELIMITER = ' '

    fun extractLetters(letters: String): List<Char> {
        val chars = letters.toMutableList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            chars.removeIf { it == DELIMITER }
        else
            chars.removeAll { it == DELIMITER }
        return chars
    }
}