package com.framework.base.data

import com.framework.base.data.userdata.UserData
import com.framework.base.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 使用：在mainActivity
 * private fun initConfigData() {
        lifecycleScope.launch {
            DataManager.getLandsAndUpload()
        }
    }
 */
object DataManager {
    val userListData= ListLiveData<UserInfo>()
    val userData = UserData()

    suspend fun getLandsAndUpload() = withContext(Dispatchers.IO) {
        val landList = userData.getLocalLands(userId)
        if (!landList.isNullOrEmpty()) {
            landList.forEach { l ->
                userData.add(l)
            }
        }
    }
    suspend fun getLands(baseId: Long, hasNet: Boolean): List<Land> {
        if (hasNet) {
            userData.getLands(baseId)
        }
        return userData.getLocalLands(baseId)
    }
}