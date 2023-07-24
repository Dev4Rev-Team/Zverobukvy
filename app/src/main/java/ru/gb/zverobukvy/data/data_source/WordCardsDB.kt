package ru.gb.zverobukvy.data.data_source

import ru.gb.zverobukvy.data.data_source_impl.IWordCardsDB
import ru.gb.zverobukvy.domain.entity.TypeCards.GREEN
import ru.gb.zverobukvy.domain.entity.TypeCards.VIOLET
import ru.gb.zverobukvy.domain.entity.TypeCards.BLUE
import ru.gb.zverobukvy.domain.entity.TypeCards.ORANGE
import ru.gb.zverobukvy.domain.entity.WordCard

open class WordCardsDB : IWordCardsDB {
    override suspend fun readWordCards(): List<WordCard> =
        listOf(
            WordCard(PIKE, listOf(VIOLET)),
            WordCard(GOAT, listOf(VIOLET)),
            WordCard(STUMP, listOf(GREEN)),
            WordCard(HAMSTER, listOf(BLUE)),
            WordCard(COCK, listOf(GREEN)),
            WordCard(EMU, listOf(BLUE)),
            WordCard(ROOK, listOf(VIOLET)),
            WordCard(HARE, listOf(BLUE)),
            WordCard(JAY, listOf(BLUE)),
            WordCard(ELEPHANT, listOf(ORANGE, BLUE)),
            WordCard(CRAB, listOf(VIOLET)),
            WordCard(GIRAFFE, listOf(VIOLET)),
            WordCard(FOREST, listOf(ORANGE, GREEN)),
            WordCard(FOX, listOf(ORANGE, GREEN)),
            WordCard(WASP, listOf(ORANGE, BLUE)),
            WordCard(STORK, listOf(ORANGE, GREEN)),
            WordCard(RACCOON, listOf(ORANGE, GREEN)),
            WordCard(OWL, listOf(ORANGE, BLUE)),
            WordCard(ZEBRA, listOf(VIOLET)),
            WordCard(SQUIRREL, listOf(VIOLET)),
            WordCard(SEA_CALF, listOf(GREEN)),
            WordCard(MOUSE, listOf(GREEN)),
            WordCard(TURKEY, listOf(GREEN)),
            WordCard(LILY, listOf(GREEN)),
            WordCard(SHEEP, listOf(BLUE)),
            WordCard(VULTURE, listOf(VIOLET)),
            WordCard(ALOE, listOf(BLUE)),
            WordCard(BUFFALO, listOf(BLUE)),
            WordCard(CHIZH, listOf(VIOLET)),
            WordCard(ECHIDNA, listOf(GREEN)),
            WordCard(HEDGEHOG, listOf(VIOLET)),
            WordCard(LION, listOf(ORANGE, BLUE)),
            WordCard(GOLDFINCH, listOf(VIOLET)),
        )

    companion object {
        private const val PIKE = "щука"
        private const val GOAT = "козёл"
        private const val STUMP = "пень"
        private const val HAMSTER = "хомяк"
        private const val COCK = "петух"
        private const val EMU = "эму"
        private const val ROOK = "грач"
        private const val HARE = "заяц"
        private const val JAY = "сойка"
        private const val ELEPHANT = "слон"
        private const val CRAB = "краб"
        private const val GIRAFFE = "жираф"
        private const val FOREST = "лес"
        private const val FOX = "лиса"
        private const val WASP = "оса"
        private const val STORK = "аист"
        private const val RACCOON = "енот"
        private const val OWL = "сова"
        private const val ZEBRA = "зебра"
        private const val SQUIRREL = "белка"
        private const val SEA_CALF = "тюлень"
        private const val MOUSE = "мышь"
        private const val TURKEY = "индюк"
        private const val LILY = "ландыш"
        private const val SHEEP = "овца"
        private const val VULTURE = "гриф"
        private const val ALOE = "алоэ"
        private const val BUFFALO = "буйвол"
        private const val CHIZH = "чиж"
        private const val ECHIDNA = "ехидна"
        private const val HEDGEHOG = "ёж"
        private const val LION = "лев"
        private const val GOLDFINCH = "щегол"
    }
}