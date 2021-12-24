package com.framework.template

import android.content.Intent
import com.framework.base.parent.BaseViewModelActivity
import com.framework.base.parent.WorkLoading
import com.framework.base.simple.WorkLoadingActivity
import com.framework.base.simple.WorkLoadingFragmentActivity
import com.framework.template.databinding.ActivityMainBinding

class MainActivity : BaseViewModelActivity<SimpleViewModel, ActivityMainBinding>() {

    override fun contextViewId() = R.layout.activity_main
    override fun initialize() {
        dataBinding.tvHello.setOnClickListener {
//            startActivity(Intent(this, WorkLoadingFragmentActivity::class.java))
            startActivity(Intent(this, WorkLoadingActivity::class.java))
        }
    }

    override fun modelClass() = SimpleViewModel::class
}