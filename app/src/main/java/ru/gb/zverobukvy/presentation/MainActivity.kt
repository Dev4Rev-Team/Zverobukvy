package ru.gb.zverobukvy.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.presentation.main_menu.MainMenuFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

       if (savedInstanceState==null)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainMenuFragment.newInstance(), MainMenuFragment.TAG_MAIN_MENU_FRAGMENT)
            .commitAllowingStateLoss()
    }

    companion object {
        lateinit var sharedPreferences: SharedPreferences
    }

}