package com.ytech.ui.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.ytech.common.text.isNotEmptyStr
import com.ytech.ui.R

open class TypefaceTextView : AppCompatTextView {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(
        context,
        attributeSet,
        android.R.attr.textViewStyle
    )

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        with(context.obtainStyledAttributes(attributeSet, R.styleable.TypefaceTextView)) {
            val typeface = getString(R.styleable.TypefaceTextView_typeface)
            if (typeface.isNotEmptyStr()) {
                val font =
                    Typeface.createFromAsset(context.assets, "fonts/$typeface") ?: Typeface.DEFAULT
                setTypeface(font)
            } else {
                setTypeface(Typeface.DEFAULT)
            }
            recycle()
        }
    }
}