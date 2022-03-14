package com.framework.base.common

import android.graphics.drawable.Drawable
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.ListView
import androidx.databinding.BindingAdapter
import com.framework.base.utils.GlideUtil
import com.framework.base.utils.ViewUtils

@BindingAdapter(value = ["drawableUrl"])
fun ImageView.loadUrlImage(drawableUrl: String) {
    GlideUtil.loadImage(context, this, drawableUrl)
}

@BindingAdapter("setGridViewData", "numColumns")
fun setGridViewData(gridView: GridView, adapter: BaseAdapter, numColumns: Int) {
    gridView.adapter = adapter
    val height = ViewUtils.calculatedGridViewHeight(gridView, adapter, numColumns)
    ViewUtils.setViewHeight(gridView, height)

}

@BindingAdapter("setListViewData")
fun setListViewData(listView: ListView, adapter: BaseAdapter) {
    listView.adapter = adapter
    val height = ViewUtils.calculatedListViewHeight(listView, adapter)
    ViewUtils.setViewHeight(listView, height)
}