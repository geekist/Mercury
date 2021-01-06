package com.ytech.home.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.youth.banner.indicator.CircleIndicator
import com.ytech.core.arouter.ARouterConstant
import com.ytech.home.R
import com.ytech.home.databinding.FragmentHomeBinding
import com.ytech.home.home.adapter.HomeBannerAdapter
import com.ytech.model.home.Banner
import com.ytech.home.homelist.HomeListFragment
import com.ytech.ui.base.SupportFragment

@Route(path = ARouterConstant.ModuleHome.FRAGMENT_HOME)
class HomeFragment : SupportFragment() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )

        loadRootFragment(R.id.homeListContainer, HomeListFragment())

        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        fragmentHomeBinding.banner.indicator = CircleIndicator(context)
    }

    private fun initData() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getBanner()
        homeViewModel.getBannerLiveData().observe(viewLifecycleOwner,
            Observer<List<com.ytech.model.home.Banner>> {
                fragmentHomeBinding.banner.adapter = HomeBannerAdapter(it)
            })
    }
}