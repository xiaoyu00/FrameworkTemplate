package com.framework.base.parent

import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KClass

/**
 * View Model Fragment 基类
 */
abstract class BaseViewModelLoadingFragment<M : ViewModel, D : ViewDataBinding> :
    BaseBindingLoadingFragment<D>() {
    val viewModel: M by viewModels()

    /**
     * view model 具体类型
     */
    abstract fun modelClass(): KClass<M>

    /**
     * 获取view model 标志
     *  true 表示从Fragment获取, false 表示从activity获取
     */
    open fun isOwner() = true

    /**
     * 创建ViewModel
     */
    @MainThread
    private fun viewModels(
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

