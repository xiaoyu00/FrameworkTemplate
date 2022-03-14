package com.framework.base

import android.app.Application
import com.framework.base.utils.SharedPreferencesUtil

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesUtil.init(applicationContext)
    }
}