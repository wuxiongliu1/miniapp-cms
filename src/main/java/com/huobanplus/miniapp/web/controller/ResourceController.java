package com.huobanplus.miniapp.web.controller;

import com.huobanplus.miniapp.web.annotations.UserAuthenticationPrincipal;
import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.util.StringUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
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

    /**
     * 上传资源
     *
     * @return
     */
    @RequestMapping(value = "/upload")
    @ResponseBody
    public ApiResult upload(@UserAuthenticationPrincipal(value = "user") User user, @RequestParam CommonsMultipartFile[] file, HttpServletRequest request) {

        String relativePathPrefix = generateFilePath(user.getUsername());
        List<String> fileRelativePaths = new ArrayList<>();

        for (int i = 0; i < file.length; i++) {
            String originalFilename = file[i].getOriginalFilename();
            String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = new Date().getTime() + RandomStringUtils.randomNumeric(6) + fileSuffix;
            String realPath = request.getSession().getServletContext().getRealPath(relativePathPrefix);
            String relativePath = relativePathPrefix + fileName;
            fileRelativePaths.add(relativePath);
            File targetFile = new File(realPath, fileName);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            try {
                file[i].transferTo(targetFile);
            } catch (Exception e) {
                log.info("uploadException: " + e.getMessage());
            }
        }
        return ApiResult.resultWith(ResultCode.SUCCESS, fileRelativePaths);
    }

    /**
     * 移除资源
     *
     * @return
     */
    @RequestMapping(value = "/remove")
    @ResponseBody
    public ApiResult remove(String filePath, HttpServletRequest request) {
        String realPath = request.getSession().getServletContext().getRealPath(filePath);
        File file = new File(realPath);
        if (file.exists()) {
            file.delete();
            return ApiResult.resultWith(ResultCode.SUCCESS);
        } else {
            return ApiResult.resultWith(ResultCode.NO_FILE);
        }
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
