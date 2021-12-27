package com.framework.base.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PictureConverter {
    @TypeConverter
    fun stringToList(peoplesString: String): List<Picture> {
        val type = object : TypeToken<List<Picture>>() {

        }.type
        return Gson().fromJson(peoplesString, type)
    }

    @TypeConverter
    fun listToString(peoples: List<Picture>?): String {
        if (peoples.isNullOrEmpty()) {
            return "[]"
        }
        val gson = Gson()
        return gson.toJson(peoples)
    }
}