package com.icss.cnblog.modules.blog.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.icss.cnblog.modules.blog.entity.BizCategoryEntity;
import com.icss.cnblog.modules.blog.entity.BizTagsEntity;
import com.icss.cnblog.modules.blog.entity.dto.BizArticleDto;
import com.icss.cnblog.modules.blog.service.BizArticleService;
import com.icss.cnblog.modules.blog.service.BizCategoryService;
import com.icss.cnblog.modules.blog.service.BizTagsService;
import com.icss.cnblog.utils.PageUtils;
import com.icss.cnblog.utils.Query;
import com.icss.cnblog.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 首页
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:28
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class IndexController {

    @Autowired
    private BizCategoryService bizCategoryService;
    @Autowired
    private BizArticleService bizArticleService;
    @Autowired
    private BizTagsService bizTagsService;

    /**
     * 导航栏
     * @return
     */
    @GetMapping("/nav")
    public Result nav(){
        List<BizCategoryEntity> nav = bizCategoryService.selectNav();
        return Result.success(nav);
    }

    /**
     * 文章列表
     * @param query
     * @return
     */
    @GetMapping("/article/list")
    public Result list(BizArticleDto query){
        PageUtils page = bizArticleService.selectIndexPageList(query);
        return Result.success(page);
    }

    /**
     * 标签展示
     * @return
     */
    @GetMapping("/tags")
    public Result tags(){
        List<BizTagsEntity> tags = bizTagsService.selectAll();
        return Result.success(tags);
    }


    /**
     * 获取QQ 信息
     * @param qq
     * @return
     */
    @PostMapping("/qq/{qq}")
    public Result qq(@PathVariable("qq") String qq) {
        if (StringUtils.isEmpty(qq)) {
            return Result.error("QQ号码为空");
        }
        Map<String, String> resultMap = new HashMap<>(4);
        String nickname = "匿名";
        String json = HttpUtil.get("http://users.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=" + qq, CharsetUtil.CHARSET_GBK);
        if (!StringUtils.isEmpty(json)) {
            try {
                json = json.replaceAll("portraitCallBack|\\\\s*|\\t|\\r|\\n", "");
                json = json.substring(1, json.length() - 1);
                log.info(json);
                JSONObject object = JSONObject.parseObject(json);
                JSONArray array = object.getJSONArray(qq);
                nickname = array.getString(6);
            } catch (Exception e) {
                log.error("通过QQ号获取用户昵称发生异常", e);
            }
        }
        resultMap.put("avatar", "https://q1.qlogo.cn/g?b=qq&nk=" + qq + "&s=40");
        resultMap.put("nickname", nickname);
        resultMap.put("email", qq + "@qq.com");
        resultMap.put("url", "https://user.qzone.qq.com/" + qq);
        return Result.success(resultMap);
    }



}
