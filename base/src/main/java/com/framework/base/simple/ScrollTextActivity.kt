package com.framework.base.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.framework.base.R

/**
 * textView 跑马灯效果
 */
class ScrollTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_text)
    }
}