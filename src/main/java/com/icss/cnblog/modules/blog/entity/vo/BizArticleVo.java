package com.icss.cnblog.modules.blog.entity.vo;

import com.icss.cnblog.modules.blog.entity.BizArticleEntity;
import com.icss.cnblog.modules.blog.entity.BizTagsEntity;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-04-14 21:37
 **/
@Data
public class BizArticleVo extends BizArticleEntity {

    /**
     * 类别名称
     */
    private String name;
    /**
     * 类别别名(用于url访问)
     */
    private String alias;

    /**
     * 标签列表
     */
    private List<BizTagsEntity> tagList;

    /**
     * 发布时间
     */
    private String releaseTime;

}
