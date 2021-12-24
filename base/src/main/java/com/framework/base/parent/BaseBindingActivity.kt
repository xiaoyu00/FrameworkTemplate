package com.framework.base.parent

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Data binding Activity 基类
 */
abstract class BaseBindingActivity<D : ViewDataBinding> : BaseActivity() {

    lateinit var dataBinding: D
    override fun initBase() {
        dataBinding = DataBindingUtil.setContentView(
            this, contextViewId()
        )
        dataBinding.lifecycleOwner = this
    }

}