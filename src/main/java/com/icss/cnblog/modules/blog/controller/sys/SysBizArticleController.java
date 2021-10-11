package com.icss.cnblog.modules.blog.controller.sys;
import com.alibaba.fastjson.JSONObject;
import com.icss.cnblog.annotation.SysLog;
import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.controller.BaseController;
import com.icss.cnblog.entity.SysUserEntity;
import com.icss.cnblog.modules.blog.entity.BizArticleEntity;
import com.icss.cnblog.modules.blog.service.BizArticleService;
import com.icss.cnblog.modules.blog.service.BizArticleTagsService;
import com.icss.cnblog.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;


/**
 * 文章表
 *
 * @author cd
 * @email 14163548@qq.com
 * @date 2020-02-16 14:29:27
 */
@Slf4j
@RestController
@RequestMapping("sys/article")
public class SysBizArticleController extends BaseController {
    @Autowired
    private BizArticleService articleService;
    @Autowired
    private BizArticleTagsService articleTagsService;

    @RequestMapping("list")
    @RequiresPermissions("sys:article:list")
    public Result list(Query query) {
        PageUtils page = articleService.selectPageList(query);
        return Result.success(page);
    }

    /**
     * 添加
     * @param article
     * @return
     */
    @SysLog(value = "添加文章",paramsSave = false)
    @PostMapping("add")
    @RequiresPermissions("sys:article:add")
    public Result add(@RequestBody BizArticleEntity article){
        article.setUserId(getUserId());
        articleService.add(article);
        return Result.success();
    }

    /**
     * 信息
     */
    @RequestMapping("info/{articleId}")
    @RequiresPermissions("sys:article:info")
    public Result info(@PathVariable("articleId") Long articleId){
        BizArticleEntity article = articleService.selectById(articleId);
        //添加标签
        List<String> tags = articleTagsService.queryTagList(article.getId());
        if(article != null){
            article.setTags(new HashSet<String>(tags));
        }
        return Result.success(article);
    }

    @SysLog(value = "修改文章",paramsSave = false)
    @PostMapping("update")
    @RequiresPermissions("sys:article:update")
    public Result update(@RequestBody BizArticleEntity article){
        article.setUserId(getUserId());
        articleService.update(article);
        return Result.success();
    }

    /**
     * 删除
     */
    @SysLog("删除文章")
    @PostMapping("delete")
    @RequiresPermissions("sys:article:delete")
    public Result delete(@RequestBody Long[] articleIds){
        articleService.deleteBatch(articleIds);
        return Result.success();
    }

    /**
     * 上传封面图片
     */
    @SysLog(value = "上传文章封面",paramsSave = false)
    @PostMapping("/upload")
    @RequiresPermissions("sys:article:upload")
    public Result upload(@RequestParam("file") MultipartFile file){
        try
        {
            if (!file.isEmpty())
            {
                String path = UploadUtil.upload(CommonConst.getUploadPath(), file);
                return Result.success(path);
            }
            return Result.error("文件为空");
        }
        catch (Exception e)
        {
            log.error("上传文件失败！", e);
            return Result.error(e.getMessage());
        }

    }

    /**
     * 上传图片
     */
    @SysLog(value = "上传文章图片(base64)",paramsSave = false)
    @PostMapping("/upload/base64")
    @RequiresPermissions("sys:article:upload:base64")
    public Result uploadBase64(@RequestBody String base64){
        try
        {
            if (StringUtils.isNotEmpty(base64)){
                String path = UploadUtil.uploadBase64(CommonConst.getUploadPath(), base64, "jpg");
                return Result.success(path);
            }
            return Result.error("文件为空");
        }
        catch (Exception e)
        {
            log.error("上传文件失败！", e);
            return Result.error(e.getMessage());
        }

    }


    /**
     * 上传markdown编辑器图片
     */
    @SysLog(value = "上传文章Markdown编辑器图片",paramsSave = false)
    @PostMapping("/upload/markdown")
    @RequiresPermissions("sys:article:upload:markdown")
    @ResponseBody
    public JSONObject uploadAvatar(HttpServletRequest request){
        JSONObject jsonObject=new JSONObject();
        try {

            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> itr = multiRequest.getFileNames();
            while (!itr.hasNext()) {
                jsonObject.put("error", 1);
                jsonObject.put("message", "请选择文件!");
                return jsonObject;
            }
            MultipartFile file = multiRequest.getFile(itr.next());
            if (!file.isEmpty()) {
                jsonObject = UploadUtil.uploadImg(CommonConst.getUploadPath(), file);
                // 下面response返回的json格式是editor.md所限制的，规范输出就OK
                jsonObject.put("error", 0);
                jsonObject.put("message", "上传成功");
                return jsonObject;
            }else{
                jsonObject.put("error", 1);
                jsonObject.put("message", "文件为空");
                return jsonObject;
            }
        } catch (Exception e){
            log.error("上传文件失败！", e);
            jsonObject.put("error", 1);
            jsonObject.put("message", e.getMessage());
            return jsonObject;
        }
    }

}
