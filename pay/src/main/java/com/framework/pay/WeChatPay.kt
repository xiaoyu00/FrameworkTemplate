package com.framework.pay

import android.content.Context
import android.util.Log
import com.framework.base.config.WECHAT_APPID
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory


class WeChatPay {
    private val TAG = WeChatPay::class.simpleName
    var wxApi: IWXAPI? = null

    fun checkInstall(): Boolean {
        return wxApi?.isWXAppInstalled == true
    }

    fun init(context: Context) {
        wxApi = WXAPIFactory.createWXAPI(context, WECHAT_APPID)
    }

    fun pay(payReq: PayReq): Boolean {
        return wxApi?.sendReq(payReq) == true
    }

    fun onHandle(resp: BaseResp, payCall: PayCall?) {
        val msg: String
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                msg = "支付成功"
                payCall?.onSuccess()
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                msg = "用户取消支付"
                payCall?.onError(resp.errCode, msg)
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                msg = "支付被拒绝"
                payCall?.onError(resp.errCode, msg)
            }
            BaseResp.ErrCode.ERR_UNSUPPORT -> {
                msg = "不支持支付"
                payCall?.onError(resp.errCode, msg)
            }
            else -> {
                msg = "未知错误"
                payCall?.onError(resp.errCode, msg)
            }
        }
        Log.e(TAG, msg)
    }
}