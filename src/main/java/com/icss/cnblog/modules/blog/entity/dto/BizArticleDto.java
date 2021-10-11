package com.icss.cnblog.modules.blog.entity.dto;

import com.icss.cnblog.utils.Query;
import lombok.Data;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-04-14 22:02
 **/
@Data
public class BizArticleDto extends Query {
    /**
     * 类别Id
     */
    private Long categoryId;
    /**
     * 标签ID
     */
    private Long tagId;
}
