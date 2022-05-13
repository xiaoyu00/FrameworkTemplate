package com.framework.template.simple

import com.framework.base.parent.basics.BaseBindingActivity
import com.framework.share.ShareManager
import com.framework.share.core.ShareData
import com.framework.template.R
import com.framework.template.databinding.ActivityShareBinding

/**
 * 分享
 */
class ShareActivity : BaseBindingActivity<ActivityShareBinding>() {

    override fun contextViewId(): Int = R.layout.activity_share

    override fun initialize() {
        ShareManager.init(this)
        dataBinding.share.setOnClickListener {
            ShareManager.showSharePop(
                this,
                ShareData.Builder().title("fenxiang").content("分享").description("描述").build()
            )
        }
    }
}
