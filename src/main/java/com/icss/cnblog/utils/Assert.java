package com.icss.cnblog.utils;

import com.icss.cnblog.exception.GlobalException;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 数据校验
 * @author: Mr.Wang
 * @create: 2020-02-09 17:09
 **/
public class Assert {

    /**
     * 验证字符串为空
     * @param str
     * @param message
     */
    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new GlobalException(message);
        }
    }

    /**
     * 验证对象是否为空
     * @param object
     * @param message
     */
    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new GlobalException(message);
        }
    }

    /**
     * 验证对象不为空
     * @param obj
     * @param message
     */
    public static void isNotNull(Object obj, String message) {
        if (null != obj && !obj.equals((Object)null)) {
            throw new GlobalException(message);
        }
    }
}
