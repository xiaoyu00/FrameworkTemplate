package com.framework.pay.`interface`

import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelbiz.*
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject

interface WeChatPlatformCall {
    fun onPaySuccess(resp: BaseResp){}
    fun onAuthSuccess(authResp: SendAuth.Resp){}
    fun onSubscribeMessageSuccess(subscribeMsgResp: SubscribeMessage.Resp){}
    fun onLaunchMiniProgramSuccess(launchMiniProgramResp: WXLaunchMiniProgram.Resp){}
    fun onServiceChatSuccess(serviceChatResp: WXOpenCustomerServiceChat.Resp){}
    fun onOpenBusinessViewSuccess(openBusinessViewResp: WXOpenBusinessView.Resp){}
    fun onOpenBusinessWebViewSuccess(openBusinessWebViewResp: WXOpenBusinessWebview.Resp){}
    fun onShowMessageFromWxSuccess(obj: WXAppExtendObject){}
    fun onError(errCode: Int, msg: String) {}
}