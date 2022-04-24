package com.framework.base.parent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.framework.base.parent.basics.BaseLoadingFragment

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
}