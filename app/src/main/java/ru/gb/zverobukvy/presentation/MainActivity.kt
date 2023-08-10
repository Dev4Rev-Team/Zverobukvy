package ru.gb.zverobukvy.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.presentation.game_zverobukvy.GameZverobukvyFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameStart =
            GameZverobukvyFragment.GameStart(
                listOf(TypeCards.BLUE, TypeCards.GREEN), listOf(
                    PlayerInGame("Nik1", 0),
                    PlayerInGame("Nik2", 0)
                )
            )

        val toFragment = GameZverobukvyFragment.newInstance(gameStart)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, toFragment, TAG_TO_FRAGMENT)
            .addToBackStack(TAG_TO_FRAGMENT).commit()
    }

    companion object {
        private const val TAG_TO_FRAGMENT = "GameZverobukvyFragment"
    }

}