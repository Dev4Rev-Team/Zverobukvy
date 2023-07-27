package ru.gb.zverobukvy.data.data_source_impl

import ru.gb.zverobukvy.data.data_source.WordCardsDB
import ru.gb.zverobukvy.domain.entity.TypeCards.GREEN
import ru.gb.zverobukvy.domain.entity.TypeCards.VIOLET
import ru.gb.zverobukvy.domain.entity.TypeCards.BLUE
import ru.gb.zverobukvy.domain.entity.TypeCards.ORANGE
import ru.gb.zverobukvy.domain.entity.WordCard

open class WordCardsDBImpl : WordCardsDB {
    override suspend fun readWordCards(): List<WordCard> =
        listOf(
            WordCard(PIKE, listOf(VIOLET), url = FAKE_URL),
            WordCard(GOAT, listOf(VIOLET), url = FAKE_URL),
            WordCard(STUMP, listOf(GREEN), url = FAKE_URL),
            WordCard(HAMSTER, listOf(BLUE), url = FAKE_URL),
            WordCard(COCK, listOf(GREEN), url = FAKE_URL),
            WordCard(EMU, listOf(BLUE), url = FAKE_URL),
            WordCard(ROOK, listOf(VIOLET), url = FAKE_URL),
            WordCard(HARE, listOf(BLUE), url = FAKE_URL),
            WordCard(JAY, listOf(BLUE), url = FAKE_URL),
            WordCard(ELEPHANT, listOf(ORANGE, BLUE), url = FAKE_URL),
            WordCard(CRAB, listOf(VIOLET), url = FAKE_URL),
            WordCard(GIRAFFE, listOf(VIOLET), url = FAKE_URL),
            WordCard(FOREST, listOf(ORANGE, GREEN), url = FAKE_URL),
            WordCard(FOX, listOf(ORANGE, GREEN), url = FAKE_URL),
            WordCard(WASP, listOf(ORANGE, BLUE), url = FAKE_URL),
            WordCard(STORK, listOf(ORANGE, GREEN), url = FAKE_URL),
            WordCard(RACCOON, listOf(ORANGE, GREEN), url = FAKE_URL),
            WordCard(OWL, listOf(ORANGE, BLUE), url = FAKE_URL),
            WordCard(ZEBRA, listOf(VIOLET), url = FAKE_URL),
            WordCard(SQUIRREL, listOf(VIOLET), url = FAKE_URL),
            WordCard(SEA_CALF, listOf(GREEN), url = FAKE_URL),
            WordCard(MOUSE, listOf(GREEN), url = FAKE_URL),
            WordCard(TURKEY, listOf(GREEN), url = FAKE_URL),
            WordCard(LILY, listOf(GREEN), url = FAKE_URL),
            WordCard(SHEEP, listOf(BLUE), url = FAKE_URL),
            WordCard(VULTURE, listOf(VIOLET), url = FAKE_URL),
            WordCard(ALOE, listOf(BLUE), url = FAKE_URL),
            WordCard(BUFFALO, listOf(BLUE), url = FAKE_URL),
            WordCard(CHIZH, listOf(VIOLET), url = FAKE_URL),
            WordCard(ECHIDNA, listOf(GREEN), url = FAKE_URL),
            WordCard(HEDGEHOG, listOf(VIOLET), url = FAKE_URL),
            WordCard(LION, listOf(ORANGE, BLUE), url = FAKE_URL),
            WordCard(GOLDFINCH, listOf(VIOLET), url = FAKE_URL)
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
        private const val FAKE_URL = "url"
    }
}