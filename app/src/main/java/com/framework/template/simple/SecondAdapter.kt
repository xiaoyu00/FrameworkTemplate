package com.framework.template.simple

//class HomeAdapter(private val listener: ItemClickListener<String>) :
//    BaseAdapter<String, ItemHomeBinding>() {
//
//    override fun ItemHomeBinding.setListener() {
//        itemClickListener = listener
//    }
//
//    override fun ItemHomeBinding.onBindViewHolder(bean: String, position: Int) {
//        this.bean = bean
//        this.position = position
//        tv.text = bean
//    }
//}
//
//class SecondAdapter : BaseMultiTypeAdapter<Person>() {
//
//    companion object {
//        private const val ITEM_DEFAULT_TYPE = 0
//        private const val ITEM_STUDENT_TYPE = 1
//        private const val ITEM_TEACHER_TYPE = 2
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return when (getItem(position)) {
//            is Student -> ITEM_STUDENT_TYPE
//            is Teacher -> ITEM_TEACHER_TYPE
//            else -> ITEM_DEFAULT_TYPE
//        }
//    }
//
//    override fun onCreateMultiViewHolder(parent: ViewGroup, viewType: Int): ViewDataBinding {
//        return when (viewType) {
//            ITEM_STUDENT_TYPE -> loadLayout(ItemStudentBinding::class.java, parent)
//            ITEM_TEACHER_TYPE -> loadLayout(ItemTeacherBinding::class.java, parent)
//            else -> loadLayout(ItemPersionBinding::class.java, parent)
//        }
//    }
//
//    override fun MultiTypeViewHolder.onBindViewHolder(
//        holder: MultiTypeViewHolder,
//        item: Person,
//        position: Int
//    ) {
//        when (holder.binding) {
//            is ItemStudentBinding -> {
//                Log.d("ItemStudentBinding", "item : $item   position : $position")
//            }
//            is ItemTeacherBinding -> {
//                Log.d("ItemTeacherBinding", "item : $item   position : $position")
//            }
//        }
//    }
//}