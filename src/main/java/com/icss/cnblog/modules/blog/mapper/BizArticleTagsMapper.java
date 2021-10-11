package com.icss.cnblog.modules.blog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.icss.cnblog.modules.blog.entity.BizArticleTagsEntity;

import java.util.List;

/**
 * 文章和标签关联表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
public interface BizArticleTagsMapper extends BaseMapper<BizArticleTagsEntity> {

    /**
     * 根据文章ID,获取标签名
     */
    List<String> queryTagList(Long articleId);

    /**
     * 根据文章ID,获取标签ID
     */
    List<Long> queryTagIdList(Long articleId);

    /**
     * 根据文章ID数组，批量删除
     * @param ids
     */
    void deleteBatchByArticleId(Long[] ids);


    /**
     * 根据标签ID数组， 批量删除
     * @param tagIds
     */
    void deleteBatch(Long[] tagIds);
}
