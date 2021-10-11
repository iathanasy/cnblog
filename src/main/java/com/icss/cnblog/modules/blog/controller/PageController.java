package com.icss.cnblog.modules.blog.controller;

import com.icss.cnblog.modules.blog.entity.BizArticleEntity;
import com.icss.cnblog.modules.blog.entity.BizCategoryEntity;
import com.icss.cnblog.modules.blog.entity.dto.BizArticleDto;
import com.icss.cnblog.modules.blog.service.BizArticleService;
import com.icss.cnblog.modules.blog.service.BizCategoryService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 页面渲染
 * @author: Mr.Wang
 * @create: 2020-02-03 19:29
 **/
@Controller
public class PageController {

    public final static String theme = "blog/";

    @Autowired
    private BizCategoryService bizCategoryService;
    @Autowired
    private BizArticleService bizArticleService;

    /**
     * 加载首页数据
     * @param request
     * @param query
     */
    private void loadIndexPage(HttpServletRequest request, Query query){
        PageUtils page = bizArticleService.selectIndexPageList(query);
        request.setAttribute("page", page);
    }

    /**
     * 首页
     * @return
     */
    @RequestMapping({"/","index"})
    public String index(HttpServletRequest request){
        return page(request, 1);
    }

    /**
     * 分页数据
     * @param request
     * @param pageNum
     */
    @GetMapping({"/index/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum){
        BizArticleDto query = new BizArticleDto();
        query.setCurrPage(pageNum);

        request.setAttribute("url", "/index");
        loadIndexPage(request, query);
        return theme + "index";
    }

    /**
     * 分类列表页
     *
     * @return
     */
    @GetMapping({"/category/{categoryName}"})
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName) {
        return category(request, categoryName, 1);
    }

    /**
     * 分类列表页
     * @param request
     * @param categoryAlias 分类别名
     * @param pageNum
     * @return
     */
    @GetMapping({"/category/{categoryAlias}/{pageNum}"})
    public String category(HttpServletRequest request, @PathVariable("categoryAlias") String categoryAlias, @PathVariable("pageNum") Integer pageNum) {
        BizArticleDto query = new BizArticleDto();
        query.setCurrPage(pageNum);
        request.setAttribute("url", "/category/" + categoryAlias);

        //查询 分类
        BizCategoryEntity categoryEntity = bizCategoryService.selectOneByAlias(categoryAlias);
        query.setCategoryId(categoryEntity.getId());

        loadIndexPage(request, query);
        return theme + "index";
    }

    /**
     * 标签列表页
     *
     * @return
     */
    @GetMapping({"/tag/{tagId}"})
    public String tag(HttpServletRequest request, @PathVariable("tagId") Long tagId) {
        return tag(request, tagId, 1);
    }

    /**
     * 标签列表页
     * @param request
     * @param tagId 标签ID
     * @param pageNum
     * @return
     */
    @GetMapping({"/tag/{tagId}/{pageNum}"})
    public String tag(HttpServletRequest request, @PathVariable("tagId") Long tagId, @PathVariable("pageNum") Integer pageNum) {
        BizArticleDto query = new BizArticleDto();
        query.setCurrPage(pageNum);
        query.setTagId(tagId);
        request.setAttribute("url", "/tag/" + tagId);

        loadIndexPage(request, query);
        return theme + "index";
    }

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword) {
        return search(request, keyword, 1);
    }

    /**
     * 搜索列表页
     *
     * @return
     */
    @GetMapping({"/search/{keyword}/{pageNum}"})
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("pageNum") Integer pageNum) {
        long startTime = System.currentTimeMillis();
        BizArticleDto query = new BizArticleDto();
        query.setCurrPage(pageNum);
        query.setKeywords(keyword);

        request.setAttribute("url", "search/" + keyword);
        request.setAttribute("keyword", keyword);

        loadIndexPage(request, query);

        long endTime = System.currentTimeMillis();
        request.setAttribute("ms", endTime - startTime + "ms");

        return theme + "search";
    }

    /**
     * 详情页
     * @return
     */
    @GetMapping("article/{articleId}")
    public String detail(HttpServletRequest request,@PathVariable("articleId") Long articleId){
        BizArticleEntity article = bizArticleService.selectOneById(articleId);

        request.setAttribute("article", article);
        return theme + "detail";
    }

}
