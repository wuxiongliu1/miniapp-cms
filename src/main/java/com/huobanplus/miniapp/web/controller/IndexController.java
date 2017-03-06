package com.huobanplus.miniapp.web.controller;

import com.baidu.ueditor.ActionEnter;
import com.google.gson.Gson;
import com.huobanplus.miniapp.web.annotations.UserAuthenticationPrincipal;
import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.Constant;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import com.huobanplus.miniapp.web.model.UeditorUploadResp;
import com.huobanplus.miniapp.web.service.ArticleService;
import com.huobanplus.miniapp.web.service.StaticResourceService;
import com.huobanplus.miniapp.web.service.UserService;
import com.huobanplus.miniapp.web.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
@Controller
public class IndexController {
    private static final Log log = LogFactory.getLog(IndexController.class);


    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private StaticResourceService resourceServer;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String toLogin(HttpServletRequest request, Model model) {

        Object user = request.getSession().getAttribute("user");
        User attrUser = new User();
        boolean attrRemember = false;
        if (user != null) {
            Object rememberMe = request.getSession().getAttribute("remember");

            if (rememberMe != null) {
                boolean remember = (boolean) rememberMe;
                if (remember) {
                    attrUser = (User) user;
                    attrRemember = true;
                }
            }
        }

        model.addAttribute("user", attrUser);
        model.addAttribute("remember", attrRemember);

        return "login";
    }

    @RequestMapping(value = {"/", "index"})
    public String index(@UserAuthenticationPrincipal(value = "user") User user, Model model) {

        ArticleSearch articleSearch = new ArticleSearch();
        articleSearch.setUserId(user.getId());
        articleSearch.setEnabled(true);
        Page<Article> articlePage = articleService.findAll(articleSearch, 1, Constant.PAGE_SIZE, new Sort(Sort.Direction.DESC, "updateTime"));
        List<Article> articleList = articlePage.getContent();

        model.addAttribute("articleList", articleList);
        model.addAttribute("articleSearch", new ArticleSearch());
        model.addAttribute("hasPrev", articlePage.hasPrevious());
        model.addAttribute("hasNext", articlePage.hasNext());
        model.addAttribute("pageNum", articlePage.getTotalPages());
        model.addAttribute("pageIndex", 1);
        model.addAttribute("pageSize", Constant.PAGE_SIZE);

        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult login(HttpServletRequest request, String username, String password, Model model, boolean rememberUser) throws UnsupportedEncodingException {
        // 先用原密码查询，然后再用加密后的密码查询

        User user = userService.findUser(username, password);
        if (user == null) {
            User md5User = userService.findUser(username, DigestUtils.md5Hex(password.getBytes("utf-8")));
            if (md5User == null) {
                model.addAttribute("errorMsg", "用户名或密码错误");
                return ApiResult.resultWith(ResultCode.NO_USER, "用户名或密码错误", null);
            } else {
                user = md5User;
            }
        }

        request.getSession().setAttribute("remember", rememberUser);
        request.getSession().setAttribute("user", user);
        request.getSession().setMaxInactiveInterval(86400);// session过期时间24小时

        return ApiResult.resultWith(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, Model model) {
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("remember");
        return "redirect:/login";
    }

    @RequestMapping(value = "/ueditor")
    public String ueditor() {
        return "ueditordemo";
    }

    @RequestMapping(value = "/ueditor/upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException, URISyntaxException {

        request.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "text/html");
        Date now = new Date();

        if (request.getParameter("action").equals("config")) {
            WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
            ServletContext application = webApplicationContext.getServletContext();
            String rootPath = application.getRealPath("/");

            response.getWriter().write(new ActionEnter(request, rootPath).exec());
        } else {
            MultipartHttpServletRequest multipartReq = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartReq.getFile("upfile");
            InputStream fin = null;
            String fileName = file.getOriginalFilename();
            String originalFileName = fileName;
            String path = "";
            String newFileName = "";

            if (!StringUtil.isEmpty(fileName)) {
                fin = file.getInputStream();
                String fileSuffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                newFileName = StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + fileSuffix;
                path = StaticResourceService.ARTICLE_IMG + StringUtil.DateFormat(now, "yyyyMMdd") + "/"
                        + newFileName;

                URI uri = resourceServer.uploadResource(path, fin);

                UeditorUploadResp ueditorUploadResp = new UeditorUploadResp();
                ueditorUploadResp.setState("SUCCESS");
                ueditorUploadResp.setOriginal(originalFileName);
                ueditorUploadResp.setTitle(newFileName);
                ueditorUploadResp.setType("." + fileSuffix);
                ueditorUploadResp.setUrl(uri.toString());
                ueditorUploadResp.setSize(String.valueOf(file.getSize()));
                Gson gson = new Gson();

                response.getWriter().write(gson.toJson(ueditorUploadResp));
            }
        }
    }
}
