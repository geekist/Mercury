package com.ytech.common.device

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.ytech.common.common.windowManager
import java.io.File

/**
 * 根据手机的分辨率将dp转成为px。
 */
fun dp2px(context: Context, dp: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率将px转成dp。
 */
fun px2dp(context: Context, px: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * Value of sp to value of px.
 *
 * @param spValue The value of sp.
 * @return value of px
 */
fun sp2px(spValue: Float): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

/**
 * Value of px to value of sp.
 *
 * @param pxValue The value of px.
 * @return value of sp
 */
fun px2sp(pxValue: Float): Int {
    val fontScale = Resources.getSystem().displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

/**
 * 是否是平板设备
 *
 * @return true 是， false 不是
 */
fun Context.isTablet(): Boolean {
    return this.resources.configuration.screenLayout and Configuration
        .SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

fun Fragment.isTablet(): Boolean {
    return activity?.isTablet() ?: false
}

fun getScreenWidth(context: Context): Int =
    context.resources.displayMetrics.widthPixels

fun getScreenHeight(context: Context): Int =
    context.resources.displayMetrics.heightPixels

fun Context.getRealScreenHeight(): Int {
    val outPoint = Point()
    windowManager?.defaultDisplay?.getRealSize(outPoint)
    return outPoint.y
}

/**
 * 获取屏幕像素：对获取的宽高进行拼接。例：1080X2340。
 */
fun getScreenPixel(context: Context): String {
    context.resources.displayMetrics.run {
        return "${widthPixels}X${heightPixels}"
    }
}

//fun Context.getNavBarHeight() = if (XPopupUtils.isNavBarVisible(this)) XPopupUtils.getNavBarHeight() else 0

fun isDeviceRooted(): Boolean {
    val su = "su"
    val locations = arrayOf(
        "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
        "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
        "/system/sbin/", "/usr/bin/", "/vendor/bin/"
    )
    for (location in locations) {
        if (File(location + su).exists()) {
            return true
        }
    }
    return false
}

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
fun isAdbEnabled(context: Context): Boolean {
    return Settings.Secure.getInt(
        context.contentResolver,
        Settings.Global.ADB_ENABLED, 0
    ) > 0
}


