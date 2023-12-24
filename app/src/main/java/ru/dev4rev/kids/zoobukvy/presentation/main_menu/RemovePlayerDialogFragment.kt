package ru.dev4rev.kids.zoobukvy.presentation.main_menu

import androidx.core.os.bundleOf
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.utility.ui.BaseDialogFragment

class RemovePlayerDialogFragment : BaseDialogFragment() {

    override fun initPositiveButton(){
        binding.yesButton.setOnClickListener {
            createRemovePlayerDialogFragmentResult(arguments?.getInt(KEY_POSITION_REMOVE_PLAYER))
            dismissAllowingStateLoss()
        }
    }

    override fun initNegativeButton(){
        binding.noButton.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun initMessageTextView(){
        binding.messageTextView.text = getString(R.string.main_menu_fragment_delete_player)
    }

    private fun createRemovePlayerDialogFragmentResult(position: Int?) =
        requireActivity().supportFragmentManager.setFragmentResult(
            MainMenuFragment.KEY_RESULT_FROM_REMOVE_PLAYER_DIALOG_FRAGMENT,
            bundleOf(KEY_POSITION_REMOVE_PLAYER to position)
        )

    companion object {
        @JvmStatic
        fun newInstance(): RemovePlayerDialogFragment =
            RemovePlayerDialogFragment()

        const val KEY_POSITION_REMOVE_PLAYER = "KeyPositionRemovePlayer"

        const val TAG_REMOVE_PLAYER_DIALOG_FRAGMENT = "RemovePlayerDialogFragment"
    }
}