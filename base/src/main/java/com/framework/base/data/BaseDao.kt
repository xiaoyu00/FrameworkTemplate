package com.framework.base.data

import androidx.room.*


@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * FROM table_breeding_base WHERE isOffline IN (:offLine) AND workUserId = :userId")
    fun getUploadBases(offLine: List<String>, userId: String): List<Base>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg base: Base)

    @Delete
    fun delete(vararg base: Base)

    @Update
    fun update(vararg base: Base)
}