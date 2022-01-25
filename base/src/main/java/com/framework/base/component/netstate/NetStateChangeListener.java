package com.framework.base.component.netstate;

public interface NetStateChangeListener {
    void onDisconnect();

    void onMobileConnect();

    void onWifiConnect();
}
