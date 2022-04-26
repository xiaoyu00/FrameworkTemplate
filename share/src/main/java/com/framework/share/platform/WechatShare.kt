package com.framework.share.platform

import android.content.Context
import com.framework.base.config.IS_DEBUG
import com.framework.base.config.WECHAT_APPID
import com.framework.base.config.WECHAT_APPLETID
import com.framework.base.config.WECHAT_OPENID
import com.framework.base.utils.ImageUtils
import com.framework.share.core.BaseShare
import com.framework.share.core.ShareCallBack
import com.framework.share.core.ShareData
import com.framework.share.core.ShareType
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * 微信分享
 */
class WechatShare : BaseShare() {
    private var mWxApi: IWXAPI? = null
    private var call: ShareCallBack? = null
    override fun init(context: Context) {
        //三个参数分别是上下文、应用的appId、是否检查签名（默认为false）
        mWxApi = WXAPIFactory.createWXAPI(context, WECHAT_APPID, true)
        // 注册
        mWxApi?.registerApp(WECHAT_APPID)
    }

    override fun checkShare(): Boolean? {
        val check = mWxApi?.isWXAppInstalled
        call?.onCheck(check == true)
        return check
    }

    private fun buildThumbImage(share: ShareData): ByteArray {
        if (!share.imageUrl.isNullOrEmpty()) {
            if (share.imageUrl?.startsWith("http://") == true || share.imageUrl?.startsWith("https://") == true) {
                ImageUtils.getBitmapForUrl(share.imageUrl!!)
            } else {
                return ImageUtils.getImageByteArrayForPath(share.imageUrl!!)
            }
        } else {
            share.imageBitmap?.let {
                return ImageUtils.bmpToByteArray(it, true)
            }
        }
        return byteArrayOf()
    }

    private fun buildShareData(share: ShareData): WXMediaMessage {
        val msg = WXMediaMessage()
        msg.mediaObject = when (share.type) {
            ShareType.TEXT -> buildShareText(share)
            ShareType.IMAGE -> buildShareImage(share)
            ShareType.VIDEO -> buildShareVideo(share)
            ShareType.PAGE -> buildSharePage(share)
            ShareType.APPLET -> buildShareApplet(share)
            ShareType.FILE -> buildShareFile(share)
            ShareType.MUSIC -> buildShareMusic(share)
            ShareType.MUSIC_VIDEO -> buildShareMusicVideo(share)
        }
        msg.title = share.title
        msg.thumbData = buildThumbImage(share)
        msg.description = share.description
        msg.messageExt = share.messageExt

        return msg
    }

    private fun buildShareText(share: ShareData): WXMediaMessage.IMediaObject {
        val textObj = WXTextObject()
        textObj.text = share.content
        return textObj
    }

    private fun buildShareImage(share: ShareData): WXMediaMessage.IMediaObject {
        return if (!share.imageUrl.isNullOrEmpty()) {
            val imgObj = WXImageObject()
            imgObj.imagePath = share.imageUrl
            imgObj
        } else {
            WXImageObject(buildThumbImage(share))
        }
    }

    private fun buildShareVideo(share: ShareData): WXMediaMessage.IMediaObject {
        //初始化一个WXVideoObject，填写url
        val video = WXVideoObject()
        video.videoUrl = share.videoUrl
        return video
    }

    private fun buildSharePage(share: ShareData): WXMediaMessage.IMediaObject {
        val webpage = WXWebpageObject()
        webpage.webpageUrl = share.webPageUrl
        return webpage
    }

    private fun buildShareApplet(share: ShareData): WXMediaMessage.IMediaObject {
        val miniProgramObj = WXMiniProgramObject()
        miniProgramObj.webpageUrl = share.webPageUrl // 兼容低版本的网页链接
        miniProgramObj.miniprogramType =
            if (IS_DEBUG) WXMiniProgramObject.MINIPROGRAM_TYPE_TEST else WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE // 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = WECHAT_APPLETID // 小程序原始id
        miniProgramObj.path = share.appletPath//小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"
        miniProgramObj.withShareTicket = true
        return miniProgramObj
    }

    private fun buildShareFile(share: ShareData): WXMediaMessage.IMediaObject {
        val fileObj = WXFileObject()
        fileObj.filePath = share.filePath
        return fileObj
    }

    private fun buildShareMusic(share: ShareData): WXMediaMessage.IMediaObject {
        val musicObj = WXMusicObject()
        musicObj.musicUrl = share.musicUrl
        return musicObj
    }

    private fun buildShareMusicVideo(share: ShareData): WXMediaMessage.IMediaObject {
        val musicVideo = WXMusicVideoObject()
        musicVideo.musicUrl = "https://www.qq.com" // 音乐url
        musicVideo.musicDataUrl = "http://xxx/xx.mp3" // 音乐音频url
        musicVideo.songLyric = "xxx" // 歌词
        musicVideo.hdAlbumThumbFilePath = "xxx" // 专辑图本地文件路径
        musicVideo.singerName = "xxx"
        musicVideo.albumName = "album_xxx"
        musicVideo.musicGenre = "流行歌曲"
        musicVideo.issueDate = 1610713585
        musicVideo.identification = "sample_identification"
        musicVideo.duration = 120000 // 单位为毫秒
        return musicVideo

    }

    private fun buildTransaction(string: String?): String {
        return if (string.isNullOrEmpty()) System.currentTimeMillis()
            .toString() else string + System.currentTimeMillis()
    }

    /**
     * 分享到会话 小程序目前只支持会话
     * SendMessageToWX.Req.WXSceneSession
     * 分享到朋友圈:
     * SendMessageToWX.Req.WXSceneTimeline;
     * 分享到收藏:
     * SendMessageToWX.Req.WXSceneFavorite
     */
    override fun share(mode: Int, share: ShareData, shareCallBack: ShareCallBack?) {

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction(share.type.name)
        req.message = buildShareData(share)
        req.scene = mode
        req.userOpenId = WECHAT_OPENID
        val shareBack = mWxApi?.sendReq(req)
        call?.onShare(shareBack == true)
    }
}