package com.framework.base.utils

import android.os.Build
import android.text.TextUtils
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * @author yu
 * @version
 * @date 2021/4/26
 * @description 手机型号
 */
object RomUtils {
    private const val TAG = "Rom"
    const val ROM_MIUI = "MIUI"
    const val ROM_EMUI = "EMUI"
    const val ROM_FLYME = "FLYME"
    const val ROM_OPPO = "OPPO"
    const val ROM_SMARTISAN = "SMARTISAN"
    const val ROM_VIVO = "VIVO"
    const val ROM_QIKU = "QIKU"
    private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    private const val KEY_VERSION_EMUI = "ro.build.version.emui"
    private const val KEY_VERSION_OPPO = "ro.build.version.opporom"
    private const val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
    private const val KEY_VERSION_VIVO = "ro.vivo.os.version"
    private var sName: String? = null
    private var sVersion: String? = null
    private var BRAND = "" //Build.BRAND;
    private var MANUFACTURER = "" //Build.MANUFACTURER;


    //华为
    fun isEmui(): Boolean {
        return check(ROM_EMUI)
    }

    //小米
    fun isMiui(): Boolean {
        return check(ROM_MIUI)
    }

    //vivo
    fun isVivo(): Boolean {
        return check(ROM_VIVO)
    }

    //oppo
    fun isOppo(): Boolean {
        return check(ROM_OPPO)
    }

    //魅族
    fun isFlyme(): Boolean {
        return check(ROM_FLYME)
    }

    //360手机
    fun is360(): Boolean {
        return check(ROM_QIKU) || check(
            "360"
        )
    }

    fun isSmartisan(): Boolean {
        return check(ROM_SMARTISAN)
    }

    fun getName(): String? {
        if (sName == null) {
            check("")
        }
        return sName
    }

    fun getVersion(): String? {
        if (sVersion == null) {
            check("")
        }
        return sVersion
    }

    fun check(rom: String): Boolean {
        if (sName != null) {
            return sName == rom
        }
        if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_MIUI)
                    .also { sVersion = it }
            )
        ) {
            sName =
                ROM_MIUI
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_EMUI)
                    .also { sVersion = it }
            )
        ) {
            sName =
                ROM_EMUI
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_OPPO)
                    .also { sVersion = it }
            )
        ) {
            sName =
                ROM_OPPO
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_VIVO)
                    .also { sVersion = it }
            )
        ) {
            sName =
                ROM_VIVO
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_SMARTISAN)
                    .also { sVersion = it }
            )
        ) {
            sName =
                ROM_SMARTISAN
        } else {
            sVersion = Build.DISPLAY
            if (sVersion!!.toUpperCase().contains(
                    ROM_FLYME
                )
            ) {
                sName =
                    ROM_FLYME
            } else {
                sVersion = Build.UNKNOWN
                sName = Build.MANUFACTURER.toUpperCase()
            }
        }
        return sName == rom
    }

    fun getProp(name: String): String? {
        var line: String? = null
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            Log.e(TAG, "Unable to read prop $name", ex)
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }

    /**
     * 判断是否为小米设备
     */
    fun isBrandXiaoMi(): Boolean {
        return ("xiaomi".equals(
            getBrand(),
            ignoreCase = true
        )
                || "xiaomi".equals(
            getManufacturer(),
            ignoreCase = true
        ))
    }

    /**
     * 判断是否为华为设备
     */
    fun isBrandHuawei(): Boolean {
        return "huawei".equals(
            getBrand(),
            ignoreCase = true
        ) ||
                "huawei".equals(
                    getManufacturer(),
                    ignoreCase = true
                ) ||
                "honor".equals(
                    getBrand(),
                    ignoreCase = true
                ) ||
                "honor".equals(
                    getManufacturer(),
                    ignoreCase = true
                )
    }

    /**
     * 判断是否为魅族设备
     */
    fun isBrandMeizu(): Boolean {
        return ("meizu".equals(
            getBrand(),
            ignoreCase = true
        )
                || "meizu".equals(
            getManufacturer(),
            ignoreCase = true
        )
                || "22c4185e".equals(
            getBrand(),
            ignoreCase = true
        ))
    }

    /**
     * 判断是否是 oppo 设备, 包含子品牌
     *
     * @return
     */
    fun isBrandOppo(): Boolean {
        return "oppo".equals(
            getBrand(),
            ignoreCase = true
        ) ||
                "realme".equals(
                    getBrand(),
                    ignoreCase = true
                ) ||
                "oneplus".equals(
                    getBrand(),
                    ignoreCase = true
                ) ||
                "oppo".equals(
                    getManufacturer(),
                    ignoreCase = true
                ) ||
                "realme".equals(
                    getManufacturer(),
                    ignoreCase = true
                ) ||
                "oneplus".equals(
                    getManufacturer(),
                    ignoreCase = true
                )
    }

    /**
     * 判断是否是vivo设备
     *
     * @return
     */
    fun isBrandVivo(): Boolean {
        return ("vivo".equals(
            getBrand(),
            ignoreCase = true
        )
                || "vivo".equals(
            getManufacturer(),
            ignoreCase = true
        ))
    }

//    /**
//     * 判断是否支持谷歌服务
//     *
//     * @return
//     */
//    fun isGoogleServiceSupport(): Boolean {
//        val googleApiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()
//        val resultCode: Int =
//            googleApiAvailability.isGooglePlayServicesAvailable(TUIOfflinePushService.appContext)
//        return resultCode == ConnectionResult.SUCCESS
//    }
private fun getBrand(): String {
        if (BRAND.isEmpty()) {
            synchronized(RomUtils::class.java) {
                if (BRAND.isEmpty()) {
                    BRAND = Build.BRAND
                    Log.i(
                        TAG,
                        "get BRAND by Build.BRAND :$BRAND"
                    )
                }
            }
        }
        return BRAND
    }
    private fun getManufacturer(): String {
        if (MANUFACTURER.isEmpty()) {
            synchronized(RomUtils::class.java) {
                if (MANUFACTURER.isEmpty()) {
                    MANUFACTURER =
                        Build.MANUFACTURER
                    Log.i(
                        TAG,
                        "get MANUFACTURER by Build.MANUFACTURER :$MANUFACTURER"
                    )
                }
            }
        }
        return MANUFACTURER
    }
}