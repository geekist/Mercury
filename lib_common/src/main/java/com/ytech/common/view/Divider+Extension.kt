package com.ytech.common.view

import android.content.Context
import androidx.annotation.ColorRes
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration
import com.ytech.common.device.dp2px

fun Context.generateHorizontalDivider(
    sizeDp: Float, @ColorRes color: Int,
    marginDp: Float = 0f
): HorizontalDividerItemDecoration {
    return HorizontalDividerItemDecoration.Builder(this)
        .size(dp2px(this, sizeDp))
        .colorResId(color)
        .margin(dp2px(this, marginDp))
        .build()
}

fun Context.generateHorizontalDivider(
    sizeDp: Float, @ColorRes color: Int,
    marginLeftDp: Float = 0f,
    marginRightDp: Float = 0f
): HorizontalDividerItemDecoration {
    return HorizontalDividerItemDecoration.Builder(this)
        .size(dp2px(this, sizeDp))
        .colorResId(color)
        .margin(dp2px(this, marginLeftDp), dp2px(this, marginRightDp))
        .build()
}

fun Context.generateVerticalDivider(sizeDp: Float, @ColorRes color: Int): VerticalDividerItemDecoration {
    return VerticalDividerItemDecoration.Builder(this)
        .size(dp2px(this, sizeDp))
        .colorResId(color)
        .build()
}