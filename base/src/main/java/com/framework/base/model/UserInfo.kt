package com.framework.base.model

import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "table_user", primaryKeys = ["id"])
data class UserInfo(
    var id: Long,
    var isOffline: Int? = 0,
    var researchLaboratory: Long? = null,
    var baseName: String? = null,
    var area: String? = null,
    var gdRings: String? = null,
    var location: String? = null,
    var x: String? = null,
    var y: String? = null,
    var status: String? = null,
    var headUserId: String? = null,
    var avatar: List<Picture>? = null,
    var headUserName: String? = null,
    var type: Int? = 0
) : Serializable

data class Picture(
    var id: Long,
    var path: String? = null,
) : Serializable