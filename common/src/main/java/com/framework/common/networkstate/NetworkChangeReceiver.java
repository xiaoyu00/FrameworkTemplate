package com.framework.common.networkstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;

import com.framework.base.utils.NetWorkStateUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private ConnectivityManager.NetworkCallback networkCallback;

    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            if(networkCallback!=null){
                NetWorkStateUtils.getNetConnectivity(context,networkCallback);
            }
        }
    }


    /**
     * 动态注册
     */
    public void registerReceiver(Context context,ConnectivityManager.NetworkCallback networkCallback) {
        this.networkCallback=networkCallback;
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, intentFilter);
    }

    /**
     * 动态注册
     */
    public void unRegisterReceiver(Context context) {
        context.unregisterReceiver(this);
        mediaPlayer.release();
        mediaPlayer = null;
    }

}
