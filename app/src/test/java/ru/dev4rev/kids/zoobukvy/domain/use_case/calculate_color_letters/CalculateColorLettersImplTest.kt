package ru.dev4rev.kids.zoobukvy.domain.use_case.calculate_color_letters

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.dev4rev.kids.zoobukvy.data.room.entity.card.LettersColor

class CalculateColorLettersImplTest {

    private val calculateColorLetters:CalculateColorLettersImpl = CalculateColorLettersImpl()
    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getColor() {
        var word = "кот"
        var resultExpected = mapOf(
            ('к' to LettersColor.Blue),
            ('о' to LettersColor.Red),
            ('т' to LettersColor.Blue),
        )
        assertEquals(resultExpected, calculateColorLetters.calculateColor(word))

         word = "объявление"
         resultExpected = mapOf(
            ('о' to LettersColor.Red),
            ('б' to LettersColor.Blue),
            ('ъ' to LettersColor.Black),
            ('я' to LettersColor.Red),
            ('в' to LettersColor.Blue),
            ('л' to LettersColor.Green),
            ('е' to LettersColor.Red),
            ('н' to LettersColor.Green),
            ('и' to LettersColor.Red),
            ('е' to LettersColor.Red),
        )
        assertEquals(resultExpected, calculateColorLetters.calculateColor(word))

         word = "фонетический"
         resultExpected = mapOf(
            ('ф' to LettersColor.Blue),
            ('о' to LettersColor.Red),
            ('н' to LettersColor.Green),
            ('е' to LettersColor.Red),
            ('т' to LettersColor.Green),
            ('и' to LettersColor.Red),
            ('ч' to LettersColor.Green),
            ('е' to LettersColor.Red),
            ('с' to LettersColor.Blue),
            ('к' to LettersColor.Green),
            ('и' to LettersColor.Red),
            ('й' to LettersColor.Green),
        )
        assertEquals(resultExpected, calculateColorLetters.calculateColor(word))

    }

    @Test
    fun setColor() {

    }


}