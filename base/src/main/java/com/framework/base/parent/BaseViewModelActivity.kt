package com.framework.base.parent

import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.framework.base.parent.basics.BaseBindingActivity
import kotlin.reflect.KClass

/**
 * View Model Activity 基类
 */
abstract class BaseViewModelActivity<M : ViewModel, D : ViewDataBinding> :
    BaseBindingActivity<D>() {

    val viewModel: M by activityViewModels()

    /**
     * view model 具体类型
     */
    abstract fun modelClass(): KClass<M>

    /**
     * 创建 ViewMode
     */
    @MainThread
    private fun activityViewModels(factoryProducer: (() -> ViewModelProvider.Factory)? = null): Lazy<M> {
        val factoryPromise = factoryProducer ?: {
            defaultViewModelProviderFactory
        }
        return ViewModelLazy(modelClass(), { viewModelStore }, factoryPromise)
    }
}