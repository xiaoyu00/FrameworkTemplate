package com.framework.template.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;

import androidx.annotation.Nullable;

import com.framework.template.R;

/**
 * 程序运行提示service（在通知栏一直显示）
 */
public class DaemonService extends Service {
    private String TAG = DaemonService.class.getSimpleName();
    private long RECEIVER_INTERVAL = 5000L;
    private String notificationId = "serviceid";

    public Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getText(R.string.app_name))
                .setContentText(getResources().getText(R.string.app_name) + "正在后台运行");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId);
        }
        Notification notification = builder.build();
        return notification;
    }


    // 启动notification的id，两次启动应是同一个id
    private int NOTIFICATION_ID = Process.myPid();

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //数字是随便写的“40”，
            nm.createNotificationChannel(
                    new NotificationChannel(
                            "WorkService_id",
                            "WorkService",
                            NotificationManager.IMPORTANCE_DEFAULT
                    )
            );
        }
        startForeground(NOTIFICATION_ID, getNotification());
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 如果Service被终止
        // 当资源允许情况下，重启service
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}