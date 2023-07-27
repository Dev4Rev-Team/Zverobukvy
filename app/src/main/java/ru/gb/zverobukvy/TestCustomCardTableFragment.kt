package ru.gb.zverobukvy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gb.zverobukvy.databinding.FragmentTestCustomCardTableBinding
import ru.gb.zverobukvy.presentation.customview.CustomCard
import ru.gb.zverobukvy.presentation.customview.CustomCardTable

class TestCustomCardTableFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentTestCustomCardTableBinding.inflate(inflater, container, false)
        initUI(binding)
        return binding.root
    }

    private fun initUI(binding: FragmentTestCustomCardTableBinding) {
        binding.setListButton.setOnClickListener {
            val letterCardList = listOf(
                CustomCardTable.LetterCard('A', false, "img1.png"),
                CustomCardTable.LetterCard('B', false, "img2.png"),
                CustomCardTable.LetterCard('C', false, "img3.png"),
                CustomCardTable.LetterCard('D', true, "img4.png"),
                CustomCardTable.LetterCard('E', false, "img5.png"),
                CustomCardTable.LetterCard('F', false, "img6.png"),
                CustomCardTable.LetterCard('A', true, "img7.png"),
                CustomCardTable.LetterCard('A', true, "img8.png"),
                CustomCardTable.LetterCard('A', false, "img9.png"),
                //                CustomCardTable.LetterCard('A', false, "img10.png"),
                //                CustomCardTable.LetterCard('C', false, "img3.png"),
                //                CustomCardTable.LetterCard('D', true, "img4.png"),
                //                CustomCardTable.LetterCard('E', false, "img5.png"),
                //                CustomCardTable.LetterCard('F', false, "img6.png"),
                //                CustomCardTable.LetterCard('A', true, "img7.png"),
                //                CustomCardTable.LetterCard('A', true, "img8.png"),
                //                CustomCardTable.LetterCard('A', false, "img9.png"),
                //                CustomCardTable.LetterCard('A', false, "img10.png"),
                //                CustomCardTable.LetterCard('E', false, "img5.png"),
                //                CustomCardTable.LetterCard('F', false, "img6.png"),
                //                CustomCardTable.LetterCard('A', true, "img7.png"),
                //                CustomCardTable.LetterCard('A', true, "img8.png"),
                //                CustomCardTable.LetterCard('A', false, "img9.png"),
                //                CustomCardTable.LetterCard('A', false, "img10.png"),
                //                CustomCardTable.LetterCard('C', false, "img3.png"),
                //                CustomCardTable.LetterCard('D', true, "img4.png"),
                //                CustomCardTable.LetterCard('E', false, "img5.png"),
                //                CustomCardTable.LetterCard('F', false, "img6.png"),
                //                CustomCardTable.LetterCard('A', true, "img7.png"),
                //                CustomCardTable.LetterCard('A', true, "img8.png"),
                //                CustomCardTable.LetterCard('A', false, "img9.png"),
                //                CustomCardTable.LetterCard('A', false, "img10.png"),
                //                CustomCardTable.LetterCard('A', false, "img8.png")
            )
            binding.cardTable.setListItem(letterCardList, "img1.png") {
                CustomCard(requireContext()).apply {
                    radius = 50f
                    elevation = 3f
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TestCustomCardTableFragment()
    }
}