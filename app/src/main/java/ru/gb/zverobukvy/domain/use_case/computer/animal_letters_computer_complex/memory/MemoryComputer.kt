package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory

interface MemoryComputer {
    fun addLetters(pos: Int)
    fun getLearnedLetters(): Set<Int>
}