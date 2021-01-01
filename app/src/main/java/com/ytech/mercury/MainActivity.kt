package com.ytech.mercury

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.lxj.xpopup.XPopup
import com.permissionx.guolindev.PermissionX
import com.ytech.core.arouter.ARouterConstant
import com.ytech.ui.base.SupportActivity
import com.ytech.ui.dialog.ServiceAgreementDialog
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

@Route(path = ARouterConstant.MAIN_ACTIVITY)
class MainActivity : SupportActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.fl_container, MainFragment())
        }

        showPermission()
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置横向(和安卓4.x动画相同)
        return DefaultHorizontalAnimator()
    }

    private fun showPermission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            )
            // .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "去设置", "取消")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }

                showServiceAgreement()

            }
    }

    private fun showServiceAgreement() {
        XPopup.Builder(this)
            .asCustom(
                ServiceAgreementDialog(this)
                    .onConfirmClick {
                        Toast.makeText(this@MainActivity, "confirm", Toast.LENGTH_LONG).show()
                    }.onDismissClick {
                        finish()
                    }.onServiceAgreementClick {
                        Toast.makeText(this@MainActivity, "conService", Toast.LENGTH_LONG).show()

                    })
            .show()
    }




}