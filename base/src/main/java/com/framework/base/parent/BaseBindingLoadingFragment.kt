package com.framework.base.parent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Data binding Fragment 基类
 */
abstract class BaseBindingLoadingFragment<D : ViewDataBinding> : BaseLoadingFragment() {

    lateinit var dataBinding: D
    override fun initBase(inflater: LayoutInflater, container: ViewGroup?): View {
        dataBinding = DataBindingUtil.inflate(inflater, contextViewId(), container, false)
        initLoading(
            requireContext(),
            dataBinding.root
        ) {
            onLoadErrorClickListener()
        }
        dataBinding.lifecycleOwner = viewLifecycleOwner

        return getWorkView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::dataBinding.isInitialized) {
            dataBinding.unbind()
        }
    }
}