package com.framework.base.data

import android.content.Context
import androidx.annotation.MainThread
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.framework.base.model.UserInfo

@Database(
    entities = [UserInfo::class],
    version = 1
)
@TypeConverters(
    PictureConverter::class,
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        lateinit var appDataBase: AppDataBase
        private val MIGRATION = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE table_breeding_base "
                            + " RENAME COLUMN region TO area"
                )
                database.execSQL(
                    "ALTER TABLE table_breeding_laboratory "
                            + " ADD COLUMN createDate TEXT"
                )
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE table_breeding_monitor "
                            + " RENAME COLUMN filId TO plantId"
                )
            }
        }

        @MainThread
        fun initDataBase(context: Context): AppDataBase {
            appDataBase = if (::appDataBase.isInitialized) appDataBase else Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "breeding_database"
            ).addMigrations(MIGRATION, MIGRATION_3_4).fallbackToDestructiveMigration().build()
            return appDataBase
        }
    }

}