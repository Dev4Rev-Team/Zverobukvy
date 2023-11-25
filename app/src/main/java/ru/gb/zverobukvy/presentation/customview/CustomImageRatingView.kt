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
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog.PlayerUI
import ru.gb.zverobukvy.utility.ui.dipToPixels

class CustomImageRatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    private var mainFlow: Flow? = null
    private val cardViewWinnerList = mutableMapOf<Int, MutableList<CardView>>()
    private val sizeAvatarList = mutableListOf(SIZE_AVATAR_ONE, SIZE_AVATAR_TWO, SIZE_AVATAR_THREE)
    private val colorAvatarList = mutableListOf(COLOR_ONE, COLOR_TWO, COLOR_THREE)
    private val colorAvatarStrokeList =
        mutableListOf(COLOR_STROKE_ONE, COLOR_STROKE_TWO, COLOR_STROKE_THREE)

    private val shiftDownList = mutableListOf(SHIFT_DOWN_ONE, SHIFT_DOWN_TWO, SHIFT_DOWN_THREE)
    private var imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl


    init {
        for (i in 0 until COUNT_WINNER) {
            cardViewWinnerList[i] = mutableListOf()
        }
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
            val naxGap = SiZE_AVATARS_PLACE - sizeAvatarList[i]
            val gap = sizeAvatarList[i] - (naxGap / countWinner).toInt()
            ratingFlow[i]?.setHorizontalGap(context.dipToPixels(-1 * gap))
        }

        var cnt = 1
        for (i in 0 until COUNT_WINNER) {
            cardViewWinnerList[i]?.forEach {
                delayAndRun(cnt * DURATION_ANIMATION_ONE_ITEM) {
                    it.visibility = VISIBLE
                    val durationAnimation = DURATION_ANIMATION_ONE_ITEM
                    createScaleAnimation(it, 1.1f, 1f).apply { duration = durationAnimation }
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
                paddingTop = context.dipToPixels(shiftDownList[i])
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
            context.dipToPixels(SiZE_AVATARS_PLACE * 3) +
                    context.dipToPixels(MAIN_HORIZONTAL_GAP * 2),
            LayoutParams.MATCH_PARENT
        )
        setHorizontalGap(context.dipToPixels(MAIN_HORIZONTAL_GAP))
        setMaxElementsWrap(context.dipToPixels(COUNT_WINNER))
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
                    LayoutParams(context.dipToPixels(SiZE_AVATARS_PLACE), LayoutParams.MATCH_PARENT)
                setHorizontalBias(1f)
            }

            else -> {
                layoutParams =
                    LayoutParams(context.dipToPixels(SiZE_AVATARS_PLACE), LayoutParams.MATCH_PARENT)
                setHorizontalBias(0f)
            }
        }
        setWrapMode(Flow.WRAP_ALIGNED)
        setHorizontalStyle(Flow.CHAIN_PACKED)
        setVerticalStyle(Flow.CHAIN_PACKED)
        this@CustomImageRatingView.addView(this)
    }

    private fun createCardAvatar(place: Int, player: PlayerUI, size: Int): CardView {
        val sizeDp = context.dipToPixels(size)
        val cardView = MaterialCardView(context).apply {
            id = generateViewId()
            radius = sizeDp / 2f
            elevation = 3f
            visibility = INVISIBLE
            layoutParams = LayoutParams(sizeDp, sizeDp)
            strokeColor = colorAvatarStrokeList[place]
            strokeWidth = context.dipToPixels(STROKE_WIDTH)
            this@CustomImageRatingView.addView(this)
        }
        val imageView = CustomImageView(context).apply {
            this.layoutParams = FrameLayout.LayoutParams(
                LayoutParams.MATCH_CONSTRAINT, LayoutParams.MATCH_CONSTRAINT
            )
            id = generateViewId()
            imageAvatarLoader.loadImageAvatar(player.player.avatar, this)
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setPadding(context.dipToPixels(PADDING_IMAGE))
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
        const val MAIN_HORIZONTAL_GAP = 15
        const val PADDING_IMAGE = 7
        val COLOR_ONE = Color.parseColor("#EEE020")
        val COLOR_TWO = Color.parseColor("#C0C0C0")
        val COLOR_THREE = Color.parseColor("#C48322")
        val COLOR_STROKE_ONE = Color.parseColor("#55000000")
        val COLOR_STROKE_TWO = Color.parseColor("#55000000")
        val COLOR_STROKE_THREE = Color.parseColor("#55000000")
        const val STROKE_WIDTH = 1
        const val SIZE_AVATAR_ONE = 60
        const val SIZE_AVATAR_TWO = 55
        const val SIZE_AVATAR_THREE = 50
        const val SiZE_AVATARS_PLACE = SIZE_AVATAR_ONE * 1.7f
        const val SHIFT_DOWN_ONE = 0
        const val SHIFT_DOWN_TWO = 30
        const val SHIFT_DOWN_THREE = 30
        const val DURATION_ANIMATION_ONE_ITEM = 300L


    }
}

