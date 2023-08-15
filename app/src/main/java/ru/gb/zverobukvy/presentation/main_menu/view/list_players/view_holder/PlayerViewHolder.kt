package ru.gb.zverobukvy.presentation.main_menu.view.list_players.view_holder

import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.databinding.ItemPlayerInSettingsBinding
import ru.gb.zverobukvy.domain.entity.PlayerInSettings

class PlayerViewHolder(
    override val viewBinding: ItemPlayerInSettingsBinding,
    private val itemPlayerClickListener: (Int) -> Unit,
    private val editMenuClickListener: (Int) -> Unit
) :
    BaseViewHolder(viewBinding) {
    override fun bindView(playerInSetting: PlayerInSettings?) {
        playerInSetting?.let {
            viewBinding.run {
                itemPlayerInSettingsTextViewPlayerName.text = it.player.name
                if (it.isSelectedForGame)
                    playerCardView.setBackgroundColor(itemView.context.getColor(R.color.color_card_green))
                else
                    playerCardView.setBackgroundColor(itemView.context.getColor(R.color.color_card_blue))
                playerCardView.setOnClickListener {
                    itemPlayerClickListener(this@PlayerViewHolder.adapterPosition)
                }
                itemPlayerInSettingsImageButtonEdit.setOnClickListener {
                    editMenuClickListener(this@PlayerViewHolder.adapterPosition)
                }
            }
        }
    }
}
