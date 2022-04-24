package com.framework.base.work.retrofit

import com.framework.base.data.LoginInfo
import okhttp3.OkHttpClient

object RetrofitClient : BaseRetrofitClient() {

    val service by lazy { getService(ApiService::class.java, "") }//ApiService.BASE_URL

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        builder.addInterceptor {
            val request = it.request().newBuilder().apply {
                addHeader(
                    "token",
                    if (LoginInfo.user == null || LoginInfo.user?.headUserId.isNullOrEmpty()) "" else LoginInfo.user!!.headUserId!!
                )
            }.build()
            it.proceed(request)
        }
    }

}