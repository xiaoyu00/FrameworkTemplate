package com.framework.share

import android.content.Context
import com.framework.share.core.ShareCallBack
import com.framework.share.core.ShareData
import com.framework.share.platform.SystemShare
import com.framework.share.platform.WechatShare

object ShareManager {
    private val wechatShare = WechatShare()
    private val systemShare = SystemShare()
    fun init(context: Context) {
        wechatShare.init(context)
    }

    /**
     * 微信分享
     * 分享到会话 小程序目前只支持会话
     * SendMessageToWX.Req.WXSceneSession
     * 分享到朋友圈:
     * SendMessageToWX.Req.WXSceneTimeline;
     * 分享到收藏:
     * SendMessageToWX.Req.WXSceneFavorite
     */
    fun shareToWeChat(mode: Int, shareData: ShareData, shareCallBack: ShareCallBack?) {
        wechatShare.shareCheck(mode, shareData, shareCallBack)
    }

    /**
     * 微博分享
     */
    fun shareToWebo(shareData: ShareData, shareCallBack: ShareCallBack?) {

    }

    /**
     * 系统分享
     */
    fun shareToSystem(context: Context, shareData: ShareData) {
        systemShare.shareSystem(context, shareData)
    }

    /**
     * 第三方sdk分享
     */
    fun shareToSdk() {

    }

    fun showSharePop(context: Context) {

    }
}