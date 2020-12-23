package com.ytech.mercury

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hjq.toast.ToastUtils
import com.ytech.about.AboutFragment
import com.ytech.apply.ApplyFragment
import com.ytech.core.animator.CustomHorizontalAnimator
import com.ytech.core.arouter.ARouterConstant
import com.ytech.core.arouter.ARouterUtils
import com.ytech.core.support.SupportFragment
import com.ytech.home.HomeFragment
import com.ytech.knowledge.KnowledgeFragment
import kotlinx.android.synthetic.main.fragment_main.*
import me.yokeyword.fragmentation.anim.DefaultNoAnimator

class MainFragment : SupportFragment() {
    companion object {
        const val MODULE_HOME_INDEX = 0
        const val MODULE_APPLY_INDEX = 1
        const val MODULE_KNOWLEDGE_INDEX = 2
        const val MODULE_ABOUT_INDEX = 3
    }

    private val mTitles by lazy { arrayOf("首页", "应用", "知识", "关于") }
    private var mSelectIndex = 0

    private val mFragments by lazy {
        arrayOf(
            ARouterUtils.obtainFragment(ARouterConstant.ModuleHome.FRAGMENT_HOME),
            ARouterUtils.obtainFragment(ARouterConstant.ModuleApply.FRAGMENT_APPLY),
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
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState, view)
    }

    private fun initView(savedInstanceState: Bundle?, rootView: View) {
        mLine.setGuidelineEnd(mTabs.getTabHeight())
        mTabs.addItem(
            R.drawable.ic_message_unchecked,
            R.drawable.ic_message_checked,
            mTitles[MODULE_HOME_INDEX]
        ).addItem(
            R.drawable.ic_attendance_unchecked,
            R.drawable.ic_attendance_checked,
            mTitles[MODULE_APPLY_INDEX]
        ).addItem(
            R.drawable.ic_classroom_unchecked,
            R.drawable.ic_classroom_checked,
            mTitles[MODULE_KNOWLEDGE_INDEX]
        ).addItem(
            R.drawable.ic_student_unchecked,
            R.drawable.ic_student_checked,
            mTitles[MODULE_ABOUT_INDEX]
        ).complete(mSelectIndex)

        initListener()
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
            //   fastShow("再按一次退出程序")
            mPressedTime = currentTime
            return true
        }
        return super.onBackPressedSupport()
    }

    private fun initMultipleRootFragment() {
        val firstFragment = findChildFragment(HomeFragment::class.java)
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
                mFragments[MODULE_APPLY_INDEX] = findChildFragment(ApplyFragment::class.java)
                mFragments[MODULE_KNOWLEDGE_INDEX] = findChildFragment(KnowledgeFragment::class.java)
                mFragments[MODULE_ABOUT_INDEX] = findChildFragment(AboutFragment::class.java)
            }
        }
    }

    private fun initListener() {
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
}