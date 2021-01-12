package com.ytech.apply.apply

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ytech.apply.R
import com.ytech.apply.apply.adapter.ApplyFragmentStateAdapter
import com.ytech.apply.databinding.FragmentApplyBinding
import com.ytech.core.arouter.ARouterConstant
import com.ytech.model.apply.ProjectTabItem
import com.ytech.ui.base.SupportFragment

@Route(path = ARouterConstant.ModuleApply.FRAGMENT_APPLY)
class ApplyFragment : SupportFragment() {

    lateinit var viewModel: ProjectViewModel
    lateinit var viewBinding: FragmentApplyBinding

    private lateinit var mediator: TabLayoutMediator
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager2

    private var listProjectTabItem: MutableList<ProjectTabItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_apply, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //首先初始化数据，获得tab的个数和名称，然后初始化view
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediator.detach()
    }

    private fun initData() {
        viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        viewModel.getTabData()
        viewModel.getTabDataLiveData().observe(viewLifecycleOwner, Observer {
            listProjectTabItem = it
            initView()
        })
    }

    private fun initView() {
        mViewPager = viewBinding.viewPager
        mTabLayout = viewBinding.tabLayout

        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView as TextView
                customView.textSize = 14f
                customView.typeface = Typeface.DEFAULT
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView as TextView
                customView.textSize = 16f
                customView.typeface = Typeface.DEFAULT_BOLD
            }
        })

        mViewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        mViewPager.adapter =
            listProjectTabItem?.let {
                ApplyFragmentStateAdapter(
                    it,
                    childFragmentManager,
                    lifecycle
                )
            }

        mediator = TabLayoutMediator(
            mTabLayout,
            mViewPager,
            true,
            TabLayoutMediator.TabConfigurationStrategy { tab, position -> //创建tab
                tab.customView = createTabView(position)
            })
        mediator.attach()

    }

    private fun createTabView(position: Int): View {
        if (listProjectTabItem != null && listProjectTabItem!!.size > 0) {
            val item = listProjectTabItem!![position]
            val textView = TextView(requireContext())
            textView.text = Html.fromHtml(item.name)
            return textView
        }

        return TextView(requireContext())
    }
}