package com.rami.dataConsumption.ui

import android.Manifest
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rami.dataConsumption.PackageAdapter
import com.rami.dataConsumption.R
import com.rami.dataConsumption.model.PackageData
import com.rami.dataConsumption.utils.NetworkStatsHelper
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val packageAdapter: PackageAdapter = PackageAdapter()
    private var swipeRefresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)
        val rvItems = findViewById<RecyclerView>(R.id.rv_items)
        rvItems.adapter = packageAdapter

        swipeRefresh?.setOnRefreshListener {
            getPackagesData()
        }
    }

    override fun onResume() {
        super.onResume()

        getPackagesData()
    }

    private fun getPackagesData() {
        if (hasPermissions()) {
            fitchData()
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if (!hasPermissionToReadPhoneStats()) {
            requestPhoneStateStats()
        } else if (!hasPermissionToReadNetworkHistory()) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    private fun hasPermissions(): Boolean {
        return hasPermissionToReadNetworkHistory() && hasPermissionToReadPhoneStats()
    }

    private fun hasPermissionToReadNetworkHistory(): Boolean {
        val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == MODE_ALLOWED
    }

    private fun hasPermissionToReadPhoneStats(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPhoneStateStats() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_PHONE_STATE),
            100
        )
    }

    private fun fitchData() {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            val list = NetworkStatsHelper(this).getPackagesData()
            handler.post {
                swipeRefresh?.isRefreshing = false
                updateUi(list)
            }
        }
    }

    private fun updateUi(packagesData: List<PackageData>?) {
        if (packagesData.isNullOrEmpty()){
            Toast.makeText(this, "Empty list", Toast.LENGTH_SHORT).show()
        }
        packageAdapter.submitList(packagesData)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            getPackagesData()
        }
    }
}