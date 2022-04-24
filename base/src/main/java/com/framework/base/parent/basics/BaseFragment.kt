package com.framework.base.parent.basics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Fragment 基类
 */
abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initBase(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    /**
     * 构建布局layoutId
     */
    abstract fun contextViewId(): Int

    /**
     * 基础初始化
     */
    open fun initBase(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        return inflater.inflate(contextViewId(), container, false)
    }
    /**
     * 初始化
     */
    abstract fun initialize()

}