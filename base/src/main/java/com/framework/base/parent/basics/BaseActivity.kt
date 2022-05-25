package com.framework.base.parent.basics

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.R
import com.framework.base.utils.ScreenUtils

/**
 * Activity 基类
 */
abstract class BaseActivity : AppCompatActivity() {
    var displayMetrics: DisplayMetrics? = null
    var mStatusBarHeight = 0
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBase()
        initialize()
        if (isFullScreen()) {
            ScreenUtils.hideWindowStatusBar(window)
            initStatusBarHeight()
        } else {
            setToolBar()
        }
        setGlobalLayoutListener()
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
     * 是否全屏
     *  true 隐藏状态栏并获取 并获取获取除虚拟按键屏幕的尺寸
     *  false setToolbar
     */
    open fun isFullScreen() = false
    /**
     * 内容视图布局过程已完成回调（可在这里计算尺寸）
     */
    open fun onGlobalLayoutCompleted() {}
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

    private fun setGlobalLayoutListener() {
        val layout = findViewById<View>(Window.ID_ANDROID_CONTENT)
        val observer = layout.viewTreeObserver
        observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                layout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                onGlobalLayoutCompleted()
            }
        })
    }

    private fun initStatusBarHeight() {
        mStatusBarHeight = ScreenUtils.getStatusBarHeight(this)
    }

    private fun getDisplayMetrics() {
        displayMetrics = ScreenUtils.getDisplayMetrics(this)
    }
}