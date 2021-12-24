package com.framework.base.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences工具类
 */
object SharedPreferencesUtil {

    private const val FILE_NAME = "share_data"
    private lateinit var mPreferences: SharedPreferences

    fun init(context: Context) {
        mPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 存放数据
     * @param key 存放数据key
     * @param value 存放的数据
     */
    fun putData(key: String?, value: Any?) {
        mPreferences.edit().apply {
            when (value) {
                is String -> {
                    putString(key, value)
                }
                is Int -> {
                    putInt(key, value)
                }
                is Boolean -> {
                    putBoolean(key, value)
                }
                is Float -> {
                    putFloat(key, value)
                }
                is Long -> {
                    putLong(key, value)
                }
            }
            apply()
        }
    }

    /**
     * 获取数据
     * @param key 获取数据key
     * @param defaultValue 获取数据默认值
     */
    fun getData(key: String?, defaultValue: Any?): Any? {
        var value: Any? = null
        mPreferences.apply {
            when (defaultValue) {
                is String -> {
                    value = getString(key, defaultValue)
                }
                is Int -> {
                    value = getInt(key, defaultValue)
                }
                is Boolean -> {
                    value = getBoolean(key, defaultValue)
                }
                is Float -> {
                    value = getFloat(key, defaultValue)
                }
                is Long -> {
                    value = getLong(key, defaultValue)
                }
            }
        }
        return value
    }

    /**
     * 移除某个数据
     * @param key 移除数据的key
     */
    fun removeData(key: String?) {
        mPreferences.edit().run {
            remove(key)
            apply()
        }
    }

    /**
     * 清楚所有数据
     */
    fun clearData() {
        mPreferences.edit().run {
            clear()
            apply()
        }
    }
}