package com.framework.base.views.loading.shape

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.databinding.DataBindingUtil
import com.framework.base.R
import com.framework.base.databinding.DialogLoadShapeBinding
import com.framework.base.views.loading.BaseLoadingDialog

class ShapeLoadingDialog (content: Context) : BaseLoadingDialog(content) {
    lateinit var binding: DialogLoadShapeBinding
    private var loadingTips: String? = null
    override fun getLoadingView(): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.dialog_load_shape, null, false)
//        binding.root.setBackgroundDrawable(
//            CornerUtils.cornerDrawable(
//                Color.parseColor("#ffffff"),
//                dp2px(4f).toFloat()
//            )
//        )
        loadingTips?.let {
            binding.loadView.setLoadingText(it)
        }
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(true)
        return binding.root
    }

    override fun show(loadingTips: String) {
        this.loadingTips = loadingTips
        show()
    }
}