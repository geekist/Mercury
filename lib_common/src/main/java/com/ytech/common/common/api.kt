package com.ytech.common.common

import android.os.Build

inline fun toApi(toVersion: Int, inclusive: Boolean = false, action: () -> Unit) {
    if (Build.VERSION.SDK_INT < toVersion || (inclusive && Build.VERSION.SDK_INT == toVersion)) action()
}

inline fun fromApi(fromVersion: Int, inclusive: Boolean = true, action: () -> Unit) {
    if (Build.VERSION.SDK_INT > fromVersion || (inclusive && Build.VERSION.SDK_INT == fromVersion)) action()
}

fun hasMinimumSdk(minimumSdk: Int): Boolean {
    return Build.VERSION.SDK_INT >= minimumSdk
}


/**
 * Return the version name of device's system.
 *
 * @return the version name of device's system
 */
fun getSDKVersionName(): String? {
    return Build.VERSION.RELEASE
}

/**
 * Return version code of device's system.
 *
 * @return version code of device's system
 */
fun getSDKVersionCode(): Int {
    return Build.VERSION.SDK_INT
}