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
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    private var mainFlow: Flow? = null
    private val cardViewWinnerList = mutableListOf<CardView>()
    private val sizeAvatarList = mutableListOf(SIZE_AVATAR_ONE, SIZE_AVATAR_TWO, SIZE_AVATAR_THREE)
    private val colorAvatarList =
        mutableListOf(COLOR_STROKE_ONE, COLOR_STROKE_TWO, COLOR_STROKE_THREE)
    private val shiftDownList = mutableListOf(SHIFT_DOWN_ONE, SHIFT_DOWN_TWO, SHIFT_DOWN_THREE)
    private var imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl


    init {

    }

    fun show(list: List<PlayerUI>) {
        if (mainFlow != null) {
            removeAllViews()
            cardViewWinnerList.clear()
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
            cardViewWinnerList.add(cardView)

            ratingFlow[place]?.addView(cardView)
        }
    }

    private fun initContentView(mainFlow: Flow?): Map<Int, Flow> {

        val flowRatingMap = mutableMapOf<Int, Flow>()
        for (i in 0 until COUNT_WINNER) {
            flowRatingMap[i] = createNewFlow().apply {
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
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        setHorizontalGap(context.dipToPixels(HORIZONTAL_GAP))
        setMaxElementsWrap(context.dipToPixels(COUNT_WINNER))
        setWrapMode(Flow.WRAP_CHAIN)
        setHorizontalStyle(Flow.CHAIN_PACKED)
        setVerticalStyle(Flow.CHAIN_PACKED)

        this@CustomImageRatingView.addView(this)
    }

    private fun createNewFlow() = Flow(context).apply {
        id = generateViewId()
        layoutParams =
            Constraints.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        setHorizontalGap(context.dipToPixels(HORIZONTAL_GAP))
        setVerticalGap(context.dipToPixels(VERTICAL_GAP))
        setMaxElementsWrap(MAX_AVATAR_HORIZONTAL)
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
            elevation = 1f
            layoutParams = LayoutParams(sizeDp, sizeDp)
            strokeColor = colorAvatarList[place]
            strokeWidth = context.dipToPixels(STROKE_WIDTH)
            this@CustomImageRatingView.addView(this)
        }
        val imageView = CustomImageView(context).apply {
            this.layoutParams = FrameLayout.LayoutParams(
                LayoutParams.MATCH_CONSTRAINT, LayoutParams.MATCH_CONSTRAINT
            )
            id = generateViewId()
            visibility = VISIBLE
            imageAvatarLoader.loadImageAvatar(player.player.avatar, this)
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            setPadding(context.dipToPixels(PADDING_IMAGE))
            setBackgroundColor(colorAvatarList[place])
        }
        cardView.addView(imageView)
        return cardView
    }


    companion object {
        private const val COUNT_WINNER = 3
        const val MAX_AVATAR_HORIZONTAL = 2
        const val HORIZONTAL_GAP = 15
        const val VERTICAL_GAP = 15
        const val PADDING_IMAGE = 7
        val COLOR_STROKE_ONE = Color.parseColor("#EEE020")
        val COLOR_STROKE_TWO = Color.parseColor("#C0C0C0")
        val COLOR_STROKE_THREE = Color.parseColor("#C48322")
        const val STROKE_WIDTH = 5
        const val SIZE_AVATAR_ONE = 60
        const val SIZE_AVATAR_TWO = 55
        const val SIZE_AVATAR_THREE = 50
        const val SHIFT_DOWN_ONE = 0
        const val SHIFT_DOWN_TWO = 30
        const val SHIFT_DOWN_THREE = 30


    }
}