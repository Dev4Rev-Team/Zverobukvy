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
            LetterCard(A, listOf(ORANGE, BLUE, GREEN, VIOLET), faceImageName = A_FACE_IMAGE_NAME),
            LetterCard(B, listOf(BLUE, VIOLET), faceImageName = B_FACE_IMAGE_NAME),
            LetterCard(V, listOf(ORANGE, BLUE), faceImageName = V_FACE_IMAGE_NAME),
            LetterCard(G, listOf(VIOLET), faceImageName = G_FACE_IMAGE_NAME),
            LetterCard(D, listOf(GREEN), faceImageName = D_FACE_IMAGE_NAME),
            LetterCard(E, listOf(ORANGE, BLUE, GREEN, VIOLET), faceImageName = E_FACE_IMAGE_NAME),
            LetterCard(YO, listOf(VIOLET), faceImageName = YO_FACE_IMAGE_NAME),
            LetterCard(ZH, listOf(VIOLET), faceImageName = ZH_FACE_IMAGE_NAME),
            LetterCard(Z, listOf(BLUE, VIOLET), faceImageName = Z_FACE_IMAGE_NAME),
            LetterCard(I, listOf(ORANGE, GREEN, VIOLET), faceImageName = I_FACE_IMAGE_NAME),
            LetterCard(J, listOf(BLUE), faceImageName = J_FACE_IMAGE_NAME),
            LetterCard(K, listOf(BLUE, GREEN, VIOLET), faceImageName = K_FACE_IMAGE_NAME),
            LetterCard(L, listOf(ORANGE, BLUE, GREEN, VIOLET), faceImageName = L_FACE_IMAGE_NAME),
            LetterCard(M, listOf(BLUE, GREEN), faceImageName = M_FACE_IMAGE_NAME),
            LetterCard(N, listOf(ORANGE, BLUE, GREEN), faceImageName = N_FACE_IMAGE_NAME),
            LetterCard(O, listOf(ORANGE, BLUE, GREEN, VIOLET), faceImageName = O_FACE_IMAGE_NAME),
            LetterCard(P, listOf(GREEN), faceImageName = P_FACE_IMAGE_NAME),
            LetterCard(R, listOf(VIOLET), faceImageName = R_FACE_IMAGE_NAME),
            LetterCard(S, listOf(ORANGE, BLUE, GREEN), faceImageName = S_FACE_IMAGE_NAME),
            LetterCard(T, listOf(ORANGE, BLUE, GREEN), faceImageName = T_FACE_IMAGE_NAME),
            LetterCard(U, listOf(BLUE, GREEN, VIOLET), faceImageName = U_FACE_IMAGE_NAME),
            LetterCard(F, listOf(VIOLET), faceImageName = F_FACE_IMAGE_NAME),
            LetterCard(KH, listOf(BLUE, GREEN), faceImageName = KH_FACE_IMAGE_NAME),
            LetterCard(C, listOf(BLUE), faceImageName = C_FACE_IMAGE_NAME),
            LetterCard(CH, listOf(VIOLET), faceImageName = CH_FACE_IMAGE_NAME),
            LetterCard(SH, listOf(GREEN), faceImageName = SH_FACE_IMAGE_NAME),
            LetterCard(SHCH, listOf(VIOLET), faceImageName = SHCH_FACE_IMAGE_NAME),
            LetterCard(HARD_SIGN, listOf(VIOLET), faceImageName = HARD_SIGN_FACE_IMAGE_NAME),
            LetterCard(Y, listOf(GREEN), faceImageName = Y_FACE_IMAGE_NAME),
            LetterCard(SOFT_SIGN, listOf(GREEN), faceImageName = SOFT_SIGN_FACE_IMAGE_NAME),
            LetterCard(EH, listOf(BLUE), faceImageName = EH_FACE_IMAGE_NAME),
            LetterCard(YU, listOf(GREEN, VIOLET), faceImageName = YU_FACE_IMAGE_NAME),
            LetterCard(YA, listOf(BLUE), faceImageName = YA_FACE_IMAGE_NAME)
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
        private const val A_FACE_IMAGE_NAME = "a.png"
        private const val B_FACE_IMAGE_NAME = "b.png"
        private const val V_FACE_IMAGE_NAME = "v.png"
        private const val G_FACE_IMAGE_NAME = "g.png"
        private const val D_FACE_IMAGE_NAME = "d.png"
        private const val E_FACE_IMAGE_NAME = "e.png"
        private const val YO_FACE_IMAGE_NAME = "yo.png"
        private const val ZH_FACE_IMAGE_NAME = "zh.png"
        private const val Z_FACE_IMAGE_NAME = "z.png"
        private const val I_FACE_IMAGE_NAME = "i.png"
        private const val J_FACE_IMAGE_NAME = "j.png"
        private const val K_FACE_IMAGE_NAME = "k.png"
        private const val L_FACE_IMAGE_NAME = "l.png"
        private const val M_FACE_IMAGE_NAME = "m.png"
        private const val N_FACE_IMAGE_NAME = "n.png"
        private const val O_FACE_IMAGE_NAME = "o.png"
        private const val P_FACE_IMAGE_NAME = "p.png"
        private const val R_FACE_IMAGE_NAME = "r.png"
        private const val S_FACE_IMAGE_NAME = "s.png"
        private const val T_FACE_IMAGE_NAME = "t.png"
        private const val U_FACE_IMAGE_NAME = "u.png"
        private const val F_FACE_IMAGE_NAME = "f.png"
        private const val KH_FACE_IMAGE_NAME = "kh.png"
        private const val C_FACE_IMAGE_NAME = "c.png"
        private const val CH_FACE_IMAGE_NAME = "ch.png"
        private const val SH_FACE_IMAGE_NAME = "sh.png"
        private const val SHCH_FACE_IMAGE_NAME = "shch.png"
        private const val HARD_SIGN_FACE_IMAGE_NAME = "hard_sign.png"
        private const val Y_FACE_IMAGE_NAME = "y.png"
        private const val SOFT_SIGN_FACE_IMAGE_NAME = "soft_sign.png"
        private const val EH_FACE_IMAGE_NAME = "eh.png"
        private const val YU_FACE_IMAGE_NAME = "yu.png"
        private const val YA_FACE_IMAGE_NAME = "ya.png"
    }
}