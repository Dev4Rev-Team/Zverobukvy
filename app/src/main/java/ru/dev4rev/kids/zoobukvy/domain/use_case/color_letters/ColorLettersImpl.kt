package ru.dev4rev.kids.zoobukvy.domain.use_case.color_letters

import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard

class ColorLettersImpl : ColorLetters {
    override fun setColor(word: String, letterCards: List<LetterCard>) {
        val colorMap = getColor(word)
        letterCards.forEach { letterCard ->
            val c = letterCard.letter
            when {
                c in colorMap.keys -> colorMap[c]?.let { letterCard.colorLetterEnum = it }
                c in isVowel -> letterCard.colorLetterEnum = ColorLetterEnum.RED
                c in isAlwaysSoft -> letterCard.colorLetterEnum = ColorLetterEnum.GREEN
                c in isAlwaysHard -> letterCard.colorLetterEnum = ColorLetterEnum.BLUE
                c in isNoSound -> letterCard.colorLetterEnum = ColorLetterEnum.BLACK

            }
        }
    }

    fun getColor(word: String): Map<Char, ColorLetterEnum> {
        val map = mutableMapOf<Char, ColorLetterEnum>()
        word.lowercase().forEachIndexed { index, c ->
            val indexNextChar = index + 1
            val isNextChar = indexNextChar < word.length
            map[c] = when {
                c in isVowel -> ColorLetterEnum.RED
                c in isAlwaysSoft -> ColorLetterEnum.GREEN
                c in isAlwaysHard -> ColorLetterEnum.BLUE
                c in isNoSound -> ColorLetterEnum.BLACK
                c == 'н' && isNextChar && word[indexNextChar] in isNSoft -> ColorLetterEnum.GREEN
                c == 'р' && isNextChar && word[indexNextChar] in isPSoft -> ColorLetterEnum.GREEN
                isNextChar && word[indexNextChar] in isPairConsonantSoft -> ColorLetterEnum.GREEN
                else -> ColorLetterEnum.BLUE
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