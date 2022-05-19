package com.framework.template.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.framework.base.config.ALI_PID
import com.framework.base.config.ALI_SIGN
import com.framework.pay.PayCall
import com.framework.pay.PayManager
import com.framework.template.R
import com.tencent.mm.opensdk.modelpay.PayReq
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

/**
 * textView 跑马灯效果
 */
class PayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        /**
         * 微信支付需要在Application里注册
         * api = WXAPIFactory.createWXAPI(this, WECHAT_APPID);
         * api.registerApp(Constants.APP_ID)
         */
        PayManager.init(this)//项目应放到Application里
        PayManager.setPayCallBack(object : PayCall {
            override fun onSuccess() {
            }

            override fun onError(errCode: Int, msg: String) {
            }
        })

        findViewById<Button>(R.id.pay_wechat).setOnClickListener {
            val request =  PayReq()

            request.appId = "wxd930ea5d5a258f4f";

            request.partnerId = "1900000109";

            request.prepayId= "1101000000140415649af9fc314aa427";

            request.packageValue = "Sign=WXPay";

            request.nonceStr= "1101000000140429eb40476f8896f4c9";

            request.timeStamp= "1398746574";

            request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
            PayManager.payForWeChat(request)
        }
        findViewById<Button>(R.id.pay_ali).setOnClickListener {
            //返回订单信息后调用
            val orderInfo: String = buildOrderParam(
                buildOrderParamMap(
                    ALI_PID,
                    true
                )
            ) + "&sign=" + ALI_SIGN
            PayManager.payForAli(this, orderInfo)
        }
    }

    /**
     * 构造支付订单参数列表
     */
    fun buildOrderParamMap(app_id: String, rsa2: Boolean): Map<String, String> {
        val keyValues: MutableMap<String, String> = HashMap()
        keyValues["app_id"] = app_id
        keyValues["biz_content"] =
            "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"0.01\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"" + getOutTradeNo() + "\"}"
        keyValues["charset"] = "utf-8"
        keyValues["method"] = "alipay.trade.app.pay"
        keyValues["sign_type"] = if (rsa2) "RSA2" else "RSA"
        keyValues["timestamp"] = "2016-07-29 16:55:53"
        keyValues["version"] = "1.0"
        return keyValues
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map
     * 支付订单参数
     * @return
     */
    fun buildOrderParam(map: Map<String, String?>): String? {
        val keys: List<String> = ArrayList(map.keys)
        val sb = StringBuilder()
        for (i in 0 until keys.size - 1) {
            val key = keys[i]
            val value = map[key]
            sb.append(buildKeyValue(key, value, true))
            sb.append("&")
        }
        val tailKey = keys[keys.size - 1]
        val tailValue = map[tailKey]
        sb.append(
            buildKeyValue(
                tailKey,
                tailValue,
                true
            )
        )
        return sb.toString()
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private fun buildKeyValue(key: String, value: String?, isEncode: Boolean): String? {
        val sb = StringBuilder()
        sb.append(key)
        sb.append("=")
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                sb.append(value)
            }
        } else {
            sb.append(value)
        }
        return sb.toString()
    }

    /**
     * 要求外部订单号必须唯一。
     * @return
     */
    private fun getOutTradeNo(): String? {
        val format = SimpleDateFormat("MMddHHmmss", Locale.getDefault())
        val date = Date()
        var key = format.format(date)
        val r = Random()
        key = key + r.nextInt()
        key = key.substring(0, 15)
        return key
    }
}