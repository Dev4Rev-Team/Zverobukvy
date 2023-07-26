package ru.gb.zverobukvy.data.data_source_impl

import ru.gb.zverobukvy.data.data_source.LetterCardsDB
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.TypeCards.GREEN
import ru.gb.zverobukvy.domain.entity.TypeCards.VIOLET
import ru.gb.zverobukvy.domain.entity.TypeCards.BLUE
import ru.gb.zverobukvy.domain.entity.TypeCards.ORANGE

open class LetterCardsDBImpl : LetterCardsDB {
    override suspend fun readLetterCards(): List<LetterCard> =
        listOf(
            LetterCard(A, listOf(ORANGE, BLUE, GREEN, VIOLET), url = FAKE_URL),
            LetterCard(B, listOf(BLUE, VIOLET), url = FAKE_URL),
            LetterCard(V, listOf(ORANGE, BLUE), url = FAKE_URL),
            LetterCard(G, listOf(VIOLET), url = FAKE_URL),
            LetterCard(D, listOf(GREEN), url = FAKE_URL),
            LetterCard(E, listOf(ORANGE, BLUE, GREEN, VIOLET), url = FAKE_URL),
            LetterCard(YO, listOf(VIOLET), url = FAKE_URL),
            LetterCard(ZH, listOf(VIOLET), url = FAKE_URL),
            LetterCard(Z, listOf(BLUE, VIOLET), url = FAKE_URL),
            LetterCard(I, listOf(ORANGE, GREEN, VIOLET), url = FAKE_URL),
            LetterCard(J, listOf(BLUE), url = FAKE_URL),
            LetterCard(K, listOf(BLUE, GREEN, VIOLET), url = FAKE_URL),
            LetterCard(L, listOf(ORANGE, BLUE, GREEN, VIOLET), url = FAKE_URL),
            LetterCard(M, listOf(BLUE, GREEN), url = FAKE_URL),
            LetterCard(N, listOf(ORANGE, BLUE, GREEN), url = FAKE_URL),
            LetterCard(O, listOf(ORANGE, BLUE, GREEN, VIOLET), url = FAKE_URL),
            LetterCard(P, listOf(GREEN), url = FAKE_URL),
            LetterCard(R, listOf(VIOLET), url = FAKE_URL),
            LetterCard(S, listOf(ORANGE, BLUE, GREEN), url = FAKE_URL),
            LetterCard(T, listOf(ORANGE, BLUE, GREEN), url = FAKE_URL),
            LetterCard(U, listOf(BLUE, GREEN, VIOLET), url = FAKE_URL),
            LetterCard(F, listOf(VIOLET), url = FAKE_URL),
            LetterCard(KH, listOf(BLUE, GREEN), url = FAKE_URL),
            LetterCard(C, listOf(BLUE), url = FAKE_URL),
            LetterCard(CH, listOf(VIOLET), url = FAKE_URL),
            LetterCard(SH, listOf(GREEN), url = FAKE_URL),
            LetterCard(SHCH, listOf(VIOLET), url = FAKE_URL),
            LetterCard(HARD_SIGN, listOf(VIOLET), url = FAKE_URL),
            LetterCard(Y, listOf(GREEN), url = FAKE_URL),
            LetterCard(SOFT_SIGN, listOf(GREEN), url = FAKE_URL),
            LetterCard(EH, listOf(BLUE), url = FAKE_URL),
            LetterCard(YU, listOf(GREEN, VIOLET), url = FAKE_URL),
            LetterCard(YA, listOf(BLUE), url = FAKE_URL)
        )

    companion object {
        private const val A = 'а'
        private const val B = 'б'
        private const val V = 'в'
        private const val G = 'г'
        private const val D = 'д'
        private const val E = 'е'
        private const val YO = 'ё'
        private const val ZH = 'ж'
        private const val Z = 'з'
        private const val I = 'и'
        private const val J = 'й'
        private const val K = 'к'
        private const val L = 'л'
        private const val M = 'м'
        private const val N = 'н'
        private const val O = 'о'
        private const val P = 'п'
        private const val R = 'р'
        private const val S = 'с'
        private const val T = 'т'
        private const val U = 'у'
        private const val F = 'ф'
        private const val KH = 'х'
        private const val C = 'ц'
        private const val CH = 'ч'
        private const val SH = 'ш'
        private const val SHCH = 'щ'
        private const val HARD_SIGN = 'ъ'
        private const val Y = 'ы'
        private const val SOFT_SIGN = 'ь'
        private const val EH = 'э'
        private const val YU = 'ю'
        private const val YA = 'я'
        private const val FAKE_URL = "url"
    }
}