package com.ytech.ui.adapter

import androidx.annotation.IntDef

import kotlin.annotation.Retention

@Target(AnnotationTarget.VALUE_PARAMETER)
@IntDef(flag = true, value = [ISelect.SINGLE_MODE, ISelect.MULTIPLE_MODE, ISelect.SINGLE_MODE_DISABLE])
@Retention(AnnotationRetention.SOURCE)
annotation class SelectMode
