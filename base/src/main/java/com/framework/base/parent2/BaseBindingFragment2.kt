package com.framework.base.parent2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.framework.base.parent2.`interface`.BaseBinding
import com.framework.base.parent2.`interface`.getViewBinding

/**
 * Fragment 基类
 */
abstract class BaseBindingFragment2<D : ViewDataBinding> : Fragment(), BaseBinding<D> {
    private lateinit var mBinding: D
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = getViewBinding(inflater, container)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.initBinding()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mBinding.isInitialized) {
            mBinding.unbind()
        }
    }
}