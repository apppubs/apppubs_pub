package com.hingecloud.apppubs.pub.utils;

import java.util.List;
import java.util.UUID;

/**
 * Author: D.Yang
 * Email: koyangslash@gmail.com
 * Date: 16/8/31
 * Time: 下午5:42
 * Describe: String工具类
 */
public class StringUtil {

    // 定义下划线
    private static final char UNDERLINE = '_';

    /**
     * String为空判断
     *
     * @param str 需判断字符串
     * @return true:为空 false:不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * String不为空判断
     *
     * @param str 需判断字符串
     * @return true:不为空 false:为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String getString(String str, String def) {
        if (isNotEmpty(str)) {
            return str;
        }
        return def;
    }

    /**
     * 驼峰转下划线工具
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String camelToUnderline(String param) {
        if (isNotEmpty(param)) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for (int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (Character.isUpperCase(c)) {
                    sb.append(UNDERLINE);
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 下划线转驼峰工具
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String underlineToCamel(String param) {
        if (isNotEmpty(param)) {
            int len = param.length();
            StringBuilder sb = new StringBuilder(len);

            for (int i = 0; i < len; ++i) {
                char c = param.charAt(i);
                if (c == 95) {
                    ++i;
                    if (i < len) {
                        sb.append(Character.toUpperCase(param.charAt(i)));
                    }
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }


    /**
     * 在字符串两周添加''
     *
     * @param param 字符串
     * @return 处理后的字符串
     */
    public static String addSingleQuotes(String param) {
        return "\'" + param + "\'";
    }

    public static String convertArr2Str(List<String> list) {
        if (list == null || list.size() < 1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = -1; ++i < list.size(); ) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * 生成16位的UUID
     *
     * @return
     */
    public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }
}
