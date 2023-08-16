package ru.gb.zverobukvy.utility.ui

import android.util.TypedValue
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

fun CardView.enableClickAnimation() {
    isClickable = true
    val outValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
    foreground = ContextCompat.getDrawable(context, outValue.resourceId)
}