package com.ytech.common.common

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Process
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun getAppProcessName(): String {
    return try {
        val file = File("/proc/" + Process.myPid() + "/" + "cmdline")
        val mBufferedReader = BufferedReader(FileReader(file))
        val processName = mBufferedReader.readLine().trim { it <= ' ' }
        mBufferedReader.close()
        processName
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

/**
 * 获取应用的版本名称
 *
 * @param pkgName 包名
 * @return App版本号  ""表示失败
 */
fun Context.getVersionName(pkgName: String = packageName): String {
    if (pkgName.isBlank()) return ""
    return try {
        packageManager.getPackageInfo(pkgName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}

/**
 * 获取App版本码
 *
 * @param pkgName 包名
 * @return App版本码  -1表示失败
 */
fun Context.getVersionCode(pkgName: String = packageName): Int {
    if (pkgName.isBlank()) return -1
    return try {
        packageManager.getPackageInfo(pkgName, 0).versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        -1
    }
}


/**
 * 获取当前app的版本号
 */
fun Context.getAppVersionName(): String {
    val appContext = applicationContext
    val manager = appContext.packageManager
    try {
        val info = manager.getPackageInfo(appContext.packageName, 0)

        if (info != null)
            return info.versionName

    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return ""
}

fun Context.getAppVersionCode(): Int {
    val appContext = applicationContext
    val manager = appContext.packageManager
    try {
        val info = manager.getPackageInfo(appContext.packageName, 0)
        if (info != null)
            return info.versionCode

    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return 0
}

/**
 * 获取应用的包名
 */
fun Context.getPackageName(): String = packageName


/**
 * 安装apk
 */
fun Context.installApk(file: File) {
    val intent = Intent()
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.action = Intent.ACTION_VIEW
    val uri: Uri
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    } else {
        uri = Uri.fromFile(file)
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    this.startActivity(intent)
}

/**
 * 判断App是否安装
 *
 * @param pkgName 包名
 * @return
 */
fun Context.isInstallApp(pkgName: String): Boolean {
    return pkgName.isNotBlank() && packageManager.getLaunchIntentForPackage(packageName) != null
}

/**
 * 判断App是否处于前台
 *
 * @return
 */
fun Context.isAppForeground(pkgName: String = packageName): Boolean {
    if (pkgName.isBlank()) return false
    val infoList: (List<ActivityManager.RunningAppProcessInfo>)? =
        activityManager?.runningAppProcesses
    infoList?.forEach {
        if (it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            return it.processName == pkgName
        }
    }
    return false
}


/*
  ---------- Fragment ----------
 */
fun Fragment.getVersionName(pkgName: String = activity!!.packageName): String {
    return activity?.getVersionName(pkgName) ?: ""
}

fun Fragment.getVersionCode(pkgName: String = activity!!.packageName): Int {
    return activity?.getVersionCode(pkgName) ?: -1
}

/*fun Fragment.installApp(file: File, authority: String, writeEnable: Boolean) {
    activity?.installApp(file, authority, writeEnable)
}*/

fun Fragment.isInstallApp(pkgName: String): Boolean {
    return activity?.isInstallApp(pkgName) ?: false
}

fun Fragment.isAppForeground(pkgName: String = activity!!.packageName): Boolean {
    return activity?.isAppForeground(pkgName) ?: false
}
