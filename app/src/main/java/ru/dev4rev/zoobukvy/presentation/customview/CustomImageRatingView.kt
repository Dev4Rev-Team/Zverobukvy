package ru.dev4rev.zoobukvy.presentation.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.setPadding
import com.google.android.material.card.MaterialCardView
import ru.dev4rev.zoobukvy.R
import ru.dev4rev.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.zoobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import ru.dev4rev.zoobukvy.presentation.animal_letters_game.game_is_over_dialog.PlayerUI
import ru.dev4rev.zoobukvy.utility.ui.dipToPixels

class CustomImageRatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    private var mainHorizontalGap = MAIN_HORIZONTAL_GAP
    private var paddingImage = PADDING_IMAGE

    private var colorOne = COLOR_ONE
    private var colorTwo = COLOR_TWO
    private var colorThree = COLOR_THREE
    private var colorStrokeOne = COLOR_STROKE_ONE
    private var colorStrokeTwo = COLOR_STROKE_TWO
    private var colorStrokeThree = COLOR_STROKE_THREE
    private var strokeWidth = STROKE_WIDTH

    private var sizeAvatarOne = SIZE_AVATAR_ONE
    private var sizeAvatarTwo = SIZE_AVATAR_TWO
    private var sizeAvatarThree = SIZE_AVATAR_THREE

    private var sizeAvatarsPlace = SIZE_AVATARS_PLACE

    private var shiftDownOne = SHIFT_DOWN_ONE
    private var shiftDownTwo = SHIFT_DOWN_TWO
    private var shiftDownThree = SHIFT_DOWN_THREE

    private var durationAnimationOneItem = DURATION_ANIMATION_ONE_ITEM

    private var mainFlow: Flow? = null
    private val cardViewWinnerList = mutableMapOf<Int, MutableList<CardView>>()
    private val sizeAvatarList = mutableListOf<Int>()
    private val colorAvatarList = mutableListOf<Int>()
    private val colorAvatarStrokeList = mutableListOf<Int>()

    private val shiftDownList = mutableListOf<Int>()
    private var imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl


    init {
        initAttributes(context, attrs, defStyle)
        for (i in 0 until COUNT_WINNER) {
            cardViewWinnerList[i] = mutableListOf()
        }
        initVal()
    }

    private fun initVal() {
        sizeAvatarList.addAll(listOf(sizeAvatarOne, sizeAvatarTwo, sizeAvatarThree))
        colorAvatarList.addAll(listOf(colorOne, colorTwo, colorThree))
        colorAvatarStrokeList.addAll(listOf(colorStrokeOne, colorStrokeTwo, colorStrokeThree))
        shiftDownList.addAll(listOf(shiftDownOne, shiftDownTwo, shiftDownThree))
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomImageRatingView, defStyle, 0)


        mainHorizontalGap = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_main_horizontal_gap,
            context.dipToPixels(MAIN_HORIZONTAL_GAP)
        )
        paddingImage = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_padding_image,
            context.dipToPixels(PADDING_IMAGE)
        )

        colorOne = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_one,
            COLOR_ONE
        )
        colorTwo = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_two,
            COLOR_TWO
        )
        colorThree = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_three,
            COLOR_THREE
        )
        colorStrokeOne = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_stroke_one,
            COLOR_STROKE_ONE
        )
        colorStrokeTwo = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_stroke_two,
            COLOR_STROKE_TWO
        )
        colorStrokeThree = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_stroke_three,
            COLOR_STROKE_THREE
        )
        strokeWidth = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_stroke_width,
            context.dipToPixels(STROKE_WIDTH)
        )

        sizeAvatarOne = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_size_avatar_one,
            context.dipToPixels(SIZE_AVATAR_ONE)
        )
        sizeAvatarTwo = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_size_avatar_two,
            context.dipToPixels(SIZE_AVATAR_TWO)
        )
        sizeAvatarThree = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_size_avatar_three,
            context.dipToPixels(SIZE_AVATAR_THREE)
        )

        sizeAvatarsPlace = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_size_avatars_place,
            context.dipToPixels(SIZE_AVATARS_PLACE)
        )

        shiftDownOne = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_shift_down_one,
            context.dipToPixels(SHIFT_DOWN_ONE)
        )
        shiftDownTwo = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_shift_down_two,
            context.dipToPixels(SHIFT_DOWN_TWO)
        )
        shiftDownThree = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_shift_down_three,
            context.dipToPixels(SHIFT_DOWN_THREE)
        )

        durationAnimationOneItem = typedArray.getInteger(
            R.styleable.CustomImageRatingView_duration_animation_one_item,
            DURATION_ANIMATION_ONE_ITEM
        )
        typedArray.recycle()
    }

    fun show(list: List<PlayerUI>) {
        if (mainFlow != null) {
            removeAllViews()
            for (i in 0 until COUNT_WINNER) {
                cardViewWinnerList[i]?.clear()
            }
        }
        mainFlow = createMainFlow()
        val ratingFlow = initContentView(mainFlow)
        var place = 0
        var rating = list[0].scoreInCurrentGame
        for (player in list) {
            if (player.scoreInCurrentGame != rating) {
                rating = player.scoreInCurrentGame
                place++
            }
            if (place >= COUNT_WINNER) break

            val cardView = createCardAvatar(place, player, sizeAvatarList[place])
            cardViewWinnerList[place]?.add(cardView)
            ratingFlow[place]?.addView(cardView)
        }
        for (i in 0 until COUNT_WINNER) {
            val countWinner = cardViewWinnerList[i]?.size ?: 1
            if (countWinner > 0) {
                val naxGap = sizeAvatarsPlace - sizeAvatarList[i]
                val gap = sizeAvatarList[i] - (naxGap / countWinner)
                ratingFlow[i]?.setHorizontalGap(-1 * gap)
            }
        }

        var cnt = 1
        for (i in 0 until COUNT_WINNER) {
            cardViewWinnerList[i]?.forEach {
                delayAndRun(cnt * durationAnimationOneItem.toLong()) {
                    it.visibility = VISIBLE
                    val durationAnimation = DURATION_ANIMATION_ONE_ITEM
                    createScaleAnimation(it, 1.1f, 1f).apply {
                        duration = durationAnimation.toLong()
                    }
                        .start()
                }
                cnt++
            }
        }

    }

    private fun initContentView(mainFlow: Flow?): Map<Int, Flow> {

        val flowRatingMap = mutableMapOf<Int, Flow>()
        for (i in 0 until COUNT_WINNER) {
            flowRatingMap[i] = createNewFlow(i).apply {
                paddingTop = shiftDownList[i]
            }
        }

        mainFlow?.apply {
            addView(flowRatingMap[1])
            addView(flowRatingMap[0])
            addView(flowRatingMap[2])
        }
        return flowRatingMap
    }

    private fun createMainFlow() = Flow(context).apply {
        id = generateViewId()
        layoutParams = LayoutParams(
            sizeAvatarsPlace * 3 + mainHorizontalGap * 2,
            LayoutParams.MATCH_PARENT
        )
        setHorizontalGap(mainHorizontalGap)
        setMaxElementsWrap(COUNT_WINNER)
        setWrapMode(Flow.WRAP_CHAIN)
        setHorizontalStyle(Flow.CHAIN_PACKED)
        setVerticalStyle(Flow.CHAIN_PACKED)

        this@CustomImageRatingView.addView(this)
    }

    private fun createNewFlow(place: Int) = Flow(context).apply {
        id = generateViewId()
        when (place) {
            0 -> {
                layoutParams =
                    Constraints.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
            }

            1 -> {
                layoutParams =
                    LayoutParams(sizeAvatarsPlace, LayoutParams.MATCH_PARENT)
                setHorizontalBias(1f)
            }

            else -> {
                layoutParams =
                    LayoutParams(sizeAvatarsPlace, LayoutParams.MATCH_PARENT)
                setHorizontalBias(0f)
            }
        }
        this@CustomImageRatingView.addView(this)
    }

    private fun createCardAvatar(place: Int, player: PlayerUI, size: Int): CardView {
        val cardView = MaterialCardView(context).apply {
            id = generateViewId()
            radius = size / 2f
            elevation = 3f
            visibility = INVISIBLE
            layoutParams = LayoutParams(size, size)
            strokeColor = colorAvatarStrokeList[place]
            strokeWidth = strokeWidth
            this@CustomImageRatingView.addView(this)
        }
        val imageView = CustomImageView(context).apply {
            this.layoutParams = FrameLayout.LayoutParams(
                LayoutParams.MATCH_CONSTRAINT, LayoutParams.MATCH_CONSTRAINT
            )
            id = generateViewId()
            imageAvatarLoader.loadImageAvatar(player.player.avatar, this)
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setPadding(paddingImage)
            setBackgroundColor(colorAvatarList[place])
        }
        cardView.addView(imageView)
        return cardView
    }

    private fun delayAndRun(time: Long, block: () -> Unit) {
        rootView.postDelayed({
            if (rootView != null) {
                block.invoke()
            }
        }, time)
    }

    companion object {
        private const val COUNT_WINNER = 3
        const val MAIN_HORIZONTAL_GAP = 16
        const val PADDING_IMAGE = 8
        val COLOR_ONE = Color.parseColor("#EEE020")
        val COLOR_TWO = Color.parseColor("#C0C0C0")
        val COLOR_THREE = Color.parseColor("#C48322")
        val COLOR_STROKE_ONE = Color.parseColor("#55000000")
        val COLOR_STROKE_TWO = Color.parseColor("#55000000")
        val COLOR_STROKE_THREE = Color.parseColor("#55000000")
        const val STROKE_WIDTH = 1
        const val SIZE_AVATAR_ONE = 60
        const val SIZE_AVATAR_TWO = 52
        const val SIZE_AVATAR_THREE = 48
        const val SIZE_AVATARS_PLACE = (SIZE_AVATAR_ONE * 1.7).toInt()
        const val SHIFT_DOWN_ONE = 0
        const val SHIFT_DOWN_TWO = 24
        const val SHIFT_DOWN_THREE = 24
        const val DURATION_ANIMATION_ONE_ITEM = 300
    }
}

