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
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.framework.base.component.chatinput.*
import com.framework.base.component.face.FaceChatFragment
import com.framework.base.keyboard.KeyBoardInsetsCallBack
import com.framework.base.keyboard.KeyBoardListener
import com.framework.base.utils.SoftKeyBoardUtil
import com.framework.template.R

/**
 * 表情
 */

class FaceActivity : AppCompatActivity(), PanelControlListener {
    var PANEL_HEIGHT = 1000
    private val faceChatFragment = FaceChatFragment()
    private val moreInputFragment = MoreInputFragment()
    private val chatInputFragment = ChatInputFragment()
    private var panelAnimator: ObjectAnimator? = null
    lateinit var listView: View
    lateinit var contentLayout: LinearLayout

    val chatInputViewModel:ChatInputViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face)
//        val button=findViewById<Button>(R.id.test_button)
//        ViewCompat.setWindowInsetsAnimationCallback(button,)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        listView = findViewById(R.id.view_list)
        contentLayout = findViewById<LinearLayout>(R.id.layout_content)
        initView()
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
                    initKeyBord()
                }
            })

        switchPanelFragment(1)
    }

    private fun switchPanelFragment(panelIndex: Int) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        when (panelIndex) {
            1 -> {
                if (faceChatFragment.isAdded) {
                    transaction.show(faceChatFragment)
                } else {
                    transaction.add(
                        R.id.fragment_panel,
                        faceChatFragment,
                        faceChatFragment.javaClass.simpleName
                    )
                }
                if (moreInputFragment.isAdded) {
                    transaction.hide(moreInputFragment)
                }
                transaction.commit()
            }
            else -> {
                if (moreInputFragment.isAdded) {
                    transaction.show(moreInputFragment)
                } else {
                    transaction.add(
                        R.id.fragment_panel,
                        moreInputFragment,
                        moreInputFragment.javaClass.simpleName
                    )
                }
                transaction.hide(faceChatFragment)
                transaction.commit()
            }
        }

    }


    private fun calculationLayoutSize() {
        val layoutParams = contentLayout.layoutParams as FrameLayout.LayoutParams
        val layoutParams2 = listView.layoutParams as LinearLayout.LayoutParams
        val cHeight: Int = contentLayout.height
        PANEL_HEIGHT=(cHeight*0.45).toInt()
        layoutParams2.height = cHeight
        listView.layoutParams = layoutParams2
        layoutParams.height = cHeight + PANEL_HEIGHT
        contentLayout.layoutParams = layoutParams
    }

    private fun initKeyBord() {
        val keyBoardInsetsCallBack = KeyBoardInsetsCallBack(object : KeyBoardListener {
            override fun onAnimStart(moveDistance:Int) {
                chatInputViewModel.isPanelShow = when (chatInputViewModel.handleType) {
                    HandleType.ONLY_PANEL_DOWN -> false
                    HandleType.ONLY_PANEL_UP -> true
                    HandleType.ONLY_KEYBOARD_DOWN -> false
                    HandleType.ONLY_KEYBOARD_UP -> false
                    HandleType.KD_PU -> {
                        panelAnimateTo(-PANEL_HEIGHT)
                        true
                    }
                    HandleType.KU_PD -> {
                        panelAnimateTo(-moveDistance)
                        false
                    }
                }
            }
            override fun onAnimDoing(offsetX: Int, offsetY: Int) {
                if (chatInputViewModel.handleType != HandleType.KU_PD && chatInputViewModel.handleType != HandleType.KD_PU) {
                    contentLayout.translationY = offsetY.toFloat()
                }
            }

            override fun onAnimEnd() {
                chatInputViewModel.handleType= HandleType.ONLY_KEYBOARD_UP
            }
        })
        ViewCompat.setWindowInsetsAnimationCallback(
            window.decorView,
            keyBoardInsetsCallBack
        )
    }


    private fun panelAnimateTo(offset: Int) {
        panelAnimator = ObjectAnimator.ofFloat(contentLayout, "translationY", offset.toFloat())
        panelAnimator?.interpolator = FastOutSlowInInterpolator()
        panelAnimator?.start()
    }

    override fun onPanelShow() {
        if(!chatInputViewModel.isPanelShow){
            panelAnimateTo(-PANEL_HEIGHT)
        }
    }
    override fun onPanelHide() {
        if(chatInputViewModel.isPanelShow){
            panelAnimateTo(0)
        }
    }

    override fun onSwitchPanelFragment(index: Int) {
        switchPanelFragment(index)
    }
}
