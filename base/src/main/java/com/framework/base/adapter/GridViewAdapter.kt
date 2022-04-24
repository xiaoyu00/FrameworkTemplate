package com.framework.base.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.framework.base.model.UserInfo

class GridViewAdapter(private val homeConfigList: List<UserInfo>) : BaseAdapter() {

    override fun getCount(): Int = homeConfigList.size

    override fun getItem(position: Int): Any = homeConfigList[position]

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
//        val itemBinding: ItemBreedingCommonIconBinding = if (convertView == null) {
//            DataBindingUtil.inflate(
//                LayoutInflater.from(parent?.context),
//                R.layout.item_breeding_common_icon,
//                parent,
//                false
//            )
//        } else {
//            DataBindingUtil.getBinding(convertView)!!
//        }
//        itemBinding.homeGrid = homeConfigList[position]
//        itemBinding.icon.setImageResource(homeConfigList[position].resId)
//        return itemBinding.root
        return null
    }
}
