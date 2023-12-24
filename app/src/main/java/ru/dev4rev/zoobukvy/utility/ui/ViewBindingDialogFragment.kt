package ru.dev4rev.zoobukvy.utility.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

abstract class ViewBindingDialogFragment<T : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater,
        parent: ViewGroup?, attachToParent: Boolean,
    ) -> T,
) : DialogFragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflateBinding(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
