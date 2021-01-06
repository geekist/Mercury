package com.ytech.apply.applydetail

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
import com.ytech.apply.R
import com.ytech.apply.apply.MutablePageKeyedDataSource
import com.ytech.apply.applydetail.adapter.PeojectPageAdapter
import com.ytech.apply.databinding.AbsListLayoutBinding
import com.ytech.model.apply.ProjectItemSub
import com.ytech.ui.base.SupportFragment

class ApplyItemFragment : SupportFragment(), OnRefreshListener,
    OnLoadMoreListener {

    companion object {
        fun newInstance(id: Int): ApplyItemFragment {
            val args = Bundle()
            args.putInt("id", id)
            val fragment = ApplyItemFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mBinding: AbsListLayoutBinding
    lateinit var mViewModel: ApplyItemViewModel

    lateinit var mAdapter: PagedListAdapter<com.ytech.model.apply.ProjectItemSub, RecyclerView.ViewHolder>
    private lateinit var mRefreshLayout: SmartRefreshLayout
    private lateinit var mRecycleView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.abs_list_layout,
            container,
            false
        )

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        val arguments = requireArguments()
        val id = arguments.getInt("id")
        mViewModel.setId(id)
    }

    private fun initView() {
        hiddenActionBar()

        initRecyclerView()
        initRefreshLayout()
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

    private fun initData() {
        mAdapter =
            PeojectPageAdapter(context!!) as PagedListAdapter<com.ytech.model.apply.ProjectItemSub, RecyclerView.ViewHolder>
        mRecycleView.adapter = mAdapter

        mViewModel = ViewModelProvider(this).get(ApplyItemViewModel::class.java)
        mViewModel.getPageData().observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
    }

    private fun hiddenActionBar() {
        mBinding.actionBar.visibility = View.GONE
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.getDataSource()!!.invalidate()
        finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

        val currentList = mAdapter.currentList
        if (currentList == null || currentList.size <= 0) {
            return
        }

        val config = currentList!!.config

        val count = currentList.size / 15

        mViewModel.getTabPageData(count,
            object : PageKeyedDataSource.LoadCallback<Int, com.ytech.model.apply.ProjectItemSub>() {
                override fun onResult(data: MutableList<com.ytech.model.apply.ProjectItemSub>, adjacentPageKey: Int?) {

                    val dataSource =
                        MutablePageKeyedDataSource<ProjectItemSub>()

                    dataSource.data.addAll(currentList)
                    dataSource.data.addAll(data)

                    val pageList = dataSource.buildNewPageList(config)

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