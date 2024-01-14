package ru.dev4rev.kids.zoobukvy.domain.use_case.calculate_color_letters

import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard
import ru.dev4rev.kids.zoobukvy.domain.entity.sound.VoiceActingStatus

interface CalculateColorLetters {
    fun calculate(
        word: String,
        letterCards: List<LetterCard>,
        voiceActingStatus: VoiceActingStatus = VoiceActingStatus.SOUND
    )
}

