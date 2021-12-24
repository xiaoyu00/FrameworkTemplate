package com.framework.base.work

import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody

class OkHttpDo1 {
    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }
    fun doPost(){

        "".toRequestBody()
    }

}