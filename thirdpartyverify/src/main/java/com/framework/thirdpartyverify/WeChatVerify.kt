package com.framework.thirdpartyverify

import android.content.Context
import android.util.Log
import com.framework.base.config.WECHAT_APPID
import com.framework.base.utils.StringUtils
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.net.URLEncoder

/**
 * 微信验证
 */
class WeChatVerify {
    private val TAG=WeChatVerify::class.simpleName
    private var wxApi: IWXAPI? = null
    fun checkInstall(): Boolean {
        return wxApi?.isWXAppInstalled == true
    }

    fun init(context: Context) {
        wxApi = WXAPIFactory.createWXAPI(context, WECHAT_APPID)
    }

    fun openWeChat() {
        wxApi?.openWXApp()
    }

    fun login() {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state =
            "wechat_sdk_demo_test"//必要时使用简单加密串 URLEncoder.encode(StringUtils.getRandomString(8),"UTF-8")
        wxApi?.sendReq(req)
    }

    fun getToken(){
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact"
        req.state = "none"
        wxApi?.sendReq(req)
    }

}