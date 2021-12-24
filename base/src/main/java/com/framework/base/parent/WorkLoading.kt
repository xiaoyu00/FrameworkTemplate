package com.framework.base.parent

import android.content.Context
import android.view.View
import com.framework.base.views.loading.BaseLoadingDialog
import com.framework.base.views.loading.BaseWorkView

interface WorkLoading {

    fun initLoading(context: Context,contentView: View, onLoadErrorClickListener: (() -> Unit)?)

    fun showUploadLoading(loadingTips: String)

    fun closeUploadLoading()

    fun createUploadLoadingDialog(context: Context): BaseLoadingDialog?

    fun createWorkLoadingView(
        context: Context,
        contentView: View,
        onLoadErrorClickListener: (() -> Unit)?
    ): BaseWorkView?

    fun createDefaultWorkLoadingView(
        context: Context,
        contentView: View,
        onLoadErrorClickListener: (() -> Unit)?
    ): BaseWorkView

    fun createDefaultLoadingDialog(context: Context): BaseLoadingDialog
}