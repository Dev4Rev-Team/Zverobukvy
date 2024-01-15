package ru.dev4rev.kids.zoobukvy.domain.use_case.calculate_color_letters

import ru.dev4rev.kids.zoobukvy.data.room.entity.card.LettersColor
import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard
import ru.dev4rev.kids.zoobukvy.domain.entity.sound.VoiceActingStatus

class CalculateColorLettersImpl : CalculateColorLetters {

    override fun calculate(
        word: String,
        letterCards: List<LetterCard>,
        voiceActingStatus: VoiceActingStatus
    ) {
        when(voiceActingStatus){
            VoiceActingStatus.SOUND -> {
                setColorForRegimeSound(word, letterCards)
                setSoundForRegimeSound(letterCards)
            }
            VoiceActingStatus.LETTER -> {
                setColorForRegimeLetter(letterCards)
                setSoundForLetter(letterCards)
            }
            VoiceActingStatus.OFF -> {
                setColorForRegimeNoSound(letterCards)
            }
        }
    }

    private fun setSoundForRegimeSound(letterCards: List<LetterCard>) {
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

    private fun setSoundForLetter(letterCards: List<LetterCard>) {
        letterCards.forEach { letterCard ->
            letterCard.apply { soundName = letterName }
        }
    }

    private fun setColorForRegimeSound(word: String, letterCards: List<LetterCard>) {
        val colorMap = calculateColorMap(word)
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

    private fun setColorForRegimeLetter(letterCards: List<LetterCard>) {
        letterCards.forEach { letterCard ->
            val c = letterCard.letter
            letterCard.color = when (c) {
                in isVowel -> LettersColor.Red
                in isNoSound -> LettersColor.Black
                else -> LettersColor.Blue
            }
        }
    }

    private fun setColorForRegimeNoSound(letterCards: List<LetterCard>) {
        letterCards.forEach { letterCard ->
            letterCard.color = LettersColor.Black
        }
    }
    fun calculateColorMap(word: String): Map<Char, LettersColor> {
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