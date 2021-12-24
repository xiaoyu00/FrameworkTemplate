package com.framework.base.simple

import android.os.Handler
import com.framework.base.R
import com.framework.base.databinding.ActivityWorkLoadingBinding
import com.framework.base.parent.BaseBindingLoadingActivity

class WorkLoadingActivity : BaseBindingLoadingActivity<ActivityWorkLoadingBinding>() {
    override fun contextViewId(): Int = R.layout.activity_work_loading

    override fun initialize() {
        dataBinding.tvContent.setOnClickListener {
            showUploadLoading("正在加载中。。。")
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