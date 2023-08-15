package ru.gb.zverobukvy.presentation.main_menu.view.list_players.view_holder

import android.widget.Toast
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.databinding.FragmentMainMenuItemPlayerModeViewBinding
import ru.gb.zverobukvy.domain.entity.PlayerInSettings
import kotlin.coroutines.coroutineContext

class PlayerViewHolder(
    override val viewBinding: FragmentMainMenuItemPlayerModeViewBinding,
    private val itemPlayerClickListener: (Int) -> Unit,
    private val editMenuClickListener: (Int) -> Unit
) :
    BaseViewHolder(viewBinding) {
    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewBinding.run {
                playerNameTextView.text = it.player.name
                if (it.isSelectedForGame) {
                    playerStateCardView.setCardBackgroundColor(itemView.context.getColor(R.color.color_green_pastel))
                } else {
                    playerStateCardView.setCardBackgroundColor(itemView.context.getColor(R.color.color_red_pastel))
                }
                playerCardView.setOnClickListener {
                    itemPlayerClickListener(this@PlayerViewHolder.adapterPosition)
                }
                editImageButton.setOnClickListener {
                    editMenuClickListener(this@PlayerViewHolder.adapterPosition)
                }

            }
        }
    }
}
