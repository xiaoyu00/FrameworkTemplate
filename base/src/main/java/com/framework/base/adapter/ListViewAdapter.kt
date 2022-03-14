package com.framework.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.framework.base.data.UserInfo

class ListViewAdapter(private val dataList: List<UserInfo>) : BaseAdapter() {

    private var itemClickListener: ((data: UserInfo) -> Unit)? = null

    fun setOnItemClickListener(listener: ((data: UserInfo) -> Unit)) {
        this.itemClickListener = listener
    }

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEnabled(position: Int): Boolean {
        return false
    }

    override fun getCount(): Int = dataList.size

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemBreedingBaseBinding = if (convertView == null) {
            ItemBreedingBaseBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        } else {
            DataBindingUtil.getBinding(convertView)!!
        }
        binding.data = dataList[position]
        return binding.root
    }
}