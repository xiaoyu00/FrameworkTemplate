package com.framework.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import com.framework.base.common.GlobalData
import com.framework.base.component.face.core.FaceManager
import com.framework.base.data.AppDataBase
import com.framework.base.utils.SharedPreferencesUtil

open class BaseApplication : Application() , ViewModelStoreOwner {
    companion object {
        lateinit var baseApplication: BaseApplication
        lateinit var vmOwnerContext: ViewModelStoreOwner
        public fun getAppContext(): Context {
            return baseApplication
        }

        fun getAppVMContext(): ViewModelStoreOwner {
            return vmOwnerContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        baseApplication = this
        vmOwnerContext = this
        GlobalData.init(this)
        SharedPreferencesUtil.init(applicationContext)
//        AppDataBase.initDataBase(applicationContext)
        FaceManager.loadFaceFiles()

    }
}