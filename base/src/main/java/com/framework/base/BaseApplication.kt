package com.framework.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import com.framework.base.data.AppDataBase
import com.framework.base.utils.SharedPreferencesUtil

class BaseApplication : Application() {
//    companion object {
//        private lateinit var context: Context
//        public fun getAppContext(): Context {
//            return context
//        }
//    }
    override fun onCreate() {
        super.onCreate()
//        context = applicationContext
        SharedPreferencesUtil.init(applicationContext)
        AppDataBase.initDataBase(applicationContext)
    }
}