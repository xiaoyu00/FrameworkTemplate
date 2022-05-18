package com.framework.template.simple.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.framework.base.parent.BaseBindingLoadingFragment
import com.framework.template.R
import com.framework.template.databinding.FragmentWorkLoadingBinding

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WorkLoadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkLoadingFragment : BaseBindingLoadingFragment<FragmentWorkLoadingBinding>() {
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SimpleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WorkLoadingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun contextViewId(): Int = R.layout.fragment_work_loading

    override fun initialize() {
        dataBinding.tvContent.setOnClickListener {
            showUploadLoading("正在加载中。。。")
        }
        showWorkLoading()

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            showWorkError()
        }, 3000)
    }
    override fun onLoadErrorClickListener() {
        workFinish()
    }
}