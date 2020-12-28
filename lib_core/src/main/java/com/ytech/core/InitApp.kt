package com.ytech.core

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.ytech.common.common.getAppProcessName
import com.ytech.core.arouter.ARouterUtils
import me.yokeyword.fragmentation.Fragmentation


open class InitApp : Application() {
    private var mAppProcessName = ""

    companion object {
        lateinit var context : Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        mAppProcessName = getAppProcessName()
        if (mAppProcessName == packageName)
            ARouterUtils.init(this)
        
        GlobalConfig.init(this)

        //fragmentation栈查看器，正式版删除
        Fragmentation.builder() // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
            .stackViewMode(Fragmentation.BUBBLE)
            .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
            /**
             * 可以获取到[me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning]
             * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
             */
            .handleException {
                // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                // Bugtags.sendException(e);
            }
            .install()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouterUtils.destroy()
    }





}
