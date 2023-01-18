package com.framework.base.parent

import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.framework.base.parent.basics.BaseBindingFragment
import kotlin.reflect.KClass

/**
 * View Model Fragment 基类
 */
abstract class BaseViewModelFragment<M : ViewModel, D : ViewDataBinding> :
    BaseBindingFragment<D>() {
    val viewModel: M by fragmentViewModels()

    /**
     * view model 具体类型
     */
    abstract fun modelClass(): KClass<M>

    /**
     * 获取view model 标志
     *  true 表示从Fragment获取, false 表示从activity获取
     */
    open fun isOwner() = false

    /**
     * 创建ViewModel
     */
    @MainThread
    private fun fragmentViewModels(
        factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<M> {
        return if (isOwner()) {
            val ownerProducer: () -> ViewModelStoreOwner = { this }
            createViewModelLazy(modelClass(), { ownerProducer().viewModelStore }, factoryProducer)
        } else {
            createViewModelLazy(
                modelClass(), { requireActivity().viewModelStore },
                factoryProducer ?: { requireActivity().defaultViewModelProviderFactory }
            )
        }
    }
}

