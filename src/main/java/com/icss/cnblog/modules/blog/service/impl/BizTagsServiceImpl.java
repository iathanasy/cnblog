package com.icss.cnblog.modules.blog.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.modules.blog.entity.BizTagsEntity;
import com.icss.cnblog.modules.blog.mapper.BizTagsMapper;
import com.icss.cnblog.modules.blog.service.BizArticleTagsService;
import com.icss.cnblog.modules.blog.service.BizTagsService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Service("bizTagsService")
public class BizTagsServiceImpl extends ServiceImpl<BizTagsMapper, BizTagsEntity> implements BizTagsService {

    @Autowired
    private BizArticleTagsService articleTagsService;

    @Override
    public PageUtils selectPageList(Query query) {
        String keywords = query.getKeywords();
        Page page = this.selectPage(query.getPagePlus(),
                new EntityWrapper<BizTagsEntity>().like(StringUtils.isNotBlank(keywords),"name", keywords));
        return new PageUtils(page);
    }

    @Override
    public void insertList(Set<String> tags) {
        List<BizTagsEntity> insertList = new ArrayList<BizTagsEntity>();
        for (String name: tags) {
            BizTagsEntity tag = this.selectOne(new EntityWrapper<BizTagsEntity>().eq("name", name.trim()));
            if (tag == null) {
                //不存在就新增
                BizTagsEntity t = new BizTagsEntity();
                t.setName(name);
                t.setColor("#"+RandomUtil.randomColorStr());
                t.setRemark(name);
                insertList.add(t);
            }
        }
        //新增标签数据
        if (!CollectionUtils.isEmpty(insertList)) {
            this.insertBatch(insertList);
        }
    }

    @Override
    public List<BizTagsEntity> selectAll() {
        return this.selectList(new EntityWrapper<BizTagsEntity>());
    }

    @Override
    public List<Long> selectIdsByName(Set<String> names) {
        if (!CollectionUtils.isEmpty(names)) {
            return baseMapper.selectIdsByName(names);
        }
        return null;
    }

    @Override
    public void deleteBatch(Long[] tagIds) {
        //删除标签
        this.deleteBatchIds(Arrays.asList(tagIds));
        //删除标签与文章的关系
        articleTagsService.deleteBatch(tagIds);
    }

    @Override
    public List<BizTagsEntity> selectListByArticleId(Long articleId) {
        return baseMapper.selectListByArticleId(articleId);
    }
}
