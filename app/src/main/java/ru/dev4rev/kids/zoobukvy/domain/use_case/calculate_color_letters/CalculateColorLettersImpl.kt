package ru.dev4rev.kids.zoobukvy.domain.use_case.calculate_color_letters

import ru.dev4rev.kids.zoobukvy.data.room.entity.card.LettersColor
import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard

class CalculateColorLettersImpl : CalculateColorLetters {

    override fun calculate(word: String, letterCards: List<LetterCard>) {
        setColor(word, letterCards)
        setSound(letterCards)
    }

    private fun setSound(letterCards: List<LetterCard>) {
        letterCards.forEach { letterCard ->
            when (letterCard.color) {
                LettersColor.Green -> letterCard.apply {
                    soundName = softSoundName ?: baseSoundName
                }

                else -> {
                    letterCard.apply { soundName = baseSoundName }
                }
            }
        }
    }

    private fun setColor(word: String, letterCards: List<LetterCard>) {
        val colorMap = calculateColor(word)
        letterCards.forEach { letterCard ->
            val c = letterCard.letter
            letterCard.color = when (c) {
                in colorMap.keys -> colorMap[c]!!
                in isVowel -> LettersColor.Red
                in isAlwaysSoft -> LettersColor.Green
                in isAlwaysHard -> LettersColor.Blue
                in isNoSound -> LettersColor.Black
                else -> LettersColor.Blue
            }
        }
    }

    fun calculateColor(word: String): Map<Char, LettersColor> {
        val map = mutableMapOf<Char, LettersColor>()
        word.lowercase().forEachIndexed { index, c ->
            val indexNextChar = index + 1
            val isNextChar = indexNextChar < word.length
            map[c] = when {
                c in isVowel -> LettersColor.Red
                c in isAlwaysSoft -> LettersColor.Green
                c in isAlwaysHard -> LettersColor.Blue
                c in isNoSound -> LettersColor.Black
                c == 'н' && isNextChar && word[indexNextChar] in isNSoft -> LettersColor.Green
                c == 'р' && isNextChar && word[indexNextChar] in isPSoft -> LettersColor.Green
                isNextChar && word[indexNextChar] in isPairConsonantSoft -> LettersColor.Green
                else -> LettersColor.Blue
            }
        }
        return map
    }

    companion object {
        private val isVowel = listOf('а', 'о', 'у', 'э', 'ы', 'е', 'ё', 'я', 'ю', 'и')
        private val isAlwaysSoft = listOf('ч', 'й', 'щ')
        private val isAlwaysHard = listOf('ж', 'ш', 'ц')
        private val isNoSound = listOf('ъ', 'ь')
        private val isNSoft = listOf('ч', 'щ')
        private val isPSoft = listOf('щ')
        private val isPairConsonantSoft = listOf('е', 'ё', 'я', 'ю', 'и', 'ь')
    }
}