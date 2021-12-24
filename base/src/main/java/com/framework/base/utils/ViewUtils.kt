package com.framework.base.utils

import android.view.View
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ListView

/**
 * 控件工具类
 */
object ViewUtils {

    /**
     * 设置控件高度
     * @param height 控件高度  单位为px
     */
    fun setViewHeight(view: View, height: Int) {
        val layoutParams = view.layoutParams
        layoutParams.height = height
        view.layoutParams = layoutParams
    }

    /**
     * 设置控件宽度
     * @param width 控件宽度  单位为px
     */
    fun setViewWidth(view: View, width: Int) {
        val layoutParams = view.layoutParams
        layoutParams.width = width
        view.layoutParams = layoutParams
    }

    /**
     * 设置控件大小
     * @param width 控件宽度  单位为px
     * @param height 控件高度  单位为px
     */
    fun setViewSize(view: View, width: Int, height: Int) {
        val layoutParams = view.layoutParams
        layoutParams.width = width
        layoutParams.height = height
        view.layoutParams = layoutParams
    }

    /**
     * 计算listView高度
     * @param adapter 组装ListView数据adapter
     */
    fun calculatedListViewHeight(listView: ListView, adapter: BaseAdapter): Int {
        var total = 0
        for (i in 0 until adapter.count) {
            val listItem: View = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            total += listItem.measuredHeight
        }
        var dividerHeight = 0
        if (adapter.getCount() > 0) {
            dividerHeight = (listView.dividerHeight) * (adapter.count - 1)
        }
        return total + dividerHeight
    }

    /**
     * 计算gridView高度
     */
    fun calculatedGridViewHeight(
        gridView: GridView?,
        baseAdapter: BaseAdapter,
        numColumns: Int
    ): Int {
        val total: Int
        val view = baseAdapter.getView(0, null, gridView)
        view.measure(0, 0)
        total = Math.ceil((baseAdapter.count / numColumns.toFloat()).toDouble())
            .toInt() * view.measuredHeight
        return total
    }
}