package ru.gb.zverobukvy.presentation.main_menu
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gb.zverobukvy.R

class MainMenuFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainMenuFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}