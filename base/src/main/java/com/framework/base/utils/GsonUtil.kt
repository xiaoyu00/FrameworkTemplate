package com.framework.base.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun <T> stringToGson(gsonString: String): T {
    val type = object : TypeToken<T>() {}.type
    return Gson().fromJson<T>(gsonString, type)
}

fun gsonToString(model: Any): String {
    return Gson().toJson(model)
}