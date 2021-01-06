package com.ytech.knowledge.knowledge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.ytech.core.arouter.ARouterConstant
import com.ytech.core.arouter.ARouterUtils
import com.ytech.ui.base.SupportFragment
import com.ytech.knowledge.R
import com.ytech.knowledge.databinding.FragmentKnowledgeBinding
import com.ytech.knowledge.knowledge.adapter.KnowledgeAdapter

@Route(path = ARouterConstant.ModuleKnowledge.FRAGMENT_KNOWLEDGE)
class KnowledgeFragment : SupportFragment() {
    lateinit var fragmentKnowledgeBinding: FragmentKnowledgeBinding
    lateinit var knowledgeViewModel: KnowledgeViewModel

    private lateinit var recyclerView: RecyclerView
    lateinit var knowledgeAdapter: KnowledgeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentKnowledgeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_knowledge,
            container,
            false
        )

        return fragmentKnowledgeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        recyclerView = fragmentKnowledgeBinding.rvKnowledge
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.home_list_divider
            )!!
        )
        recyclerView.addItemDecoration(decoration)
    }

    private fun initData() {
        knowledgeViewModel = ViewModelProvider(this).get(KnowledgeViewModel::class.java)
        knowledgeViewModel.getTreeList()

        //请求数据
        knowledgeViewModel.getKnowledgeLiveData().observe(viewLifecycleOwner) {
            knowledgeAdapter = KnowledgeAdapter(this@KnowledgeFragment.context!!, it)
            knowledgeAdapter.mOnItemClickListener { item, position ->
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


}