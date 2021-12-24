package com.framework.base.work

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

// 处理器
interface RequestProcessor {
    fun onBeforeProcessor(params: Map<*, *>) {

    }

    fun onAfterProcessor() {

    }
}

class WorkCall : Callback {
    override fun onFailure(call: Call, e: IOException) {
    }

    override fun onResponse(call: Call, response: Response) {

    }

}