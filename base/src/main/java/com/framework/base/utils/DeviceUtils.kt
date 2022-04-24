package com.framework.base.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.os.Build
import android.os.PowerManager
import android.telephony.TelephonyManager
import java.util.*
import android.content.Context.TELEPHONY_SERVICE

import androidx.core.content.ContextCompat.getSystemService


/**
 * @author yu
 * @version 1.0
 * @date 2021/4/15
 * @description
 */
object DeviceUtil {
    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    fun getSystemLanguageList(): Array<Locale> {
        return Locale.getAvailableLocales()
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    fun getSystemVersion(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    fun getSystemModel(): String {
        return Build.MODEL
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    fun getDeviceBrand(): String {
        return Build.BRAND
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    fun getIMEI(ctx: Context): String? {
        val tm = ctx.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tm.meid
        } else {
            tm.deviceId
        }
    }

    fun isAppRunningForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessInfos = activityManager.runningAppProcesses ?: return false
        val packageName = context.packageName
        for (appProcessInfo in runningAppProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                && appProcessInfo.processName == packageName
            ) {
                return true
            }
        }
        return false
    }

    /**
     * 唤醒手机屏幕并解锁
     */
    fun wakeUpAndUnlock(context: Context) {
        //获取电源管理器对象
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        if (!pm.isInteractive) {
            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            val wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                "BaseApplication:bright"
            )

            //点亮屏幕
            wl.acquire(30 * 1000L)
            wl.release()
        }
        //得到键盘锁管理器对象
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val kl = km.newKeyguardLock("unLock")

        //解锁
        kl.disableKeyguard()
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String? {
        val deviceId: String?
        val telephonyMgr = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager?
        deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (telephonyMgr?.imei == null) {
                telephonyMgr?.meid
            } else {
                telephonyMgr.imei
            }

        } else {
            telephonyMgr?.deviceId
        }
        return deviceId
    }

}