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
    private lateinit var flow: Flow
    private var click: ((pos: Int) -> Unit)? = null

    init {
        initAttributes(context, attrs, defStyle)
        initContentView(context)
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

    private fun initContentView(context: Context) {
        flow = Flow(context)
        flow.setHorizontalGap(horizontalGap)
        flow.setVerticalGap(verticalGap)
        flow.setMaxElementsWrap(maxNumberOfCardsHorizontally)
        flow.setWrapMode(Flow.WRAP_ALIGNED)
        flow.setHorizontalStyle(Flow.CHAIN_PACKED)
        addView(flow)

    }


    fun setListItem(list: List<Item>) {
        val countCard = list.count()
        maxNumberOfCardsHorizontally =
            min(ceil(sqrt(countCard.toDouble())).toInt(), MAX_NUMBER_OF_CARDS_HORIZONTALLY)
        repeat(countCard) { pos: Int ->
            val customCard = CustomCard(context).apply {
                setSrc(list[pos].url, 0)
                setOnClickCardListener(pos) {
                    click?.run { invoke(it) }
                }
            }

            flow.addView(customCard)
        }
    }


    companion object {
        const val MAX_NUMBER_OF_CARDS_HORIZONTALLY = 5
        const val HORIZONTAL_GAP = 24
        const val VERTICAL_GAP = 24
    }

    interface Item {
        val url: Int
    }
}