package com.framework.base.simple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.framework.base.R

/**
 * 锁屏唤醒并解锁屏幕
 * 需要添加权限
 * <uses-permission android：name="android.permission.WAKE_LOCK" />
 * <uses-permission android：name="android.permission.DISABLE_KEYGUARD" />
 */
class WeekUpScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置点亮屏幕
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        setContentView(R.layout.activity_week_up_screen)
    }

    public fun startWeekUpScreenActivity() {
        val intent: Intent = Intent(
            this@WeekUpScreenActivity,
            WeekUpScreenActivity::class.java
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }
}