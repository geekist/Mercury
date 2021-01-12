package com.ytech.knowledge.knowledge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.ytech.common.device.dp2px
import com.ytech.core.arouter.ARouterConstant
import com.ytech.core.arouter.ARouterUtils
import com.ytech.knowledge.KnowledgeMainFragment
import com.ytech.knowledge.R
import com.ytech.knowledge.databinding.FragmentKnowledgeBinding
import com.ytech.knowledge.knowledge.adapter.KnowledgeAdapter
import com.ytech.ui.base.SupportFragment
import com.ytech.ui.view.RecyclerViewDivider

@Route(path = ARouterConstant.ModuleKnowledge.FRAGMENT_KNOWLEDGE)
class KnowledgeFragment : SupportFragment(), OnLoadMoreListener, OnRefreshListener {
    lateinit var dataBinding: FragmentKnowledgeBinding
    lateinit var viewModel: KnowledgeViewModel

    private lateinit var mRefreshLayout: SmartRefreshLayout
    private lateinit var recyclerView: RecyclerView
    lateinit var knowledgeAdapter: KnowledgeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_knowledge,
            container,
            false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRefreshLayout()
        initData()
    }

    private fun initView() {
        recyclerView = dataBinding.rvKnowledge
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val divider = RecyclerViewDivider(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            dp2px(requireContext(), 1.0F),
            requireContext().resources.getColor(R.color.divider)
        )
        recyclerView.addItemDecoration(divider)
    }

    private fun initRefreshLayout() {
        mRefreshLayout = dataBinding.refreshLayout
        mRefreshLayout.setEnableRefresh(true)
        mRefreshLayout.setEnableLoadMore(true)
        mRefreshLayout.setOnRefreshListener(this)
        mRefreshLayout.setOnLoadMoreListener(this)
    }

    private fun initData() {
        viewModel = ViewModelProvider(this).get(KnowledgeViewModel::class.java)
        viewModel.getTreeList()

        //请求数据
        viewModel.getKnowledgeLiveData().observe(viewLifecycleOwner) {
            knowledgeAdapter = KnowledgeAdapter(this@KnowledgeFragment.context!!, it)
            knowledgeAdapter.onItemClickListener { item, position ->
                //  (parentFragment as KnowledgeMainFragment).startB
                start(
                    ARouterUtils.obtainFragmentWithBundle(
                        ARouterConstant.ModuleKnowledge.FRAGMENT_KNOWLEDGE_DETAIL,
                        "cid" to item.id, "title" to item.name
                    )
                )
            }
            recyclerView.adapter = knowledgeAdapter
        }
    }

    /****************************  Smart refresh layout interface ***************************************/
    override fun onLoadMore(refreshLayout: RefreshLayout) {
        finishRefresh()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        viewModel = ViewModelProvider(this).get(KnowledgeViewModel::class.java)
        viewModel.getTreeList()

        //请求数据
        viewModel.getKnowledgeLiveData().observe(viewLifecycleOwner) {
            knowledgeAdapter = KnowledgeAdapter(this@KnowledgeFragment.context!!, it)
            knowledgeAdapter.onItemClickListener { item, position ->
                //  (parentFragment as KnowledgeMainFragment).startB
                start(
                    ARouterUtils.obtainFragmentWithBundle(
                        ARouterConstant.ModuleKnowledge.FRAGMENT_KNOWLEDGE_DETAIL,
                        "cid" to item.id, "title" to item.name
                    )
                )
            }
            recyclerView.adapter = knowledgeAdapter

            finishRefresh()
        }
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