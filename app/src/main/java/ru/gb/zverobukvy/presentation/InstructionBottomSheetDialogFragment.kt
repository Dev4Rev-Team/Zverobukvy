package ru.gb.zverobukvy.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import ru.gb.zverobukvy.databinding.BottomSheetDialogFragmentInstructionBinding
import ru.gb.zverobukvy.utility.ui.ViewBindingDialogFragment

class InstructionBottomSheetDialogFragment :
    ViewBindingDialogFragment<BottomSheetDialogFragmentInstructionBinding>(
        BottomSheetDialogFragmentInstructionBinding::inflate
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerImageView.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLICK_HEADER,
                bundleOf()
            )
        }
    }

    companion object {
        const val TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLICK_HEADER =
            "InstructionBottomSheetDialogFragmentClickHeader"

        @JvmStatic
        fun instance(): InstructionBottomSheetDialogFragment =
            InstructionBottomSheetDialogFragment()

        fun setOnListenerClickHeader(activity: AppCompatActivity, f: (() -> Unit)?) {
            activity.supportFragmentManager.setFragmentResultListener(
                TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLICK_HEADER,
                activity
            ) { _, _ ->
                f?.invoke()
            }
        }
    }
}