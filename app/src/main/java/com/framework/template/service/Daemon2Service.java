package com.framework.template.service;

import android.app.Notification;
import android.os.Build;

import androidx.annotation.NonNull;

import com.framework.base.component.service.WorkService;
import com.framework.template.R;

/**
 * 程序运行提示service（在通知栏一直显示）
 */
public class Daemon2Service extends WorkService {
    @NonNull
    @Override
    public Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getText(R.string.app_name))
                .setContentText(getResources().getText(R.string.app_name) + "正在后台运行");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(getChannelId());
        }
        Notification notification = builder.build();
        return notification;
    }

}