package ru.gb.zverobukvy.presentation.customview

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
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog.PlayerUI
import ru.gb.zverobukvy.utility.ui.dipToPixels

class CustomImageRatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    private var main_horizontal_gap = MAIN_HORIZONTAL_GAP
    private var padding_image = PADDING_IMAGE

    private var color_one = COLOR_ONE
    private var color_two = COLOR_TWO
    private var color_three = COLOR_THREE
    private var color_stroke_one = COLOR_STROKE_ONE
    private var color_stroke_two = COLOR_STROKE_TWO
    private var color_stroke_three = COLOR_STROKE_THREE
    private var stroke_width = STROKE_WIDTH

    private var size_avatar_one = SIZE_AVATAR_ONE
    private var size_avatar_two = SIZE_AVATAR_TWO
    private var size_avatar_three = SIZE_AVATAR_THREE

    private var size_avatars_place = SIZE_AVATARS_PLACE

    private var shift_down_one = SHIFT_DOWN_ONE
    private var shift_down_two = SHIFT_DOWN_TWO
    private var shift_down_three = SHIFT_DOWN_THREE

    private var duration_animation_one_item = DURATION_ANIMATION_ONE_ITEM

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
        sizeAvatarList.addAll(listOf(size_avatar_one, size_avatar_two, size_avatar_three))
        colorAvatarList.addAll(listOf(color_one, color_two, color_three))
        colorAvatarStrokeList.addAll(listOf(color_stroke_one, color_stroke_two, color_stroke_three))
        shiftDownList.addAll(listOf(shift_down_one, shift_down_two, shift_down_three))
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CustomImageRatingView, defStyle, 0)


        main_horizontal_gap = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_main_horizontal_gap,
            context.dipToPixels(MAIN_HORIZONTAL_GAP)
        )
        padding_image = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_padding_image,
            context.dipToPixels(PADDING_IMAGE)
        )

        color_one = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_one,
            COLOR_ONE
        )
        color_two = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_two,
            COLOR_TWO
        )
        color_three = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_three,
            COLOR_THREE
        )
        color_stroke_one = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_stroke_one,
            COLOR_STROKE_ONE
        )
        color_stroke_two = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_stroke_two,
            COLOR_STROKE_TWO
        )
        color_stroke_three = typedArray.getColor(
            R.styleable.CustomImageRatingView_color_stroke_three,
            COLOR_STROKE_THREE
        )
        stroke_width = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_stroke_width,
            context.dipToPixels(STROKE_WIDTH)
        )

        size_avatar_one = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_size_avatar_one,
            context.dipToPixels(SIZE_AVATAR_ONE)
        )
        size_avatar_two = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_size_avatar_two,
            context.dipToPixels(SIZE_AVATAR_TWO)
        )
        size_avatar_three = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_size_avatar_three,
            context.dipToPixels(SIZE_AVATAR_THREE)
        )

        size_avatars_place = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_size_avatars_place,
            context.dipToPixels(SIZE_AVATARS_PLACE)
        )

        shift_down_one = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_shift_down_one,
            context.dipToPixels(SHIFT_DOWN_ONE)
        )
        shift_down_two = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_shift_down_two,
            context.dipToPixels(SHIFT_DOWN_TWO)
        )
        shift_down_three = typedArray.getDimensionPixelSize(
            R.styleable.CustomImageRatingView_shift_down_three,
            context.dipToPixels(SHIFT_DOWN_THREE)
        )

        duration_animation_one_item = typedArray.getInteger(
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
                val naxGap = size_avatars_place - sizeAvatarList[i]
                val gap = sizeAvatarList[i] - (naxGap / countWinner)
                ratingFlow[i]?.setHorizontalGap(-1 * gap)
            }
        }

        var cnt = 1
        for (i in 0 until COUNT_WINNER) {
            cardViewWinnerList[i]?.forEach {
                delayAndRun(cnt * duration_animation_one_item.toLong()) {
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
            size_avatars_place * 3 + main_horizontal_gap * 2,
            LayoutParams.MATCH_PARENT
        )
        setHorizontalGap(main_horizontal_gap)
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
                    LayoutParams(size_avatars_place, LayoutParams.MATCH_PARENT)
                setHorizontalBias(1f)
            }

            else -> {
                layoutParams =
                    LayoutParams(size_avatars_place, LayoutParams.MATCH_PARENT)
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
            strokeWidth = stroke_width
            this@CustomImageRatingView.addView(this)
        }
        val imageView = CustomImageView(context).apply {
            this.layoutParams = FrameLayout.LayoutParams(
                LayoutParams.MATCH_CONSTRAINT, LayoutParams.MATCH_CONSTRAINT
            )
            id = generateViewId()
            imageAvatarLoader.loadImageAvatar(player.player.avatar, this)
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setPadding(padding_image)
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

