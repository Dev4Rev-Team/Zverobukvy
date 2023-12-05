package ru.gb.zverobukvy.presentation.awards_screen

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.appComponent
import ru.gb.zverobukvy.databinding.FragmentAwardsScreenBinding
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf

class AwardsScreenFragment : Fragment() {

    private var _binding: FragmentAwardsScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AwardsScreenViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactoryOf {
            requireContext().appComponent.awardsScreenViewModel
        })[AwardsScreenViewModelImpl::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = FragmentAwardsScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToViewModelEvents()

        binding.root.setOnClickListener { viewModel.onNextClick() }
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

        /*AnimatorSet()
            .playOneAfterAnother(
                listOf(
                    ObjectAnimator.ofInt(
                        *(state.oldViewRating.rating.rangeTo(99).toList().toIntArray())
                    ).addUpdateViewListener {
                        binding.cardsCounterTextView.text = it.animatedValue.toString()
                    }.setDuration(COUNTER_ANIM_DELAY),
                    AnimatorSet()
                        .playAtOnce(animatorsOfHidingCardField())
                        .addEndListener {
                            binding.cardsCounterTextView.text = 0.toString()
                            binding.cardMountTwoCardView.alpha = 0f
                            binding.cardMountTwoCardView.setCardBackgroundColor(getColorId(state.newViewRating.decoration.idColor))
                        }
                        .setDuration(ANIM_DELAY),
                    AnimatorSet()
                        .playAtOnce(animatorsOfMountColorShift())
                        .addEndListener {
                            binding.cardMountCardView.setCardBackgroundColor(getColorId(state.newViewRating.decoration.idColor))
                            binding.cardMountCardView.alpha = 1f
                            binding.cardMountTwoCardView.setCardBackgroundColor(getColorId(R.color.white))
                        }
                        .setDuration(ANIM_DELAY),
                    AnimatorSet()
                        .playAtOnce(animatorsOfAppearanceCardField())
                        .setDuration(ANIM_DELAY),
                    ObjectAnimator.ofInt(
                        *(0.rangeTo(state.newViewRating.rating).toList()
                            .toIntArray())
                    ).addUpdateViewListener {
                        binding.cardsCounterTextView.text =
                            it.animatedValue.toString()
                    }.setDuration(COUNTER_ANIM_DELAY)
                    )
            )
            .setDuration(10000L)
            .start()*/

        ObjectAnimator.ofInt(
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
                                    }.setDuration(COUNTER_ANIM_DELAY).start()
                                }
                                .setDuration(ANIM_DELAY).start()
                        }
                        .setDuration(ANIM_DELAY).start()
                }
                .setDuration(ANIM_DELAY).start()
        }.setDuration(COUNTER_ANIM_DELAY).start()

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
            .playAtOnce(animatorsOfDisappearanceRankInFlesh())
            .addEndListener {
                binding.rangTextView.text = state.newRank.name
                binding.rangTextView.setTextColor(state.newRank.idRankTextColor)

                AnimatorSet()
                    .playAtOnce(animatorsOfAppearanceRankFromFlesh())
                    .setDuration(ANIM_DELAY).start()
            }
            .setDuration(ANIM_DELAY).start()
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

                    binding.playerNameTextView.text = it.playerName
                }

                is AwardsScreenState.Main.CancelScreen -> {
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun changePlayerScreenState() {
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

        const val ANIM_DELAY = 3000L
        const val COUNTER_ANIM_DELAY = 1000L

        const val TAG = "AwardsScreenFragmentTag"

        fun newInstance() = AwardsScreenFragment()
    }
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
