package com.ytech.ui.base

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import com.gyf.barlibrary.ImmersionBar
import com.gyf.barlibrary.ImmersionOwner
import com.gyf.barlibrary.ImmersionProxy
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.ytech.common.common.hasMinimumSdk
import com.ytech.ui.R
import com.ytech.ui.dialog.LoadingDialog
import com.ytech.ui.widget.CollapsingTitleBar
import com.ytech.ui.widget.TitleBar

/**
 * 所有Fragment的基类，提供Fragmentation、Immersion、Toolbar以及Menu的加载，还提供了一个loading dialog
 */
abstract class BaseFragment : SupportFragment(), ImmersionOwner, Toolbar.OnMenuItemClickListener {
    private val mImmersionProxy: ImmersionProxy by lazy { ImmersionProxy(this) }
    private val mImmersionBar: ImmersionBar by lazy { ImmersionBar.with(this) }
    private var mSupportStatusBarDarkFontFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImmersionProxy.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mImmersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View
        val rootContainer = initRootContainer()
        rootView = when (rootContainer) {
            is Int -> inflater.inflate(rootContainer, container, false)
            is View -> rootContainer
            else -> throw ClassCastException("${javaClass.simpleName} onCreateView rootContainer must be Integer or View")
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState, view)
        initTitleBar()
        initData()
        initListener()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        mImmersionProxy.onResume()
    }

    override fun onPause() {
        hideSoftInput()
        super.onPause()
        mImmersionProxy.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mImmersionProxy.onDestroy()
    }

    /************************  子类重载的方法 ***********************/
    abstract fun initView(savedInstanceState: Bundle?, rootView: View)

    protected abstract fun initRootContainer(): Any

    open fun initData() {}

    open fun initListener() {}

    open fun getTitleBar(): TitleBar? = null

    open fun getCollapsingTitleBar(): CollapsingTitleBar? = null

    @MenuRes
    open fun getMenuRes(): Int = -1

    open fun onNavigationIconClick() {
        activity?.onBackPressed()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean = false

    protected open fun initTitleBar() {
        getTitleBar()?.let {
            it.getToolBar().setNavigationOnClickListener { onNavigationIconClick() }
            val menuRes = getMenuRes()
            if (menuRes != -1) {
                it.getToolBar().inflateMenu(menuRes)
                it.getToolBar().setOnMenuItemClickListener(this)
                it.changeMenuColorTint()
            }
        }
        getCollapsingTitleBar()?.let {
            it.getToolBar().setNavigationOnClickListener { onNavigationIconClick() }
            val menuRes = getMenuRes()
            if (menuRes != -1) {
                it.getToolBar().inflateMenu(menuRes)
                it.getToolBar().setOnMenuItemClickListener(this)
                it.changeMenuColorTint()
            }
        }
    }

    /*************************  show or hide a loading dialog  **********************************/
    private val mLoadingDialog: BasePopupView by lazy {
        XPopup.Builder(context)
            .hasShadowBg(false)
            .dismissOnTouchOutside(false)
            .asCustom(LoadingDialog(this@BaseFragment.context!!))
    }

    protected open fun getLoadingDialog() = mLoadingDialog

    fun showLoadingDialog(hideSoftInput: Boolean = true) {
        if (!getLoadingDialog().isShow) {
            if (hideSoftInput) hideSoftInput()
            getLoadingDialog().show()
        }
    }

    fun hideLoadingDialog(runnable: Runnable?) {
        if (!getLoadingDialog().isDismiss) {
            getLoadingDialog().dismissWith(runnable)
        }
    }

    /********************    Immersion status bar interface    *******************************/
    open fun supportStatusBarDarkFont(): Boolean = mSupportStatusBarDarkFontFlag

    fun changeSupportStatusBarDarkFont(supportStatusBarDarkFont: Boolean) {
        mSupportStatusBarDarkFontFlag = supportStatusBarDarkFont
    }

    override fun initImmersionBar() {
        refreshImmersionBar(supportStatusBarDarkFont())
    }

    /**
     * 懒加载，在view初始化完成之前执行
     * On lazy after view.
     */
    override fun onLazyBeforeView() {
    }

    /**
     * 懒加载，在view初始化完成之后执行
     * On lazy before view.
     */
    override fun onLazyAfterView() {
    }

    /**
     * Fragment用户可见时候调用
     * On visible.
     */
    override fun onVisible() {
    }

    /**
     * Fragment用户不可见时候调用
     * On invisible.
     */
    override fun onInvisible() {
    }

    open fun refreshImmersionBar(supportStatusBarDarkFont: Boolean) {
        mImmersionBar.statusBarDarkFont(
            supportStatusBarDarkFont, if (hasMinimumSdk(Build.VERSION_CODES.M)) 0f else 0.4f
        ).autoNavigationBarDarkModeEnable(true, 0f)
            .navigationBarColor(R.color.black).init()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mImmersionProxy.isUserVisibleHint = isVisibleToUser
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mImmersionProxy.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mImmersionProxy.onConfigurationChanged(newConfig)
    }

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    override fun immersionBarEnabled(): Boolean = false
}