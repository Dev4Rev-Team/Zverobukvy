package ru.dev4rev.kids.zoobukvy.domain.entity.sound

import ru.dev4rev.kids.zoobukvy.R

enum class VoiceActingStatus(val messageId: Int) {
    SOUND(R.string.voice_acting_status_sound), LETTER(R.string.voice_acting_status_letter), OFF(R.string.voice_acting_status_off)
}