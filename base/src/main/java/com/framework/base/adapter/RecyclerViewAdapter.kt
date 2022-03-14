package com.framework.base.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mc.base.UrlConfig
import com.mc.breeding.common.getImageAdapter
import com.mc.breeding.common.getOperationItemLayoutId
import com.mc.breeding.common.getType
import com.mc.breeding.common.setStateString
import com.mc.breeding.databinding.*
import com.mc.breeding.model.*
import com.mc.breeding.utils.*

class RecyclerViewAdapter(
    private val activity: FragmentActivity,
    private var homeConfig: HomeGlide?,
    private val list: List<Any>
) : RecyclerView.Adapter<OperationViewHolder>() {
    private var itemClickListener: ((data: Any) -> Unit)? = null
    private var variety: Variety? = null
    fun notifyChange(homeGlide: HomeGlide?, v: Variety?) {
        this.homeConfig = homeGlide
        this.variety = v
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: ((data: Any) -> Unit)) {
        itemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return homeConfig?.type ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {

        return OperationViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getOperationItemLayoutId(homeConfig?.type, variety),
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
        val imageList = mutableListOf<String>()
        when (binding) {
            is ItemObserveBinding -> {
                val observe = data as GrassObserve
                observe.photos?.let { p ->
                    imageList.clear()
                    imageList.addAll(p.mapNotNull { it.fileName })
                }
                binding.adapter = getImageAdapter(activity, imageList)
                binding.layoutContent.removeAllViews()
                observe.data?.let {
                    it.forEach {
                        val binding2: ItemObserveDataBinding =
                            ItemObserveDataBinding.inflate(LayoutInflater.from(binding.root.context))
                        binding2.data = it
                        setStateString(it, binding2.tvState, binding.root.context)
                        binding.layoutContent.addView(binding2.root)
                    }
                }

            }
            is ItemGrassYieldBinding -> {
                binding.yield = data as GrassYield
                data.photos?.let { p ->
                    imageList.clear()
                    imageList.addAll(p.mapNotNull { it.fileName })
                }
                binding.adapter = getImageAdapter(activity, imageList)
            }
        }
        binding.root.setOnClickListener {
            listener?.invoke(data)
        }
    }
}
