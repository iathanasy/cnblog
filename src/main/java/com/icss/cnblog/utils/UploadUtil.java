package com.icss.cnblog.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.icss.cnblog.consts.CommonConst;
import com.icss.cnblog.exception.GlobalException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @description: 上传工具类
 * @author: Mr.Wang
 * @create: 2020-02-25 14:18
 **/
public class UploadUtil {

    private static final String TYPE = "^image|flash|media|file$";

    private static final String DISALLOW_SUFFIX_REG = "\\.(asp|asa|jsp|php|(a-zA-z)?htm|swf|fl(a|v)|xml|js|css)";

    private static long MAX_SIZE = 99999999;


    /**
     * 上传图片
     * @param baseDir 上传路径
     * @param file 图片
     * @return 返回上传成功的文件名
     */
    public static final JSONObject uploadImg(String baseDir, MultipartFile file) throws IOException {
        //文件名
        String fileName = extractFilename(file.getOriginalFilename());
        if (fileName.indexOf('\u0000') != -1) {
            throw new GlobalException("上传文件名非法!");
        }
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        // 只过滤动态脚本,uploads目录应该设置为不解析的静态文件目录
        if (Pattern.compile(DISALLOW_SUFFIX_REG, Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(fileExt).find()) {
            throw new GlobalException("上传文件扩展名是不允许的扩展名!");
        }
        //创建文件
        File desc = getAbsoluteFile(baseDir, fileName);
        //写入数据
        file.transferTo(desc);
        //获取返回数据
        String pathFileName = getPathFileName(baseDir, fileName);

        JSONObject jsonObject=new JSONObject();
        // 只支持JPG、GIF、PNG、BMP、TIFF格式的图片
        ImageInfo info = ImageUtils.getImageInfo(desc);
        jsonObject.put("isImage", info.getWidth() > 0 || info.getHeight() > 0);
        jsonObject.put("width", info.getWidth());
        jsonObject.put("height", info.getHeight());
        jsonObject.put("filename", fileName);
        jsonObject.put("url", pathFileName);
        return jsonObject;
    }

    /**
     * 上传文件
     * @param baseDir 上传路径
     * @param file 文件
     * @return 返回上传成功的文件名
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException {
        //文件名
        String fileName = extractFilename(file.getOriginalFilename());
        if (fileName.indexOf('\u0000') != -1) {
            throw new GlobalException("上传文件名非法!");
        }
        //创建文件
        File desc = getAbsoluteFile(baseDir, fileName);
        //写入数据
        file.transferTo(desc);
        //获取返回数据
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;
    }

    /**
     * base64字符串转换成图片
     * @param baseDir		图片存放路径
     * @param base64	base64字符串
     * @param suffix 文件后缀
     * @return 返回上传成功的文件名
     */
    public static final String uploadBase64(String baseDir,String base64, String suffix) throws IOException {
        //去掉前面的“data:image/jpeg;base64,”的字样
        base64 = base64.replaceAll("data:image\\/.*;base64,","");

        // 对字节数组字符串进行Base64解码并生成图片
        BASE64Decoder decoder = new BASE64Decoder();
        // Base64解码
        byte[] b = decoder.decodeBuffer(base64);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {// 调整异常数据
                b[i] += 256;
            }
        }
        //名称
        String generateName = UUID.randomUUID().toString().replace("-","") + "." + suffix;

        //文件名
        String fileName = extractFilename(generateName);
        //创建文件
        File desc = getAbsoluteFile(baseDir, fileName);
        //写入数据
        FileUtil.writeBytes(b, desc);
        //获取返回数据
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;

    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(String fileName)
    {
        String extension = getExtension(fileName);
        fileName = DateUtil.format(new Date(),"yyyy/MM/dd") + "/" +
                encodingFilename(fileName) + "." + extension;
        return fileName;
    }

    /**
     * 编码文件名
     */
    private static final String encodingFilename(String fileName)
    {
        fileName = fileName.replace("_", " ");
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        return fileName;
    }

    /**
     * 获取文件名的后缀
     *
     * @param fileName 文件名
     * @return 后缀名
     */
    public static final String getExtension(String fileName)
    {
        String extension = FileUtil.extName(fileName);
        return extension;
    }

    /**
     * 创建文件
     * @param uploadDir
     * @param fileName
     * @return
     * @throws IOException
     */
    private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException
    {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists())
        {
            desc.createNewFile();
        }
        return desc;
    }

    /**
     * 获取返回文件名
     * @param uploadDir
     * @param fileName
     * @return
     * @throws IOException
     */
    private static final String getPathFileName(String uploadDir, String fileName) throws IOException
    {
        int dirLastIndex = CommonConst.profile.length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = CommonConst.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        return pathFileName;
    }


}
