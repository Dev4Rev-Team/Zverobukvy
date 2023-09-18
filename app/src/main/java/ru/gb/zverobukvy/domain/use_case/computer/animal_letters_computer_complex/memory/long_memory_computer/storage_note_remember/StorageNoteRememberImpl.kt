package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.long_memory_computer.storage_note_remember

import ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.long_memory_computer.storage_note_remember.note_remember.NoteRemember
import ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.long_memory_computer.storage_note_remember.note_remember.NoteRememberImpl

class StorageNoteRememberImpl() : StorageNoteRemember {
    private val notes: MutableSet<NoteRemember> = mutableSetOf()

    override fun addRemember(pos: Int, power: Float) {
        if (notes.none { it.isNote(pos) }) {
            notes.add(NoteRememberImpl(pos, 0f))
        }
        val noteOne = notes.first { it.isNote(pos) }
        noteOne.addRemember(power)
    }

    override fun subRemember(power: Float) {
        notes.forEach {
            it.subRemember(power)
        }
    }

    override fun getRememberPosition(triggerThresholdPower: Float): Set<Int> {
        return notes.filter { it.isRemember(triggerThresholdPower) }
            .map { it.getPosition() }
            .toSet()
    }
}