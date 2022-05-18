package com.framework.template.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.framework.base.views.expandabletext.ExpandableTextView
import com.framework.base.views.expandabletext.MoreLineTextView
import com.framework.base.views.loading.shape.ShapeLoadingDialog
import com.framework.template.R

/**
 * 动画loading（纯画布实现） shapeloading
 */
class ShapeLoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shape_loading)
        val shapeLoadingDialog:ShapeLoadingDialog= ShapeLoadingDialog(this)
        shapeLoadingDialog.show("正在加载中。。。。")
    }

}