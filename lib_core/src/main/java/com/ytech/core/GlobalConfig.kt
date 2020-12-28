package com.ytech.core

import android.content.Context

object GlobalConfig {
    private lateinit var  applicationContext : Context
    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    fun getApplicationContext() = applicationContext
}

