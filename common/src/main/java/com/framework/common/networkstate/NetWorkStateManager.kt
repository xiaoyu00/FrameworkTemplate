package com.framework.common.networkstate

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.Network
import androidx.lifecycle.MutableLiveData
import com.framework.base.utils.NetWorkStateUtils

object NetWorkStateManager {
    val netState= MutableLiveData<Int>(NetWorkStateUtils.TYPE_STATE_AVAILABLE)
    private val networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver()

    fun startMonitor(context: Context) {
        networkChangeReceiver.registerReceiver(context,object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                netState.postValue(NetWorkStateUtils.TYPE_STATE_AVAILABLE)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                netState.postValue(NetWorkStateUtils.TYPE_STATE_UNAVAILABLE)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                netState.postValue(NetWorkStateUtils.TYPE_STATE_LOST)
            }
        })
    }

    fun stopMonitor(context: Context) {
        networkChangeReceiver.unRegisterReceiver(context)
    }
}