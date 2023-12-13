package ru.gb.zverobukvy.presentation.awards_screen

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.animalLettersGameSubcomponentContainer
import ru.gb.zverobukvy.appComponent
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
            requireContext().appComponent.awardsScreenViewModel
        })[AwardsScreenViewModelImpl::class.java]
    }

    private val soundEffectPlayer: SoundEffectPlayer by lazy {
        requireContext().animalLettersGameSubcomponentContainer.getAnimalLettersGameSubcomponent().soundEffectPlayer
    }

    private var animatorsList: MutableList<Animator> = mutableListOf()

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

                    animateRangIncrease(state)
                }

                is AwardsScreenState.Second.ViewRatingIncreaseState -> {
                    viewRatingAwardScreenState()

                    animateViewRatingIncrease(state)
                }
            }
        }
    }


    private fun animateViewRatingIncrease(state: AwardsScreenState.Second.ViewRatingIncreaseState) {
        binding.cardFieldCardView.setCardBackgroundColor(state.typeCards.getColorId())
        binding.cardMountCardView.setCardBackgroundColor(getColorId(state.oldViewRating.decoration.idColor))
        binding.cardsCounterTextView.text = state.oldViewRating.rating.toString()

        AnimatorSet()
            .playOneAfterAnother(
                listOf(
                    ObjectAnimator.ofInt(
                        *(state.oldViewRating.rating.rangeTo(99).toList().toIntArray())
                    ).addUpdateViewListener {
                        binding.cardsCounterTextView.text = it.animatedValue.toString()
                    }.setDuration(DURATION_OF_INCREASING_COUNTER),
                    AnimatorSet()
                        .playAtOnce(animatorsOfHidingCardField())
                        .addEndListener {
                            binding.cardsCounterTextView.text = 0.toString()
                            binding.cardMountTwoCardView.alpha = 0f
                            binding.cardMountTwoCardView.setCardBackgroundColor(getColorId(state.newViewRating.decoration.idColor))
                        }
                        .setDuration(DURATION_OF_CARD_STROKE_DISAPPEARANCE),
                    AnimatorSet()
                        .playAtOnce(animatorsOfMountColorShift())
                        .addEndListener {
                            binding.cardMountCardView.setCardBackgroundColor(getColorId(state.newViewRating.decoration.idColor))
                            binding.cardMountCardView.alpha = 1f
                            binding.cardMountTwoCardView.setCardBackgroundColor(getColorId(R.color.white))
                        }
                        .setDuration(DURATION_OF_CARD_STROKE_SHIFT),
                    AnimatorSet()
                        .playAtOnce(animatorsOfAppearanceCardField())
                        .setDuration(DURATION_OF_CARD_STROKE_APPEARANCE),
                    ObjectAnimator.ofInt(
                        *(0.rangeTo(state.newViewRating.rating).toList()
                            .toIntArray())
                    ).addUpdateViewListener {
                        binding.cardsCounterTextView.text =
                            it.animatedValue.toString()
                    }.setDuration(DURATION_OF_INCREASING_COUNTER)
                )
            )
            .start(animatorsList)

        /*ObjectAnimator.ofInt(
            *(state.oldViewRating.rating.rangeTo(99).toList().toIntArray())
        ).addUpdateViewListener {
            binding.cardsCounterTextView.text = it.animatedValue.toString()
        }.addEndListener {
            AnimatorSet()
                .playAtOnce(animatorsOfHidingCardField())
                .addEndListener {
                    binding.cardsCounterTextView.text = 0.toString()
                    binding.cardMountTwoCardView.alpha = 0f
                    binding.cardMountTwoCardView.setCardBackgroundColor(getColorId(state.newViewRating.decoration.idColor))
                    AnimatorSet()
                        .playAtOnce(animatorsOfMountColorShift())
                        .addEndListener {
                            binding.cardMountCardView.setCardBackgroundColor(getColorId(state.newViewRating.decoration.idColor))
                            binding.cardMountCardView.alpha = 1f
                            binding.cardMountTwoCardView.setCardBackgroundColor(getColorId(R.color.white))

                            AnimatorSet()
                                .playAtOnce(animatorsOfAppearanceCardField())
                                .addEndListener {
                                    ObjectAnimator.ofInt(
                                        *(0.rangeTo(state.newViewRating.rating).toList()
                                            .toIntArray())
                                    ).addUpdateViewListener {
                                        binding.cardsCounterTextView.text =
                                            it.animatedValue.toString()
                                    }.setDuration(COUNTER_ANIM_DELAY).start(animatorsList)
                                }
                                .setDuration(ANIM_DELAY).start(animatorsList)
                        }
                        .setDuration(ANIM_DELAY).start(animatorsList)
                }
                .setDuration(ANIM_DELAY).start(animatorsList)
        }.setDuration(COUNTER_ANIM_DELAY).start(animatorsList)*/

    }

    private fun animatorsOfMountColorShift(): List<Animator> {
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
    }

    private fun animateRangIncrease(state: AwardsScreenState.Second.RankIncreaseState) {
        binding.rangTextView.text = state.oldRank.name
        binding.rangTextView.setTextColor(state.oldRank.idRankTextColor)

        AnimatorSet()
            .playOneAfterAnother(
                listOf(
                    AnimatorSet()
                        .playAtOnce(animatorsOfDisappearanceRankInFlesh())
                        .addEndListener {
                            binding.rangTextView.text = state.newRank.name
                            binding.rangTextView.setTextColor(state.newRank.idRankTextColor)
                        }
                        .setDuration(DURATION_OF_TEXT_DISAPPEARANCE),
                    AnimatorSet()
                        .playAtOnce(animatorsOfAppearanceRankFromFlesh())
                        .setDuration(DURATION_OF_TEXT_APPEARANCE)
                )
            ).start(animatorsList)

        /*AnimatorSet()
            .playAtOnce(animatorsOfDisappearanceRankInFlesh())
            .addEndListener {
                binding.rangTextView.text = state.newRank.name
                binding.rangTextView.setTextColor(state.newRank.idRankTextColor)

                AnimatorSet()
                    .playAtOnce(animatorsOfAppearanceRankFromFlesh())
                    .setDuration(ANIM_DELAY).start()
            }
            .setDuration(ANIM_DELAY).start()*/
    }

    private fun animatorsOfDisappearanceRankInFlesh(): List<Animator> {
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
    }

    private fun subscribeToMainLiveData() {
        viewModel.getMainAwardsLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AwardsScreenState.Main.AwardedPlayerState -> {
                    changePlayerScreenState()

                    animatePlayerChange(it)
                }

                is AwardsScreenState.Main.CancelScreen -> {
                    requireContext().animalLettersGameSubcomponentContainer.deleteAnimalLettersGameSubcomponent()
                    parentFragmentManager.popBackStack()
                }

                is AwardsScreenState.Main.StartScreen -> {
                    startScreenState()
                }
            }
        }
    }

    private fun animatePlayerChange(state: AwardsScreenState.Main.AwardedPlayerState) {
        val textSize = binding.playerNameTextView.textSize.toInt()
        val reductionArray = 0.rangeTo(textSize).toList().toIntArray().reversedArray()
        val increaseArray = 0.rangeTo(textSize).toList().toIntArray()

        ObjectAnimator.ofInt(*reductionArray)
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
            .start()
    }

    private fun startScreenState() {
        binding.startScreen.visibility = View.VISIBLE
        binding.mainScreen.visibility = View.INVISIBLE
    }

    private fun changePlayerScreenState() {
        binding.startScreen.visibility = View.INVISIBLE
        binding.mainScreen.visibility = View.VISIBLE
        binding.viewRatingAwardLayout.visibility = View.INVISIBLE
        binding.rangAwardLayout.visibility = View.INVISIBLE
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
