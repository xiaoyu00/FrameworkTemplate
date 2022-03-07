package com.framework.base.work.retrofit

import java.io.Serializable


open class ResponseResultBean {
    open var code: Int? = null
    open val msg: String? = null
}

class ResponseDataBean<T> : ResponseResultBean(), Serializable {
    val data: T? = null
    val date: String? = null
}

class ResponseListBean<T> : ResponseResultBean(), Serializable {
    val total: Int? = null
    val rows: T? = null
}