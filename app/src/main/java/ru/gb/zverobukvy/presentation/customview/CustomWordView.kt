package ru.gb.zverobukvy.presentation.customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import ru.gb.zverobukvy.R
import kotlin.math.max

class CustomWordView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,

    ) : ConstraintLayout(context, attrs, defStyle) {

    private var horizontalGap = CustomWordView.HORIZONTAL_GAP
    private var verticalGap = CustomWordView.VERTICAL_GAP
    private var flow: Flow? = null

    private var listLetterCards: List<CustomCardTable.LetterCardUI>? = null
    private val listOfCardsOnTable = mutableListOf<CustomCard>()
    private val listOfInvalidCards = mutableListOf<CustomCard>()

    private fun initContentView() {
        val padding = max(horizontalGap, verticalGap)
        setPadding(padding, padding, padding, padding)
        clipToPadding = false
        clipChildren = false
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

    companion object{
        private const val HORIZONTAL_GAP = 24
        private const val VERTICAL_GAP = 24
    }
}