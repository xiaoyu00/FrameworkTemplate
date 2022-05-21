package com.framework.pay

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.alipay.sdk.app.PayTask
import com.framework.pay.`interface`.AliPlatformCall
import com.alipay.sdk.app.AuthTask

import com.framework.pay.*
import com.framework.pay.*


private const val ALI_TYPE_PAY = 1
private const val ALI_TYPE_AUTH = 2

object AliPlatform {
    private val TAG = AliPlatform::class.simpleName
    private var aliPlatformCall: AliPlatformCall? = null
    private val mHandler = Handler { msg ->
        msg.obj?.let {
            onHandle(msg.what, it as Map<*, *>?)
        }

        true
    }

    fun setPlatformCall(aliPlatformCall: AliPlatformCall) {
        this.aliPlatformCall = aliPlatformCall
    }

    /**
     * 已经安装过支付宝，会直接调用支付宝支付
     * 没有安装支付宝，会调起支付宝的H5页面支付
     */
    fun checkInstall(): Boolean {
        return true
    }

    /**
     * 支付
     * [orderInfo] 由服务器返回
     * app_id=2015052600090779&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id
     * %22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%
     * 22%2C%22total_amount%22%3A%220.02%22%2C%22subject%22%3A%221%22%2C%22body%22
     * %3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22
     * out_trade_no%22%3A%22314VYGIAGG7ZOYY%22%7D&charset=utf-8&method=
     * alipay.trade.app.pay&sign_type=RSA2&timestamp=2016-08-15%2012%3A12%3A15&version=1.0
     * &sign=MsbylYkCzlfYLy9PeRwUUIg9nZPeN9SfXPNavUCroGKR5Kqvx0nEnd3eRmKxJuthNUx4ERCXe552EV9PfwexqW%2B1wbKOdYtDIb4%2B7PL3Pc94RZL0zKaWcaY3tSL89%2FuAVUsQuFqEJdhIukuKygrXucvejOUgTCfoUdwTi7z%2BZzQ%3D
     */
    fun pay(activity: Activity, orderInfo: String) {
        val payRunnable = Runnable {
            val alipay = PayTask(activity)
            val result = alipay.payV2(orderInfo, true)
            val msg = Message()
            msg.what = ALI_TYPE_PAY
            msg.obj = result
            mHandler.sendMessage(msg)
        }
        // 必须异步调用
        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 登录
     * [authInfo] 由服务器返回
     * apiname=com.alipay.account.auth&app_id=xxxxx&app_name=mc&auth_type=
     * AUTHACCOUNT&biz_type=openservice&method=alipay.open.auth.sdk.code.get&pid=
     * xxxxx&product_id=APP_FAST_LOGIN&scope=kuaijie&sign_type=RSA2&target_id=
     * 20141225xxxx
     * &sign=fMcp4GtiM6rxSIeFnJCVePJKV43eXrUP86CQgiLhDHH2u%2FdN75eEvmywc2ulkm7qKRetkU9fbVZtJIqFdMJcJ9Yp%2BJI%2FF%2FpESafFR6rB2fRjiQQLGXvxmDGVMjPSxHxVtIqpZy5FDoKUSjQ2%2FILDKpu3%2F%2BtAtm2jRw1rUoMhgt0%3D
     */
    fun auth(activity: Activity, authInfo: String) {
        val authRunnable = Runnable {
            val authTask = AuthTask(activity)
            val result = authTask.authV2(authInfo, true)
            val msg = Message()
            msg.what = ALI_TYPE_AUTH
            msg.obj = result
            mHandler.sendMessage(msg)
        }
        // 必须异步调用
        val authThread = Thread(authRunnable)
        authThread.start()
    }

    /**
     * {
    "memo" : "xxxxx",
    "result" : "{
    \"alipay_trade_app_pay_response\":{
    \"code\":\"10000\",
    \"msg\":\"Success\",
    \"app_id\":\"2014072300007148\",
    \"out_trade_no\":\"081622560194853\",
    \"trade_no\":\"2016081621001004400236957647\",
    \"total_amount\":\"9.00\",
    \"seller_id\":\"2088702849871851\",
    \"charset\":\"utf-8\",
    \"timestamp\":\"2016-10-11 17:43:36\"
    },
    \"sign\":\"NGfStJf3i3ooWBuCDIQSumOpaGBcQz+aoAqyGh3W6EqA/gmyPYwLJ2REFijY9XPTApI9YglZyMw+ZMhd3kb0mh4RAXMrb6mekX4Zu8Nf6geOwIa9kLOnw0IMCjxi4abDIfXhxrXyj********\",
    \"sign_type\":\"RSA2\"
    }",
    "resultStatus" : "9000"
    }
     */
    private fun onHandle(type: Int, result: Map<*, *>?) {
        /**
         * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
         */
        if (result == null) {
            return
        }
        val resultInfo: String? = result["result"] as String? // 同步返回需要验证的信息
        val resultStatus: String? = result["resultStatus"] as String?
        if (TextUtils.equals(resultStatus, "9000")) {
            when (type) {
                ALI_TYPE_PAY -> aliPlatformCall?.onPaySuccess(resultInfo)
                ALI_TYPE_AUTH -> aliPlatformCall?.onAuthSuccess(resultInfo)
            }
        } else {
            Log.e(TAG, "操作失败")
            var code = -1
            resultStatus?.toInt()?.let {
                code = it
            }
            aliPlatformCall?.onError(code, "操作失败")
        }


    }
}