package com.framework.template.simple.selectsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.mc.produce.R
import com.mc.produce.bean.People
import com.mc.produce.databinding.ItemPeopleBinding

class PeopleAdapter(
    private val peoples: List<People>,
    private val selectPeoples: MutableList<People>
) :
    BaseAdapter() {

    override fun getCount(): Int = peoples.size

    override fun getItem(position: Int): Any = peoples[position]

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemBinding: ItemPeopleBinding = if (convertView == null) {
            DataBindingUtil.inflate(
                LayoutInflater.from(parent?.context),
                R.layout.item_people,
                parent,
                false
            )
        } else {
            DataBindingUtil.getBinding(convertView)!!
        }
        val people = peoples[position]
        itemBinding.people = people
        itemBinding.check.isChecked =
            selectPeoples.firstOrNull { it.userId == people.userId } != null
        itemBinding.root.setOnClickListener {
            itemBinding.check.isChecked = !itemBinding.check.isChecked
            if (itemBinding.check.isChecked) {
                selectPeoples.add(people)
            } else {
                val index= selectPeoples.indexOfFirst { it.userId == people.userId }
                if(index!=-1){
                    selectPeoples.removeAt(index)
                }
            }
            notifyDataSetChanged()
        }
        return itemBinding.root
    }
}
