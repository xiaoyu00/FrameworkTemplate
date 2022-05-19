package com.framework.pay

import com.tencent.mm.opensdk.modelbase.BaseResp

interface PayCall {
    fun onSuccess()
    fun onError(errCode: Int, msg: String) {}
}