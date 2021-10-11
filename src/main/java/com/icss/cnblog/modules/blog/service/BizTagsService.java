package com.icss.cnblog.modules.blog.service;

import com.baomidou.mybatisplus.service.IService;
import com.icss.cnblog.modules.blog.entity.BizTagsEntity;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;

import java.util.List;
import java.util.Set;

/**
 * 标签表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
public interface BizTagsService extends IService<BizTagsEntity> {

    /**
     * 分页
     * @param query
     * @return
     */
    PageUtils selectPageList(Query query);

    /**
     * 新增多个标签
     * @param tags
     */
    void insertList(Set<String> tags);

    /**
     * 查询全部标签
     * @return
     */
    List<BizTagsEntity> selectAll();

    /**
     * 根据名称查询ID
     * @param names
     * @return
     */
    List<Long> selectIdsByName(Set<String> names);

    /**
     * 删除标签
     * @param tagIds
     */
    void deleteBatch(Long[] tagIds);


    /**
     * 根据文章ID查询
     * @param articleId
     * @return
     */
    List<BizTagsEntity> selectListByArticleId(Long articleId);
}

