package com.ytech.about

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ktx.immersionBar
import com.ytech.about.databinding.ActivityBrandBinding
import com.ytech.common.common.getAppVersionName
import com.ytech.core.arouter.ARouterConstant
import com.ytech.ui.base.SupportActivity


@Route(path= ARouterConstant.ModuleAbout.ACTIVITY_BRAND)
class BrandActivity : SupportActivity() {

    lateinit var binding: ActivityBrandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_brand)

        binding.version.text = getAppVersionName()

        initActionBar()
        initImmersionBar()
    }

    private fun initActionBar() {
        binding.mTvTitle.text = resources.getString(R.string.app_name)
        binding.mIvBack.setOnClickListener {
            finish()
        }
    }

    private fun initImmersionBar() {
        immersionBar {
            //   fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
            //.statusBarColor(R.color.primaryColor)
            // transparentStatusBar()
            statusBarColor(R.color.white)
            // navigationBarColor(R.color.colorPrimary)
            statusBarDarkFont(true,0.2f)
        }
    }
}