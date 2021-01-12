package com.ytech.ui.base

import android.os.Bundle
import android.view.View
import com.hjq.toast.ToastUtils
import com.ytech.common.network.isNetworkConnected
import com.ytech.ui.R
import com.ytech.ui.widget.MultipleStatusLayout

/**
 * 当没有网络或没有数据时，显示提示或刷新按钮
 */
abstract class BaseLoadingFragment : BaseFragment() {
    private var mStatusLayout: MultipleStatusLayout? = null

    override fun initView(savedInstanceState: Bundle?, rootView: View) {
        mStatusLayout = rootView.findViewById(R.id.mStatusLayout)
        setUpStatusLayout()
    }

    protected open fun setUpStatusLayout() {
        getStatusLayout().setOnRetry { actionText -> onRetry(actionText) }
    }

    open fun showLoading() {
        getStatusLayout().showLoadingLayout()
    }

    open fun hideLoading(runnable: Runnable?) {
        if (context!!.isNetworkConnected) getStatusLayout().showContentLayout()
    }

    open fun showMessage(message: String) {
        ToastUtils.show(message)
        checkAndShowNoNetworkStatusLayout()
    }

    private fun checkAndShowNoNetworkStatusLayout() {
        if (!context!!.isNetworkConnected) {
            // 没有网络首先显示无网络错误视图
            getStatusLayout().showErrorLayout(
                iconResId = R.drawable.ic_no_network,
                title = context!!.getString(R.string.empty_title3)
            )
        }
    }

    /**
     * 错误页面的按钮点击事件
     */
    open fun onRetry(actionText: String) {}

    protected open fun getStatusLayout(): MultipleStatusLayout = mStatusLayout!!
}