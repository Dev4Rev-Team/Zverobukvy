package ru.dev4rev.kids.zoobukvy.presentation.customview

import android.animation.AnimatorSet
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.configuration.Conf


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
    private var durationAnimation: Long = DURATION_ANIMATION

    private lateinit var frontSideImageView: CustomImageView
    private lateinit var backSideImageView: CustomImageView
    private lateinit var frontBackgroundImageView: CustomImageView

    private var animationFlipOpenSet: AnimatorSet? = null
    private var animationFlipCloseSet: AnimatorSet? = null

    private var clickCorrectCard: ((pos: Int) -> Unit)? = null
    fun setOnClickCorrectCard(block: (pos: Int) -> Unit) {
        clickCorrectCard = block
    }

    init {
        initAttributes(context, attrs, defStyle)
        initContentView(context)
        initAnimator()
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
            R.styleable.CustomCard_durationAnimation, DURATION_ANIMATION.toInt()
        ).toLong()
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

    private fun initAnimator() {

        animationFlipOpenSet = AnimatorSet().apply {
        }
        animationFlipCloseSet = AnimatorSet().apply {
        }

        val animationFlip = createFlipAnimation(this, durationAnimation)

        val alphaBackSideVisible =
            createAlphaShowAnimation(backSideImageView, durationAnimation / 2, 0L, true)
        val alphaBackSideInvisible =
            createAlphaShowAnimation(backSideImageView, durationAnimation / 2, 0L, false)

        animationFlipOpenSet?.apply {
            playTogether(
                animationFlip,
                alphaBackSideInvisible
            )
        }
        animationFlipCloseSet?.apply {
            playTogether(
                animationFlip,
                alphaBackSideVisible
            )
        }
        cameraDistance = 7500 * context.resources.displayMetrics.density
    }


    private fun startAnimationFlip() {
        bringToFront()
        if (isOpen) {
            animationFlipOpenSet?.start()
        } else {
            animationFlipCloseSet?.start()
        }
    }

    private fun setOpenDisplay(isOpen: Boolean) {
        if (isOpen) {
            backSideImageView.visibility = INVISIBLE
        } else {
            backSideImageView.visibility = VISIBLE
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

    fun setColorCard(color: Int) {
        frontSideImageView.setColorFilter(color)
    }

    fun setOpenCard(isOpen: Boolean) {
        if (isOpen != this.isOpen) {
            this.isOpen = isOpen
            startAnimationFlip()
        }
        if (!isOpen) {
            isCorrect = false
        }
    }

    fun setCorrectCard() {
        this.isCorrect = true
    }

    fun setImageOpenBackground(@DrawableRes openBackground: Int) {
        frontBackgroundImageView.setImageResource(openBackground)
    }

    fun setImageCloseBackground(@DrawableRes backSide: Int) {
        backSideImageView.setImageResource(backSide)
    }
    fun setImageCloseBackground(backSide: Drawable) {
        backSideImageView.setImageDrawable(backSide)
    }

    fun setImageOpen(frontSide: Drawable) {
        frontSideImageView.setImageDrawable(frontSide)
    }

    override fun onDetachedFromWindow() {
        animationFlipOpenSet?.end()
        animationFlipCloseSet?.end()
        super.onDetachedFromWindow()
    }

    companion object {
        private val SRC_CLOSE = R.drawable.ic_launcher_background
        private val SRC_OPEN = R.drawable.ic_launcher_background
        private const val IS_OPEN = false
        private const val DURATION_ANIMATION = Conf.DURATION_FLIP
    }

}