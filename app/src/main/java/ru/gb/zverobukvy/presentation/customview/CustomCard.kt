package ru.gb.zverobukvy.presentation.customview

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import ru.gb.zverobukvy.R
import java.io.IOException
import java.io.InputStream


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

    private var srcClose: Int = SRC_CLOSE
    private var srcOpen: Int = SRC_OPEN
    private var durationAnimation: Int = DURATION_ANIMATION

    private lateinit var frontSideImageView: CustomImageView
    private lateinit var backSideImageView: CustomImageView
    private lateinit var frontBackgroundImageView: CustomImageView

    init {
        initAttributes(context, attrs, defStyle)
        initContentView(context)
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


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }

    private fun getVisibility(isVisible: Boolean) = if (isVisible) VISIBLE else INVISIBLE


    fun setOpenCard(isOpen: Boolean) {
        if (isOpen != this.isOpen) {
            this.isOpen = isOpen
            startAnimationFlip()
        }
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

    private fun animationScale(view: CustomCard, x1: Float, x2: Float): AnimatorSet {
        val animatorSet = AnimatorSet()
        val scaleX = ObjectAnimator.ofFloat(view, SCALE_X, x1, x2)
        val scaleY = ObjectAnimator.ofFloat(view, SCALE_Y, x1, x2)
        animatorSet.playTogether(scaleX, scaleY)
        return animatorSet
    }

    private fun animationRotation(view: CustomCard): AnimatorSet {
        val animatorSet = AnimatorSet()
        val rotationClosing = ObjectAnimator.ofFloat(view, ROTATION_Y, 0f, 90f).apply {
            doOnEnd {
                setOpenDisplay(isOpen)
            }
        }
        val rotationOpening = ObjectAnimator.ofFloat(view, ROTATION_Y, 270f, 360f)
        animatorSet.playSequentially(rotationClosing, rotationOpening)
        return animatorSet
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


    fun setVisibilityCard(isVisibility: Boolean) {
        visibility = getVisibility(isVisibility)
    }

    fun setSrcFromRes(@DrawableRes srcOpen: Int, @DrawableRes srcClose: Int) {
        frontSideImageView.setImageResource(srcOpen)
        backSideImageView.setImageResource(srcClose)
    }

    fun setSrcFromAssert(srcOpen: String, srcClose: String) {
        setImageFromAssert(frontSideImageView, srcOpen)
        setImageFromAssert(backSideImageView, srcClose)
    }

    private fun setImageFromAssert(ImageView: ImageView, src: String) {
        try {
            val ims: InputStream = context.assets.open(src)
            val d = Drawable.createFromStream(ims, null)
            ImageView.setImageDrawable(d)
            ims.close()
        } catch (ex: IOException) {
            return
        }
    }

    fun setSrcOpenBackgroundFromAssert(srcOpenBackground: String) {
        setImageFromAssert(frontBackgroundImageView, srcOpenBackground)
    }

    /** pos - set position view
     *
     */
    fun setOnClickCardListener(pos: Int, click: (pos: Int) -> Unit) {
        setOnClickListener {
            click(pos)
        }
    }

    companion object {
        private val SRC_CLOSE = R.drawable.ic_launcher_background
        private val SRC_OPEN = R.drawable.ic_launcher_foreground
        private const val IS_OPEN = false
        private const val DURATION_ANIMATION = 250
        private const val SCALE = 1.08f
        private const val NORMAL = 1f
        private const val PERCENTAGE_OF_ANIMATION_TIME_UP = 0.1f
        private const val PERCENTAGE_OF_ANIMATION_TIME_FLIP = 0.8f
        private const val PERCENTAGE_OF_ANIMATION_TIME_DOWN = 0.1f
    }

}