package com.framework.base.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View

/**
 * @author yu
 * @version 1.0
 * @date 2021/4/14
 * @description
 */
object CommonUtils {

    private var lastId = 0
    private var lastTimeStamp: Long = 0

    // 防连击
    fun isValidClick(view: View): Boolean {
        val time = System.currentTimeMillis()
        val valid = view.id != lastId || time - lastTimeStamp > 500
        lastId = view.id
        lastTimeStamp = time
        return valid
    }

    fun copyString(context: Context, value: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(value, value)
        clipboard.setPrimaryClip(clipData);
    }

    // 获取随机数【length 随机数长度】
    fun getRandomString(length: Int): String {
        val allowedChars = "0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}

