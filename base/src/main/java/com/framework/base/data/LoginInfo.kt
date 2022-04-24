package com.framework.base.data

import com.framework.base.model.UserInfo
import com.framework.base.utils.SharedPreferencesUtil
import com.google.gson.Gson

private const val LOGIN_INFO="login_info"
object LoginInfo {
    var user: UserInfo? = null
        get() {
            if (field == null) {
                val userString = SharedPreferencesUtil.getData(LOGIN_INFO, "")
                if ((userString as String).isNotEmpty()) {
                    field = Gson().fromJson(userString, UserInfo::class.java)
                }
            }
            return field
        }
        set(value) {
            val userString = Gson().toJson(value)
            SharedPreferencesUtil.putData(LOGIN_INFO, userString)
            field = value
        }

    fun clearUser() {
        SharedPreferencesUtil.removeData(LOGIN_INFO)
        user = null
    }
}