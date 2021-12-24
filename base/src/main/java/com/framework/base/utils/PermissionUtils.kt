package com.framework.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    /**
     * 检测权限
     *
     * @return true：已授权； false：未授权；
     */
    fun checkPermission(context: Context, permission: String?): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检测多个权限
     *
     * @return 未授权的权限
     */
    fun checkMorePermissions(context: Context, permissions: List<String>): List<String> {
        val permissionList: MutableList<String> = ArrayList()
        for (i in permissions.indices) {
            if (!checkPermission(context, permissions[i])) permissionList.add(
                permissions[i]
            )
        }
        return permissionList
    }

    /**
     * 请求权限
     */
    fun requestPermission(context: Context, permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(
            (context as Activity),
            arrayOf(permission),
            requestCode
        )
    }


    /**
     * 请求多个权限
     */
    fun requestMorePermissions(context: Context, permissions: List<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(
            (context as Activity),
            permissions.toTypedArray(),
            requestCode
        )
    }

    /**
     * 判断是否已拒绝过权限
     *
     * @return
     * @describe :如果应用之前请求过此权限但用户拒绝，此方法将返回 true;
     * -----------如果应用第一次请求权限或 用户在过去拒绝了权限请求，
     * -----------并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
     */
    fun judgePermission(context: Context, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            (context as Activity),
            permission
        )
    }

    /**
     * 检测权限并请求权限：如果没有权限，则请求权限
     */
    fun checkAndRequestPermission(context: Context, permission: String, requestCode: Int) {
        if (!checkPermission(context, permission)) {
            requestPermission(context, permission, requestCode)
        }
    }

    /**
     * 检测并请求多个权限
     */
    fun checkAndRequestMorePermissions(
        context: Context,
        permissions: List<String>,
        requestCode: Int
    ) {
        val permissionList = checkMorePermissions(context, permissions)
        requestMorePermissions(context, permissionList, requestCode)
    }

    /**
     * 检测权限
     *
     * @describe：具体实现由回调接口决定
     */
    fun checkPermission(context: Context, permission: String, callBack: PermissionCheckCallBack) {
        if (checkPermission(context, permission)) { // 用户已授予权限
            callBack.onHasPermission()
        } else {
            if (judgePermission(context, permission)) // 用户之前已拒绝过权限申请
                callBack.onUserHasAlreadyTurnedDown(permission) else  // 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
                callBack.onUserHasAlreadyTurnedDownAndDontAsk(permission)
        }
    }

    /**
     * 检测多个权限
     *
     * @describe：具体实现由回调接口决定
     */
    fun checkMorePermissions(
        context: Context,
        permissions: List<String>,
        callBack: PermissionCheckCallBack
    ) {
        val permissionList = checkMorePermissions(context, permissions)
        if (permissionList.isEmpty()) {  // 用户已授予权限
            callBack.onHasPermission()
        } else {
            var isFirst = true
            for (i in permissionList.indices) {
                val permission = permissionList[i]
                if (judgePermission(context, permission)) {
                    isFirst = false
                    break
                }
            }
            val unauthorizedMorePermissions = permissionList.toTypedArray()
            if (isFirst) // 用户之前已拒绝过权限申请
                callBack.onUserHasAlreadyTurnedDownAndDontAsk(*unauthorizedMorePermissions) else  // 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
                callBack.onUserHasAlreadyTurnedDown(*unauthorizedMorePermissions)
        }
    }

    /**
     * 检测并申请权限
     */
    fun checkAndRequestPermission(
        context: Context,
        permission: String,
        requestCode: Int,
        callBack: PermissionRequestSuccessCallBack
    ) {
        if (checkPermission(context, permission)) { // 用户已授予权限
            callBack.onHasPermission()
        } else {
            requestPermission(context, permission, requestCode)
        }
    }

    /**
     * 检测并申请多个权限
     */
    fun checkAndRequestMorePermissions(
        context: Context,
        permissions: List<String>,
        requestCode: Int,
        callBack: PermissionRequestSuccessCallBack
    ) {
        val permissionList = checkMorePermissions(context, permissions)
        if (permissionList.isEmpty()) {  // 用户已授予权限
            callBack.onHasPermission()
        } else {
            requestMorePermissions(context, permissionList, requestCode)
        }
    }

    /**
     * 判断权限是否申请成功
     */
    fun isPermissionRequestSuccess(grantResults: IntArray): Boolean {
        return (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * 用户申请权限返回
     */
    fun onRequestPermissionResult(
        context: Context,
        permission: String,
        grantResults: IntArray,
        callback: PermissionCheckCallBack
    ) {
        if (isPermissionRequestSuccess(grantResults)) {
            callback.onHasPermission()
        } else {
            if (judgePermission(context, permission)) {
                callback.onUserHasAlreadyTurnedDown(permission)
            } else {
                callback.onUserHasAlreadyTurnedDownAndDontAsk(permission)
            }
        }
    }

    /**
     * 用户申请多个权限返回
     */
    fun onRequestMorePermissionsResult(
        context: Context,
        permissions: List<String>,
        callback: PermissionCheckCallBack
    ) {
        var isBannedPermission = false
        val permissionList = checkMorePermissions(context, permissions)
        if (permissionList.isEmpty()) callback.onHasPermission() else {
            for (i in permissionList.indices) {
                if (!judgePermission(context, permissionList[i])) {
                    isBannedPermission = true
                    break
                }
            }
            //　已禁止再次询问权限
            if (isBannedPermission) callback.onUserHasAlreadyTurnedDownAndDontAsk(*permissions.toTypedArray()) else  // 拒绝权限
                callback.onUserHasAlreadyTurnedDown(*permissions.toTypedArray())
        }
    }

    /**
     * 判断是否在白名单里
     *
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        var isIgnoring = false
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.packageName)
        }
        return isIgnoring
    }

    /**
     * 申请加入白名单
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun requestIgnoreBatteryOptimizations(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:" + context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 跳转到权限设置界面
     */
    fun toAppSetting(context: Context) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", context.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.action = Intent.ACTION_VIEW
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName())
        }
        context.startActivity(intent)
    }

    interface PermissionRequestSuccessCallBack {
        /**
         * 用户已授予权限
         */
        fun onHasPermission()
    }

    interface PermissionCheckCallBack {
        /**
         * 用户已授予权限
         */
        fun onHasPermission()

        /**
         * 用户已拒绝过权限
         *
         * @param permission:被拒绝的权限
         */
        fun onUserHasAlreadyTurnedDown(vararg permission: String?)

        /**
         * 用户已拒绝过并且已勾选不再询问选项、用户第一次申请权限;
         *
         * @param permission:被拒绝的权限
         */
        fun onUserHasAlreadyTurnedDownAndDontAsk(vararg permission: String?)
    }
}
