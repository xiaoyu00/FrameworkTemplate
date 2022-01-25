package com.framework.base.component.netstate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.framework.base.utils.NetWorkStateUtils;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            int connectivityStatus = NetWorkStateUtils.getConnectivityStatus(context);
            notifyObservers(connectivityStatus);
        }
    }

    private void notifyObservers(int networkType){
    }

    /**
     * 动态注册
     */
    public void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, intentFilter);
    }
    /**
     * 动态注册
     */
    public void unRegisterReceiver(Context context) {
        context.unregisterReceiver(this);
    }
}
