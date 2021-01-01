package com.ytech.ui.dialog

import android.content.Context
import com.lxj.xpopup.core.CenterPopupView
import com.ytech.ui.R

class LoadingDialog(context: Context) : CenterPopupView(context) {
    override fun getImplLayoutId() = R.layout.common_layout_loading_dialog
}