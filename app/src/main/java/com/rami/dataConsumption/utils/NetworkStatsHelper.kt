package com.rami.dataConsumption.utils

import android.annotation.SuppressLint
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.RemoteException
import android.telephony.TelephonyManager
import com.rami.dataConsumption.model.PackageData

/**
 * Created by Rami El-bouhi on 09,December,2021
 */
class NetworkStatsHelper constructor(private val context: Context) {

    private val networkStatsManager =
        context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager

    fun getPackagesData(): List<PackageData> {
        val packagesData = PackageManagerHelper.getPackagesData(context)
        val subscribeId = getSubscriberId(ConnectivityManager.TYPE_MOBILE)
        for (packageData in packagesData) {
            packageData.bytes = getPackageMobileBytes(packageData.uid ?: 0, subscribeId)
        }
        // remove zero consuming
        packagesData.removeAll { it.bytes?:0L == 0L }
        // sort by usage
        packagesData.sortByDescending { it.bytes }
        return packagesData
    }

    private fun getPackageMobileBytes(uid: Int, subscriberId:String?): Long {
        val networkStats: NetworkStats? = try {
            networkStatsManager.queryDetailsForUid(
                ConnectivityManager.TYPE_MOBILE,
                subscriberId,
                0,
                System.currentTimeMillis(),
                uid
            )
        } catch (e: RemoteException) {
            return -1
        }
        var bytes = 0L
        val bucket = NetworkStats.Bucket()
        while (networkStats?.hasNextBucket() == true) {
            networkStats.getNextBucket(bucket)
            bytes += bucket.rxBytes
            bytes += bucket.txBytes
        }
        networkStats?.close()
        return bytes
    }

    @SuppressLint("MissingPermission")
    private fun getSubscriberId(networkType: Int): String? {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm.subscriberId
        }
        return ""
    }
}