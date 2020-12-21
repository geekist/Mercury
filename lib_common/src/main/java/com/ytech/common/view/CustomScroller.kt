package com.ytech.common.view

import android.content.Context
import android.widget.OverScroller

class CustomScroller constructor(context: Context) {
    private val mScroller by lazy { OverScroller(context) }

    fun fling(
        startX: Int, startY: Int, velocityX: Int, velocityY: Int,
        minX: Int, maxX: Int, minY: Int, maxY: Int, overX: Int, overY: Int
    ) {
        mScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY)
    }

    fun isFinished() = mScroller.isFinished

    fun forceFinished(finished: Boolean) {
        mScroller.forceFinished(finished)
    }

    fun getCurrX() = mScroller.currX

    fun getCurrY() = mScroller.currY

    fun computeScrollOffset() = mScroller.computeScrollOffset()
}