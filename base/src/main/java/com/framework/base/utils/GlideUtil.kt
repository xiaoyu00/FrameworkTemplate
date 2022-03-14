package com.framework.base.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition

object GlideUtil {

    fun loadImage(
        context: Context?,
        imageView: ImageView,
        url: String
    ) {
        Glide.with(context!!)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.image_land_test)
            .skipMemoryCache(false)
            .apply(options)
            .dontAnimate()
            .into(imageView)
    }

}