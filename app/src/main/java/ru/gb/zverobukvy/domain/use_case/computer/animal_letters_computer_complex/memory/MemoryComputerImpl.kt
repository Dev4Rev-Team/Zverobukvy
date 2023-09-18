package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory

import ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory.long_memory_computer.LongMemoryComputer

class MemoryComputerImpl(
    slot: Int,
    countLettersOnField: Int,
    percentageRememberLetters: Float,
    triggerThreshold: Float,
) : MemoryComputer {
    private val shortMemoryComputer: MemoryComputer = ShortMemoryComputer(slot)
    private val longMemoryComputer: MemoryComputer = LongMemoryComputer(
        countLettersOnField,
        percentageRememberLetters,
        triggerThreshold
    )

    override fun addLetters(pos: Int) {
        shortMemoryComputer.addLetters(pos)
        longMemoryComputer.addLetters(pos)
    }

    override fun getLearnedLetters(): Set<Int> {
        val learnedLetters1 = shortMemoryComputer.getLearnedLetters()
        val learnedLetters2 = longMemoryComputer.getLearnedLetters()
        return learnedLetters1.plus(learnedLetters2)
    }
}