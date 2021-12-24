package com.framework.base.parent

import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

/**
 * View Model Activity 基类
 */
abstract class BaseViewModelActivity<M : ViewModel, D : ViewDataBinding> :
    BaseBindingActivity<D>() {

    val viewModel: M by viewModels()

    /**
     * view model 具体类型
     */
    abstract fun modelClass(): KClass<M>

    /**
     * 创建 ViewMode
     */
    @MainThread
    private fun viewModels(factoryProducer: (() -> ViewModelProvider.Factory)? = null): Lazy<M> {
        val factoryPromise = factoryProducer ?: {
            defaultViewModelProviderFactory
        }
        return ViewModelLazy(modelClass(), { viewModelStore }, factoryPromise)
    }
}