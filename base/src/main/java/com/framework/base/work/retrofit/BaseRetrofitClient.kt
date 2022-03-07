package com.framework.base.work.retrofit

import androidx.viewbinding.BuildConfig
import com.framework.base.data.UserInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient {

    companion object CLIENT {
        private const val TIME_OUT = 5
    }
    private val client: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .addInterceptor(getHttpLoggingInterceptor())
            .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        handleBuilder(builder)
//        builder.addInterceptor {
//            val request = it.request().newBuilder().apply {
//                addHeader(
//                    "token",
//                    if (UserInfo.user == null || UserInfo.user?.token.isNullOrEmpty()) "" else UserInfo.user!!.token!!
//                )
//                addHeader("appmac", CommonUtil.getDeviceUniqueId())
//            }.build()
//            it.proceed(request)
//        }
        builder.build()
    }
    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }
        return logging
    }

    abstract fun handleBuilder(builder: OkHttpClient.Builder)

    open fun <Service> getService(serviceClass: Class<Service>, baseUrl: String): Service {
        return Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(serviceClass)
    }
}
