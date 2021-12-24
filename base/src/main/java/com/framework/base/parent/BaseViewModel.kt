package com.framework.base.parent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

/**
 * 界面数据状态枚举
 */
enum class LoadState {
    LOAD_LOADING,
    LOAD_FINISH,
    LOAD_FAIL
}

/**
 * 网络请求状态枚举
 */
enum class WorkState {
    LOADING,
    SUCCESS,
    ERROR
}

/**
 * ViewModel 基类
 */
open class BaseViewModel : ViewModel() {

    val loadState = MutableLiveData<LoadState>()
    
    val workState = MutableLiveData<WorkState>()

    val user: LiveData<String> = liveData {
        emit("")
    }
}