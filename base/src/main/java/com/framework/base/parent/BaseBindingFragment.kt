package com.framework.base.parent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Data binding Fragment 基类
 */
abstract class BaseBindingFragment<D : ViewDataBinding> : BaseFragment() {

    lateinit var dataBinding: D
    override fun initBase(inflater: LayoutInflater, container: ViewGroup?): View {
        dataBinding = DataBindingUtil.inflate(inflater, contextViewId(), container, false)
        return dataBinding.apply {
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        if (::dataBinding.isInitialized) {
//            dataBinding.unbind()
//        }
//    }
}