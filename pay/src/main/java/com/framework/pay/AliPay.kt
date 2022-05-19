package com.framework.pay

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.alipay.sdk.app.PayTask


class AliPay {
    private val TAG = AliPay::class.simpleName

    fun checkInstall(): Boolean {
        return true
    }

    fun init(context: Context) {
    }

    fun pay(activity: Activity, orderInfo: String, payCall: PayCall?) {
        val alipay = PayTask(activity)
        val result = alipay.payV2(orderInfo, true)
        onHandle(result, payCall)
    }

    private fun onHandle(result: Map<String, String>, payCall: PayCall?) {
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

    }
}