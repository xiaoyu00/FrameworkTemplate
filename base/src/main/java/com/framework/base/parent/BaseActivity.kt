package com.framework.base.parent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Activity 基类
 */
abstract class BaseActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBase()
        setToolBar()
        initialize()
    }

    /**
     * 构建布局layoutId
     */
    abstract fun contextViewId(): Int

    /**
     * 基础初始化
     */
    open fun initBase() {
        setContentView(contextViewId())
    }
    private fun setToolBar() {
        getToolBar()?.let {
            toolbar = it
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = ""
                toolbar?.setNavigationOnClickListener { finish() }
            }
        }
    }
    open fun getToolBar(): Toolbar? = null
    /**
     * 初始化
     */
    abstract fun initialize()

}