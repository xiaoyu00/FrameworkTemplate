package com.framework.base.simple

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.framework.base.R
import com.framework.base.adapter.DataRecyclerAdapter
import com.framework.base.data.DataManager
import com.framework.base.data.UserInfo
import com.framework.base.databinding.ActivityListBinding
import com.framework.base.databinding.ItemSimpleListBinding
import com.framework.base.parent.BaseBindingActivity
import com.framework.base.utils.CommonUtils
import com.framework.base.utils.DateUtils

class ListActivity : BaseBindingActivity<ActivityListBinding>() {

    override fun contextViewId(): Int = R.layout.activity_list

    private fun initDatas() {
        val list = mutableListOf<UserInfo>()
        for (i in 0 until 5) {
            list.add(
                UserInfo(
                    i.toLong(),
                    baseName = "initDatas:" + DateUtils.formatTimeForMilliSecond(System.currentTimeMillis())
                )
            )
        }
        DataManager.userListData.addAll(list)
    }

    override fun initialize() {
        initDatas()
        val ad = Adapter(this)
        dataBinding.recyclerview.adapter = ad

        dataBinding.addOne0.setOnClickListener {
            val index = CommonUtils.getRandomNumber(0, DataManager.userListData.getData().size)
            DataManager.userListData.add(
                UserInfo(
                    index.toLong(),
                    baseName = "addOne0:" + DateUtils.formatTimeForMilliSecond(System.currentTimeMillis())
                )
            )
        }
        dataBinding.addOne.setOnClickListener {
            val index = CommonUtils.getRandomNumber(0, DataManager.userListData.getData().size)
            DataManager.userListData.add(
                UserInfo(index.toLong(), baseName = "addOne:$index" + System.currentTimeMillis()),
                index
            )
        }
        dataBinding.addMultiple.setOnClickListener {
            val list = mutableListOf<UserInfo>()
            for (i in 0 until 5) {
                list.add(
                    UserInfo(
                        i.toLong(),
                        baseName = "addMultiple:" + DateUtils.formatTimeForMilliSecond(System.currentTimeMillis())
                    )
                )
            }
            DataManager.userListData.addAll(list)
        }
        dataBinding.change0.setOnClickListener {
            val index = CommonUtils.getRandomNumber(0, DataManager.userListData.getData().size)
            DataManager.userListData.replace(
                UserInfo(index.toLong(), baseName = "change0:$index"), null
            ) {
                it.id == index.toLong()
            }
        }
        dataBinding.change.setOnClickListener {
            val index = CommonUtils.getRandomNumber(0, DataManager.userListData.getData().size)
            DataManager.userListData.replace(
                UserInfo(index.toLong(), baseName = "change:$index"),
                index
            )
        }
        dataBinding.changeMultiple.setOnClickListener {
            val list = mutableListOf<UserInfo>()
            for (i in 0 until 5) {
                list.add(
                    UserInfo(
                        i.toLong(),
                        baseName = "changeMultiple:" + DateUtils.formatTimeForMilliSecond(System.currentTimeMillis())
                    )
                )
            }
            DataManager.userListData.replaceAll(list)
        }
        dataBinding.remove.setOnClickListener {
            val last = DataManager.userListData.getData().last()
            DataManager.userListData.remove(last)
        }
        dataBinding.removeindex.setOnClickListener {
            val index = CommonUtils.getRandomNumber(0, DataManager.userListData.getData().size)
            DataManager.userListData.remove(index)
        }
        dataBinding.refresh.setOnClickListener {
            ad.notifyDataSetChanged()
        }
    }

    class Adapter(activity: FragmentActivity) :
        DataRecyclerAdapter<UserInfo, RViewHolder>(activity) {
        override fun initDataSource() {
            dataListLiveData = DataManager.userListData
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RViewHolder {
            return RViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_simple_list,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: RViewHolder, position: Int) {
            holder.bindingData(dataListLiveData.getData()[position])
        }
    }

    class RViewHolder(val binding: ItemSimpleListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindingData(data: UserInfo) {
            binding.textName.text = data.baseName
        }
    }
}