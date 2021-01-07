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
import com.ytech.apply.applydetail.adapter.ProjectPageAdapter
import com.ytech.common.device.dp2px
import com.ytech.model.apply.ProjectItemSub
import com.ytech.ui.base.SupportFragment
import com.ytech.ui.databinding.AbcListLayoutBinding
import com.ytech.ui.view.RecyclerViewDivider

class ApplyItemFragment : SupportFragment(), OnRefreshListener,OnLoadMoreListener {

    companion object {
        fun newInstance(id: Int): ApplyItemFragment {
            val args = Bundle()
            args.putInt("id", id)
            val fragment = ApplyItemFragment()
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var viewModel: ApplyItemViewModel
    lateinit var viewBinding: AbcListLayoutBinding

    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var recycleView: RecyclerView
    lateinit var adapter: PagedListAdapter<ProjectItemSub, RecyclerView.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.abc_list_layout,
            container,
            false
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        val arguments = requireArguments()
        val id = arguments.getInt("id")
        viewModel.setId(id)
    }

    private fun initView() {
        hideActionBar()
        initRefreshLayout()
        initRecyclerView()
    }

    private fun initRefreshLayout() {
        refreshLayout = viewBinding.refreshLayout
        refreshLayout.setEnableRefresh(true)
        refreshLayout.setEnableLoadMore(true)
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setOnLoadMoreListener(this)
    }

    private fun initRecyclerView() {
        recycleView = viewBinding.recycleView
        recycleView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = ProjectPageAdapter(context!!) as PagedListAdapter<ProjectItemSub, RecyclerView.ViewHolder>
        recycleView.adapter = adapter

        val divider = RecyclerViewDivider(requireContext(),
            LinearLayoutManager.HORIZONTAL,
            dp2px(requireContext(), 1.0F),
            requireContext().resources.getColor(R.color.divider))
        recycleView.addItemDecoration(divider)

    }

    private fun initData() {
        viewModel = ViewModelProvider(this).get(ApplyItemViewModel::class.java)
        viewModel.getPageData().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    /*******************  SmartRefreshLayout  Listener ***************************/
    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel.getDataSource()!!.invalidate()
        finishRefresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        /*
        val currentList = adapter.currentList
        if (currentList == null || currentList.size <= 0) {
            return
        }

        val config = currentList!!.config
        val count = currentList.size / 15
        viewModel.getTabPageData(count,
            object : PageKeyedDataSource.LoadCallback<Int, ProjectItemSub>() {
                override fun onResult(data: MutableList<ProjectItemSub>, adjacentPageKey: Int?) {
                    val dataSource = MutablePageKeyedDataSource<ProjectItemSub>()
                    dataSource.data.addAll(currentList)
                    dataSource.data.addAll(data)
                    val pageList = dataSource.buildNewPageList(config)
                    adapter.submitList(pageList)
                }

            })*/

        finishRefresh()

    }

    private fun finishRefresh() {
        val state = refreshLayout.state
        if (state.isOpening && state.isHeader) {
            refreshLayout.finishRefresh()
        } else if (state.isOpening && state.isFooter) {
            refreshLayout.finishLoadMore()
        }
    }

    private fun hideActionBar() {
        viewBinding.actionBar.visibility = View.GONE
    }
}