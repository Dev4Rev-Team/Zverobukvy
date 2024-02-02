package ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.game_is_over_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.dev4rev.kids.zoobukvy.animalLettersGameSubcomponentContainer
import ru.dev4rev.kids.zoobukvy.databinding.DialogFragmentGameIsOverBinding
import ru.dev4rev.kids.zoobukvy.presentation.review.ReviewImpl
import ru.dev4rev.kids.zoobukvy.utility.parcelable
import ru.dev4rev.kids.zoobukvy.utility.ui.ViewBindingFragment
import ru.dev4rev.kids.zoobukvy.utility.ui.viewModelProviderFactoryOf


class GameIsOverDialogFragment : ViewBindingFragment<DialogFragmentGameIsOverBinding>(
    DialogFragmentGameIsOverBinding::inflate
) {
    val viewModel: GameIsOverDialogViewModel by lazy {
        ViewModelProvider(this,
            viewModelProviderFactoryOf {
                requireContext().animalLettersGameSubcomponentContainer
                    .getAnimalLettersGameSubcomponent()
                    .gameIsOverDialogViewModel
            })[GameIsOverDialogViewModelImpl::class.java]
    }

    private val review by lazy { ReviewImpl(viewModel.getIsUserFeedback()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: DataGameIsOverDialog? = arguments?.parcelable(DATE)
        binding.playersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            data?.let {
                adapter = PlayersRVAdapter(
                    it.list,
                    viewModel.getPlayersBeforeGame(),
                    viewModel.getPlayersAfterGame()
                )
            }
        }

        binding.okButton.setOnClickListener {
            review.launchReviewFlow(requireActivity()) {
                requireContext().animalLettersGameSubcomponentContainer.deleteAnimalLettersGameSubcomponent()
                parentFragmentManager.popBackStack()
            }
        }

        data?.time.also { binding.timeTextView.text = it }
        //todo deleter test
//        data?.let {
//            it.list[0].scoreInCurrentGame = 1
//            it.list[1].scoreInCurrentGame = 2
//            it.list[2].scoreInCurrentGame = 3
//            it.list[3].scoreInCurrentGame = 1
//            it.list[4].scoreInCurrentGame = 2
//            it.list[5].scoreInCurrentGame = 2
//            it.list[6].scoreInCurrentGame = 2
//            it.list[7].scoreInCurrentGame = 2
//            it.list[8].scoreInCurrentGame = 4
//            it.list[9].scoreInCurrentGame = 4
//            it.list[10].scoreInCurrentGame = 4
//            it.list[11].scoreInCurrentGame = 4
//            it.list[12].scoreInCurrentGame = 4
//            it.list[13].scoreInCurrentGame = 4
//            it.list[14].scoreInCurrentGame = 4
//        }
        data?.let {
            binding.winnerAvatarCustomImageRatingView.show(it.list)
//            binding.winnerAvatarCustomImageRatingView.show(it.list.sortedByDescending { srt -> srt.scoreInCurrentGame })
        }

        review.requestReviewFlow(requireActivity())
    }

    companion object {
        const val TAG = "GameIsOverDialogFragment"
        private const val DATE = "DATE"

        @JvmStatic
        fun instance(data: DataGameIsOverDialog): GameIsOverDialogFragment {
            val arg = bundleOf(DATE to data)
            return GameIsOverDialogFragment().apply { arguments = arg }
        }
    }

}