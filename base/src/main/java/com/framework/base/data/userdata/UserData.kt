package com.framework.base.data.userdata

import com.framework.base.data.AppDataBase
import com.framework.base.model.UserInfo
import com.framework.base.work.retrofit.ResponseDataBean
import com.framework.base.work.retrofit.STATE_ERROR
import com.framework.base.work.retrofit.STATE_SUCCESS

class UserData {
    private val userWorkData = UserWorkData()
    fun getLocalLands(baseId: Long): List<UserInfo> {
        val list = AppDataBase.appDataBase.userDao().getByIds(baseId)
        if (!list.isNullOrEmpty()) {
            return list
        }
        return emptyList()
    }

    suspend fun add(land: UserInfo, hasNet: Boolean = true): ResponseDataBean<Long> {
        val responseDataBean = ResponseDataBean<Long>()
        responseDataBean.code = STATE_SUCCESS
        if (hasNet) {
            val result = userWorkData.addLand(land)
            if (result != null) {
                if (result?.code == STATE_SUCCESS) {
                    AppDataBase.appDataBase.userDao().insert(land)
                }
                return result
            } else {
                responseDataBean.code = STATE_ERROR
            }
        } else {
            land.isOffline = 1
            AppDataBase.appDataBase.userDao().insert(land)
        }
        return responseDataBean
    }
}

