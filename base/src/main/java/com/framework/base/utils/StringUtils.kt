package com.framework.base.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.util.*

object StringUtils {

    /**
     * 截取字符串前8位
     */
    fun getSubString(s: String): String? {
        return s.substring(8, s.length)
    }

//    fun getRandomString(length: Int): String? {
//        val base = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM0123456789"
//        val random = Random()
//        val sb = StringBuffer()
//        for (i in 0 until length) {
//            val number = random.nextInt(base.length)
//            sb.append(base[number])
//        }
//        return sb.toString()
//    }

    /**
     * kotlin 写法
     */
    fun getRandomString(length: Int): String {
        val allowedChars = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM0123456789"
        return (0 until length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun base64ToBitmap(base64String: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val bitmapArray: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

}