package com.ytech.ui.dialog

import android.content.Context
import android.graphics.Color
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.util.XPopupUtils
import com.ytech.common.text.clickSpan
import com.ytech.common.view.click
import com.ytech.ui.R
import kotlinx.android.synthetic.main.common_layout_service_agreement_dialog.view.*


class ServiceAgreementDialog(context: Context) : CenterPopupView(context) {
    private var mOnConfirm: (() -> Unit)? = null
    private var mOnDismiss: (() -> Unit)? = null
    private var mOnServiceAgreementClick: ((dialog: ServiceAgreementDialog) -> Unit)? = null

    fun onConfirmClick(l: () -> Unit): ServiceAgreementDialog {
        this.mOnConfirm = l
        return this
    }

    fun onDismissClick(l: () -> Unit): ServiceAgreementDialog {
        this.mOnDismiss = l
        return this
    }

    fun onServiceAgreementClick(l: (dialog: ServiceAgreementDialog) -> Unit): ServiceAgreementDialog {
        this.mOnServiceAgreementClick = l
        return this
    }

    override fun getImplLayoutId() = R.layout.common_layout_service_agreement_dialog

    override fun onCreate() {
        val serviceAgreement = "《用户协议与隐私政策》"
        val content =
            "请你务必审慎阅读、充分理解“服务协议”各条款，包括但不限于：为了向你提供即时通讯、内容分享等服务，我们需要收集你的设备信息、操作日志等个人信息。你可阅读${serviceAgreement}了解详细信息。如你同意，请点击“同意”开始接受我们的服务。"
        val index = content.indexOf(serviceAgreement)
        mTvContent.clickSpan(content,
            index..index + serviceAgreement.length,
            color =  ContextCompat.getColor(context,R.color.secondaryColor),
            clickListener = OnClickListener {
                mOnServiceAgreementClick?.invoke(this)
            })
        mTvCancel.click { dismissWith { mOnDismiss?.invoke() } }
        mTvConfirm.click { dismissWith { mOnConfirm?.invoke() } }
    }

    override fun getMaxWidth(): Int {
        return (XPopupUtils.getWindowWidth(context) * 0.7f).toInt()
    }
}