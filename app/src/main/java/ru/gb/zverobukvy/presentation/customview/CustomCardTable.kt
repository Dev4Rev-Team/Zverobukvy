package ru.gb.zverobukvy.presentation.customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import ru.gb.zverobukvy.R
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.sqrt

class CustomCardTable @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,

    ) : ConstraintLayout(context, attrs, defStyle) {

    private var maxNumberOfCardsHorizontally = 0
    private var horizontalGap = 0
    private var verticalGap = 0
    private var flow: Flow? = null
    private var click: ((pos: Int) -> Unit)? = null
    private var isClick = false
    private var listLetterCards: List<LetterCard>? = null
    private val listOfCardsOnTable = mutableListOf<CustomCard>()
    private val listOfInvalidCards = mutableListOf<CustomCard>()

    init {
        initAttributes(context, attrs, defStyle)
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomCardTable, defStyle, 0)
        maxNumberOfCardsHorizontally = typedArray.getInteger(
            R.styleable.CustomCardTable_maxNumberOfCardsHorizontally,
            MAX_NUMBER_OF_CARDS_HORIZONTALLY
        )
        horizontalGap = typedArray.getInteger(
            R.styleable.CustomCardTable_horizontalGap,
            HORIZONTAL_GAP
        )
        verticalGap = typedArray.getInteger(
            R.styleable.CustomCardTable_verticalGap,
            VERTICAL_GAP
        )
        typedArray.recycle()
    }

    private fun createNewFlow(countCard: Int) {
        if (flow != null) {
            removeAllViews()
        }
        flow = Flow(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setHorizontalGap(horizontalGap)
            setVerticalGap(verticalGap)
            setMaxElementsWrap(
                min(
                    ceil(sqrt(countCard.toDouble())).toInt(),
                    maxNumberOfCardsHorizontally
                )
            )
            setWrapMode(Flow.WRAP_ALIGNED)
            setHorizontalStyle(Flow.CHAIN_PACKED)
            setVerticalStyle(Flow.CHAIN_PACKED)
        }
        addView(flow)
    }


    fun setListItem(
        list: List<LetterCard>,
        srcClose: String,
        factory: (() -> CustomCard)? = null,
    ) {
        listOfCardsOnTable.clear()
        listLetterCards = list
        val countCard = list.count()
        createNewFlow(countCard)

        repeat(countCard) { pos: Int ->
            val customCard = (factory?.run { invoke() } ?: CustomCard(context)).apply {
                layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
                id = generateViewId()

                val letterCard = list[pos]
                setOpenCard(letterCard.isVisible)
                setSrcFromAssert(letterCard.url, srcClose)

                setOnClickCardListener(pos) {
                    if (!isClick) {
                        isClick = false
                        updateView(pos)
                        click?.run { invoke(it) }
                    }

                }
            }

            this@CustomCardTable.addView(customCard, 1 + pos)
            flow?.addView(customCard)
            listOfCardsOnTable.add(customCard)
        }
    }

    private fun updateView(pos: Int) {
        listOfCardsOnTable[pos].setOpenCard(true)
    }

    fun setCorrectLetterCard(card: LetterCard) {
        isClick = true
    }

    fun setInvalidLetterCard(letterCard: LetterCard) {
        listLetterCards?.indexOf(letterCard)?.let {
            listOfInvalidCards.add(listOfCardsOnTable[it])
        }
        isClick = false
    }

    fun nextPlayer() {
        listOfInvalidCards.forEach {
            it.setOpenCard(false)
        }
        isClick = true
    }

    fun nextWord() {
        isClick = true
        listOfCardsOnTable.forEach { it.setOpenCard(false) }
    }

    companion object {
        const val MAX_NUMBER_OF_CARDS_HORIZONTALLY = 5
        const val HORIZONTAL_GAP = 24
        const val VERTICAL_GAP = 24
    }

    interface Card {
        val url: Int
    }

    data class LetterCard(
        val letter: Char,
        var isVisible: Boolean = false,
        val url: String,
    )

}