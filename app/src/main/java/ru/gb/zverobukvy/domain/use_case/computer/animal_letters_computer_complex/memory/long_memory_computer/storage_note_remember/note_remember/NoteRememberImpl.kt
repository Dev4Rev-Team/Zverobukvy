package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.long_memory_computer.storage_note_remember.note_remember

class NoteRememberImpl(
    private val pos: Int,
    private var remember: Float = 0f,
) : NoteRemember {
    override fun isNote(position: Int): Boolean {
        return pos == position
    }

    override fun addRemember(power: Float) {
        remember += power
    }

    override fun subRemember(power: Float) {
        remember -= power
        if (remember < 0) {
            remember = 0f
        }
    }

    override fun isRemember(triggerThresholdPower: Float): Boolean {
        return remember > triggerThresholdPower
    }

    override fun getPosition(): Int {
        return pos
    }
}