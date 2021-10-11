package com.icss.cnblog.modules.blog.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.icss.cnblog.modules.blog.entity.BizArticleEntity;
import com.icss.cnblog.modules.blog.entity.vo.BizArticleVo;
import com.icss.cnblog.utils.Query;

import java.util.List;

/**
 * 文章表
 * 
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:27
 */
public interface BizArticleMapper extends BaseMapper<BizArticleEntity> {

    public List<BizArticleEntity> selectPageList(Query query, Page page);

    public List<BizArticleVo> selectIndexPageList(Query query, Page page);

    BizArticleVo selectOneById(Long id);
}
