package com.framework.base.work.retrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface WorkCall<T : ResponseResultBean> : Callback<T> {
    override fun onResponse(
        call: Call<T>,
        response: Response<T>
    ) {
        response.body()?.let {
            parseData(it)
        }
    }

    fun parseData(result: T) {
        when (result.code) {
            STATE_SUCCESS -> onSuccess(result)
            else -> onFailed(result.code, result.msg)
        }
        onComplete()
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailed(STATE_ERROR, "")
        onComplete()
    }

    fun onSuccess(result: T?)
    fun onFailed(code: Int?, msg: String?)
    fun onComplete() {}
}

interface VerificationWorkCall<T : ResponseResultBean> : WorkCall<T> {
    fun onTokenVerificationFailed()
    fun onChangePwd()
    override fun parseData(result: T) {
        when (result.code) {
            STATE_SUCCESS -> onSuccess(result)
            STATE_VERIFICATION_FAILED -> onTokenVerificationFailed()
            STATE_CHANGE_PASSWORD -> onChangePwd()
            else -> onFailed(result.code, result.msg)
        }
        onComplete()
    }
}

const val STATE_SUCCESS = 200
const val STATE_VERIFICATION_FAILED = 401
const val STATE_CHANGE_PASSWORD = 301
const val STATE_ERROR = -1
