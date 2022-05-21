package com.framework.pay

import android.content.Context
import android.util.Log
import com.framework.base.config.WECHAT_APPID
import com.framework.base.config.WECHAT_SIGN
import com.framework.pay.`interface`.WeChatPlatformCall
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbiz.*
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.util.*


object WeChatPlatform {
    private val TAG = WeChatPlatform::class.simpleName
    var wxApi: IWXAPI? = null
    private var weChatPlatformCall: WeChatPlatformCall? = null

    fun setPlatformCallBack(weChatPlatformCall: WeChatPlatformCall) {
        this.weChatPlatformCall = weChatPlatformCall
    }

    fun checkInstall(): Boolean {
        return wxApi?.isWXAppInstalled == true
    }

    fun init(context: Context) {
        wxApi = WXAPIFactory.createWXAPI(context, WECHAT_APPID)
        wxApi?.registerApp(WECHAT_APPID)
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

    /**
     * 获取微信访问Token
     */
    fun getToken() {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact"
        req.state = "none"
        wxApi?.sendReq(req)
    }

    /**
     * 订阅消息
     */
    fun subscribeMsg(scene: Int, templateID: String, reserved: String): Boolean {
        if (!isSubscribeMsgSupport()) {
            return false
        }
        val req = SubscribeMessage.Req()
        req.scene = scene
        req.templateID = templateID
        req.reserved = reserved

        return wxApi?.sendReq(req) == true
    }

    /**
     * 订阅小程序消息
     */
    fun subscribeMiniMsg(miniProgramAppId: String): Boolean {
        if (!isMiniSupport()) {
            return false
        }
        val req = SubscribeMiniProgramMsg.Req()
        req.miniProgramAppId = miniProgramAppId

        return wxApi?.sendReq(req) == true
    }

    /**
     * 拉起微信小程序
     * [userName] 小程序原始id
     * [path] 拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
     * [miniprogramType] 可选打开 开发版，体验版和正式版
     */
    fun launchMiniProgram(
        userName: String,
        path: String,
        miniprogramType: Int = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE
    ): Boolean {
        val req = WXLaunchMiniProgram.Req()
        req.userName = userName
        req.path = path
        req.miniprogramType = miniprogramType

        return wxApi?.sendReq(req) == true
    }

    /**
     * 打开微信客服
     * [url] 客服url
     * [corpId] 企业ID
     */
    fun openServiceChat(url: String, corpId: String): Boolean {
        if (!isServiceChatSupport()) {
            return false
        }
        val req = WXOpenCustomerServiceChat.Req()
        req.corpId = corpId
        req.url = url
        return wxApi?.sendReq(req) == true
    }

    /**
     * 开启支付分
     * [query] 查询数据 "mch_id=1230000109&service_id=88888888000011&
     *   out_request_no=1234323JKHDFE1243252&
     *   timestamp=1530097563&nonce_str=zyx53Nkey8o4bHpxTQvd8m7e92nG5mG2&
     *   sign_type=HMAC-SHA256&sign=029B52F67573D7E3BE74904BF9AEA";
     *   req.extInfo = "{\"miniProgramType\": 0}"
     */
    fun openBusinessView(query: String): Boolean {
        if (!isOpenBusinessViewSupport()) {
            return false
        }
        val req = WXOpenBusinessView.Req()
        req.businessType = "wxpayScoreEnable"
        req.query = query
        req.extInfo = "{\"miniProgramType\": 0}"
        return wxApi?.sendReq(req) == true
    }

    /**
     * 发起纯签约(如：自动续费)
     * [queryInfo] 预签约id queryInfo.put("pre_entrustweb_id","5778aadY9nltAsZzXixCkFIGYnVV1");
     */
    fun openBusinessWebView(queryInfo: HashMap<String, String>): Boolean {
        val req = WXOpenBusinessWebview.Req()
        req.businessType = 12//固定值
        req.queryInfo = queryInfo
        return wxApi?.sendReq(req) == true
    }

    /**
     * 支付
     * [prepayId] 预付id
     * [partnerId] 商户id
     * [nonceStr] 随机字符串 （由服务器返回）
     * [timeStamp]当前时间戳 （由服务器返回）
     */
    fun pay(partnerId: String, prepayId: String, nonceStr: String, timeStamp: String): Boolean {

        val request = PayReq()

        request.appId = WECHAT_APPID

        request.partnerId = partnerId

        request.prepayId = prepayId

        request.packageValue = "Sign=WXPay";

        request.nonceStr = nonceStr

        request.timeStamp = timeStamp
        request.sign = WECHAT_SIGN

        return wxApi?.sendReq(request) == true
    }

    /**
     * 是否支持开启支付分
     */
    private fun isOpenBusinessViewSupport(): Boolean {
        if (wxApi == null) {
            return false
        }
        return wxApi!!.wxAppSupportAPI >= Build.OPEN_BUSINESS_VIEW_SDK_INT
    }

    /**
     * 是否支持订阅消息
     */
    private fun isSubscribeMsgSupport(): Boolean {
        if (wxApi == null) {
            return false
        }
        return wxApi!!.wxAppSupportAPI >= Build.SUBSCRIBE_MESSAGE_SUPPORTED_SDK_INT
    }

    /**
     * 是否支持小程序订阅消息
     */
    private fun isMiniSupport(): Boolean {
        if (wxApi == null) {
            return false
        }
        return wxApi!!.wxAppSupportAPI >= Build.SUBSCRIBE_MINI_PROGRAM_MSG_SUPPORTED_SDK_INT
    }

    /**
     * 是否支持客服
     */
    private fun isServiceChatSupport(): Boolean {
        if (wxApi == null) {
            return false
        }
        return wxApi!!.wxAppSupportAPI >= Build.SUPPORT_OPEN_CUSTOMER_SERVICE_CHAT
    }

    fun onHandle(resp: BaseResp) {
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            when (resp.type) {
                ConstantsAPI.COMMAND_PAY_BY_WX -> onPayHandle(resp)
                ConstantsAPI.COMMAND_SENDAUTH -> onAuthHandle(resp)
                ConstantsAPI.COMMAND_SUBSCRIBE_MESSAGE -> onSubscribeMessageHandle(resp)
                ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM -> onLaunchMiniProgramHandle(resp)
                ConstantsAPI.COMMAND_OPEN_CUSTOMER_SERVICE_CHAT -> onServiceChatHandle(resp)
                ConstantsAPI.COMMAND_OPEN_BUSINESS_VIEW -> onOpenBusinessView(resp)
                ConstantsAPI.COMMAND_OPEN_BUSINESS_WEBVIEW -> onOpenBusinessWebView(resp)
            }
        } else {
            val msg: String
            when (resp.errCode) {
                BaseResp.ErrCode.ERR_USER_CANCEL -> {
                    msg = "用户取消该操作"
                    weChatPlatformCall?.onError(resp.errCode, msg)
                }
                BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                    msg = "被用户拒绝"
                    weChatPlatformCall?.onError(resp.errCode, msg)
                }
                BaseResp.ErrCode.ERR_UNSUPPORT -> {
                    msg = "不支持该功能"
                    weChatPlatformCall?.onError(resp.errCode, msg)
                }
                else -> {
                    msg = "未知错误"
                    weChatPlatformCall?.onError(resp.errCode, msg)
                }
            }
            Log.e(TAG, msg)
        }


    }

    fun onReqHandle(req: BaseReq) {
        if (req.type == ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX) {
            onShowMessageFromWx(req)
        }
    }

    private fun onPayHandle(resp: BaseResp) {
        val msg: String
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                msg = "支付成功"
                weChatPlatformCall?.onPaySuccess(resp)
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                msg = "用户取消支付"
                weChatPlatformCall?.onError(resp.errCode, msg)
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                msg = "支付被拒绝"
                weChatPlatformCall?.onError(resp.errCode, msg)
            }
            BaseResp.ErrCode.ERR_UNSUPPORT -> {
                msg = "不支持支付"
                weChatPlatformCall?.onError(resp.errCode, msg)
            }
            else -> {
                msg = "未知错误"
                weChatPlatformCall?.onError(resp.errCode, msg)
            }
        }
        Log.e(TAG, msg)
    }

    /**
     * 微信登录，获取token 回调
     */
    private fun onAuthHandle(resp: BaseResp) {
        val authResp = resp as SendAuth.Resp
        weChatPlatformCall?.onAuthSuccess(authResp)
    }

    private fun onSubscribeMessageHandle(resp: BaseResp) {
        val subscribeMsgResp = resp as SubscribeMessage.Resp
        weChatPlatformCall?.onSubscribeMessageSuccess(subscribeMsgResp)
    }

    private fun onLaunchMiniProgramHandle(resp: BaseResp) {
        val launchMiniProgramResp = resp as WXLaunchMiniProgram.Resp
        weChatPlatformCall?.onLaunchMiniProgramSuccess(launchMiniProgramResp)
    }

    private fun onServiceChatHandle(resp: BaseResp) {
        val serviceChatResp = resp as WXOpenCustomerServiceChat.Resp
        weChatPlatformCall?.onServiceChatSuccess(serviceChatResp)
    }

    private fun onOpenBusinessView(resp: BaseResp) {
        val openBusinessViewResp = resp as WXOpenBusinessView.Resp
        weChatPlatformCall?.onOpenBusinessViewSuccess(openBusinessViewResp)
    }

    /**
     * 签约回调
     */
    private fun onOpenBusinessWebView(resp: BaseResp) {
        val openBusinessWebViewResp = resp as WXOpenBusinessWebview.Resp
        weChatPlatformCall?.onOpenBusinessWebViewSuccess(openBusinessWebViewResp)
    }

    private fun onShowMessageFromWx(req: BaseReq) {
        val showReq = req as ShowMessageFromWX.Req
        val wxMsg = showReq.message
        val obj = wxMsg.mediaObject as WXAppExtendObject
        weChatPlatformCall?.onShowMessageFromWxSuccess(obj)
    }
}