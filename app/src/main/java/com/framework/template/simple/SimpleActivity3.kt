package com.framework.template.simple

import android.util.Log
import android.view.View
import com.framework.base.parent2.BaseBindingActivity2
import com.framework.template.databinding.ActivityMainBinding

//class SimpleActivity3 : BaseBindingActivity2<ActivityMainBinding>() {
//    lateinit var homeAdapter:HomeAdapter
//    override fun ActivityMainBinding.initBinding() {
//        homeAdapter = HomeAdapter(itemClickListener)
//        with(recyclerView){
//            layoutManager = LinearLayoutManager(this@SimpleActivity3).apply {
//                orientation = RecyclerView.VERTICAL
//            }
//            adapter = homeAdapter
//        }
//        homeAdapter.setData(listOf("a","b","c","d","e","f"))
//        btn.setOnClickListener {
//            Log.d("刷新", "第二次setData")
//            homeAdapter.setData(listOf("c","d","e","f"))
//        }
//    }
//
//    private val itemClickListener = object : ItemClickListener<String> {
//        override fun onItemClick(view: View, position: Int, data: String) {
//            Log.d("onItemClick", "data:$data   position:$position")
//        }
//    }
//}



