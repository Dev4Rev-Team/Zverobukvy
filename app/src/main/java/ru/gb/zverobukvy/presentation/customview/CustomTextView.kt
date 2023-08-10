package ru.gb.zverobukvy.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat

class CustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            this,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
            this, AUTO_SIZE_MIN_TEXT_SIZE, AUTO_SIZE_MAX_TEXT_SIZE, AUTO_SIZE_STEP_GRANULARITY,
            TypedValue.COMPLEX_UNIT_SP
        )

        gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(height, height)
    }

    companion object {
        private const val AUTO_SIZE_MIN_TEXT_SIZE = 8
        private const val AUTO_SIZE_MAX_TEXT_SIZE = 8 * 100
        private const val AUTO_SIZE_STEP_GRANULARITY = 8
    }
}
