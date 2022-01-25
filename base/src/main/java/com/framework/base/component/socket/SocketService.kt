package com.framework.base.component.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * 处理其它操作
 */
class SocketService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}