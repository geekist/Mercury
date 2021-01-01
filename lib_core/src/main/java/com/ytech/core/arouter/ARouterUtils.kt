package com.ytech.core.arouter

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.ytech.common.common.buildBundle
import com.ytech.core.BuildConfig
import me.yokeyword.fragmentation.ISupportFragment

object ARouterUtils {
    /**
     * App onCreate()执行
     */
    fun init(application: Application) {
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()     // 打印日志
            ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application) // 尽可能早，推荐在Application中初始化
    }

    /**
     * App onTerminate()执行
     */
    fun destroy() {
        ARouter.getInstance().destroy()
    }

    /**
     * 在activity中添加
     */
    fun injectActivity(activity: FragmentActivity?) {
        activity?.let { ARouter.getInstance().inject(it) }
    }

    /**
     * 在fragment中添加
     */
    fun injectFragment(fragment: Fragment?) {
        fragment?.let { ARouter.getInstance().inject(fragment) }
    }

    /**
     * 简单跳转
     */
    fun navigation(path: String?) {
        path?.let {
            ARouter.getInstance()
                .build(it)
                .navigation()
        }
    }

    fun obtainService(path: String): Any? {
        return ARouter.getInstance()
            .build(path)
            .navigation()
    }

    fun obtainFragment(path: String): ISupportFragment {
        return ARouter.getInstance()
            .build(path)
            .navigation() as ISupportFragment
    }

    fun obtainFragmentWithBundle(
        path: String, vararg params: Pair<String, Any?>
    ): ISupportFragment {
        return obtainFragmentWithBundle(path, buildBundle(*params))
    }

    private fun obtainFragmentWithBundle(path: String, args: Bundle): ISupportFragment {
        return ARouter.getInstance()
            .build(path)
            .with(args)
            .navigation() as ISupportFragment
    }

    fun startWrapActivity(
        context: Context, fragmentPath: String,
        vararg params: Pair<String, Any?>
    ) {
        /*ARouter.getInstance()
            .build(ARouterConstant.WRAP_ACTIVITY)
            .withString(Constants.EXTRA_FRAGMENT_PATH, fragmentPath)
            .withBundle(Constants.EXTRA_BUNDLE, buildBundle(*params))
            .navigation(context)*/
    }


}