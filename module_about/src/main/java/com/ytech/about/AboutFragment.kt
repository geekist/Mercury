package com.ytech.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.ytech.about.databinding.FragmentAboutBinding
import com.ytech.core.arouter.ARouterConstant
import com.ytech.core.arouter.ARouterUtils
import com.ytech.ui.base.SupportFragment


@Route(path = ARouterConstant.ModuleAbout.FRAGMENT_ABOUT)
class AboutFragment : SupportFragment() {

    lateinit var mDataBinding : FragmentAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_about,
            container,
            false
        )

        return mDataBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mDataBinding.aboutMe.setOnClickListener {
            ARouterUtils.navigation(ARouterConstant.ModuleAbout.ACTIVITY_ABOUT)
        }

    }
}