package com.framework.base.views.loading

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.framework.base.R

class DefaultWorkView(
    context: Context,
    contentView: View,
    onLoadErrorClickListener: (() -> Unit)?
) : BaseWorkView(context, contentView, onLoadErrorClickListener) {
    override fun getLoadingView(): View? {
        return LayoutInflater.from(context).inflate(R.layout.layout_loading_view, null)
    }

    override fun getLoadingErrorView(): View? {
        return LayoutInflater.from(context).inflate(R.layout.layout_error_loading_view, null)
    }
}