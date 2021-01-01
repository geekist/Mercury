package com.ytech.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.ytech.common.common.*
import com.ytech.common.view.setHeight
import com.ytech.ui.R

import kotlinx.android.synthetic.main.common_layout_title_bar.view.*

open class TitleBar : AppBarLayout {
    companion object {
        /*const val SCROLL_FLAG_SCROLL = 1
        const val SCROLL_FLAG_SNAP = 16
        const val SCROLL_FLAG_ENTER_ALWAYS = 4*/
    }

    private var mToolbarHeight = -1

    private var mCenterLayoutResId = -1
    private var mCenterLayoutMatch = false
    private var mComposeLayoutResId = -1

    private var mTitle: String? = null
    private var mTitleSize: Float = 0f
    private var mTitleColor = 0
    private var mActualTitleColor = 0

    private var mNavIcon = -1
    private var mNavIconTint = 0
    private var mIsCenterTitle = true
    private var mIsStartTitle = false
    private var mIsBoldTitle = false
    private var mShowDivider = true
    private var mSupportImmersiveBar = true

    private var mBgColor = 0
    private var mActualBgColor = 0

    private var mEnableTint = true

    private var mMenuColor = 0
    private var mActualMenuColor = 0

    private val mDividerWidth by lazy { context.loadDimen(R.dimen.common_divider) }
    private val mDividerColor by lazy { context.loadColor(R.color.dark05) }

    private var mTvCenterTitle: AppCompatTextView? = null

    private var mScrollFlags = -1

    private val mDividerPaint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = mDividerColor
        paint.strokeWidth = mDividerWidth
        paint
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        with(context.obtainStyledAttributes(attributeSet, R.styleable.TitleBar)) {
            mToolbarHeight = getDimensionPixelSize(R.styleable.TitleBar_tb_toolbar_height, -1)
            mCenterLayoutResId = getResourceId(R.styleable.TitleBar_tb_center_layout, -1)
            mCenterLayoutMatch = getBoolean(R.styleable.TitleBar_tb_center_layout_match, false)
            mComposeLayoutResId = getResourceId(R.styleable.TitleBar_tb_compose_layout, -1)
            mTitle = getString(R.styleable.TitleBar_tb_title)
            // 默认title文本大小为18sp
            mTitleSize =
                getDimension(
                    R.styleable.TitleBar_tb_title_size,
                    context.getRawSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                )
            mTitleColor = getColor(R.styleable.TitleBar_tb_title_color, Color.BLACK)
            mActualTitleColor = getColor(R.styleable.TitleBar_tb_actual_title_color, Color.BLACK)

            mBgColor =
                getColor(R.styleable.TitleBar_tb_bg_color, loadColor(R.color.backgroundPanel))
            mActualBgColor = getColor(
                R.styleable.TitleBar_tb_actual_bg_color,
                loadColor(R.color.backgroundPanel)
            )
            mMenuColor = getColor(R.styleable.TitleBar_tb_menu_color, loadColor(R.color.white))
            mActualMenuColor =
                getColor(R.styleable.TitleBar_tb_actual_menu_color, loadColor(R.color.black))

            mNavIcon = getResourceId(R.styleable.TitleBar_tb_nav_icon, -1)
            mNavIconTint =
                getColor(R.styleable.TitleBar_tb_nav_icon_tint, Color.parseColor("#333333"))
            mIsCenterTitle = getBoolean(R.styleable.TitleBar_tb_show_center_title, true)
            mIsStartTitle = getBoolean(R.styleable.TitleBar_tb_show_start_title, false)
            mIsBoldTitle = getBoolean(R.styleable.TitleBar_tb_show_bold_title, false)
            mShowDivider = getBoolean(R.styleable.TitleBar_tb_show_divider, true)
            mSupportImmersiveBar = getBoolean(R.styleable.TitleBar_tb_immersive_bar_support, true)
            mEnableTint = getBoolean(R.styleable.TitleBar_tb_enable_tint, true)

            mScrollFlags = getInt(R.styleable.TitleBar_tb_layout_scrollFlags, mScrollFlags)
        }

        if (TextUtils.isEmpty(mTitle)) mTitle = ""
        initLayout()
    }

    private fun initLayout() {
        setBackgroundColor(mBgColor)

        LayoutInflater.from(context)
            .inflate(R.layout.common_layout_title_bar, this)
        if (mToolbarHeight != -1) mToolbar.setHeight(mToolbarHeight)
        mToolbar.contentInsetStartWithNavigation = 0
        if (mScrollFlags != -1) {
            (mToolbar.layoutParams as AppBarLayout.LayoutParams).scrollFlags = mScrollFlags
        }

        // 当mBgColor等于透明颜色的时候分割线需要隐藏
        if (mShowDivider && mBgColor == Color.TRANSPARENT) mDividerPaint.color = Color.TRANSPARENT
        setImmersiveBar(mSupportImmersiveBar)
        setNavigationIcon(mNavIcon)
        setTitleOrCenterLayout()
        setComposeLayout()
    }

    private fun setImmersiveBar(supportImmersiveBar: Boolean) {
        if (supportImmersiveBar) {
            setPadding(paddingLeft, context.getStatusBarHeight(), paddingRight, paddingBottom)
        } else {
            setPadding(paddingLeft, 0, paddingRight, paddingBottom)
        }
    }

    fun setNavigationIcon(@DrawableRes navIcon: Int = -1) {
        if (navIcon != -1) {
            getToolBar().setNavigationIcon(navIcon)
            if (mEnableTint)
                getToolBar().navigationIcon?.setColorFilter(mNavIconTint, PorterDuff.Mode.SRC_IN)
        }
    }

    fun getToolBar(): Toolbar = mToolbar

    private fun setTitleOrCenterLayout() {
        if (mCenterLayoutResId == -1) {
            if (mIsCenterTitle || mIsStartTitle) {
                mCenterLayout.layoutResource = R.layout.common_layout_center_title
                mCenterLayout.inflatedId = R.id.tv_center_title
                val params = mCenterLayout.layoutParams as Toolbar.LayoutParams
                params.gravity = when {
                    mIsCenterTitle -> Gravity.CENTER
                    mIsStartTitle -> Gravity.START
                    else -> Gravity.CENTER
                }
                mTvCenterTitle = mCenterLayout.inflate() as AppCompatTextView
                mTvCenterTitle?.paint?.isFakeBoldText = mIsBoldTitle
                mTvCenterTitle?.text = mTitle
                mTvCenterTitle?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize)
                mTvCenterTitle?.setTextColor(mTitleColor)
                mToolbar.title = ""
            } else {
                getToolBar().title = mTitle
            }
        } else {
            // 加载自定义layout
            if (mCenterLayoutMatch) {
                val lp = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mShowDivider) {
            canvas?.drawLine(
                0f, measuredHeight - mDividerWidth, measuredWidth.toFloat(),
                measuredHeight - mDividerWidth, mDividerPaint
            )
        }
    }

    fun changeAlpha(percent: Float) {
        setBackgroundColor(ColorUtils.adjustAlpha(mActualBgColor, percent))
        changeMenuColorTint(ColorUtils.blendColors(mMenuColor, mActualMenuColor, percent))
        if (mTvCenterTitle != null) {
            mTvCenterTitle!!.setTextColor(ColorUtils.adjustAlpha(mActualTitleColor, percent))
        } else {
            getToolBar().setTitleTextColor(ColorUtils.adjustAlpha(mActualTitleColor, percent))
        }

        mDividerPaint.color = ColorUtils.adjustAlpha(mDividerColor, percent)
        invalidate()
    }

    fun changeMenuColorTint(color: Int = mMenuColor) {
        if (mEnableTint) {
            for (i in 0 until mToolbar.menu.size()) {
                val menuItem = mToolbar.menu.getItem(i)
                menuItem.icon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            }
        }
    }

    fun changeNavigationIconTint(color: Int = mNavIconTint) {
        if (mEnableTint)
            getToolBar().navigationIcon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    fun getToolbarHeight() =
        if (mToolbarHeight != -1) mToolbarHeight else context.getToolbarHeight()

    fun setTitle(title: String) {
        if ((mIsCenterTitle || mIsStartTitle) && mTvCenterTitle != null) {
            mTvCenterTitle!!.text = title
        } else {
            getToolBar().title = title
        }
        mTitle = title
    }

    fun setTitle(@StringRes resId: Int) {
        mTitle = context.getString(resId)
        if ((mIsCenterTitle || mIsStartTitle) && mTvCenterTitle != null) {
            mTvCenterTitle!!.text = mTitle
        } else {
            getToolBar().title = mTitle
        }
    }

    fun setShowDivider(showDivider: Boolean) {
        this.mShowDivider = showDivider
        invalidate()
    }

    fun getMenuColor() = mMenuColor

    fun getActualMenuColor() = mActualMenuColor
}