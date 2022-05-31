package com.framework.base.parent.basics

import android.os.Bundle
import android.view.*
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
        setGlobalLayoutListener()
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

    private fun setGlobalLayoutListener() {
        val layout = requireActivity().findViewById<View>(Window.ID_ANDROID_CONTENT)
        val observer = layout.viewTreeObserver
        observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //如果去掉此行代码后 每次布局变化都会执行onGlobalLayoutCompleted
                layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                onGlobalLayoutCompleted()
            }
        })
    }

    open fun onGlobalLayoutCompleted() {}

    /**
     * 初始化(onViewCreated)
     */
    abstract fun initialize()

}