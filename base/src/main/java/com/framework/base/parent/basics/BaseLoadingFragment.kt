package com.framework.base.parent.basics

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.framework.base.views.loading.BaseLoadingDialog
import com.framework.base.views.loading.BaseWorkView
import com.framework.base.views.loading.DefaultLoadingDialog
import com.framework.base.views.loading.DefaultWorkView

/**
 * Fragment 基类
 */
abstract class BaseLoadingFragment : BaseFragment(), WorkLoading {

    private lateinit var loadingDialog: BaseLoadingDialog
    private lateinit var workView: BaseWorkView

    override fun initBase(inflater: LayoutInflater, container: ViewGroup?): View {
        val contentView = layoutInflater.inflate(contextViewId(), null)
        initLoading(requireContext(), contentView) {
            onLoadErrorClickListener()
        }
        return workView
    }

    override fun initLoading(
        context: Context,
        contentView: View,
        onLoadErrorClickListener: (() -> Unit)?
    ) {
        loadingDialog = createLoadingDialog(context) ?: createDefaultLoadingDialog(context)
        workView = createWorkLoadingView(context, contentView, onLoadErrorClickListener)
            ?: createDefaultWorkLoadingView(context, contentView, onLoadErrorClickListener)
    }

    open fun onLoadErrorClickListener() {

    }

    fun showWorkLoading() {
        workView.showLoading()
    }

    fun showWorkError() {
        workView.showError()
    }

    fun workFinish() {
        workView.workFinish()
    }

    fun getWorkView(): View = workView
    override fun showLoadingDialog(loadingTips: String) {
        loadingDialog.show(loadingTips)
    }

    override fun closeLoadingDialog() {
        loadingDialog.apply {
            if (isShowing) {
                dismiss()
            }
        }
    }

    override fun createLoadingDialog(context: Context): BaseLoadingDialog? {
        return null
    }

    override fun createWorkLoadingView(
        context: Context,
        contentView: View,
        onLoadErrorClickListener: (() -> Unit)?
    ): BaseWorkView? {
        return null
    }

    override fun createDefaultWorkLoadingView(
        context: Context,
        contentView: View,
        onLoadErrorClickListener: (() -> Unit)?
    ): BaseWorkView {
        return DefaultWorkView(context, contentView, onLoadErrorClickListener)
    }

    override fun createDefaultLoadingDialog(context: Context): BaseLoadingDialog {
        return DefaultLoadingDialog(context)
    }
}