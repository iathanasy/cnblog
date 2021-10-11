package com.icss.cnblog.modules.blog.service;

import com.baomidou.mybatisplus.service.IService;
import com.icss.cnblog.modules.blog.entity.BizCategoryEntity;

import java.util.List;

/**
 * 类别表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
public interface BizCategoryService extends IService<BizCategoryEntity> {

    /**
     * 查询一级类别
     * @return
     */
    List<BizCategoryEntity> selectOneLevelList();

    /**
     * 查询处理过的列表集(tree)
     * @return
     */
    List<BizCategoryEntity> selectList();

    /**
     * 查询所有类别
     * @return
     */
    List<BizCategoryEntity> selectAllList();

    /**
     * 查询显示的类别(状态 status == 1)
     * @return
     */
    List<BizCategoryEntity> selectShowList();

    /**
     * 查询是否有子菜单
     * @param parentId
     * @return
     */
    List<BizCategoryEntity> selectListParentId(Long parentId);

    /**
     * 删除
     * @param categoryId
     */
    void delete(Long categoryId);

    /********************************前端****************************************/
    List<BizCategoryEntity> selectNav();

    /**
     * 别名查询
     * @param alias
     * @return
     */
    BizCategoryEntity selectOneByAlias(String alias);
}

