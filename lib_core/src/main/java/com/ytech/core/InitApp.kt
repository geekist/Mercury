package com.ytech.core

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.ytech.common.common.getAppProcessName
import com.ytech.core.arouter.ARouterUtils


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
