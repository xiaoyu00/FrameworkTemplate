package com.framework.base.work

import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_BYTE_ARRAY
import okio.ByteString
import org.json.JSONObject
import java.io.File


fun buildRequestBody(contentType: MediaType, params: Any, method: HttpMethod): RequestBody? {
    if (method == HttpMethod.GET || method == HttpMethod.HEAD) {
        return null
    }
    return when (params) {
        is RequestBody -> params
        is String -> params.toRequestBody(contentType)
        is Map<*, *> -> params.toRequestBody(contentType)
        is ByteString -> params.toRequestBody(contentType)
        is ByteArray -> params.toRequestBody(contentType)
        is File -> params.asRequestBody(contentType)
        else -> null
    }
}

private fun Map<*, *>.toRequestBody(contentType: MediaType): RequestBody? {

    return when {
        "${contentType.type}/${contentType.subtype}" == "application/json" -> JSONObject(this).toString()
            .toRequestBody(contentType)
        contentType == "application/x-www-form-urlencoded".toMediaType() -> FormBody.Builder()
            .apply {
                this@toRequestBody.forEach {
                    add("${it.key}", "${it.value}")
                }
            }.build()
        contentType == MultipartBody.FORM -> MultipartBody.Builder().apply {
            setType(MultipartBody.FORM)
            addFormDataPart("title", "Square Logo")
            addFormDataPart(
                "image", "logo-square.png",
                File("docs/images/logo-square.png").asRequestBody()
            )
        }.build()
        else -> throw IllegalArgumentException("不支持该类型： $contentType")
    }
}