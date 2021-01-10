package com.ytech.about

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.gyf.immersionbar.ktx.immersionBar

import com.ytech.about.databinding.ActivityAboutMeBinding
import com.ytech.core.arouter.ARouterConstant
import com.ytech.core.arouter.ARouterUtils
import com.ytech.ui.base.SupportActivity

@Route(path=ARouterConstant.ModuleAbout.ACTIVITY_ABOUT)
class AboutMeActivity : SupportActivity() {

    private lateinit var binding: ActivityAboutMeBinding
    private lateinit var pdfView : PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_about_me)
        initImmersionBar()
        initActionBar()

        pdfView = binding.pdfView
        pdfView.setBackgroundColor(Color.LTGRAY)
        displayFromAsset("me.pdf")
    }

    private fun displayFromAsset(assetFileName: String) {
        pdfView.fromAsset(assetFileName)
            .defaultPage(0)
           // .onPageChange(this)
            .enableAnnotationRendering(true)
          //  .onLoad(this)
          //  .scrollHandle(DefaultScrollHandle(this))
            .spacing(2) // in dp
           // .onPageError(this)
            .pageFitPolicy(FitPolicy.BOTH)
            .load()
    }

    private fun initActionBar() {
        binding.mTvTitle.text = "个人简介"
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