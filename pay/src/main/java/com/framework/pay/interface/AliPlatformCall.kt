package com.framework.pay.`interface`

interface AliPlatformCall {
    fun onPaySuccess(result: String) {}
    fun onAuthSuccess(result: String) {}
    fun onError(errCode: Int, msg: String) {}
}