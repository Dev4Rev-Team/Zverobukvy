package ru.gb.zverobukvy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.gb.zverobukvy.databinding.FragmentTestCustomCardBinding

class TestCustomCardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentTestCustomCardBinding.inflate(inflater, container, false)
        initUI(binding)
        return binding.root
    }

    private fun initUI(binding: FragmentTestCustomCardBinding) {
        binding.openButton.setOnClickListener {
            binding.card.setOpenCard(true)
        }

        binding.closeButton.setOnClickListener {
            binding.card.setOpenCard(false)
        }

        binding.visibleButton.setOnClickListener {
            binding.card.setVisibilityCard(true)
        }

        binding.invisibleButton.setOnClickListener {
            binding.card.setVisibilityCard(false)
        }

        binding.card.setOnClickCardListener(389) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
        }

        binding.resIdButton.setOnClickListener {
            binding.card.setSrcFromRes(R.drawable.ic_launcher_background, R.drawable.img)
        }

        binding.resStringButton.setOnClickListener {
            binding.card.setSrcFromRes("img", "ic_launcher_foreground")
        }
        binding.assertsButton.setOnClickListener {

            binding.card.setSrcFromAssert("img1.jpg", "img2.jpg")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TestCustomCardFragment()
    }
}