package com.framework.base.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or

object StringUtils {

    /**
     * 截取字符串前8位
     */
    fun getSubString(s: String): String? {
        return s.substring(8, s.length)
    }

//    fun getRandomString(length: Int): String? {
//        val base = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM0123456789"
//        val random = Random()
//        val sb = StringBuffer()
//        for (i in 0 until length) {
//            val number = random.nextInt(base.length)
//            sb.append(base[number])
//        }
//        return sb.toString()
//    }

    /**
     * kotlin 写法
     */
    fun getRandomString(length: Int): String {
        val allowedChars = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPASDFGHJKLZXCVBNM0123456789"
        return (0 until length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    // 根据Unicode编码判断中文汉字和符号
    private fun isChinese(c: Char): Boolean {
        val ub = Character.UnicodeBlock.of(c)
        return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
    }

    // 判断中文汉字和符号
    fun isChinese(strName: String): Boolean {
        val ch = strName.toCharArray()
        for (i in ch.indices) {
            val c = ch[i]
            if (isChinese(c)) {
                return true
            }
        }
        return false
    }

    /**
     * 下划线命名转为驼峰命名
     *
     * @param str 下划线命名格式
     * @return 驼峰命名格式
     */
    fun underScoreCase2CamelCase(str: String): String {
        if (!str.contains("_")) return str
        val sb = StringBuilder()
        val chars = str.toCharArray()
        var hitUnderScore = false
        sb.append(chars[0])
        for (i in 1 until chars.size) {
            val c = chars[i]
            if (c == '_') {
                hitUnderScore = true
            } else {
                if (hitUnderScore) {
                    sb.append(Character.toUpperCase(c))
                    hitUnderScore = false
                } else {
                    sb.append(c)
                }
            }
        }
        return sb.toString()
    }

    /**
     * 驼峰命名法转为下划线命名
     *
     * @param str 驼峰命名格式
     * @return 下划线命名格式
     */
    fun camelCase2UnderScoreCase(str: String): String {
        val sb = StringBuilder()
        val chars = str.toCharArray()
        for (i in chars.indices) {
            val c = chars[i]
            if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c))
            } else {
                sb.append(c)
            }
        }
        return sb.toString()
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param str 字符串
     * @return 字符串ASCII码
     */
    fun toASCII(str: String): String? {
        val strBuf = StringBuffer()
        val bGBK = str.toCharArray()
        for (c in bGBK) {
            strBuf.append(c.toInt())
        }
        return strBuf.toString()
    }

    /**
     * 将字符串转移为Unicode码
     * @param str 字符串
     * @return
     */
    fun toUnicode(str: String): String? {
        val strBuf = StringBuffer()
        val chars = str.toCharArray()
        for (i in chars.indices) {
            strBuf.append("\\u").append(Integer.toHexString(chars[i].toInt()))
        }
        return strBuf.toString()
    }

    /**
     * 将字符串转移为Unicode码
     * @param chars 字符数组
     * @return
     */
    fun toUnicodeString(chars: CharArray): String? {
        val strBuf = StringBuffer()
        for (i in chars.indices) {
            strBuf.append("\\u").append(Integer.toHexString(chars[i].toInt()))
        }
        return strBuf.toString()
    }


    /**
     * 描述：从输入流中获得String.
     *
     * @param is 输入流
     * @return 获得的String
     */
    fun convertStreamToString(`is`: InputStream): String? {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = java.lang.StringBuilder()
        var line: String? = null
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(
                    """
                    $line
                    
                    """.trimIndent()
                )
            }

            // 最后一个\n删除
            if (sb.indexOf("\n") != -1
                && sb.lastIndexOf("\n") == sb.length - 1
            ) {
                sb.delete(sb.lastIndexOf("\n"), sb.lastIndexOf("\n") + 1)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }

    /**
     * 获取大小的描述.
     *
     * @param size 字节个数
     * @return 大小的描述
     */
    fun getSizeDesc(size: Long): String? {
        var size = size
        var suffix = "B"
        if (size >= 1024) {
            suffix = "K"
            size = size shr 10
            if (size >= 1024) {
                suffix = "M"
                // size /= 1024;
                size = size shr 10
                if (size >= 1024) {
                    suffix = "G"
                    size = size shr 10
                    // size /= 1024;
                }
            }
        }
        return size.toString() + suffix
    }

    /**
     * 描述：ip地址转换为10进制数.
     *
     * @param ip the ip
     * @return the long
     */
    fun ip2int(ip: String): Long {
        var ip = ip
        ip = ip.replace(".", ",")
        val items = ip.split(",").toTypedArray()
        return java.lang.Long.valueOf(items[0]) shl 24 or (java.lang.Long.valueOf(items[1]) shl 16
                ) or (java.lang.Long.valueOf(items[2]) shl 8) or java.lang.Long.valueOf(items[3])
    }

    /**
     * 获取UUID
     *
     * @return 32UUID小写字符串
     */
    fun gainUUID(): String? {
        var strUUID = UUID.randomUUID().toString()
        strUUID = strUUID.replace("-".toRegex(), "").toLowerCase()
        return strUUID
    }
    fun isEmpty(cs: CharSequence?): Boolean {
        return cs == null || cs.isEmpty()
    }
    /**
     * 手机号码，中间4位星号替换
     *
     * @param phone 手机号
     * @return 星号替换的手机号
     */
    fun phoneNoHide(phone: String): String? {
        // 括号表示组，被替换的部分$n表示第n组的内容
        // 正则表达式中，替换字符串，括号的意思是分组，在replace()方法中，
        // 参数二中可以使用$n(n为数字)来依次引用模式串中用括号定义的字串。
        // "(\d{3})\d{4}(\d{4})", "$1****$2"的这个意思就是用括号，
        // 分为(前3个数字)中间4个数字(最后4个数字)替换为(第一组数值，保持不变$1)(中间为*)(第二组数值，保持不变$2)
        return phone.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
    }

    /**
     * 银行卡号，保留最后4位，其他星号替换
     *
     * @param cardId 卡号
     * @return 星号替换的银行卡号
     */
    fun cardIdHide(cardId: String): String? {
        return cardId.replace("\\d{15}(\\d{3})".toRegex(), "**** **** **** **** $1")
    }

    /**
     * 身份证号，中间10位星号替换
     *
     * @param id 身份证号
     * @return 星号替换的身份证号
     */
    fun idHide(id: String): String? {
        return id.replace("(\\d{4})\\d{10}(\\d{4})".toRegex(), "$1** **** ****$2")
    }
}