package com.framework.base.views.loading

import android.content.Context
import android.view.View
import android.widget.RelativeLayout

abstract class BaseWorkView(
    context: Context,
    contentView: View,
    onLoadErrorClickListener: (() -> Unit)?
) :
    RelativeLayout(context) {
    private var loadingErrorView: View? = null
    private var loadingView: View? = null
    private var contentView: View? = null
    private var layoutParams =
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            addRule(CENTER_IN_PARENT)
        }
    private var contentLayoutParams =
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    init {
        this.contentView = contentView
        initViews(onLoadErrorClickListener)
    }

    fun showLoading() {
        loadingView?.visibility = View.VISIBLE
        loadingErrorView?.visibility = View.GONE
        contentView?.visibility = View.GONE
    }

    fun showError() {
        loadingView?.visibility = View.GONE
        loadingErrorView?.visibility = View.VISIBLE
        contentView?.visibility = View.GONE
    }

    fun workFinish() {
        loadingView?.visibility = View.GONE
        loadingErrorView?.visibility = View.GONE
        contentView?.visibility = View.VISIBLE
    }

    private fun initViews(onLoadErrorClickListener: (() -> Unit)?) {
        this.contentView?.let {
            this.addView(it, contentLayoutParams)
        }
        loadingErrorView = getLoadingErrorView()
        loadingErrorView?.let {
            it.visibility = View.GONE
            it.setOnClickListener {
                onLoadErrorClickListener?.invoke()
            }
            this.addView(it, layoutParams)
        }
        loadingView = getLoadingView()
        loadingView?.let {
            it.visibility = View.GONE
            this.addView(it, layoutParams)
        }

    }

    abstract fun getLoadingView(): View?
    abstract fun getLoadingErrorView(): View?
}
