package ru.gb.zverobukvy.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import ru.gb.zverobukvy.R

/**
 * Card on the field
 */
class CustomCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : CardView(context, attrs, defStyle) {

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

        frontSideImageView = createImageView(context, layoutParams)
        backSideImageView = createImageView(context, layoutParams)
        setSrc(srcOpen, srcClose)
        setOpenCard(isOpen)

        addView(frontSideImageView)
        addView(backSideImageView)
    }

    private fun createLayoutParams() = LayoutParams(
        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
    )

    private fun createImageView(
        context: Context,
        layoutParams: LayoutParams,
    ): ImageView = ImageView(context).apply {
        this.layoutParams = layoutParams
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }

    private fun getVisibility(isOpen: Boolean) = if (isOpen) VISIBLE else INVISIBLE


    fun setOpenCard(isOpen: Boolean) {
        this.isOpen = isOpen
        frontSideImageView.visibility = getVisibility(isOpen)
        backSideImageView.visibility = getVisibility(!isOpen)
    }

    fun setVisibilityCard(isVisibility: Boolean) {
        visibility = getVisibility(isVisibility)
    }

    fun setSrc(@DrawableRes srcOpen: Int, @DrawableRes srcClose: Int) {
        frontSideImageView.setImageResource(srcOpen)
        backSideImageView.setImageResource(srcClose)
    }

    /** pos - set position view
     *  (pos:Int) -> Unit callback function
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
        private const val DURATION_ANIMATION = 300
    }

}