package com.icss.cnblog.modules.blog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.icss.cnblog.modules.blog.entity.BizTagsEntity;

import java.util.List;
import java.util.Set;

/**
 * 标签表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
public interface BizTagsMapper extends BaseMapper<BizTagsEntity> {

    /**
     * 根据名称查询ID
     * @param names
     * @return
     */
    List<Long> selectIdsByName(Set<String> names);

    /**
     * 根据文章ID查询
     * @param articleId
     * @return
     */
    List<BizTagsEntity> selectListByArticleId(Long articleId);
}
