package com.icss.cnblog.modules.blog.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.icss.cnblog.exception.GlobalException;
import com.icss.cnblog.modules.blog.entity.BizArticleEntity;
import com.icss.cnblog.modules.blog.entity.BizTagsEntity;
import com.icss.cnblog.modules.blog.entity.vo.BizArticleVo;
import com.icss.cnblog.modules.blog.mapper.BizArticleMapper;
import com.icss.cnblog.modules.blog.service.BizArticleService;
import com.icss.cnblog.modules.blog.service.BizArticleTagsService;
import com.icss.cnblog.modules.blog.service.BizTagsService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.RelativeDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;


@Service("bizArticleService")
public class BizArticleServiceImpl extends ServiceImpl<BizArticleMapper, BizArticleEntity> implements BizArticleService {

    @Autowired
    private BizArticleTagsService articleTagsService;
    @Autowired
    private BizTagsService tagsService;


    @Override
    public PageUtils selectPageList(Query query) {
        Page<BizArticleEntity> page = query.getPagePlus();
        page.setRecords(baseMapper.selectPageList(query, page));
        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(BizArticleEntity article) {
        //处理标签数据
        Set<String> tags = article.getTags();
        if (tags.size() > 3) {
            throw new GlobalException("标签数据最大为3个");
        }
        //保存文章
        this.insert(article);
        //新增tag
        tagsService.insertList(tags);

        //查询文章标签ID
        List<Long> ids = tagsService.selectIdsByName(tags);
        //保存文章与标签的关系
        articleTagsService.saveOrUpdate(article.getId(), ids);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BizArticleEntity article) {
        //处理标签数据
        Set<String> tags = article.getTags();
        if (tags.size() > 3) {
            throw new GlobalException("标签数据最大为3个");
        }
        //修改文章
        this.updateById(article);
        //新增tag
        tagsService.insertList(tags);

        //查询文章标签ID
        List<Long> ids = tagsService.selectIdsByName(tags);
        //保存文章与标签的关系
        articleTagsService.saveOrUpdate(article.getId(), ids);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        //删除文章
        this.deleteBatchIds(Arrays.asList(ids));
        //删除文章与标签关系
        articleTagsService.deleteBatchByArticleId(ids);
    }

    @Override
    public PageUtils selectIndexPageList(Query query) {
        Page<BizArticleVo> page = query.getPagePlus();
        List<BizArticleVo> bizArticleVos = baseMapper.selectIndexPageList(query, page);
        bizArticleVos.forEach((articleVo)->{
            List<BizTagsEntity> tags = tagsService.selectListByArticleId(articleVo.getId());
            articleVo.setTagList(tags);
            //格式化日期
            String formatDate = RelativeDateFormat.format(articleVo.getCreateTime());
            articleVo.setReleaseTime(formatDate);
        });

        page.setRecords(bizArticleVos);
        return new PageUtils(page);
    }

    @Override
    public BizArticleVo selectOneById(Long id) {
        BizArticleVo articleVo = baseMapper.selectOneById(id);
        //增加浏览量
        incrPv(articleVo);
        List<BizTagsEntity> tags = tagsService.selectListByArticleId(articleVo.getId());
        articleVo.setTagList(tags);
        //格式化日期
        String formatDate = RelativeDateFormat.format(articleVo.getCreateTime());
        articleVo.setReleaseTime(formatDate);
        return articleVo;
    }

    private void incrPv(BizArticleEntity articleEntity){
        AtomicLong pv = new AtomicLong(articleEntity.getPv());
        //增加浏览量
        articleEntity.setPv(pv.incrementAndGet());
        baseMapper.updateById(articleEntity);
    }
}
