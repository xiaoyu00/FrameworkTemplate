package com.framework.template.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.framework.template.R

/**
 * String 占位符
 */
class StringTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_string_text)
        findViewById<TextView>(R.id.tvTip).text=getString(R.string.string_tips,"张三")
    }
}