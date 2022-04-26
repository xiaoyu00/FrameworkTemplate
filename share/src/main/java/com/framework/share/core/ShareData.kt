package com.framework.share.core

import android.graphics.Bitmap
import android.text.TextUtils

/**
 * 分享内容实体
 */
class ShareData(
    var title: String? = null,
    var content: String? = null,
    var description: String? = null,
    var messageExt: String? = null,
    var type: ShareType = ShareType.TEXT,
    var imageUrl: String? = null,
    var imageBitmap: Bitmap? = null,
    var videoUrl: String? = null,
    var webPageUrl: String? = null,
    var filePath: String? = null,
    var musicUrl: String? = null,
    var appletPath: String? = null
) {

    class Builder {
        private var title: String? = null
        private var content: String? = null
        private var description: String? = null
        private var imageUrl: String? = null
        private var videoUrl: String? = null
        private var webPageUrl: String? = null
        private var filePath: String? = null
        private var musicUrl: String? = null
        private var appletPath: String? = null
        private var messageExt: String? = null
        var imageBitmap: Bitmap? = null
        private var type: ShareType = ShareType.TEXT

        /**
         * 分享用的标题
         *
         */
        fun title(title: String): Builder {
            this.title = title
            return this
        }

        /**
         * 分享用的内容
         *
         */
        fun content(content: String?): Builder {
            this.content = content
            return this
        }

        fun description(description: String?): Builder {
            this.description = description
            return this
        }

        /**
         * 分享用的图片链接
         */
        fun imageUrl(imageUrl: String?): Builder {
            if (TextUtils.isEmpty(imageUrl)) {
                this.imageUrl = "https://m-api.xcar.com.cn/static/xcaricon_12.png"
            } else {
                this.imageUrl = imageUrl
            }
            return this
        }

        fun videoUrl(videoUrl: String?): Builder {
            this.videoUrl = imageUrl
            return this
        }

        fun webPageUrl(webPageUrl: String?): Builder {
            this.webPageUrl = webPageUrl
            return this
        }

        fun filePath(filePath: String?): Builder {
            this.filePath = filePath
            return this
        }

        fun musicUrl(musicUrl: String?): Builder {
            this.musicUrl = musicUrl
            return this
        }

        fun appletPath(appletPath: String?): Builder {
            this.appletPath = appletPath
            return this
        }

        fun messageExt(messageExt: String?): Builder {
            this.messageExt = messageExt
            return this
        }

        fun type(type: ShareType?): Builder {
            if (type == null) {
                this.type = ShareType.TEXT
            } else {
                this.type = type
            }

            return this
        }

        fun imageBitmap(imageBitmap: Bitmap?): Builder {
            this.imageBitmap = imageBitmap
            return this
        }

        fun build(): ShareData {
            if (title.isNullOrEmpty()) {
                throw IllegalArgumentException("分享标题不能为空")
            }
            return ShareData(
                title,
                content,
                description,
                messageExt,
                type,
                imageUrl,
                imageBitmap,
                videoUrl,
                webPageUrl,
                filePath,
                musicUrl,
                appletPath
            )
        }
    }
}

