package ru.gb.zverobukvy.presentation.customview

import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnStart


class CustomLetterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : CardView(context, attrs, defStyle) {

    private lateinit var layoutBackground: FrameLayout
    private lateinit var textView: CustomTextView
    private var padding = 8
    private var char: Char = ' '
    var colorTrue = Color.GREEN

    init {
        initAttributes(context, attrs, defStyle)
        initContentView()
    }

    private fun initContentView() {
        setPadding(padding, padding, padding, padding)
        val layoutParams = createLayoutParams()
        layoutBackground = FrameLayout(context).apply {
            this.layoutParams = layoutParams
        }
        textView = CustomTextView(context).apply {
            this.layoutParams = layoutParams
            text = char.toString()
        }

        addView(layoutBackground)
        addView(textView)
    }

    private fun createLayoutParams() = LayoutParams(
        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
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

    fun setTrue(isAnimation: Boolean = true) {
        if (isAnimation) {
            val animatorSet = AnimatorSet()
            val scaleUp = createScaleAnimation(this, 1f, 1.2f).apply {
                duration = DURATION_ANIMATION.toLong()
                doOnStart {
                    layoutBackground.setBackgroundColor(colorTrue)
                }
            }
            val scaleDown = createScaleAnimation(this, 1.2f, 1f).apply {
                duration = DURATION_ANIMATION.toLong()
            }
            animatorSet.playSequentially(scaleUp, scaleDown)
            animatorSet.doOnStart { bringToFront() }
            animatorSet.startDelay = START_DELAY_ANIMATION.toLong()
            animatorSet.start()
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(height, height)
    }

    companion object {
        private const val DURATION_ANIMATION = 250
        private const val START_DELAY_ANIMATION = 300
    }
}