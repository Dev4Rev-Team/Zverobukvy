package ru.gb.zverobukvy.presentation.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.utility.ui.dipToPixels
import kotlin.properties.Delegates

class CustomLevelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : View(context, attrs, defStyle) {

    private var radiusCard by Delegates.notNull<Int>()
    private var colorMap: MutableMap<TypeCards, Int> = mutableMapOf()
    private var paintMap: MutableMap<TypeCards, Paint> = mutableMapOf()
    private var rectMap: MutableMap<TypeCards, RectF> = mutableMapOf()
    private val typeCards: MutableList<TypeCards> = mutableListOf()

    init {
        initAttributes(context, attrs, defStyle)
        intiBrush()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomLevelView, defStyle, 0)
        radiusCard = typedArray.getDimensionPixelSize(
            R.styleable.CustomLevelView_radiusCards,
            context.dipToPixels(RADIUS_CARDS.toFloat())
        )
        colorMap[TypeCards.ORANGE] = typedArray.getColor(
            R.styleable.CustomLevelView_colorOrange,
            COLOR_ORANGE.toInt()
        )
        colorMap[TypeCards.GREEN] = typedArray.getColor(
            R.styleable.CustomLevelView_colorGreen,
            COLOR_GREEN.toInt()
        )
        colorMap[TypeCards.BLUE] = typedArray.getColor(
            R.styleable.CustomLevelView_colorBlue,
            COLOR_BLUE.toInt()
        )
        colorMap[TypeCards.VIOLET] = typedArray.getColor(
            R.styleable.CustomLevelView_colorViolet,
            COLOR_VIOLET.toInt()
        )
        typedArray.recycle()
    }

    private fun intiBrush() {
        TypeCards.values().forEach { card ->
            paintMap[card] = Paint().apply {
                colorMap[card]?.let { color = it }
                style = Paint.Style.FILL
            }
            rectMap[card] = RectF()
        }
    }

    fun setCards(typeCards: List<TypeCards>) {
        this.typeCards.clear()
        this.typeCards.addAll(typeCards)
        this.typeCards.sort()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        typeCards.forEach { card ->
            rectMap[card]?.let { rect ->
                paintMap[card]?.let { paint ->
                    canvas.drawRoundRect(rect, radiusCard.toFloat(), radiusCard.toFloat(), paint)
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var numCard = 0
        typeCards.forEach { card ->
            rectMap[card]?.let { rectF ->
                rectF.left = 0f
                rectF.top = (h - radiusCard) / typeCards.size * numCard.toFloat()
                rectF.right = w.toFloat()
                rectF.bottom = h.toFloat()
            }
            numCard++
        }
    }

    companion object {
        private const val RADIUS_CARDS = 8
        private const val COLOR_ORANGE = 0xFFFFA063
        private const val COLOR_GREEN = 0xFF8EFF93
        private const val COLOR_BLUE = 0xFF6DD2FF
        private const val COLOR_VIOLET = 0xFFA47AEF
    }
}