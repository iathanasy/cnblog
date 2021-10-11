package com.icss.cnblog.modules.blog.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.modules.blog.entity.BizArticleTagsEntity;
import com.icss.cnblog.modules.blog.mapper.BizArticleTagsMapper;
import com.icss.cnblog.modules.blog.service.BizArticleTagsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service("bizArticleTagsService")
public class BizArticleTagsServiceImpl extends ServiceImpl<BizArticleTagsMapper, BizArticleTagsEntity> implements BizArticleTagsService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long articleId, List<Long> tagIdList) {
        HashMap<String, Object> map = new HashMap();
        map.put("article_id", articleId);
        //先删除文章与标签的关系
        this.deleteByMap(map);

        if(tagIdList == null || tagIdList.size() == 0){
            return ;
        }

        //保存用户与角色关系
        List<BizArticleTagsEntity> list = new ArrayList<>(tagIdList.size());
        for(Long tagId : tagIdList){
            BizArticleTagsEntity atag = new BizArticleTagsEntity();
            atag.setArticleId(articleId);
            atag.setTagId(tagId);

            list.add(atag);
        }
        this.insertBatch(list);
    }

    @Override
    public List<String> queryTagList(Long articleId) {
        return baseMapper.queryTagList(articleId);
    }

    @Override
    public List<Long> queryTagIdList(Long articleId) {
        return baseMapper.queryTagIdList(articleId);
    }

    @Override
    public void deleteBatchByArticleId(Long[] ids) {
        baseMapper.deleteBatchByArticleId(ids);
    }

    @Override
    public void deleteBatch(Long[] tagIds) {
        baseMapper.deleteBatch(tagIds);
    }
}
