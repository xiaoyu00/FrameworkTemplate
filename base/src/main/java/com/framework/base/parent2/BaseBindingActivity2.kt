package com.framework.base.parent2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.framework.base.parent2.`interface`.BaseBinding
import com.framework.base.parent2.`interface`.getViewBinding

/**
 * Data binding Activity 基类
 */
abstract class BaseBindingActivity2<D : ViewDataBinding> : AppCompatActivity() , BaseBinding<D> {

    private val mBinding: D by lazy(mode = LazyThreadSafetyMode.NONE) {
        getViewBinding<D>(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.initBinding()
    }
}