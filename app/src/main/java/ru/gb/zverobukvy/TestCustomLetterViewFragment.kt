package ru.gb.zverobukvy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.gb.zverobukvy.databinding.FragmentTestCustomLetterViewBinding


class TestCustomLetterViewFragment : Fragment() {

    lateinit var binding: FragmentTestCustomLetterViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTestCustomLetterViewBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setChar.setOnClickListener {
            binding.customLetterView.setChar("A"[0])
        }

        binding.setTrue.setOnClickListener {
            binding.customLetterView.setTrue()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TestCustomLetterViewFragment()
    }
}