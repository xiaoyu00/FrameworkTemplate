package com.framework.template.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.framework.base.config.ALI_PID
import com.framework.base.config.ALI_SIGN
import com.framework.pay.*
import com.framework.pay.`interface`.AliPlatformCall
import com.framework.pay.`interface`.WeChatPlatformCall
import com.framework.template.R
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayReq
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

/**
 * 支付（微信，支付宝）
 */
class PayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        WeChatPlatform.init(this)//项目应放到Application里
        WeChatPlatform.setPlatformCallBack(object : WeChatPlatformCall {
            override fun onPaySuccess(resp: BaseResp) {
            }

            override fun onError(errCode: Int, msg: String) {
            }
        })
        AliPlatform.setPlatformCall(object : AliPlatformCall {
            override fun onPaySuccess(result: String?) {
            }

            override fun onError(errCode: Int, msg: String) {
            }
        })
        findViewById<Button>(R.id.pay_wechat).setOnClickListener {
            WeChatPlatform.pay(
                "1900000109",
                "1101000000140415649af9fc314aa427",
                "1101000000140429eb40476f8896f4c9",
                "1398746574"
            )
        }
        findViewById<Button>(R.id.pay_ali).setOnClickListener {
            //返回订单信息后调用
            val orderInfo: String = AliPayUtil.buildOrderParam(
                AliPayUtil.buildOrderParamMap(
                    ALI_PID,
                    true
                )
            ) + "&sign=" + ALI_SIGN
            AliPlatform.pay(this, orderInfo)
        }
    }

}