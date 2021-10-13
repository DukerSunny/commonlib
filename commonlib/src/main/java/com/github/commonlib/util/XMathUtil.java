package com.github.commonlib.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @title 数学计算工具类
 * @description 提供常用的数值加减乘除计算 , 及多个数值的求和 , 平均值 , 最大最小值计算
 */
public class XMathUtil {
    /**
     * 默认的除法精确度
     */
    private static final int DEF_DIV_SCALE = 2;

    /**
     * 精确加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和(BigDecimal)
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        if (null == v1) {
            v1 = BigDecimal.ZERO;
        }
        if (null == v2) {
            v2 = BigDecimal.ZERO;
        }
        return v1.add(v2);
    }

    /**
     * 精确减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差(BigDecimal)
     */
    public static BigDecimal subtract(BigDecimal v1, BigDecimal v2) {
        if (null == v1) {
            v1 = BigDecimal.ZERO;
        }
        if (null == v2) {
            v2 = BigDecimal.ZERO;
        }
        return v1.subtract(v2);
    }

    /**
     * 精确乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积(BigDecimal)
     */
    public static BigDecimal multiply(BigDecimal v1, BigDecimal v2) {
        if (null == v1) {
            v1 = BigDecimal.ONE;
        }
        if (null == v2) {
            v2 = BigDecimal.ONE;
        }
        return v1.multiply(v2);
    }

    /**
     * ( 相对 )精确除法运算 , 当发生除不尽情况时 , 精确到 小数点以后2位 , 以后数字四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商(BigDecimal)
     */
    public static BigDecimal divide(BigDecimal v1, BigDecimal v2) {
        return v1.divide(v2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * ( 相对 )精确除法运算 . 当发生除不尽情况时 , 由scale参数指 定精度 , 以后数字四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位
     * @return 两个参数的商(BigDecimal)
     */
    public static BigDecimal divide(BigDecimal v1, BigDecimal v2, Integer scale) {
        if (null == v1) {
            return BigDecimal.ZERO;
        }
        if (null == v2) {
            v2 = BigDecimal.ONE;
        }

        if (v2.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("除数不能为0");
        }

        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0");
        }

        return v1.divide(v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 精确加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和(String)
     */
    public static String add(String v1, String v2) {
        if (isBlank(v1)) {
            v1 = "0";
        }
        if (isBlank(v2)) {
            v2 = "0";
        }
        v1 = removeDot(v1);
        v2 = removeDot(v2);
        BigDecimal b1 = new BigDecimal(v1.trim());
        BigDecimal b2 = new BigDecimal(v2.trim());
        return String.valueOf(add(b1, b2));
    }


    /**
     * 去掉金额中的逗号
     *
     * @param v1
     * @return
     */
    private static String removeDot(String v1) {
        if (!TextUtils.isEmpty(v1))
            return v1.replace(",", "");
        return v1;
    }


    /**
     * 精确减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差(String)
     */
    public static String subtract(String v1, String v2) {
        if (isBlank(v1)) {
            v1 = "0";
        }
        if (isBlank(v2)) {
            v2 = "0";
        }
        v1 = removeDot(v1);
        v2 = removeDot(v2);
        BigDecimal b1 = new BigDecimal(v1.trim());
        BigDecimal b2 = new BigDecimal(v2.trim());
        return String.valueOf(subtract(b1, b2));
    }

    /**
     * 精确乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积(String)
     */
    public static String multiply(String v1, String v2) {
        if (isBlank(v1)) {
            v1 = "1";
        }
        if (isBlank(v2)) {
            v2 = "1";
        }
        v1 = removeDot(v1);
        v2 = removeDot(v2);
        BigDecimal b1 = new BigDecimal(v1.trim());
        BigDecimal b2 = new BigDecimal(v2.trim());
        return String.valueOf(multiply(b1, b2));
    }

    /**
     * ( 相对 )精确除法运算 , 当发生除不尽情况时 , 精确到 小数点以后2位 , 以后数字四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商(String)
     */
    public static String divide(String v1, String v2) {
        return divide(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * ( 相对 )精确除法运算 . 当发生除不尽情况时 , 由scale参数指 定精度 , 以后数字四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位
     * @return 两个参数的商(String)
     */
    public static String divide(String v1, String v2, Integer scale) {
        if (null == v1) {
            return "0";
        }
        if (null == v2) {
            v2 = "1";
        }
        v1 = removeDot(v1);
        v2 = removeDot(v2);
        BigDecimal b1 = new BigDecimal(v1.trim());
        BigDecimal b2 = new BigDecimal(v2.trim());
        return String.valueOf(divide(b1, b2, scale));
    }

    /**
     * 精确加法运算 , 计算多个数值总和 , 若其中有null值则忽略
     *
     * @param valList 被加数集合
     * @return 两个参数的和(BigDecimal)
     */
    public static BigDecimal sum(BigDecimal v1, BigDecimal... valList) {
        if (null == v1) {
            v1 = BigDecimal.ZERO;
        }
        if (null == valList || valList.length == 0) {
            return v1;
        }
        for (BigDecimal val : valList) {
            if (null != val) {
                v1 = v1.add(val);
            }
        }
        return v1;
    }

    /**
     * 精确加法运算 , 计算多个数值总和 , 若其中有null值则忽略
     *
     * @param valList 被加数集合
     * @return 两个参数的和(String)
     */
    public static String sum(String v1, String... valList) {
        if (isBlank(v1)) {
            v1 = "0";
        }
        if (null == valList || valList.length == 0) {
            return v1;
        }
        v1 = removeDot(v1);
        BigDecimal b1 = new BigDecimal(v1.trim());
        for (String val : valList) {
            val = removeDot(val);
            if (!isBlank(val)) {
                b1 = add(b1, new BigDecimal(val.trim()));
            }
        }
        return String.valueOf(b1);
    }

    /**
     * 平均数
     *
     * @param valList
     * @return
     */
    public static BigDecimal avg(BigDecimal... valList) {
        if (null != valList && valList.length != 0) {
            return divide(sum(BigDecimal.ZERO, valList), new BigDecimal(valList.length));
        }
        return BigDecimal.ZERO;
    }

    /**
     * 平均数
     *
     * @param valList
     * @return
     */
    public static String avg(String... valList) {
        if (null != valList && valList.length != 0) {
            return divide(sum("0", valList), String.valueOf(valList.length));
        }
        return "0";
    }

    /**
     * 最大值
     *
     * @param v1
     * @param valList
     * @return
     */
    public static BigDecimal max(BigDecimal v1, BigDecimal... valList) {
        BigDecimal max = v1;
        if (null == valList || valList.length == 0) {
            return max;
        }
        for (BigDecimal val : valList) {
            if (null != val && val.compareTo(max) > 0) {
                max = val;
            }
        }
        return max;
    }

    /**
     * 最大值
     *
     * @param valList
     * @return
     */
    public static BigDecimal maxArr(BigDecimal... valList) {
        if (null == valList || valList.length == 0) {
            return null;
        }

        return max(valList[0], valList);
    }

    /**
     * 最小值
     *
     * @param v1
     * @param valList
     * @return
     */
    public static BigDecimal min(BigDecimal v1, BigDecimal... valList) {
        BigDecimal min = v1;
        if (null == valList || valList.length == 0) {
            return min;
        }
        for (BigDecimal val : valList) {
            if (null != val && val.compareTo(min) < 0) {
                min = val;
            }
        }
        return min;
    }

    /**
     * 最小值
     *
     * @param valList
     * @return
     */
    public static BigDecimal minArr(BigDecimal... valList) {
        if (null == valList || valList.length == 0) {
            return null;
        }
        return min(valList[0], valList);
    }

    /**
     * 最大值
     *
     * @param v1
     * @param valList
     * @return
     */
    public static String max(String v1, String... valList) {
        if (isBlank(v1)) {
            return null;
        }
        if (null == valList || valList.length == 0) {
            return v1;
        }
        v1 = removeDot(v1);
        BigDecimal maxBd = new BigDecimal(v1.trim());

        for (String val : valList) {
            val = removeDot(val);
            if (!isBlank(val) && new BigDecimal(val).compareTo(maxBd) > 0) {
                maxBd = new BigDecimal(val);
            }
        }
        return String.valueOf(maxBd);
    }

    /**
     * 最大值
     *
     * @param valList
     * @return
     */
    public static String maxArr(String... valList) {
        if (null == valList || valList.length == 0) {
            return null;
        }
        return max(valList[0], valList);
    }

    /**
     * 最小值
     *
     * @param v1
     * @param valList
     * @return
     */
    public static String min(String v1, String... valList) {
        if (isBlank(v1)) {
            return null;
        }
        if (null == valList || valList.length == 0) {
            return v1;
        }
        v1 = removeDot(v1);
        BigDecimal minBd = new BigDecimal(v1.trim());

        for (String val : valList) {
            val = removeDot(val);
            if (!isBlank(val) && new BigDecimal(val).compareTo(minBd) < 0) {
                minBd = new BigDecimal(val);
            }
        }
        return String.valueOf(minBd);
    }

    /**
     * 最小值
     *
     * @param valList
     * @return
     */
    public static String minArr(String... valList) {
        if (null == valList || valList.length == 0) {
            return null;
        }
        return min(valList[0], valList);
    }

    /**
     * 判断字符串是否为空(不依赖第三方)
     *
     * @param str
     * @return
     */
    private static boolean isBlank(String str) {
        return null == str || str.trim().length() == 0;
    }


    /**
     * 比较两个数的大小
     *
     * @param v1 第一个数
     * @param v2 第二个数
     * @return 如果第一个比第二个数小，返回-1。两个数一样大返回0.第一个数比第二个数大返回1
     */
    public static int compareTo(String v1, String v2) {
        if (isBlank(v1)) {
            v1 = "1";
        }
        if (isBlank(v2)) {
            v2 = "1";
        }
        v1 = removeDot(v1);
        v2 = removeDot(v2);
        BigDecimal b1 = new BigDecimal(v1.trim());
        BigDecimal b2 = new BigDecimal(v2.trim());
        return compareTo(b1, b2);
    }

    /**
     * 比较两个数的大小
     *
     * @param v1 第一个数
     * @param v2 第二个数
     * @return 如果第一个比第二个数小，返回-1。两个数一样大返回0.第一个数比第二个数大返回1
     */
    public static int compareTo(BigDecimal v1, BigDecimal v2) {
        if (null == v1) {
            v1 = BigDecimal.ONE;
        }
        if (null == v2) {
            v2 = BigDecimal.ONE;
        }
        return v1.compareTo(v2);
    }

    /**
     * 去掉数字中多余的零
     *
     * @param number
     * @return
     */
    public static String getPrettyNumber(String number) {
        if (isBlank(number)) {
            number = "0";
        }
        number = removeDot(number);
        Double value = 0.0;
        try {
            value = Double.valueOf(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NumberFormat nf = new DecimalFormat("#,###.##");
        return nf.format(value);
//        return number;
    }



    /**
     * 格式化金钱
     *
     * @param number
     * @return
     */
    public static String formatMoney(String number) {
        if (isBlank(number)) {
            number = "0";
        }
        number = removeDot(number);
        Double value = 0.0;
        try {
            value = Double.valueOf(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NumberFormat nf = new DecimalFormat("#,##0.00");
        return nf.format(value);
    }

    public static String round_down(String v, int i) {
        if (isBlank(v)) {
            v = "1";
        }
        v = removeDot(v);
        return String.valueOf(new BigDecimal(v).divide(BigDecimal.ONE, i, BigDecimal.ROUND_DOWN));
    }
}