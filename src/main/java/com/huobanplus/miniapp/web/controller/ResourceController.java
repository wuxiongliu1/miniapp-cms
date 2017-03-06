package com.huobanplus.miniapp.web.controller;

import com.huobanplus.miniapp.web.annotations.UserAuthenticationPrincipal;
import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.service.StaticResourceService;
import com.huobanplus.miniapp.web.util.StringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

/**
 * Created by wuxiongliu on 2017-02-20.
 * 资源上传，移除controller
 */
@Controller
@RequestMapping(value = "/res")
public class ResourceController {

    private static final Log log = LogFactory.getLog(ResourceController.class);

    @RequestMapping(value = "/toUpload")
    public String toUpload() {
        return "test/upload";
    }

    @Autowired
    private StaticResourceService resourceServer;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult upload2(@UserAuthenticationPrincipal(value = "user") User user, HttpServletRequest request) throws Exception {

        Date now = new Date();

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> items = upload.parseRequest(request);
        URI uri = null;
        String path = "";
        for (FileItem item : items) {

            String originalFilename = item.getName();
            String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            path = StaticResourceService.ARTICLE_IMG + StringUtil.DateFormat(now, "yyyyMMdd") + "/"
                    + StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + fileSuffix;
            resourceServer.uploadResource(path, item.getInputStream());
        }

        return ApiResult.resultWith(ResultCode.SUCCESS, path);
    }

    /**
     * 上传资源
     *
     * @return
     */
    @RequestMapping(value = "/uploadUseless", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult upload(@UserAuthenticationPrincipal(value = "user") User user, @RequestParam CommonsMultipartFile myfile, HttpServletRequest request) {

        String relativePathPrefix = generateFilePath(user.getUsername());
        String originalFilename = myfile.getOriginalFilename();
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = new Date().getTime() + RandomStringUtils.randomNumeric(6) + fileSuffix;
        String realPath = request.getSession().getServletContext().getRealPath(relativePathPrefix);
        String relativePath = relativePathPrefix + fileName;
        File targetFile = new File(realPath, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            myfile.transferTo(targetFile);
        } catch (Exception e) {
            log.info("uploadException: " + e.getMessage());
        }
        return ApiResult.resultWith(ResultCode.SUCCESS, relativePath);
    }

    /**
     * 移除资源
     *
     * @return
     */
    @RequestMapping(value = "/remove")
    @ResponseBody
    public ApiResult remove(String filePath, HttpServletRequest request) throws IOException {
        resourceServer.deleteResource(filePath);
        return ApiResult.resultWith(ResultCode.SUCCESS);
    }

    /**
     * 生成文件上传路径 /upload/{username}/image/{yyyyMMdd}/
     *
     * @param username
     * @return
     */
    private String generateFilePath(String username) {
        StringBuilder sb = new StringBuilder();
        sb.append("/upload/")
                .append(username)
                .append("/image/")
                .append(StringUtil.DateFormat(new Date(), "yyyyMMdd"))
                .append("/");
        return sb.toString();
    }

}
