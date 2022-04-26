package com.framework.share.core

import android.content.Context

abstract class BaseShare {
    abstract fun init(context: Context)
    abstract fun checkShare(): Boolean?
    abstract fun share(mode: Int, share: ShareData, shareCallBack: ShareCallBack? = null)
    open fun shareCheck(mode: Int, share: ShareData, shareCallBack: ShareCallBack? = null) {
        if (checkShare() == true) {
            share(mode, share, shareCallBack)
        }
    }

}