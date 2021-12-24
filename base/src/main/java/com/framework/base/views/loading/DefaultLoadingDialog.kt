package com.framework.base.views.loading

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.databinding.DataBindingUtil
import com.framework.base.R
import com.framework.base.databinding.DialogDefaultLoadingBinding

class DefaultLoadingDialog(content: Context) : BaseLoadingDialog(content) {
    lateinit var binding: DialogDefaultLoadingBinding
    private var loadingTips: String? = null
    override fun getLoadingView(): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_default_loading, null, false)
//        binding.root.setBackgroundDrawable(
//            CornerUtils.cornerDrawable(
//                Color.parseColor("#ffffff"),
//                dp2px(4f).toFloat()
//            )
//        )
        loadingTips?.let {
            binding.tvTips.text = it
        }
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        return binding.root
    }

    override fun show(loadingTips: String) {
        this.loadingTips = loadingTips
        show()
    }


}
