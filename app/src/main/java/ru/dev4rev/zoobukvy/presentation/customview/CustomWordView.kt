package ru.dev4rev.zoobukvy.presentation.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import ru.dev4rev.zoobukvy.R
import ru.dev4rev.zoobukvy.utility.ui.dipToPixels
import kotlin.properties.Delegates


class CustomWordView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : ConstraintLayout(context, attrs, defStyle) {

    private var horizontalGap by Delegates.notNull<Int>()
    private var flow: Flow? = null
    private val listOfLetterView = mutableListOf<CustomLetterView>()
    private var colorGuessed by Delegates.notNull<Int>()
    private var colorUnsolved by Delegates.notNull<Int>()
    private var radiusCard by Delegates.notNull<Int>()

    init {
        initAttributes(context, attrs, defStyle)
        initContentView()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomWordView, defStyle, 0)
        horizontalGap = typedArray.getDimensionPixelSize(
            R.styleable.CustomWordView_horizontalGap,
            context.dipToPixels(HORIZONTAL_GAP.toFloat())
        )
        colorGuessed = typedArray.getColor(
            R.styleable.CustomWordView_colorGuessed,
            COLOR_GUESSED
        )
        colorUnsolved = typedArray.getColor(
            R.styleable.CustomWordView_colorUnsolved,
            COLOR_UNSOLVED
        )
        radiusCard = typedArray.getDimensionPixelSize(
            R.styleable.CustomWordView_radiusCards,
            context.dipToPixels(RADIUS_CARD.toFloat())
        )
        typedArray.recycle()
    }

    private fun initContentView() {
        val padding = horizontalGap + SHIFT_PADDING
        setPadding(0, padding / 2, 0, padding / 2)
        clipToPadding = false
        clipChildren = false
    }

    private fun createNewFlow(countLetter: Int) {
        if (flow != null) {
            removeAllViews()
        }
        flow = Flow(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setHorizontalGap(horizontalGap)

            setMaxElementsWrap(countLetter)
            setWrapMode(Flow.WRAP_ALIGNED)
            setHorizontalStyle(Flow.CHAIN_PACKED)
            setVerticalStyle(Flow.CHAIN_PACKED)
        }
        addView(flow)
    }

    fun setWord(
        word: WordCardUI,
        factory: (() -> CustomLetterView)? = null,
    ) {
        listOfLetterView.clear()
        val countLetter = word.word.length
        createNewFlow(countLetter)

        repeat(countLetter) { pos: Int ->
            val customLetter = (factory?.run { invoke() }
                ?: CustomLetterView(context)).apply {
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, 0)
                id = generateViewId()
                colorGuessed = this@CustomWordView.colorGuessed
                colorUnsolved = this@CustomWordView.colorUnsolved
                radius = radiusCard.toFloat()

                setChar(word.word[pos].uppercaseChar())
                if (word.positionsGuessedLetters.contains(pos)) {
                    setGuessed(false)
                } else {
                    setUnsolved()
                }
            }

            this@CustomWordView.addView(customLetter, SHIFT_PADDING + pos)
            flow?.addView(customLetter)
            listOfLetterView.add(customLetter)
        }
    }

    fun setPositionLetterInWord(pos: Int) {
        listOfLetterView[pos].setGuessed()
    }

    companion object {
        private const val COLOR_GUESSED = Color.GREEN
        private const val COLOR_UNSOLVED = Color.WHITE
        private const val RADIUS_CARD = 8
        private const val SHIFT_PADDING = 1
        private const val HORIZONTAL_GAP = 24
    }


    interface WordCardUI {
        val word: String
        val positionsGuessedLetters: MutableList<Int>
        val soundName: String
    }
}