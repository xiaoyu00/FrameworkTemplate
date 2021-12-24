package com.framework.base.parent

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Data binding Activity 基类
 */
abstract class BaseBindingLoadingActivity<D : ViewDataBinding> : BaseLoadingActivity() {

    lateinit var dataBinding: D
    override fun initBase() {
        dataBinding = DataBindingUtil.inflate(layoutInflater, contextViewId(), null, false)
        initLoading(
            this,
            dataBinding.root
        ) {
            onLoadErrorClickListener()
        }
        dataBinding.lifecycleOwner = this
    }
}