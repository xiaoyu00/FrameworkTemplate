package com.framework.base.adapter

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.framework.base.component.notify.NotifyType
import com.framework.base.data.ListLiveData
abstract class DataRecyclerAdapter<D:Any,VH : RecyclerView.ViewHolder>(private val activity: FragmentActivity) :
    RecyclerView.Adapter<VH>() {

    lateinit var dataListLiveData: ListLiveData<D>

    init {
        this.initDataSource()
        initNotifyChange()
    }

    abstract fun initDataSource()
    private fun initNotifyChange() {
        dataListLiveData.observe(activity, {
            Log.e("ssss", "sss::" )
            val isCollection = it.content is Collection<*>
            when (it.type) {
                NotifyType.ADD -> {
                    if (isCollection) {
                        this.notifyItemRangeInserted(it.index, (it.content as List<*>).size)
                    } else {
                        this.notifyItemInserted(it.index)
                    }
                }
                NotifyType.CHANGE -> {
                    if (isCollection) {
                        this.notifyItemRangeChanged(it.index, (it.content as List<*>).size)
                    } else {
                        this.notifyItemChanged(it.index)
                    }
                }
                NotifyType.DELETE -> {
                    this.notifyItemRemoved(it.index)
                    this.notifyItemRangeChanged(it.index, dataListLiveData.getData().size-it.index-1)
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return dataListLiveData.getData().size
    }
}
