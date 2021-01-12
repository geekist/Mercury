package com.ytech.ui.adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.lang.Exception

abstract class BaseSelectAdapter<T : Any, K : BaseViewHolder>(@LayoutRes layoutResId: Int, data: MutableList<T>?) :
    BaseQuickAdapter<T, K>(layoutResId, data) {

    // 默认是单选模式
    private var mCurrentSelectMode = ISelect.SINGLE_MODE
    // 默认开启选择模式
    private var mSelectModeEnable: Boolean = true
    // 默认长按能触发选择
    private var mLongTouchEnable: Boolean = true
    // 多选模式下最多可以选择多少张(小于0表示没有限制 等于0表示不能再选了)
    private var mMaxSelectable: Int = -1

    private var mClickListenerFlag: Boolean = false

    // 记录选中状态
    // private val mSelectedStates: SparseBooleanArray = SparseBooleanArray()
    private val mSelectedStates = linkedMapOf<Int, Boolean>()

    private val mSelectedState = linkedMapOf<String, Boolean>()

    private var mBeforeItemSelected: ((position: Int, isSelected: Boolean, positions: IntArray) -> Unit)? = null
    private var mOnItemSelected: ((position: Int, isSelected: Boolean) -> Unit)? = null
    private var mOnLimit: ((currentSelectedSize: Int, maxSelectedSize: Int) -> Unit)? = null
    private var mOnNothingSelected: (() -> Unit)? = null

    fun beforeItemSelected(l: (position: Int, isSelected: Boolean, positions: IntArray) -> Unit): BaseSelectAdapter<T, K> {
        mBeforeItemSelected = l
        return this
    }

    fun onItemSelected(l: (position: Int, isSelected: Boolean) -> Unit): BaseSelectAdapter<T, K> {
        mOnItemSelected = l
        return this
    }

    fun onLimit(l: (currentSelectedSize: Int, maxSelectedSize: Int) -> Unit): BaseSelectAdapter<T, K> {
        mOnLimit = l
        return this
    }

    fun onNothingSelected(l: () -> Unit): BaseSelectAdapter<T, K> {
        mOnNothingSelected = l
        return this
    }

    private var mInnerOnItemClickListener: ((view: View, position: Int) -> Unit)? = null
    private var mInnerOnItemLongClickListener: ((view: View, position: Int) -> Boolean)? = null

    init {
        onItemClickListener = OnItemClickListener { _, view, position ->
            mInnerOnItemClickListener?.invoke(view, position)
        }
        onItemLongClickListener = OnItemLongClickListener { _, view, position ->
            if (mInnerOnItemLongClickListener != null) {
                return@OnItemLongClickListener mInnerOnItemLongClickListener!!.invoke(view, position)
            }
            return@OnItemLongClickListener true
        }
        mClickListenerFlag = true
    }

    override fun setOnItemClickListener(listener: OnItemClickListener?) {
        if (mClickListenerFlag) throw Exception("使用此Adapter不能自己调用setOnItemClickListener方法")
        super.setOnItemClickListener(listener)
    }

    override fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
        if (mClickListenerFlag) throw Exception("使用此Adapter不能自己调用setOnItemLongClickListener方法")
        super.setOnItemLongClickListener(listener)
    }

    override fun setOnItemClick(view: View?, position: Int) {
        if (mSelectModeEnable) {
            // 如果处于选择模式则自己处理逻辑
            handle(position)
        } else {
            super.setOnItemClick(view, position)
        }
    }

    override fun setOnItemLongClick(view: View?, position: Int): Boolean {
        return if (mSelectModeEnable && mLongTouchEnable) {
            // 如果处于选择模式并且可以长按触发 则自己处理逻辑
            handle(position)
            true
        } else {
            super.setOnItemLongClick(view, position)
        }
    }

    private fun handle(position: Int) {
        val selected = mSelectedStates[position] ?: false
        when (mCurrentSelectMode) {
            ISelect.SINGLE_MODE, ISelect.SINGLE_MODE_DISABLE -> {
                // 是单选
                if (selected) {
                    if (mCurrentSelectMode == ISelect.SINGLE_MODE_DISABLE) {
                        // 已经选择了不能反选 直接return
                        return
                    } else {
                        // 当前已经选中则反选
                        mSelectedStates.remove(position)
                    }

                    // this.mOnItemSelected?.invoke(position, false)
                } else {
                    if (mSelectedStates.size == 1) {
                        // 当前没有选中 但是有其他项被选中 则取消选择 选中当前项
                        val prePosition = mSelectedStates.keys.iterator().next()

                        mSelectedStates.remove(prePosition)
                        notifyItemChanged(prePosition + headerLayoutCount)

                        // this.mOnItemSelected?.invoke(position, false)
                    }

                    mSelectedStates[position] = true
                }
            }
            ISelect.MULTIPLE_MODE -> {
                if (mMaxSelectable >= 0 && mSelectedStates.size >= mMaxSelectable && !selected) {
                    // 当前没有选中 将要点击选中的时候 如果超过最大限制 则return
                    mOnLimit?.invoke(mSelectedStates.size, mMaxSelectable)
                    return
                }
                if (selected) {
                    mSelectedStates.remove(position)
                } else {
                    mSelectedStates[position] = true
                }
            }
        }
        mBeforeItemSelected?.invoke(position, !selected, mSelectedStates.keys.toIntArray())
        notifyItemChanged(position + headerLayoutCount)
        mSelectedStates.keys.iterator().forEach {
            notifyItemChanged(it + headerLayoutCount)
        }

        if (mSelectedStates.size == 0) {
            mOnNothingSelected?.invoke()
        }
        mOnItemSelected?.invoke(position, !selected)
    }

    protected fun isSelected(position: Int): Boolean = mSelectedStates[position - headerLayoutCount] ?: false

    /**
     * 位于position的item的点击顺序
     */
    protected fun getSelectionPosition(position: Int): Int {
        var selectionPosition = 0
        var flag = 0
        for ((index, pos) in mSelectedStates.keys.iterator().withIndex()) {
            flag++
            if (position == pos) {
                selectionPosition = index
                break
            }
        }
        return selectionPosition
    }

    fun getSelectedPositionWhenSingleMode() = mSelectedStates.keys.single()

    /**
     * 获取选中的items
     */
    fun getSelectedItems(): MutableList<T> {
        val selectedItems = mutableListOf<T>()
                                                      mSelectedStates.keys.iterator().forEach {
            getItem(it)?.let { item ->
                selectedItems.add(item)
            }
        }
        return selectedItems
    }

    fun getSelectedPositions() = mSelectedStates.keys.toIntArray()

    open fun clearRvAnim(rv: RecyclerView?): BaseSelectAdapter<T, K> {
        rv?.let {
            val animator = rv.itemAnimator
            if (animator is DefaultItemAnimator)
                animator.supportsChangeAnimations = false
            rv.itemAnimator?.changeDuration = 333
            rv.itemAnimator?.moveDuration = 333
        }
        return this
    }

    open fun isSelectModeEnable(): Boolean = mSelectModeEnable

    open fun setSelectedEnable(enable: Boolean): BaseSelectAdapter<T, K> {
        if (mSelectModeEnable != enable) {
            mSelectModeEnable = enable
            if (!enable && mSelectedStates.size > 0) {
                // 如果之前是选择模式 更改为非选择模式 需要取消选择
                mSelectedStates.clear()
            }
            notifyDataSetChanged()
        }
        return this
    }

    open fun setSelectMode(@SelectMode mode: Int): BaseSelectAdapter<T, K> {
        this.mCurrentSelectMode = mode
        return setSelectedEnable(true)
    }

    open fun setLongTouchEnable(enable: Boolean): BaseSelectAdapter<T, K> {
        this.mLongTouchEnable = enable
        return this
    }

    open fun setInnerOnItemClickListener(l: (view: View, position: Int) -> Unit): BaseSelectAdapter<T, K> {
        this.mInnerOnItemClickListener = l
        return this
    }

    open fun setInnerOnItemLongClickListener(l: (view: View, position: Int) -> Boolean): BaseSelectAdapter<T, K> {
        this.mInnerOnItemLongClickListener = l
        return this
    }

    fun setMaxSelectSize(maxSelectSize: Int): BaseSelectAdapter<T, K> {
        mMaxSelectable = if (maxSelectSize < 0) -1 else maxSelectSize
        return this
    }

    fun getMaxSelectSize() = mMaxSelectable

    /**
     * 获取已经选中item的size
     */
    fun getSelectedSize() = mSelectedStates.size

    /**
     * 选中positions选项
     */
    fun select(vararg positions: Int): BaseSelectAdapter<T, K> {

        positions.forEach { position ->
            mSelectedStates[position] = true
            notifyItemChanged(position + headerLayoutCount)
        }
        return this
    }

    fun selectAll(): BaseSelectAdapter<T, K> {
        for (index in data.indices) {
            val states = mSelectedStates[index]
            if (states == null) {
                mSelectedStates[index] = true
                notifyItemChanged(index + headerLayoutCount)
            } else {
                if (!states) {
                    mSelectedStates[index] = true
                    notifyItemChanged(index + headerLayoutCount)
                }
            }
        }
        return this
    }

    fun clearSelected(position: Int): BaseSelectAdapter<T, K> {
        mSelectedStates.remove(position)
        notifyItemChanged(position + headerLayoutCount)
        return this
    }

    /**
     * 清除所有选项
     * @param notifyItemChanged 是否刷新界面
     */
    fun clearSelected(notifyItemChanged: Boolean = false): BaseSelectAdapter<T, K> {
        if (notifyItemChanged) {
            mSelectedStates.forEach {
                val key = it.key
                mSelectedStates[key] = false
                notifyItemChanged(key + headerLayoutCount)
            }
        }
        mSelectedStates.clear()
        return this
    }
}