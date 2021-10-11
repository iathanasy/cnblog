package com.icss.cnblog.utils;

import lombok.Data;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-02-04 12:28
 **/
public enum ResultEnum {
    SUCCESS(200, "操作成功"),
    UNAUTHZ(401, "未授权"),
    UNAUTHZC(403, "未认证"),
    BAD_REQUEST(400, "数据库异常"),
    NOT_FOUND(404, "未找到"),
    METHOD_NOT_ALLOWD(405,"请求方法（GET、POST、HEAD、Delete、PUT、TRACE等）对指定的资源不适用"),

    NVALID_PARAMS(500,"参数异常"),
    ERROR(500, "系统异常")
    ;


    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
