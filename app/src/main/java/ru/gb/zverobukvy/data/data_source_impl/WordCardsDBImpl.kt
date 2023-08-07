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
            WordCard(PIKE, listOf(VIOLET), faceImageName = PIKE_FACE_IMAGE_NAME),
            WordCard(GOAT, listOf(VIOLET), faceImageName = GOAT_FACE_IMAGE_NAME),
            WordCard(STUMP, listOf(GREEN), faceImageName = STUMP_FACE_IMAGE_NAME),
            WordCard(HAMSTER, listOf(BLUE), faceImageName = HAMSTER_FACE_IMAGE_NAME),
            WordCard(COCK, listOf(GREEN), faceImageName = COCK_FACE_IMAGE_NAME),
            WordCard(EMU, listOf(BLUE), faceImageName = EMU_FACE_IMAGE_NAME),
            WordCard(ROOK, listOf(VIOLET), faceImageName = ROOK_FACE_IMAGE_NAME),
            WordCard(HARE, listOf(BLUE), faceImageName = HARE_FACE_IMAGE_NAME),
            WordCard(JAY, listOf(BLUE), faceImageName = JAY_FACE_IMAGE_NAME),
            WordCard(ELEPHANT, listOf(ORANGE, BLUE), faceImageName = ELEPHANT_FACE_IMAGE_NAME),
            WordCard(CRAB, listOf(VIOLET), faceImageName = CRAB_FACE_IMAGE_NAME),
            WordCard(GIRAFFE, listOf(VIOLET), faceImageName = GIRAFFE_FACE_IMAGE_NAME),
            WordCard(FOREST, listOf(ORANGE, GREEN), faceImageName = FOREST_FACE_IMAGE_NAME),
            WordCard(FOX, listOf(ORANGE, GREEN), faceImageName = FOX_FACE_IMAGE_NAME),
            WordCard(WASP, listOf(ORANGE, BLUE), faceImageName = WASP_FACE_IMAGE_NAME),
            WordCard(STORK, listOf(ORANGE, GREEN), faceImageName = STORK_FACE_IMAGE_NAME),
            WordCard(RACCOON, listOf(ORANGE, GREEN), faceImageName = RACCOON_FACE_IMAGE_NAME),
            WordCard(OWL, listOf(ORANGE, BLUE), faceImageName = OWL_FACE_IMAGE_NAME),
            WordCard(ZEBRA, listOf(VIOLET), faceImageName = ZEBRA_FACE_IMAGE_NAME),
            WordCard(SQUIRREL, listOf(VIOLET), faceImageName = SQUIRREL_FACE_IMAGE_NAME),
            WordCard(SEA_CALF, listOf(GREEN), faceImageName = SEA_CALF_FACE_IMAGE_NAME),
            WordCard(MOUSE, listOf(GREEN), faceImageName = MOUSE_FACE_IMAGE_NAME),
            WordCard(TURKEY, listOf(GREEN), faceImageName = TURKEY_FACE_IMAGE_NAME),
            WordCard(LILY, listOf(GREEN), faceImageName = LILY_FACE_IMAGE_NAME),
            WordCard(SHEEP, listOf(BLUE), faceImageName = SHEEP_FACE_IMAGE_NAME),
            WordCard(VULTURE, listOf(VIOLET), faceImageName = VULTURE_FACE_IMAGE_NAME),
            WordCard(ALOE, listOf(BLUE), faceImageName = ALOE_FACE_IMAGE_NAME),
            WordCard(BUFFALO, listOf(BLUE), faceImageName = BUFFALO_FACE_IMAGE_NAME),
            WordCard(CHIZH, listOf(VIOLET), faceImageName = CHIZH_FACE_IMAGE_NAME),
            WordCard(ECHIDNA, listOf(GREEN), faceImageName = ECHIDNA_FACE_IMAGE_NAME),
            WordCard(HEDGEHOG, listOf(VIOLET), faceImageName = HEDGEHOG_FACE_IMAGE_NAME),
            WordCard(LION, listOf(ORANGE, BLUE), faceImageName = LION_FACE_IMAGE_NAME),
            WordCard(GOLDFINCH, listOf(VIOLET), faceImageName = GOLDFINCH_FACE_IMAGE_NAME)
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
        private const val PIKE_FACE_IMAGE_NAME = "PIKE.webp"
        private const val GOAT_FACE_IMAGE_NAME = "GOAT.webp"
        private const val STUMP_FACE_IMAGE_NAME = "STUMP.webp"
        private const val HAMSTER_FACE_IMAGE_NAME = "HAMSTER.webp"
        private const val COCK_FACE_IMAGE_NAME = "COCK.webp"
        private const val EMU_FACE_IMAGE_NAME = "EMU.webp"
        private const val ROOK_FACE_IMAGE_NAME = "ROOK.webp"
        private const val HARE_FACE_IMAGE_NAME = "HARE.webp"
        private const val JAY_FACE_IMAGE_NAME = "JAY.webp"
        private const val ELEPHANT_FACE_IMAGE_NAME = "ELEPHANT.webp"
        private const val CRAB_FACE_IMAGE_NAME = "CRAB.webp"
        private const val GIRAFFE_FACE_IMAGE_NAME = "GIRAFFE.webp"
        private const val FOREST_FACE_IMAGE_NAME = "FOREST.webp"
        private const val FOX_FACE_IMAGE_NAME = "FOX.webp"
        private const val WASP_FACE_IMAGE_NAME = "WASP.webp"
        private const val STORK_FACE_IMAGE_NAME = "STORK.webp"
        private const val RACCOON_FACE_IMAGE_NAME = "RACCOON.webp"
        private const val OWL_FACE_IMAGE_NAME = "OWL.webp"
        private const val ZEBRA_FACE_IMAGE_NAME = "ZEBRA.webp"
        private const val SQUIRREL_FACE_IMAGE_NAME = "SQUIRREL.webp"
        private const val SEA_CALF_FACE_IMAGE_NAME = "SEA_CALF.webp"
        private const val MOUSE_FACE_IMAGE_NAME = "MOUSE.webp"
        private const val TURKEY_FACE_IMAGE_NAME = "TURKEY.webp"
        private const val LILY_FACE_IMAGE_NAME = "LILY.webp"
        private const val SHEEP_FACE_IMAGE_NAME = "SHEEP.webp"
        private const val VULTURE_FACE_IMAGE_NAME = "VULTURE.webp"
        private const val ALOE_FACE_IMAGE_NAME = "ALOE.webp"
        private const val BUFFALO_FACE_IMAGE_NAME = "BUFFALO.webp"
        private const val CHIZH_FACE_IMAGE_NAME = "CHIZH.webp"
        private const val ECHIDNA_FACE_IMAGE_NAME = "ECHIDNA.webp"
        private const val HEDGEHOG_FACE_IMAGE_NAME = "HEDGEHOG.webp"
        private const val LION_FACE_IMAGE_NAME = "LION.webp"
        private const val GOLDFINCH_FACE_IMAGE_NAME = "GOLDFINCH.webp"
    }
}