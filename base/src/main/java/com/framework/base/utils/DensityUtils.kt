package com.framework.base.utils

import android.content.Context
import android.util.TypedValue

/**
 * 单位转换
 */
object DensityUtils {
    /**
     * 将dp值转换为px值
     */
    fun dpToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 将px值转换为dp值
     */
    fun pxToDp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp转px
     *
     * @param context
     * @param val
     * @return
     */
    fun spTopx(context: Context, spVal: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, spVal,
            context.resources.displayMetrics
        )
    }

    /**
     * px转sp
     *
     * @param fontScale
     * @param pxVal
     * @return
     */
    fun pxToSp(context: Context, pxVal: Float): Float {
        return pxVal / context.resources.displayMetrics.scaledDensity
    }
}