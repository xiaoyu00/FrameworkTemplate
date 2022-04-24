package com.framework.base.parent.basics

import android.R
import android.os.Bundle
import android.view.MenuItem
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
            if (supportActionBar == null) {
                setSupportActionBar(toolbar)
            }
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getToolBarTitle()
        }
    }

    open fun getToolBar(): Toolbar? = null
    open fun getToolBarTitle(): String = ""

    /**
     * 初始化
     */
    abstract fun initialize()
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}