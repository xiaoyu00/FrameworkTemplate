package com.framework.base.simple

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.framework.base.R
import com.framework.base.utils.PermissionUtils
import java.util.*

/**
 * 权限申请(兼容 Android 11)
 * 注：在Android 11 定位权限【ACCESS_BACKGROUND_LOCATION】需要单独申请
 */
class PermissionActivity : AppCompatActivity() {
    var permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.MODIFY_AUDIO_SETTINGS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permssion)
    }

    private fun requestPermissions() {
        val permissionList: List<String> =
            PermissionUtils.checkMorePermissions(this, listOf(*permissions))
        if (permissionList.isEmpty()) {
            Toast.makeText(this@PermissionActivity, "权限通过", Toast.LENGTH_SHORT).show()
        } else {
            PermissionUtils.requestMorePermissions(this, listOf(*permissions), 1008611)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isPermissionSuccess = true
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                isPermissionSuccess = false
                break
            }
        }
        if (isPermissionSuccess) {
            if (!PermissionUtils.checkPermission(
                    this@PermissionActivity,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                PermissionUtils.requestPermission(
                    this@PermissionActivity,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    1008612
                )
            }
        }
    }
}