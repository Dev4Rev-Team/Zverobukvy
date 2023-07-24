package ru.gb.zverobukvy.presentation.customview

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
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

        frontSideImageView = createImageView(context, layoutParams, srcOpen)
        backSideImageView = createImageView(context, layoutParams, srcClose)
        setOpenCard(isOpen)

        addView(frontSideImageView)
        addView(backSideImageView)
    }

    private fun createLayoutParams() = LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
    )

    private fun createImageView(
        context: Context,
        layoutParams: LayoutParams,
        src: Int
    ): ImageView = ImageView(context).apply {
        setImageResource(src)
        this.layoutParams = layoutParams
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }
    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putBoolean(IS_OPEN_KEY,isOpen)
        bundle.putParcelable(INSTANCE_STATE_KEY, super.onSaveInstanceState())
        return bundle
    }
    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        isOpen = bundle.getBoolean(IS_OPEN_KEY)
        setOpenCard(isOpen)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE_KEY , Parcelable::class.java))
        }else {
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE_KEY))
        }
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

    companion object {
        private val SRC_CLOSE = R.drawable.ic_launcher_background
        private val SRC_OPEN = R.drawable.ic_launcher_foreground
        private const val IS_OPEN = false
        private const val DURATION_ANIMATION = 300

        private const val IS_OPEN_KEY = "isOpenKey"
        private const val INSTANCE_STATE_KEY = "instanceStateKey"

    }

}