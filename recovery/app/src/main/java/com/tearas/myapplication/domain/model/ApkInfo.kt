package com.tearas.myapplication.domain.model

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import java.io.File

data class ApkInfo(
    val appName: String,
    val appIcon: Drawable,
    val packageName: String,
    val versionName: String,
    val versionCode: Int,
    val apkFile: File
)

fun getApkInfo(context: Context, apkFile: File): ApkInfo? {
    val packageManager: PackageManager = context.packageManager
    val packageInfo =
        packageManager.getPackageArchiveInfo(apkFile.path, PackageManager.GET_META_DATA)
    packageInfo?.applicationInfo?.let {
        it.sourceDir = apkFile.path
        it.publicSourceDir = apkFile.path

        val appName = packageManager.getApplicationLabel(it).toString()
        val appIcon = packageManager.getApplicationIcon(it)
        val packageName = packageInfo.packageName
        val versionName = packageInfo.versionName
        val versionCode = packageInfo.versionCode

        return ApkInfo(appName, appIcon, packageName, versionName, versionCode, apkFile)
    }
    return null
}
