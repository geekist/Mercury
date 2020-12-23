package com.ytech.ui.tabs

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import android.util.AttributeSet
import android.widget.FrameLayout

abstract class BaseTabItem : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * 设置选中状态
     */
    abstract fun setChecked(checked: Boolean)

    /**
     * 设置消息数字。注意：一般数字需要大于0才会显示
     */
    abstract fun setMessageNumber(number: Int)

    /**
     * 设置未选中状态下的图标
     */
    abstract fun setDefaultDrawable(drawable: Drawable)

    /**
     * 设置选中状态下的图标
     */
    abstract fun setSelectedDrawable(drawable: Drawable)

    abstract fun setTitle(title: String)

    abstract fun getTitle(): String

    /**
     * 已选中的状态下再次点击时触发
     */
    fun onRepeat() {}
}