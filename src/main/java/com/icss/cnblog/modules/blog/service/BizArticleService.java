package com.icss.cnblog.modules.blog.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.icss.cnblog.modules.blog.entity.BizArticleEntity;
import com.icss.cnblog.modules.blog.entity.vo.BizArticleVo;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;

import java.util.List;

/**
 * 文章表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:27
 */
public interface BizArticleService extends IService<BizArticleEntity> {

    PageUtils selectPageList(Query query);

    void add(BizArticleEntity article);

    void update(BizArticleEntity article);

    void deleteBatch(Long[] ids);

    /***********************文章列表*********************************/

    PageUtils selectIndexPageList(Query query);

    BizArticleVo selectOneById(Long id);
}

