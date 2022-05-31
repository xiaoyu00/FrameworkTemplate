package com.framework.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import com.framework.base.common.GlobalData
import com.framework.base.component.face.core.FaceManager
import com.framework.base.data.AppDataBase
import com.framework.base.utils.SharedPreferencesUtil

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalData.init(this)
        SharedPreferencesUtil.init(applicationContext)
//        AppDataBase.initDataBase(applicationContext)
        FaceManager.loadFaceFiles()
    }
}