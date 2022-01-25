package com.framework.base.component.notify

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData

class StockLiveData(symbol: String) : LiveData<Any>() {
    private val stockManager: StockManager = StockManager(symbol)

    private val listener = { data: Any ->
        value = data
    }

    override fun onActive() {
        stockManager.requestDataUpdates(listener)
    }

    override fun onInactive() {
        stockManager.removeUpdates(listener)
    }

    companion object {
        private lateinit var sInstance: StockLiveData

        @MainThread
        fun get(symbol: String): StockLiveData {
            sInstance = if (Companion::sInstance.isInitialized) sInstance else StockLiveData(symbol)
            return sInstance
        }
    }
}