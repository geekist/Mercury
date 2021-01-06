package com.ytech.mercury

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.hjq.toast.ToastUtils
import com.ytech.about.AboutFragment
import com.ytech.apply.apply.ApplyFragment
import com.ytech.core.GlobalConfig
import com.ytech.core.animator.CustomHorizontalAnimator
import com.ytech.core.arouter.ARouterConstant
import com.ytech.core.arouter.ARouterUtils
import com.ytech.home.home.HomeFragment
import com.ytech.knowledge.knowledge.KnowledgeFragment
import com.ytech.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*
import me.yokeyword.fragmentation.anim.DefaultNoAnimator

class MainFragment : BaseFragment() {
    companion object {
        const val MODULE_HOME_INDEX = 0
        const val MODULE_APPLY_INDEX = 1
        const val MODULE_KNOWLEDGE_INDEX = 2
        const val MODULE_ABOUT_INDEX = 3
    }

    private val mTitles by lazy { context!!.resources.getStringArray(R.array.bottomNavigation)}
    private var mSelectIndex = 0

    private val mFragments by lazy {
        arrayOf(
            ARouterUtils.obtainFragment(ARouterConstant.ModuleApply.FRAGMENT_APPLY),
            ARouterUtils.obtainFragment(ARouterConstant.ModuleHome.FRAGMENT_HOME),
            ARouterUtils.obtainFragment(ARouterConstant.ModuleKnowledge.FRAGMENT_KNOWLEDGE),
            ARouterUtils.obtainFragment(ARouterConstant.ModuleAbout.FRAGMENT_ABOUT)
        )
    }

    private var mPressedTime = 0L


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initRootContainer(): Any {
        return R.layout.fragment_main
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

     override fun initView(savedInstanceState: Bundle?, rootView: View) {
        mLine.setGuidelineEnd(mTabs.getTabHeight())
        mTabs.addItem(
            getDrawable(R.drawable.ic_home_white_24dp,R.color.text_subtitle)!!,
            getDrawable(R.drawable.ic_home_white_24dp,R.color.secondaryColor)!!,
            mTitles[MODULE_HOME_INDEX]
        ).addItem(
            getDrawable(R.drawable.ic_project_white_24dp,R.color.text_subtitle)!!,
            getDrawable(R.drawable.ic_project_white_24dp,R.color.secondaryColor)!!,
            mTitles[MODULE_APPLY_INDEX]
        ).addItem(
            getDrawable(R.drawable.ic_apply_white_24dp,R.color.text_subtitle)!!,
            getDrawable(R.drawable.ic_apply_white_24dp,R.color.secondaryColor)!!,
            mTitles[MODULE_KNOWLEDGE_INDEX]
        ).addItem(
            getDrawable(R.drawable.ic_about_white_24dp,R.color.text_subtitle)!!,
            getDrawable(R.drawable.ic_about_white_24dp,R.color.secondaryColor)!!,
            mTitles[MODULE_ABOUT_INDEX]
        ).complete(mSelectIndex)
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        fragmentAnimator = CustomHorizontalAnimator()
        //    checkVersion()
        initMultipleRootFragment()
    }

    override fun onCreateFragmentAnimator() = DefaultNoAnimator()

    override fun onBackPressedSupport(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mPressedTime > 2000) {
            ToastUtils.show("再按一次退出程序")
            mPressedTime = currentTime
            return true
        }
        return super.onBackPressedSupport()
    }

    private fun initMultipleRootFragment() {
        val firstFragment = findChildFragment(ApplyFragment::class.java)
        when (firstFragment == null) {
            true -> {
                loadMultipleRootFragment(
                    R.id.fl_Container,
                    mSelectIndex,
                    mFragments[MODULE_HOME_INDEX],
                    mFragments[MODULE_APPLY_INDEX],
                    mFragments[MODULE_KNOWLEDGE_INDEX],
                    mFragments[MODULE_ABOUT_INDEX]
                )
            }
            else -> {
                mFragments[MODULE_HOME_INDEX] = firstFragment
                mFragments[MODULE_APPLY_INDEX] = findChildFragment(HomeFragment::class.java)
                mFragments[MODULE_KNOWLEDGE_INDEX] = findChildFragment(KnowledgeFragment::class.java)
                mFragments[MODULE_ABOUT_INDEX] = findChildFragment(AboutFragment::class.java)
            }
        }
    }

    override fun initListener() {
        mTabs.setOnItemClick { index, oldIndex ->
            mSelectIndex = index
            showHideFragment(mFragments[index], mFragments[oldIndex])
            false
        }
        mTabs.setOnItemReselected { index ->
            mFragments[index].let { fragment ->
              //  if (fragment is IRefresh) fragment.refresh()
            }
        }
    }

    private fun getDrawable(@DrawableRes drawableRes: Int,@ColorRes colorRes: Int): Drawable? {
        val context = GlobalConfig.getApplicationContext()
        val drawable = ContextCompat.getDrawable(context,drawableRes)
        drawable?.setTint(ContextCompat.getColor(context,colorRes))
        return drawable
    }
}