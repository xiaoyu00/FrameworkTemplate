package com.framework.template.simple

import com.framework.base.parent.basics.BaseBindingActivity
import com.framework.template.R
import com.framework.template.databinding.ActivityWorkLoadingFragmentBinding

class WorkLoadingFragmentActivity : BaseBindingActivity<ActivityWorkLoadingFragmentBinding>() {
    override fun contextViewId(): Int = R.layout.activity_work_loading_fragment

    override fun initialize() {

    }
}