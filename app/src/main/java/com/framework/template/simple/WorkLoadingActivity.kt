package com.framework.template.simple

import android.os.Handler
import com.framework.base.parent.BaseBindingLoadingActivity
import com.framework.template.R
import com.framework.template.databinding.ActivityWorkLoadingBinding

class WorkLoadingActivity : BaseBindingLoadingActivity<ActivityWorkLoadingBinding>() {
    override fun contextViewId(): Int = R.layout.activity_work_loading

    override fun initialize() {
        dataBinding.tvContent.setOnClickListener {
            showLoadingDialog("正在加载中。。。")
        }
        showWorkLoading()
        Handler(mainLooper).postDelayed(Runnable {
            showWorkError()
        }, 3000)
    }

    override fun onLoadErrorClickListener() {
        workFinish()
    }

}