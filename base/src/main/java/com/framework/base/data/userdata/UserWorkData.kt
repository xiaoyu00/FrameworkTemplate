package com.framework.base.data.userdata

import com.framework.base.model.UserInfo
import com.framework.base.work.retrofit.ResponseDataBean
import com.framework.base.work.retrofit.RetrofitClient
import com.framework.base.work.retrofit.STATE_SUCCESS
import retrofit2.await

class UserWorkData {

    suspend fun addLand(land: UserInfo): ResponseDataBean<Long>? {
        try {
            val result = RetrofitClient.service.addUserInfo(land)?.await()
            result?.let { responseDataBean ->
                if (responseDataBean.code == STATE_SUCCESS) {
                    land.isOffline = 1
                    result.data?.let {
                        land.id = it
                    }
                }
                return result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

