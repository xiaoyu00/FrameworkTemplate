package com.framework.base.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.framework.base.R

/**
 * glide工具类
 */
object GlideUtil {

    fun loadImage(
        context: Context?,
        imageView: ImageView,
        url: String
    ) {
        Glide.with(context!!)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.ic_launcher)
            .skipMemoryCache(false)
            .dontAnimate()
            .into(imageView)
    }

}