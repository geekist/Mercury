package com.ytech.home.homelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.ytech.model.DatasBean
import com.ytech.home.R
import com.ytech.home.databinding.AbcListLayoutBinding
import com.ytech.home.homelist.adapter.HomeListAdapter
import com.ytech.ui.base.SupportFragment

class HomeListFragment : SupportFragment(), OnRefreshListener, OnLoadMoreListener {

    private lateinit var mBinding: AbcListLayoutBinding
    lateinit var mViewModel: HomeListViewModel

    lateinit var mAdapter: PagedListAdapter<com.ytech.model.DatasBean, RecyclerView.ViewHolder>
    private lateinit var mRefreshLayout: SmartRefreshLayout
    private lateinit var mRecycleView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.abc_list_layout,
            container,
            false
        )

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        initRecyclerView()
        initRefreshLayout()
        hiddenActionBar()
    }

    private fun initData() {
        mAdapter =
            HomeListAdapter(context!!) as PagedListAdapter<com.ytech.model.DatasBean, RecyclerView.ViewHolder>
        mRecycleView.adapter = mAdapter

        mViewModel = ViewModelProvider(this).get(HomeListViewModel::class.java)
        mViewModel.getPageData().observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
    }

    private fun initRecyclerView() {
        mRecycleView = mBinding.recycleView
        mRecycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        decoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.home_list_divier
            )!!
        )
        mRecycleView.addItemDecoration(decoration)
    }

    private fun initRefreshLayout() {
        mRefreshLayout = mBinding.refreshLayout
        mRefreshLayout.setEnableRefresh(true)
        mRefreshLayout.setEnableLoadMore(true)
        mRefreshLayout.setOnRefreshListener(this)
        mRefreshLayout.setOnLoadMoreListener(this)
    }

    private fun hiddenActionBar() {
        mBinding.actionBar.visibility = View.GONE
    }

    /************************** SmartRefreshLayout interface ***********************************/
    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.getDataSource()!!.invalidate()
        finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        //当加载更多的时候返回了空数据 需要手动接管数据加载否则paging框架将不会继续加载数据
        val currentList = mAdapter.currentList
        if (currentList == null || currentList.size <= 0) {
            return
        }

        val key = currentList.size / 20
        mViewModel.getHomeList(
            key,
            key + 1,
            object : PageKeyedDataSource.LoadCallback<Int, com.ytech.model.DatasBean>() {
                override fun onResult(data: MutableList<com.ytech.model.DatasBean>, adjacentPageKey: Int?) {

                    //把data转成pageList
                    val dataSource = MutablePageKeyedDataSource<com.ytech.model.DatasBean>()

                    dataSource.data.addAll(currentList)
                    dataSource.data.addAll(data)

                    val pageList =
                        dataSource.buildNewPageList(mAdapter.currentList!!.config)

                    mAdapter.submitList(pageList)
                }
            })

        finishRefresh()
    }

    private fun finishRefresh() {
        val state = mRefreshLayout.state
        if (state.isOpening && state.isHeader) {
            mRefreshLayout.finishRefresh()
        } else if (state.isOpening && state.isFooter) {
            mRefreshLayout.finishLoadMore()
        }
    }
}
