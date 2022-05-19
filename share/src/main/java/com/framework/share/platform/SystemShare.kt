package com.framework.share.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.framework.share.core.BaseShare
import com.framework.share.core.ShareCallBack
import com.framework.share.core.ShareData
import com.framework.share.core.ShareType
import java.io.File

/**
 * 系统分享
 */
class SystemShare : BaseShare() {
    override fun init(context: Context) {
    }

    override fun checkShare(): Boolean? {
        return true
    }

    /**
     * 系统分享只有俩种类型
     */
    private fun buildShareData(share: ShareData): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        when (share.type) {
            ShareType.TEXT -> {
                intent.putExtra(Intent.EXTRA_TEXT, share.content)
                intent.type = "text/plain"
            }
            ShareType.IMAGE -> {
                share.imageUrl?.let {
                    val uri: Uri = Uri.fromFile(File(it))
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.type = "image/*"
                }

            }
            ShareType.VIDEO -> {
                share.videoUrl?.let {
                    val uri: Uri = Uri.fromFile(File(it))
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.type = "**"
                }

            }
            ShareType.FILE -> {
                share.filePath?.let {
                    val uri: Uri = Uri.fromFile(File(it))
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.type = "**"
                }

            }
            ShareType.MUSIC, ShareType.MUSIC_VIDEO -> {
                share.musicUrl?.let {
                    val uri: Uri = Uri.fromFile(File(it))
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.type = "**"
                }

            }
            ShareType.PAGE -> {
                intent.putExtra(Intent.EXTRA_TEXT, share.webPageUrl)
                intent.type = "text/plain"
            }
            ShareType.APPLET -> {
                intent.putExtra(Intent.EXTRA_TEXT, share.appletPath)
                intent.type = "text/plain"
            }
        }

        return intent
    }

    override fun share(mode: Int, share: ShareData, shareCallBack: ShareCallBack?) {

    }

    fun shareSystem(context: Context, share: ShareData) {
        context.startActivity(Intent.createChooser(buildShareData(share), "分享到"));
    }
}