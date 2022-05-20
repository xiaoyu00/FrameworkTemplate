package com.framework.pay

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.alipay.sdk.app.PayTask
import com.framework.pay.`interface`.AliPlatformCall

private const val ALI_TYPE_PAY = 1

class AliPlatform {
    private val TAG = AliPlatform::class.simpleName
    private var aliPlatformCall: AliPlatformCall? = null

    fun setPlatformCall(aliPlatformCall: AliPlatformCall) {
        this.aliPlatformCall = aliPlatformCall
    }

    fun checkInstall(): Boolean {
        return true
    }

    fun init(context: Context) {
    }

    fun pay(activity: Activity, orderInfo: String) {
        val alipay = PayTask(activity)
        val result = alipay.payV2(orderInfo, true)
        onHandle(ALI_TYPE_PAY, result)
    }

    private fun onHandle(type: Int, result: Map<String, String>) {
        /**
         * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
         */
        val resultInfo: String? = result["result"] // 同步返回需要验证的信息
        val resultStatus: String? = result["resultStatus"]
        // 判断resultStatus 为9000则代表支付成功
        if (TextUtils.equals(resultStatus, "9000")) {
            Log.e(TAG, "支付成功")
            payCall?.onSuccess()
        } else {
            Log.e(TAG, "支付失败")
            var code = -1
            resultStatus?.toInt()?.let {
                code = it
            }
            payCall?.onError(code, "支付失败")
        }
        when (type) {
            ALI_TYPE_PAY ->
        }

    }
}