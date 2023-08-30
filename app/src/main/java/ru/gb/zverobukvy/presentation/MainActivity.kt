package ru.gb.zverobukvy.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.presentation.main_menu.MainMenuFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMainMenu(savedInstanceState)
        initBottomSheet()
    }

    private fun initMainMenu(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.container,
                    MainMenuFragment.newInstance(),
                    MainMenuFragment.TAG_MAIN_MENU_FRAGMENT
                )
                .commitAllowingStateLoss()
    }

    private fun initBottomSheet() {
        MainMenuFragment.setOnListenerShowInstruction(this) {
            val bottomFragment =
                InstructionBottomSheetDialogFragment.instance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerBottomSheet, bottomFragment)
                .commit()
            findViewById<View>(R.id.containerBottomSheet).visibility = View.VISIBLE
        }
    }
}