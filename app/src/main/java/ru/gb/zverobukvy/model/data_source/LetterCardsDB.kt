package ru.gb.zverobukvy.model.data_source

import ru.gb.zverobukvy.model.dto.LetterCard
import ru.gb.zverobukvy.model.dto.TypeCards.GREEN
import ru.gb.zverobukvy.model.dto.TypeCards.VIOLET
import ru.gb.zverobukvy.model.dto.TypeCards.BLUE
import ru.gb.zverobukvy.model.dto.TypeCards.ORANGE

open class LetterCardsDB : ILetterCardsDB {
    override fun readLetterCards(): List<LetterCard> =
        listOf(
            LetterCard(A, listOf(ORANGE, BLUE, GREEN, VIOLET)),
            LetterCard(B, listOf(BLUE, VIOLET)),
            LetterCard(V, listOf(ORANGE, BLUE)),
            LetterCard(G, listOf(VIOLET)),
            LetterCard(D, listOf(GREEN)),
            LetterCard(E, listOf(ORANGE, BLUE, GREEN, VIOLET)),
            LetterCard(YO, listOf(VIOLET)),
            LetterCard(ZH, listOf(VIOLET)),
            LetterCard(Z, listOf(BLUE, VIOLET)),
            LetterCard(I, listOf(ORANGE, GREEN, VIOLET)),
            LetterCard(J, listOf(BLUE)),
            LetterCard(K, listOf(BLUE, GREEN, VIOLET)),
            LetterCard(L, listOf(ORANGE, BLUE, GREEN, VIOLET)),
            LetterCard(M, listOf(BLUE, GREEN)),
            LetterCard(N, listOf(ORANGE, BLUE, GREEN)),
            LetterCard(O, listOf(ORANGE, BLUE, GREEN, VIOLET)),
            LetterCard(P, listOf(GREEN)),
            LetterCard(R, listOf(VIOLET)),
            LetterCard(S, listOf(ORANGE, BLUE, GREEN)),
            LetterCard(T, listOf(ORANGE, BLUE, GREEN)),
            LetterCard(U, listOf(BLUE, GREEN, VIOLET)),
            LetterCard(F, listOf(VIOLET)),
            LetterCard(KH, listOf(BLUE, GREEN)),
            LetterCard(C, listOf(BLUE)),
            LetterCard(CH, listOf(VIOLET)),
            LetterCard(SH, listOf(GREEN)),
            LetterCard(SHCH, listOf(VIOLET)),
            LetterCard(HARD_SIGN, listOf(VIOLET)),
            LetterCard(Y, listOf(GREEN)),
            LetterCard(SOFT_SIGN, listOf(GREEN)),
            LetterCard(EH, listOf(BLUE)),
            LetterCard(YU, listOf(GREEN, VIOLET)),
            LetterCard(YA, listOf(BLUE))
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
    }
}