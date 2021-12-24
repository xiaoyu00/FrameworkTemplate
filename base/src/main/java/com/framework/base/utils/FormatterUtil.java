package com.framework.base.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FormatterUtil {

    public static final String TAG = NumberFormat.class.getSimpleName();
    private static NumberFormat priceFormatter;
    private static NumberFormat coinInputFormatter;
    private static NumberFormat currencyInputFormatter;
    private static NumberFormat percentFormatter;
    private static NumberFormat percentNoSignFormatter;
    private static NumberFormat percentFormatterDownOrUp;
    private static NumberFormat percentFormatterUp;
    private static NumberFormat percentNoSignFormatterTwo;
    private static NumberFormat percentNoFractionFormatter;
    private static NumberFormat percentTwoDecimalFormatter;
    private static NumberFormat percentOneDecimalFormatter;
    private static NumberFormat percentNoSignTwoDecimalFormatter;

    public static final double EPSION = 1e-13;
    public static Calendar calendarDay;
    public static String YYYY = "yyyy";
    public static String YYYYMM = "yyyy/MM";
    public static String MMDD = "MM/dd";

    private static MessageFormat messageFormat = new MessageFormat("{0}");
    public static String xDay = "{0} Day";
    public static String xDays = "{0} Days";
    public static String xm_xs = "{0}m {1}s";
    public static String xh_xm_xs = "{0}h {1}m {2}s";


    /**
     * 字符串拼接
     */
    public static String formatMessages(String pattern, String... arguments) {
        messageFormat.applyPattern(pattern);
        return messageFormat.format(arguments);
    }

    // 最多8位小数的CoinFormatter
    public static NumberFormat getCoinFormatter() {
        if (coinInputFormatter == null) {
            synchronized (FormatterUtil.class) {
                coinInputFormatter = NumberFormat.getNumberInstance(Locale.CHINA);
                coinInputFormatter.setMaximumFractionDigits(8);
                //千分符
                coinInputFormatter.setGroupingUsed(true);

            }
        }
        return coinInputFormatter;
    }

    // 最多fractionCount位小数的CoinFormatter
    public static NumberFormat getVariableFractionCoinFormatter(int fractionCount) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setMaximumFractionDigits(fractionCount);
        format.setGroupingUsed(false);
        return format;
    }

    //formatter整数
    public static String formatterInteger(int amount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CHINA);
        numberFormat.setGroupingUsed(true);
        return numberFormat.format(amount);
    }

    //formatter long
    public static String formatterInteger(long amount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CHINA);
        numberFormat.setGroupingUsed(false);
        return numberFormat.format(amount);
    }

    //formatter 最小两位整数
    public static String formatterIntegerDoubleDigits(long amount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CHINA);
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(2);
        return numberFormat.format(amount);
    }

    public static NumberFormat getCurrencyFormatter() {
        if (currencyInputFormatter == null) {
            synchronized (FormatterUtil.class) {
                currencyInputFormatter = NumberFormat.getNumberInstance(Locale.CHINA);
                // 法币只保留两位小数点，输入的时候
                currencyInputFormatter.setMaximumFractionDigits(2);
                currencyInputFormatter.setGroupingUsed(true);
            }
        }
        return currencyInputFormatter;
    }

    public static NumberFormat getPercentFormatter() {
        if (percentFormatter == null) {
            synchronized (FormatterUtil.class) {
                percentFormatter = NumberFormat.getPercentInstance(Locale.CHINA);
                //展示到小数点后两位
                percentFormatter.setMaximumFractionDigits(2);
                percentFormatter.setMinimumFractionDigits(2);
                percentFormatter.setGroupingUsed(false);

                // A little hack to add '+' to percent.
                DecimalFormat formatter = (DecimalFormat) percentFormatter;
                formatter.setPositivePrefix("+");
            }
        }
        return percentFormatter;
    }

    public static NumberFormat getPercentFormatterTwoDown() {
        if (percentFormatterDownOrUp == null) {
            synchronized (FormatterUtil.class) {
                percentFormatterDownOrUp = NumberFormat.getPercentInstance(Locale.CHINA);
                percentFormatterDownOrUp.setGroupingUsed(false);
                percentFormatterDownOrUp.setMaximumFractionDigits(2);
                percentFormatterDownOrUp.setMinimumFractionDigits(2);
                percentFormatterDownOrUp.setRoundingMode(RoundingMode.DOWN);
                DecimalFormat formatter = (DecimalFormat) percentFormatterDownOrUp;
                formatter.setPositivePrefix("+");
            }
        }
        return percentFormatterDownOrUp;
    }

    public static NumberFormat getPercentFormatterTwoDownNoSignDown() {
        if (percentNoSignFormatterTwo == null) {
            synchronized (FormatterUtil.class) {
                percentNoSignFormatterTwo = NumberFormat.getPercentInstance(Locale.CHINA);
                percentNoSignFormatterTwo.setGroupingUsed(false);
                percentNoSignFormatterTwo.setMaximumFractionDigits(2);
                percentNoSignFormatterTwo.setMinimumFractionDigits(2);
                percentNoSignFormatterTwo.setRoundingMode(RoundingMode.DOWN);
            }
        }
        return percentNoSignFormatterTwo;
    }

    //xx%,不保留小数,向下取整
    public static NumberFormat getPercentNoFractionFormatter() {
        if (percentNoFractionFormatter == null) {
            synchronized (FormatterUtil.class) {
                percentNoFractionFormatter = NumberFormat.getPercentInstance(Locale.CHINA);
                //不保留小数
                percentNoFractionFormatter.setGroupingUsed(false);
                percentNoFractionFormatter.setMaximumFractionDigits(0);
                percentNoFractionFormatter.setRoundingMode(RoundingMode.DOWN);
                DecimalFormat formatter = (DecimalFormat) percentNoFractionFormatter;
                formatter.setPositivePrefix("");
            }
        }
        return percentNoFractionFormatter;
    }

    //xx.xx%,保留两位小数,四舍五入
    public static NumberFormat getPercentTwoDecimalFormatter() {
        if (percentTwoDecimalFormatter == null) {
            synchronized (FormatterUtil.class) {
                percentTwoDecimalFormatter = NumberFormat.getPercentInstance(Locale.CHINA);
                //不保留小数
                percentTwoDecimalFormatter.setGroupingUsed(false);
                percentTwoDecimalFormatter.setMaximumFractionDigits(2);
                percentTwoDecimalFormatter.setRoundingMode(RoundingMode.HALF_UP);
                DecimalFormat formatter = (DecimalFormat) percentTwoDecimalFormatter;
                formatter.setPositivePrefix("");
            }
        }
        return percentTwoDecimalFormatter;
    }

    //xx.x%,保留一位小数,四舍五入
    public static NumberFormat getPercentOneDecimalFormatter() {
        if (percentOneDecimalFormatter == null) {
            synchronized (FormatterUtil.class) {
                percentOneDecimalFormatter = NumberFormat.getPercentInstance(Locale.CHINA);
                //不保留小数
                percentOneDecimalFormatter.setGroupingUsed(false);
                percentOneDecimalFormatter.setMaximumFractionDigits(1);
                percentOneDecimalFormatter.setMinimumFractionDigits(1);
                percentOneDecimalFormatter.setRoundingMode(RoundingMode.HALF_UP);
                DecimalFormat formatter = (DecimalFormat) percentOneDecimalFormatter;
                formatter.setPositivePrefix("");
            }
        }
        return percentOneDecimalFormatter;
    }

    public static NumberFormat getPercentNoSignFormatter() {
        if (percentNoSignFormatter == null) {
            synchronized (FormatterUtil.class) {
                percentNoSignFormatter = NumberFormat.getPercentInstance(Locale.CHINA);
                //最多展示到小数点后两位
                percentNoSignFormatter.setGroupingUsed(false);
                percentNoSignFormatter.setMaximumFractionDigits(2);
                DecimalFormat formatter = (DecimalFormat) percentNoSignFormatter;
                formatter.setPositivePrefix("");
            }
        }
        return percentNoSignFormatter;
    }

    public static NumberFormat getPercentNoSignTwoDecimalFormatter() {
        if (percentNoSignTwoDecimalFormatter == null) {
            synchronized (FormatterUtil.class) {
                percentNoSignTwoDecimalFormatter = NumberFormat.getPercentInstance(Locale.CHINA);
                //展示到小数点后两位
                percentNoSignTwoDecimalFormatter.setGroupingUsed(false);
                percentNoSignTwoDecimalFormatter.setMaximumFractionDigits(2);
                percentNoSignTwoDecimalFormatter.setMinimumFractionDigits(2);
                DecimalFormat formatter = (DecimalFormat) percentNoSignTwoDecimalFormatter;
                formatter.setPositivePrefix("");
            }
        }
        return percentNoSignTwoDecimalFormatter;
    }

    /**
     * 对时间戳进行更改
     * 单位---天
     * 时间戳--毫秒
     */
    public static long changeTimestamp(long time, int day) {
        if (calendarDay == null) {
            synchronized (FormatterUtil.class) {
                calendarDay = new GregorianCalendar();
            }
        }
        calendarDay.setTime(new Date(time));
        calendarDay.add(Calendar.DATE, day);
        return calendarDay.getTime().getTime();
    }

    /**
     * 对时间戳进行更改
     * 单位---分钟
     * 时间戳--秒
     */
    public static long changeTimeMinute(long time, int minute) {
        return time + minute * 60;
    }

    /**
     * 对时间戳进行更改
     * 单位---天
     * 时间戳--秒
     */
    public static long changeTimeDay(long time, int day) {
        return time + day * 60 * 60 * 24;
    }


    //获得手机设置的时区，例如：中国标准时间
    public static String getTimeZoneLong() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.LONG);
    }

    //获得手机设置的时区，例如：GMT+08:00
    public static String getTimeZoneShort() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
    }

    //金额固定两位小数的显示方法
    public static String formatCurrencyNoSignTwoDecimal(double amount) {
        final NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        return format.format(amount);
    }

    //金额最多两位小数的显示方法
    public static String formatCurrencyNoSignMaxTwoDecimal(double amount) {
        final NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(2);
        return format.format(amount);
    }

    public static String formatCoin(double count, RoundingMode roundingMode) {
        final NumberFormat format = getCoinFormatter();
        final RoundingMode oldRoundMode = format.getRoundingMode();
        format.setRoundingMode(roundingMode);
        final String coinCount = format.format(count);
        format.setRoundingMode(oldRoundMode);
        format.setGroupingUsed(false);
        return coinCount;
    }

    public static NumberFormat getCoinWithMaxFractionFormatter(RoundingMode roundingMode, int fraction) {
        final NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setMaximumFractionDigits(fraction);
        format.setRoundingMode(roundingMode);
        format.setGroupingUsed(false);
        return format;
    }

    public static String upperCase(final String text) {
        return text.toUpperCase(Locale.CHINA);
    }

    public static String lowerCase(final String text) {
        return text.toLowerCase(Locale.CHINA);
    }

    // 普通数字，最多保留maxFraction位小数的formatter
    public static NumberFormat getVariableFractionNumberFormatter(int maxFraction) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setMaximumFractionDigits(maxFraction);
        //设置了以后不会有千分位，如果不设置，默认是有的
        format.setGroupingUsed(false);
        return format;
    }

    // 设置整数最大、小数最大
    public static NumberFormat getMaxDigitNumberFormatter(int maximumInteger, int maximumFraction, boolean isRoundingModeUp) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        //设置了以后不会有千分位，如果不设置，默认是有的
        format.setGroupingUsed(false);
        format.setMaximumIntegerDigits(maximumInteger);
        format.setMaximumFractionDigits(maximumFraction);
        format.setRoundingMode(isRoundingModeUp ? RoundingMode.UP : RoundingMode.DOWN);
        DecimalFormat decimalFormat = (DecimalFormat) format;//开启了NumberFormat底层的DecimalFormat的BigDecimal模式
        decimalFormat.setParseBigDecimal(true);
        return format;
    }

    // 设置整数最大、小数最大、四舍五入
    public static NumberFormat getMaxDigitNumberFormatter(int maximumInteger, int maximumFraction) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        //设置了以后不会有千分位，如果不设置，默认是有的
        format.setGroupingUsed(false);
        format.setMaximumIntegerDigits(maximumInteger);
        format.setMaximumFractionDigits(maximumFraction);
        DecimalFormat decimalFormat = (DecimalFormat) format;//开启了NumberFormat底层的DecimalFormat的BigDecimal模式
        decimalFormat.setParseBigDecimal(true);
        return format;
    }

    // 设置整数最大、小数最大 ,小数最小
    public static NumberFormat getMaxMinDigitNumberFormatter(int maximumInteger, int maximumFraction, int minimumFraction, boolean isRoundingModeUp) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        //设置了以后不会有千分位，如果不设置，默认是有的
        format.setGroupingUsed(false);
        format.setMaximumIntegerDigits(maximumInteger);
        format.setMaximumFractionDigits(maximumFraction);
        format.setMinimumFractionDigits(minimumFraction);
        format.setRoundingMode(isRoundingModeUp ? RoundingMode.UP : RoundingMode.DOWN);
        DecimalFormat decimalFormat = (DecimalFormat) format;//开启了NumberFormat底层的DecimalFormat的BigDecimal模式
        decimalFormat.setParseBigDecimal(true);
        return format;
    }

    // 设置整数最大、小数最大 加EPSION
    public static String getMaxDigitNumberFormatter(double amount, int maximumInteger, int maximumFraction, boolean isRoundingModeUp) {
        return getMaxDigitNumberFormatter(maximumInteger, maximumFraction, isRoundingModeUp).format(isRoundingModeUp ? amount - EPSION : amount + EPSION);
    }

    //
    public static String number(float num, RoundingMode mode, int digits) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setGroupingUsed(true);
        format.setMaximumFractionDigits(digits);
        format.setRoundingMode(mode);
        return format.format(num);
    }

    /**
     * 金额（数量折合CNY）的规范：
     * 向上取整
     * 有千分位
     */
    public static String getCoinFormatterIsCNY(double cny) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setGroupingUsed(true);
        format.setMaximumFractionDigits(2);
        format.setRoundingMode(RoundingMode.UP);
        return format.format(cny);
    }

    public static String getCoinFormatterIsCNY(double cny, RoundingMode mode) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setGroupingUsed(true);
        format.setMaximumFractionDigits(2);
        format.setRoundingMode(mode);
        return format.format(cny);
    }

    public static String getCoinFormatterIsCNY(double cny, RoundingMode mode, int digits) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setGroupingUsed(true);
        format.setMaximumFractionDigits(digits);
        format.setRoundingMode(mode);
        return format.format(cny);
    }

    public static String getLiveFormatter(double cny) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setGroupingUsed(true);
        format.setMaximumFractionDigits(0);
        format.setRoundingMode(RoundingMode.UP);
        return format.format(cny);
    }

    /**
     * 金额（数量折合CNY）的规范：
     * 2018年12月11日 各操作流程数字格式规范 9+2
     * https://git.windimg.com/production/work-in-progress/issues/434
     * 向上取整
     */
    public static NumberFormat getCoinFormatterIsCNY() {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        //设置了以后不会有千分位，如果不设置，默认是有的
        format.setGroupingUsed(false);
        format.setMaximumIntegerDigits(9);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setRoundingMode(RoundingMode.UP);
        return format;
    }

    /**
     * 金额（数量折合CNY）的规范：
     * 向下取整
     */
    public static NumberFormat getCoinFormatterIsCNYDown() {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        //设置了以后不会有千分位，如果不设置，默认是有的
        format.setGroupingUsed(false);
        format.setMaximumIntegerDigits(9);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        format.setRoundingMode(RoundingMode.DOWN);
        return format;
    }

    /**
     * 数量（虚拟币数量）的规范：
     * 2018年12月11日 各操作流程数字格式规范  10+8  首页 资产列表页 币种详情页 币种选择页  行情页  交易详情页
     * https://git.windimg.com/production/work-in-progress/issues/434
     * 币种数量-向下取整
     */
    public static NumberFormat getCoinFormatterIsAMOUNT(double unitPrice) {
        if (unitPrice >= 100) {
            return getMaxDigitNumberFormatter(6, 8, false);
        } else if (unitPrice >= 0 && unitPrice < 100) {
            return getMaxDigitNumberFormatter(10, 8, false);
        }
        return getMaxDigitNumberFormatter(10, 8, false);
    }

    /**
     * 币种数量-向上取整
     */
    public static NumberFormat getCoinFormatterIsAMOUNTUp(double unitPrice) {
        if (unitPrice >= 100) {
            return getMaxDigitNumberFormatter(6, 8, true);
        } else if (unitPrice >= 0 && unitPrice < 100) {
            return getMaxDigitNumberFormatter(10, 8, true);
        }
        return getMaxDigitNumberFormatter(10, 8, true);
    }

    /**
     * 收益-向上取整 最多8位小数 最少8位小数
     */
    public static NumberFormat getIncomFormatterIsAMOUNTUp(double unitPrice) {
        if (unitPrice >= 100) {
            return getMaxMinDigitNumberFormatter(6, 8, 8, false);
        } else if (unitPrice >= 0 && unitPrice < 100) {
            return getMaxMinDigitNumberFormatter(10, 8, 8, false);
        }
        return getMaxMinDigitNumberFormatter(10, 8, 8, false);
    }

    /**
     * 收益-向上取整 最多8位小数 最少8位小数
     */
    public static NumberFormat getIncom12FormatterIsAMOUNTUp(double unitPrice) {
        if (unitPrice >= 100) {
            return getMaxMinDigitNumberFormatter(6, 12, 12, false);
        } else if (unitPrice >= 0 && unitPrice < 100) {
            return getMaxMinDigitNumberFormatter(10, 12, 12, false);
        }
        return getMaxMinDigitNumberFormatter(10, 12, 12, false);
    }

    /**
     * Add EPSILON before round down, to avoid float accuracy problem.
     *
     * @param count
     * @param unitPrice
     * @return
     */
    public static String formatCoinIsAMOUNT(double count, double unitPrice) {
        final NumberFormat format = getCoinFormatterIsAMOUNT(unitPrice);
        if (count == 0) {
            return format.format(count);
        }
        return format.format(count + EPSION);
    }

    /**
     * Sub EPSILON before round up, to avoid float accuracy problem.
     *
     * @param count
     * @param unitPrice
     * @return
     */
    public static String formatCoinIsAMOUNTUp(double count, double unitPrice) {
        final NumberFormat format = getCoinFormatterIsAMOUNTUp(unitPrice);
        if (count <= EPSION) {
            return format.format(count);
        }
        return format.format(count - EPSION);
    }

    /**
     * 向上取整 保留4位  带有百分号
     *
     * @return
     */
    public static NumberFormat getPercentNoSignFormatterUp() {
        if (percentFormatterUp == null) {
            synchronized (FormatterUtil.class) {
                percentFormatterUp = NumberFormat.getPercentInstance(Locale.CHINA);
                percentFormatterUp.setGroupingUsed(false);
                percentFormatterUp.setMaximumFractionDigits(2);//最多展示到小数点后两位
                percentFormatterUp.setRoundingMode(RoundingMode.UP);
                DecimalFormat formatter = (DecimalFormat) percentFormatterUp;
                formatter.setPositivePrefix("");
            }
        }
        return percentFormatterUp;
    }

    /**
     * 未发放预估收益 向上取整 最多12位 最小12位
     *
     * @param count
     * @param unitPrice
     * @return
     */
    public static String formatIncom12IsAMOUNTDown(double count, double unitPrice) {
        final NumberFormat format = getIncom12FormatterIsAMOUNTUp(unitPrice);
        if (count <= EPSION) {
            return format.format(count);
        }
        return format.format(count - EPSION);
    }


    /**
     * @param maximum 0 整数
     *                1 万-小数点省略一位
     *                2 亿-小数点省略两位
     */
    public static NumberFormat getPriceFormatter(int maximum) {
        if (priceFormatter == null) {
            synchronized (FormatterUtil.class) {
                priceFormatter = NumberFormat.getNumberInstance(Locale.CHINA);
                priceFormatter.setGroupingUsed(true);
            }
        }
        priceFormatter.setMaximumFractionDigits(maximum);
        return priceFormatter;
    }

    //两个double数字相除防止丢失精度
    public static Double doubleExcept(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, 4, RoundingMode.HALF_UP).doubleValue();
    }

    //两个double数字相乘防止丢失精度
    public static Double doubleMultiply(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }


    //两个Double数相减
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();

    }

    //两个Double数相加
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 手机号中间****
     */
    public static String phoneFormat(String phone) {
        if (TextUtils.isEmpty(phone)) return "";
        String str = "";
        for (int i = 0; i < phone.length() - 4; i++) {
            str += "*";
        }
        return str + phone.substring(phone.length() - 4);
    }


    /**
     * 手机号中间****
     */
    public static String phoneFormat(String phone, int length) {
        if (TextUtils.isEmpty(phone)) return "";
        String str = "";
        for (int i = 0; i < length; i++) {
            str += "*";
        }
        return phone.substring(0, phone.length() - 4 - length) + str + phone.substring(phone.length() - 4);
    }

    // Format成UTC相对时区
    public static String formatterZone(long timeUnixSecond, String timeZone) {
        TimeZone tz = TimeZone.getTimeZone(TextUtils.isEmpty(timeZone) ? "Asia/Shanghai" : timeZone);
        int hourDiff = tz.getOffset(timeUnixSecond * 1000) / 3600 / 1000;
        if (hourDiff > 0) {
            return String.format(Locale.ENGLISH, "UTC +%d", hourDiff);
        } else {
            return String.format(Locale.ENGLISH, "UTC %d", hourDiff);
        }
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
//    private void openGPSDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("请打开GPS连接")
//                .setMessage("为了提高定位的准确度，更好的为您服务，请打开GPS")
//                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //跳转到手机打开GPS页面
//                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        //设置完成后返回原来的界面
//                    }
//                })
//                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }).show();
//    }

}
