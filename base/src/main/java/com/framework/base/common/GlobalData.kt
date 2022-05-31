package com.framework.base.common

import android.content.Context

object GlobalData {
    lateinit var applicationContext: Context
    fun init(context: Context) {
        applicationContext = context
    }
}