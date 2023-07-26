package ru.gb.zverobukvy.presentation.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
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

    private var srcClose: Int = SRC_CLOSE
    private var srcOpen: Int = SRC_OPEN
    private var isOpen: Boolean = IS_OPEN
    private var durationAnimation: Int = DURATION_ANIMATION

    private lateinit var frontSideImageView: CustomViewImage
    private lateinit var backSideImageView: CustomViewImage

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
        setSrcFromRes(srcOpen, srcClose)
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
    ): CustomViewImage = CustomViewImage(context).apply {
        this.layoutParams = layoutParams
        scaleType = ImageView.ScaleType.CENTER_CROP
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

    fun setSrcFromRes(@DrawableRes srcOpen: Int, @DrawableRes srcClose: Int) {
        frontSideImageView.setImageResource(srcOpen)
        backSideImageView.setImageResource(srcClose)
    }

    /**
     * load from res drawable
     */
    fun setSrcFromRes(srcOpen: String, srcClose: String) {
        setImageFromRes(frontSideImageView, srcOpen)
        setImageFromRes(backSideImageView, srcClose)
    }

    private fun setImageFromRes(imageView: ImageView, src: String) {
        getIdRes(src).let {
            if (it != 0) {
                imageView.setImageResource(it)
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun getIdRes(resource: String) =
        resources.getIdentifier(resource, "drawable", context.packageName)

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