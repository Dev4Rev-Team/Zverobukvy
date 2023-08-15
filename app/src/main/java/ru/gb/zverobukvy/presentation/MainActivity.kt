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

//        val gameStart =
//            GameZverobukvyFragment.GameStart(
//                listOf(TypeCards.BLUE, TypeCards.GREEN), listOf(
//                    PlayerInGame("Nik1", 0),
//                    PlayerInGame("Nik2", 0)
//                )
//            )
//
//        val toFragment = GameZverobukvyFragment.newInstance(gameStart)
//
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.container, toFragment, TAG_TO_FRAGMENT)
//            .addToBackStack(TAG_TO_FRAGMENT).commit()


        val toFragment = MainMenuFragment.newInstance()

       if (savedInstanceState==null)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, toFragment, MainMenuFragment.TAG_MAIN_MENU_FRAGMENT)
            .addToBackStack(TAG_TO_FRAGMENT).commit()
    }

    companion object {
        private const val TAG_TO_FRAGMENT = "GameZverobukvyFragment"
        lateinit var sharedPreferences: SharedPreferences
    }

}