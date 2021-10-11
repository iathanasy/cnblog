package com.icss.cnblog.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-06 10:37
 **/
public class CommonConst {

    /**
     * website站点缓存
     */
    public static Map<String, String> WEBSITE = new HashMap<String, String>();

    /**
     * User 的 session key;k
     */
    public static final String SESSION_KEY_USER = "SESSION_KEY_USER";

    /**
     * kaptcha 的 session key
     */
    public final static String SESSION_KEY_KAPTCHA = "SESSION_KEY_KAPTCHA";

    /**
     * 设置Session会话的kickout属性
     */
    public final static String SESSION_ATTR_KICKOUT = "kickout";

    /**
     * 账号状态 （0正常 1停用）
     */
    public final static Integer USER_STATUS = 1;

    /**
     * 超级用户
     */
    public final static String USER_ADMIN = "admin";


    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";
    /** 上传文件路径 示例（ Windows配置D:/cnblog/uploadPath，Linux配置 /home/cnblog/uploadPath）*/
    public static final String  profile = "D:/cnblog/uploadPath";

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return profile + "/avatar";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return profile + "/upload";
    }

    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG("M"),
        /**
         * 菜单
         */
        MENU("C"),
        /**
         * 按钮
         */
        BUTTON("F");

        private String value;

        MenuType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
