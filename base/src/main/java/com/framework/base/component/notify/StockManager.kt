package com.framework.base.component.notify

/**
 * 里面做输出处理 如:计数器
 */
class StockManager(symbol: String) {

    private val listeners = mutableListOf<(Any) -> Unit>()

    fun requestDataUpdates(listener: (Any) -> Unit) {
        listeners.add(listener)
    }

    fun removeUpdates(listener: (Any) -> Unit) {
        listeners.remove(listener)
    }
}