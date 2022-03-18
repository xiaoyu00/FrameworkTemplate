package com.framework.base.data

import androidx.lifecycle.MutableLiveData
import com.framework.base.component.notify.Notification
import com.framework.base.component.notify.NotifyType

class ListLiveData<T : Any> : MutableLiveData<Notification<*>>() {
    private var data = mutableListOf<T>()

    fun getData(): List<T> {
        return data
    }

    fun add(d: T, index: Int? = null) {
        if (index != null) {
            if (index < data.size && index > -1) {
                data.add(index, d)
            }
        } else {
            data.add(d)
        }
        val i = index ?: data.size
        notifyChange(NotifyType.ADD, i, d)
    }

    fun addAll(d: List<T>) {
        val index = data.size
        data.addAll(d)
        notifyChange(NotifyType.ADD, index, d)
    }

    fun replaceAll(d: List<T>) {
        data.clear()
        data.addAll(d)
        notifyChange(NotifyType.CHANGE, 0, d)
    }

    fun replace(d: T, index: Int? = null,predicate:( (T) -> Boolean)? = null) {

        val i = if(predicate!=null) data.indexOfFirst(predicate) else index ?: data.indexOf(d)
        if (i != -1) {
            data[i] = d
        }
        notifyChange(NotifyType.CHANGE, i, d)
    }

    fun remove(index: Int?) {
        if (data.isEmpty()) {
            return
        }
        val d: T?
        val i: Int?
        if (index != null && index < data.size && index > -1) {
            d = data[index]
            i = index
            data.removeAt(index)
        } else {
            d = data.last()
            i = data.size - 1
            data.remove(d)
        }

        notifyChange(NotifyType.DELETE, i, d)
    }

    fun remove(d: T) {
        val index = data.indexOf(d)
        data.remove(d)
        notifyChange(NotifyType.DELETE, index, d)
    }

    private fun notifyChange(type: NotifyType, index: Int, content: Any) {
        this.value = Notification(type, index, content)
    }
}