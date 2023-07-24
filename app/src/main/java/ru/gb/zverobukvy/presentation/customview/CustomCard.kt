package ru.gb.zverobukvy.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import ru.gb.zverobukvy.R

/**
 * Card on the field
 */
class CustomCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : FrameLayout(context, attrs, defStyle) {

    private var srcClose: Int = 0
    private var srcOpen: Int = 0
    private var isOpen: Boolean = false
    private var durationAnimation: Int = 0

    private lateinit var frontSideImageView: ImageView
    private lateinit var backSideImageView: ImageView

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

        frontSideImageView = createImageView(context, layoutParams, srcOpen, isOpen)
        backSideImageView = createImageView(context, layoutParams, srcClose, !isOpen)

        addView(frontSideImageView)
        addView(backSideImageView)
    }

    private fun createLayoutParams() = LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
    )

    private fun createImageView(
        context: Context,
        layoutParams: LayoutParams,
        src: Int,
        isOpen: Boolean,
    ): ImageView = ImageView(context).apply {
        setImageResource(src)
        this.layoutParams = layoutParams
        visibility = getVisibility(isOpen)
        setBackgroundColor(context.getColor(androidx.appcompat.R.color.abc_background_cache_hint_selector_material_light))
    }

    private fun getVisibility(isOpen: Boolean) = if (isOpen) VISIBLE else INVISIBLE


    fun setOpenCard(isOpen: Boolean) {
        frontSideImageView.visibility = getVisibility(isOpen)
        backSideImageView.visibility = getVisibility(!isOpen)
    }

    fun setVisibilityCard(isVisibility: Boolean) {
        visibility = getVisibility(isVisibility)
    }

    companion object {
        val SRC_CLOSE = R.drawable.ic_launcher_background
        val SRC_OPEN = R.drawable.ic_launcher_foreground
        const val IS_OPEN = false
        const val DURATION_ANIMATION = 300

    }

}