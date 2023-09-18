package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.long_memory_computer.storage_note_remember

interface StorageNoteRemember {
    fun addRemember(pos: Int, power: Float)
    fun subRemember(power: Float)
    fun getRememberPosition(triggerThresholdPower: Float): Set<Int>
}