package com.framework.base.parent

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.framework.base.parent.basics.BaseLoadingActivity

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