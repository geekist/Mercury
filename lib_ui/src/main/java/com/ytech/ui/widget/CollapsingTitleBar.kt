package com.ytech.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.ytech.common.common.*
import com.ytech.common.log.L
import com.ytech.ui.R

import kotlinx.android.synthetic.main.common_layout_collapsing_title_bar.view.*
import kotlinx.android.synthetic.main.common_layout_title_bar.view.mCenterLayout
import kotlinx.android.synthetic.main.common_layout_title_bar.view.mComposeLayout
import kotlinx.android.synthetic.main.common_layout_title_bar.view.mToolbar
import kotlin.math.abs

class CollapsingTitleBar : AppBarLayout, AppBarLayout.OnOffsetChangedListener {
    companion object {
        private const val EXPANDED = 0
        private const val COLLAPSED = 1
        private const val IDLE = 2
    }

    private var mCurrentState = IDLE

    private var mComposeLayoutResId = -1
    private var mCollapsingLayoutResId = -1

    private var mCenterLayoutResId = -1
    private var mCenterLayoutMatch = false

    private var mTitle: String? = null
    private var mTitleSize: Float = 0f
    private var mTitleColor = 0

    private var mActualTitleColor = 0
    private var mActualBgColor = 0
    private var mActualMenuColor = 0
    private var mActualNavIconTint = 0

    private var mNavIcon = -1
    private var mNavIconTint = 0
    private var mShowDivider = true

    private var mBgColor = 0

    private var mMenuColor = 0

    private val mDividerWidth by lazy { context.loadDimen(R.dimen.common_divider) }
    private val mDividerColor by lazy { context.loadColor(R.color.dark05) }

    private var mTvCenterTitle: AppCompatTextView? = null

    private val mDividerPaint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.TRANSPARENT
        paint.strokeWidth = mDividerWidth
        paint
    }

    // AppBarLayout完全展开的回调
    private var mOnExpanded: ((appBarLayout: AppBarLayout) -> Unit)? = null

    // AppBarLayout完全折叠的回调
    private var mOnCollapsed: ((appBarLayout: AppBarLayout) -> Unit)? = null

    // 移动AppBarLayout的回调
    private var mOnMoving: ((appBarLayout: AppBarLayout, percent: Float) -> Unit)? = null

    fun onExpanded(l: (appBarLayout: AppBarLayout) -> Unit) {
        this.mOnExpanded = l
    }

    fun onCollapsed(l: (appBarLayout: AppBarLayout) -> Unit) {
        this.mOnCollapsed = l
    }

    fun onMoving(l: (appBarLayout: AppBarLayout, percent: Float) -> Unit) {
        this.mOnMoving = l
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        with(context.obtainStyledAttributes(attributeSet, R.styleable.CollapsingTitleBar)) {
            mCenterLayoutResId = getResourceId(R.styleable.CollapsingTitleBar_ctb_center_layout, -1)
            mCenterLayoutMatch = getBoolean(R.styleable.CollapsingTitleBar_ctb_center_layout_match, false)
            mComposeLayoutResId = getResourceId(R.styleable.CollapsingTitleBar_ctb_compose_layout, -1)
            mCollapsingLayoutResId = getResourceId(R.styleable.CollapsingTitleBar_ctb_collapsing_layout, -1)
            mTitle = getString(R.styleable.CollapsingTitleBar_ctb_title)
            // 默认title文本大小为18sp
            mTitleSize =
                getFloat(
                    R.styleable.CollapsingTitleBar_ctb_title_size,
                    context.getRawSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                )
            mTitleColor = getColor(R.styleable.CollapsingTitleBar_ctb_title_color, Color.TRANSPARENT)
            mActualTitleColor = getColor(
                R.styleable.CollapsingTitleBar_ctb_actual_title_color,
                loadColor(R.color.colorGray333)
            )

            mBgColor = getColor(R.styleable.CollapsingTitleBar_ctb_bg_color, Color.TRANSPARENT)
            mActualBgColor = getColor(
                R.styleable.CollapsingTitleBar_ctb_actual_bg_color,
                loadColor(R.color.backgroundPanel)
            )
            mMenuColor = getColor(R.styleable.CollapsingTitleBar_ctb_menu_color, loadColor(R.color.white))
            mActualMenuColor =
                getColor(R.styleable.CollapsingTitleBar_ctb_actual_menu_color, loadColor(R.color.colorGray333))

            mNavIcon = getResourceId(R.styleable.CollapsingTitleBar_ctb_nav_icon, -1)
            mNavIconTint = getColor(R.styleable.CollapsingTitleBar_ctb_nav_icon_tint, loadColor(R.color.white))
            mActualNavIconTint =
                getColor(R.styleable.CollapsingTitleBar_ctb_actual_nav_icon_tint, loadColor(R.color.colorGray333))
            mShowDivider = getBoolean(R.styleable.CollapsingTitleBar_ctb_show_divider, true)
        }
        if (TextUtils.isEmpty(mTitle)) mTitle = ""
        initLayout()
    }

    private fun initLayout() {
        setBackgroundColor(mActualBgColor)
        LayoutInflater.from(context)
            .inflate(R.layout.common_layout_collapsing_title_bar, this)
        mToolbar.contentInsetStartWithNavigation = 0
        val statusBarHeight = context.getStatusBarHeight()
        mToolbar.setPadding(mToolbar.paddingLeft, statusBarHeight, mToolbar.paddingRight, mToolbar.paddingBottom)

        setNavigationIcon(mNavIcon)
        setTitleOrCenterLayout()
        setComposeLayout()
        setCollapsingLayout()
    }

    private fun setNavigationIcon(@DrawableRes navIcon: Int = -1) {
        if (navIcon != -1) {
            getToolBar().setNavigationIcon(navIcon)
            getToolBar().navigationIcon?.setColorFilter(mNavIconTint, PorterDuff.Mode.SRC_IN)
        }
    }

    fun getToolBar(): Toolbar = mToolbar

    fun getToolbarHeight() = context.getToolbarHeight()

    private fun setTitleOrCenterLayout() {
        if (mCenterLayoutResId == -1) {
            mCenterLayout.layoutResource = R.layout.common_layout_center_title
            mCenterLayout.inflatedId = R.id.tv_center_title
            mTvCenterTitle = mCenterLayout.inflate() as AppCompatTextView?
            mTvCenterTitle?.text = mTitle
            mTvCenterTitle?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize)
            mTvCenterTitle?.setTextColor(mTitleColor)
            mToolbar.title = ""
        } else {
            // 加载自定义layout
            if (mCenterLayoutMatch) {
                val lp = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                mCenterLayout.layoutParams = lp
            }
            mCenterLayout.layoutResource = mCenterLayoutResId
            mCenterLayout.inflate()
        }
    }

    private fun setComposeLayout() {
        if (mComposeLayoutResId != -1) {
            mComposeLayout.layoutResource = mComposeLayoutResId
            mComposeLayout.inflate()
        }
    }

    private fun setCollapsingLayout() {
        if (mCollapsingLayoutResId != -1) {
            mCollapsingLayout.layoutResource = mCollapsingLayoutResId
            mCollapsingLayout.inflate()
        }
    }

    fun setTitle(title: String) {
        if (mTvCenterTitle != null) {
            mTvCenterTitle!!.text = title
        } else {
            getToolBar().title = title
        }
        mTitle = title
    }

    fun setCenterTitleDrawableRight(@DrawableRes resId: Int, drawablePadding: Int) {
        if (mTvCenterTitle != null) {
            mTvCenterTitle!!.drawable(drawableRightResId = resId, drawablePadding = drawablePadding)
        }
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        canvas ?: return
        with(canvas) {
            if (mShowDivider) {
                L.e("position = ${measuredHeight - mDividerWidth}")
                drawLine(
                    0f, measuredHeight - mDividerWidth, measuredWidth.toFloat(),
                    measuredHeight - mDividerWidth, mDividerPaint
                )
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addOnOffsetChangedListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeOnOffsetChangedListener(this)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        L.e("appBarLayout = $appBarLayout verticalOffset = $verticalOffset")
        appBarLayout ?: return
        val percent = abs(verticalOffset) * 1f / appBarLayout.totalScrollRange
        mOnMoving?.invoke(appBarLayout, percent)
        when (percent) {
            0f -> {
                if (mCurrentState != EXPANDED) {
                    // 展开
                    L.e("appBarLayout已经展开")
                    mOnExpanded?.invoke(appBarLayout)
                }
                mCurrentState = EXPANDED
            }
            1f -> {
                if (mCurrentState != COLLAPSED) {
                    // 折叠
                    L.e("appBarLayout已经折叠")
                    mOnCollapsed?.invoke(appBarLayout)
                }
                mCurrentState = COLLAPSED
            }
            else -> {
                if (mCurrentState != IDLE) {
                    // 中间
                    L.e("appBarLayout中间状态")
                }
                mCurrentState = IDLE
            }
        }
        changeAlpha(percent)
    }

    fun changeAlpha(percent: Float) {
        getToolBar().setBackgroundColor(ColorUtils.adjustAlpha(mActualBgColor, percent))
        changeMenuColorTint(ColorUtils.blendColors(mMenuColor, mActualMenuColor, percent))
        changeNavigationIconTint(ColorUtils.blendColors(mNavIconTint, mActualNavIconTint, percent))
        if (mTvCenterTitle != null) {
            mTvCenterTitle!!.setTextColor(ColorUtils.adjustAlpha(mActualTitleColor, percent))
        } else {
            getToolBar().setTitleTextColor(ColorUtils.adjustAlpha(mActualTitleColor, percent))
        }

        mDividerPaint.color = ColorUtils.adjustAlpha(mDividerColor, percent)
        invalidate()
    }

    fun changeMenuColorTint(color: Int = mMenuColor) {
        for (i in 0 until mToolbar.menu.size()) {
            val menuItem = mToolbar.menu.getItem(i)
            menuItem.icon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }

    fun changeNavigationIconTint(color: Int = mNavIconTint) {
        getToolBar().navigationIcon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
}