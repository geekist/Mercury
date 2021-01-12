package com.ytech.knowledge.knowledgedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.ytech.core.arouter.ARouterConstant
import com.ytech.knowledge.R
import com.ytech.knowledge.databinding.FragmentKnowledgeDetailBinding
import com.ytech.knowledge.knowledgedetail.adapter.TreeDetailListAdapter
import com.ytech.model.DatasBean
import com.ytech.ui.base.SupportFragment

@Route(path = ARouterConstant.ModuleKnowledge.FRAGMENT_KNOWLEDGE_DETAIL)
class KnowledgeDetailFragment : SupportFragment(), OnLoadMoreListener, OnRefreshListener {

    @Autowired
    @JvmField
    var cid: Int = 0

    @Autowired
    lateinit var title: String

    lateinit var knowledgeDetailBinding: FragmentKnowledgeDetailBinding
    lateinit var treeDetailViewModel: TreeDetailViewModel

    private lateinit var mRefreshLayout: SmartRefreshLayout
    private lateinit var mRecycleView: RecyclerView
    lateinit var mAdapter: PagedListAdapter<DatasBean, RecyclerView.ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        knowledgeDetailBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_knowledge_detail,
            container,
            false
        )

        return knowledgeDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        initActionBar()
        initRefreshLayout()
        initRecyclerView()
    }

    private fun initData() {
        mAdapter =
            TreeDetailListAdapter(context!!)  as PagedListAdapter<DatasBean, RecyclerView.ViewHolder>
        mRecycleView.adapter = mAdapter

        treeDetailViewModel = ViewModelProvider(this).get(TreeDetailViewModel::class.java)
        treeDetailViewModel.getPageData().observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })

        treeDetailViewModel.setCid(cid)
    }

    private fun initRefreshLayout() {
        mRefreshLayout = knowledgeDetailBinding.refreshLayout
        mRefreshLayout.setEnableRefresh(true)
        mRefreshLayout.setEnableLoadMore(true)
        mRefreshLayout.setOnRefreshListener(this)
        mRefreshLayout.setOnLoadMoreListener(this)
    }

    private fun initRecyclerView() {
        mRecycleView = knowledgeDetailBinding.recycleView
        mRecycleView.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

     //   decoration.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.home_list_divider)!!)
        mRecycleView.addItemDecoration(decoration)
    }

    private fun initActionBar() {
        knowledgeDetailBinding.mTvTitle.text = title
        knowledgeDetailBinding.mIvBack.setOnClickListener {
            pop()
        }
    }

    /****************************  Smart refresh layout interface ***************************************/
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        finishRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        treeDetailViewModel.getDataSource()?.invalidate()
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