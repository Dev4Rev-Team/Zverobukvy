package ru.gb.zverobukvy.presentation.customview

import android.animation.AnimatorSet
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnStart
import ru.gb.zverobukvy.R


/**
 * Card on the field
 */
class CustomCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : CardView(context, attrs, defStyle) {

    var isOpen: Boolean = IS_OPEN
        private set
    private var position: Int = 0
    private var isCorrect: Boolean = false

    private var srcClose: Int = SRC_CLOSE
    private var srcOpen: Int = SRC_OPEN
    private var durationAnimation: Int = DURATION_ANIMATION

    private lateinit var frontSideImageView: CustomImageView
    private lateinit var backSideImageView: CustomImageView
    private lateinit var frontBackgroundImageView: CustomImageView

    private var clickCorrectCard: ((pos: Int) -> Unit)? = null
    fun setOnClickCorrectCard(block: (pos: Int) -> Unit) {
        clickCorrectCard = block
    }

    init {
        initAttributes(context, attrs, defStyle)
        initContentView(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomCard, defStyle, 0)
        srcClose = typedArray.getResourceId(R.styleable.CustomCard_srcClose, SRC_CLOSE)
        srcOpen = typedArray.getResourceId(R.styleable.CustomCard_srcOpen, SRC_OPEN)
        isOpen = typedArray.getBoolean(R.styleable.CustomCard_isOpen, IS_OPEN)
        durationAnimation = typedArray.getInteger(
            R.styleable.CustomCard_durationAnimation, DURATION_ANIMATION
        )
        typedArray.recycle()
    }

    private fun initContentView(context: Context) {
        val layoutParams = createLayoutParams()
        frontSideImageView = createImageView(context, layoutParams)
        backSideImageView = createImageView(context, layoutParams)
        frontBackgroundImageView = createImageView(context, layoutParams)

        setSrcFromRes(srcOpen, srcClose)
        setOpenDisplay(isOpen)

        addView(frontBackgroundImageView)
        addView(frontSideImageView)
        addView(backSideImageView)
    }

    private fun createLayoutParams() = LayoutParams(
        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
    )

    private fun createImageView(
        context: Context,
        layoutParams: LayoutParams,
    ): CustomImageView = CustomImageView(context).apply {
        this.layoutParams = layoutParams
        scaleType = ImageView.ScaleType.CENTER_CROP
    }

    private fun startAnimationFlip() {
        val animatorSet = AnimatorSet()

        val scaleUp = createScaleAnimation(this, NORMAL, SCALE).apply {
            duration = (durationAnimation * PERCENTAGE_OF_ANIMATION_TIME_UP).toLong()
        }
        val rotation = createFlipAnimation(this) {
            setOpenDisplay(isOpen)
        }.apply {
            duration = (durationAnimation * PERCENTAGE_OF_ANIMATION_TIME_FLIP).toLong()
        }
        val scaleNormal = createScaleAnimation(this, SCALE, NORMAL).apply {
            duration = (durationAnimation * PERCENTAGE_OF_ANIMATION_TIME_DOWN).toLong()
        }

        animatorSet.playSequentially(scaleUp, rotation, scaleNormal)
        animatorSet.doOnStart { bringToFront() }
        animatorSet.start()

        cameraDistance = 7500 * context.resources.displayMetrics.density
    }

    private fun setOpenDisplay(isOpen: Boolean) {
        if (isOpen) {
            frontSideImageView.alpha = 1f
            backSideImageView.alpha = 0f
        } else {
            frontSideImageView.alpha = 0f
            backSideImageView.alpha = 1f
        }
    }

    private fun setSrcFromRes(@DrawableRes srcOpen: Int, @DrawableRes srcClose: Int) {
        frontSideImageView.setImageResource(srcOpen)
        backSideImageView.setImageResource(srcClose)
    }

    /** pos - set position view
     *
     */
    fun setOnClickCardListener(pos: Int, click: (pos: Int) -> Unit) {
        position = pos
        setOnClickListener {
            if (isCorrect) {
                clickCorrectCard?.invoke(position)
            }
            click(position)
        }
    }

    fun setOpenCard(isOpen: Boolean) {
        if (isOpen != this.isOpen) {
            this.isOpen = isOpen
            startAnimationFlip()
        }
        if(!isOpen){
            isCorrect = false
        }
    }

    fun setCorrectCard() {
        this.isCorrect = true
    }

    fun setImageOpenBackground(openBackground: Drawable) {
        frontBackgroundImageView.setImageDrawable(openBackground)
    }

    fun setImageSide(frontSide: Drawable, backSide: Drawable) {
        frontSideImageView.setImageDrawable(frontSide)
        backSideImageView.setImageDrawable(backSide)
    }

    companion object {
        private val SRC_CLOSE = R.drawable.ic_launcher_background
        private val SRC_OPEN = R.drawable.ic_launcher_background
        private const val IS_OPEN = false
        private const val DURATION_ANIMATION = 250
        private const val SCALE = 1.08f
        private const val NORMAL = 1f
        private const val PERCENTAGE_OF_ANIMATION_TIME_UP = 0.1f
        private const val PERCENTAGE_OF_ANIMATION_TIME_FLIP = 0.8f
        private const val PERCENTAGE_OF_ANIMATION_TIME_DOWN = 0.1f
    }

}