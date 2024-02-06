package ru.dev4rev.kids.zoobukvy.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.dev4rev.kids.zoobukvy.databinding.BottomSheetDialogFragmentInstructionBinding
import ru.dev4rev.kids.zoobukvy.utility.ui.ViewBindingDialogFragment

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
            "instruction_bottom_sheet_dialog_fragment_click_header"
        const val TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLICK_CLOSE =
            "instruction_bottom_sheet_dialog_fragment_click_close"
        private const val TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_SHOW_INSTRUCTIONS =
            "instruction_bottom_sheet_dialog_fragment_show_instructions"
        private const val TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLOSE_ANIMATION =
            "instruction_bottom_sheet_dialog_fragment_close_animation"

        fun show(fragment: Fragment) {
            fragment.parentFragmentManager.setFragmentResult(
                TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_SHOW_INSTRUCTIONS,
                bundleOf()
            )
        }

        fun closeAnimation(activity: AppCompatActivity) {
            activity.supportFragmentManager.setFragmentResult(
                TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLOSE_ANIMATION,
                bundleOf()
            )
        }

        @JvmStatic
        fun instance(): InstructionBottomSheetDialogFragment =
            InstructionBottomSheetDialogFragment()

        fun setOnListenerShowInstruction(activity: AppCompatActivity, f: (() -> Unit)?) {
            activity.supportFragmentManager.setFragmentResultListener(
                TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_SHOW_INSTRUCTIONS,
                activity
            ) { _, _ ->
                f?.invoke()
            }
        }

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

        fun setOnListenerCloseAnimation(fragment: Fragment, f: (() -> Unit)?) {
            fragment.requireActivity().supportFragmentManager.setFragmentResultListener(
                TAG_INSTRUCTION_BOTTOM_SHEET_DIALOG_FRAGMENT_CLOSE_ANIMATION,
                fragment.viewLifecycleOwner
            ) { _, _ ->
                f?.invoke()
            }
        }
    }
}