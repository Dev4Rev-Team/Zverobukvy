package ru.dev4rev.kids.zoobukvy.presentation.customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.domain.use_case.color_letters.ColorLetterEnum
import ru.dev4rev.kids.zoobukvy.utility.ui.dipToPixels
import kotlin.math.max
import kotlin.properties.Delegates

class CustomCardTable @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,

    ) : ConstraintLayout(context, attrs, defStyle) {

    private var horizontalGap = HORIZONTAL_GAP
    private var verticalGap = VERTICAL_GAP
    private var flow: Flow? = null
    private var click: ((pos: Int) -> Unit)? = null
    private var isWorkClick = false
    private var listLetterCards: List<LetterCardUI>? = null
    private val listOfCardsOnTable = mutableListOf<CustomCard>()
    private var radiusCard by Delegates.notNull<Int>()
    var countCardHorizontally: Int = 0

    init {
        initAttributes(context, attrs, defStyle)
        initContentView()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomCardTable, defStyle, 0)
        horizontalGap = typedArray.getDimensionPixelSize(
            R.styleable.CustomCardTable_horizontalGap, context.dipToPixels(HORIZONTAL_GAP.toFloat())
        )
        verticalGap = typedArray.getDimensionPixelSize(
            R.styleable.CustomCardTable_verticalGap, context.dipToPixels(VERTICAL_GAP.toFloat())
        )
        radiusCard = typedArray.getDimensionPixelSize(
            R.styleable.CustomCardTable_radiusCards, context.dipToPixels(RADIUS_CARD.toFloat())
        )
        typedArray.recycle()
    }

    private fun initContentView() {
        val padding = max(horizontalGap, verticalGap)
        setPadding(padding, padding, padding, padding)
        clipToPadding = false
        clipChildren = false
    }

    private fun createNewFlow(countCard: Int) {
        if (flow != null) {
            removeAllViews()
        }
        flow = Flow(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            setHorizontalGap(horizontalGap)
            setVerticalGap(verticalGap)
            countCardHorizontally = when {
                countCard <= COUNT_CARDS_FOR_CARDS_HORIZONTALLY_3 -> 3
                countCard <= COUNT_CARDS_FOR_CARDS_HORIZONTALLY_4 -> 4
                else -> 5
            }
            setMaxElementsWrap(
                countCardHorizontally
            )
            setWrapMode(Flow.WRAP_ALIGNED)
            setHorizontalStyle(Flow.CHAIN_PACKED)
            setVerticalStyle(Flow.CHAIN_PACKED)
        }
        addView(flow)
    }

    fun setWorkClick(switch: Boolean) {
        isWorkClick = switch
    }

    fun setListItem(
        list: List<LetterCardUI>,
        assetsImageCash: AssetsImageCash,
        factory: (() -> CustomCard)? = null,
    ) {
        setWorkClick(false)
        listOfCardsOnTable.clear()
        listLetterCards = list
        val countCard = list.count()
        createNewFlow(countCard)

        repeat(countCard) { pos: Int ->
            val customCard = (factory?.run { invoke() } ?: CustomCard(context)).apply {
                layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
                id = generateViewId()
                radius = radiusCard.toFloat()

                val letterCard = list[pos]
                setOpenCard(letterCard.isVisible)

                setImageSide(
                    assetsImageCash.getImage(letterCard.faceImageName),
                    assetsImageCash.getImage(letterCard.backImageName)
                )

                setOnClickCardListener(pos) {

                    val customCard = listOfCardsOnTable[pos]
                    if (isWorkClick && !customCard.isOpen) {
                        click?.run { invoke(it) }
                    }
                }
            }

            this@CustomCardTable.addView(customCard, 1 + pos)
            flow?.addView(customCard)
            listOfCardsOnTable.add(customCard)
        }
    }

    fun openCard(letterCard: LetterCardUI) {
        listOfCardsOnTable[getPositionCard(letterCard)].setOpenCard(true)
    }

    fun setCorrectlyCard(letterCard: LetterCardUI) {
        listOfCardsOnTable[getPositionCard(letterCard)].setCorrectCard()
    }

    fun closeCard(letterCard: LetterCardUI) {
        listOfCardsOnTable[getPositionCard(letterCard)].setOpenCard(false)
    }

    fun closeCardAll() {
        listOfCardsOnTable.forEach {
            it.setOpenCard(false)
        }
    }

    private fun getPositionCard(letterCard: LetterCardUI): Int {
        return listLetterCards?.withIndex()?.find { it.value.letter == letterCard.letter }?.index
            ?: throw IllegalArgumentException("No card found $letterCard")
    }

    fun setOnClickListener(click: (pos: Int) -> Unit) {
        this.click = click
    }

    companion object {
        private const val COUNT_CARDS_FOR_CARDS_HORIZONTALLY_3 = 12
        private const val COUNT_CARDS_FOR_CARDS_HORIZONTALLY_4 = 20
        private const val HORIZONTAL_GAP = 24
        private const val VERTICAL_GAP = 24
        private const val RADIUS_CARD = 8
    }

    interface LetterCardUI {
        val letter: Char
        var isVisible: Boolean
        val faceImageName: String
        val backImageName: String
        val soundName: String
        val letterName: String
        var colorLetterEnum: ColorLetterEnum
    }
}
