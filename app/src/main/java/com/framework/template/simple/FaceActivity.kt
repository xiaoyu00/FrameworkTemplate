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
    val SOFT_INPUT_HEIGHT = 835
    val PANEL_HEIGHT = 1000
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
        layoutParams2.height = cHeight
        listView.layoutParams = layoutParams2
        layoutParams.height = cHeight + PANEL_HEIGHT
        contentLayout.layoutParams = layoutParams
    }

    private fun initKeyBord() {
        val keyBoardInsetsCallBack = KeyBoardInsetsCallBack(object : KeyBoardListener {
            override fun onAnimStart() {
                if(SoftKeyBoardUtil.isSoftInputShown(this@FaceActivity)){
                    chatInputViewModel.panelState=PanelState.HIDE
                }
            }
            override fun onAnimDoing(offsetX: Int, offsetY: Int) {
                if (chatInputViewModel.panelState != PanelState.SHOW && panelAnimator?.isRunning != true) {
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


    private fun panelHalf() {
        if (chatInputViewModel.panelState != PanelState.HALF) {
            panelAnimateTo(-SOFT_INPUT_HEIGHT)
            chatInputViewModel.panelState = PanelState.HALF
        }
    }

    private fun panelShow() {
        if (chatInputViewModel.panelState != PanelState.SHOW) {
            panelAnimateTo(-PANEL_HEIGHT)
            chatInputViewModel.panelState = PanelState.SHOW
        }
    }

    private fun panelHide() {
        if (chatInputViewModel.panelState != PanelState.HIDE) {
            panelAnimateTo(0)
            chatInputViewModel.panelState = PanelState.HIDE
        }
    }

    private fun panelAnimateTo(offset: Int) {
        panelAnimator = ObjectAnimator.ofFloat(contentLayout, "translationY", offset.toFloat())
        panelAnimator?.interpolator = FastOutSlowInInterpolator()
        panelAnimator?.start()
    }

    override fun onPanelHide() {

        panelHide()
    }

    override fun onPanelHalf() {
        panelHalf()
    }

    override fun onPanelShow() {
        panelShow()
    }

    override fun onSwitchPanelFragment(index: Int) {
        switchPanelFragment(index)
    }
}
