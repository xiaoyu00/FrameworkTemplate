package com.framework.base.utils

import java.text.NumberFormat

/**
 * @author yu
 * @version 1.0
 * @date 2021/4/8
 * @description 格式化数据
 */
object FormatUtils {
    /**
     * 格式化double
     * @param d 格式化数据
     * @return string字符串
     */
    fun formatDouble(d: Double): String {
        val nf = NumberFormat.getInstance()
        nf.isGroupingUsed = false
        return nf.format(d)
    }

    /**
     * 格式化double字符串 保留俩位小数
     * @return string字符串
     */
    fun formatStringDecimal(doubleString: String): String {
        return String.format("%.2f", doubleString.toDouble())
    }

}