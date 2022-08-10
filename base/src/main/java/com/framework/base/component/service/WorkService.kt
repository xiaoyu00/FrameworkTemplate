package com.framework.base.component.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Process
import android.util.Log

/**
 * 处理其它操作
 */
abstract class WorkService : Service() {
    private val TAG: String = WorkService::class.java.simpleName
    private val RECEIVER_INTERVAL = 5000L

    // 启动notification的id，两次启动应是同一个id
    private val NOTIFICATION_ID = Process.myPid()
    private val mRunnable = Runnable {
        while (true) {
            Log.e(TAG + 1111, "" + System.currentTimeMillis());
            // 做一些事情 例如 定位
            try {
                Thread.sleep(RECEIVER_INTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace();
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            //数字是随便写的“40”，
            nm.createNotificationChannel(
                NotificationChannel(
                    "WorkService_id",
                    "WorkService",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        startForeground(NOTIFICATION_ID, getNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 测试线程，判断Service是否在工作
        //Thread(mRunnable).start()

        // 如果Service被终止
        // 当资源允许情况下，重启service
        return START_STICKY
    }

    abstract fun getNotification(): Notification

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }
}