package com.ytech.ui.tabs

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ytech.ui.R
import kotlinx.android.synthetic.main.common_layout_round_msg_view.view.*
import java.util.*


class RoundMsgView : FrameLayout {
    private var mMessageNumber: Int = 0
    private var mHasMessage: Boolean = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context, attributeSet, defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.common_layout_round_msg_view, this, true)
        mMessages.typeface = Typeface.DEFAULT_BOLD
        mMessages.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f)
    }

    fun setMessageNumber(number: Int) {
        mMessageNumber = number

        if (mMessageNumber > 0) {
            mOval.visibility = View.INVISIBLE
            mMessages.visibility = View.VISIBLE

            if (mMessageNumber < 10) {
                mMessages.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f)
            } else {
                mMessages.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10f)
            }

            if (mMessageNumber <= 99) {
                mMessages.text = String.format(Locale.ENGLISH, "%d", mMessageNumber)
            } else {
                mMessages.text = String.format(Locale.ENGLISH, "%d+", 99)
            }
        } else {
            mMessages.visibility = View.INVISIBLE
            if (mHasMessage) {
                mOval.visibility = View.VISIBLE
            }
        }
    }

    fun setHasMessage(hasMessage: Boolean) {
        mHasMessage = hasMessage

        if (hasMessage) {
            mOval.visibility = if (mMessageNumber > 0) View.INVISIBLE else View.VISIBLE
        } else {
            mOval.visibility = View.INVISIBLE
        }
    }

    fun tintMessageBackground(@ColorInt color: Int) {
        val drawable = tinting(ContextCompat.getDrawable(context, R.drawable.shape_round)!!, color)
        ViewCompat.setBackground(mOval, drawable)
        ViewCompat.setBackground(mMessages, drawable)
    }

    private fun tinting(drawable: Drawable, color: Int): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        wrappedDrawable.mutate()
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }

    fun setMessageNumberColor(@ColorInt color: Int) {
        mMessages.setTextColor(color)
    }

    fun getMessageNumber(): Int {
        return mMessageNumber
    }

    fun hasMessage(): Boolean {
        return mHasMessage
    }
}