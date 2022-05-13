package com.framework.share

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import com.framework.share.core.ShareCallBack
import com.framework.share.core.ShareData
import com.framework.share.databinding.LayoutSharePopBinding
import com.framework.share.platform.SystemShare
import com.framework.share.platform.WechatShare
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX

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

    fun showSharePop(context: Context, shareData: ShareData) {
        val bDialog = BottomSheetDialog(context)
        val binding = LayoutSharePopBinding.inflate(LayoutInflater.from(context))
        binding.imageCancel.setOnClickListener {
            if (bDialog.isShowing) {
                bDialog.dismiss()
            }
        }
        binding.weichatFriends.setOnClickListener {
            wechatShare.shareCheck(SendMessageToWX.Req.WXSceneTimeline, shareData)
        }
        binding.weichat.setOnClickListener {
//            wechatShare.shareCheck()
        }
        binding.qqZone.setOnClickListener {

        }
        binding.qqFriends.setOnClickListener {

        }
        bDialog.setContentView(binding.root)
        bDialog.show()
    }
}