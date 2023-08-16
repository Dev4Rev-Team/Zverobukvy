package ru.gb.zverobukvy.presentation.main_menu

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import ru.gb.zverobukvy.R

class RemovePlayerDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext()).setTitle(TITLE_REMOVE_PLAYER)
            .setIcon(R.drawable.baseline_delete_24)
            .setMessage(createMessage(arguments?.getString(KEY_NAME_PLAYER)))
            .setPositiveButton(R.string.ok) { _, _ ->
                createRemovePlayerDialogFragmentResult(arguments?.getInt(KEY_POSITION_REMOVE_PLAYER))
            }.setNegativeButton(R.string.cancel) { _, _ ->
                dismissAllowingStateLoss()
            }.create()

    private fun createMessage(namePlayer: String?) =
        if (namePlayer.isNullOrBlank()) "$MESSAGE_REMOVE_PLAYER?"
        else "$MESSAGE_REMOVE_PLAYER \"$namePlayer\"?"

    private fun createRemovePlayerDialogFragmentResult(position: Int?) =
        requireActivity().supportFragmentManager.setFragmentResult(
            MainMenuFragment.KEY_RESULT_FROM_REMOVE_PLAYER_DIALOG_FRAGMENT,
            bundleOf(KEY_POSITION_REMOVE_PLAYER to position)
        )

    companion object {
        const val KEY_NAME_PLAYER = "KeyNamePlayer"
        const val KEY_POSITION_REMOVE_PLAYER = "KeyPositionRemovePlayer"
        private const val TITLE_REMOVE_PLAYER = "Удаление игрока"
        private const val MESSAGE_REMOVE_PLAYER = "Вы действительно хотите удалить игрока"
    }

}