package com.ytech.ui.widget

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.ytech.common.common.buildBundle
import com.ytech.common.view.visible
import com.ytech.ui.R

import kotlinx.android.synthetic.main.common_layout_multiple_status.view.*

/**
 * 关于错误布局
 * icon title subTitle actionText默认为null
 * 如果为空则隐藏对应控件
 */
class MultipleStatusLayout : ConstraintLayout {
    companion object {
        const val TAG = "MultipleStatusLayout"
        const val CURRENT_LAYOUT_ID = "current_layout_id"

        const val LAYOUT_CONTENT_ID = 1
        const val LAYOUT_ERROR_ID = 2
        const val LAYOUT_LOADING_ID = 3
    }

    override fun onSaveInstanceState(): Parcelable? {
        return buildBundle(
            TAG to super.onSaveInstanceState(),
            CURRENT_LAYOUT_ID to mCurrentLayoutId
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            showHideViewById(state.getInt(CURRENT_LAYOUT_ID, mCurrentLayoutId))
            super.onRestoreInstanceState(state.getParcelable(TAG))
            return
        }
        super.onRestoreInstanceState(state)
    }

    // 图标
    private var mIcon: Int = -1
    // 标题文本
    private var mTitle: CharSequence? = null
    // 副标题文本
    private var mSubTitle: CharSequence? = null
    // 按钮文本
    private var mActionText: CharSequence? = null
    private var mContentLayoutResId: Int = -1

    private var mContentView: View? = null

    private var mOnRetry: ((actionText: String) -> Unit)? = null

    private var mCurrentLayoutId: Int = LAYOUT_LOADING_ID

    private var mAnimLading: AnimationDrawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.MultipleStatusLayout)
        mIcon = ta.getResourceId(R.styleable.MultipleStatusLayout_msl_icon, -1)
        mTitle = ta.getString(R.styleable.MultipleStatusLayout_msl_title)
        mSubTitle = ta.getString(R.styleable.MultipleStatusLayout_msl_sub_title)
        mActionText = ta.getString(R.styleable.MultipleStatusLayout_msl_action_text)
        mContentLayoutResId =
            ta.getResourceId(R.styleable.MultipleStatusLayout_msl_content_layout, -1)
        // 当前显示那个界面
        val currentLayoutId =
            ta.getInt(R.styleable.MultipleStatusLayout_msl_status, mCurrentLayoutId)
        ta.recycle()

        initLayout(currentLayoutId)
    }

    private fun initLayout(layoutId: Int) {
        LayoutInflater.from(context)
            .inflate(R.layout.common_layout_multiple_status, this)

        // 设置图标和文本
        iconTitleViewSettings(mIcon, mTitle, mSubTitle, mActionText)
        if (mContentLayoutResId != -1) {
            mContentLayout.layoutResource = mContentLayoutResId
            mContentView = mContentLayout.inflate()
        }
        mBtnAction.setOnClickListener { mOnRetry?.invoke(mBtnAction.text.toString()) }
        if (mIvLoading.drawable is AnimationDrawable) {
            mAnimLading = mIvLoading.drawable as AnimationDrawable
        }

        showHideViewById(layoutId)
    }

    fun setContentLayout(@LayoutRes layoutResId: Int) {
        mContentLayoutResId = layoutResId
        mContentLayout.layoutResource = mContentLayoutResId
        mContentView = mContentLayout.inflate()
    }

    fun getContentLayout(): View? = mContentView

    /**
     * 如果传入的值等于默认值则隐藏对应控件
     */
    fun showErrorLayout(
        @DrawableRes iconResId: Int = -1,
        title: CharSequence? = null,
        subTitle: CharSequence? = null,
        actionText: CharSequence? = null
    ) {
        showHideViewById(LAYOUT_ERROR_ID)
        /*val resId = if (iconResId == -1) mIcon else iconResId
        val tempTitle = if (TextUtils.isEmpty(title)) mTitle else title
        val tempSubTitle = if (TextUtils.isEmpty(subTitle)) mSubTitle else subTitle
        val tempActionText = if (TextUtils.isEmpty(actionText)) mActionText else actionText
        iconTitleViewSettings(resId, tempTitle, tempSubTitle, tempActionText)*/
        iconTitleViewSettings(iconResId, title, subTitle, actionText)
    }

    /**
     * 显示内容布局
     */
    fun showContentLayout() {
        showHideViewById(LAYOUT_CONTENT_ID)
    }

    /**
     * 显示加载中布局
     */
    fun showLoadingLayout() {
        showHideViewById(LAYOUT_LOADING_ID)
    }

    /**
     * 是否正在加载中
     */
    fun isLoading() = mIvLoading.visible

    private fun iconTitleViewSettings(
        @DrawableRes iconResId: Int = -1,
        title: CharSequence? = null,
        subTitle: CharSequence? = null,
        actionText: CharSequence? = null
    ) {
        if (iconResId != -1) {
            mIvIcon.visibility = View.VISIBLE
            mIvIcon.setImageResource(iconResId)
            mIcon = iconResId
        } else {
            mIvIcon.visibility = View.GONE
            mIcon = -1
        }

        if (!TextUtils.isEmpty(title)) {
            mTvTitle.visibility = View.VISIBLE
            mTvTitle.text = title
            mTitle = title
        } else {
            mTvTitle.visibility = View.GONE
            mTitle = null
        }

        if (!TextUtils.isEmpty(subTitle)) {
            mTvSubTitle.visibility = View.VISIBLE
            mTvSubTitle.text = subTitle
            mSubTitle = subTitle
        } else {
            mTvSubTitle.visibility = View.GONE
            mSubTitle = null
        }

        if (!TextUtils.isEmpty(actionText)) {
            mBtnAction.visibility = View.VISIBLE
            mBtnAction.text = actionText
            mActionText = actionText
        } else {
            mBtnAction.visibility = View.GONE
            mActionText = null
        }
    }

    /**
     * 显示传进来的layoutId 隐藏其他layoutId
     */
    private fun showHideViewById(layoutId: Int) {
        mCurrentLayoutId = layoutId
        mContentView?.visibility = if (layoutId == LAYOUT_CONTENT_ID) View.VISIBLE else View.GONE
        // mErrorGroup.visibility = if (layoutId == LAYOUT_ERROR_ID) View.VISIBLE else View.GONE

        if (layoutId == LAYOUT_ERROR_ID) {
            mIvIcon.visibility = View.VISIBLE
            mTvTitle.visibility = View.VISIBLE
            mTvSubTitle.visibility = View.VISIBLE
            mBtnAction.visibility = View.VISIBLE
        } else {
            mIvIcon.visibility = View.GONE
            mTvTitle.visibility = View.GONE
            mTvSubTitle.visibility = View.GONE
            mBtnAction.visibility = View.GONE
        }

        mIvLoading.visibility = if (layoutId == LAYOUT_LOADING_ID) View.VISIBLE else View.GONE
        if (layoutId == LAYOUT_LOADING_ID) {
            mAnimLading?.start()
        } else {
            mAnimLading?.stop()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAnimLading?.stop()
    }

    fun setOnRetry(l: (actionText: String) -> Unit) {
        this.mOnRetry = l
    }
}