package com.icss.cnblog.modules.blog.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.modules.blog.entity.BizCategoryEntity;
import com.icss.cnblog.modules.blog.mapper.BizCategoryMapper;
import com.icss.cnblog.modules.blog.service.BizCategoryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


@Service("bizCategoryService")
public class BizCategoryServiceImpl extends ServiceImpl<BizCategoryMapper, BizCategoryEntity> implements BizCategoryService {


    @Override
    public List<BizCategoryEntity> selectOneLevelList() {
        return baseMapper.selectParentList();
    }

    /**
     * 查询列表
     * @return
     */
    @Override
    public List<BizCategoryEntity> selectList() {
        return tree();
    }

    @Override
    public List<BizCategoryEntity> selectAllList() {
        return this.selectList(new EntityWrapper<BizCategoryEntity>());
    }

    @Override
    public List<BizCategoryEntity> selectShowList() {
        return this.selectList(new EntityWrapper<BizCategoryEntity>().eq("status",1));
    }

    @Override
    public List<BizCategoryEntity> selectListParentId(Long parentId) {
        HashMap map = new HashMap<String, Object>();
        map.put("parent_id", parentId);
        return this.selectByMap(map);
    }

    @Override
    public void delete(Long categoryId) {
        this.deleteById(categoryId);
    }

    @Override
    public List<BizCategoryEntity> selectNav() {
        List<BizCategoryEntity> parentList = baseMapper.selectNav();
        for (BizCategoryEntity c: parentList) {
            c.setChilds(selectNavChildren(c.getId()));
        }
        return parentList;
    }

    @Override
    public BizCategoryEntity selectOneByAlias(String alias) {
        return this.selectOne(new EntityWrapper<BizCategoryEntity>().eq("alias", alias));
    }

    /**
     * 首页导航(子集)
     * @param parentId
     * @return
     */
    private List<BizCategoryEntity> selectNavChildren(Long parentId) {
        List<BizCategoryEntity> childsList = baseMapper.selectNavChilds(parentId);
        for (BizCategoryEntity c : childsList) {
            if (c.getParentId().equals(parentId)) {
                c.setChilds(selectNavChildren(c.getId()));
            }
        }
        return childsList;
    }


    /**
     * 查询一级
     * @return
     */
    private List<BizCategoryEntity> tree(){
        List<BizCategoryEntity> parentList = baseMapper.selectParentList();
        for (BizCategoryEntity c: parentList) {
            c.setChilds(children(c.getId()));
        }
        return parentList;
    }

    /**
     * 递归遍历子集
     * @param parentId
     * @return
     */
    private List<BizCategoryEntity> children(Long parentId){
        List<BizCategoryEntity> childsList = baseMapper.selectChildsList(parentId);
        for (BizCategoryEntity c: childsList) {
            if(c.getParentId().equals(parentId)) {
                c.setChilds(children(c.getId()));
            }
        }
        return childsList;
    }


}
