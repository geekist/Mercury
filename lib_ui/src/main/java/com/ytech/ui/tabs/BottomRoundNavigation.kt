package com.ytech.ui.tabs

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import com.ytech.common.common.buildBundle
import com.ytech.common.device.dp2px
import com.ytech.common.log.L
import com.ytech.common.view.views
import com.ytech.ui.R

class BottomRoundNavigation : ConstraintLayout {
    companion object {
        const val TAG = "BottomRoundNavigation"
        const val SELECTED_INDEX = "selected_index"
    }

    override fun onSaveInstanceState(): Parcelable? {
        return buildBundle(
            TAG to super.onSaveInstanceState(),
            SELECTED_INDEX to mSelectedIndex
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            complete(state.getInt(SELECTED_INDEX, mSelectedIndex))
            super.onRestoreInstanceState(state.getParcelable(TAG))
            return
        }
        super.onRestoreInstanceState(state)
    }

    private var mTabHeight = 0

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mPath = Path()

    private var mIconSize = 0

    private var mIsLinear = false

    private var mBezierWidth: Int = 0

    private var mBezierHeight: Int = 0

    private var mShadowRadius: Float = 0f

    private var mShadowColor: Int = 0
    private var mNavBackgroundColor: Int = 0

    private var mSelectedIndex: Int = 0
    private var mSelectedTab: SpecialTab? = null

    // 返回值如果返回true表示拦截此次click 不会选中当前tab
    private var mOnItemClick: ((index: Int, oldIndex: Int) -> Boolean)? = null

    private var mOnItemReselected: ((index: Int) -> Unit)? = null

    private var mOnCenterIconClick: (() -> Unit)? = null

    /**
     * 中间的按钮布局
     * 里面放置一个ImageView显示按钮
     * 如果mIsLinear 宽高一致即正方形
     * 否则高度match 宽度为mBezierWidth
     */
    private var mCenterLayout: FrameLayout? = null

    private val mCenterLayoutGuideline: Guideline by lazy {
        val line = Guideline(context)
        line.id = View.generateViewId()
        val centerLayoutGuideLineLp = generateDefaultLayoutParams()
        centerLayoutGuideLineLp.orientation = LayoutParams.HORIZONTAL
        line.layoutParams = centerLayoutGuideLineLp
        line
    }
    private val mTabsGuideline: Guideline by lazy {
        val line = Guideline(context)
        line.id = View.generateViewId()
        val tabsGuideLineLp = generateDefaultLayoutParams()
        tabsGuideLineLp.orientation = LayoutParams.HORIZONTAL
        line.layoutParams = tabsGuideLineLp
        line
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        with(context.obtainStyledAttributes(attributeSet, R.styleable.BottomRoundNavigation)) {
            mTabHeight = getDimensionPixelSize(
                R.styleable.BottomRoundNavigation_brn_tab_height,
                dp2px(context, 50.5f)
            )
            mIconSize = getDimensionPixelSize(
                R.styleable.BottomRoundNavigation_brn_icon_size, dp2px(context, 20f)
            )
            mIsLinear = getBoolean(
                R.styleable.BottomRoundNavigation_brn_is_linear, false
            )
            mBezierWidth = getDimensionPixelSize(
                R.styleable.BottomRoundNavigation_brn_bezier_width,
                dp2px(context, 80f)
            )
            mBezierHeight = getDimensionPixelSize(
                R.styleable.BottomRoundNavigation_brn_bezier_height,
                dp2px(context, 13f)
            )
            mShadowRadius = getDimension(
                R.styleable.BottomRoundNavigation_brn_shadow_radius,
                dp2px(context, 4f).toFloat()
            )
            mShadowColor = getColor(
                R.styleable.BottomRoundNavigation_brn_shadow_color, Color.parseColor("#33000000")
            )
            mNavBackgroundColor = getColor(
                R.styleable.BottomRoundNavigation_brn_background_color, Color.parseColor("#ffffff")
            )
            recycle()
        }

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mShadowPaint.color = mShadowColor
        mShadowPaint.style = Paint.Style.FILL
        if (mShadowRadius > 0) {
            val bf = BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.SOLID)
            mShadowPaint.maskFilter = bf
        }

        mPaint.color = mNavBackgroundColor
        mPaint.style = Paint.Style.FILL

        setWillNotDraw(false)

        mCenterLayoutGuideline.setGuidelineBegin(
            if (mIsLinear) mShadowRadius.toInt() else mBezierHeight
        )
        mTabsGuideline.setGuidelineBegin(
            if (mIsLinear) mShadowRadius.toInt() else (mBezierHeight + mShadowRadius).toInt()
        )

        addView(mCenterLayoutGuideline)
        addView(mTabsGuideline)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val centerX = measuredWidth * 0.5f
        var x: Float
        mPath.reset()
        when (mIsLinear) {
            true -> {
                // 线性
                mPath.moveTo(0f, mShadowRadius)
                mPath.lineTo(measuredWidth.toFloat(), mShadowRadius)
                mPath.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat())
                mPath.lineTo(0f, measuredHeight.toFloat())
                mPath.close()
            }
            else -> {
                // 中间凸起
                mPath.moveTo(0f, mBezierHeight + mShadowRadius)
                x = centerX - mBezierWidth * 0.5f
                mPath.lineTo(x, mBezierHeight + mShadowRadius)
                mPath.cubicTo(
                    x + mBezierWidth / 4, mBezierHeight + mShadowRadius,
                    x + mBezierWidth / 4, 0f + mShadowRadius,
                    x + mBezierWidth / 2, 0f + mShadowRadius
                )
                x = centerX + mBezierWidth * 0.5f
                mPath.cubicTo(
                    x - mBezierWidth / 4, 0f + mShadowRadius,
                    x - mBezierWidth / 4, mBezierHeight + mShadowRadius,
                    x, mBezierHeight + mShadowRadius
                )
                mPath.lineTo(measuredWidth.toFloat(), mBezierHeight + mShadowRadius)
                mPath.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat())
                mPath.lineTo(0f, measuredHeight.toFloat())
                mPath.close()
            }
        }

        canvas?.drawPath(mPath, mShadowPaint)
        canvas?.drawPath(mPath, mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val lp = layoutParams
        lp.width = LayoutParams.MATCH_PARENT
        lp.height = mTabHeight +
                if (mIsLinear) mShadowRadius.toInt() else (mBezierHeight + mShadowRadius).toInt()
        layoutParams = lp
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        handler.post { requestLayout() }
    }

    /**
     * 获取不包括凸起部分的tab栏高度
     */
    fun getTabHeight(): Int {
        val tabHeight = mTabHeight
        return if (tabHeight >= 0) tabHeight else 0
    }

    fun addItem(@DrawableRes drawable: Int, @DrawableRes checkedDrawable: Int, title: String): BottomRoundNavigation {
        val tab = buildTab(drawable = drawable, checkedDrawable = checkedDrawable, title = title)
        addView(tab)
        return this
    }

    fun addItem(@DrawableRes animDrawable: Int, title: String): BottomRoundNavigation {
        val tab = buildTab(animDrawable = animDrawable, title = title)
        addView(tab)
        return this
    }

    private fun buildTab(
        @DrawableRes drawable: Int = 0, @DrawableRes checkedDrawable: Int = 0,
        @DrawableRes animDrawable: Int = 0, title: String
    ): SpecialTab {
        val specialTab = SpecialTab(context)
        specialTab.id = View.generateViewId()
        specialTab.tag = buildTabIndex()
        if (animDrawable != 0) {
            specialTab.initialize(animDrawable, title)
        } else {
            specialTab.initialize(drawable, checkedDrawable, title)
        }

        specialTab.setOnClickListener { onClickTab(it as SpecialTab) }
        return specialTab
    }

    private fun onClickTab(tab: SpecialTab) {
        val selectedIndex: Int = tab.tag as Int
        when (selectedIndex) {
            mSelectedIndex -> {
                // 重复选中
                tab.getAnimationDrawable()?.let { tab.setChecked(true) }
                mOnItemReselected?.invoke(mSelectedIndex)
            }
            else -> {
                val interceptClick = mOnItemClick?.invoke(selectedIndex, mSelectedIndex) ?: false

                if (!interceptClick) {
                    tab.setChecked(true)
                    mSelectedTab?.setChecked(false)

                    mSelectedIndex = selectedIndex
                    mSelectedTab = tab
                }
            }
        }
    }

    /**
     * 计算tab的index
     * 2表示2根GuideLine
     */
    private fun buildTabIndex(): Int = childCount - 2 - if (mCenterLayout != null) 1 else 0

    fun addCenterItem(@DrawableRes drawable: Int): BottomRoundNavigation {
        if (mCenterLayout == null) {
            mCenterLayout = FrameLayout(context)
            mCenterLayout!!.id = View.generateViewId()
            addView(mCenterLayout)

            val centerIcon = AppCompatImageView(context)
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            lp.gravity = Gravity.CENTER
            centerIcon.layoutParams = lp
            centerIcon.scaleType = ImageView.ScaleType.FIT_CENTER
            centerIcon.setImageResource(drawable)
            centerIcon.setOnClickListener { mOnCenterIconClick?.invoke() }
            mCenterLayout!!.addView(centerIcon)
        }
        return this
    }

    fun complete(selectedIndex: Int = 0) {
        val tabs = mutableListOf<View>()
        views.filter { it is SpecialTab || it is FrameLayout }
            .forEach {
                if (it is SpecialTab) it.setChecked(false)
                tabs.add(it)
            }

        if (tabs.size <= 1)
            L.e("tab数量必须大于1")
        // throw IllegalArgumentException("tab数量必须大于1")
        for ((index, view) in tabs.withIndex()) {
            if (view is SpecialTab && view.tag == selectedIndex) {
                mSelectedIndex = view.tag as Int
                mSelectedTab = view
                mSelectedTab!!.setChecked(true)
            }

            val lp: LayoutParams = view.layoutParams as LayoutParams
            lp.topToTop = if (view is SpecialTab) mTabsGuideline.id else mCenterLayoutGuideline.id
            lp.bottomToBottom = LayoutParams.PARENT_ID
            lp.height = 0
            when (index) {
                0 -> {
                    lp.width = 0
                    lp.leftToLeft = LayoutParams.PARENT_ID
                    lp.rightToLeft = tabs[index + 1].id
                }
                tabs.size - 1 -> {
                    lp.width = 0
                    lp.leftToRight = tabs[index - 1].id
                    lp.rightToRight = LayoutParams.PARENT_ID
                }
                else -> {
                    lp.width = if (!(view is SpecialTab)) {
                        if (mIsLinear) 0 else mBezierWidth
                    } else 0
                    lp.leftToRight = tabs[index - 1].id
                    lp.rightToLeft = tabs[index + 1].id
                }
            }
            view.layoutParams = lp
        }
    }

    /**
     * number小于0表示隐藏
     * 等于0表示显示小红点
     * 大于0表示显示number
     */
    fun setMessageNumber(index: Int = 0, number: Int) {
        val tab: SpecialTab? = getTabAtIndex(index)
        tab?.setMessageNumber(number)
    }

    private fun getTabAtIndex(index: Int = 0): SpecialTab? {
        var tab: SpecialTab? = null
        views.filter { it is SpecialTab }
            .forEach {
                it as SpecialTab
                if (index == it.tag) {
                    tab = it
                    return@forEach
                }
            }
        return tab
    }

    /**
     * 改变选中的tab
     */
    fun changeTab(index: Int) {
        if (mSelectedIndex == index) return
        getTabAtIndex(index)?.let { onClickTab(it) }
    }

    fun setOnItemClick(l: (index: Int, oldIndex: Int) -> Boolean) {
        this.mOnItemClick = l
    }

    fun setOnItemReselected(l: (index: Int) -> Unit) {
        this.mOnItemReselected = l
    }

    fun setOnCenterIconClick(l: () -> Unit) {
        this.mOnCenterIconClick = l
    }

    fun getNavBackgroundColor() = mNavBackgroundColor
}