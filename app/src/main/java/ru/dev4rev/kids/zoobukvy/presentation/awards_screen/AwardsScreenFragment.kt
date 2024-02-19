package ru.dev4rev.kids.zoobukvy.presentation.awards_screen

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.animalLettersGameSubcomponentContainer
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.Rank
import ru.dev4rev.kids.zoobukvy.databinding.DefaultViewRatingLayoutBinding
import ru.dev4rev.kids.zoobukvy.databinding.FragmentAwardScreenMainBinding
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.kids.zoobukvy.presentation.sound.SoundEffectPlayer
import ru.dev4rev.kids.zoobukvy.presentation.sound.SoundEnum
import ru.dev4rev.kids.zoobukvy.utility.ui.viewModelProviderFactoryOf
import timber.log.Timber


class AwardsScreenFragment : Fragment() {

    private var _binding: FragmentAwardScreenMainBinding? = null
    private val binding get() = _binding!!

    private val viewRatingBinding by lazy {
        DefaultViewRatingLayoutBinding.bind(binding.viewRatingLayoutContainer.findViewById(R.id.view_rating_layout))
    }

    private val viewModel: AwardsScreenViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactoryOf {
            requireContext().animalLettersGameSubcomponentContainer
                .getAnimalLettersGameSubcomponent().awardsScreenViewModel
        })[AwardsScreenViewModelImpl::class.java]
    }

    private val soundEffectPlayer: SoundEffectPlayer by lazy {
        requireContext().animalLettersGameSubcomponentContainer.getAnimalLettersGameSubcomponent().soundEffectPlayer
    }

    private val imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl()

    private var animatorsList: MutableList<Animator> = mutableListOf()

    private var isFirstPlayerAward: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentAwardScreenMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        initView()
        soundEffectPlayer.play(SoundEnum.WORD_IS_GUESSED)
    }

    private fun initView() {
        subscribeToViewModelEvents()

        binding.root.setOnClickListener {
            if (animatorsList.isEmpty()) {
                viewModel.onNextClick()
            } else {
                animatorsList.forEach { animator ->
                    if (animator is AnimatorSet) {
                        animator.end()
                    }
                    animatorsList.remove(animator)
                }
            }
        }
    }

    private fun subscribeToViewModelEvents() {
        subscribeToMainLiveData()
        subscribeToSecondLiveData()
    }

    private fun subscribeToMainLiveData() {
        viewModel.getMainAwardsLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AwardsScreenState.Main.AwardedPlayerState -> {
                    changePlayerStartScreenState()

                    soundEffectPlayer.play(SoundEnum.NEW_AWARDED_PLAYER)
                    animatePlayerChange(it)
                }

                is AwardsScreenState.Main.CancelScreen -> {
                    requireContext().animalLettersGameSubcomponentContainer.deleteAnimalLettersGameSubcomponent()
                    parentFragmentManager.popBackStack()
                }

                is AwardsScreenState.Main.StartScreen -> {
                    soundEffectPlayer.play(SoundEnum.AWARD_SCREEN_INIT)
                    startScreenState()
                }
            }
        }
    }

    private fun subscribeToSecondLiveData() {
        viewModel.getSecondAwardsLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AwardsScreenState.Second.RankIncreaseState -> {
                    rangAwardScreenState()

                    soundEffectPlayer.play(SoundEnum.RANK_INCREASE)
                    animateRangIncrease(state)
                }

                is AwardsScreenState.Second.ViewRatingIncreaseState -> {
                    viewRatingAwardScreenState()

                    soundEffectPlayer.play(SoundEnum.VIEW_RATING_INCREASE)
                    animateViewRatingIncrease(state)
                }
            }
        }
    }


    private fun animateViewRatingIncrease(state: AwardsScreenState.Second.ViewRatingIncreaseState) {

        val target = when (state.typeCards) {
            TypeCards.ORANGE -> R.layout.orange_view_rating_layout
            TypeCards.GREEN -> R.layout.green_view_rating_layout
            TypeCards.BLUE -> R.layout.blue_view_rating_layout
            TypeCards.VIOLET -> R.layout.violet_view_rating_layout
        }

        val targetCardView = when (state.typeCards) {
            TypeCards.ORANGE -> viewRatingBinding.orangeViewRatingCard
            TypeCards.GREEN -> viewRatingBinding.greenViewRatingCard
            TypeCards.BLUE -> viewRatingBinding.blueViewRatingCard
            TypeCards.VIOLET -> viewRatingBinding.violetViewRatingCard
        }

        val targetTextView = when (state.typeCards) {
            TypeCards.ORANGE -> viewRatingBinding.orangeViewRattingTextView
            TypeCards.GREEN -> viewRatingBinding.greenViewRattingTextView
            TypeCards.BLUE -> viewRatingBinding.blueViewRattingTextView
            TypeCards.VIOLET -> viewRatingBinding.violetViewRattingTextView
        }

        /*val constraintSetStart = ConstraintSet()
        constraintSetStart.clone(requireContext(), R.layout.default_view_rating_layout)
        val transitionStart = TransitionSet().apply {
            addTransition(ChangeBounds())
        }
        transitionStart.duration = 0
        TransitionManager.beginDelayedTransition(binding.viewRatingLayoutContainer, transitionStart)
        constraintSetStart.applyTo(viewRatingBinding.viewRatingLayout)*/

        animateTransitionToRatingIncrease(state, target, targetTextView, targetCardView)

        /*val sceneLayout = when (state.typeCards) {
            TypeCards.ORANGE -> R.layout.orange_view_rating_layout
            TypeCards.GREEN -> R.layout.green_view_rating_layout
            TypeCards.BLUE -> R.layout.blue_view_rating_layout
            TypeCards.VIOLET -> R.layout.violet_view_rating_layout
        }

        val scene = Scene.getSceneForLayout(
            binding.viewRatingLayoutContainer,
            sceneLayout,
            requireContext()
        )

        TransitionManager.go(scene, TransitionSet().apply {
            setStartDelay(1000L)
            setDuration(1200L)
            addTransition(ChangeBounds())
            addTransition(ChangeTransform())
            setOrdering(TransitionSet.ORDERING_TOGETHER)
        })

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                delay(4000L)
            }
            withContext(Dispatchers.Main) {
                val scene2 = Scene.getSceneForLayout(
                    binding.viewRatingLayoutContainer,
                    R.layout.default_view_rating_layout,
                    requireContext()
                )
                TransitionManager.go(scene2, TransitionSet().apply {
                    setDuration(1200L)
                    addTransition(ChangeBounds())
                    addTransition(ChangeTransform())
                    setOrdering(TransitionSet.ORDERING_TOGETHER)
                })
            }
        }*/
    }

    private fun animateTransitionToRatingIncrease(
        state: AwardsScreenState.Second.ViewRatingIncreaseState,
        target: Int,
        targetTextView: AppCompatTextView,
        targetCardView: MaterialCardView,
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                delay(1000L)
            }
            withContext(Dispatchers.Main) {
                val constraintSetRatingIncrease = ConstraintSet()
                constraintSetRatingIncrease.clone(requireContext(), target)
                val transitionRatingIncrease =
                    TransitionSet()
                        .addTransition(ChangeBounds())
                        .addTransition(ChangeTransform())
                transitionRatingIncrease.interpolator = AnticipateInterpolator(1.0f)
                transitionRatingIncrease.duration = 2000
                transitionRatingIncrease.addTransitionListener(
                    onStart = {
                        val reductionArray = (16..24).toList().toIntArray()

                        ObjectAnimator.ofInt(*reductionArray)
                            .addUpdateViewListener {
                                targetTextView.setTextSize(
                                    TypedValue.COMPLEX_UNIT_SP,
                                    (it.animatedValue as Int).toFloat()
                                )
                            }.apply { startDelay = 500L }.setDuration(1500L).start()

                        val metrics = requireActivity().resources.displayMetrics

                        val strokeWidthArray = (2..5).toList().toIntArray()
                        ObjectAnimator.ofInt(*strokeWidthArray)
                            .addUpdateViewListener {
                                val value = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    (it.animatedValue as Int).toFloat(),
                                    metrics
                                )
                                targetCardView.strokeWidth = value.toInt()
                            }.apply { startDelay = 1000L }.setDuration(1000L).start()
                    },
                    onEnd = {
                        animateChangeStrokeColor(
                            targetCardView,
                            state.newViewRating.decoration.colorId
                        )
                        /* targetCardView.strokeColor =
                             getColorId(state.newViewRating.decoration.idColor)*/
                        animateTransitionToRatingDefault(targetTextView, targetCardView)
                    }
                )
                TransitionManager.beginDelayedTransition(
                    binding.viewRatingLayoutContainer,
                    transitionRatingIncrease
                )
                constraintSetRatingIncrease.applyTo(viewRatingBinding.viewRatingLayout)
            }
        }
    }

    private fun animateTransitionToRatingDefault(
        targetTextView: AppCompatTextView,
        targetCardView: MaterialCardView,
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                delay(2500L)
            }
            withContext(Dispatchers.Main) {
                val constraintSetRatingDefault = ConstraintSet()
                constraintSetRatingDefault.clone(
                    requireContext(),
                    R.layout.default_view_rating_layout
                )
                val transitionRatingDefault =
                    TransitionSet()
                        .addTransition(ChangeBounds())
                        .addTransition(ChangeTransform())
                transitionRatingDefault.interpolator = AnticipateInterpolator(1.0f)
                transitionRatingDefault.duration = 2000
                transitionRatingDefault.addTransitionListener(
                    onStart = {
                        val reductionArray = (24 downTo 16).toList().toIntArray()

                        ObjectAnimator.ofInt(*reductionArray)
                            .addUpdateViewListener {
                                targetTextView.setTextSize(
                                    TypedValue.COMPLEX_UNIT_SP,
                                    (it.animatedValue as Int).toFloat()
                                )
                            }.apply { startDelay = 500L }.setDuration(1500L).start()

                        val metrics = requireActivity().resources.displayMetrics

                        val strokeWidthArray = (5 downTo 2).toList().toIntArray()
                        ObjectAnimator.ofInt(*strokeWidthArray)
                            .addUpdateViewListener {
                                val value = TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    (it.animatedValue as Int).toFloat(),
                                    metrics
                                )
                                targetCardView.strokeWidth = value.toInt()
                            }.apply { startDelay = 1000L }.setDuration(1000L)
                            .start()
                    }
                )
                TransitionManager.beginDelayedTransition(
                    binding.viewRatingLayoutContainer,
                    transitionRatingDefault
                )
                constraintSetRatingDefault.applyTo(viewRatingBinding.viewRatingLayout)
            }
        }
    }

    /*private fun animatorsOfMountColorShift(): List<Animator> {
        return listOf(
            ObjectAnimator.ofFloat(
                binding.cardMountCardView,
                View.ALPHA,
                1f,
                0f
            ),
            ObjectAnimator.ofFloat(
                binding.cardMountTwoCardView,
                View.ALPHA,
                0f,
                1f
            )
        )
    }

    private fun animatorsOfHidingCardField(): List<Animator> {
        return listOf(
            ObjectAnimator.ofFloat(
                binding.cardFieldCardView,
                View.ALPHA,
                1f,
                0f
            ),
            ObjectAnimator.ofFloat(
                binding.cardFieldFieldCardView,
                View.ALPHA,
                1f,
                0f
            )
        )
    }

    private fun animatorsOfAppearanceCardField(): List<Animator> {
        return listOf(
            ObjectAnimator.ofFloat(
                binding.cardFieldCardView,
                View.ALPHA,
                0f,
                1f
            ),
            ObjectAnimator.ofFloat(
                binding.cardFieldFieldCardView,
                View.ALPHA,
                0f,
                1f
            )
        )
    }*/

    private fun animateRangIncrease(state: AwardsScreenState.Second.RankIncreaseState) {

        val reductionArray = (42 downTo 0).toList().toIntArray()
        val increaseArray = (0..42).toList().toIntArray()

        if (state.oldRank != Rank.DEFAULT) {
            rankTextDisappearance(reductionArray, state, increaseArray)
        } else {
            binding.rankTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                (0).toFloat()
            )
            rankTextAppearance(state, increaseArray)
        }
    }

    private fun rankTextDisappearance(
        reductionArray: IntArray,
        state: AwardsScreenState.Second.RankIncreaseState,
        increaseArray: IntArray,
    ) {
        ObjectAnimator.ofInt(*reductionArray)
            .addUpdateViewListener {
                binding.rankTextView.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    (it.animatedValue as Int).toFloat()
                )
            }
            .addEndListener {
                rankTextAppearance(state, increaseArray)
            }
            .setDuration(1500L)
            .start()
    }

    private fun animateChangeStrokeColor(targetCardView: MaterialCardView, colorRes: Int) {

        val metrics = requireActivity().resources.displayMetrics
        val defStrokeWidth = targetCardView.strokeWidth
        Timber.i(defStrokeWidth.toString())

        val strokeWidthArray = (5 downTo 0).toList().toIntArray()

        AnimatorSet().playOneAfterAnother(
            listOf(
                ObjectAnimator.ofInt(*strokeWidthArray)
                    .addUpdateViewListener {
                        val value = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            (it.animatedValue as Int).toFloat(),
                            metrics
                        )
                        targetCardView.strokeWidth = value.toInt()
                    }
                    .addEndListener { targetCardView.strokeColor = getColorId(colorRes) }
                    .setDuration(300L),

                ObjectAnimator.ofInt(*strokeWidthArray.reversedArray())
                    .addUpdateViewListener {
                        val value = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            (it.animatedValue as Int).toFloat(),
                            metrics
                        )
                        targetCardView.strokeWidth = value.toInt()
                    }
                    .setDuration(300L)
            )
        ).start()
    }

    private fun rankTextAppearance(
        state: AwardsScreenState.Second.RankIncreaseState,
        increaseArray: IntArray,
    ) {
        /*val color = arrayOf(
            ColorDrawable(state.oldRank.idBorderRankColor),
            ColorDrawable(state.newRank.idBorderRankColor)
        )
        val colorTransition = TransitionDrawable(color)

        binding.avatar.setBackgroundDrawable(colorTransition)
        colorTransition.startTransition(2000)*/

        binding.rankTextView.text = state.newRank.name
        binding.rankTextView.setTextColor(state.newRank.rankTextColorId)
        //binding.avatar.strokeColor = getColorId(state.newRank.idBorderRankColor)
        binding.avatar.setStrokeColor(ColorStateList.valueOf(state.newRank.borderRankColorId))

        ObjectAnimator.ofInt(*increaseArray)
            .addUpdateViewListener {
                binding.rankTextView.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    (it.animatedValue as Int).toFloat()
                )
            }
            .apply { setStartDelay(1000L) }
            .setDuration(1500L)
            .start()
    }

    /*private fun animatorsOfDisappearanceRankInFlesh(): List<Animator> {
        return listOf(
            ObjectAnimator.ofFloat(
                binding.rangTextView,
                View.ALPHA,
                1f,
                0f
            ),
            ObjectAnimator.ofFloat(
                binding.flashFrameLayout,
                View.ALPHA,
                0f,
                1f
            )
        )
    }

    private fun animatorsOfAppearanceRankFromFlesh(): List<Animator> {
        return listOf(
            ObjectAnimator.ofFloat(
                binding.rangTextView,
                View.ALPHA,
                0f,
                1f
            ),
            ObjectAnimator.ofFloat(
                binding.flashFrameLayout,
                View.ALPHA,
                1f,
                0f
            )
        )
    }*/


    private fun animatePlayerChange(state: AwardsScreenState.Main.AwardedPlayerState) {

        isFirstPlayerAward = true

        val constraintSetStart = ConstraintSet()
        constraintSetStart.clone(requireContext(), R.layout.fragment_award_screen_change_player)
        val transitionStart = TransitionSet().addTransition(ChangeBounds()).addTransition(Fade())
        transitionStart.duration = 0
        TransitionManager.beginDelayedTransition(binding.rootContainer, transitionStart)
        constraintSetStart.applyTo(binding.rootContainer)
        changePlayerStartScreenState()
        binding.playerNameTextView.text = state.playerName
        imageAvatarLoader.loadImageAvatar(state.playerAvatar, binding.playerAvatarImageView)
        binding.avatar.strokeColor = getColorId(state.rank.borderRankColorId)
        if (state.rank != Rank.DEFAULT) {
            binding.rankTextView.text = state.rank.name
            binding.rankTextView.setTextColor(state.rank.rankTextColorId)
        }

        viewRatingBinding.orangeViewRatingCard.strokeColor =
            getColorId(state.orangeViewRating.decoration.colorId)
        viewRatingBinding.orangeViewRattingTextView.text = "+${state.changeOrangeViewRating}"

        viewRatingBinding.greenViewRatingCard.strokeColor =
            getColorId(state.greenViewRating.decoration.colorId)
        viewRatingBinding.greenViewRattingTextView.text = "+${state.changeGreenViewRating}"

        viewRatingBinding.blueViewRatingCard.strokeColor =
            getColorId(state.blueViewRating.decoration.colorId)
        viewRatingBinding.blueViewRattingTextView.text = "+${state.changeBlueViewRating}"

        viewRatingBinding.violetViewRatingCard.strokeColor =
            getColorId(state.violetViewRating.decoration.colorId)
        viewRatingBinding.violetViewRattingTextView.text = "+${state.changeVioletViewRating}"

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                delay(1000L)
            }
            withContext(Dispatchers.Main) {
                val constraintSetEnd = ConstraintSet()
                constraintSetEnd.clone(requireContext(), R.layout.fragment_award_screen_main)
                val transitionEnd =
                    TransitionSet().addTransition(ChangeBounds()).addTransition(Fade())
                transitionEnd.interpolator = AnticipateOvershootInterpolator(1.0f)
                transitionEnd.duration = 1200
                TransitionManager.beginDelayedTransition(binding.rootContainer, transitionEnd)
                constraintSetEnd.applyTo(binding.rootContainer)
                changePlayerEndScreenState()
            }
        }


        /*ObjectAnimator.ofInt(*reductionArray)
            .addUpdateViewListener {
                binding.playerNameTextView.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    (it.animatedValue as Int).toFloat()
                )
            }
            .addEndListener {
                binding.playerNameTextView.text = state.playerName

                ObjectAnimator.ofInt(*increaseArray)
                    .addUpdateViewListener {
                        binding.playerNameTextView.setTextSize(
                            TypedValue.COMPLEX_UNIT_PX,
                            (it.animatedValue as Int).toFloat()
                        )
                    }
                    .setDuration(DURATION_OF_PLAYER_APPEARANCE)
                    .start()
            }
            .setDuration(DURATION_OF_PLAYER_DISAPPEARANCE)
            .start()*/
    }

    private fun durationBeforePlayerAward(): Long {
        return if (isFirstPlayerAward) {
            isFirstPlayerAward = false
            DURATION_BEFORE_FIRST_PLAYER_AWARD
        } else DURATION_BEFORE_REST_PLAYER_AWARDS
    }

    private fun startScreenState() {
        binding.startScreen.visibility = VISIBLE
        binding.awardedPlayerLayout.visibility = INVISIBLE
        binding.viewRatingLayoutContainer.visibility = INVISIBLE
    }

    private fun changePlayerStartScreenState() {
        binding.startScreen.visibility = INVISIBLE
        binding.awardedPlayerLayout.visibility = VISIBLE
        binding.viewRatingLayoutContainer.visibility = INVISIBLE
    }

    private fun changePlayerEndScreenState() {
        binding.startScreen.visibility = INVISIBLE
        binding.awardedPlayerLayout.visibility = VISIBLE
        binding.viewRatingLayoutContainer.visibility = VISIBLE
    }

    private fun rangAwardScreenState() {

    }

    private fun viewRatingAwardScreenState() {

    }

    private fun getColorId(resId: Int): Int {
        return requireActivity().getColor(resId)
    }

    private fun TypeCards.getColorId(): Int {
        return when (this) {
            TypeCards.ORANGE -> requireActivity().getColor(R.color.color_orange)
            TypeCards.GREEN -> requireActivity().getColor(R.color.color_green)
            TypeCards.BLUE -> requireActivity().getColor(R.color.color_blue)
            TypeCards.VIOLET -> requireActivity().getColor(R.color.color_violet)
        }
    }

    companion object {

        // AllAwards
        const val DURATION_BEFORE_FIRST_PLAYER_AWARD = 300L
        const val DURATION_BEFORE_REST_PLAYER_AWARDS = 100L

        // PlayerChange
        const val DURATION_OF_PLAYER_APPEARANCE = 300L
        const val DURATION_OF_PLAYER_DISAPPEARANCE = 300L

        // RankIncrease
        const val DURATION_OF_TEXT_APPEARANCE = 3000L
        const val DURATION_OF_TEXT_DISAPPEARANCE = 3000L

        // ViewRatingIncrease
        const val DURATION_OF_INCREASING_COUNTER = 1000L
        const val DURATION_OF_CARD_STROKE_APPEARANCE = 2500L
        const val DURATION_OF_CARD_STROKE_SHIFT = 700L
        const val DURATION_OF_CARD_STROKE_DISAPPEARANCE = 2500L

        const val TAG = "AwardsScreenFragmentTag"

        fun newInstance() = AwardsScreenFragment()
    }
}

private fun Transition.addTransitionListener(
    onStart: ((Transition) -> Unit)? = null,
    onEnd: ((Transition) -> Unit)? = null,
    onCancel: ((Transition) -> Unit)? = null,
    onPause: ((Transition) -> Unit)? = null,
    onResume: ((Transition) -> Unit)? = null,
) {
    this.addListener(object : Transition.TransitionListener {
        override fun onTransitionStart(transition: Transition) {
            onStart?.invoke(transition)
        }

        override fun onTransitionEnd(transition: Transition) {
            onEnd?.invoke(transition)
        }

        override fun onTransitionCancel(transition: Transition) {
            onCancel?.invoke(transition)
        }

        override fun onTransitionPause(transition: Transition) {
            onPause?.invoke(transition)
        }

        override fun onTransitionResume(transition: Transition) {
            onResume?.invoke(transition)
        }

    })
}

private fun AnimatorSet.playOneAfterAnother(list: List<Animator>): AnimatorSet {
    this.playSequentially(list)

    return this
}


private fun Animator.start(animations: MutableList<Animator>): Animator {
    animations.add(this)
    this.addEndListener { animations.remove(this) }
    this.start()
    return this
}

private fun ValueAnimator.addUpdateViewListener(function: (animation: ValueAnimator) -> Unit): ValueAnimator {
    this.addUpdateListener { function.invoke(it) }
    return this
}


private fun AnimatorSet.playAtOnce(items: List<Animator>): AnimatorSet {
    this.playTogether(items)
    return this
}

private fun Animator.addEndListener(onEndFunction: () -> Unit): Animator {
    addListener(
        onEnd = { onEndFunction.invoke() }
    )
    return this
}
