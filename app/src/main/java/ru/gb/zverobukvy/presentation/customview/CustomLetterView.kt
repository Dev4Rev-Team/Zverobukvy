package ru.gb.zverobukvy.presentation.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.TextViewCompat

class CustomLetterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : CardView(context, attrs, defStyle) {

    private lateinit var textView: TextView
    private var padding = 8
    private var char: Char = ' '
    val colorTrue = Color.GREEN

    init {
        initAttributes(context, attrs, defStyle)
        initContentView()
    }

    private fun initContentView() {
        setPadding(padding, padding, padding, padding)

        val layoutParams = createLayoutParams()
        textView = TextView(context).apply {
            this.layoutParams = layoutParams
            TextViewCompat.setAutoSizeTextTypeWithDefaults(
                this,
                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
            );
            text = char.toString()
        }

        addView(textView)
    }

    private fun createLayoutParams() = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
    )

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyle: Int) {
//        val typedArray =
//            context.obtainStyledAttributes(attrs, R.styleable.CustomCardTable, defStyle, 0)
//        padding = typedArray.getInteger(
//            R.styleable.CustomCardTable_horizontalGap,
//            CustomCardTable.HORIZONTAL_GAP
//        )
//        colorTrue
//
//        typedArray.recycle()
    }

    fun setChar(char: Char) {
        textView.text = char.toString()
    }

    fun setTrue(){
        setBackgroundColor(colorTrue)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }
}