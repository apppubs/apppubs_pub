package com.hingecloud.apppubs.pub.tools;

import com.hingecloud.apppubs.pub.exception.ArgumentCheckException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public final class ValidateHelper {

    private static final List<String> SUPPORTED_IMG_TYPE = Arrays.asList(".jpeg", ".jpg", ".gif", ".bmp", ".png");

    public static void isTrue(boolean expression, String message) {
        if (expression == false) {
            throw new ArgumentCheckException(message);
        }
    }

    public static void notBlank(String argument, String message) {
        isTrue(StringUtils.isNotBlank(argument), message);
    }

    public static void notNull(Object argument, String message) {
        isTrue(argument != null, message);
    }

    public static void isIdcard(String idcard) {
        int length = StringUtils.length(idcard);
        if ((length != 15 && length != 18) || !StringUtils.isAlphanumeric(idcard)) {
            throw new ArgumentCheckException("您输入的身份证号码格式不正确");
        }
    }

    public static void checkPassword(String password) {
        if (StringUtils.length(password) < 6) {
            throw new ArgumentCheckException("密码长度至少为6位");
        }
    }

    public static void isSupportedImgType(String filename) {
        String suffix = extractFilenameSuffix(filename);
        if (!SUPPORTED_IMG_TYPE.contains(suffix)) {
            throw new ArgumentCheckException(String.format("不支持的图片格式[%s]", suffix));
        }
    }

    private static String extractFilenameSuffix(String filename) {
        return StringUtils.lowerCase(filename.substring(filename.lastIndexOf(".")));
    }

}
