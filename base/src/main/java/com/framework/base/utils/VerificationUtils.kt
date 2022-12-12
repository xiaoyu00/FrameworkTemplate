package com.framework.base.utils

import android.text.TextUtils
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern

/**
 * 数据验证工具类
 */
object VerificationUtils {


    //邮箱表达式
    private val email_pattern =
        Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")

    //银行卡号表达式
    private val bankNo_pattern = Pattern.compile("^[0-9]{16,19}$")

    //座机号码表达式
    private val plane_pattern =
        Pattern.compile("^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$")

    //非零表达式
    private val notZero_pattern = Pattern.compile("^\\+?[1-9][0-9]*$")

    //数字表达式
    private val number_pattern = Pattern.compile("^[0-9]*$")

    //大写字母表达式
    private val upChar_pattern = Pattern.compile("^[A-Z]+$")

    //小写字母表达式
    private val lowChar_pattern = Pattern.compile("^[a-z]+$")

    //大小写字母表达式
    private val letter_pattern = Pattern.compile("^[A-Za-z]+$")

    //中文汉字表达式
    private val chinese_pattern = Pattern.compile("^[\u4e00-\u9fa5],{0,}$")

    //条形码表达式
    private val onecode_pattern = Pattern.compile("^(([0-9])|([0-9])|([0-9]))\\d{10}$")

    //邮政编码表达式
    private val postalcode_pattern = Pattern.compile("([0-9]{3})+.([0-9]{4})+")

    //IP地址表达式
    private val ipaddress_pattern =
        Pattern.compile("[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))")

    //URL地址表达式
    private val url_pattern =
        Pattern.compile("(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?")

    //用户名表达式
    private val username_pattern = Pattern.compile("^[A-Za-z0-9_]{1}[A-Za-z0-9_.-]{3,31}")

    //真实姓名表达式
    private val realnem_pattern = Pattern.compile("[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*")

    //匹配HTML标签,通过下面的表达式可以匹配出HTML中的标签属性。
    private val html_patter =
        Pattern.compile("<\\\\/?\\\\w+((\\\\s+\\\\w+(\\\\s*=\\\\s*(?:\".*?\"|'.*?'|[\\\\^'\">\\\\s]+))?)+\\\\s*|\\\\s*)\\\\/?>")

    //抽取注释,如果你需要移除HMTL中的注释，可以使用如下的表达式。
    private val notes_patter = Pattern.compile("<!--(.*?)-->")

    //查找CSS属性,通过下面的表达式，可以搜索到相匹配的CSS属性。
    private val css_patter =
        Pattern.compile("^\\\\s*[a-zA-Z\\\\-]+\\\\s*[:]{1}\\\\s[a-zA-Z0-9\\\\s.#]+[;]{1}")

    //提取页面超链接,提取html中的超链接。
    private val hyperlink_patter =
        Pattern.compile("(<a\\\\s*(?!.*\\\\brel=)[^>]*)(href=\"https?:\\\\/\\\\/)((?!(?:(?:www\\\\.)?'.implode('|(?:www\\\\.)?', \$follow_list).'))[^\"]+)\"((?!.*\\\\brel=)[^>]*)(?:[^>]*)>")

    //提取网页图片,假若你想提取网页中所有图片信息，可以利用下面的表达式。
    private val image_patter =
        Pattern.compile("\\\\< *[img][^\\\\\\\\>]*[src] *= *[\\\\\"\\\\']{0,1}([^\\\\\"\\\\'\\\\ >]*)")

    //提取Color Hex Codes,有时需要抽取网页中的颜色代码，可以使用下面的表达式。
    private val color_patter = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")

    //文件路径及扩展名校验,验证windows下文件路径和扩展名（下面的例子中为.txt文件）
    private val route_patter =
        Pattern.compile("^([a-zA-Z]\\\\:|\\\\\\\\)\\\\\\\\([^\\\\\\\\]+\\\\\\\\)*[^\\\\/:*?\"<>|]+\\\\.txt(l)?$")

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


    //提取URL链接,下面的这个表达式可以筛选出一段文本中的URL
    // ^(f|ht){1}(tp|tps):\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w- ./?%&=]*)?
    //检查URL的前缀,应用开发中很多时候需要区分请求是HTTPS还是HTTP，通过下面的表达式可以取出一个url的前缀然后再逻辑判断。
//if (!s.match(/^[a-zA-Z]+:\\/\\//))
//	{
//		s = 'http://' + s;
//	}
    //校验IP-v6地址
//	(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))
//校验IP-v4地址
//	\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b
//	判断IE的版本
//	^.*MSIE [5-8](?:\\.[0-9]+)?(?!.*Trident\\/[5-9]\\.0).*$
//	校验金额
//^[0-9]+(.[0-9]{2})?$
//	校验密码强度
//^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$


    //提取URL链接,下面的这个表达式可以筛选出一段文本中的URL
    // ^(f|ht){1}(tp|tps):\\/\\/([\\w-]+\\.)+[\\w-]+(\\/[\\w- ./?%&=]*)?
    //检查URL的前缀,应用开发中很多时候需要区分请求是HTTPS还是HTTP，通过下面的表达式可以取出一个url的前缀然后再逻辑判断。
    //if (!s.match(/^[a-zA-Z]+:\\/\\//))
    //	{
    //		s = 'http://' + s;
    //	}
    //校验IP-v6地址
    //	(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))
    //校验IP-v4地址
    //	\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b
    //	判断IE的版本
    //	^.*MSIE [5-8](?:\\.[0-9]+)?(?!.*Trident\\/[5-9]\\.0).*$
    //	校验金额
    //^[0-9]+(.[0-9]{2})?$
    //	校验密码强度
    //^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$
    /**
     * 获取身份证号所有区域编码设置
     *
     * @return Hashtable
     */
    private fun getAreaCodeAll(): Hashtable<String, String> {
        val hashtable: Hashtable<String, String> = Hashtable<String, String>()
        hashtable["11"] = "北京"
        hashtable["12"] = "天津"
        hashtable["13"] = "河北"
        hashtable["14"] = "山西"
        hashtable["15"] = "内蒙古"
        hashtable["21"] = "辽宁"
        hashtable["22"] = "吉林"
        hashtable["23"] = "黑龙江"
        hashtable["31"] = "上海"
        hashtable["32"] = "江苏"
        hashtable["33"] = "浙江"
        hashtable["34"] = "安徽"
        hashtable["35"] = "福建"
        hashtable["36"] = "江西"
        hashtable["37"] = "山东"
        hashtable["41"] = "河南"
        hashtable["42"] = "湖北"
        hashtable["43"] = "湖南"
        hashtable["44"] = "广东"
        hashtable["45"] = "广西"
        hashtable["46"] = "海南"
        hashtable["50"] = "重庆"
        hashtable["51"] = "四川"
        hashtable["52"] = "贵州"
        hashtable["53"] = "云南"
        hashtable["54"] = "西藏"
        hashtable["61"] = "陕西"
        hashtable["62"] = "甘肃"
        hashtable["63"] = "青海"
        hashtable["64"] = "宁夏"
        hashtable["65"] = "新疆"
        hashtable["71"] = "台湾"
        hashtable["81"] = "香港"
        hashtable["82"] = "澳门"
        hashtable["91"] = "国外"
        return hashtable
    }


    /**
     * 根据身份号返回所在区域信息
     *
     * @param idCard
     * @return String
     */
    fun getIDCardArea(idCard: String): String? {
        val ht: Hashtable<String, String> = getAreaCodeAll()
        return ht[idCard.substring(0, 2)]
    }


    /**
     * 56名族定义
     *
     * @return Hashtable
     */
    fun getMinorityAll(): Hashtable<String, String> {
        val hashtable: Hashtable<String, String> = Hashtable<String, String>()
        hashtable["汉族"] = "汉族"
        hashtable["壮族"] = "壮族"
        hashtable["满族"] = "满族"
        hashtable["回族"] = "回族"
        hashtable["苗族"] = "苗族"
        hashtable["维吾尔族"] = "维吾尔族"
        hashtable["土家族"] = "土家族"
        hashtable["彝族"] = "彝族"
        hashtable["蒙古族"] = "蒙古族"
        hashtable["藏族"] = "藏族"
        hashtable["布依族"] = "布依族"
        hashtable["侗族"] = "侗族"
        hashtable["瑶族"] = "瑶族"
        hashtable["朝鲜族"] = "朝鲜族"
        hashtable["白族"] = "白族"
        hashtable["哈尼族"] = "哈尼族"
        hashtable["哈萨克族"] = "哈萨克族"
        hashtable["黎族"] = "黎族"
        hashtable["傣族"] = "傣族"
        hashtable["畲族"] = "畲族"
        hashtable["傈僳族"] = "傈僳族"
        hashtable["仡佬族"] = "仡佬族"
        hashtable["东乡族"] = "东乡族"
        hashtable["高山族"] = "高山族"
        hashtable["拉祜族"] = "拉祜族"
        hashtable["水族"] = "水族"
        hashtable["佤族"] = "佤族"
        hashtable["纳西族"] = "纳西族"
        hashtable["羌族"] = "羌族"
        hashtable["土族"] = "土族"
        hashtable["仫佬族"] = "仫佬族"
        hashtable["锡伯族"] = "锡伯族"
        hashtable["柯尔克孜族"] = "柯尔克孜族"
        hashtable["达斡尔族"] = "达斡尔族"
        hashtable["景颇族"] = "景颇族"
        hashtable["毛南族"] = "毛南族"
        hashtable["撒拉族"] = "撒拉族"
        hashtable["布朗族"] = "布朗族"
        hashtable["塔吉克族"] = "塔吉克族"
        hashtable["阿昌族"] = "阿昌族"
        hashtable["普米族"] = "普米族"
        hashtable["鄂温克族"] = "鄂温克族"
        hashtable["怒族"] = "怒族"
        hashtable["京族"] = "京族"
        hashtable["基诺族"] = "基诺族"
        hashtable["德昂族"] = "德昂族"
        hashtable["保安族"] = "保安族"
        hashtable["俄罗斯族"] = "俄罗斯族"
        hashtable["裕固族"] = "裕固族"
        hashtable["乌孜别克族"] = "乌孜别克族"
        hashtable["门巴族"] = "门巴族"
        hashtable["鄂伦春族"] = "鄂伦春族"
        hashtable["独龙族"] = "独龙族"
        hashtable["塔塔尔族"] = "塔塔尔族"
        hashtable["赫哲族"] = "赫哲族"
        hashtable["珞巴族"] = "珞巴族"
        return hashtable
    }

    /**
     * 验证非零正整数
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isNotZero(str: String?): Boolean {
        return notZero_pattern.matcher(str).matches()
    }


    /**
     * 验证是数字
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isNumber(str: String?): Boolean {
        return number_pattern.matcher(str).matches()
    }


    /**
     * 验证是大写字母
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isUpChar(str: String?): Boolean {
        return upChar_pattern.matcher(str).matches()
    }


    /**
     * 验证是小写字母
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isLowChar(str: String?): Boolean {
        return lowChar_pattern.matcher(str).matches()
    }


    /**
     * 验证是英文字母
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isLetter(str: String?): Boolean {
        return letter_pattern.matcher(str).matches()
    }


    /**
     * 验证输入汉字
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isChinese(str: String?): Boolean {
        return chinese_pattern.matcher(str).matches()
    }


    /**
     * 验证真实姓名
     *
     * @param str 验证字符
     * @return
     */
    fun isRealName(str: String?): Boolean {
        return realnem_pattern.matcher(str).matches()
    }


    /**
     * 验证是否是条形码
     *
     * @param oneCode 条形码
     * @return boolean
     */
    fun isOneCode(oneCode: String?): Boolean {
        return onecode_pattern.matcher(oneCode).matches()
    }


    /**
     * 是否含有特殊符号
     *
     * @param str 待验证的字符串
     * @return 是否含有特殊符号
     */
    fun hasSpecialCharacter(str: String?): Boolean {
        val regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(str)
        return m.find()
    }


    /**
     * 验证邮箱是否正确
     *
     * @param email 邮箱地址
     * @return boolean
     */
    fun isEmail(email: String?): Boolean {
        return email_pattern.matcher(email).matches()
    }


    /**
     * 验证座机号码是否正确
     *
     * @param plane 座机号码
     * @return boolean
     */
    fun isPlane(plane: String?): Boolean {
        return plane_pattern.matcher(plane).matches()
    }


    /**
     * 验证邮政编码是否正确
     *
     * @param postalcode 邮政编码
     * @return boolean
     */
    fun isPostalCode(postalcode: String?): Boolean {
        return postalcode_pattern.matcher(postalcode).matches()
    }


    /**
     * 验证IP地址是否正确
     *
     * @param ipaddress IP地址
     * @return boolean
     */
    fun isIpAddress(ipaddress: String?): Boolean {
        return ipaddress_pattern.matcher(ipaddress).matches()
    }


    /**
     * 验证URL地址是否正确
     *
     * @param url 地址
     * @return boolean
     */
    fun isURL(url: String?): Boolean {
        return url_pattern.matcher(url).matches()
    }


    /**
     * 验证是否是正整数
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isInteger(str: String?): Boolean {
        return try {
            Integer.valueOf(str)
            true
        } catch (e: Exception) {
            false
        }
    }


    /**
     * 验证是否是小数
     *
     * @param paramString 验证字符
     * @return boolean
     */
    fun isPoint(paramString: String): Boolean {
        if (paramString.indexOf(".") > 0) {
            if (paramString.substring(paramString.indexOf(".")).length > 3) {
                return false
            }
        }
        return true
    }


    /**
     * 验证是否银行卡号
     *
     * @param bankNo 银行卡号
     * @return
     */
    fun isBankNo(bankNo: String): Boolean {
        //替换空格
        var bankNo = bankNo
        bankNo = bankNo.replace(" ".toRegex(), "")
        //银行卡号可为12位数字
        return if (12 == bankNo.length) {
            true
        } else bankNo_pattern.matcher(bankNo).matches()
        //银行卡号可为16-19位数字
    }


    /**
     * 判断是否有特殊字符
     *
     * @param str 验证字符
     * @return boolean
     */
    fun isPeculiarStr(str: String): Boolean {
        var flag = false
        val regEx = "[^0-9a-zA-Z\u4e00-\u9fa5]+"
        if (str.length != str.replace(regEx.toRegex(), "").length) {
            flag = true
        }
        return flag
    }


    /**
     * 判断是否为用户名账号(规则如下：用户名由下划线或字母开头，由数字、字母、下划线、点、减号组成的4-32位字符)
     *
     * @param username 用户名
     * @return boolean
     */
    fun isUserName(username: String?): Boolean {
        return username_pattern.matcher(username).matches()
    }

    /**
     * 获取字符串中文字符的长度（每个中文算2个字符）.
     *
     * @param str 指定的字符串
     * @return 中文字符的长度
     */
    fun chineseLength(str: String?): Int {
        var valueLength = 0
        val chinese = Regex("[\u0391-\uFFE5]")
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */if (str != null && str != "") {
            for (i in str.indices) {
                /* 获取一个字符 */
                val temp = str.substring(i, i + 1)
                /* 判断是否为中文字符 */if (temp.matches(chinese)) {
                    valueLength += 2
                }
            }
        }
        return valueLength
    }

    /**
     * 描述：是否只是字母和数字.
     *
     * @param str 指定的字符串
     * @return 是否只是字母和数字:是为true，否则false
     */
    fun isNumberLetter(str: String): Boolean? {
        var isNoLetter = false
        val expr = Regex("^[A-Za-z0-9]+$")
        if (str.matches(expr)) {
            isNoLetter = true
        }
        return isNoLetter
    }

    /**
     * 描述：是否包含中文.
     *
     * @param str 指定的字符串
     * @return 是否包含中文:是为true，否则false
     */
    fun isContainChinese(str: String?): Boolean? {
        var isChinese = false
        val chinese = Regex("[\u0391-\uFFE5]")
        if (str != null && str != "") {
            // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
            for (i in str.indices) {
                // 获取一个字符
                val temp = str.substring(i, i + 1)
                // 判断是否为中文字符
                if (temp.matches(chinese)) {
                    isChinese = true
                } else {
                }
            }
        }
        return isChinese
    }


    /**
     * 是否为车牌号（沪A88888）
     *
     * @param vehicleNo 车牌号
     * @return 是否为车牌号
     */
    fun checkVehicleNo(vehicleNo: String?): Boolean {
        val pattern = Pattern.compile("^[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{5}$")
        return pattern.matcher(vehicleNo).find()
    }

//	/**
//	 * 匹配中国邮政编码
//	 *
//	 * @param postcode 邮政编码
//	 * @return 验证成功返回true，验证失败返回false
//	 */
//	public static boolean checkPostcode(String postcode) {
//		String regex = "[1-9]\\d{5}";
//		return Pattern.matches(regex, postcode);
//	}


    //	/**
    //	 * 匹配中国邮政编码
    //	 *
    //	 * @param postcode 邮政编码
    //	 * @return 验证成功返回true，验证失败返回false
    //	 */
    //	public static boolean checkPostcode(String postcode) {
    //		String regex = "[1-9]\\d{5}";
    //		return Pattern.matches(regex, postcode);
    //	}
    /**
     * 判断字符串是否为连续数字 45678901等
     *
     * @param str 待验证的字符串
     * @return 是否为连续数字
     */
    fun isContinuousNum(str: String): Boolean {
        if (TextUtils.isEmpty(str)) return false
        if (!isNumber(str)) return true
        val len = str.length
        for (i in 0 until len - 1) {
            val curChar = str[i]
            var verifyChar = (curChar.toInt() + 1).toChar()
            if (curChar == '9') verifyChar = '0'
            val nextChar = str[i + 1]
            if (nextChar != verifyChar) {
                return false
            }
        }
        return true
    }


    /**
     * 是否是纯字母
     *
     * @param str 待验证的字符串
     * @return 是否是纯字母
     */
    fun isAlphaBetaString(str: String?): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        val p = Pattern.compile("^[a-zA-Z]+$") // 从开头到结尾必须全部为字母或者数字
        val m = p.matcher(str)
        return m.find()
    }

    /**
     * 判断字符串是否为连续字母 xyZaBcd等
     *
     * @param str 待验证的字符串
     * @return 是否为连续字母
     */
    fun isContinuousWord(str: String): Boolean {
        if (TextUtils.isEmpty(str)) return false
        if (!isAlphaBetaString(str)) return true
        val len = str.length
        val local = str.toLowerCase()
        for (i in 0 until len - 1) {
            val curChar = local[i]
            var verifyChar = (curChar.toInt() + 1).toChar()
            if (curChar == 'z') verifyChar = 'a'
            val nextChar = local[i + 1]
            if (nextChar != verifyChar) {
                return false
            }
        }
        return true
    }

    /**
     * 是否是日期
     * 20120506 共八位，前四位-年，中间两位-月，最后两位-日
     *
     * @param date    待验证的字符串
     * @param yearlen yearlength
     * @return 是否是真实的日期
     */
    fun isRealDate(date: String?, yearlen: Int): Boolean {
        val len = 4 + yearlen
        if (date == null || date.length != len) return false
        if (!date.matches(Regex("[0-9]+"))) return false
        val year = date.substring(0, yearlen).toInt()
        val month = date.substring(yearlen, yearlen + 2).toInt()
        val day = date.substring(yearlen + 2, yearlen + 4).toInt()
        if (year <= 0) return false
        if (month <= 0 || month > 12) return false
        return if (day <= 0 || day > 31) false else when (month) {
            4, 6, 9, 11 -> if (day > 30) false else true
            2 -> {
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) return if (day > 29) false else true
                if (day > 28) false else true
            }
            else -> true
        }
    }
}