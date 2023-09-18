package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.memory

class ShortMemoryComputer(private val slot: Int = 3) : MemoryComputer {
    private val lettersRemember: MutableList<Int> = mutableListOf()

    override fun addLetters(pos: Int) {
        if (pos >= 0) {
            lettersRemember.add(pos)
        }
        if (lettersRemember.size >= slot) {
            lettersRemember.removeLast()
        }
    }

    override fun getLearnedLetters(): Set<Int> {
        return lettersRemember.toSet()
    }
}