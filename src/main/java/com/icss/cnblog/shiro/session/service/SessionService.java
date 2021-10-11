package com.icss.cnblog.shiro.session.service;

import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;

import java.io.Serializable;

/**
 * @description: 获取会话信息与踢人功能
 * @author: Mr.Wang
 * @create: 2020-02-13 14:36
 **/
public interface SessionService {

    /**
     * 获取在线session的page对象
     */
    public PageUtils selectPageList(Query query);

    /**
     * 根据sessionId执行强制退出
     * @param sessionId
     */
    void kickout(Serializable sessionId);

}
