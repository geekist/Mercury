package com.ytech.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.ytech.common.common.loadColor
import com.ytech.ui.R


class PressedConstraintLayout : ConstraintLayout {
    /**
     * 左上圆角大小
     */
    private var mCornerRadiusLT = 0f
    /**
     * 左下圆角大小
     */
    private var mCornerRadiusLB = 0f
    /**
     * 右上圆角大小
     */
    private var mCornerRadiusRT = 0f
    /**
     * 右下圆角大小
     */
    private var mCornerRadiusRB = 0f

    /**
     * 背景颜色
     */
    private var mBgColor = 0
    /**
     * 按下的背景颜色
     */
    private var mPressBgColor = 0
    private var mEnablePress = true

    private val mPaint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paint
    }
    private val mPath by lazy { Path() }
    private val mContentRect by lazy { RectF() }

    private val mRadii by lazy {
        floatArrayOf(
            mCornerRadiusLT, mCornerRadiusLT, mCornerRadiusRT, mCornerRadiusRT,
            mCornerRadiusRB, mCornerRadiusRB, mCornerRadiusLB, mCornerRadiusLB
        )
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        with(context.obtainStyledAttributes(attributeSet, R.styleable.PressedConstraintLayout)) {
            val cornerRadius =
                getDimensionPixelSize(R.styleable.PressedConstraintLayout_pcl_corner_radius, -1)
            if (cornerRadius >= 0) {
                // 如果已经设置过cornerRadius 左上右下的都使用这个值
                mCornerRadiusLT = cornerRadius.toFloat()
                mCornerRadiusLB = cornerRadius.toFloat()
                mCornerRadiusRT = cornerRadius.toFloat()
                mCornerRadiusRB = cornerRadius.toFloat()
            } else {
                mCornerRadiusLT = getDimensionPixelSize(
                    R.styleable.PressedConstraintLayout_pcl_corner_radius_lt, 0
                ).toFloat()
                mCornerRadiusLB = getDimensionPixelSize(
                    R.styleable.PressedConstraintLayout_pcl_corner_radius_lb, 0
                ).toFloat()
                mCornerRadiusRT = getDimensionPixelSize(
                    R.styleable.PressedConstraintLayout_pcl_corner_radius_rt, 0
                ).toFloat()
                mCornerRadiusRB = getDimensionPixelSize(
                    R.styleable.PressedConstraintLayout_pcl_corner_radius_rb, 0
                ).toFloat()
            }

            mBgColor = getColor(
                R.styleable.PressedConstraintLayout_pcl_bg_color, loadColor(R.color.black)
            )
            mPressBgColor = getColor(
                R.styleable.PressedConstraintLayout_pcl_press_bg_color,
                loadColor(R.color.background_f8)
            )
            mEnablePress = getBoolean(R.styleable.PressedConstraintLayout_pcl_enable_press, true)
            recycle()
        }
        setWillNotDraw(false)
        this.isClickable = mEnablePress
        mPaint.color = mBgColor
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            mPath.reset()
            val w = measuredWidth
            val h = measuredHeight
            mContentRect.set(
                0f, 0f,
                w.toFloat(), h.toFloat()
            )
            mPath.addRoundRect(
                mContentRect, mRadii, Path.Direction.CW
            )
            it.drawPath(mPath, mPaint)
            it.clipPath(mPath)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!mEnablePress) return super.onTouchEvent(event)
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                mPaint.color = mPressBgColor
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mPaint.color = mBgColor
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }
}