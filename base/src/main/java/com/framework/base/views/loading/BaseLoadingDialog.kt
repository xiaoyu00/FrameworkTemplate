package com.framework.base.views.loading

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View

abstract class BaseLoadingDialog(content: Context) : Dialog(content) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLoadingView())
    }

    abstract fun getLoadingView(): View

    abstract fun show(loadingTips: String)

}
