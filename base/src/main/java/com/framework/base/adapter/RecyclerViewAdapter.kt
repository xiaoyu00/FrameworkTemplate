package com.framework.base.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.framework.base.data.UserInfo

class RecyclerViewAdapter(
    private val activity: FragmentActivity,
    private var userInfo: UserInfo?,
    private val list: List<Any>
) : RecyclerView.Adapter<OperationViewHolder>() {
    private var itemClickListener: ((data: Any) -> Unit)? = null
    fun notifyChange(userInfo: UserInfo?) {
        this.userInfo = userInfo
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: ((data: Any) -> Unit)) {
        itemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return userInfo?.type ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {

        return OperationViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                0,
                parent,
                false
            ), itemClickListener
        )
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        holder.bindingData(activity, list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class OperationViewHolder(val binding: ViewDataBinding, val listener: ((data: Any) -> Unit)?) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindingData(activity: FragmentActivity, data: Any) {
//        val imageList = mutableListOf<String>()
//        when (binding) {
//            is ItemObserveBinding -> {
//                val observe = data as GrassObserve
//                observe.photos?.let { p ->
//                    imageList.clear()
//                    imageList.addAll(p.mapNotNull { it.fileName })
//                }
//                binding.adapter = getImageAdapter(activity, imageList)
//                binding.layoutContent.removeAllViews()
//                observe.data?.let {
//                    it.forEach {
//                        val binding2: ItemObserveDataBinding =
//                            ItemObserveDataBinding.inflate(LayoutInflater.from(binding.root.context))
//                        binding2.data = it
//                        setStateString(it, binding2.tvState, binding.root.context)
//                        binding.layoutContent.addView(binding2.root)
//                    }
//                }
//
//            }
//            is ItemGrassYieldBinding -> {
//                binding.yield = data as GrassYield
//                data.photos?.let { p ->
//                    imageList.clear()
//                    imageList.addAll(p.mapNotNull { it.fileName })
//                }
//                binding.adapter = getImageAdapter(activity, imageList)
//            }
//        }
//        binding.root.setOnClickListener {
//            listener?.invoke(data)
//        }
    }
}
