package com.framework.pay

import android.app.Activity
import android.content.Context
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq

const val PAY_TYPE_WECHAT = 1
const val PAY_TYPE_ALI = 2

object PayManager {
    private val weChatManger = WeChatPay()
    private val aliPay = AliPay()
    private var payCall: PayCall? = null
    fun init(context: Context) {
        weChatManger.init(context)
        aliPay.init(context)
    }

    fun checkWeChatInstall(): Boolean {
        return weChatManger.checkInstall()
    }

    fun setPayCallBack(payCall: PayCall) {
        this.payCall = payCall
    }

    fun onWeChatHandle(resp: BaseResp) {
        weChatManger.onHandle(resp, payCall)
    }

    fun payForWeChat(payReq: PayReq) {
        if (weChatManger.checkInstall()) {
            weChatManger.pay(payReq)
        }
    }

    fun payForAli(activity: Activity, orderInfo: String) {
        aliPay.pay(activity, orderInfo, payCall)
    }
}