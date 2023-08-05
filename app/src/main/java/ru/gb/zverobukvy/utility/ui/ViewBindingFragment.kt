package ru.gb.zverobukvy.utility.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class ViewBindingFragment<T : ViewBinding>(
    private val inflateBinding: (
        inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
    ) -> T
) : BackPressedFragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}