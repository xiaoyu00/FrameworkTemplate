package com.framework.base.simple

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.framework.base.R
import com.framework.base.utils.PermissionUtils
import java.util.*

/**
 * 权限申请(兼容 Android 11)
 * 注：在Android 11 定位权限【ACCESS_BACKGROUND_LOCATION】需要单独申请
 */
class PermissionActivity : AppCompatActivity() {
    private var permissions = arrayOf(
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
    private var isFirst = true
    private var alertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permssion)
    }

    private fun requestPermissions() {
        val permissionList: List<String> =
            PermissionUtils.checkMorePermissions(this, listOf(*permissions))
        if (permissionList.isEmpty()) {
            if (!PermissionUtils.checkPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                PermissionUtils.requestPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    1008612
                )
            }
        } else {
            PermissionUtils.requestMorePermissions(this, listOf(*permissions), 1008611)
        }
    }
    private fun checkAndSetting(){
        if (!Settings.canDrawOverlays(this)) {
            AlertDialog.Builder(this).setTitle("视频通话功能需允许在应用上层显示").setPositiveButton(
                "去设置"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
                val intent =
                    Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(intent)
            }.setNegativeButton(
                "取消"
            ) { dialogInterface, i -> dialogInterface.dismiss() }.create().show()
        }
    }
    private fun startPermissionSetting() {
        alertDialog = AlertDialog.Builder(this).setTitle("视频通话功能需要以下权限")
            .setMessage("1.锁屏显示\n2.后台弹出界面\n3.显示悬浮窗\n\n[设置-> 权限管理]").setPositiveButton(
                "去设置"
            ) { dialogInterface, i ->
                dialogInterface.dismiss()
                PermissionUtils.toAppSetting(this@PermissionActivity)
            }.setNegativeButton(
                "取消"
            ) { dialogInterface, i -> dialogInterface.dismiss() }.create()
        alertDialog?.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1008612 || requestCode == 1008611) {
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
            if (permissions.size == 1 && permissions[0] == Manifest.permission.ACCESS_BACKGROUND_LOCATION) {
                startPermissionSetting()
            }
        }
    }
}