package com.icss.cnblog.exception;

import com.icss.cnblog.utils.ResultEnum;
import lombok.Data;

/**
 * @description: 全局异常
 * @author: Mr.Wang
 * @create: 2020-02-04 13:39
 **/
@Data
public class GlobalException extends RuntimeException{

    private ResultEnum resultEnum;

    public GlobalException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.resultEnum = resultEnum;
    }

    public GlobalException(String msg){
        super(msg);
    }
}
