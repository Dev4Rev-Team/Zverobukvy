package ru.dev4rev.zoobukvy.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import ru.dev4rev.zoobukvy.databinding.BottomSheetDialogFragmentInstructionBinding
import ru.dev4rev.zoobukvy.utility.ui.ViewBindingDialogFragment

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
        binding.headerButtonClose.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLICK_CLOSE,
                bundleOf()
            )
        }
    }

    companion object {
        const val TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLICK_HEADER =
            "InstructionBottomSheetDialogFragmentClickHeader"
        const val TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLICK_CLOSE =
            "InstructionBottomSheetDialogFragmentClickClose"

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

        fun setOnListenerClickClose(activity: AppCompatActivity, f: (() -> Unit)?) {
            activity.supportFragmentManager.setFragmentResultListener(
                TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLICK_CLOSE,
                activity
            ) { _, _ ->
                f?.invoke()
            }
        }
    }
}