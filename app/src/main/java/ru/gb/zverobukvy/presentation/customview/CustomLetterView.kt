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
    private var char: Char = EMPTY_SPACE
    var colorGuessed = DEFAULT_COLOR_GUESSED
    var colorUnsolved = DEFAULT_COLOR_UNSOLVED

    init {
        initContentView()
    }

    private fun initContentView() {
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

    fun setChar(char: Char) {
        textView.text = char.toString()
    }

    fun setGuessed(isAnimation: Boolean = true) {
        if (isAnimation) {
            val animatorSet = AnimatorSet()
            val scaleUp = createScaleAnimation(this, NORMAL, SCALE).apply {
                duration = DURATION_ANIMATION.toLong()
                doOnStart {
                    setBackground()
                }
            }
            val scaleDown = createScaleAnimation(this, SCALE, NORMAL).apply {
                duration = DURATION_ANIMATION.toLong()
            }
            animatorSet.playSequentially(scaleUp, scaleDown)
            animatorSet.doOnStart { bringToFront() }
            animatorSet.startDelay = START_DELAY_ANIMATION.toLong()
            animatorSet.start()
        } else {
            setBackground()
        }
    }

    fun setUnsolved() {
        layoutBackground.setBackgroundColor(colorUnsolved)
    }

    private fun setBackground() {
        layoutBackground.setBackgroundColor(colorGuessed)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(height, height)
    }

    companion object {
        private const val EMPTY_SPACE = ' '
        private const val DURATION_ANIMATION = 250
        private const val START_DELAY_ANIMATION = 300
        private const val SCALE = 1.2f
        private const val NORMAL = 1f
        private const val DEFAULT_COLOR_GUESSED = Color.GREEN
        private const val DEFAULT_COLOR_UNSOLVED = Color.WHITE
    }
}