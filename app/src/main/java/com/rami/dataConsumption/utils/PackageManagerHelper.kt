package com.rami.dataConsumption.utils

import android.Manifest
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import com.rami.dataConsumption.model.PackageData

/**
 * Created by Rami El-bouhi on 09,December,2021
 */
object PackageManagerHelper {

    fun getPackagesData(context: Context): MutableList<PackageData> {
        val packageManager = context.packageManager
        val packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        val packageDataList: MutableList<PackageData> = ArrayList(packageInfoList.size)
        for (packageInfo in packageInfoList) {
            if (packageManager.checkPermission(
                    Manifest.permission.INTERNET,
                    packageInfo.packageName
                ) == PackageManager.PERMISSION_DENIED
            ) {
                continue
            }
            var ai: ApplicationInfo? = null
            try {
                ai = packageManager.getApplicationInfo(
                    packageInfo.packageName,
                    PackageManager.GET_META_DATA
                )
            } catch (e: NameNotFoundException) {
                e.printStackTrace()
            }
            if (ai == null) {
                continue
            }
            val appName = packageManager.getApplicationLabel(ai)
            packageDataList.add(
                PackageData(
                    name = appName.toString(),
                    version = packageInfo.versionName,
                    packageName = packageInfo.packageName,
                    uid = getPackageUid(packageManager, packageInfo.packageName)
                )
            )
        }
        return packageDataList
    }

    private fun getPackageUid(packageManager: PackageManager?, packageName: String?): Int {
        var uid = -1
        try {
            val packageInfo =
                packageManager?.getPackageInfo(packageName ?: "", PackageManager.GET_META_DATA)
            uid = packageInfo?.applicationInfo?.uid ?: -1
        } catch (e: NameNotFoundException) {
        }
        return uid
    }
}