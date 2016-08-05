package com.ipvans.meetapp.data

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

fun getSDKVersion() = Build.VERSION.SDK_INT

fun getResolution(context: Context): String {
    val dm = DisplayMetrics()
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    wm.defaultDisplay.getMetrics(dm)
    return "${dm.widthPixels}x${dm.heightPixels}"
}

fun getCodeVersion(context: Context): Int {
    try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        return 0
    }
}

fun getCodeName(context: Context): String {
    try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        return ""
    }
}