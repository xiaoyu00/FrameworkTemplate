package com.framework.base.utils

/**
 * 验证
 */
object VerificationUtils {
    // 判断是否符合身份证号码的规范
    fun isIDCard(idCard: String?): Boolean {
        if (idCard != null) {
            Regex("")
            val idCardRegex = Regex("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)")
            return idCard.matches(idCardRegex)
        }
        return false
    }

    // 判断是否符合手机号码的规范
    fun isPhoneNumber(phone: String?): Boolean {
        //^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$ 精确
        if (phone != null) {
            Regex("")
            val idCardRegex = Regex("(^1[3|4|5|7|8][0-9]\\d{4,8}\$)")
            return phone.matches(idCardRegex)
        }
        return false
    }
}