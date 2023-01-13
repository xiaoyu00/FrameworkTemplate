package com.framework.template.simple

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.framework.base.component.chatinput.ChatInputFragment
import com.framework.base.component.chatinput.MoreInputFragment
import com.framework.base.component.chatinput.PanelControlListener
import com.framework.base.component.face.FaceChatFragment
import com.framework.base.keyboard.KeyBoardInsetsCallBack
import com.framework.base.keyboard.KeyBoardListener
import com.framework.template.R

/**
 * 表情
 */
class FaceActivity : AppCompatActivity(), PanelControlListener {
    val SOFT_INPUT_HEIGHT = 835
    val PANEL_HEIGHT = 1000
    private val faceChatFragment = FaceChatFragment()
    private val moreInputFragment = MoreInputFragment()
    private val chatInputFragment = ChatInputFragment()
    private var panelAnimator: ObjectAnimator? = null
    lateinit var listView: View
    lateinit var contentLayout: LinearLayout

    var isPanelShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face)
//        val button=findViewById<Button>(R.id.test_button)
//        ViewCompat.setWindowInsetsAnimationCallback(button,)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        listView = findViewById(R.id.view_list)
        contentLayout = findViewById<LinearLayout>(R.id.layout_content)
        initView()
        initKeyBord()
    }

    private fun initView() {
        chatInputFragment.setFaceChatFragment(faceChatFragment)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_input_view, chatInputFragment).commit()
        contentLayout.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    contentLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    calculationLayoutSize()
                }
            })
        switchPanelFragment(1)
    }

    fun switchPanelFragment(panelIndex: Int) {
        when (panelIndex) {
            1 -> supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(
                    R.id.fragment_panel,
                    faceChatFragment,
                    faceChatFragment.javaClass.simpleName
                )
                .commit()
            else -> supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(
                    R.id.fragment_panel,
                    moreInputFragment,
                    moreInputFragment.javaClass.simpleName
                )
                .commit()
        }

    }


    private fun calculationLayoutSize() {
        val layoutParams = contentLayout.layoutParams as FrameLayout.LayoutParams
        val layoutParams2 = listView.layoutParams as LinearLayout.LayoutParams
        val cHeight: Int = contentLayout.height
        layoutParams2.height = cHeight
        listView.layoutParams = layoutParams2
        layoutParams.height = cHeight + PANEL_HEIGHT
        contentLayout.layoutParams = layoutParams
    }

    private fun initKeyBord() {
        val keyBoardInsetsCallBack = KeyBoardInsetsCallBack(object : KeyBoardListener {
            override fun onAnimStart() {}
            override fun onAnimDoing(offsetX: Int, offsetY: Int) {
                if (!isPanelShow && (panelAnimator == null || !panelAnimator!!.isRunning)) {
                    contentLayout.translationY = offsetY.toFloat()
                }
            }

            override fun onAnimEnd() {}
        })
        ViewCompat.setWindowInsetsAnimationCallback(
            window.decorView,
            keyBoardInsetsCallBack
        )
    }

    private fun panelHide() {
        if (isPanelShow) {
            isPanelShow = false
            panelAnimateTo(-SOFT_INPUT_HEIGHT)
        }
    }

    private fun panelShow() {
        if (!isPanelShow) {
            isPanelShow = true
            panelAnimateTo(-PANEL_HEIGHT)
        }
    }

    private fun panelAnimateTo(offset: Int) {
        panelAnimator = ObjectAnimator.ofFloat(contentLayout, "translationY", offset.toFloat())
        panelAnimator?.duration = 300
        panelAnimator?.interpolator = FastOutSlowInInterpolator()
        panelAnimator?.start()
    }

    override fun onPanelHide() {
        panelHide()
    }

    override fun onPanelShow() {
        panelShow()
    }

    override fun onSwitchPanelFragment(index: Int) {
        switchPanelFragment(index)
    }
}
