package ru.gb.zverobukvy.presentation.customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.max

class CustomWordView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,

    ) : ConstraintLayout(context, attrs, defStyle) {

    private var horizontalGap = HORIZONTAL_GAP
    private var verticalGap = VERTICAL_GAP
    private var flow: Flow? = null
    private val listOfLetterView = mutableListOf<CustomLetterView>()


    init {
        initContentView()
    }

    private fun initContentView() {
        val padding = max(horizontalGap, verticalGap)
        setPadding(padding, padding, padding, padding)
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
            setVerticalGap(verticalGap)

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
            val customLetter = (factory?.run { invoke() } ?: CustomLetterView(context)).apply {
                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, 0)
                id = generateViewId()

                setChar(word.word[pos])
                if (word.positionsGuessedLetters.contains(pos)) {
                    setTrue(false)
                }
            }

            this@CustomWordView.addView(customLetter, 1 + pos)
            flow?.addView(customLetter)
            listOfLetterView.add(customLetter)
        }
    }

    fun setPositionLetterInWord(pos: Int) {
        listOfLetterView[pos].setTrue()
    }


    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyle: Int) {
//        val typedArray =
//            context.obtainStyledAttributes(attrs, R.styleable.CustomCardTable, defStyle, 0)
//        horizontalGap = typedArray.getInteger(
//            R.styleable.CustomCardTable_horizontalGap,
//            CustomWordView.HORIZONTAL_GAP
//        )
//        verticalGap = typedArray.getInteger(
//            R.styleable.CustomCardTable_verticalGap,
//            CustomWordView.VERTICAL_GAP
//        )
//        typedArray.recycle()
    }

    companion object {
        private const val HORIZONTAL_GAP = 24
        private const val VERTICAL_GAP = 24
    }


    interface WordCardUI {
        val word: String
        val positionsGuessedLetters: MutableList<Int>
    }
}