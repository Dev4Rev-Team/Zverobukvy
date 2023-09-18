package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.long_memory_computer

import ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.MemoryComputer
import ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.long_memory_computer.storage_note_remember.StorageNoteRemember
import ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.long_memory_computer.storage_note_remember.StorageNoteRememberImpl


/**
 * @param countLettersOnField
 * size field
 * @param percentageRememberLetters
 * [0,1] percentage remembers letters on field
 */
class LongMemoryComputer(
    countLettersOnField: Int,
    percentageRememberLetters: Float,
    private val triggerThreshold: Float = 0f
) : MemoryComputer {
    private val lettersRemember: StorageNoteRemember = StorageNoteRememberImpl()
    private val powerMemory = ONE
    private val powerForget = powerMemory / (countLettersOnField * percentageRememberLetters)


    override fun addLetters(pos: Int) {
        lettersRemember.subRemember(powerForget)
        lettersRemember.addRemember(pos, powerMemory)
    }

    override fun getLearnedLetters(): Set<Int> {
        return lettersRemember.getRememberPosition(triggerThreshold)
    }

    companion object {
        private const val ONE = 1f
    }
}

