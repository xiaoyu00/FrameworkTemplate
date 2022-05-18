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

    // 根据Unicode编码判断中文汉字和符号
    private fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
    }

    // 判断中文汉字和符号
    fun isChinese(strName: String): Boolean {
        val ch = strName.toCharArray()
        for (i in ch.indices) {
            val c = ch[i]
            if (isChinese(c)) {
                return true
            }
        }
        return false
    }
}