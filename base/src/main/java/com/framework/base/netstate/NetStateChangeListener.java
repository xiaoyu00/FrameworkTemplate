package com.framework.base.netstate;

public interface NetStateChangeListener {
    void onDisconnect();

    void onMobileConnect();

    void onWifiConnect();
}
