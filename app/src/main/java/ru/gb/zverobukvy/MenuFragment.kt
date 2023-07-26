package ru.gb.zverobukvy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.gb.zverobukvy.databinding.FragmentMenuBinding
import ru.gb.zverobukvy.presentation.customview.CustomCardTable

class MenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        binding.customCardButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, TestCustomCardFragment.newInstance())
            transaction.addToBackStack("ok")
            transaction.commit()
        }
        binding.customCardTableButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, TestCustomCardTableFragment.newInstance())
            transaction.addToBackStack("ok")
            transaction.commit()
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}