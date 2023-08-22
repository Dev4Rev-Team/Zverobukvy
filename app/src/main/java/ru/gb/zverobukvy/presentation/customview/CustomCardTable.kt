package ru.gb.zverobukvy.presentation.customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import ru.gb.zverobukvy.R
import kotlin.math.max

class CustomCardTable @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,

    ) : ConstraintLayout(context, attrs, defStyle) {

    private var horizontalGap = HORIZONTAL_GAP
    private var verticalGap = VERTICAL_GAP
    private var flow: Flow? = null
    private var click: ((pos: Int) -> Unit)? = null
    private var isClick = false
    private var listLetterCards: List<LetterCardUI>? = null
    private val listOfCardsOnTable = mutableListOf<CustomCard>()
    private val listOfInvalidCards = mutableListOf<CustomCard>()

    init {
        initAttributes(context, attrs, defStyle)
        initContentView()
    }

    private fun initContentView() {
        val padding = max(horizontalGap, verticalGap)
        setPadding(padding, padding, padding, padding)
        clipToPadding = false
        clipChildren = false
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomCardTable, defStyle, 0)
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
                when {
                    countCard <= COUNT_CARDS_FOR_CARDS_HORIZONTALLY_3 -> 3
                    countCard <= COUNT_CARDS_FOR_CARDS_HORIZONTALLY_4 -> 4
                    else -> 5
                }
            )
            setWrapMode(Flow.WRAP_ALIGNED)
            setHorizontalStyle(Flow.CHAIN_PACKED)
            setVerticalStyle(Flow.CHAIN_PACKED)
        }
        addView(flow)
    }


    fun setListItem(
        list: List<LetterCardUI>,
        assetsImageCash: AssetsImageCash,
        factory: (() -> CustomCard)? = null,
    ) {
        isClick = false
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

                setImageSide(
                    assetsImageCash.getImage(letterCard.faceImageName),
                    assetsImageCash.getImage(letterCard.backImageName)
                )

                setOnClickCardListener(pos) {
                    if (!isClick && !listOfCardsOnTable[pos].isOpen) {
                        isClick = true
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

    fun setCorrectLetterCard() {
        isClick = false
    }

    fun setInvalidLetterCard(letterCard: LetterCardUI) {
        listLetterCards?.indexOf(letterCard)?.let {
            listOfInvalidCards.add(listOfCardsOnTable[it])
        }
        isClick = true
    }

    fun nextPlayer() {
        listOfInvalidCards.forEach {
            it.setOpenCard(false)
        }
        listOfInvalidCards.clear()
        isClick = false
    }

    fun nextWord() {
        isClick = false
        listOfCardsOnTable.forEach { it.setOpenCard(false) }
    }

    fun openUheEntireTable(delayMillis: Long) {
        isClick = true
        listOfInvalidCards.forEach { it.setOpenCard(true) }
        postDelayed(
            {
                listOfInvalidCards.forEach { it.setOpenCard(false) }
                isClick = false
            },
            delayMillis
        )
    }

    fun setOnClickListener(click: (pos: Int) -> Unit) {
        this.click = click
    }

    companion object {
        private const val COUNT_CARDS_FOR_CARDS_HORIZONTALLY_3 = 12
        private const val COUNT_CARDS_FOR_CARDS_HORIZONTALLY_4 = 20
        private const val HORIZONTAL_GAP = 24
        private const val VERTICAL_GAP = 24
    }

    interface LetterCardUI {
        val letter: Char
        var isVisible: Boolean
        val faceImageName: String
        val backImageName: String
    }
}
