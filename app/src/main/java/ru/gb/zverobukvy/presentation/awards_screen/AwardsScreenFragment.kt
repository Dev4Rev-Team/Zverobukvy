package ru.gb.zverobukvy.presentation.awards_screen

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.animalLettersGameSubcomponentContainer
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import ru.gb.zverobukvy.data.view_rating_provider.Rank
import ru.gb.zverobukvy.databinding.FragmentAwardsScreenBinding
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.presentation.sound.SoundEffectPlayer
import ru.gb.zverobukvy.presentation.sound.SoundEnum
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf

class AwardsScreenFragment : Fragment() {

    private var _binding: FragmentAwardsScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AwardsScreenViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactoryOf {
            requireContext().animalLettersGameSubcomponentContainer
                .getAnimalLettersGameSubcomponent().awardsScreenViewModel
        })[AwardsScreenViewModelImpl::class.java]
    }

    private val soundEffectPlayer: SoundEffectPlayer by lazy {
        requireContext().animalLettersGameSubcomponentContainer.getAnimalLettersGameSubcomponent().soundEffectPlayer
    }

    private val imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

    private var animatorsList: MutableList<Animator> = mutableListOf()

    private var isFirstPlayerAward: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentAwardsScreenBinding.inflate(layoutInflater, container, false)
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


    private fun subscribeToSecondLiveData() {
        viewModel.getSecondAwardsLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AwardsScreenState.Second.RankIncreaseState -> {
                    rangAwardScreenState()

                    soundEffectPlayer.play(SoundEnum.RANK_INCREASE)
                    animateRankIncrease(state)
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
        binding.cardFieldUpperCardView.setCardBackgroundColor(state.typeCards.getColorId())
        binding.cardMountUpperCardView .setCardBackgroundColor(getColorId(state.oldViewRating.decoration.idColor))
        binding.cardsCounterTextView.text = state.oldViewRating.rating.toString()

        AnimatorSet()
            .playOneAfterAnother(
                listOf(
                    ObjectAnimator.ofFloat(
                        binding.awardsScreen,
                        View.ALPHA,
                        0f,
                        1f
                    ).setDuration(durationBeforePlayerAward()),
                    ObjectAnimator.ofInt(
                        *(state.oldViewRating.rating.rangeTo(99).toList().toIntArray())
                    ).addUpdateViewListener {
                        binding.cardsCounterTextView.text = it.animatedValue.toString()
                    }
                        .addEndListener {
                            binding.cardMountLowerCardView.setCardBackgroundColor(getColorId(state.newViewRating.decoration.idColor))
                            binding.cardsCounterTextView.text = 1.toString()
                        }
                        .setDuration(DURATION_OF_INCREASING_COUNTER),
                    /*AnimatorSet()
                        .playAtOnce(animatorsOfHidingCardField())
                        .addEndListener {
                            binding.cardsCounterTextView.text = 1.toString()
                            binding.cardMountLowerCardView.alpha = 0f
                            binding.cardMountLowerCardView.setCardBackgroundColor(getColorId(state.newViewRating.decoration.idColor))
                        }
                        .setDuration(DURATION_OF_CARD_STROKE_DISAPPEARANCE),*/
                    AnimatorSet()
                        .playAtOnce(animatorsOfMountColorShift())
                        .addEndListener {
                            binding.cardMountUpperCardView.setCardBackgroundColor(getColorId(state.newViewRating.decoration.idColor))
                            binding.cardMountUpperCardView.alpha = 1f
                            binding.cardMountLowerCardView.setCardBackgroundColor(getColorId(R.color.white))
                        }
                        .apply { startDelay = 300L }
                        .setDuration(DURATION_OF_CARD_STROKE_SHIFT),
                    /*AnimatorSet()
                        .playAtOnce(animatorsOfAppearanceCardField())
                        .setDuration(DURATION_OF_CARD_STROKE_APPEARANCE),*/
                    ObjectAnimator.ofInt(
                        *(1.rangeTo(state.newViewRating.rating).toList()
                            .toIntArray())
                    ).addUpdateViewListener {
                        binding.cardsCounterTextView.text =
                            it.animatedValue.toString()
                    }.setDuration(DURATION_OF_INCREASING_COUNTER)
                )
            )
            .start(animatorsList)
    }

    private fun animatorsOfMountColorShift(): List<Animator> {
        return listOf(
            ObjectAnimator.ofFloat(
                binding.cardMountUpperCardView,
                View.Y,
                0f,
                700f
            )
        )
        return listOf(
            ObjectAnimator.ofFloat(
                binding.cardMountUpperCardView,
                View.ALPHA,
                1f,
                0f
            ),
            ObjectAnimator.ofFloat(
                binding.cardMountLowerCardView,
                View.ALPHA,
                0f,
                1f
            )
        )
        return listOf(
            ObjectAnimator.ofFloat(
                binding.cardMountUpperCardView,
                View.ALPHA,
                1f,
                0f
            )
        )
    }

    private fun animatorsOfHidingCardField(): List<Animator> {
        return listOf(
            ObjectAnimator.ofFloat(
                binding.cardFieldUpperCardView,
                View.ALPHA,
                1f,
                0f
            ),
            ObjectAnimator.ofFloat(
                binding.cardFieldLowerCardView,
                View.ALPHA,
                1f,
                0f
            )
        )
    }

    private fun animatorsOfAppearanceCardField(): List<Animator> {
        return listOf(
            ObjectAnimator.ofFloat(
                binding.cardFieldUpperCardView,
                View.ALPHA,
                0f,
                1f
            ),
            ObjectAnimator.ofFloat(
                binding.cardFieldLowerCardView,
                View.ALPHA,
                0f,
                1f
            )
        )
    }

    private fun animateRankIncrease(state: AwardsScreenState.Second.RankIncreaseState) {
        if (state.oldRank != Rank.DEFAULT)
            binding.rankTextView.text = state.oldRank.name
        binding.rankTextView.setTextColor(state.oldRank.idRankTextColor)
        binding.avatar.strokeColor = getColorId(state.oldRank.idBorderRankColor)

        AnimatorSet()
            .playOneAfterAnother(
                listOf(
                    ObjectAnimator.ofFloat(
                        binding.awardsScreen,
                        View.ALPHA,
                        0f,
                        1f
                    ).setDuration(durationBeforePlayerAward()),
                    AnimatorSet()
                        .playAtOnce(animatorsOfDisappearanceRankInFlesh())
                        .addEndListener {
                            binding.rankTextView.text = state.newRank.name
                            binding.rankTextView.setTextColor(state.newRank.idRankTextColor)
                            binding.avatar.strokeColor = getColorId(state.newRank.idBorderRankColor)
                        }
                        .setDuration(DURATION_OF_TEXT_DISAPPEARANCE),
                    AnimatorSet()
                        .playAtOnce(animatorsOfAppearanceRankFromFlesh())
                        .setDuration(DURATION_OF_TEXT_APPEARANCE)
                )
            ).start(animatorsList)
    }

    private fun animatorsOfDisappearanceRankInFlesh(): List<Animator> {
        return listOf(
            ObjectAnimator.ofFloat(
                binding.rankTextView,
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
                binding.rankTextView,
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

    private fun animatePlayerChange(state: AwardsScreenState.Main.AwardedPlayerState) {
        val textSize = binding.playerNameTextView.textSize.toInt()
        val reductionArray = 0.rangeTo(textSize).toList().toIntArray().reversedArray()
        val increaseArray = 0.rangeTo(textSize).toList().toIntArray()

        imageAvatarLoader.loadImageAvatar(state.playerAvatar, binding.playerAvatarImageView)

        isFirstPlayerAward = true

        val constraintSetStart = ConstraintSet()
        constraintSetStart.clone(requireContext(), R.layout.fragment_award_screen_awarded_player)
        val transitionStart = TransitionSet().addTransition(ChangeBounds()).addTransition(Fade())
        transitionStart.interpolator = AnticipateOvershootInterpolator(1.0f)
        transitionStart.duration = 0
        TransitionManager.beginDelayedTransition(binding.rootContainer, transitionStart)
        constraintSetStart.applyTo(binding.rootContainer)
        changePlayerStartScreenState()
        binding.playerNameTextView.text = state.playerName

        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                delay(1000L)
            }
            withContext(Dispatchers.Main) {
                val constraintSetEnd = ConstraintSet()
                constraintSetEnd.clone(requireContext(), R.layout.fragment_awards_screen)
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
        }
        else DURATION_BEFORE_REST_PLAYER_AWARDS
    }

    private fun startScreenState() {
        binding.startScreen.visibility = View.VISIBLE
        binding.awardedPlayerScreen.visibility = View.INVISIBLE
        binding.playerChangeBackgroundScreen.visibility = View.INVISIBLE
        binding.awardsScreen.visibility = View.INVISIBLE
    }

    private fun changePlayerStartScreenState() {
        binding.startScreen.visibility = View.INVISIBLE
        binding.awardedPlayerScreen.visibility = View.VISIBLE
        binding.playerChangeBackgroundScreen.visibility = View.VISIBLE
        binding.awardsScreen.visibility = View.INVISIBLE

        binding.viewRatingAwardLayout.visibility = View.INVISIBLE
        binding.rangAwardLayout.visibility = View.INVISIBLE
    }

    private fun changePlayerEndScreenState() {
        binding.startScreen.visibility = View.INVISIBLE
        binding.awardedPlayerScreen.visibility = View.VISIBLE
        binding.playerChangeBackgroundScreen.visibility = View.INVISIBLE
        binding.awardsScreen.visibility = View.VISIBLE
    }

    private fun rangAwardScreenState() {
        binding.viewRatingAwardLayout.visibility = View.INVISIBLE
        binding.rangAwardLayout.visibility = View.VISIBLE
    }

    private fun viewRatingAwardScreenState() {
        binding.viewRatingAwardLayout.visibility = View.VISIBLE
        binding.rangAwardLayout.visibility = View.INVISIBLE
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
        const val DURATION_OF_CARD_STROKE_SHIFT = 2000L
        const val DURATION_OF_CARD_STROKE_DISAPPEARANCE = 2500L

        const val TAG = "AwardsScreenFragmentTag"

        fun newInstance() = AwardsScreenFragment()
    }
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
