package com.framework.base.work

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception


object HttpExecute {
    private val OKHTTP_CLIENT = OkHttpClient()
    private val DEFAULT_CONFIG = 0
    private val DEFAULT_HEADERS = Headers.Builder().build()
    fun setHttpConfig() {

    }

    fun workExecute(
        url: String,
        params: Any,
        callback: WorkCall,
        headers: Headers = DEFAULT_HEADERS,
        method: HttpMethod = HttpMethod.GET
    ) {
        buildRequestBody(,)
        val body = getBody()
        val request: Request = Request.Builder().headers(headers).url(buildUrl(url, method, params))
            .method(method.name, body).build()
        OKHTTP_CLIENT.newCall(request).enqueue(callback)

    }

    private fun buildUrl(url: String, method: HttpMethod, params: Any) =
        url.toHttpUrl().let { httpUrl ->
            when (method) {
                HttpMethod.GET, HttpMethod.HEAD -> httpUrl.newBuilder().apply {
                    when (params) {
                        is String -> query(params)
                        is Map<*, *> -> params.forEach {
                            addQueryParameter("${it.key}", it.value?.toString())
                        }
                    }
                }.build()
                else -> httpUrl
            }
        }
}
