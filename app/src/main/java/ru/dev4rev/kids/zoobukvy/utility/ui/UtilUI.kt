package ru.dev4rev.kids.zoobukvy.utility.ui

import android.content.Context
import android.util.TypedValue
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

fun CardView.enableClickAnimation() {
    isClickable = true
    val outValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
    foreground = ContextCompat.getDrawable(context, outValue.resourceId)
}

fun Context.dipToPixels(dipValue: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, resources.displayMetrics)
        .toInt()
fun Context.dipToPixels(dipValue: Int) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue.toFloat(), resources.displayMetrics)
        .toInt()
