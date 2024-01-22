package com.framework.template.simple

import android.app.Activity
import android.app.Presentation
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.framework.template.R
import com.framework.template.databinding.ActivityDualScreenBinding
import com.framework.template.databinding.LayoutPresentationBinding

/**
 * 双屏异显
 */
class DualScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDualScreenBinding
    private var presentation: MyPresentation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_dual_screen
        )
        binding.lifecycleOwner=this
        binding.btnShow.setOnClickListener {
            showScreen()
        }
        binding.btnClose.setOnClickListener {
            closeScreen()
        }

    }

    private fun showScreen() {
        val displayManager = getSystemService<DisplayManager>()
        val d: DisplayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val displays = displayManager?.displays
        displays?.let {
            if (it.size > 1) {
                //displays[0] 主屏，displays[1] 副屏
                presentation = MyPresentation(this, it[1])
                presentation?.show()
            }
        }
    }

    private fun closeScreen() {
        presentation?.dismiss()
    }

    //副屏 监听键盘输入，注意这个也只能在Activity里面才能监听到
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
//        if (keyListener != null) {
//            val action = event!!.action
//            when (action) {
//                KeyEvent.ACTION_DOWN -> {
//                    if (event!!.keyCode === KeyEvent.KEYCODE_VOLUME_UP) {
//                        return super.dispatchKeyEvent(event)
//                    }
//                    if (event!!.keyCode === KeyEvent.KEYCODE_VOLUME_DOWN) {
//                        return super.dispatchKeyEvent(event)
//                    }
//                    if (event!!.keyCode === KeyEvent.KEYCODE_BACK) {
//                        return super.dispatchKeyEvent(event)
//                    }
//                    if (event!!.keyCode === KeyEvent.KEYCODE_MENU) {
//                        return super.dispatchKeyEvent(event)
//                    }
//                    if (event!!.keyCode === KeyEvent.KEYCODE_HOME) {
//                        return super.dispatchKeyEvent(event)
//                    }
//                    if (event!!.keyCode === KeyEvent.KEYCODE_POWER) {
//                        return super.dispatchKeyEvent(event)
//                    }
//                    return if (event!!.keyCode === KeyEvent.KEYCODE_F1) {
//                        keyListener.textSure("功能")
//                        true
//                    } else if (event!!.keyCode === KeyEvent.KEYCODE_F2) {
//                        keyListener.textSure("设置")
//                        true
//                    } else if (event!!.keyCode === 111 || event!!.keyCode === 4) {
//                        keyListener.textSure("取消")
//                        true
//                    } else if (event!!.keyCode === KeyEvent.KEYCODE_DEL) {
//                        keyListener.textSure("删除")
//                        true
//                    } else if (event!!.keyCode === KeyEvent.KEYCODE_ENTER) {
//                        keyListener.textSure("确认")
//                        true
//                    } else if (event!!.keyCode === KeyEvent.KEYCODE_DPAD_UP) {
//                        keyListener.textSure("上箭头")
//                        true
//                    } else if (event!!.keyCode === KeyEvent.KEYCODE_DPAD_DOWN) {
//                        keyListener.textSure("下箭头")
//                        true
//                    } else {
//                        val unicodeChar = event!!.unicodeChar
//                        keyListener.textChange(unicodeChar.toChar().toString())
//                        true
//                    }
//                }
//            }
//        }
        return super.dispatchKeyEvent(event)
    }
    class MyPresentation(context: Context, display: Display) : Presentation(context, display) {
        private lateinit var binding: LayoutPresentationBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.layout_presentation,
                null,
                false)
            setContentView(binding.root)
//            setContentView(R.layout.layout_presentation)
            // 副屏常驻 Presentation 不随着主屏 Activity 的生命周期显示隐藏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.GRAY))
            binding.lyClose.setOnClickListener {
                dismiss()
            }
        }
    }
}