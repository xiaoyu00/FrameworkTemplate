package com.framework.base.parent.basics

import android.content.Context
import android.view.View
import com.framework.base.views.loading.BaseLoadingDialog
import com.framework.base.views.loading.BaseWorkView

interface WorkLoading {

    fun initLoading(context: Context,contentView: View, onLoadErrorClickListener: (() -> Unit)?)

    fun showLoadingDialog(loadingTips: String)

    fun closeLoadingDialog()

    fun createLoadingDialog(context: Context): BaseLoadingDialog?

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