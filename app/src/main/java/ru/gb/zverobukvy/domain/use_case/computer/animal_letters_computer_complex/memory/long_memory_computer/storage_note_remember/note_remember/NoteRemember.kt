package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.long_memory_computer.storage_note_remember.note_remember

interface NoteRemember {
    fun isNote(position: Int): Boolean
    fun addRemember(power: Float)
    fun subRemember(power: Float)
    fun isRemember(triggerThresholdPower: Float): Boolean
    fun getPosition(): Int
}