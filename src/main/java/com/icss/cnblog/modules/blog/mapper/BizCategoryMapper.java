package com.icss.cnblog.modules.blog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.icss.cnblog.modules.blog.entity.BizCategoryEntity;

import java.util.List;

/**
 * 类别表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
public interface BizCategoryMapper extends BaseMapper<BizCategoryEntity> {

    /**
     * 列表(第一级)
     */
    List<BizCategoryEntity> selectParentList();

    /**
     *  列表(子集)
     */
    List<BizCategoryEntity> selectChildsList(Long parentId);

    /**
     * 首页导航(一级)
     * @return
     */
    List<BizCategoryEntity> selectNav();

    /**
     * 首页导航(子集)
     * @param parentId
     * @return
     */
    List<BizCategoryEntity> selectNavChilds(Long parentId);
}
