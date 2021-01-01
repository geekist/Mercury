package com.ytech.apply.apply

import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ytech.apply.R
import com.ytech.apply.apply.model.ProjectTabItem
import com.ytech.apply.applydetail.ApplyItemFragment
import com.ytech.apply.databinding.FragmentApplyBinding
import com.ytech.core.arouter.ARouterConstant
import com.ytech.ui.base.SupportFragment

@Route(path = ARouterConstant.ModuleApply.FRAGMENT_APPLY)
class ApplyFragment : SupportFragment() {

    lateinit var mViewModel: ProjectViewModel
    lateinit var mViewBinding: FragmentApplyBinding

    private var mData: MutableList<ProjectTabItem>? = null
    private lateinit var mediator: TabLayoutMediator
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_apply, container, false)

        return mViewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //首先初始化数据，获得tab的个数和名称，然后才可以初始化view
        initData()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediator.detach()
    }

    private fun initData() {
        mViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        mViewModel.getTabData()
        mViewModel.getTabDataLiveData().observe(viewLifecycleOwner, Observer {
            mData = it
            initView()
        })
    }

     private fun initView() {

        mViewPager = mViewBinding.viewPager
        mTabLayout = mViewBinding.tabLayout

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

        mViewPager.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                if (mData != null && mData!!.size > 0) {
                    return mData!!.size
                }
                return 0
            }

            override fun createFragment(position: Int): Fragment {
                val item = mData!![position]
                return createItemFragment(item.id)
            }
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

    private fun createItemFragment(id: Int): Fragment {
        return ApplyItemFragment.newInstance(id)
    }

    private fun createTabView(position: Int): View {

        if (mData != null && mData!!.size > 0) {
            val item = mData!![position]
            val textView = TextView(requireContext())


            textView.text = Html.fromHtml(item.name)
            return textView
        }

        return TextView(requireContext())

    }


}