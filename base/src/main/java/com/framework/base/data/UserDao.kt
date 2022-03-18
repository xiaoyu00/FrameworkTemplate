package com.framework.base.data

import androidx.room.*


@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * FROM table_user WHERE isOffline IN (:offLine) AND id = :userId")
    fun getUploadBases(offLine: List<String>, userId: String): List<UserInfo>
    @Transaction
    @Query("SELECT * FROM table_user WHERE id = :userId")
    fun getByIds(userId: Long): List<UserInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg base: UserInfo)

    @Delete
    fun delete(vararg base: UserInfo)

    @Update
    fun update(vararg base: UserInfo)
}