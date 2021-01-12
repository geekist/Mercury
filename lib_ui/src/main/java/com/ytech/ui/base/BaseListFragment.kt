package com.ytech.ui.base

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.ytech.common.common.then
import com.ytech.common.network.isNetworkConnected
import com.ytech.ui.R
import com.ytech.ui.adapter.BaseSelectAdapter

/**
 * RecyclerView + RefreshLayout + BaseQuickAdapter
 */
abstract class BaseListFragment<ITEM : Any,  ADAPTER : BaseQuickAdapter<ITEM, BaseViewHolder>,
        LM : RecyclerView.LayoutManager> : BaseLoadingFragment() {
    private var mRvList: RecyclerView? = null
    private var mRefreshLayout: SmartRefreshLayout? = null

    private var mAdapter: ADAPTER? = null
    private var mLayoutManager: LM? = null

    private var mItemDecoration: RecyclerView.ItemDecoration? = null

    abstract fun createLayoutManager(): LM

    abstract fun createAdapter(): ADAPTER

    override fun initView(savedInstanceState: Bundle?, rootView: View) {
        super.initView(savedInstanceState, rootView)

        initRefreshLayout()
        initLayoutManager()
        initAdapter()
        setUpRecyclerView()
    }

    override fun setUpStatusLayout() {
        val contentLayout = getStatusLayout().getContentLayout()
            ?: throw NullPointerException("${this::class.java.simpleName} 内容布局为空")
        mRvList = contentLayout.findViewById(R.id.mRvList)
        mRefreshLayout = contentLayout.findViewById(R.id.mRefreshLayout)
        getStatusLayout().setOnRetry { actionText -> onRetry(actionText) }
    }

    override fun showLoading() {
        (getAdapter().data.isEmpty() && getAdapter().headerLayoutCount <= 0).then {
            getStatusLayout().showLoadingLayout()
        }
    }

    override fun hideLoading(runnable: Runnable?) {
        (getAdapter().data.isNotEmpty() || getAdapter().headerLayoutCount > 0).then {
            getStatusLayout().showContentLayout()
        }
        getRefreshLayout().finishRefresh()
        getRefreshLayout().finishLoadMore()
    }

    override fun showMessage(message: String) {
        super.showMessage(message)
        getRefreshLayout().finishRefresh()
        getRefreshLayout().finishLoadMore()
        if (context!!.isNetworkConnected) {
            // 有网络检查列表有无数据 若无数据显示空视图
            checkList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getAdapter().unregisterAdapterDataObserver(mDataObserver)
        getRecyclerView().removeOnScrollListener(mOnScrollListener)
    }

    // 能否触发滚动列表到顶部并刷新
    protected open fun canScrollToTopRefresh() = getRefreshLayout().state == RefreshState.None

    protected open fun isRefreshing() = getRefreshLayout().state == RefreshState.Refreshing

    protected open fun isLoading() = getRefreshLayout().state == RefreshState.Loading

    protected open fun invalidateItemDecoration() {
        val itemDecoration = mItemDecoration
        if (itemDecoration == null) {
            mItemDecoration = getItemDecoration()
            mItemDecoration?.let { getRecyclerView().addItemDecoration(it) }
        } else {
            getRecyclerView().removeItemDecoration(itemDecoration)
            mItemDecoration = getItemDecoration()
            mItemDecoration?.let { getRecyclerView().addItemDecoration(it) }
        }
    }

    protected open fun getItemDecoration(): RecyclerView.ItemDecoration? = null

    protected open fun invalidateLayoutManager() {
        initLayoutManager()
        getRecyclerView().layoutManager = mLayoutManager
    }
    private fun initRefreshLayout() {
        getRefreshLayout().setEnableRefresh(isEnableRefresh())
        getRefreshLayout().setEnableLoadMore(isEnableLoadMore())
        getRefreshLayout().setEnableOverScrollDrag(true)
        // getRefreshLayout().setEnableAutoLoadMore(false)
        // getRefreshLayout().setDisableContentWhenRefresh(true)
        // getRefreshLayout().setDisableContentWhenLoading(true)

        getRefreshLayout().setOnRefreshListener { onRefresh(it) }
        getRefreshLayout().setOnLoadMoreListener { onLoadMore(it) }
        getRefreshLayout().setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onFooterFinish(footer: RefreshFooter?, success: Boolean) {
                getRecyclerView().invalidateItemDecorations()
            }
        })
    }

    private fun initLayoutManager() {
        mLayoutManager = createLayoutManager()
    }

    private fun initAdapter() {
        mAdapter = createAdapter()
        if (getAdapter() !is BaseSelectAdapter<*, *>) {
            getAdapter().setOnItemClickListener { adapter, view, position ->
                onItemClick(adapter, view, position)
            }
        }
        getAdapter().setOnItemChildClickListener { adapter, view, position ->
            onItemChildClick(adapter, view, position)
        }
        getAdapter().registerAdapterDataObserver(mDataObserver)
    }

    private fun setUpRecyclerView() {
        getRecyclerView().layoutManager = getLayoutManager()
        (getRecyclerView().adapter == null).then { getRecyclerView().adapter = getAdapter() }
        invalidateItemDecoration()
        getRecyclerView().addOnScrollListener(mOnScrollListener)
    }

    private val mDataObserver by lazy {
        object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                checkList()
            }
        }
    }

    private val mOnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is StaggeredGridLayoutManager) {
                    layoutManager.invalidateSpanAssignments()
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getRecyclerView().invalidateItemDecorations()
                }
            }
        }
    }

    protected open fun checkList() {
        (getAdapter().data.isEmpty() && getAdapter().headerLayoutCount <= 0).then({
            getStatusLayout().showErrorLayout(iconResId = R.drawable.ic_empty_0)
        }, {
            getStatusLayout().showContentLayout()
        })
    }

    /**
     * 是否支持下拉刷新(默认支持)
     */
    open fun isEnableRefresh() = true

    /**
     * 是否支持上拉加载(默认支持)
     */
    open fun isEnableLoadMore() = true

    /**
     * 下拉刷新的回调
     */
    open fun onRefresh(refreshLayout: RefreshLayout) {}

    /**
     * 上拉加载的回调
     */
    open fun onLoadMore(refreshLayout: RefreshLayout) {}

    /**
     * RecyclerView的Item点击事件
     */
    open fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}

    /**
     * RecyclerView的子View点击事件
     */
    open fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}

    /*********************************************************************/


//    override fun refresh(needScrollToTop: Boolean) {
//        if (canScrollToTopRefresh()) {
//            // 如果正在上拉加载 或者正在下拉刷新
//            getRecyclerView().scrollToPosition(0)
//            getRefreshLayout().autoRefresh()
//        }
//    }


    protected fun getRefreshLayout(): SmartRefreshLayout = mRefreshLayout!!

    protected fun getAdapter(): ADAPTER = mAdapter!!

    protected fun getLayoutManager(): LM = mLayoutManager!!

    protected fun getRecyclerView(): RecyclerView = mRvList!!
}