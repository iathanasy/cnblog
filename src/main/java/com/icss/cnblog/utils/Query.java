package com.icss.cnblog.utils;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * @description: 查询参数
 * @author: Mr.Wang
 * @create: 2020-02-05 15:49
 **/
@Data
public class Query<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**默认每页显示*/
    public final static int DEFAULT_PAGE_SIZE = 10;

    /**当前页*/
    private int currPage;

    /**每页显示条数*/
    private int pageSize;

    /**排序字段*/
    private String sidx;

    /**排序方式 asc升序  desc降序*/
    private String sord;

    /**搜索条件*/
    private String keywords;

    /**开始时间*/
    private Date beginTime;

    /**结束时间*/
    private Date endTime;

    public int getPageSize() {
        return pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
    }

    /**
     * 获取mybatisPlus封装的Page对象
     * @return Page
     */
    public Page<T> getPagePlus(){
        Page<T> pagePlus = new Page<T>(this.currPage, getPageSize());
        //排序
        if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(sord)){
            pagePlus.setAsc("asc".equalsIgnoreCase(this.sord));
            pagePlus.setOrderByField(this.sidx);
        }
        return pagePlus;
    }
}
