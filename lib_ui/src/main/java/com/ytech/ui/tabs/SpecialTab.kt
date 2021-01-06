package com.ytech.ui.tabs

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.flyco.tablayout.utils.UnreadMsgUtils
import com.ytech.common.common.loadColor
import com.ytech.common.device.dp2px
import com.ytech.ui.R
import kotlinx.android.synthetic.main.common_layout_special_tab.view.*

class SpecialTab : BaseTabItem {
    private var mDefaultDrawable: Drawable? = null
    private var mCheckedDrawable: Drawable? = null

    private var mDefaultTextColor = loadColor(R.color.text_subtitle)
    private var mCheckedTextColor = loadColor(R.color.secondaryColor)

    private var mChecked: Boolean = false

    private var mAnimationDrawable: AnimationDrawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.common_layout_special_tab, this)
    }

    fun initialize(@DrawableRes drawableRes: Int, @DrawableRes checkedDrawableRes: Int, title: String) {
        mDefaultDrawable = ContextCompat.getDrawable(context, drawableRes)
        mCheckedDrawable = ContextCompat.getDrawable(context, checkedDrawableRes)
        mTitle.text = title
    }

    fun initialize(drawableRes: Drawable, checkedDrawableRes: Drawable, title: String) {
        mDefaultDrawable = drawableRes
        mCheckedDrawable = checkedDrawableRes
        mTitle.text = title
    }

    fun initialize(@DrawableRes animDrawableRes: Int, title: String) {
        val drawable = ContextCompat.getDrawable(context, animDrawableRes)
        if (drawable is AnimationDrawable) {
            mAnimationDrawable = drawable
        }
        mTitle.text = title
    }

    fun getAnimationDrawable() = mAnimationDrawable

    private fun startAnimation() {
        stopAnimation(mAnimationDrawable)
        mAnimationDrawable?.isOneShot = true
        mAnimationDrawable?.start()
    }

    private fun stopAnimation(vararg animations: AnimationDrawable?) {
        animations.forEach { it?.stop() }
    }

    override fun setChecked(checked: Boolean) {
        when (checked) {
            true -> {
                startAnimation()
                mIcon.setImageDrawable(
                    if (mAnimationDrawable != null) mAnimationDrawable else mCheckedDrawable
                )
                mTitle.setTextColor(mCheckedTextColor)
            }
            else -> {
                stopAnimation(mAnimationDrawable)
                mAnimationDrawable?.setVisible(true, true)
                mIcon.setImageDrawable(
                    if (mAnimationDrawable != null) mAnimationDrawable else mDefaultDrawable
                )
                mTitle.setTextColor(mDefaultTextColor)
            }
        }
        mChecked = checked
    }

    override fun setSelectedDrawable(drawable: Drawable) {
        mCheckedDrawable = drawable
        if (!mChecked) {
            mIcon.setImageDrawable(drawable)
        }
    }

    override fun getTitle(): String = mTitle.text.toString()

    override fun setMessageNumber(number: Int) {
        when {
            number < 0 -> mMsgTip.visibility = View.INVISIBLE
            number == 0 -> {
                UnreadMsgUtils.show(mMsgTip, 0)
                UnreadMsgUtils.setSize(mMsgTip, dp2px(context, 8f))
            }
            else -> UnreadMsgUtils.show(mMsgTip, number)
        }
    }

    override fun setDefaultDrawable(drawable: Drawable) {
        mDefaultDrawable = drawable
        if (!mChecked) {
            mIcon.setImageDrawable(drawable)
        }
    }

    override fun setTitle(title: String) {
        mTitle.text = title
    }

    fun setTextDefaultColor(@ColorInt color: Int) {
        mDefaultTextColor = color
    }

    fun setTextCheckedColor(@ColorInt color: Int) {
        mCheckedTextColor = color
    }
}